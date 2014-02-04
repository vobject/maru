package maru.map.preferences;

import maru.core.utils.DaylengthDefinition;

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

public class PreferenceConstants
{
    public static final String P_MAP_ANTI_ALIASING = "mapAntiAliasingPreference";

    public static final String P_MAP_SHOW_UMBRA = "mapShowUmbraPreference";
    public static final String P_MAP_GROUNDTRACK_STEP_SIZE = "mapGroundtrackStepSizePreference";
    public static final String P_MAP_GROUNDTRACK_LENGTH = "mapGroundtrackLengthPreference";

    public static final String P_MAP_SHOW_VISIBILITY_CIRCLES = "mapShowVisibilityCirclesreference";
    public static final String P_MAP_SHOW_VISIBILITY_SC_TO_SC = "mapShowVisibilityScToScPreference";
    public static final String P_MAP_SHOW_VISIBILITY_SC_TO_GS = "mapShowVisibilityScToGsPreference";

    public static final String P_MAP_NIGHT = "mapNightPreference";
    public static final String P_MAP_NIGHT_STEPSIZE = "mapNightIndicatorStepSizePreference";

    public static final String P_MAP_LAT_LON_LINE_STEPSIZE = "mapLatLonLineStepSizePreference";

    public static final String P_MAP_DAYLENGTH_DEFINITION = "mapDaylengthDefinitionPreference";
}
