package maru.core.model.template;

import java.util.ArrayList;
import java.util.Collection;

import maru.core.model.ICoordinate;
import maru.core.model.IPropagatable;
import maru.core.model.IPropagationListener;
import maru.core.model.IPropagator;

public abstract class Propagator implements IPropagator
{
    private static final long serialVersionUID = 1L;

    /**
     * Listeners that are notified when a new coordinate was calculated.
     *
     * A listener is usually the Spacecraft that a Propagator is associated with.
     */
    private final Collection<IPropagationListener> propagationListeners;

    public Propagator()
    {
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
    public void startTimeChanged(IPropagatable element, long time)
    {

    }

    @Override
    public void stopTimeChanged(IPropagatable element, long time)
    {

    }

    @Override
    public void currentTimeChanged(IPropagatable element, long currentTime)
    {
        ICoordinate position = getCoordinate(element, currentTime);
        notifyPropagationListeners(element, position);
    }

    @Override
    public void clearCoordinateCache()
    {

    }

    protected void notifyPropagationListeners(IPropagatable element, ICoordinate position)
    {
        for (IPropagationListener listener : propagationListeners) {
            listener.propagationChanged(element, position);
        }
    }
}
