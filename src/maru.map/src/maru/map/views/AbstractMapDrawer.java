package maru.map.views;

import maru.centralbody.projection.ICoordinateProjector;
import maru.ui.model.UiProject;
import maru.ui.model.UiPropagatable;

public abstract class AbstractMapDrawer implements IMapDrawer
{
    private final AbstractMapView parent;

    private ICoordinateProjector projector;
    private UiPropagatable selectedElement;

    private MapViewParameters params;
    private MapViewSettings settings;

    public AbstractMapDrawer(AbstractMapView parent)
    {
        this.parent = parent;
        this.params = new MapViewParameters();
        this.settings = new MapViewSettings();
    }

    public AbstractMapView getParent()
    {
        return parent;
    }

    public void redraw()
    {
        getParent().redraw();
    }

    @Override
    public void dispose()
    {

    }

    @Override
    public MapViewParameters getParameters()
    {
        return params;
    }

    @Override
    public void setParameters(MapViewParameters mapViewParameters)
    {
        this.params = mapViewParameters;
    }

    @Override
    public MapViewSettings getSettings()
    {
        return settings;
    }

    @Override
    public void setSettings(MapViewSettings mapViewSettings)
    {
        this.settings = mapViewSettings;
    }

    @Override
    public ICoordinateProjector getProjector()
    {
        return projector;
    }

    @Override
    public void setProjector(ICoordinateProjector projector)
    {
        this.projector = projector;
    }

    @Override
    public UiPropagatable getSelectedElement()
    {
        return selectedElement;
    }

    @Override
    public void setSelectedElement(UiPropagatable element)
    {
        this.selectedElement = element;
    }

    @Override
    public void draw(Object context, UiProject project)
    {
        updateContext(context);

        if (params.getSettingsChanged()) {
            updateMapParameters(project);
        }

        if (settings.getSettingsChanged()) {
            updateMapSettings(project);
        }

        doProjectDrawJobs(project);
        doProjectAnimationJobs(project);
    }

    @Override
    public void mouseEvent(int btn, int mask, int count, int x, int y)
    {

    }

    protected abstract void updateContext(Object context);
    protected abstract void updateMapParameters(UiProject project);
    protected abstract void updateMapSettings(UiProject project);

    protected abstract void doProjectDrawJobs(UiProject project);
    protected abstract void doProjectAnimationJobs(UiProject project);
}