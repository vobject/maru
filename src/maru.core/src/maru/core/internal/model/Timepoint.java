package maru.core.internal.model;

import java.util.Map;

import maru.core.model.IScenarioElement;
import maru.core.model.ITimepoint;
import maru.core.model.template.ScenarioElement;
import maru.core.utils.TimeUtils;

public class Timepoint extends ScenarioElement implements ITimepoint
{
    private static final long serialVersionUID = 1L;

    /** Timestamp in seconds. */
    private long time;

    public Timepoint(String name, long time)
    {
        super(name);
        this.time = time;
    }

    public Timepoint(long time)
    {
        this("Timepoint", time);
    }

    public Timepoint(ITimepoint timepoint)
    {
        this(timepoint.getElementName(), timepoint.getTime());
        setParent(timepoint.getParent());
    }

    @Override
    public int compareTo(IScenarioElement other)
    {
        if (other instanceof ITimepoint) {
            ITimepoint otherTimepoint = (ITimepoint) other;
            return Long.compare(getTime(), otherTimepoint.getTime());
        }
        return super.compareTo(other);
    }

    @Override
    public long getTime()
    {
        return time;
    }

    public void setTime(long time)
    {
        this.time = time;
    }

    @Override
    public Map<String, String> getPropertyMap()
    {
        Map<String, String> properties = super.getPropertyMap();

        properties.put("Time", TimeUtils.asISO8601(getTime()));

        return properties;
    }
}
