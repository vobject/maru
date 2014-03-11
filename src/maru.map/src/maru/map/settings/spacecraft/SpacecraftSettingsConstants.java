package maru.map.settings.spacecraft;

public final class SpacecraftSettingsConstants
{
    // all possible settings for a IGroundstation in the current plugin
    public static final String SHOW_ELEMENT_ICON = "showElementIcon";
    public static final String SHOW_ELEMENT_NAME = "showElementName";
    public static final String ELEMENT_ICON_SIZE = "elementIconSize";
    public static final String GROUNDTRACK_LENGTH = "groundtrackLength";
    public static final String GROUNDTRACK_LINE_WIDTH = "groundtrackLineWidth";

    // default values
    public static final boolean DEFAULT_SHOW_ELEMENT_ICON = true;
    public static final boolean DEFAULT_SHOW_ELEMENT_NAME = true;
    public static final long DEFAULT_ELEMENT_ICON_SIZE = 32L;
    public static final long DEFAULT_GROUNDTRACK_LENGTH = 2L * 60 * 60; // seconds
    public static final long DEFAULT_GROUNDTRACK_LINE_WIDTH = 2L;

    // description strings
    public static final String DESCRIPTION_SHOW_ELEMENT_ICON = "Show element icon";
    public static final String DESCRIPTION_SHOW_ELEMENT_NAME = "Show element name";
    public static final String DESCRIPTION_ELEMENT_ICON_SIZE = "Icon size in pixel";
    public static final String DESCRIPTION_GROUNDTRACK_LENGTH = "Ground track length in hours";
    public static final String DESCRIPTION_GROUNDTRACK_LINE_WIDTH = "Ground track line width in pixel";
}
