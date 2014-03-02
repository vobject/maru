package maru.core.model;

import org.orekit.time.AbsoluteDate;

public interface ITimeListener extends IScenarioElement
{
    void startTimeChanged(AbsoluteDate time);
    void stopTimeChanged(AbsoluteDate time);
    void currentTimeChanged(AbsoluteDate time);
}
