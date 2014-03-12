package maru.ui.debug;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import maru.ui.MaruUIPlugin;
import maru.ui.preferences.UserInterfacePreferenceConstants;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.activities.IActivityManager;
import org.eclipse.ui.activities.IWorkbenchActivitySupport;

public class DebugPreferenceStoreListener
{
    /** Reference to the plugin's preference store. */
    private IPreferenceStore preferenceStore;

    /** The preference change listener. */
    private IPropertyChangeListener preferenceListener;

    public DebugPreferenceStoreListener()
    {
        init();
    }

    public void startListening()
    {
        preferenceStore.addPropertyChangeListener(preferenceListener);
    }

    public void stopListening()
    {
        preferenceStore.removePropertyChangeListener(preferenceListener);
    }

    private void init()
    {
        preferenceListener = new IPropertyChangeListener()
        {
            @Override
            public void propertyChange(PropertyChangeEvent event)
            {
                String changedProperty = event.getProperty();

                if (changedProperty.equals(UserInterfacePreferenceConstants.P_UI_SHOW_DEBUG_MENU))
                {
                    boolean showMenu;

                    // stupid Eclipse sometimes provides a String from event.getNewValue()
                    // when "Restore Defaults" is clicked.
                    Object newValue = event.getNewValue();
                    if (newValue instanceof String) {
                        showMenu = Boolean.parseBoolean((String) newValue);
                    } else {
                        // default case
                        showMenu = (boolean) newValue;
                    }

                    showDebugMenu(showMenu);
                }
            }
        };

        preferenceStore = MaruUIPlugin.getDefault().getPreferenceStore();

        boolean showMenu = preferenceStore.getBoolean(UserInterfacePreferenceConstants.P_UI_SHOW_DEBUG_MENU);
        showDebugMenu(showMenu);
    }

    private void showDebugMenu(boolean show)
    {
        IWorkbenchActivitySupport activitySupport = PlatformUI.getWorkbench().getActivitySupport();

        if (show)
        {
            IActivityManager activityMgr = activitySupport.getActivityManager();

            Set<String> enabledActivities = new HashSet<String>();
            String activityId = "maru.ui.activities.debug";

            if (activityMgr.getActivity(activityId).isDefined()) {
                enabledActivities.add(activityId);
            }
            activitySupport.setEnabledActivityIds(enabledActivities);
        }
        else
        {
            activitySupport.setEnabledActivityIds(Collections.EMPTY_SET);
        }
    }
}
