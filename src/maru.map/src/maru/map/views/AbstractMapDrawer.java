package maru.map.views;

import javax.media.opengl.GLContext;

import maru.centralbody.model.projection.EquirectangularProjector;
import maru.centralbody.model.projection.ICoordinateProjector;
import maru.ui.model.UiProject;
import maru.ui.model.UiVisible;


public abstract class AbstractMapDrawer implements IMapDrawer
{
    public static final int DEFAULT_PROJECTOR_CACHE_SIZE = 1024 * 32;

    private final AbstractGLView parent;

    private MapViewParameters params;
    private MapViewSettings settings;

    // projector that converts from scenario element frames to pixel positions
    private ICoordinateProjector mapProjector;

    // the element currently selected in the scenario explorer (or null)
    private UiVisible selectedElement;

    public AbstractMapDrawer(AbstractGLView parent)
    {
        this.parent = parent;
        this.params = new MapViewParameters();
        this.settings = new MapViewSettings();
        this.mapProjector = new EquirectangularProjector();
        this.mapProjector.setCacheSize(DEFAULT_PROJECTOR_CACHE_SIZE);
    }

    public AbstractGLView getView()
    {
        return parent;
    }

    public void redraw()
    {
        getView().redraw();
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
    public void draw(GLContext context)
    {
        updateContext(context);
    }

    @Override
    public void draw(GLContext context, UiProject project)
    {
        updateContext(context, project);

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

    protected abstract void updateContext(GLContext context);
    protected abstract void updateContext(GLContext context, UiProject project);

    protected abstract void updateMapParameters(UiProject project);
    protected abstract void updateMapSettings(UiProject project);

    protected abstract void doProjectDrawJobs(UiProject project);
    protected abstract void doProjectAnimationJobs(UiProject project);
}