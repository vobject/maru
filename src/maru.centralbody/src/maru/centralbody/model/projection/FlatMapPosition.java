package maru.centralbody.model.projection;

public class FlatMapPosition
{
    public final int X;
    public final int Y;

    public FlatMapPosition(int x, int y)
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
