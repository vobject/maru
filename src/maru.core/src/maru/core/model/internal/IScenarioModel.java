package maru.core.model.internal;

import java.util.List;

import maru.core.model.IParent;
import maru.core.model.IScenarioElement;
import maru.core.model.IScenarioProject;

public interface IScenarioModel extends IScenarioElement, IParent
{
    List<IScenarioProject> getScenarioProjects();
    IScenarioProject getScenarioProject(String name);

    void addScenarioProject(IScenarioProject project);
    void removeScenarioProject(IScenarioProject project);
}
