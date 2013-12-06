package maru.core.units;

import java.io.Serializable;

public class Velocity implements Serializable
{
    private static final long serialVersionUID = 1L;

    private final double x;
    private final double y;
    private final double z;

    public Velocity(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Velocity(Velocity other)
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
