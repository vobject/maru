package maru.core.model;

import java.io.Serializable;

public class VisibleElementColor implements Serializable
{
    private static final long serialVersionUID = 1L;

    public int r;
    public int g;
    public int b;

    public VisibleElementColor(int r, int g, int b)
    {
        this.r = r;
        this.g = g;
        this.b = b;
    }
}
