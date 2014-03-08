package maru.map.jobs.gl;

import maru.centralbody.model.projection.ICoordinateProjector;
import maru.map.jobs.IProjectDrawJob;
import maru.ui.model.UiProject;
import maru.ui.model.UiVisibleElement;

public abstract class GLProjectDrawJob extends GLDrawJob implements IProjectDrawJob
{
    private UiProject project;
    private UiVisibleElement selectedElement;
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

    public UiVisibleElement getSelectedElement()
    {
        return selectedElement;
    }

    public void setSelectedElement(UiVisibleElement selectedElement)
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
