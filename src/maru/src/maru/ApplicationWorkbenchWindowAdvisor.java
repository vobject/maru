package maru;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import maru.preferences.PreferenceConstants;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.internal.dialogs.WorkbenchWizardElement;
import org.eclipse.ui.internal.wizards.AbstractExtensionWizardRegistry;
import org.eclipse.ui.wizards.IWizardCategory;
import org.eclipse.ui.wizards.IWizardDescriptor;

@SuppressWarnings("restriction")
public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor
{
    public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer)
    {
        super(configurer);
    }

    @Override
    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer)
    {
        return new ApplicationActionBarAdvisor(configurer);
    }

    @Override
    public void preWindowOpen()
    {
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        configurer.setInitialSize(new Point(1024, 768));
        configurer.setShowCoolBar(true);
        configurer.setShowPerspectiveBar(false);
        configurer.setShowStatusLine(false);
        configurer.setShowProgressIndicator(false);

        IPreferenceStore preferenceStore = MaruPlugin.getDefault().getPreferenceStore();
        setWindowTitle(preferenceStore.getString(PreferenceConstants.P_APP_WINDOW_TITLE));
    }

    @Override
    public void postWindowOpen()
    {
        initPreferenceStoreListener();
        removeDefaultNewWizards();
    }

    /**
     * Remove the wizard category 'General'.
     */
    private void removeDefaultNewWizards()
    {
        // based on:
        // http://stackoverflow.com/questions/11307367/how-to-remove-default-wizards-from-file-new-menu-in-rcp-application
        // http://www.eclipse.org/forums/index.php/t/261462

        AbstractExtensionWizardRegistry wizardRegistry = (AbstractExtensionWizardRegistry) PlatformUI.getWorkbench().getNewWizardRegistry();
        IWizardCategory[] categories = PlatformUI.getWorkbench().getNewWizardRegistry().getRootCategory().getCategories();

        for (IWizardDescriptor wizard : getAllWizards(categories))
        {
            if(wizard.getCategory().getId().matches("org.eclipse.ui.Basic"))
            {
                WorkbenchWizardElement wizardElement = (WorkbenchWizardElement) wizard;
                wizardRegistry.removeExtension(wizardElement.getConfigurationElement().getDeclaringExtension(), new Object[] { wizardElement });
            }
        }
    }

    private List<IWizardDescriptor> getAllWizards(IWizardCategory[] categories)
    {
        List<IWizardDescriptor> results = new ArrayList<>();

        for (IWizardCategory wizardCategory : categories)
        {
            results.addAll(Arrays.asList(wizardCategory.getWizards()));
            results.addAll(getAllWizards(wizardCategory.getCategories()));
        }
        return results;
    }

    private void setWindowTitle(String title)
    {
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();

        if ((title == null) || (title.isEmpty())) {
            // show the path to the workspace directory in the title bar
            configurer.setTitle("Maru - " + getDataPath());
        } else {
            // show the user defined window title
            configurer.setTitle(title);
        }
    }

    private void initPreferenceStoreListener()
    {
        IPreferenceStore preferenceStore = MaruPlugin.getDefault().getPreferenceStore();

        // install window title change listener
        preferenceStore.addPropertyChangeListener(new IPropertyChangeListener()
        {
            @Override
            public void propertyChange(PropertyChangeEvent event)
            {
                if (event.getProperty().equals(PreferenceConstants.P_APP_WINDOW_TITLE)) {
                    setWindowTitle((String) event.getNewValue());
                }
            }
        });
    }

    /**
     * Get the path of the -data directory also known as the workspace.
     *
     * @return the path to the workspace directory
     */
    private String getDataPath()
    {
        Location location = Platform.getInstanceLocation();
        if (location == null) {
            return "@none";
        } else {
            return location.getURL().getPath();
        }
    }
}
