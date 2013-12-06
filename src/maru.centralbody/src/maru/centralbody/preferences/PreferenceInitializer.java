package maru.centralbody.preferences;

import maru.centralbody.MaruCentralBodyPlugin;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

public class PreferenceInitializer extends AbstractPreferenceInitializer
{
    @Override
    public void initializeDefaultPreferences()
    {
        IPreferenceStore store = MaruCentralBodyPlugin.getDefault().getPreferenceStore();
        store.setDefault(PreferenceConstants.P_EXTERNAL_IMAGES, "");
    }
}
