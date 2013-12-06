package maru.core.internal.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import maru.IMaruPluginResource;
import maru.core.model.ICentralBody;
import maru.core.model.IGroundstation;
import maru.core.model.IPropagatable;
import maru.core.model.IPropagationListener;
import maru.core.model.IScenarioElement;
import maru.core.model.IScenarioModelListener;
import maru.core.model.IScenarioProject;
import maru.core.model.ISpacecraft;
import maru.core.model.ITimeProvider;
import maru.core.model.ITimepoint;
import maru.core.model.template.Propagatable;
import maru.core.model.template.Propagator;
import maru.core.model.template.ScenarioElement;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.swt.graphics.RGB;

final public class ScenarioModelManager implements IResourceChangeListener
{
    private enum ScenarioEvent
    {
        SCENARIO_CREATED,
        SCENARIO_ADDED,
        SCENARIO_REMOVED,

        ELEMENT_ADDED,
        ELEMENT_REMOVED,
        ELEMENT_RENAMED,
        ELEMENT_COMMENTED,

        ELEMENT_COLORED,
        ELEMENT_GRAPHIC2D_CHANGED,

        PROPAGATABLES_TIME_CHANGED,

        TIMEPOINT_START_CHANGED,
        TIMEPOINT_STOP_CHANGED,
        TIMEPOINT_CURRENT_CHANGED,

        TIMEPOINT_ADDED,
        TIMEPOINT_REMOVED,
        TIMEPOINT_CHANGED
    }

    private final ScenarioModel model;

    private final Map<IScenarioProject, ScenarioProjectStorage> projectStorages;
    private final Collection<IScenarioModelListener> listeners;

    private static volatile ScenarioModelManager manager;

    private ScenarioModelManager()
    {
        model = new ScenarioModel();
        projectStorages = new HashMap<>();
        listeners = new ArrayList<>();
    }

    public static ScenarioModelManager getDefault()
    {
        if (manager == null)
        {
            synchronized (ScenarioModelManager.class)
            {
                if (manager == null)
                {
                    manager = new ScenarioModelManager();
                    ResourcesPlugin.getWorkspace().addResourceChangeListener(manager, IResourceChangeEvent.PRE_DELETE);
                }
            }
        }
        return manager;
    }

    public void startup()
    {
        getDefault();
    }

    public void shutdown()
    {
        ResourcesPlugin.getWorkspace().removeResourceChangeListener(manager);
        manager = null;
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

    public boolean hasProject(IProject project)
    {
        return hasProject(project.getName());
    }

    public boolean hasProject(String name)
    {
        return (model.getScenarioProject(name) != null);
    }

    public Collection<IScenarioProject> getProjects()
    {
        return model.getScenarioProjects();
    }

    public IScenarioProject getProject(IProject project)
    {
        return getProject(project.getName());
    }

    public IScenarioProject getProject(String name)
    {
        return model.getScenarioProject(name);
    }

    public IScenarioProject createProject(IProject project,
                                          Timepoint start,
                                          Timepoint stop,
                                          String comment,
                                          ICentralBody centralBody)
    {
        ScenarioProjectStorage scenarioStorage =
            new ScenarioProjectStorage(model, project,
                                       start, stop,
                                       comment, centralBody);
        ScenarioProject scenarioProject = scenarioStorage.getScenarioProject();

        model.addScenarioProject(scenarioProject);
        projectStorages.put(scenarioProject, scenarioStorage);
        return scenarioProject;
    }

    public IScenarioProject createProject(IProject project)
    {
        ScenarioProjectStorage scenarioStorage = new ScenarioProjectStorage(model, project);
        ScenarioProject scenarioProject = scenarioStorage.getScenarioProject();

        model.addScenarioProject(scenarioProject);
        projectStorages.put(scenarioProject, scenarioStorage);
        return scenarioProject;
    }

    public void removeProject(IProject project, boolean update)
    {
        IScenarioProject scenarioProject = model.getScenarioProject(project.getName());

        // throw away the reference to the storage file
        projectStorages.remove(scenarioProject);

        // remove the project from the model
        model.removeScenarioProject(scenarioProject);

        if (update) {
            // tell the listener (UI) that we removed this project
            notifyScenarioRemoved(scenarioProject);
        }
    }

    public void renameElement(IScenarioElement element, String name, boolean update)
    {
        ((ScenarioElement) element).setElementName(name);

        if (update) {
            notifyElementRenamed(element);
        }
    }

    public void removeElement(IScenarioElement element, boolean update)
    {
        ((Parent) element.getParent()).removeChild(element);

        if (update) {
            notifyElementRemoved(element);
        }
    }

    public void commentElement(IScenarioElement element, String comment, boolean update)
    {
        ((ScenarioElement) element).setElementComment(comment);

        if (update) {
            notifyElementCommented(element);
        }
    }

    public void addTimepoint(IScenarioProject project, long time, boolean update)
    {
        ScenarioProject scenarioProject = (ScenarioProject) project;
        Timepoint tp = new Timepoint(time);

        scenarioProject.addTimepoint(tp);

        if (update) {
            if (scenarioProject.getStartTime() == tp) {
                notifyStartChanged(tp);
            } else if (scenarioProject.getStopTime() == tp) {
                notifyStopChanged(tp);
            } else {
                notifyTimepointAdded(tp);
            }
        }
    }

    public void removeTimepoint(ITimepoint timepoint, boolean update)
    {
        ScenarioProject scenarioProject = (ScenarioProject) timepoint.getScenarioProject();
        Timepoint tp = (Timepoint) timepoint;

        boolean startChanged = scenarioProject.getStartTime() == tp;
        boolean stopChanged = scenarioProject.getStopTime() == tp;

        scenarioProject.removeTimepoint(tp);

        if (update) {
            if (startChanged) {
                notifyStartChanged(scenarioProject.getStartTime());
            } else if (stopChanged) {
                notifyStopChanged(scenarioProject.getStopTime());
            } else {
                notifyTimepointRemoved(tp);
            }
        }
    }

    public void changeTimepoint(ITimepoint timepoint, long time, boolean update)
    {
        ScenarioProject scenarioProject = (ScenarioProject) timepoint.getScenarioProject();
        Timepoint scenarioTimepoint = (Timepoint) timepoint;

        scenarioProject.changeTimepoint(scenarioTimepoint, time);

        if (update) {
            if (scenarioProject.getStartTime() == scenarioTimepoint) {
                notifyStartChanged(scenarioTimepoint);
            } else if (scenarioProject.getStopTime() == scenarioTimepoint) {
                notifyStopChanged(scenarioTimepoint);
            } else if (scenarioProject.getCurrentTime() == scenarioTimepoint) {
                notifyCurrentChanged(scenarioTimepoint);
            } else {
                notifyTimepointChanged(scenarioTimepoint);
            }
        }
    }

    public void changePropagatablesTime(IScenarioProject project, long time, boolean update)
    {
        for (IPropagatable element : project.getPropagatables()) {
            element.currentTimeChanged(time);
        }

        if (update) {
            notifyPropagatablesTimeChanged(project);
        }
    }

    public void addGroundstation(IScenarioProject project, IGroundstation groundstation, boolean update)
    {
        ((GroundstationContainer) project.getGroundstationContainer()).addGroundstation(groundstation);

        if (update) {
            notifyElementAdded(groundstation);
        }
    }

    public void addSpacecraft(IScenarioProject project, ISpacecraft spacecraft, boolean update)
    {
        ((SpacecraftContainer) project.getSpacecraftContainer()).addSpacecraft(spacecraft);

        if (update) {
            notifyElementAdded(spacecraft);
        }
    }

    public void setElementColor(IPropagatable element, RGB color, boolean update)
    {
        ((Propagatable) element).setElementColor(color);

        if (update) {
            notifyElementColored(element);
        }
    }

    public void setElementGraphics2D(IScenarioElement element, IMaruPluginResource graphic2d, boolean update)
    {
        ((ScenarioElement) element).setElementGraphic2D(graphic2d);

        if (update) {
            notifyElementGraphic2DChanged(element);
        }
    }

    public void addTimeProvider(IScenarioProject project, ITimeProvider provider)
    {
        for (IPropagatable element : ((ScenarioProject) project).getPropagatables()) {
            ((Propagatable) element).addTimeProvider(provider);
        }
    }

    public void removeTimeProvider(IScenarioProject project, ITimeProvider provider)
    {
        for (IPropagatable element : ((ScenarioProject) project).getPropagatables()) {
            ((Propagatable) element).removeTimeProvider(provider);
        }
    }

    public void addTimeProvider(IPropagatable element, ITimeProvider provider)
    {
        ((Propagatable) element).addTimeProvider(provider);
    }

    public void removeTimeProvider(IPropagatable element, ITimeProvider provider)
    {
        ((Propagatable) element).removeTimeProvider(provider);
    }

    public void addPropagationListener(IScenarioProject project, IPropagationListener listener)
    {
        for (IPropagatable element : ((ScenarioProject) project).getPropagatables()) {
            ((Propagatable) element).addPropagationListener(listener);
        }
    }

    public void removePropagationListener(IScenarioProject project, IPropagationListener listener)
    {
        for (IPropagatable element : ((ScenarioProject) project).getPropagatables()) {
            ((Propagatable) element).removePropagationListener(listener);
        }
    }

    public void addPropagationListener(IPropagatable element, IPropagationListener listener)
    {
        ((Propagatable) element).addPropagationListener(listener);
    }

    public void removePropagationListener(IPropagatable element, IPropagationListener listener)
    {
        ((Propagatable) element).removePropagationListener(listener);
    }

    public void setPropagator(IPropagatable element, Propagator propagator)
    {
        ((Propagatable) element).setPropagator(propagator);
    }

    public void notifyScenarioCreated(IScenarioProject project)
    {
        writeProjectStorage(project);
        notifyScenarioElementListeners(ScenarioEvent.SCENARIO_CREATED, project);
    }

    public void notifyScenarioAdded(IScenarioProject project)
    {
        notifyScenarioElementListeners(ScenarioEvent.SCENARIO_ADDED, project);
    }

    public void notifyScenarioRemoved(IScenarioProject project)
    {
        notifyScenarioElementListeners(ScenarioEvent.SCENARIO_REMOVED, project);
    }

    public void notifyElementAdded(IScenarioElement element)
    {
        writeProjectStorage(element.getScenarioProject());
        notifyScenarioElementListeners(ScenarioEvent.ELEMENT_ADDED, element);
    }

    public void notifyElementRemoved(IScenarioElement element)
    {
        writeProjectStorage(element.getScenarioProject());
        notifyScenarioElementListeners(ScenarioEvent.ELEMENT_REMOVED, element);
    }

    private void notifyElementRenamed(IScenarioElement element)
    {
        writeProjectStorage(element.getScenarioProject());
        notifyScenarioElementListeners(ScenarioEvent.ELEMENT_RENAMED, element);
    }

    private void notifyElementCommented(IScenarioElement element)
    {
        writeProjectStorage(element.getScenarioProject());
        notifyScenarioElementListeners(ScenarioEvent.ELEMENT_COMMENTED, element);
    }

    private void notifyElementColored(IPropagatable element)
    {
        writeProjectStorage(element.getScenarioProject());
        notifyScenarioElementListeners(ScenarioEvent.ELEMENT_COLORED, element);
    }

    private void notifyElementGraphic2DChanged(IScenarioElement element)
    {
        writeProjectStorage(element.getScenarioProject());
        notifyScenarioElementListeners(ScenarioEvent.ELEMENT_GRAPHIC2D_CHANGED, element);
    }

    private void notifyStartChanged(ITimepoint element)
    {
        writeProjectStorage(element.getScenarioProject());
        notifyScenarioElementListeners(ScenarioEvent.TIMEPOINT_START_CHANGED, element);
    }

    private void notifyStopChanged(ITimepoint element)
    {
        writeProjectStorage(element.getScenarioProject());
        notifyScenarioElementListeners(ScenarioEvent.TIMEPOINT_STOP_CHANGED, element);
    }

    private void notifyCurrentChanged(ITimepoint element)
    {
        notifyScenarioElementListeners(ScenarioEvent.TIMEPOINT_CURRENT_CHANGED, element);
    }

    private void notifyPropagatablesTimeChanged(IScenarioProject element)
    {
        notifyScenarioElementListeners(ScenarioEvent.PROPAGATABLES_TIME_CHANGED, element);
    }

    private void notifyTimepointAdded(ITimepoint element)
    {
        writeProjectStorage(element.getScenarioProject());
        notifyScenarioElementListeners(ScenarioEvent.TIMEPOINT_ADDED, element);
    }

    private void notifyTimepointRemoved(ITimepoint element)
    {
        writeProjectStorage(element.getScenarioProject());
        notifyScenarioElementListeners(ScenarioEvent.TIMEPOINT_REMOVED, element);
    }

    private void notifyTimepointChanged(ITimepoint element)
    {
        writeProjectStorage(element.getScenarioProject());
        notifyScenarioElementListeners(ScenarioEvent.TIMEPOINT_CHANGED, element);
    }

    @Override
    public void resourceChanged(IResourceChangeEvent event)
    {
        if (event.getType() != IResourceChangeEvent.PRE_DELETE) {
            return;
        }

        if (event.getResource().getType() != IResource.PROJECT) {
            return;
        }

        removeProject((IProject) event.getResource(), true);
    }

    private void notifyScenarioElementListeners(ScenarioEvent type, IScenarioElement element)
    {
        for (IScenarioModelListener listener : listeners)
        {
            switch (type)
            {
                case SCENARIO_CREATED:
                    listener.scenarioCreated((IScenarioProject) element);
                    break;
                case SCENARIO_ADDED:
                    listener.scenarioAdded((IScenarioProject) element);
                    break;
                case SCENARIO_REMOVED:
                    listener.scenarioRemoved((IScenarioProject) element);
                    break;

                case ELEMENT_ADDED:
                    listener.elementAdded(element);
                    break;
                case ELEMENT_REMOVED:
                    listener.elementRemoved(element);
                    break;
                case ELEMENT_RENAMED:
                    listener.elementRenamed(element);
                    break;
                case ELEMENT_COMMENTED:
                    listener.elementCommented(element);
                    break;
                case ELEMENT_COLORED:
                    listener.elementColored((IPropagatable) element);
                    break;
                case ELEMENT_GRAPHIC2D_CHANGED:
                    listener.elementGraphic2DChanged((IPropagatable) element);
                    break;

                case PROPAGATABLES_TIME_CHANGED:
                    listener.propagatablesTimeChanged((IScenarioProject) element);
                    break;

                case TIMEPOINT_START_CHANGED:
                    listener.timepointStartChanged((ITimepoint) element);
                    break;
                case TIMEPOINT_STOP_CHANGED:
                    listener.timepointStopChanged((ITimepoint) element);
                    break;
                case TIMEPOINT_CURRENT_CHANGED:
                    listener.timepointCurrentChanged((ITimepoint) element);
                    break;

                case TIMEPOINT_ADDED:
                    listener.timepointAdded((ITimepoint) element);
                    break;
                case TIMEPOINT_REMOVED:
                    listener.timepointRemoved((ITimepoint) element);
                    break;
                case TIMEPOINT_CHANGED:
                    listener.timepointChanged((ITimepoint) element);
                    break;
            }
        }
    }

    private void writeProjectStorage(IScenarioProject project)
    {
        projectStorages.get(project).writeScenarioProject();
    }
}
