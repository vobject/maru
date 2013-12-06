package maru.map.jobs;

import maru.map.views.MapViewParameters;
import maru.map.views.MapViewSettings;

public abstract class DrawJob implements IDrawJob
{
    private MapViewParameters areaSettings;
    private MapViewSettings drawSettings;

    public MapViewParameters getParameters()
    {
        return areaSettings;
    }

    public void setMapAreaSettings(MapViewParameters areaSettings)
    {
        this.areaSettings = areaSettings;
    }

    public MapViewSettings getSettings()
    {
        return drawSettings;
    }

    public void setMapDrawSettings(MapViewSettings drawSettings)
    {
        this.drawSettings = drawSettings;
    }
}
