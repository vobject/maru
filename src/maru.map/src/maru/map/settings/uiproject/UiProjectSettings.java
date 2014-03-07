package maru.map.settings.uiproject;

import maru.core.model.utils.DaylengthDefinition;

import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

public class UiProjectSettings
{
    private final Preferences prjParent;
    private Preferences prjNode;
    private String prjName;

    public UiProjectSettings(Preferences uiProjectParent, Preferences uiProjectNode, String uiProjectName)
    {
        this.prjParent = uiProjectParent;
        this.prjNode = uiProjectNode;
        this.prjName = uiProjectName;
    }

    public boolean exists()
    {
        return prjNode != null;
    }

    public void initialize()
    {
        if (!exists()) {
            prjNode = prjParent.node(prjName);
        }

        // initialize settings it with default values
        UiProjectSettingsInitializer.initialize(prjName, prjNode);
    }

    public void remove()
    {
        try
        {
            if (exists()) {
                prjNode.removeNode();
            }
        }
        catch (BackingStoreException e)
        {
            e.printStackTrace();
        }
        finally
        {
            // invalidate the current object
            prjNode = null;
            prjName = null;
        }
    }

    @Override
    public String toString()
    {
        if (exists()) {
            return prjNode.toString();
        } else {
            return null;
        }
    }

    /** Whether or not to show visibility circles. */
    public boolean getShowVisibilityCircles()
    {
        return prjNode.getBoolean(UiProjectSettingsConstants.SHOW_VISIBILITY_CIRCLES, UiProjectSettingsConstants.DEFAULT_SHOW_VISIBILITY_CIRCLES);
    }

    /** Whether or not to show visibility lines between spacecrafts. */
    public boolean getShowVisibilitySpacecraftToSpacecraft()
    {
        return prjNode.getBoolean(UiProjectSettingsConstants.SHOW_VISIBILITY_SC_TO_SC, UiProjectSettingsConstants.DEFAULT_SHOW_VISIBILITY_SC_TO_SC);
    }

    /** Whether or not to show visibility lines between spacecrafts and ground stations. */
    public boolean getShowVisibilitySpacecraftToGroundstation()
    {
        return prjNode.getBoolean(UiProjectSettingsConstants.SHOW_VISIBILITY_SC_TO_GS, UiProjectSettingsConstants.DEFAULT_SHOW_VISIBILITY_SC_TO_GS);
    }

    /** Whether or not to show when a spacecraft is in shadow when drawing its groundtrack. */
    public boolean getShowUmbraOnGroundtrack()
    {
        return prjNode.getBoolean(UiProjectSettingsConstants.SHOW_UMBRA_ON_GROUNDTRACK, UiProjectSettingsConstants.DEFAULT_SHOW_UMBRA_ON_GROUNDTRACK);
    }

    /** The step size that the ground track is calculated with in seconds. */
    public long getGroundtrackStepSize()
    {
        return prjNode.getLong(UiProjectSettingsConstants.GROUNDTRACK_STEP_SIZE, UiProjectSettingsConstants.DEFAULT_GROUNDTRACK_STEP_SIZE);
    }

    /** The length of the ground track in seconds. */
    public long getGroundtrackLength()
    {
        return prjNode.getLong(UiProjectSettingsConstants.GROUNDTRACK_LENGTH, UiProjectSettingsConstants.DEFAULT_GROUNDTRACK_LENGTH);
    }

    /** The step size for which to show latitude/longitude in degree. */
    public long getLatLonStepSize()
    {
        return prjNode.getLong(UiProjectSettingsConstants.LAT_LON_LINE_STEPSIZE, UiProjectSettingsConstants.DEFAULT_LAT_LON_LINE_STEPSIZE);
    }

    /** Whether or not to show the map in red color. */
    public boolean getShowNightMode()
    {
        return prjNode.getBoolean(UiProjectSettingsConstants.SHOW_NIGHT_MODE, UiProjectSettingsConstants.DEFAULT_SHOW_NIGHT_MODE);
    }

    /** Whether or not to show the sun terminator. */
    public boolean getShowNightOverlay()
    {
        return prjNode.getBoolean(UiProjectSettingsConstants.SHOW_NIGHT_OVERLAY, UiProjectSettingsConstants.DEFAULT_SHOW_NIGHT_OVERLAY);
    }

    /** The step size for which to calculate the sun terminator in pixel. */
    public long getNightStepSize()
    {
        return prjNode.getLong(UiProjectSettingsConstants.NIGHT_OVERLAY_PIXELSTEPS, UiProjectSettingsConstants.DEFAULT_NIGHT_OVERLAY_PIXELSTEPS);
    }

    /** The definition of sunrise/sunset; affects the sun terminator. */
    public DaylengthDefinition getDaylengthDefinition()
    {
        return DaylengthDefinition.toDaylengthDefinition(prjNode.get(UiProjectSettingsConstants.DAYLENGTH_DEFINITION, UiProjectSettingsConstants.DEFAULT_DAYLENGTH_DEFINITION));
    }

    public void setShowVisibilityCircles(boolean show)
    {
        prjNode.putBoolean(UiProjectSettingsConstants.SHOW_VISIBILITY_CIRCLES, show);
    }

    public void sethowVisibilitySpacecraftToSpacecraft(boolean show)
    {
        prjNode.putBoolean(UiProjectSettingsConstants.SHOW_VISIBILITY_SC_TO_SC, show);
    }

    public void setShowVisibilitySpacecraftToGroundstation(boolean show)
    {
        prjNode.putBoolean(UiProjectSettingsConstants.SHOW_VISIBILITY_SC_TO_GS, show);
    }

    public void setShowUmbraOnGroundtrack(boolean show)
    {
        prjNode.putBoolean(UiProjectSettingsConstants.SHOW_UMBRA_ON_GROUNDTRACK, show);
    }

    public void setGroundtrackStepSize(long stepSize)
    {
        prjNode.putLong(UiProjectSettingsConstants.GROUNDTRACK_STEP_SIZE, stepSize);
    }

    public void setGroundtrackLength(long length)
    {
        prjNode.putLong(UiProjectSettingsConstants.GROUNDTRACK_LENGTH, length);
    }

    public void setLatLonLinesStepSize(long stepSize)
    {
        prjNode.putLong(UiProjectSettingsConstants.LAT_LON_LINE_STEPSIZE, stepSize);
    }

    public void setShowNightMode(boolean show)
    {
        prjNode.putBoolean(UiProjectSettingsConstants.SHOW_NIGHT_MODE, show);
    }

    public void setShowNightOverlay(boolean show)
    {
        prjNode.putBoolean(UiProjectSettingsConstants.SHOW_NIGHT_OVERLAY, show);
    }

    public void setNightOverlayPixelSteps(long stepSize)
    {
        prjNode.putLong(UiProjectSettingsConstants.NIGHT_OVERLAY_PIXELSTEPS, stepSize);
    }

    public void setDaylengthDefinition(DaylengthDefinition definition)
    {
        prjNode.put(UiProjectSettingsConstants.DAYLENGTH_DEFINITION, Double.toString(definition.getValue()));
    }
}
