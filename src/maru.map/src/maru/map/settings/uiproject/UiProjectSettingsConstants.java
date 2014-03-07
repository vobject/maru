package maru.map.settings.uiproject;

import maru.core.model.utils.DaylengthDefinition;

class DaylightDefinitionChoices
{
    public static final String DEF_CHOICE_0_NAME = "Sun center even with horizon (0.0 deg)";
    public static final String DEF_CHOICE_0_VALUE = Double.toString(DaylengthDefinition.SUN_CENTER_EVEN_WITH_HORIZON.getValue());

    public static final String DEF_CHOICE_1_NAME = "Sun top even with horizon (0.26667 deg)";
    public static final String DEF_CHOICE_1_VALUE = Double.toString(DaylengthDefinition.SUN_TOP_EVEN_WITH_HORIZON.getValue());

    public static final String DEF_CHOICE_2_NAME = "Sun top apperantly even with horizon (0.8333 deg)";
    public static final String DEF_CHOICE_2_VALUE = Double.toString(DaylengthDefinition.SUN_TOP_APPERANTLY_EVEN_WITH_HORIZON.getValue());

    public static final String DEF_CHOICE_3_NAME = "With civil twilight (6.0 deg)";
    public static final String DEF_CHOICE_3_VALUE = Double.toString(DaylengthDefinition.WITH_CIVIL_TWILIGHT.getValue());

    public static final String DEF_CHOICE_4_NAME = "With nautical twilight (12.0 deg)";
    public static final String DEF_CHOICE_4_VALUE = Double.toString(DaylengthDefinition.WITH_NAUTICAL_TWILIGHT.getValue());

    public static final String DEF_CHOICE_5_NAME = "With astronomical twilight (18.0 deg)";
    public static final String DEF_CHOICE_5_VALUE = Double.toString(DaylengthDefinition.WITH_ASTRONOMICAL_TWILIGHT.getValue());
}

public final class UiProjectSettingsConstants
{
    // all possible settings for a UiProject in the current plugin
    public static final String SHOW_VISIBILITY_CIRCLES = "showVisibilityCircles";
    public static final String SHOW_VISIBILITY_SC_TO_SC = "showVisibilityScToSc";
    public static final String SHOW_VISIBILITY_SC_TO_GS = "showVisibilityScToGs";
    public static final String SHOW_UMBRA_ON_GROUNDTRACK = "showUmbraOnGroundtrack";
    public static final String GROUNDTRACK_STEP_SIZE = "groundtrackStepSize";
    public static final String GROUNDTRACK_LENGTH = "groundtrackLength";
    public static final String LAT_LON_LINE_STEPSIZE = "latLonLinesStepSize";
    public static final String SHOW_NIGHT_MODE = "showNightMode";
    public static final String SHOW_NIGHT_OVERLAY = "showNightOverlay";
    public static final String NIGHT_OVERLAY_PIXELSTEPS = "nightOverlayPixelSteps";
    public static final String DAYLENGTH_DEFINITION = "daylengthDefinition";

    // default values
    public static final boolean DEFAULT_SHOW_VISIBILITY_CIRCLES = false;
    public static final boolean DEFAULT_SHOW_VISIBILITY_SC_TO_SC = true;
    public static final boolean DEFAULT_SHOW_VISIBILITY_SC_TO_GS = true;
    public static final boolean DEFAULT_SHOW_UMBRA_ON_GROUNDTRACK = true;
    public static final long DEFAULT_GROUNDTRACK_STEP_SIZE = 90L;
    public static final long DEFAULT_GROUNDTRACK_LENGTH = 2L * 60 * 60; // seconds
    public static final long DEFAULT_LAT_LON_LINE_STEPSIZE = 30L;
    public static final boolean DEFAULT_SHOW_NIGHT_MODE = false;
    public static final boolean DEFAULT_SHOW_NIGHT_OVERLAY = true;
    public static final long DEFAULT_NIGHT_OVERLAY_PIXELSTEPS = 2L;
    public static final String DEFAULT_DAYLENGTH_DEFINITION = DaylightDefinitionChoices.DEF_CHOICE_2_VALUE;

    // description strings
    public static final String DESCRIPTION_SHOW_VISIBILITY_CIRCLES = "Show visibility circles (buggy around poles)";
    public static final String DESCRIPTION_SHOW_VISIBILITY_SC_TO_SC = "Show visibility lines between spacecrafts";
    public static final String DESCRIPTION_SHOW_VISIBILITY_SC_TO_GS = "Show visibility lines between spacecrafts and ground stations";
    public static final String DESCRIPTION_SHOW_UMBRA_ON_GROUNDTRACK = "Show umbra on ground tracks";
    public static final String DESCRIPTION_GROUNDTRACK_STEP_SIZE = "Ground track step size in seconds";
    public static final String DESCRIPTION_GROUNDTRACK_LENGTH = "Ground track length in hours";
    public static final String DESCRIPTION_LAT_LON_LINE_STEPSIZE = "Latitude/Longitude lines step size in degree";
    public static final String DESCRIPTION_SHOW_NIGHT_MODE = "Enable night mode";
    public static final String DESCRIPTION_SHOW_NIGHT_OVERLAY = "Show night overlay on map";
    public static final String DESCRIPTION_NIGHT_OVERLAY_PIXELSTEPS = "Night overlay step size in pixels";
    public static final String DESCRIPTION_DAYLENGTH_DEFINITION = "Day light definition";
}
