package maru.core.model;

public interface ITimeProvider
{
    void addTimeListener(ITimeListener listener);
    void removeTimeListener(ITimeListener listener);
}
