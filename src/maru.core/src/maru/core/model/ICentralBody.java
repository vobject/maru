package maru.core.model;

import maru.IMaruResource;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.orekit.bodies.BodyShape;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.errors.OrekitException;
import org.orekit.frames.Frame;
import org.orekit.time.AbsoluteDate;

public interface ICentralBody extends IScenarioElement
{
    IMaruResource getTexture();

    /** return the body shape of the central body. */
    BodyShape getBodyShape();

    /** return the body oriented, body centered frame. */
    Frame getFrame();

    /** get the position of the central body in a given frame */
    Vector3D getPosition(Frame frame, AbsoluteDate date) throws OrekitException;

    /** return the standard gravitational parameter of the body */
    double getGM();

    /** return the equatorial radius of the body */
    double getEquatorialRadius();

    /** return the flattening of the body */
    double getFlattening();

    GeodeticPoint getIntersection(ICoordinate src) throws OrekitException;
    GeodeticPoint getIntersection(ICoordinate src, ICoordinate dest) throws OrekitException;
}
