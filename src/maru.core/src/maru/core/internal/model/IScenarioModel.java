package maru.core.internal.model;

import java.util.Collection;

import maru.core.model.IParent;
import maru.core.model.IScenarioElement;
import maru.core.model.IScenarioProject;

public interface IScenarioModel extends IScenarioElement, IParent
{
    Collection<IScenarioProject> getScenarioProjects();
    IScenarioProject getScenarioProject(String name);
}
