package maru.map.views;

import org.orekit.time.AbsoluteDate;

public class GroundtrackBarrier
{
    private AbsoluteDate start;
    private AbsoluteDate stop;
    private long gtLength;

    public GroundtrackBarrier(AbsoluteDate current, long groundtrackLength)
    {
        this.start = current.shiftedBy(-(groundtrackLength / 2));
        this.stop = current.shiftedBy((groundtrackLength / 2));
    }

    public AbsoluteDate getStart()
    {
        return start;
    }

    public void setStart(AbsoluteDate start)
    {
        this.start = start;
    }

    public AbsoluteDate getStop()
    {
        return stop;
    }

    public void setStop(AbsoluteDate stop)
    {
        this.stop = stop;
    }

    public void update(AbsoluteDate current, long groundtrackLength)
    {
        //if ((start > current) || (stop < current) || (gtLength != groundtrackLength))
        if ((start.compareTo(current) > 0) || (stop.compareTo(current) < 0) || (gtLength != groundtrackLength))
        {
            start = current.shiftedBy(-(groundtrackLength / 2));
            stop = current.shiftedBy((groundtrackLength / 2));
            gtLength = groundtrackLength;
        }
    }
}
