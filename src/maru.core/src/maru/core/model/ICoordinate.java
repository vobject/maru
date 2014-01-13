package maru.core.model;

import java.io.Serializable;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.orekit.frames.Frame;

public interface ICoordinate extends Serializable
{
    /** Get the position vector of the coordinate. */
    Vector3D getPosition();

    /** Get the velocity vector of the coordinate. */
    Vector3D getVelocity();

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
