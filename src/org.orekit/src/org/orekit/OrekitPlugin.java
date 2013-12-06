package org.orekit;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.orekit.data.DataProvidersManager;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class OrekitPlugin implements BundleActivator
{
    @Override
    public void start(BundleContext context) throws Exception
    {
        // set the configuration path for orekit. more details:
        // https://www.orekit.org/static/configuration.html
        // https://www.orekit.org/static/faq.html
        URL url = new URL("platform:/plugin/org.orekit/orekit-data-minimal.zip");
        String path = FileLocator.resolve(url).getFile();

        System.setProperty(DataProvidersManager.OREKIT_DATA_PATH, path);
    }

    @Override
    public void stop(BundleContext context) throws Exception
    {

    }
}
