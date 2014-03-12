package maru.ui.preferences;

import maru.ui.MaruUIPlugin;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

public class UserInterfacePreferenceInitializer extends AbstractPreferenceInitializer
{
    @Override
    public void initializeDefaultPreferences()
    {
        IPreferenceStore store = MaruUIPlugin.getDefault().getPreferenceStore();

        store.setDefault(UserInterfacePreferenceConstants.P_UI_SHOW_DEBUG_MENU, false);
    }
}
