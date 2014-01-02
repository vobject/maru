package maru.ui.internal.model;

import java.util.ArrayList;
import java.util.Collection;

import maru.core.model.CoreModel;
import maru.core.model.ICentralBody;
import maru.core.model.IPropagatable;
import maru.core.model.IScenarioElement;
import maru.core.model.IScenarioProject;
import maru.core.model.ITimepoint;
import maru.core.model.ScenarioModelAdapter;
import maru.ui.model.IUiProjectModelListener;
import maru.ui.model.IUiProjectModelProvider;
import maru.ui.model.IUiProjectSelectionListener;
import maru.ui.model.IUiProjectSelectionProvider;
import maru.ui.model.IUiTimelineSettingsListener;
import maru.ui.model.IUiTimelineSettingsProvider;
import maru.ui.model.UiElement;
import maru.ui.model.UiProject;

public class UiProjectModelManager extends ScenarioModelAdapter
                                   implements IUiProjectModelProvider,
                                              IUiTimelineSettingsProvider,
                                              IUiTimelineSettingsListener,
                                              IUiProjectSelectionProvider,
                                              IUiProjectSelectionListener
{
    private final UiProjectModel uiModel;

    private UiProject currentProject;
    private UiElement currentElement;

    private final Collection<IUiProjectModelListener> projectListeners;
    private final Collection<IUiProjectSelectionListener> selectionListeners;
    private final Collection<IUiTimelineSettingsListener> timeSettingsListeners;

    private static volatile UiProjectModelManager manager;

    private UiProjectModelManager()
    {
        uiModel = new UiProjectModel();

        projectListeners = new ArrayList<>();
        selectionListeners = new ArrayList<>();
        timeSettingsListeners = new ArrayList<>();
    }

    public static UiProjectModelManager getDefault()
    {
        if (manager != null) {
            return manager;
        }

        synchronized (UiProjectModelManager.class)
        {
            if (manager == null)
            {
                manager = new UiProjectModelManager();
                CoreModel.getDefault().addScenarioModelListener(manager);
            }
            return manager;
        }
    }

    public void startup()
    {
        getDefault();
    }

    public void shutdown()
    {
        uiModel.shutdown();

        // stop the UiProjectModelManager from listening to changes to
        // the underlying ScenarioProjectModel
        CoreModel.getDefault().removeScenarioModelListener(manager);
        manager = null;
    }

    @Override
    public void addTimelineSettingsListener(IUiTimelineSettingsListener listener)
    {
        if (!timeSettingsListeners.contains(listener)) {
            timeSettingsListeners.add(listener);
        }
    }

    @Override
    public void removeTimelineSettingsListener(IUiTimelineSettingsListener listener)
    {
        if (timeSettingsListeners.contains(listener)) {
            timeSettingsListeners.remove(listener);
        }
    }

    @Override
    public void addProjectModelListener(IUiProjectModelListener listener)
    {
        if (!projectListeners.contains(listener)) {
            projectListeners.add(listener);
        }
    }

    @Override
    public void removeProjectModelListener(IUiProjectModelListener listener)
    {
        if (projectListeners.contains(listener)) {
            projectListeners.remove(listener);
        }
    }

    @Override
    public void addProjectSelectionListener(IUiProjectSelectionListener listener)
    {
        if (!selectionListeners.contains(listener)) {
            selectionListeners.add(listener);
        }
    }

    @Override
    public void removeProjectSelectionListener(IUiProjectSelectionListener listener)
    {
        if (selectionListeners.contains(listener)) {
            selectionListeners.remove(listener);
        }
    }

    public boolean hasProject(IScenarioProject project)
    {
        return (getProject(project) != null);
    }

    public UiProject getProject(IScenarioProject project)
    {
        return uiModel.getUiProject(project);
    }

    public UiProject getProject(IScenarioElement element)
    {
        return uiModel.getUiProject(element);
    }

    public UiElement getElement(IScenarioElement element)
    {
        return uiModel.getUiElement(element);
    }

    public UiProject getCurrentProject()
    {
        return currentProject;
    }

    public UiElement getCurrentElement()
    {
        return currentElement;
    }

    @Override
    public void scenarioCreated(IScenarioProject project)
    {
        scenarioAdded(project);
    }

    @Override
    public void scenarioAdded(IScenarioProject project)
    {
        uiModel.addUiProject(project);
        notifyUiProjectAdded(getProject(project));
    }

    @Override
    public void scenarioRemoved(IScenarioProject project)
    {
        // a project will be removed from the workspace.

        // tell everyone that its UiProject wrapper is about to be removed
        notifyUiProjectRemoved(getProject(project));

        // remove the project's corresponding UiProject wrapper
        uiModel.removeUiProject(project);

        if (project == currentProject) {
            currentProject = null;
        }
    }

    @Override
    public void elementAdded(IScenarioElement element)
    {
        uiModel.addUiElement(element);
        notifyUiProjectChanged(getProject(element));
    }

    @Override
    public void elementRemoved(IScenarioElement element)
    {
        uiModel.removeUiElement(element);
        notifyUiProjectChanged(getProject(element));

        if (element == currentElement) {
            currentElement = null;
        }
    }

    @Override
    public void elementRenamed(IScenarioElement element)
    {
        uiProjectChanged(element);
    }

    @Override
    public void elementCommented(IScenarioElement element)
    {
        uiProjectChanged(element);
    }

    @Override
    public void elementColored(IPropagatable element)
    {
        uiProjectChanged(element);
    }

    @Override
    public void elementGraphic2DChanged(IScenarioElement element)
    {
        uiProjectChanged(element);
    }

    @Override
    public void centralbodyGmChanged(ICentralBody element)
    {
        uiProjectChanged(element);
    }

    @Override
    public void centralbodyEquatorialRadiusChanged(ICentralBody element)
    {
        uiProjectChanged(element);
    }

    @Override
    public void centralbodyPolarRadiusChanged(ICentralBody element)
    {
        uiProjectChanged(element);
    }

    @Override
    public void centralbodyMeanRadiusChanged(ICentralBody element)
    {
        uiProjectChanged(element);
    }

    @Override
    public void centralbodyFlatteningChanged(ICentralBody element)
    {
        uiProjectChanged(element);
    }

    @Override
    public void propagatablesCentralBodyChanged(IScenarioProject element)
    {
        uiProjectChanged(element);
    }

    @Override
    public void propagatablesTimeChanged(IScenarioProject element)
    {
        uiProjectChanged(element);
    }

    @Override
    public void timepointStartChanged(ITimepoint element)
    {
        uiProjectChanged(element);
    }

    @Override
    public void timepointStopChanged(ITimepoint element)
    {
        uiProjectChanged(element);
    }

    @Override
    public void timepointCurrentChanged(ITimepoint element)
    {
        uiProjectChanged(element);
    }

    @Override
    public void timepointAdded(ITimepoint element)
    {
        uiProjectChanged(element);
    }

    @Override
    public void timepointRemoved(ITimepoint element)
    {
        uiProjectChanged(element);
    }

    @Override
    public void timepointChanged(ITimepoint element)
    {
        uiProjectChanged(element);
    }

    @Override
    public void activeProjectChanged(UiProject project, UiElement element)
    {
        currentProject = project;
        currentElement = element;

        for (IUiProjectSelectionListener listener : selectionListeners) {
            listener.activeProjectChanged(project, element);
        }
    }

    @Override
    public void activeElementChanged(UiProject project, UiElement element)
    {
        currentProject = project;
        currentElement = element;

        for (IUiProjectSelectionListener listener : selectionListeners) {
            listener.activeElementChanged(project, element);
        }
    }

    @Override
    public void timeStepChanged(UiProject project, long stepSize)
    {
        notifyTimeStepChanged(project, stepSize);
    }

    private void uiProjectChanged(IScenarioElement element)
    {
        uiModel.updateUiElement(element);
        notifyUiProjectChanged(getProject(element));
    }

    private void notifyUiProjectAdded(UiProject project)
    {
        for (IUiProjectModelListener listener : projectListeners) {
            listener.projectAdded(project);
        }
    }

    private void notifyUiProjectChanged(UiProject project)
    {
        for (IUiProjectModelListener listener : projectListeners) {
            listener.projectChanged(project);
        }
    }

    private void notifyUiProjectRemoved(UiProject project)
    {
        for (IUiProjectModelListener listener : projectListeners) {
            listener.projectRemoved(project);
        }
    }

    private void notifyTimeStepChanged(UiProject project, long stepSize)
    {
        for (IUiTimelineSettingsListener listener : timeSettingsListeners) {
            listener.timeStepChanged(project, stepSize);
        }
    }
}
