package maru;

import java.net.URL;

import maru.perspectives.Planning;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.internal.ide.IDEInternalWorkbenchImages;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.osgi.framework.Bundle;

@SuppressWarnings("restriction") // see hack below
public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor
{
    @Override
    public void initialize(IWorkbenchConfigurer configurer)
    {
        super.initialize(configurer);
        configurer.setSaveAndRestore(true);

        /*
         * Project Explorer and Common Navigator fail to show projects without
         * this hack. All reports I have read are about icons not showing up but
         * in my case the navigators don't even show a project's name.
         * The override of getDefaultPageInput() is also part of the
         * workaround.
         *
         * For more details:
         * https://bugs.eclipse.org/bugs/show_bug.cgi?id=234252
         * http://help.eclipse.org/indigo/topic/org.eclipse.platform.doc.isv/guide/cnf.htm
         * http://francisu.wordpress.com/2008/05/27/magic-required-to-use-the-common-navigator-in-an-rcp-application/
         * http://stackoverflow.com/questions/10345977/showing-project-explorer-view-and-its-functionality-to-rcp
         */

        IDE.registerAdapters();

        final String ICONS_PATH = "icons/full/";
        Bundle ideBundle = Platform.getBundle(IDEWorkbenchPlugin.IDE_WORKBENCH);

        declareWorkbenchImage(configurer, ideBundle,IDE.SharedImages.IMG_OBJ_PROJECT, ICONS_PATH + "obj16/prj_obj.gif",true);
        declareWorkbenchImage(configurer, ideBundle,IDE.SharedImages.IMG_OBJ_PROJECT_CLOSED, ICONS_PATH + "obj16/cprj_obj.gif", true);
        declareWorkbenchImage(configurer, ideBundle, IDEInternalWorkbenchImages.IMG_ETOOL_PROBLEMS_VIEW, ICONS_PATH + "eview16/problems_view.gif", true);
        declareWorkbenchImage(configurer, ideBundle, IDEInternalWorkbenchImages.IMG_ETOOL_PROBLEMS_VIEW_ERROR, ICONS_PATH + "eview16/problems_view_error.gif", true);
        declareWorkbenchImage(configurer, ideBundle, IDEInternalWorkbenchImages.IMG_ETOOL_PROBLEMS_VIEW_WARNING, ICONS_PATH + "eview16/problems_view_warning.gif", true);
        declareWorkbenchImage(configurer, ideBundle, IDEInternalWorkbenchImages.IMG_OBJS_ERROR_PATH, ICONS_PATH + "obj16/error_tsk.gif", true);
        declareWorkbenchImage(configurer, ideBundle, IDEInternalWorkbenchImages.IMG_OBJS_WARNING_PATH, ICONS_PATH + "obj16/warn_tsk.gif", true);

        /* End of hack. */
    }

    @Override
    public IAdaptable getDefaultPageInput()
    {
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        return workspace.getRoot();
	}

    @Override
    public String getInitialWindowPerspectiveId()
    {
        // the planning perspective is the default perspective
        return Planning.ID;
    }

	@Override
    public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer)
    {
        return new ApplicationWorkbenchWindowAdvisor(configurer);
    }

    @Override
    public boolean preShutdown()
    {
        try
        {
            // avoid the "The workspace exited with unsaved changes in the
            // previous session" problem
            ResourcesPlugin.getWorkspace().save(true, null);
        }
        catch (CoreException e)
        {
            e.printStackTrace();
        }

        return super.preShutdown();
    }

    private void declareWorkbenchImage(IWorkbenchConfigurer cfg, Bundle ideBundle, String symbolicName, String path, boolean shared)
    {
        URL url = ideBundle.getEntry(path);
        ImageDescriptor desc = ImageDescriptor.createFromURL(url);
        cfg.declareImage(symbolicName, desc, shared);
    }
}
