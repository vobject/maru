package maru.map.preferences;

import maru.map.MaruMapPlugin;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Display;

public class MapPreferenceInitializer extends AbstractPreferenceInitializer
{
    @Override
    public void initializeDefaultPreferences()
    {
        IPreferenceStore store = MaruMapPlugin.getDefault().getPreferenceStore();

        store.setDefault(MapPreferenceConstants.P_MAP_ANTI_ALIASING, true);
        store.setDefault(MapPreferenceConstants.P_MAP_OUTLINE_ICONS, true);
        store.setDefault(MapPreferenceConstants.P_MAP_OUTLINE_TEXT, true);
        store.setDefault(MapPreferenceConstants.P_MAP_FONT, Display.getDefault().getSystemFont().getFontData()[0].getName());
        store.setDefault(MapPreferenceConstants.P_MAP_FONT_SIZE, 12L);
        store.setDefault(MapPreferenceConstants.P_MAP_GROUNDTRACK_STEP_SIZE, 90L);
        store.setDefault(MapPreferenceConstants.P_MAP_VISIBILITY_CIRCLE_POINTS, 48L);
        store.setDefault(MapPreferenceConstants.P_MAP_LAT_LON_LINE_STEPSIZE, 30L);
        store.setDefault(MapPreferenceConstants.P_MAP_NIGHT_OVERLAY_OPACITY, 25L);
        store.setDefault(MapPreferenceConstants.P_MAP_NIGHT_OVERLAY_PIXELSTEPS, 2L);
    }
}
