package maru.core.model;

public interface ISpacecraftListener
{
    void startTimeChanged(ISpacecraft element, long time);
    void stopTimeChanged(ISpacecraft element, long time);
    void currentTimeChanged(ISpacecraft element, long time);
}
