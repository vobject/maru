package maru.ui.debug;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class MaruUiDebugPlugin extends AbstractUIPlugin
{
    public static final String PLUGIN_ID = "maru.ui.debug"; //$NON-NLS-1$

    private static MaruUiDebugPlugin plugin;

    private Thread networkScenarioModelThread;
    private Thread networkMapThread;

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

    public static MaruUiDebugPlugin getDefault()
    {
        return plugin;
    }

    public Thread getNetworkScenarioModelThread()
    {
        return networkScenarioModelThread;
    }

    public void setNetworkScenarioModelThread(Thread t)
    {
        networkScenarioModelThread = t;
    }

    public Thread getNetworkMapThread()
    {
        return networkMapThread;
    }

    public void setNetworkMapThread(Thread t)
    {
        networkMapThread = t;
    }
}
