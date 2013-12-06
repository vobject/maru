package maru.spacecraft;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class MaruSpacecraftPlugin extends AbstractUIPlugin
{
    public static final String PLUGIN_ID = "maru.spacecraft"; //$NON-NLS-1$

    private static MaruSpacecraftPlugin plugin;

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

    public static MaruSpacecraftPlugin getDefault()
    {
        return plugin;
    }
}
