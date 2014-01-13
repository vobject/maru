package maru.core.model;

import maru.IMaruResource;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.errors.OrekitException;
import org.orekit.frames.Frame;

public interface ICentralBody extends IScenarioElement
{
    IMaruResource getTexture();

    /** return the standard gravitational parameter of the body */
    double getGM();

    /** return the equatorial radius of the body */
    double getEquatorialRadius();

    /** return the flattening of the body */
    double getFlattening();

    /** return the inertially oriented, body centered frame. */
    Frame getFrame();

    /** get the position of the central body in a given frame */
    Vector3D getPosition(Frame frame, long time) throws OrekitException;

    GeodeticPoint toGeodeticPoint(ICoordinate coordinate) throws OrekitException;
    GeodeticPoint toGeodeticPoint(Vector3D position, Frame frame, long time) throws OrekitException;
    Vector3D toCartesianPoint(GeodeticPoint point);
}
