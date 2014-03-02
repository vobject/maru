package maru.core.model.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import maru.core.model.ICentralBody;
import maru.core.model.ICoordinate;
import maru.core.model.IGroundstation;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.BracketingNthOrderBrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.TooManyEvaluationsException;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.errors.OrekitException;
import org.orekit.errors.OrekitExceptionWrapper;
import org.orekit.frames.TopocentricFrame;
import org.orekit.frames.Transform;

public final class VisibilityUtils
{
    public static boolean hasAccessTo(ICoordinate src, ICoordinate dest) throws OrekitException
    {
        if (src.equals(dest)) {
            return true;
        }

        ICentralBody centralBody = src.getCentralBody();

        GeodeticPoint srcIntersect = centralBody.getIntersection(src, dest);
        if (srcIntersect == null)
        {
            // there is no intersection with the central body in the direction
            // of the other spacecraft, so there is visibility.
            return true;
        }

        // we have an intersection with the central body in the direction
        // of the other spacecraft. check if the distance to the intersection
        // is smaller than the distance to the other spacecraft. if it is,
        // we have visibility.

        // get the intersection point of the src coordinate with the central
        // body as a cartesian coordinate
        Vector3D cbIntersectVec = CentralBodyUtils.getCartesianPoint(centralBody, srcIntersect);

        // transform the intersection point into the src coordinate frame
        Transform cbToSrcFrame = centralBody.getFrame().getTransformTo(src.getFrame(), src.getDate());
        Vector3D cbIntersectVecInSrcFrame = cbToSrcFrame.transformPosition(cbIntersectVec);

        // check if the distance to the intersection with the central body is
        // smaller than the distance to the dest coordinate
        double distanceToIntersect = src.getPosition().distance(cbIntersectVecInSrcFrame);
        double distanceToDest = getDistanceTo(src, dest);
        return (distanceToDest < distanceToIntersect) ? true : false;
    }

    public static boolean hasAccessTo(ICoordinate sc, IGroundstation gs) throws OrekitException
    {
        double elevation = gs.getFrame().getElevation(sc.getPosition(),
                                                      sc.getFrame(),
                                                      sc.getDate());
        return (elevation - gs.getElevationAngle()) > 0.0;
    }

    public static double getDistanceTo(ICoordinate src, ICoordinate dest) throws OrekitException
    {
        if (src.equals(dest)) {
            return 0.0;
        }

        // transform the dest position vector into the src frame and return
        // the distance between the two points.
        return src.getPosition().distance(dest.getFrame().getTransformTo(src.getFrame(), src.getDate()).transformPosition(dest.getPosition()));
    }

    public static double getDistanceTo(ICoordinate sc, IGroundstation gs) throws OrekitException
    {
        return gs.getFrame().getRange(sc.getPosition(), sc.getFrame(), sc.getDate());
    }

    public static List<GeodeticPoint> getVisibilityCircle(ICoordinate sc, int points) throws OrekitException
    {
        // based on orekit 6.1 VisibilityCircle example.

        // calculate the visibility circle around a spacecraft by abusing
        // TopocentricFrame

        ICentralBody centralBody = sc.getCentralBody();

        // the coordinate of the nearest intersection with the central body.
        // this is the same as the current ground track point.
        GeodeticPoint intersect = centralBody.getIntersection(sc);

        // define the frame of the intersection point on the central body.
        // it will be used to calculate the visibility of a specific azimuth
        // for the spacecraft's altitude.
        TopocentricFrame frame = new TopocentricFrame(centralBody.getBodyShape(),
                                                      intersect,
                                                      "tmp");

        // we want to calculate the spacecraft's distance from the middle of
        // the central body. the middle of the central body is the ZERO vector
        // in its frame. calculate what the ZERO vector of the central body is
        // in the spacecraft's frame (-> it is usually ZERO for most frames).
        Transform transformToScFrame = centralBody.getFrame().getTransformTo(sc.getFrame(), sc.getDate());
        Vector3D zeroVecInScFrame = transformToScFrame.transformPosition(Vector3D.ZERO);

        // the spacecraft's altitude in meters from the middle of the central body.
        double altitude = sc.getPosition().distance(zeroVecInScFrame);

        try
        {
            List<GeodeticPoint> circle = new ArrayList<>(points);
            for (int i = 0; i < points; i++)
            {
                double azimuth = i * (2.0 * Math.PI / points);
                circle.add(computeLimitVisibilityPoint(centralBody, frame, altitude, azimuth, 0.0));
            }
            return circle;
        }
        catch (NoBracketingException nbe)
        {
            // probably a collision between central body and scenario element
            // this is usually not a critical error.
            nbe.printStackTrace();
            return Collections.emptyList();
        }
    }

    public static List<GeodeticPoint> getVisibilityCircle(IGroundstation gs, ICoordinate sc, int points) throws OrekitException
    {
        // based on orekit 6.1 VisibilityCircle example.

        ICentralBody centralBody = gs.getCentralBody();
        TopocentricFrame frame = gs.getFrame();
        double elevationAngle = gs.getElevationAngle();

        // we want to calculate the spacecraft's distance from the middle of
        // the central body. the middle of the central body is the ZERO vector
        // in its frame. calculate what the ZERO vector of the central body is
        // in the spacecraft's frame (-> it is usually ZERO for most frames).
        Transform transformToScFrame = centralBody.getFrame().getTransformTo(sc.getFrame(), sc.getDate());
        Vector3D zeroVecInScFrame = transformToScFrame.transformPosition(Vector3D.ZERO);

        // the spacecraft's altitude in meters from the middle of the central body.
        double altitude = sc.getPosition().distance(zeroVecInScFrame);

        try
        {
            List<GeodeticPoint> circle = new ArrayList<>(points);
            for (int i = 0; i < points; i++)
            {
                double azimuth = i * (2.0 * Math.PI / points);
                circle.add(computeLimitVisibilityPoint(centralBody, frame, altitude, azimuth, elevationAngle));
            }
            return circle;
        }
        catch (NoBracketingException nbe)
        {
            // probably a collision between central body and scenario element
            // this is usually not a critical error.
            nbe.printStackTrace();
            return Collections.emptyList();
        }
    }

    private static GeodeticPoint computeLimitVisibilityPoint(final ICentralBody centralBody,
                                                             final TopocentricFrame frame,
                                                             final double radius,
                                                             final double azimuth,
                                                             final double elevation) throws OrekitException
    {
        // based on orekit 6.1 TopocentricFrame.computeLimitVisibilityPoint()
        // but with variable equatorial radius of the central body.

        try {
            // convergence threshold on point position: 1mm
            final double deltaP = 0.001;
            final UnivariateSolver solver =
                    new BracketingNthOrderBrentSolver(deltaP / centralBody.getEquatorialRadius(),
                                                      deltaP, deltaP, 5);

            // find the distance such that a point in the specified direction and at the solved-for
            // distance is exactly at the specified radius
            final double distance = solver.solve(1000, new UnivariateFunction() {
                /** {@inheritDoc} */
                @Override
                public double value(final double x) {
                    try {
                        final GeodeticPoint gp = frame.pointAtDistance(azimuth, elevation, x);
                        return frame.getParentShape().transform(gp).getNorm() - radius;
                    } catch (OrekitException oe) {
                        throw new OrekitExceptionWrapper(oe);
                    }
                }
            }, 0, 2 * radius);

            // return the limit point
            return frame.pointAtDistance(azimuth, elevation, distance);

        } catch (TooManyEvaluationsException tmee) {
            throw new OrekitException(tmee);
        } catch (OrekitExceptionWrapper lwe) {
            throw lwe.getException();
        }
    }
}
