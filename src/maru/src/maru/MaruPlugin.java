package maru;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class that controls the plugin's life cycle.
 * <p>
 * This is the main plugin activator class of the Maru application. The plugin
 * does not provide much functionality itself but defines contribution points
 * such as predefined perspectives and the root preference node for the
 * application.
 * <p>
 * The plugin project also serves as a place for target platform, product
 * definition, and various application settings and resources.
 */
public class MaruPlugin extends AbstractUIPlugin
{
    public static final String PLUGIN_ID = "maru"; //$NON-NLS-1$

    /** the shared plugin instance */
    private static MaruPlugin plugin;

    @Override
    public void start(BundleContext context) throws Exception
    {
        super.start(context);
        plugin = this;
    }

    @Override
    public void stop(BundleContext context) throws Exception
    {
        plugin = null;
        super.stop(context);
    }

    /**
     * Get the shared plugin instance.
     *
     * @return the shared instance
     */
    public static MaruPlugin getDefault()
    {
        return plugin;
    }

    /**
     * Get an image descriptor for the image file.
     *
     * @param path a path relative to the plug-in
     * @return the image descriptor
     */
    public static ImageDescriptor getImageDescriptor(String path)
    {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }
}
