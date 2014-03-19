package maru.core.model.internal;

import java.util.List;

import maru.core.model.IScenarioProject;

public class ScenarioModel extends Parent implements IScenarioModel
{
    private static final long serialVersionUID = 1L;

    public ScenarioModel()
    {
        super("ScenarioModel", true);
    }

    @Override
    public List<IScenarioProject> getScenarioProjects()
    {
        return Parent.<IScenarioProject>castList(getChildren());
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

    @Override
    public void addScenarioProject(IScenarioProject project)
    {
        addChild(project);
    }

    @Override
    public void removeScenarioProject(IScenarioProject project)
    {
        removeChild(project);
    }
}
