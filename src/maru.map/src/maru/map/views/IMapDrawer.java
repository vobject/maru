package maru.map.views;

import javax.media.opengl.GLContext;

import maru.centralbody.model.projection.ICoordinateProjector;
import maru.core.model.IScenarioProject;
import maru.core.model.IVisibleElement;

public interface IMapDrawer
{
    MapViewParameters getParameters();
    void setParameters(MapViewParameters mapAreaSettings);

    MapViewSettings getSettings();
    void setSettings(MapViewSettings mapDrawSettings);

    ICoordinateProjector getMapProjector();
    void setMapProjector(ICoordinateProjector projector);

    IVisibleElement getSelectedElement();
    void setSelectedElement(IVisibleElement element);

    void draw(GLContext context);
    void draw(GLContext context, IScenarioProject project);
    void dispose();

    void mouseEvent(int btn, int mask, int count, int x, int y);
}
