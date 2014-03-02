package maru.core.model;

import java.io.Serializable;
import java.util.List;

import org.orekit.time.AbsoluteDate;

public interface IPropagatorCache extends Serializable
{
    List<ICoordinate> getCoordinates(AbsoluteDate start, AbsoluteDate stop,
                                     long stepSize, ICoordinate initialCoordinate);
    void setCoordinates(AbsoluteDate start, AbsoluteDate stop,
                        long stepSize, ICoordinate initialCoordinate,
                        List<ICoordinate> coordinates);
    void clearCoordinates();
}
