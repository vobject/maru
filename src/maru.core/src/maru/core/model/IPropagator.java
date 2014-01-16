package maru.core.model;

import java.io.Serializable;
import java.util.Collection;

import org.orekit.time.AbsoluteDate;

public interface IPropagator extends ISpacecraftListener, Serializable
{
    String getName();

    ICoordinate getCoordinate(ISpacecraft element, AbsoluteDate date);
    Collection<ICoordinate> getCoordinates(ISpacecraft element,
                                           AbsoluteDate start,
                                           AbsoluteDate stop,
                                           long stepSize);

    void clearCoordinateCache();
}
