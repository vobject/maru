package maru.core.utils;

import maru.core.model.ICentralBody;
import maru.core.model.ICoordinate;

import org.apache.commons.math3.geometry.euclidean.threed.Line;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.errors.OrekitException;
import org.orekit.frames.Transform;

public final class CentralBodyUtils
{
    public static GeodeticPoint getIntersection(ICentralBody centralBody, ICoordinate src) throws OrekitException
    {
        Transform transform = centralBody.getFrame().getTransformTo(src.getFrame(), src.getDate());
        Vector3D zeroVecInSrcFrame = transform.transformPosition(Vector3D.ZERO);

        return getIntersection(centralBody, src, zeroVecInSrcFrame);
    }

    public static GeodeticPoint getIntersection(ICentralBody centralBody, ICoordinate src, ICoordinate dest) throws OrekitException
    {
        Transform transform = dest.getFrame().getTransformTo(src.getFrame(), src.getDate());
        Vector3D destVecInSrcFrame = transform.transformPosition(dest.getPosition());

        return getIntersection(centralBody, src, destVecInSrcFrame);
    }

    public static GeodeticPoint getIntersection(ICentralBody centralBody, ICoordinate src, Vector3D dest) throws OrekitException
    {
        // the dest vector must already be in the same frame as the src coordinate

        return centralBody.getBodyShape().getIntersectionPoint(
            new Line(src.getPosition(), dest),
            src.getPosition(),
            src.getFrame(),
            src.getDate()
        );
    }

    /** return the geodetic point of the central body as a cartesian coordinate. */
    public static Vector3D getCartesianPoint(ICentralBody centralBody, GeodeticPoint point)
    {
        return centralBody.getBodyShape().transform(point);
    }

    public static double getDistanceToHorizon(ICentralBody centralBody, ICoordinate coordinate) throws OrekitException
    {
        double radius = centralBody.getEquatorialRadius();
        double radius2 = radius * 2;

        Transform transform = centralBody.getFrame().getTransformTo(coordinate.getFrame(), coordinate.getDate());
        Vector3D zeroVecInCoordinateFrame = transform.transformPosition(Vector3D.ZERO);

        // calculate the distance from the coordinate to the zero vector.
        // it is the altitude from the central bodies center.

        double coodinateAltitude = coordinate.getPosition().distance(zeroVecInCoordinateFrame);
        double coodinateAltitudeAboveGround = coodinateAltitude - radius;

        return Math.sqrt(coodinateAltitudeAboveGround * (radius2 + coodinateAltitudeAboveGround));
    }
}
