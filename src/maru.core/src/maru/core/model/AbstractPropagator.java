package maru.core.model;

import java.util.ArrayList;
import java.util.List;

import maru.core.model.internal.PropagatorCache;

import org.orekit.errors.OrekitException;
import org.orekit.time.AbsoluteDate;

public abstract class AbstractPropagator implements IPropagator
{
    private static final long serialVersionUID = 1L;

    /** Caches propagation results. */
    private final IPropagatorCache propagatorCache;

    /**
     * Listeners that are notified when a new coordinate was calculated.
     *
     * A listener is usually the Spacecraft that a Propagator is associated with.
     */
    private final List<IPropagationListener> propagationListeners;

    public AbstractPropagator()
    {
        propagatorCache = new PropagatorCache();
        propagationListeners = new ArrayList<>();
    }

    public void addPropagationListener(IPropagationListener listener)
    {
        if (!propagationListeners.contains(listener)) {
            propagationListeners.add(listener);
        }
    }

    public void removePropagationListener(IPropagationListener listener)
    {
        if (propagationListeners.contains(listener)) {
            propagationListeners.remove(listener);
        }
    }

    @Override
    public IPropagatorCache getCache()
    {
        return propagatorCache;
    }

    @Override
    public void startTimeChanged(ISpacecraft element, AbsoluteDate date)
    {

    }

    @Override
    public void stopTimeChanged(ISpacecraft element, AbsoluteDate date)
    {

    }

    @Override
    public void currentTimeChanged(ISpacecraft element, AbsoluteDate date)
    {
        try
        {
            ICoordinate position = getCoordinate(date, element);
            notifyPropagationListeners(element, position);
        }
        catch (OrekitException e)
        {
            throw new RuntimeException("Propagation failed!", e);
        }
    }

    protected void notifyPropagationListeners(ISpacecraft element, ICoordinate position)
    {
        for (IPropagationListener listener : propagationListeners) {
            listener.propagationChanged(element, position);
        }
    }
}
