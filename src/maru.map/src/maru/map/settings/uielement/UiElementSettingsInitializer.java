package maru.map.settings.uielement;

import org.osgi.service.prefs.Preferences;

public final class UiElementSettingsInitializer
{
    public static void initialize(String prjName, Preferences prjNode)
    {
        prjNode.putBoolean(UiElementSettingsConstants.SHOW_ELEMENT_ICON, UiElementSettingsConstants.DEFAULT_SHOW_ELEMENT_ICON);
        prjNode.putLong(UiElementSettingsConstants.ELEMENT_ICON_SIZE, UiElementSettingsConstants.DEFAULT_ELEMENT_ICON_SIZE);
    }
}
