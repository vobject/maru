package maru.map.views;

import javax.media.opengl.GLContext;

import maru.centralbody.model.projection.EquirectangularProjector;
import maru.centralbody.model.projection.ICoordinateProjector;
import maru.core.model.IScenarioProject;
import maru.core.model.IVisibleElement;

interface IFontChangedListener {
    void fontChanged();
}

public abstract class AbstractMapDrawer implements IMapDrawer, IFontChangedListener
{
    public static final int DEFAULT_PROJECTOR_CACHE_SIZE = 1024 * 32;

    private final AbstractGLView parent;

    private final MapViewParameters params;
    private final MapViewSettings settings;

    /** projector that converts from scenario element frames to pixel positions. */
    private ICoordinateProjector mapProjector;

    /** the element currently selected in the scenario explorer (or null). */
    private IVisibleElement selectedElement;

    public AbstractMapDrawer(AbstractGLView parent)
    {
        this.parent = parent;
        this.params = new MapViewParameters();
        this.settings = new MapViewSettings(this);
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
    public MapViewSettings getSettings()
    {
        return settings;
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
    public IVisibleElement getSelectedElement()
    {
        return selectedElement;
    }

    @Override
    public void setSelectedElement(IVisibleElement element)
    {
        this.selectedElement = element;
    }

    @Override
    public void draw(GLContext context)
    {
        updateContext(context);
    }

    @Override
    public void draw(GLContext context, IScenarioProject project)
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
    protected abstract void updateContext(GLContext context, IScenarioProject project);

    protected abstract void updateMapParameters(IScenarioProject project);
    protected abstract void updateMapSettings(IScenarioProject project);

    protected abstract void doProjectDrawJobs(IScenarioProject project);
    protected abstract void doProjectAnimationJobs(IScenarioProject project);
}