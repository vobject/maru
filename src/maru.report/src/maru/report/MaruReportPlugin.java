package maru.report;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class MaruReportPlugin extends AbstractUIPlugin
{
    public static final String PLUGIN_ID = "maru.report"; //$NON-NLS-1$

    private static MaruReportPlugin plugin;

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

    public static MaruReportPlugin getDefault()
    {
        return plugin;
    }
}
