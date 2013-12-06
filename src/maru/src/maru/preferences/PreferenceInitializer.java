package maru.preferences;

import maru.MaruPlugin;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

public class PreferenceInitializer extends AbstractPreferenceInitializer
{
    @Override
    public void initializeDefaultPreferences()
    {
        IPreferenceStore store = MaruPlugin.getDefault().getPreferenceStore();

        store.setDefault(PreferenceConstants.P_APP_WINDOW_TITLE, "");
    }
}
