package maru.core.model;

import maru.IMaruResource;
import maru.core.units.Frame;
import maru.core.units.Position;

public interface ICentralBody extends IScenarioElement
{
    IMaruResource getTexture();

    /** return the standard gravitational parameter of the body */
    double getGM();

    /** return the equatorial radius of the body */
    double getEquatorialRadius();

    /** return the flattening of the body */
    double getFlattening();

    /** return the frame of the body */
    Frame getFrame();

    /** get the position of the central body in a given frame */
    Position getPosition(long time, Frame frame);
}
