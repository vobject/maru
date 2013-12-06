package maru.map.jobs.swt;

import maru.map.jobs.DrawJob;

import org.eclipse.swt.graphics.GC;

public abstract class SWTDrawJob extends DrawJob
{
    private GC gc;

    public GC getGC()
    {
        return gc;
    }

    public void setGC(GC gc)
    {
        this.gc = gc;
    }
}
