package maru.core;

import maru.core.model.CoreModel;
import maru.core.workspace.WorkspaceModel;

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
    private WorkspaceModel workspaceModel;

    @Override
    public void start(BundleContext context) throws Exception
    {
        super.start(context);
        plugin = this;

        workspaceModel = WorkspaceModel.getDefault();
        workspaceModel.startup();
    }

    @Override
    public void stop(BundleContext context) throws Exception
    {
        if (workspaceModel != null) {
            workspaceModel.shutdown();
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

    public WorkspaceModel getWorkspaceModel()
    {
        return workspaceModel;
    }

    public CoreModel getCoreModel()
    {
        return workspaceModel.getCoreModel();
    }
}
