package maru.map.preferences;

// TODO: evaluate if these can be replaced by maru.core.units.DaylengthDefinition
class DaylightDefinitionChoices
{
    public static final String DEF_CHOICE_0_NAME = "Sun center even with horizon (0.0 deg)";
    public static final String DEF_CHOICE_0_VALUE = "0.0";

    public static final String DEF_CHOICE_1_NAME = "Sun top even with horizon (0.26667 deg)";
    public static final String DEF_CHOICE_1_VALUE = "0.26667";

    public static final String DEF_CHOICE_2_NAME = "Sun top apperantly even with horizon (0.8333 deg)";
    public static final String DEF_CHOICE_2_VALUE = "0.8333";

    public static final String DEF_CHOICE_3_NAME = "With civil twilight (6.0 deg)";
    public static final String DEF_CHOICE_3_VALUE = "6.0";

    public static final String DEF_CHOICE_4_NAME = "With nautical twilight (12.0 deg)";
    public static final String DEF_CHOICE_4_VALUE = "12.0";

    public static final String DEF_CHOICE_5_NAME = "With astronomical twilight (18.0 deg)";
    public static final String DEF_CHOICE_5_VALUE = "18.0";
}

public class PreferenceConstants
{
    public static final String P_MAP_ANTI_ALIASING = "mapAntiAliasingPreference";

    public static final String P_MAP_SHOW_UMBRA = "mapShowUmbraPreference";
    public static final String P_MAP_GROUNDTRACK_STEP_SIZE = "mapGroundtrackStepSizePreference";
    public static final String P_MAP_GROUNDTRACK_LENGTH = "mapGroundtrackLengthPreference";

    public static final String P_MAP_NIGHT = "mapNightPreference";
    public static final String P_MAP_NIGHT_STEPSIZE = "mapNightIndicatorStepSizePreference";

    public static final String P_MAP_LAT_LON_LINE_STEPSIZE = "mapLatLonLineStepSizePreference";

    public static final String P_MAP_DAYLENGTH_DEFINITION = "mapDaylengthDefinitionPreference";
}
