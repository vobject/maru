package maru.spacecraft;

import maru.core.model.ICoordinate;
import maru.core.utils.OrekitUtils;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.orekit.frames.Frame;
import org.orekit.time.AbsoluteDate;
import org.orekit.utils.PVCoordinates;

public class OrekitCoordinate implements ICoordinate
{
    private static final long serialVersionUID = 1L;

    private final PVCoordinates pvCoordinates;
    private final AbsoluteDate absoluteDate;

    private final long time;
    private final Frame frame;

    public OrekitCoordinate(PVCoordinates coordinates,
                            AbsoluteDate date,
                            Frame frame)
    {
        this.pvCoordinates = coordinates;
        this.absoluteDate = date;

        this.time = OrekitUtils.toSeconds(absoluteDate);
        this.frame = frame;
    }

    @Override
    public Vector3D getPosition()
    {
        return pvCoordinates.getPosition();
    }

    @Override
    public Vector3D getVelocity()
    {
        return pvCoordinates.getVelocity();
    }

    @Override
    public long getTime()
    {
        return time;
    }

    @Override
    public Frame getFrame()
    {
        return frame;
    }

    public PVCoordinates getPvCoordinates()
    {
        return pvCoordinates;
    }

    public AbsoluteDate getAbsoluteDate()
    {
        return absoluteDate;
    }
}
