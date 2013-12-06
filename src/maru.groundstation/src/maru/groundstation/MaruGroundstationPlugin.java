package maru.groundstation;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class MaruGroundstationPlugin extends AbstractUIPlugin
{
    public static final String PLUGIN_ID = "maru.groundstation"; //$NON-NLS-1$

    private static MaruGroundstationPlugin plugin;

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

    public static MaruGroundstationPlugin getDefault()
    {
        return plugin;
    }
}
