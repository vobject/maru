package maru.map.views;

import maru.map.MaruMapPlugin;
import maru.map.preferences.MapPreferenceConstants;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

/**
 * This class wraps the preference store entries for the map.
 *
 * It keeps itself up to date by listening to changes in the
 * preference store.
 */
public class MapViewSettings
{
    private final IFontChangedListener fontListener;

    /** Reference to the plugin's preference store. */
    private IPreferenceStore preferenceStore;

    /** Indicates that one of the values has changed. */
    private boolean settingsChanged;

    /** Whether or not to use anti-aliasing when drawing. */
    private boolean antiAliasing;

    /** Whether or not to outline icons on the map. */
    private boolean outlineIcons;

    /** Whether or not to outline text on the map. */
    private boolean outlineText;

    /** The font to use for text on the map view. */
    private String fontName;

    /** The font size to use for text on the map view. */
    private int fontSize;

    /** The step size that the ground track is calculated with in seconds. */
    private int groundtrackSetpSize;

    /** Number of points to use for drawing visibility circles around elements. */
    private int visibilityCirclePoints;

    /** The step size for which to show latitude/longitude lines in degree. */
    private int latLonStepSize;

    /** The opacity of the night overlay in percent. */
    private int nightOverlayOpacity;

    /** The step size for which to calculate the sun terminator in pixel. */
    private int nightOverlayStepSize;

    /**
     * Initializes its values with the plugin's preferences values and
     * starts to listen to the plugin's preference changes.
     */
    public MapViewSettings(IFontChangedListener fontListener)
    {
        initPreferenceStore();
        initPreferenceStoreListener();

        this.fontListener = fontListener;
    }

    /**
     * Check if the plugin's preferences have changed.
     *
     * @return true when changes were made, false otherwise.
     */
    public boolean getSettingsChanged()
    {
        return settingsChanged;
    }

    public void setSettingsChanged(boolean changed)
    {
        settingsChanged = changed;
    }

    /**
     * Tell the object to reset its 'changed' flag.
     */
    public void update()
    {
        setSettingsChanged(false);
    }

    /** {@link #antiAliasing} */
    public boolean getAntiAliasing()
    {
        return antiAliasing;
    }

    /** {@link #outlineIcons} */
    public boolean getOutlineIcons()
    {
        return outlineIcons;
    }

    /** {@link #outlineText} */
    public boolean getOutlineText()
    {
        return outlineText;
    }

    /** {@link #fontName} */
    public String getFontName()
    {
        return fontName;
    }

    /** {@link #fontSize} */
    public int getFontSize()
    {
        return fontSize;
    }

    /** {@link #groundtrackSetpSize} */
    public int getGroundtrackStepSize()
    {
        return groundtrackSetpSize;
    }

    /** {@link #visibilityCirclePoints} */
    public int getVisibilityCirclePoints()
    {
        return visibilityCirclePoints;
    }

    /** {@link #latLonStepSize} */
    public int getLatLonStepSize()
    {
        return latLonStepSize;
    }

    /** {@link #nightOverlayOpacity} */
    public int getNightOverlayOpacity()
    {
        return nightOverlayOpacity;
    }

    /** {@link #nightOverlayStepSize} */
    public int getNightOverlayStepSize()
    {
        return nightOverlayStepSize;
    }

    private void initPreferenceStore()
    {
        preferenceStore = MaruMapPlugin.getDefault().getPreferenceStore();

        antiAliasing = preferenceStore.getBoolean(MapPreferenceConstants.P_MAP_ANTI_ALIASING);
        outlineIcons = preferenceStore.getBoolean(MapPreferenceConstants.P_MAP_OUTLINE_ICONS);
        outlineText = preferenceStore.getBoolean(MapPreferenceConstants.P_MAP_OUTLINE_TEXT);
        fontName = preferenceStore.getString(MapPreferenceConstants.P_MAP_FONT);
        fontSize = (int) preferenceStore.getLong(MapPreferenceConstants.P_MAP_FONT_SIZE);
        groundtrackSetpSize = (int) preferenceStore.getLong(MapPreferenceConstants.P_MAP_GROUNDTRACK_STEP_SIZE);
        visibilityCirclePoints = (int) preferenceStore.getLong(MapPreferenceConstants.P_MAP_VISIBILITY_CIRCLE_POINTS);
        latLonStepSize = (int) preferenceStore.getLong(MapPreferenceConstants.P_MAP_LAT_LON_LINE_STEPSIZE);
        nightOverlayOpacity = (int) preferenceStore.getLong(MapPreferenceConstants.P_MAP_NIGHT_OVERLAY_OPACITY);
        nightOverlayStepSize = (int) preferenceStore.getLong(MapPreferenceConstants.P_MAP_NIGHT_OVERLAY_PIXELSTEPS);

        setSettingsChanged(true);
    }

    private void initPreferenceStoreListener()
    {
        preferenceStore.addPropertyChangeListener(new IPropertyChangeListener()
        {
            @Override
            public void propertyChange(PropertyChangeEvent event)
            {
                String changedProperty = event.getProperty();

                if (changedProperty.equals(MapPreferenceConstants.P_MAP_ANTI_ALIASING)) {
                    // stupid Eclipse sometimes provides a String from event.getNewValue()
                    // when "Restore Defaults" is clicked.
                    Object newValue = event.getNewValue();
                    if (newValue instanceof String) {
                        antiAliasing = Boolean.parseBoolean((String) newValue);
                    } else {
                        // default case
                        antiAliasing = (boolean) newValue;
                    }
                } else if (changedProperty.equals(MapPreferenceConstants.P_MAP_OUTLINE_ICONS)) {
                    Object newValue = event.getNewValue();
                    if (newValue instanceof String) {
                        outlineIcons = Boolean.parseBoolean((String) newValue);
                    } else {
                        outlineIcons = (boolean) newValue;
                    }
                } else if (changedProperty.equals(MapPreferenceConstants.P_MAP_OUTLINE_TEXT)) {
                    Object newValue = event.getNewValue();
                    if (newValue instanceof String) {
                        outlineText = Boolean.parseBoolean((String) newValue);
                    } else {
                        outlineText = (boolean) newValue;
                    }
                } else if (changedProperty.equals(MapPreferenceConstants.P_MAP_FONT)) {
                    fontName = (String) event.getNewValue();
                    fontListener.fontChanged();
                } else if (changedProperty.equals(MapPreferenceConstants.P_MAP_FONT_SIZE)) {
                    fontSize = (int) event.getNewValue();
                    fontListener.fontChanged();
                } else if (changedProperty.equals(MapPreferenceConstants.P_MAP_GROUNDTRACK_STEP_SIZE)) {
                    groundtrackSetpSize = (int) event.getNewValue();
                } else if (changedProperty.equals(MapPreferenceConstants.P_MAP_VISIBILITY_CIRCLE_POINTS)) {
                    visibilityCirclePoints = (int) event.getNewValue();
                } else if (changedProperty.equals(MapPreferenceConstants.P_MAP_LAT_LON_LINE_STEPSIZE)) {
                    latLonStepSize = (int) event.getNewValue();
                } else if (changedProperty.equals(MapPreferenceConstants.P_MAP_NIGHT_OVERLAY_OPACITY)) {
                    nightOverlayOpacity = (int) event.getNewValue();
                } else if (changedProperty.equals(MapPreferenceConstants.P_MAP_NIGHT_OVERLAY_PIXELSTEPS)) {
                    nightOverlayStepSize = (int) event.getNewValue();
                } else {
                    return;
                }
                setSettingsChanged(true);
                MaruMapPlugin.getDefault().redraw();
            }
        });
    }
}