package maru.map.views;

import maru.centralbody.projection.EquirectangularProjector;
import maru.centralbody.projection.ICoordinateProjector;
import maru.ui.model.UiProject;
import maru.ui.model.UiVisible;

public abstract class AbstractMapDrawer implements IMapDrawer
{
    public static final int DEFAULT_PROJECTOR_CACHE_SIZE = 1024 * 64;

    private final AbstractMapView parent;

    private MapViewParameters params;
    private MapViewSettings settings;

    // projector that converts from scenario element frames to pixel positions
    private ICoordinateProjector mapProjector;

    // the element currently selected in the scenario explorer (or null)
    private UiVisible selectedElement;

    public AbstractMapDrawer(AbstractMapView parent)
    {
        this.parent = parent;
        this.params = new MapViewParameters();
        this.settings = new MapViewSettings();
        this.mapProjector = new EquirectangularProjector();
        this.mapProjector.setCacheSize(DEFAULT_PROJECTOR_CACHE_SIZE);
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
    public void setParameters(MapViewParameters parameters)
    {
        this.params = parameters;
    }

    @Override
    public MapViewSettings getSettings()
    {
        return settings;
    }

    @Override
    public void setSettings(MapViewSettings settings)
    {
        this.settings = settings;
    }

    @Override
    public ICoordinateProjector getMapProjector()
    {
        return mapProjector;
    }

    @Override
    public void setMapProjector(ICoordinateProjector projector)
    {
        this.mapProjector = projector;
    }

    @Override
    public UiVisible getSelectedElement()
    {
        return selectedElement;
    }

    @Override
    public void setSelectedElement(UiVisible element)
    {
        this.selectedElement = element;
    }

    @Override
    public void draw(Object context)
    {
        updateContext(context);
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