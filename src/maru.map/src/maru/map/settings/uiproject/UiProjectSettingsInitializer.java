package maru.map.settings.uiproject;

import org.osgi.service.prefs.Preferences;

public final class UiProjectSettingsInitializer
{
    public static void initialize(String prjName, Preferences prjNode)
    {
        prjNode.putBoolean(UiProjectSettingsConstants.SHOW_VISIBILITY_CIRCLES, UiProjectSettingsConstants.DEFAULT_SHOW_VISIBILITY_CIRCLES);
        prjNode.putBoolean(UiProjectSettingsConstants.SHOW_VISIBILITY_SC_TO_SC, UiProjectSettingsConstants.DEFAULT_SHOW_VISIBILITY_SC_TO_SC);
        prjNode.putBoolean(UiProjectSettingsConstants.SHOW_VISIBILITY_SC_TO_GS, UiProjectSettingsConstants.DEFAULT_SHOW_VISIBILITY_SC_TO_GS);

        prjNode.putBoolean(UiProjectSettingsConstants.SHOW_UMBRA_ON_GROUNDTRACK, UiProjectSettingsConstants.DEFAULT_SHOW_UMBRA_ON_GROUNDTRACK);
        prjNode.putLong(UiProjectSettingsConstants.GROUNDTRACK_STEP_SIZE, UiProjectSettingsConstants.DEFAULT_GROUNDTRACK_STEP_SIZE);
        prjNode.putLong(UiProjectSettingsConstants.GROUNDTRACK_LENGTH, UiProjectSettingsConstants.DEFAULT_GROUNDTRACK_LENGTH);

        prjNode.putLong(UiProjectSettingsConstants.LAT_LON_LINE_STEPSIZE, UiProjectSettingsConstants.DEFAULT_LAT_LON_LINE_STEPSIZE);

        prjNode.putBoolean(UiProjectSettingsConstants.SHOW_NIGHT_MODE, UiProjectSettingsConstants.DEFAULT_SHOW_NIGHT_MODE);
        prjNode.putBoolean(UiProjectSettingsConstants.SHOW_NIGHT_OVERLAY, UiProjectSettingsConstants.DEFAULT_SHOW_NIGHT_OVERLAY);
        prjNode.putLong(UiProjectSettingsConstants.NIGHT_OVERLAY_PIXELSTEPS, UiProjectSettingsConstants.DEFAULT_NIGHT_OVERLAY_PIXELSTEPS);

        prjNode.put(UiProjectSettingsConstants.DAYLENGTH_DEFINITION, UiProjectSettingsConstants.DEFAULT_DAYLENGTH_DEFINITION);
    }
}
