package maru.map.views;

import maru.centralbody.projection.ICoordinateProjector;
import maru.ui.model.UiProject;
import maru.ui.model.UiPropagatable;

public interface IMapDrawer
{
    MapViewParameters getParameters();
    void setParameters(MapViewParameters mapAreaSettings);

    MapViewSettings getSettings();
    void setSettings(MapViewSettings mapDrawSettings);

    ICoordinateProjector getMapProjector();
    void setMapProjector(ICoordinateProjector projector);

    UiPropagatable getSelectedElement();
    void setSelectedElement(UiPropagatable element);

    void draw(Object context);
    void draw(Object context, UiProject project);
    void dispose();

    void mouseEvent(int btn, int mask, int count, int x, int y);
}
