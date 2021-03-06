package maru.map.worldwind.preferences;

import maru.map.worldwind.WorldWindPlugin;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer
{
    /*
     * (non-Javadoc)
     *
     * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
     */
    @Override
    public void initializeDefaultPreferences()
    {
        IPreferenceStore store = WorldWindPlugin.getDefault().getPreferenceStore();
        store.setDefault(PreferenceConstants.P_BOOLEAN, true);
        store.setDefault(PreferenceConstants.P_CHOICE, "choice2");
        store.setDefault(PreferenceConstants.P_STRING, "Default value");
    }
}
