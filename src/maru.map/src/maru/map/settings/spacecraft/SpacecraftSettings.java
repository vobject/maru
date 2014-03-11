package maru.map.settings.spacecraft;

import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

public class SpacecraftSettings
{
    private Preferences node;

    public SpacecraftSettings(Preferences node)
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
        return node.getBoolean(SpacecraftSettingsConstants.SHOW_ELEMENT_ICON, SpacecraftSettingsConstants.DEFAULT_SHOW_ELEMENT_ICON);
    }

    /** Whether or not to show the element name on the map. */
    public boolean getShowElementName()
    {
        return node.getBoolean(SpacecraftSettingsConstants.SHOW_ELEMENT_NAME, SpacecraftSettingsConstants.DEFAULT_SHOW_ELEMENT_NAME);
    }

    /** Size of the element icon in pixels. */
    public long getElementIconSize()
    {
        return node.getLong(SpacecraftSettingsConstants.ELEMENT_ICON_SIZE, SpacecraftSettingsConstants.DEFAULT_ELEMENT_ICON_SIZE);
    }

    /** The length of the ground track in seconds. */
    public long getGroundtrackLength()
    {
        return node.getLong(SpacecraftSettingsConstants.GROUNDTRACK_LENGTH, SpacecraftSettingsConstants.DEFAULT_GROUNDTRACK_LENGTH);
    }

    /** The length of the ground track in seconds. */
    public long getGroundtrackLineWidth()
    {
        return node.getLong(SpacecraftSettingsConstants.GROUNDTRACK_LINE_WIDTH, SpacecraftSettingsConstants.DEFAULT_GROUNDTRACK_LINE_WIDTH);
    }

    public void setShowElementIcon(boolean show)
    {
        node.putBoolean(SpacecraftSettingsConstants.SHOW_ELEMENT_ICON, show);
    }

    public void setShowElementName(boolean show)
    {
        node.putBoolean(SpacecraftSettingsConstants.SHOW_ELEMENT_NAME, show);
    }

    public void setElementIconSize(long size)
    {
        node.putLong(SpacecraftSettingsConstants.ELEMENT_ICON_SIZE, size);
    }

    public void setGroundtrackLength(long length)
    {
        node.putLong(SpacecraftSettingsConstants.GROUNDTRACK_LENGTH, length);
    }

    public void setGroundtrackLineWidth(long width)
    {
        node.putLong(SpacecraftSettingsConstants.GROUNDTRACK_LINE_WIDTH, width);
    }
}
