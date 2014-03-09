package maru.map.jobs.gl;

import maru.centralbody.model.projection.ICoordinateProjector;
import maru.core.model.IScenarioProject;
import maru.core.model.IVisibleElement;
import maru.map.jobs.IProjectDrawJob;

public abstract class GLProjectDrawJob extends GLDrawJob implements IProjectDrawJob
{
    private IScenarioProject scenario;
    private IVisibleElement selectedElement;
    private ICoordinateProjector projector;

    @Override
    public IScenarioProject getScenario()
    {
        return scenario;
    }

    @Override
    public void setScenario(IScenarioProject scenario)
    {
        this.scenario = scenario;
    }

    public IVisibleElement getSelectedElement()
    {
        return selectedElement;
    }

    public void setSelectedElement(IVisibleElement selectedElement)
    {
        this.selectedElement = selectedElement;
    }

    public ICoordinateProjector getProjector()
    {
        return projector;
    }

    public void setProjector(ICoordinateProjector projector)
    {
        this.projector = projector;
    }
}
