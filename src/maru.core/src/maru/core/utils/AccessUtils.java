package maru.core.utils;

import maru.core.model.ICentralBody;
import maru.core.model.ICoordinate;
import maru.core.model.IGroundstation;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.errors.OrekitException;
import org.orekit.frames.Frame;
import org.orekit.frames.Transform;
import org.orekit.time.AbsoluteDate;

public final class AccessUtils
{
    public static boolean hasAccessTo(ICentralBody centralBody, ICoordinate from, ICoordinate to) throws OrekitException
    {
        return getDistanceTo(centralBody, from, to) > 0;
    }

    public static boolean hasAccessTo(ICentralBody centralBody, ICoordinate from, IGroundstation to) throws OrekitException
    {
        return getDistanceTo(centralBody, from, to) > 0;
    }

    public static double getDistanceTo(ICentralBody centralBody, ICoordinate from, ICoordinate to) throws OrekitException
    {
        Frame centralBodyFrame = centralBody.getFrame();

        ICoordinate myCoordinate = from;
        Frame myFrame = myCoordinate.getFrame();
        AbsoluteDate myDate = myCoordinate.getDate();
        Vector3D myVec = myCoordinate.getPosition();

        ICoordinate otherCoordinate = to;
        Frame otherFrame = otherCoordinate.getFrame();
        Vector3D otherVec = otherCoordinate.getPosition();

        Transform toMyFrame = otherFrame.getTransformTo(myFrame, myDate);
        Vector3D otherVecInMyFrame = toMyFrame.transformPosition(otherVec);
        double distanceToOther = myVec.distance(otherVecInMyFrame);

        GeodeticPoint myIntersect = centralBody.getIntersection(myCoordinate, otherVecInMyFrame);
        if (myIntersect == null)
        {
            // there is no intersection with the central body in
            // the direction of the other spacecraft.
            return distanceToOther;
        }
        else
        {
            // we have an intersection with the central body in the
            // direction of the other spacecraft. check if the distance
            // to the intersection is smaller than the distance to the
            // other spacecraft.
            Vector3D cbIntersectVec = centralBody.getCartesianPoint(myIntersect);

            Transform cbToMyFrame = centralBodyFrame.getTransformTo(myFrame, myDate);
            Vector3D cbIntersectVecInMyFrame = cbToMyFrame.transformPosition(cbIntersectVec);

            double distanceToIntersect = myVec.distance(cbIntersectVecInMyFrame);
            return (distanceToOther < distanceToIntersect) ? distanceToOther : -1.0;
        }
    }

    public static double getDistanceTo(ICentralBody centralBody, ICoordinate from, IGroundstation to) throws OrekitException
    {
        // TODO: consider the ground station's altitude
        // TODO: consider the ground station's elevation angle

        Frame centralBodyFrame = centralBody.getFrame();

        ICoordinate myCoordinate = from;
        Frame myFrame = myCoordinate.getFrame();
        AbsoluteDate myDate = myCoordinate.getDate();
        Vector3D myVec = myCoordinate.getPosition();

        Transform toMyFrame = centralBodyFrame.getTransformTo(myFrame, myDate);
        Vector3D gsVec = to.getCartesianPosition();
        Vector3D gsVecInMyFrame = toMyFrame.transformPosition(gsVec);

        double distanceToHorizon = centralBody.getDistanceToHorizon(myCoordinate);
        double distanceToGs = myVec.distance(gsVecInMyFrame);

        return (distanceToGs < distanceToHorizon) ? distanceToGs : -1.0;
    }
}
