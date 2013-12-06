package maru.core.model;

import java.io.Serializable;

import maru.core.units.Frame;
import maru.core.units.Position;
import maru.core.units.Velocity;

public interface ICoordinate extends Serializable
{
    /** Get the position vector of the coordinate. */
    Position getPosition();

    /** Get the velocity vector of the coordinate. */
    Velocity getVelocity();

    /**
     * Get the number of seconds passed since the standard base time
     * known as "the epoch", namely January 1, 1970, 00:00:00 GMT.
     *
     * @see maru.core.model.ITimepoint#getTime()
     */
    long getTime();

    /** Get the frame of the coordinate. */
    Frame getFrame();
}
