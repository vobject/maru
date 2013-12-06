package maru.core.model;

public interface IPropagatableTimeListener
{
    void startTimeChanged(IPropagatable element, long time);
    void stopTimeChanged(IPropagatable element, long time);
    void currentTimeChanged(IPropagatable element, long time);
}
