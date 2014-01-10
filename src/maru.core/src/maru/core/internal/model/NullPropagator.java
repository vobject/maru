package maru.core.internal.model;

import java.util.Collection;
import java.util.Collections;

import maru.core.model.ICoordinate;
import maru.core.model.IPropagatable;
import maru.core.model.IPropagator;

public class NullPropagator implements IPropagator
{
    private static final long serialVersionUID = 1L;

    private static final ICoordinate COORDINATE = new NullCoordinate();

    @Override
    public String getName()
    {
        return "NullPropagator";
    }

    @Override
    public ICoordinate getCoordinate(IPropagatable element, long time)
    {
        return COORDINATE;
    }

    @Override
    public Collection<ICoordinate> getCoordinates(IPropagatable element,
                                                  long start, long stop,
                                                  long stepSize)
    {
        return Collections.emptyList();
    }

    @Override
    public void startTimeChanged(IPropagatable element, long time)
    {

    }

    @Override
    public void stopTimeChanged(IPropagatable element, long time)
    {

    }

    @Override
    public void currentTimeChanged(IPropagatable element, long time)
    {

    }

    @Override
    public void clearCoordinateCache()
    {

    }
}
