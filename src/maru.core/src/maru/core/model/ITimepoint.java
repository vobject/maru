package maru.core.model;

import org.orekit.time.AbsoluteDate;

public interface ITimepoint extends IScenarioElement
{
    public AbsoluteDate getTime();
}
