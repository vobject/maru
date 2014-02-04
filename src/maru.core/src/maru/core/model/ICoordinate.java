package maru.core.model;

import java.io.Serializable;

import maru.core.utils.EclipseState;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.orekit.frames.Frame;
import org.orekit.time.AbsoluteDate;

public interface ICoordinate extends Serializable
{
    ICentralBody getCentralBody();

    /** Get the position vector of the coordinate. */
    Vector3D getPosition();

    /** Get the velocity vector of the coordinate. */
    Vector3D getVelocity();

    /** Get the frame of the coordinate. */
    Frame getFrame();

    AbsoluteDate getDate();

    EclipseState getEclipseState();
}
