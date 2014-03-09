package maru.map.views;

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

    /** Whether or not to use anti-aliasing when drawing. */
    private boolean antiAliasing;

    /** Number of points to use for drawing visibility circles around elements. */
    private int visibilityCirclePoints;

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

    /** {@link #antiAliasing} */
    public boolean getAntiAliasing()
    {
        return antiAliasing;
    }

    /** {@link #visibilityCirclePoints} */
    public int getVisibilityCirclePoints()
    {
        return visibilityCirclePoints;
    }

    private void initPreferenceStore()
    {
        preferenceStore = MaruMapPlugin.getDefault().getPreferenceStore();

        antiAliasing = preferenceStore.getBoolean(PreferenceConstants.P_MAP_ANTI_ALIASING);
        visibilityCirclePoints = (int) preferenceStore.getLong(PreferenceConstants.P_MAP_VISIBILITY_CIRCLE_POINTS);

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
                } else if (changedProperty.equals(PreferenceConstants.P_MAP_VISIBILITY_CIRCLE_POINTS)) {
                    visibilityCirclePoints = (int) event.getNewValue();
                } else {
                    return;
                }
                setSettingsChanged(true);
                MaruMapPlugin.getDefault().redraw();
            }
        });
    }
}