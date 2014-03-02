package maru.ui.model.internal;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import maru.core.model.IScenarioElement;
import maru.core.model.IScenarioProject;
import maru.ui.model.UiElement;
import maru.ui.model.UiProject;

public class UiProjectModel
{
    private final Map<IScenarioProject, UiProject> projects;

    public UiProjectModel()
    {
        projects = new HashMap<>();
    }

    public void shutdown()
    {
        for (UiProject uiProject : projects.values()) {
            uiProject.remove();
        }
    }

    public Collection<IScenarioProject> getProjects()
    {
        return projects.keySet();
    }

    public Collection<UiProject> getUiProjects()
    {
        return projects.values();
    }

    public UiProject getUiProject(IScenarioProject project)
    {
        return projects.get(project);
    }

    public UiProject getUiProject(IScenarioElement element)
    {
        return projects.get(element.getScenarioProject());
    }

    public UiElement getUiElement(IScenarioElement element)
    {
        UiProject uiProject = getUiProject(element);
        return uiProject.getChild(element);
    }

    public void addUiProject(IScenarioProject project)
    {
        projects.put(project, new UiProject(project));
    }

    public void updateUiProject(IScenarioProject project)
    {
        removeUiProject(project);

        // the UiProject instance was requested to update itself
        // using the information from the given IScenarioProject.
        // reinitialize the UiProject by creating a new instance.
        projects.put(project, new UiProject(project));
    }

    public void removeUiProject(IScenarioProject project)
    {
        getUiProject(project).remove();
        projects.remove(project);
    }

    public void addUiElement(IScenarioElement element)
    {
        // add a new element to the corresponding UiProject.
        UiProject uiProject = getUiProject(element);
        uiProject.addUiElement(element);
    }

    public void updateUiElement(IScenarioElement element)
    {
        UiProject uiProject = getUiProject(element);
        uiProject.updateUiElement(element);
    }

    public void removeUiElement(IScenarioElement element)
    {
        UiProject uiProject = getUiProject(element);
        uiProject.removeUiElement(element);
    }
}
