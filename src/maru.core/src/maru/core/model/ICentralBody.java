package maru.core.model;

import maru.IMaruResource;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.errors.OrekitException;
import org.orekit.frames.Frame;
import org.orekit.time.AbsoluteDate;

public interface ICentralBody extends IScenarioElement
{
    IMaruResource getTexture();

    /** return the standard gravitational parameter of the body */
    double getGM();

    /** return the equatorial radius of the body */
    double getEquatorialRadius();

    /** return the flattening of the body */
    double getFlattening();

    /** return the body oriented, body centered frame. */
    Frame getFrame();

    Vector3D getCartesianPoint(GeodeticPoint point);

    /** get the position of the central body in a given frame */
    Vector3D getPosition(Frame frame, AbsoluteDate date) throws OrekitException;

    GeodeticPoint getIntersection(ICoordinate from) throws OrekitException;
    GeodeticPoint getIntersection(ICoordinate from, ICoordinate to) throws OrekitException;
    GeodeticPoint getIntersection(ICoordinate from, Vector3D to) throws OrekitException;

    double getDistanceToHorizon(ICoordinate coordinate) throws OrekitException;
//    double getDistanceToIntersection(ICoordinate coordinate, ICoordinate to);
//    double getDistanceToIntersection(ICoordinate coordinate, Vector3D to);
}
