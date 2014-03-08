package maru.map.settings.uielement;

import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

public class UiElementSettings
{
    private final Preferences elemParent;
    private Preferences elemNode;
    private String elemName;

    public UiElementSettings(Preferences uiElementParent, Preferences uiElementNode, String uiElementName)
    {
        this.elemParent = uiElementParent;
        this.elemNode = uiElementNode;
        this.elemName = uiElementName;
    }

    public boolean exists()
    {
        return elemNode != null;
    }

    public void initialize()
    {
        if (!exists()) {
            elemNode = elemParent.node(elemName);
        }

        // initialize settings it with default values
        UiElementSettingsInitializer.initialize(elemName, elemNode);
    }

    public void remove()
    {
        try
        {
            if (exists()) {
                elemNode.removeNode();
            }
        }
        catch (BackingStoreException e)
        {
            e.printStackTrace();
        }
        finally
        {
            // invalidate the current object
            elemNode = null;
            elemName = null;
        }
    }

    @Override
    public String toString()
    {
        if (exists()) {
            return elemNode.toString();
        } else {
            return null;
        }
    }

    /** Whether or not to show the element icon on the map. */
    public boolean getShowElementIcon()
    {
        return elemNode.getBoolean(UiElementSettingsConstants.SHOW_ELEMENT_ICON, UiElementSettingsConstants.DEFAULT_SHOW_ELEMENT_ICON);
    }

    /** Size of the element icon in pixels. */
    public long getElementIconSize()
    {
        return elemNode.getLong(UiElementSettingsConstants.ELEMENT_ICON_SIZE, UiElementSettingsConstants.DEFAULT_ELEMENT_ICON_SIZE);
    }

    public void setShowElementIcon(boolean show)
    {
        elemNode.putBoolean(UiElementSettingsConstants.SHOW_ELEMENT_ICON, show);
    }

    public void setElementIconSize(long size)
    {
        elemNode.putLong(UiElementSettingsConstants.ELEMENT_ICON_SIZE, size);
    }
}
