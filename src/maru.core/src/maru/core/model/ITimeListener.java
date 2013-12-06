package maru.core.model;

public interface ITimeListener extends IScenarioElement
{
    void startTimeChanged(long time);
    void stopTimeChanged(long time);
    void currentTimeChanged(long time);
}
