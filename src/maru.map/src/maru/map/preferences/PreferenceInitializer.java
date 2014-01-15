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

        store.setDefault(PreferenceConstants.P_MAP_SHOW_UMBRA, true);
        store.setDefault(PreferenceConstants.P_MAP_GROUNDTRACK_STEP_SIZE, 120L);
        store.setDefault(PreferenceConstants.P_MAP_GROUNDTRACK_LENGTH, 2L);

        store.setDefault(PreferenceConstants.P_MAP_SHOW_ACCESS_SC_TO_SC, true);
        store.setDefault(PreferenceConstants.P_MAP_SHOW_ACCESS_SC_TO_GS, true);

        store.setDefault(PreferenceConstants.P_MAP_NIGHT, true);
        store.setDefault(PreferenceConstants.P_MAP_NIGHT_STEPSIZE, 4);

        store.setDefault(PreferenceConstants.P_MAP_LAT_LON_LINE_STEPSIZE, 30);

        store.setDefault(PreferenceConstants.P_MAP_DAYLENGTH_DEFINITION, DaylightDefinitionChoices.DEF_CHOICE_2_VALUE);
    }
}
