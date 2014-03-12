package maru.ui;

import maru.ui.debug.DebugPreferenceStoreListener;
import maru.ui.model.UiModel;
import maru.ui.wizards.ICentralBodyWizardPage;

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
    private ICentralBodyWizardPage centralBodyWizardPage;
    private DebugPreferenceStoreListener debugPreferenceListener;

    @Override
    public void start(BundleContext context) throws Exception
    {
        super.start(context);
        plugin = this;

        uiModel = UiModel.getDefault();
        uiModel.startup();

        debugPreferenceListener = new DebugPreferenceStoreListener();
        debugPreferenceListener.startListening();
    }

    @Override
    public void stop(BundleContext context) throws Exception
    {
        debugPreferenceListener.stopListening();

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

    /**
     * When creating a new scenario the wizard page for selecting a central body
     * does not come from the maru.ui plugin but is provided by the maru.centralbody
     * plugin which should use this method to register the wizard page.
     */
    public void setCentralBodyWizardPage(ICentralBodyWizardPage page)
    {
        centralBodyWizardPage = page;
    }

    public ICentralBodyWizardPage getCentralBodyWizardPage()
    {
        return centralBodyWizardPage;
    }
}
