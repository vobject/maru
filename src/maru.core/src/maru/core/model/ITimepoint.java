package maru.core.model;

public interface ITimepoint extends IScenarioElement
{
    /**
     * Get the number of <b>seconds</b> passed since the standard base time
     * known as "the epoch", namely January 1, 1970, 00:00:00 GMT.
     *
     * @see java.lang.System#currentTimeMillis()
     */
    public long getTime();
}
