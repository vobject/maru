package maru.centralbody.projection;

public class EquirectangularCoordinate
{
    public final int X;
    public final int Y;

    public EquirectangularCoordinate(int x, int y)
    {
        this.X = x;
        this.Y = y;
    }

    @Override
    public String toString()
    {
        return "(" + X + "," + Y + ")";
    }
}
