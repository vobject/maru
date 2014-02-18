package maru.map.views;

import javax.media.opengl.GLContext;

import maru.centralbody.projection.ICoordinateProjector;
import maru.ui.model.UiProject;
import maru.ui.model.UiVisible;

public interface IMapDrawer
{
    MapViewParameters getParameters();
    void setParameters(MapViewParameters mapAreaSettings);

    MapViewSettings getSettings();
    void setSettings(MapViewSettings mapDrawSettings);

    ICoordinateProjector getMapProjector();
    void setMapProjector(ICoordinateProjector projector);

    UiVisible getSelectedElement();
    void setSelectedElement(UiVisible element);

    void draw(GLContext context);
    void draw(GLContext context, UiProject project);
    void dispose();

    void mouseEvent(int btn, int mask, int count, int x, int y);
}
