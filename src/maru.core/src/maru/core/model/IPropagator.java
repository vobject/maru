package maru.core.model;

import java.io.Serializable;
import java.util.Collection;

import org.orekit.errors.OrekitException;
import org.orekit.time.AbsoluteDate;

public interface IPropagator extends ISpacecraftListener, Serializable
{
    String getName();

    ICoordinate getCoordinate(ISpacecraft element, AbsoluteDate date) throws OrekitException;
    Collection<ICoordinate> getCoordinates(ISpacecraft element,
                                           AbsoluteDate start,
                                           AbsoluteDate stop,
                                           long stepSize) throws OrekitException;

    void clearCoordinateCache();
}
