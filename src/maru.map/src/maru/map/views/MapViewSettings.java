package maru.map.views;

import maru.core.units.DaylengthDefinition;
import maru.map.MaruMapPlugin;
import maru.map.preferences.PreferenceConstants;

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
    /** Reference to the plugin's preference store. */
    private IPreferenceStore preferenceStore;

    /** Indicates that one of the values has changed. */
    private boolean settingsChanged;

    /** The step size for which to show latitude/longitude in degree. */
    private double latlonStepSize;

    /** Whether or not to use anti-aliasing when drawing. */
    private boolean antiAliasing;

    /** Whether or not to show when a spacecraft is in shadow when drawing its groundtrack. */
    private boolean umbraOrPenumbra;

    /** The step size that the ground track is calculated with in seconds. */
    private long groundtrackStepSize;

    /** The length of the ground track in seconds. */
    private long groundtrackLength;

    /** Whether or not to show visibility lines between spacecrafts. */
    private boolean enableAccessScToSc;

    /** Whether or not to show visibility lines between spacecrafts and ground stations. */
    private boolean enableAccessScToGs;

    /** Whether or not to show the sun terminator. */
    private boolean enableNight;

    /** The step size for which to calculate the sun terminator in pixel. */
    private int nightStepSize;

    /** The definition of sunrise/sunset; affects the sun terminator. */
    private DaylengthDefinition dayDef;

    /**
     * Initializes its values with the plugin's preferences values and
     * starts to listen to the plugin's preference changes.
     */
    public MapViewSettings()
    {
        initPreferenceStore();
        initPreferenceStoreListener();
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

    /** {@link #latlonStepSize} */
    public double getLatLonStepSize()
    {
        return latlonStepSize;
    }

    /** {@link #antiAliasing} */
    public boolean getAntiAliasing()
    {
        return antiAliasing;
    }

    /** {@link #umbraOrPenumbra} */
    public boolean getShowShadowTimes()
    {
        return umbraOrPenumbra;
    }

    /** {@link #groundtrackStepSize} */
    public long getGroundtrackStepSize()
    {
        return groundtrackStepSize;
    }

    /** {@link #groundtrackLength} */
    public long getGroundtrackLength()
    {
        return groundtrackLength;
    }

    /** {@link #enableAccessScToSc} */
    public boolean getShowAccessSpacecraftToSpacecraft()
    {
        return enableAccessScToSc;
    }

    /** {@link #enableAccessScToGs} */
    public boolean getShowAccessSpacecraftToGroundstation()
    {
        return enableAccessScToGs;
    }

    /** {@link #enableNight} */
    public boolean getShowNight()
    {
        return enableNight;
    }

    /** {@link #nightStepSize} */
    public int getNightStepSize()
    {
        return nightStepSize;
    }

    /** {@link #dayDef} */
    public DaylengthDefinition getDaylengthDefinition()
    {
        return dayDef;
    }

    private void initPreferenceStore()
    {
        preferenceStore = MaruMapPlugin.getDefault().getPreferenceStore();

        antiAliasing = preferenceStore.getBoolean(PreferenceConstants.P_MAP_ANTI_ALIASING);
        umbraOrPenumbra = preferenceStore.getBoolean(PreferenceConstants.P_MAP_SHOW_UMBRA);
        groundtrackStepSize = preferenceStore.getLong(PreferenceConstants.P_MAP_GROUNDTRACK_STEP_SIZE);
        groundtrackLength = preferenceStore.getLong(PreferenceConstants.P_MAP_GROUNDTRACK_LENGTH) * 60 * 60;
        enableAccessScToSc = preferenceStore.getBoolean(PreferenceConstants.P_MAP_SHOW_ACCESS_SC_TO_SC);
        enableAccessScToGs = preferenceStore.getBoolean(PreferenceConstants.P_MAP_SHOW_ACCESS_SC_TO_GS);
        enableNight = preferenceStore.getBoolean(PreferenceConstants.P_MAP_NIGHT);
        nightStepSize = preferenceStore.getInt(PreferenceConstants.P_MAP_NIGHT_STEPSIZE);
        latlonStepSize = preferenceStore.getDouble(PreferenceConstants.P_MAP_LAT_LON_LINE_STEPSIZE);
        dayDef = DaylengthDefinition.toDaylengthDefinition(preferenceStore.getString(PreferenceConstants.P_MAP_DAYLENGTH_DEFINITION));

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

                if (changedProperty.equals(PreferenceConstants.P_MAP_ANTI_ALIASING)) {
                    antiAliasing = (boolean) event.getNewValue();
                } else if (changedProperty.equals(PreferenceConstants.P_MAP_SHOW_UMBRA)) {
                    umbraOrPenumbra = (boolean) event.getNewValue();
                } else if (changedProperty.equals(PreferenceConstants.P_MAP_GROUNDTRACK_STEP_SIZE)) {
                    // stupid RCP preferences return Integer object even when we
                    // put in a variable of type long. the same is true for
                    // the ground track length
                    groundtrackStepSize = (int) event.getNewValue();
                } else if (changedProperty.equals(PreferenceConstants.P_MAP_GROUNDTRACK_LENGTH)) {
                    // preferences are saved in hours, but we are working with seconds
                    groundtrackLength = (int) event.getNewValue() * 60 * 60;
                } else if (changedProperty.equals(PreferenceConstants.P_MAP_SHOW_ACCESS_SC_TO_SC)) {
                    enableAccessScToSc = (boolean) event.getNewValue();
                } else if (changedProperty.equals(PreferenceConstants.P_MAP_SHOW_ACCESS_SC_TO_GS)) {
                    enableAccessScToGs = (boolean) event.getNewValue();
                } else if (changedProperty.equals(PreferenceConstants.P_MAP_NIGHT)) {
                    enableNight = (boolean) event.getNewValue();
                } else if (changedProperty.equals(PreferenceConstants.P_MAP_NIGHT_STEPSIZE)) {
                    nightStepSize = (int) event.getNewValue();
                } else if (changedProperty.equals(PreferenceConstants.P_MAP_LAT_LON_LINE_STEPSIZE)) {
                    latlonStepSize = (int) event.getNewValue();
                } else if (changedProperty.equals(PreferenceConstants.P_MAP_DAYLENGTH_DEFINITION)) {
                    dayDef = DaylengthDefinition.toDaylengthDefinition((String) event.getNewValue());
                } else {
                    return;
                }
                setSettingsChanged(true);
                MaruMapPlugin.getDefault().redraw();
            }
        });
    }
}