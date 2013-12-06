package maru.core.internal.model;

import java.util.Collection;

import maru.core.model.IScenarioProject;

public class ScenarioModel extends Parent implements IScenarioModel
{
    private static final long serialVersionUID = 1L;

    public ScenarioModel()
    {
        super("ScenarioModel", true);
    }

    @Override
    public Collection<IScenarioProject> getScenarioProjects()
    {
        return this.<IScenarioProject>castCollection(getChildren());
    }

    @Override
    public IScenarioProject getScenarioProject(String name)
    {
        for (IScenarioProject project : getScenarioProjects())
        {
            if (project.getElementName().equals(name)) {
                return project;
            }
        }
        return null;
    }

    public void addScenarioProject(IScenarioProject project)
    {
        addChild(project);
    }

    public void removeScenarioProject(IScenarioProject project)
    {
        removeChild(project);
    }
}
