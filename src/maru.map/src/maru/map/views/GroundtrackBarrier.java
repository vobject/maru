package maru.map.views;

public class GroundtrackBarrier
{
    private long start;
    private long stop;
    private long gtLength;

    public GroundtrackBarrier(long current, long groundtrackLength)
    {
        this.start = current - (groundtrackLength / 2);
        this.stop = current + (groundtrackLength / 2);
    }

    public long getStart()
    {
        return start;
    }

    public void setStart(long start)
    {
        this.start = start;
    }

    public long getStop()
    {
        return stop;
    }

    public void setStop(long stop)
    {
        this.stop = stop;
    }

    public void update(long current, long groundtrackLength)
    {
        if ((start > current) || (stop < current) || (gtLength != groundtrackLength))
        {
            start = current - (groundtrackLength / 2);
            stop = current + (groundtrackLength / 2);
            gtLength = groundtrackLength;
        }
    }
}
