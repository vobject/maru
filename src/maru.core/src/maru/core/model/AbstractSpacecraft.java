package maru.core.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import maru.MaruRuntimeException;
import maru.core.utils.EclipseState;
import maru.core.utils.VisibilityUtils;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.errors.OrekitException;
import org.orekit.frames.Frame;
import org.orekit.time.AbsoluteDate;

class NullPropagator implements IPropagator
{
    private static final long serialVersionUID = 1L;
    private static final ICoordinate NULL_COORDINATE = new NullCoordinate();
    private static final Collection<ICoordinate> NULL_COORDINATES = Collections.emptyList();;
    @Override public String getName() { return "NullPropagator"; }
    @Override public ICoordinate getCoordinate(ISpacecraft element, AbsoluteDate time) { return NULL_COORDINATE; }
    @Override public Collection<ICoordinate> getCoordinates(ISpacecraft element,  AbsoluteDate start, AbsoluteDate stop, long stepSize) { return NULL_COORDINATES; }
    @Override public void startTimeChanged(ISpacecraft element, AbsoluteDate date) { }
    @Override public void stopTimeChanged(ISpacecraft element, AbsoluteDate date) { }
    @Override public void currentTimeChanged(ISpacecraft element, AbsoluteDate date) { }
    @Override public void clearCoordinateCache() { }
}

class NullCoordinate implements ICoordinate
{
    private static final long serialVersionUID = 1L;
    @Override public ICentralBody getCentralBody() { return null; }
    @Override public Vector3D getPosition() { return null; }
    @Override public Vector3D getVelocity() { return null; }
    @Override public AbsoluteDate getDate() {  return null; }
    @Override public Frame getFrame() { return null; }
    @Override public EclipseState getEclipseState() { return null; }
}

public abstract class AbstractSpacecraft extends AbstractVisibleElement implements ISpacecraft
{
    private static final long serialVersionUID = 1L;

    private IPropagator propagator;
    private ICoordinate initialCoordinate;
    private ICoordinate currentCoordinate;
    private transient Collection<IPropagationListener> propagationListeners;

    public AbstractSpacecraft(String name)
    {
        super(name);

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
    public ICentralBody getCentralBody()
    {
        return getScenarioProject().getCentralBody();
    }

    @Override
    public ICoordinate getInitialCoordinate()
    {
        return initialCoordinate;
    }

    public void setInitialCoordinate(ICoordinate coordinate) throws OrekitException
    {
        initialCoordinate = coordinate;
        updateCurrentCoordinate();
    }

    @Override
    public ICoordinate getCurrentCoordinate()
    {
        return currentCoordinate;
    }

    @Override
    public boolean hasAccessTo(ICoordinate coordinate) throws OrekitException
    {
        return VisibilityUtils.hasAccessTo(getCurrentCoordinate(), coordinate);
    }

    @Override
    public boolean hasAccessTo(IGroundstation groundstation) throws OrekitException
    {
        return VisibilityUtils.hasAccessTo(getCurrentCoordinate(), groundstation);
    }

    @Override
    public double getDistanceTo(ICoordinate coordinate) throws OrekitException
    {
        return VisibilityUtils.getDistanceTo(getCurrentCoordinate(), coordinate);
    }

    @Override
    public double getDistanceTo(IGroundstation groundstation) throws OrekitException
    {
        return VisibilityUtils.getDistanceTo(getCurrentCoordinate(), groundstation);
    }

    @Override
    public IPropagator getPropagator()
    {
        return propagator;
    }

    public void setPropagator(AbstractPropagator propagator) throws OrekitException
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
    public void startTimeChanged(AbsoluteDate date)
    {
        propagator.startTimeChanged(this, date);
    }

    @Override
    public void stopTimeChanged(AbsoluteDate date)
    {
        propagator.stopTimeChanged(this, date);
    }

    @Override
    public void currentTimeChanged(AbsoluteDate date)
    {
        propagator.currentTimeChanged(this, date);
    }

    @Override
    public void propagationChanged(ISpacecraft element, ICoordinate coordinate)
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
    public List<GeodeticPoint> getVisibilityCircle(int points) throws OrekitException
    {
        return VisibilityUtils.getVisibilityCircle(getCurrentCoordinate(), points);
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
        props.put("Initial Time", initialCoordinate.getDate().toString());
        props.put("Initial Frame", initialCoordinate.getFrame().toString());

        props.put("Current Pos X", Double.toString(currentCoordinate.getPosition().getX()));
        props.put("Current Pos Y", Double.toString(currentCoordinate.getPosition().getY()));
        props.put("Current Pos Z", Double.toString(currentCoordinate.getPosition().getZ()));
        props.put("Current Vel X", Double.toString(currentCoordinate.getVelocity().getX()));
        props.put("Current Vel Y", Double.toString(currentCoordinate.getVelocity().getY()));
        props.put("Current Vel Z", Double.toString(currentCoordinate.getVelocity().getZ()));
        props.put("Current Time", currentCoordinate.getDate().toString());
        props.put("Current Frame", currentCoordinate.getFrame().toString());

        return props;
    }

    protected void updateCurrentCoordinate() throws OrekitException
    {
        IScenarioProject scenario = getScenarioProject();
        if (scenario == null) {
            // this object was not yet added to a scenario
            return;
        }

        AbsoluteDate currentTime = scenario.getCurrentTime().getTime();
        currentCoordinate = propagator.getCoordinate(this, currentTime);
    }
}
