package maru.core;

import maru.core.model.CoreModel;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
/**
 * The activator class that controls the plugin's life cycle.
 * <p>
 * This core plugin defines the application model. The model is the information
 * that is read and written to disk. The plugin provides various interfaces and
 * abstract classes to be used when extending the model. It also provides an
 * API to manipulate the model ({@link maru.core.model.CoreModel}) and to listen
 * to model changes ({@link maru.core.model.ScenarioModelAdapter}).
 * <p>
 * External plugins are meant to implement the interfaces or extend provided
 * abstract classes and use {@link maru.core.model.CoreModel} to add them to
 * the model. The plugin takes care of saving modifications to disk.
 */
public class MaruCorePlugin extends AbstractUIPlugin
{
    public static final String PLUGIN_ID = "maru.core"; //$NON-NLS-1$

    private static MaruCorePlugin plugin;
    private CoreModel coreModel;

    @Override
    public void start(BundleContext context) throws Exception
    {
        super.start(context);
        plugin = this;

        coreModel = CoreModel.getDefault();
        coreModel.startup();
    }

    @Override
    public void stop(BundleContext context) throws Exception
    {
        if (coreModel != null) {
            coreModel.shutdown();
        }

        plugin = null;
        super.stop(context);
    }

    /**
     * Get the shared plugin instance.
     *
     * @return the shared instance
     */
    public static MaruCorePlugin getDefault()
    {
        return plugin;
    }

    public CoreModel getCoreModel()
    {
        return coreModel;
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
