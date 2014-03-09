package maru.map.preferences;

import maru.map.MaruMapPlugin;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

public class PreferenceInitializer extends AbstractPreferenceInitializer
{
    @Override
    public void initializeDefaultPreferences()
    {
        IPreferenceStore store = MaruMapPlugin.getDefault().getPreferenceStore();

        store.setDefault(PreferenceConstants.P_MAP_ANTI_ALIASING, true);
        store.setDefault(PreferenceConstants.P_MAP_VISIBILITY_CIRCLE_POINTS, 48L);
    }
}
