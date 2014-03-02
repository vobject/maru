package maru.core.model;

import java.io.Serializable;
import java.util.List;

import org.orekit.errors.OrekitException;
import org.orekit.time.AbsoluteDate;

public interface IPropagator extends ISpacecraftListener, Serializable
{
    String getName();
    IPropagatorCache getCache();

    ICoordinate getCoordinate(AbsoluteDate date, ISpacecraft element) throws OrekitException;
    List<ICoordinate> getCoordinates(AbsoluteDate start, AbsoluteDate stop,
                                     long stepSize, ISpacecraft element) throws OrekitException;
}
