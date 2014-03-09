package maru.map.settings.groundstation;

import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

public class GroundstationSettings
{
    private Preferences node;

    public GroundstationSettings(Preferences node)
    {
        this.node = node;
    }

    public Preferences getNode()
    {
        return node;
    }

    public void remove()
    {
        try {
            node.removeNode();
        } catch (BackingStoreException e) {
            e.printStackTrace();
        } finally {
            node = null; // invalidate the current object
        }
    }

    @Override
    public String toString()
    {
        return node.toString();
    }

    /** Whether or not to show the element icon on the map. */
    public boolean getShowElementIcon()
    {
        return node.getBoolean(GroundstationSettingsConstants.SHOW_ELEMENT_ICON, GroundstationSettingsConstants.DEFAULT_SHOW_ELEMENT_ICON);
    }

    /** Whether or not to show the element name on the map. */
    public boolean getShowElementName()
    {
        return node.getBoolean(GroundstationSettingsConstants.SHOW_ELEMENT_NAME, GroundstationSettingsConstants.DEFAULT_SHOW_ELEMENT_NAME);
    }

    /** Size of the element icon in pixels. */
    public long getElementIconSize()
    {
        return node.getLong(GroundstationSettingsConstants.ELEMENT_ICON_SIZE, GroundstationSettingsConstants.DEFAULT_ELEMENT_ICON_SIZE);
    }

    public void setShowElementIcon(boolean show)
    {
        node.putBoolean(GroundstationSettingsConstants.SHOW_ELEMENT_ICON, show);
    }

    public void setShowElementName(boolean show)
    {
        node.putBoolean(GroundstationSettingsConstants.SHOW_ELEMENT_NAME, show);
    }

    public void setElementIconSize(long size)
    {
        node.putLong(GroundstationSettingsConstants.ELEMENT_ICON_SIZE, size);
    }
}
