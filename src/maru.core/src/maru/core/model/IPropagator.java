package maru.core.model;

import java.io.Serializable;
import java.util.Collection;

public interface IPropagator extends ISpacecraftListener, Serializable
{
    String getName();

    ICoordinate getCoordinate(ISpacecraft element, long time);
    Collection<ICoordinate> getCoordinates(ISpacecraft element, long start, long stop, long stepSize);

    void clearCoordinateCache();
}
