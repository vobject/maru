package maru.map.jobs.gl;

import maru.centralbody.projection.ICoordinateProjector;
import maru.map.jobs.IProjectDrawJob;
import maru.ui.model.UiProject;
import maru.ui.model.UiPropagatable;

public abstract class GLProjectDrawJob extends GLDrawJob implements IProjectDrawJob
{
    private UiProject project;
    private UiPropagatable selectedElement;
    private ICoordinateProjector projector;

    @Override
    public UiProject getProject()
    {
        return project;
    }

    @Override
    public void setProject(UiProject project)
    {
        this.project = project;
    }

    public UiPropagatable getSelectedElement()
    {
        return selectedElement;
    }

    public void setSelectedElement(UiPropagatable selectedElement)
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
