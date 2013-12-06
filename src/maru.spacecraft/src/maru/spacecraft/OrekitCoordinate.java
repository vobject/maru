package maru.spacecraft;

import maru.core.model.ICoordinate;
import maru.core.units.Frame;
import maru.core.units.Position;
import maru.core.units.Velocity;

import org.orekit.OrekitUtils;
import org.orekit.time.AbsoluteDate;
import org.orekit.utils.PVCoordinates;

public class OrekitCoordinate implements ICoordinate
{
    private static final long serialVersionUID = 1L;

    private final PVCoordinates pvCoordinates;
    private final AbsoluteDate absoluteDate;

    private final Position position;
    private final Velocity velocity;
    private final long time;
    private final Frame frame;

    public OrekitCoordinate(PVCoordinates coordinates,
                            AbsoluteDate date,
                            org.orekit.frames.Frame frame)
    {
        this.pvCoordinates = coordinates;
        this.absoluteDate = date;

        this.position = OrekitUtils.toPosition(pvCoordinates.getPosition());
        this.velocity = OrekitUtils.toVelocity(pvCoordinates.getVelocity());
        this.time = OrekitUtils.toSeconds(absoluteDate);
        this.frame = OrekitUtils.toFrame(frame);
    }

    @Override
    public Position getPosition()
    {
        return position;
    }

    @Override
    public Velocity getVelocity()
    {
        return velocity;
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
