package maru.ui.debug;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class MaruDebugPlugin extends AbstractUIPlugin
{
    public static final String PLUGIN_ID = "maru.ui.debug"; //$NON-NLS-1$

    private static MaruDebugPlugin plugin;

    @Override
    public void start(final BundleContext context) throws Exception
    {
        super.start(context);
        plugin = this;
    }

    @Override
    public void stop(final BundleContext context) throws Exception
    {
        plugin = null;
        super.stop(context);
    }

    public static MaruDebugPlugin getDefault()
    {
        return plugin;
    }
}
