package maru.map.jobs;

import maru.core.model.IScenarioProject;

public interface IProjectDrawJob extends IDrawJob
{
    IScenarioProject getScenario();
    void setScenario(IScenarioProject project);
}
