package maru.core.model.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import maru.core.model.AbstractCentralBody;
import maru.core.model.AbstractGroundstation;
import maru.core.model.AbstractPropagator;
import maru.core.model.AbstractScenarioElement;
import maru.core.model.AbstractSpacecraft;
import maru.core.model.AbstractVisibleElement;
import maru.core.model.ICentralBody;
import maru.core.model.ICoordinate;
import maru.core.model.IGroundstation;
import maru.core.model.IPropagationListener;
import maru.core.model.IScenarioElement;
import maru.core.model.IScenarioModelListener;
import maru.core.model.IScenarioProject;
import maru.core.model.ISpacecraft;
import maru.core.model.ITimeProvider;
import maru.core.model.ITimepoint;
import maru.core.model.IVisibleElement;
import maru.core.model.VisibleElementColor;
import maru.core.model.resource.IMaruResource;

import org.orekit.bodies.GeodeticPoint;
import org.orekit.errors.OrekitException;
import org.orekit.time.AbsoluteDate;

public final class ScenarioModelManager
{
    interface IModelNotification
    {
        void notifyListener(IScenarioModelListener listener);
    }

    private final Map<IScenarioProject, IScenarioProjectStorage> projectStorages;
    private final List<IScenarioModelListener> listeners;

    public ScenarioModelManager()
    {
        projectStorages = new HashMap<>();
        listeners = new ArrayList<>();
    }

    public void addModelListener(IScenarioModelListener listener)
    {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeModelListener(IScenarioModelListener listener)
    {
        if (listeners.contains(listener)) {
            listeners.remove(listener);
        }
    }

    public IScenarioProjectStorage getStorage(IScenarioProject scenario)
    {
        return projectStorages.get(scenario);
    }

    public void createScenario(final IScenarioProject scenario, IScenarioProjectStorage storage, boolean update)
    {
        projectStorages.put(scenario, storage);

        if (!update) {
            return;
        }

        // save all changes to disk and notify all listeners
        // tell the listener that we added a new project to the core model
        notifyModelListeners(new IModelNotification() {
            @Override
            public void notifyListener(IScenarioModelListener listener) {
                listener.scenarioCreated(scenario);
            }
        }, scenario, true);
    }

    public void addScenario(final IScenarioProject scenario, IScenarioProjectStorage storage, boolean update)
    {
        projectStorages.put(scenario, storage);

        if (!update) {
            return;
        }

        // tell the listener that we added this project to the core model
        notifyModelListeners(new IModelNotification() {
            @Override
            public void notifyListener(IScenarioModelListener listener) {
                listener.scenarioAdded(scenario);
            }
        }, scenario, false);
    }

    public void removeScenario(final IScenarioProject scenario, boolean update)
    {
        projectStorages.remove(scenario);

        if (!update) {
            return;
        }

        // tell the listener (UI) that we removed this project
        notifyModelListeners(new IModelNotification() {
            @Override
            public void notifyListener(IScenarioModelListener listener) {
                listener.scenarioRemoved(scenario);
            }
        }, scenario, false);
    }

    public void removeElement(final IScenarioElement element, boolean update)
    {
        ((Parent) element.getParent()).removeChild(element);

        if (!update) {
            return;
        }

        notifyModelListeners(new IModelNotification() {
            @Override
            public void notifyListener(IScenarioModelListener listener) {
                listener.elementRemoved(element);
            }
        }, element, true);
    }

    public void changeElementName(final IScenarioElement element, String name, boolean update)
    {
        ((AbstractScenarioElement) element).setElementName(name);

        if (!update) {
            return;
        }

        notifyModelListeners(new IModelNotification() {
            @Override
            public void notifyListener(IScenarioModelListener listener) {
                listener.elementRenamed(element);
            }
        }, element, true);
    }

    public void changeElementComment(final IScenarioElement element, String comment, boolean update)
    {
        ((AbstractScenarioElement) element).setElementComment(comment);

        if (!update) {
            return;
        }

        notifyModelListeners(new IModelNotification() {
            @Override
            public void notifyListener(IScenarioModelListener listener) {
                listener.elementCommented(element);
            }
        }, element, true);
    }

    public void addTimepoint(IScenarioProject scenario, AbsoluteDate date, boolean update)
    {
        final Timepoint tp = new Timepoint(date);

        ((ScenarioProject) scenario).addTimepoint(tp);

        if (!update) {
            return;
        }

        // did the new timepoint become the start/stop timepoint after it
        // has been added to the scenario?
        boolean startChanged = scenario.getStartTime() == tp;
        boolean stopChanged = scenario.getStopTime() == tp;

        if (startChanged)
        {
            notifyModelListeners(new IModelNotification() {
                @Override
                public void notifyListener(IScenarioModelListener listener) {
                    listener.timepointStartChanged(tp);
                }
            }, tp, true);
        }
        else if (stopChanged)
        {
            notifyModelListeners(new IModelNotification() {
                @Override
                public void notifyListener(IScenarioModelListener listener) {
                    listener.timepointStopChanged(tp);
                }
            }, tp, true);
        }
        else
        {
            notifyModelListeners(new IModelNotification() {
                @Override
                public void notifyListener(IScenarioModelListener listener) {
                    listener.timepointAdded(tp);
                }
            }, tp, true);
        }
    }

    public void removeTimepoint(ITimepoint timepoint, boolean update)
    {
        ScenarioProject scenario = (ScenarioProject) timepoint.getScenarioProject();
        final Timepoint tp = (Timepoint) timepoint;

        scenario.removeTimepoint(tp);

        if (!update) {
            return;
        }

        boolean startChanged = scenario.getStartTime() == tp;
        boolean stopChanged = scenario.getStopTime() == tp;

        if (startChanged)
        {
            notifyModelListeners(new IModelNotification() {
                @Override
                public void notifyListener(IScenarioModelListener listener) {
                    listener.timepointStartChanged(tp);
                }
            }, tp, true);
        }
        else if (stopChanged)
        {
            notifyModelListeners(new IModelNotification() {
                @Override
                public void notifyListener(IScenarioModelListener listener) {
                    listener.timepointStopChanged(tp);
                }
            }, tp, true);
        }
        else
        {
            notifyModelListeners(new IModelNotification() {
                @Override
                public void notifyListener(IScenarioModelListener listener) {
                    listener.timepointRemoved(tp);
                }
            }, tp, true);
        }
    }

    public void changeTimepoint(ITimepoint timepoint, AbsoluteDate date, boolean update)
    {
        ScenarioProject scenario = (ScenarioProject) timepoint.getScenarioProject();
        final Timepoint tp = (Timepoint) timepoint;

        scenario.changeTimepoint(tp, date);

        if (!update) {
            return;
        }

        // did the changed timepoint become the start/stop/current timepoint
        // after the scenario has been changed?
        boolean startChanged = scenario.getStartTime() == tp;
        boolean stopChanged = scenario.getStopTime() == tp;
        boolean currentChanged = scenario.getCurrentTime() == tp;

        if (startChanged)
        {
            notifyModelListeners(new IModelNotification() {
                @Override
                public void notifyListener(IScenarioModelListener listener) {
                    listener.timepointStartChanged(tp);
                }
            }, tp, true);
        }
        else if (stopChanged)
        {
            notifyModelListeners(new IModelNotification() {
                @Override
                public void notifyListener(IScenarioModelListener listener) {
                    listener.timepointStopChanged(tp);
                }
            }, tp, true);
        }
        else if (currentChanged)
        {
            notifyModelListeners(new IModelNotification() {
                @Override
                public void notifyListener(IScenarioModelListener listener) {
                    listener.timepointCurrentChanged(tp);
                }
            }, tp, false);
        }
        else
        {
            notifyModelListeners(new IModelNotification() {
                @Override
                public void notifyListener(IScenarioModelListener listener) {
                    listener.timepointChanged(tp);
                }
            }, tp, true);
        }
    }

    public void changeScenarioElementsTime(final IScenarioProject scenario, AbsoluteDate date, boolean update)
    {
        for (ISpacecraft element : scenario.getSpacecrafts()) {
            element.currentTimeChanged(date);
        }

        for (IGroundstation element : scenario.getGroundstations()) {
            element.currentTimeChanged(date);
        }

        if (!update) {
            return;
        }

        notifyModelListeners(new IModelNotification() {
            @Override
            public void notifyListener(IScenarioModelListener listener) {
                listener.propagatablesTimeChanged(scenario);
            }
        }, scenario, false);
    }

    public void addGroundstation(IScenarioProject scenario, final IGroundstation groundstation, boolean update)
    {
        ((GroundstationContainer) scenario.getGroundstationContainer()).addGroundstation(groundstation);

        if (!update) {
            return;
        }

        notifyModelListeners(new IModelNotification() {
            @Override
            public void notifyListener(IScenarioModelListener listener) {
                listener.elementAdded(groundstation);
            }
        }, groundstation, true);
    }

    public void addSpacecraft(IScenarioProject scenario, final ISpacecraft spacecraft, boolean update)
    {
        ((SpacecraftContainer) scenario.getSpacecraftContainer()).addSpacecraft(spacecraft);

        if (!update) {
            return;
        }

        notifyModelListeners(new IModelNotification() {
            @Override
            public void notifyListener(IScenarioModelListener listener) {
                listener.elementAdded(spacecraft);
            }
        }, spacecraft, true);
    }

    public void changeElementColor(final IVisibleElement element, VisibleElementColor color, boolean update)
    {
        ((AbstractVisibleElement) element).setElementColor(color);

        if (!update) {
            return;
        }

        notifyModelListeners(new IModelNotification() {
            @Override
            public void notifyListener(IScenarioModelListener listener) {
                listener.elementColorChanged(element);
            }
        }, element, true);
    }

    public void changeElementImage(final IVisibleElement element, IMaruResource image, boolean update)
    {
        ((AbstractVisibleElement) element).setElementImage(image);

        if (!update) {
            return;
        }

        notifyModelListeners(new IModelNotification() {
            @Override
            public void notifyListener(IScenarioModelListener listener) {
                listener.elementImageChanged(element);
            }
        }, element, true);
    }

    public void changeInitialCoordinate(final IGroundstation element, GeodeticPoint position, double elevation, boolean update)
    {
        ((AbstractGroundstation) element).setGeodeticPosition(position);
        ((AbstractGroundstation) element).setElevationAngle(elevation);

        if (!update) {
            return;
        }

        notifyModelListeners(new IModelNotification() {
            @Override
            public void notifyListener(IScenarioModelListener listener) {
                listener.elementInitialCoordinateChanged(element);
            }
        }, element, true);
    }

    public void changeInitialCoordinate(final ISpacecraft element, ICoordinate coordinate, boolean update)
    {
        try {
            ((AbstractSpacecraft) element).setInitialCoordinate(coordinate);
        } catch (OrekitException e) {
            throw new RuntimeException("Failed to set initial coorinate.", e);
        }

        if (!update) {
            return;
        }

        notifyModelListeners(new IModelNotification() {
            @Override
            public void notifyListener(IScenarioModelListener listener) {
                listener.elementInitialCoordinateChanged(element);
            }
        }, element, true);
    }

    public void changeCentralBodyImage(final ICentralBody element, IMaruResource image, boolean update)
    {
        ((AbstractCentralBody) element).setTexture(image);

        if (!update) {
            return;
        }

        notifyModelListeners(new IModelNotification() {
            @Override
            public void notifyListener(IScenarioModelListener listener) {
                listener.centralbodyImageChanged(element);
            }
        }, element, true);
    }

    public void changeCentralBodyGM(final ICentralBody element, double gm, boolean update)
    {
        ((AbstractCentralBody) element).setProperties(element.getEquatorialRadius(),
                                                      element.getFlattening(),
                                                      gm);

        // make all propagatables adapt to the new central body
        IScenarioProject scenario = element.getScenarioProject();

        for (IGroundstation gs : scenario.getGroundstations()) {
            gs.centralbodyChanged();
        }

        for (ISpacecraft sat : scenario.getSpacecrafts()) {
            sat.centralbodyChanged();
        }

        if (!update) {
            return;
        }

        notifyModelListeners(new IModelNotification() {
            @Override
            public void notifyListener(IScenarioModelListener listener) {
                listener.centralbodyGmChanged(element);
            }
        }, element, true);
    }

    public void changeCentralBodyEquatorialRadius(final ICentralBody element, double radius, boolean update)
    {
        ((AbstractCentralBody) element).setProperties(radius,
                                                      element.getFlattening(),
                                                      element.getGM());

        // make all propagatables adapt to the new central body
        IScenarioProject scenario = element.getScenarioProject();

        for (IGroundstation gs : scenario.getGroundstations()) {
            gs.centralbodyChanged();
        }

        for (ISpacecraft sat : scenario.getSpacecrafts()) {
            sat.centralbodyChanged();
        }

        if (!update) {
            return;
        }

        notifyModelListeners(new IModelNotification() {
            @Override
            public void notifyListener(IScenarioModelListener listener) {
                listener.centralbodyEquatorialRadiusChanged(element);
            }
        }, element, true);
    }

    public void changeCentralBodyFlattening(final ICentralBody element, double flattening, boolean update)
    {
        ((AbstractCentralBody) element).setProperties(element.getEquatorialRadius(),
                                                      flattening,
                                                      element.getGM());

        // make all propagatables adapt to the new central body
        IScenarioProject scenario = element.getScenarioProject();

        for (IGroundstation gs : scenario.getGroundstations()) {
            gs.centralbodyChanged();
        }

        for (ISpacecraft sat : scenario.getSpacecrafts()) {
            sat.centralbodyChanged();
        }

        if (!update) {
            return;
        }

        notifyModelListeners(new IModelNotification() {
            @Override
            public void notifyListener(IScenarioModelListener listener) {
                listener.centralbodyFlatteningChanged(element);
            }
        }, element, true);
    }

    public void addTimeProvider(IScenarioProject scenario, ITimeProvider provider)
    {
        for (IGroundstation element : scenario.getGroundstations()) {
            ((AbstractGroundstation) element).addTimeProvider(provider);
        }

        for (ISpacecraft element : scenario.getSpacecrafts()) {
            ((AbstractSpacecraft) element).addTimeProvider(provider);
        }
    }

    public void removeTimeProvider(IScenarioProject scenario, ITimeProvider provider)
    {
        for (IGroundstation element : scenario.getGroundstations()) {
            ((AbstractGroundstation) element).removeTimeProvider(provider);
        }

        for (ISpacecraft element : scenario.getSpacecrafts()) {
            ((AbstractSpacecraft) element).removeTimeProvider(provider);
        }
    }

    public void addTimeProvider(IGroundstation element, ITimeProvider provider)
    {
        ((AbstractGroundstation) element).addTimeProvider(provider);
    }

    public void removeTimeProvider(IGroundstation element, ITimeProvider provider)
    {
        ((AbstractGroundstation) element).removeTimeProvider(provider);
    }

    public void addTimeProvider(ISpacecraft element, ITimeProvider provider)
    {
        ((AbstractSpacecraft) element).addTimeProvider(provider);
    }

    public void removeTimeProvider(ISpacecraft element, ITimeProvider provider)
    {
        ((AbstractSpacecraft) element).removeTimeProvider(provider);
    }

    public void addPropagationListener(IScenarioProject scenario, IPropagationListener listener)
    {
        for (ISpacecraft element : scenario.getSpacecrafts()) {
            ((AbstractSpacecraft) element).addPropagationListener(listener);
        }
    }

    public void removePropagationListener(IScenarioProject scenario, IPropagationListener listener)
    {
        for (ISpacecraft element : scenario.getSpacecrafts()) {
            ((AbstractSpacecraft) element).removePropagationListener(listener);
        }
    }

    public void addPropagationListener(ISpacecraft element, IPropagationListener listener)
    {
        ((AbstractSpacecraft) element).addPropagationListener(listener);
    }

    public void removePropagationListener(ISpacecraft element, IPropagationListener listener)
    {
        ((AbstractSpacecraft) element).removePropagationListener(listener);
    }

    public void changePropagator(ISpacecraft element, AbstractPropagator propagator)
    {
        try {
            ((AbstractSpacecraft) element).setPropagator(propagator);
        } catch (OrekitException e) {
            throw new RuntimeException("Failed to set propagator.", e);
        }
    }

    private void notifyModelListeners(IModelNotification notification,
                                      IScenarioElement element,
                                      boolean writeToDisk)
    {
        if (writeToDisk) {
            writeProjectStorage(element.getScenarioProject());
        }

        for (IScenarioModelListener listener : listeners) {
            notification.notifyListener(listener);
        }
    }

    private void writeProjectStorage(IScenarioProject project)
    {
        projectStorages.get(project).writeScenarioProject();
    }
}
