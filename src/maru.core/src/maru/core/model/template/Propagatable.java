package maru.core.model.template;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import maru.IMaruResource;
import maru.MaruRuntimeException;
import maru.core.internal.model.NullCoordinate;
import maru.core.internal.model.NullPropagator;
import maru.core.model.ICentralBody;
import maru.core.model.ICoordinate;
import maru.core.model.IPropagatable;
import maru.core.model.IPropagationListener;
import maru.core.model.IPropagator;
import maru.core.model.IScenarioProject;
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

    public Propagatable(String name)
    {
        super(name);

        // the default color is black
        this.elementColor = new RGB(0, 0, 0);

        // by default objects have no image
        this.elementImage = null;

        // assign a dummy propagator value to reduce the amount of null-checks
        this.propagator = new NullPropagator();

        // the initial coordinate is what the propagator usually works with
        this.initialCoordinate = new NullCoordinate();

        // the current coordinate is only modified by the propagator
        this.currentCoordinate = new NullCoordinate();

        // no one initially listens to propagation changes of this object
        this.propagationListeners = new ArrayList<>();
    }

    @Override
    public RGB getElementColor()
    {
        return elementColor;
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

    public void setInitialCoordinate(ICoordinate coordinate)
    {
        if (coordinate == null) {
            throw new MaruRuntimeException("The initial coordinate may not be null.");
        }

        initialCoordinate = coordinate;
        updateCurrentCoordinate();
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

    public void setPropagator(Propagator propagator)
    {
        if (propagator == null) {
            throw new MaruRuntimeException("The propagator may not be null.");
        }

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
            // happens when we deserialized this object
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

        // may be null because it is transient
        if (propagationListeners != null)
        {
            // notify all propagation listeners that watch this propagatable
            // to tell then that its position has changed.
            for (IPropagationListener listener : propagationListeners) {
                listener.propagationChanged(this, coordinate);
            }
        }
    }

    @Override
    public Map<String, String> getPropertyMap()
    {
        Map<String, String> props = super.getPropertyMap();

        props.put("Propagator", propagator.getName());

        props.put("Initial Pos X", Double.toString(initialCoordinate.getPosition().getX()));
        props.put("Initial Pos Y", Double.toString(initialCoordinate.getPosition().getY()));
        props.put("Initial Pos Z", Double.toString(initialCoordinate.getPosition().getZ()));
        props.put("Initial Vel X", Double.toString(initialCoordinate.getVelocity().getX()));
        props.put("Initial Vel Y", Double.toString(initialCoordinate.getVelocity().getY()));
        props.put("Initial Vel Z", Double.toString(initialCoordinate.getVelocity().getZ()));
        props.put("Initial Time", TimeUtil.asISO8601(initialCoordinate.getTime()));
        props.put("Initial Frame", initialCoordinate.getFrame().toString());

        props.put("Current Pos X", Double.toString(currentCoordinate.getPosition().getX()));
        props.put("Current Pos Y", Double.toString(currentCoordinate.getPosition().getY()));
        props.put("Current Pos Z", Double.toString(currentCoordinate.getPosition().getZ()));
        props.put("Current Vel X", Double.toString(currentCoordinate.getVelocity().getX()));
        props.put("Current Vel Y", Double.toString(currentCoordinate.getVelocity().getY()));
        props.put("Current Vel Z", Double.toString(currentCoordinate.getVelocity().getZ()));
        props.put("Current Time", TimeUtil.asISO8601(currentCoordinate.getTime()));
        props.put("Current Frame", currentCoordinate.getFrame().toString());

        return props;
    }

    protected void updateCurrentCoordinate()
    {
        IScenarioProject scenario = getScenarioProject();
        if (scenario == null) {
            // this object was not yet added to a scenario
            return;
        }

        long currentTime = scenario.getCurrentTime().getTime();
        currentCoordinate = propagator.getCoordinate(this, currentTime);
    }
}
