package maru.map.jobs;

import maru.core.model.IGroundstation;
import maru.core.model.ISpacecraft;
import maru.map.MaruMapPlugin;
import maru.map.settings.groundstation.GroundstationSettings;
import maru.map.settings.scenario.ScenarioSettings;
import maru.map.settings.spacecraft.SpacecraftSettings;
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

    public ScenarioSettings getScenarioSettings()
    {
        return MaruMapPlugin.getDefault().getScenarioModelSettings().getCurrentScenario();
    }

    public GroundstationSettings getGroundstationSettings(IGroundstation element)
    {
        return getScenarioSettings().getGroundstation(element);
    }

    public SpacecraftSettings getSpacecraftSettings(ISpacecraft element)
    {
        return getScenarioSettings().getSpacecraft(element);
    }
}
