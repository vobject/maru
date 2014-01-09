package maru.core.model.template;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import maru.IMaruResource;
import maru.core.model.ICentralBody;
import maru.core.model.ICoordinate;
import maru.core.model.IPropagatable;
import maru.core.model.IPropagationListener;
import maru.core.model.IPropagator;
import maru.core.model.ITimeProvider;
import maru.core.utils.TimeUtil;

import org.eclipse.swt.graphics.RGB;

public abstract class Propagatable extends ScenarioElement implements IPropagatable
{
    private static final long serialVersionUID = 1L;

    private RGB elementColor;
    private IMaruResource elementImage;

    private IPropagator propagator;
    private ICoordinate initialCoordinate;
    private ICoordinate currentCoordinate;
    private transient Collection<IPropagationListener> propagationListeners;

    public Propagatable(String name, ICoordinate initialPosition)
    {
        super(name);

        this.elementColor = new RGB(0, 0, 0);
        this.initialCoordinate = initialPosition;
        this.currentCoordinate = initialPosition;
        this.propagationListeners = new ArrayList<>();
    }

    public void setElementColor(RGB color)
    {
        elementColor = color;
    }

    @Override
    public IMaruResource getElementImage()
    {
        return elementImage;
    }

    public void setElementImage(IMaruResource elementImage)
    {
        this.elementImage = elementImage;
    }

    public void setInitialCoordinate(ICoordinate coordinate)
    {
        initialCoordinate = coordinate;

        updateCurrentCoordinate();
    }

    public void setCurrentCoordinate(ICoordinate coordinate)
    {
        currentCoordinate = coordinate;
    }

    public void setPropagator(Propagator propagator)
    {
        this.propagator = propagator;
        propagator.addPropagationListener(this);

        updateCurrentCoordinate();
    }

    public void addTimeProvider(ITimeProvider provider)
    {
        provider.addTimeListener(this);
    }

    public void removeTimeProvider(ITimeProvider provider)
    {
        provider.removeTimeListener(this);
    }

    public void addPropagationListener(IPropagationListener listener)
    {
        if (propagationListeners == null) {
            propagationListeners = new ArrayList<>();
        }

        if (!propagationListeners.contains(listener)) {
            propagationListeners.add(listener);
        }
    }

    public void removePropagationListener(IPropagationListener listener)
    {
        if (propagationListeners == null) {
            return;
        }

        if (propagationListeners.contains(listener)) {
            propagationListeners.remove(listener);
        }
    }

    @Override
    public Map<String, String> getPropertyMap()
    {
        Map<String, String> properties = super.getPropertyMap();

        properties.put("Initial Pos X", Double.toString(initialCoordinate.getPosition().getX()));
        properties.put("Initial Pos Y", Double.toString(initialCoordinate.getPosition().getY()));
        properties.put("Initial Pos Z", Double.toString(initialCoordinate.getPosition().getZ()));
        properties.put("Initial Vel X", Double.toString(initialCoordinate.getVelocity().getX()));
        properties.put("Initial Vel Y", Double.toString(initialCoordinate.getVelocity().getY()));
        properties.put("Initial Vel Z", Double.toString(initialCoordinate.getVelocity().getZ()));
        properties.put("Initial Time", TimeUtil.asISO8601(initialCoordinate.getTime()));
        properties.put("Initial Frame", initialCoordinate.getFrame().toString());

        properties.put(propagator.getName() + " Pos X", Double.toString(currentCoordinate.getPosition().getX()));
        properties.put(propagator.getName() + " Pos Y", Double.toString(currentCoordinate.getPosition().getY()));
        properties.put(propagator.getName() + " Pos Z", Double.toString(currentCoordinate.getPosition().getZ()));
        properties.put(propagator.getName() + " Vel X", Double.toString(currentCoordinate.getVelocity().getX()));
        properties.put(propagator.getName() + " Vel Y", Double.toString(currentCoordinate.getVelocity().getY()));
        properties.put(propagator.getName() + " Vel Z", Double.toString(currentCoordinate.getVelocity().getZ()));
        properties.put(propagator.getName() + " Time", TimeUtil.asISO8601(currentCoordinate.getTime()));
        properties.put(propagator.getName() + " Frame", currentCoordinate.getFrame().toString());

        return properties;
    }

    @Override
    public RGB getElementColor()
    {
        return elementColor;
    }

    @Override
    public ICentralBody getCentralBody()
    {
        return getScenarioProject().getCentralBody();
    }

    @Override
    public ICoordinate getInitialCoordinate()
    {
        return initialCoordinate;
    }

    @Override
    public ICoordinate getCurrentCoordinate()
    {
        return currentCoordinate;
    }

    @Override
    public IPropagator getPropagator()
    {
        return propagator;
    }

    @Override
    public void startTimeChanged(long time)
    {
        propagator.startTimeChanged(this, time);
    }

    @Override
    public void stopTimeChanged(long time)
    {
        propagator.stopTimeChanged(this, time);
    }

    @Override
    public void currentTimeChanged(long time)
    {
        propagator.currentTimeChanged(this, time);
    }

    @Override
    public void propagationChanged(IPropagatable element, ICoordinate coordinate)
    {
        if (element != this) {
            return;
        }

        currentCoordinate = coordinate;

        // notify all propagation listeners that watch this propagatable
        // to tell then that its position has changed.
        for (IPropagationListener listener : propagationListeners) {
            listener.propagationChanged(this, coordinate);
        }
    }

    protected void updateCurrentCoordinate()
    {
        if (propagator == null) {
            // nothing there that could calculate the current coordinate
            return;
        }

        if (initialCoordinate == null) {
            // no initial coordinate that the current coordinate can be based on
            return;
        }

        long currentTime = getScenarioProject().getCurrentTime().getTime();
        currentCoordinate = propagator.getCoordinate(this, currentTime);
    }
}
