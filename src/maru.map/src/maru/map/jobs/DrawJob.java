package maru.map.jobs;

import maru.map.MaruMapPlugin;
import maru.map.settings.uiproject.UiProjectSettings;
import maru.map.views.MapViewParameters;
import maru.map.views.MapViewSettings;

public abstract class DrawJob implements IDrawJob
{
    private MapViewParameters mapParameters;
    private MapViewSettings mapSettings;

    public MapViewParameters getMapParameters()
    {
        return mapParameters;
    }

    public void setMapParameters(MapViewParameters mapParameters)
    {
        this.mapParameters = mapParameters;
    }

    public MapViewSettings getMapSettings()
    {
        return mapSettings;
    }

    public void setMapSettings(MapViewSettings mapSettings)
    {
        this.mapSettings = mapSettings;
    }

    public UiProjectSettings getUiProjectSettings()
    {
        return MaruMapPlugin.getDefault().getUiProjectsSettings().getCurrentProject();
    }
}
