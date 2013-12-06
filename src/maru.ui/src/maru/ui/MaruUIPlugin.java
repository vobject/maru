package maru.ui;

import maru.ui.model.UiModel;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class MaruUIPlugin extends AbstractUIPlugin
{
    public static final String PLUGIN_ID = "maru.ui"; //$NON-NLS-1$

    private static MaruUIPlugin plugin;
    private UiModel uiModel;

    @Override
    public void start(BundleContext context) throws Exception
    {
        super.start(context);
        plugin = this;

        uiModel = UiModel.getDefault();
        uiModel.startup();
    }

    @Override
    public void stop(BundleContext context) throws Exception
    {
        if (uiModel != null) {
            uiModel.shutdown();
        }

        plugin = null;
        super.stop(context);
    }

    /**
     * Returns the shared instance
     *
     * @return the shared instance
     */
    public static MaruUIPlugin getDefault()
    {
        return plugin;
    }

    public UiModel getUiModel()
    {
        return uiModel;
    }

    /**
     * Returns an image descriptor for the image file at the given
     * plug-in relative path
     *
     * @param path the path
     * @return the image descriptor
     */
    public static ImageDescriptor getImageDescriptor(String imagePath)
    {
        return imageDescriptorFromPlugin(PLUGIN_ID, imagePath);
    }

    public static Image getImage(String imagePath)
    {
        return getImageDescriptor(imagePath).createImage();
    }

    public static ImageDescriptor getSharedImageDescriptor(String imagePath)
    {
        return PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(imagePath);
    }

    public static Image getSharedImage(String imagePath)
    {
        return getSharedImageDescriptor(imagePath).createImage();
    }
}
