package maru.centralbody;

import maru.centralbody.wizards.NewCentralBodyWizardPage;
import maru.ui.MaruUIPlugin;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class MaruCentralBodyPlugin extends AbstractUIPlugin
{
    public static final String PLUGIN_ID = "maru.centralbody"; //$NON-NLS-1$

    private static MaruCentralBodyPlugin plugin;

    @Override
    public void start(BundleContext context) throws Exception
    {
        super.start(context);
        plugin = this;

        // register the central body creation page at the maru.ui plugin
        MaruUIPlugin.getDefault().setCentralBodyWizardPage(new NewCentralBodyWizardPage());
    }

    @Override
    public void stop(BundleContext context) throws Exception
    {
        plugin = null;
        super.stop(context);
    }

    public static MaruCentralBodyPlugin getDefault()
    {
        return plugin;
    }
}
