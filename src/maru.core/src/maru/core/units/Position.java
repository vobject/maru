package maru.core.units;

import java.io.Serializable;

public class Position implements Serializable
{
    private static final long serialVersionUID = 1L;

    private final double x;
    private final double y;
    private final double z;

    public Position(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Position(Position other)
    {
        this(other.x, other.y, other.z);
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    public double getZ()
    {
        return z;
    }
}
