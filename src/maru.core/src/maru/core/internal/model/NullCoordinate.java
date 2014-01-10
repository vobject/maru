package maru.core.internal.model;

import maru.core.model.ICoordinate;
import maru.core.units.Frame;
import maru.core.units.Position;
import maru.core.units.Velocity;

public class NullCoordinate implements ICoordinate
{
    private static final long serialVersionUID = 1L;

    private static final Position POSITION = new Position(0.0, 0.0, 0.0);
    private static final Velocity VELOCITY = new Velocity(0.0, 0.0, 0.0);

    @Override
    public Position getPosition()
    {
        return POSITION;
    }

    @Override
    public Velocity getVelocity()
    {
        return VELOCITY;
    }

    @Override
    public long getTime()
    {
        return 0;
    }

    @Override
    public Frame getFrame()
    {
        return Frame.NULL;
    }
}
