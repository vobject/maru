package maru.core.internal.model;

import java.util.Map;

import maru.core.model.AbstractScenarioElement;
import maru.core.model.IScenarioElement;
import maru.core.model.ITimepoint;
import maru.core.utils.TimeUtils;

import org.orekit.time.AbsoluteDate;

public class Timepoint extends AbstractScenarioElement implements ITimepoint
{
    private static final long serialVersionUID = 1L;

    /** Timestamp date. */
    private AbsoluteDate date;

    public Timepoint(String name, AbsoluteDate date)
    {
        super(name);
        this.date = TimeUtils.copy(date);
    }

    public Timepoint(AbsoluteDate date)
    {
        this("Timepoint", date);
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
            return getTime().compareTo(otherTimepoint.getTime());
        }
        return super.compareTo(other);
    }

    @Override
    public AbsoluteDate getTime()
    {
        return date;
    }

    public void setTime(AbsoluteDate date)
    {
        this.date = TimeUtils.copy(date);
    }

    @Override
    public Map<String, String> getPropertyMap()
    {
        Map<String, String> properties = super.getPropertyMap();

        properties.put("Time", getTime().toString());

        return properties;
    }
}
