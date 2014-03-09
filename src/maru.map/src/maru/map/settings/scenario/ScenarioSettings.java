package maru.map.settings.scenario;

import maru.core.model.IGroundstation;
import maru.core.model.IScenarioElement;
import maru.core.model.ISpacecraft;
import maru.core.model.utils.DaylengthDefinition;
import maru.map.settings.groundstation.GroundstationSettings;
import maru.map.settings.spacecraft.SpacecraftSettings;

import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

public class ScenarioSettings
{
    private Preferences node;
    private final Preferences groundstationNode;
    private final Preferences spacecraftNode;

    public ScenarioSettings(Preferences node)
    {
        this.node = node;
        this.groundstationNode = node.node("Groundstations");
        this.spacecraftNode = node.node("Spacecrafts");
    }

    public Preferences getNode()
    {
        return node;
    }

    public Preferences getGroundstationNode()
    {
        return groundstationNode;
    }

    public Preferences getSpacecraftNode()
    {
        return spacecraftNode;
    }

    public void remove()
    {
        try {
            node.removeNode();
        } catch (BackingStoreException e) {
            e.printStackTrace();
        } finally {
            node = null; // invalidate the current object
        }
    }

    public void removeElement(IScenarioElement element)
    {
        try
        {
            if (element instanceof IGroundstation) {
                groundstationNode.node(element.getElementName()).removeNode();
            } else if (element instanceof ISpacecraft) {
                spacecraftNode.node(element.getElementName()).removeNode();
            }
        }
        catch (BackingStoreException e)
        {
            e.printStackTrace();
        }
    }

    public GroundstationSettings getGroundstation(IGroundstation element)
    {
        return getGroundstation(element.getElementName());
    }

    public GroundstationSettings getGroundstation(String name)
    {
        Preferences node = groundstationNode.node(name);
        return new GroundstationSettings(node);
    }

    public SpacecraftSettings getSpacecraft(ISpacecraft element)
    {
        return getSpacecraft(element.getElementName());
    }

    public SpacecraftSettings getSpacecraft(String name)
    {
        Preferences node = spacecraftNode.node(name);
        return new SpacecraftSettings(node);
    }

    @Override
    public String toString()
    {
        return node.toString();
    }

    /** Whether or not to show visibility circles. */
    public boolean getShowVisibilityCircles()
    {
        return node.getBoolean(ScenarioSettingsConstants.SHOW_VISIBILITY_CIRCLES, ScenarioSettingsConstants.DEFAULT_SHOW_VISIBILITY_CIRCLES);
    }

    /** Whether or not to show visibility lines between spacecrafts. */
    public boolean getShowVisibilitySpacecraftToSpacecraft()
    {
        return node.getBoolean(ScenarioSettingsConstants.SHOW_VISIBILITY_SC_TO_SC, ScenarioSettingsConstants.DEFAULT_SHOW_VISIBILITY_SC_TO_SC);
    }

    /** Whether or not to show visibility lines between spacecrafts and ground stations. */
    public boolean getShowVisibilitySpacecraftToGroundstation()
    {
        return node.getBoolean(ScenarioSettingsConstants.SHOW_VISIBILITY_SC_TO_GS, ScenarioSettingsConstants.DEFAULT_SHOW_VISIBILITY_SC_TO_GS);
    }

    /** Whether or not to show when a spacecraft is in shadow when drawing its groundtrack. */
    public boolean getShowUmbraOnGroundtrack()
    {
        return node.getBoolean(ScenarioSettingsConstants.SHOW_UMBRA_ON_GROUNDTRACK, ScenarioSettingsConstants.DEFAULT_SHOW_UMBRA_ON_GROUNDTRACK);
    }

    /** The step size that the ground track is calculated with in seconds. */
    public long getGroundtrackStepSize()
    {
        return node.getLong(ScenarioSettingsConstants.GROUNDTRACK_STEP_SIZE, ScenarioSettingsConstants.DEFAULT_GROUNDTRACK_STEP_SIZE);
    }

    /** The length of the ground track in seconds. */
    public long getGroundtrackLength()
    {
        return node.getLong(ScenarioSettingsConstants.GROUNDTRACK_LENGTH, ScenarioSettingsConstants.DEFAULT_GROUNDTRACK_LENGTH);
    }

    /** The step size for which to show latitude/longitude in degree. */
    public long getLatLonStepSize()
    {
        return node.getLong(ScenarioSettingsConstants.LAT_LON_LINE_STEPSIZE, ScenarioSettingsConstants.DEFAULT_LAT_LON_LINE_STEPSIZE);
    }

    /** Whether or not to show the map in red color. */
    public boolean getShowNightMode()
    {
        return node.getBoolean(ScenarioSettingsConstants.SHOW_NIGHT_MODE, ScenarioSettingsConstants.DEFAULT_SHOW_NIGHT_MODE);
    }

    /** Whether or not to show the sun terminator. */
    public boolean getShowNightOverlay()
    {
        return node.getBoolean(ScenarioSettingsConstants.SHOW_NIGHT_OVERLAY, ScenarioSettingsConstants.DEFAULT_SHOW_NIGHT_OVERLAY);
    }

    /** The step size for which to calculate the sun terminator in pixel. */
    public long getNightStepSize()
    {
        return node.getLong(ScenarioSettingsConstants.NIGHT_OVERLAY_PIXELSTEPS, ScenarioSettingsConstants.DEFAULT_NIGHT_OVERLAY_PIXELSTEPS);
    }

    /** The definition of sunrise/sunset; affects the sun terminator. */
    public DaylengthDefinition getDaylengthDefinition()
    {
        return DaylengthDefinition.toDaylengthDefinition(node.get(ScenarioSettingsConstants.DAYLENGTH_DEFINITION, ScenarioSettingsConstants.DEFAULT_DAYLENGTH_DEFINITION));
    }

    public void setShowVisibilityCircles(boolean show)
    {
        node.putBoolean(ScenarioSettingsConstants.SHOW_VISIBILITY_CIRCLES, show);
    }

    public void setShowVisibilitySpacecraftToSpacecraft(boolean show)
    {
        node.putBoolean(ScenarioSettingsConstants.SHOW_VISIBILITY_SC_TO_SC, show);
    }

    public void setShowVisibilitySpacecraftToGroundstation(boolean show)
    {
        node.putBoolean(ScenarioSettingsConstants.SHOW_VISIBILITY_SC_TO_GS, show);
    }

    public void setShowUmbraOnGroundtrack(boolean show)
    {
        node.putBoolean(ScenarioSettingsConstants.SHOW_UMBRA_ON_GROUNDTRACK, show);
    }

    public void setGroundtrackStepSize(long stepSize)
    {
        node.putLong(ScenarioSettingsConstants.GROUNDTRACK_STEP_SIZE, stepSize);
    }

    public void setGroundtrackLength(long length)
    {
        node.putLong(ScenarioSettingsConstants.GROUNDTRACK_LENGTH, length);
    }

    public void setLatLonLinesStepSize(long stepSize)
    {
        node.putLong(ScenarioSettingsConstants.LAT_LON_LINE_STEPSIZE, stepSize);
    }

    public void setShowNightMode(boolean show)
    {
        node.putBoolean(ScenarioSettingsConstants.SHOW_NIGHT_MODE, show);
    }

    public void setShowNightOverlay(boolean show)
    {
        node.putBoolean(ScenarioSettingsConstants.SHOW_NIGHT_OVERLAY, show);
    }

    public void setNightOverlayPixelSteps(long stepSize)
    {
        node.putLong(ScenarioSettingsConstants.NIGHT_OVERLAY_PIXELSTEPS, stepSize);
    }

    public void setDaylengthDefinition(DaylengthDefinition definition)
    {
        node.put(ScenarioSettingsConstants.DAYLENGTH_DEFINITION, Double.toString(definition.getValue()));
    }
}
