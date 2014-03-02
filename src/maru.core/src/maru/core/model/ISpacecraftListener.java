package maru.core.model;

import org.orekit.time.AbsoluteDate;

public interface ISpacecraftListener
{
    void startTimeChanged(ISpacecraft element, AbsoluteDate date);
    void stopTimeChanged(ISpacecraft element, AbsoluteDate date);
    void currentTimeChanged(ISpacecraft element, AbsoluteDate date);
}
