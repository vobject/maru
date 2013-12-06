package maru.core.model;

import java.io.Serializable;
import java.util.Collection;

public interface IPropagator extends IPropagatableTimeListener, Serializable
{
    String getName();

    ICoordinate getCoordinate(IPropagatable element, long time);
    Collection<ICoordinate> getCoordinates(IPropagatable element, long start, long stop, long stepSize);
}
