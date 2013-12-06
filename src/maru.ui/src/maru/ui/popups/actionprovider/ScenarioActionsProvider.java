package maru.ui.popups.actionprovider;

import maru.ui.MaruUIPlugin;
import maru.ui.model.UiGroundstation;
import maru.ui.model.UiSpacecraft;
import maru.ui.model.UiTimepoint;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.ResourceSelectionUtil;
import org.eclipse.ui.navigator.CommonActionProvider;
import org.eclipse.ui.navigator.ICommonActionExtensionSite;
import org.eclipse.ui.navigator.ICommonMenuConstants;
import org.eclipse.ui.navigator.ICommonViewerWorkbenchSite;
import org.eclipse.ui.navigator.WizardActionGroup;

public class ScenarioActionsProvider extends CommonActionProvider
{
    private static final String NEW_MENU_NAME = "common.new.menu";

    private WizardActionGroup newWizardActionGroup;
    private ScenarioProjectDeleteAction projectDeleteAction;
    private ScenarioElementDeleteAction elementDeleteAction;

    @Override
    public void init(ICommonActionExtensionSite aSite)
    {
        if (!(aSite.getViewSite() instanceof ICommonViewerWorkbenchSite)) {
            return;
        }

        ICommonViewerWorkbenchSite workbenchSite = (ICommonViewerWorkbenchSite) aSite.getViewSite();
        IWorkbenchWindow window = workbenchSite.getWorkbenchWindow();

        newWizardActionGroup = new WizardActionGroup(
            window,
            PlatformUI.getWorkbench().getNewWizardRegistry(),
            WizardActionGroup.TYPE_NEW,
            aSite.getContentService()
        );

        projectDeleteAction = new ScenarioProjectDeleteAction(window);
        projectDeleteAction.setDisabledImageDescriptor(MaruUIPlugin.getSharedImageDescriptor(ISharedImages.IMG_TOOL_DELETE_DISABLED));
        projectDeleteAction.setImageDescriptor(MaruUIPlugin.getSharedImageDescriptor(ISharedImages.IMG_TOOL_DELETE));

        elementDeleteAction = new ScenarioElementDeleteAction();
        elementDeleteAction.setDisabledImageDescriptor(MaruUIPlugin.getSharedImageDescriptor(ISharedImages.IMG_TOOL_DELETE_DISABLED));
        elementDeleteAction.setImageDescriptor(MaruUIPlugin.getSharedImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
    }

    @Override
    public void fillContextMenu(IMenuManager menu)
    {
        IStructuredSelection selection = (IStructuredSelection) getContext().getSelection();

        IMenuManager submenu = new MenuManager("New", NEW_MENU_NAME);

        // fill the menu from the commonWizard contributions
        newWizardActionGroup.setContext(getContext());
        newWizardActionGroup.fillContextMenu(submenu);
        submenu.add(new Separator(ICommonMenuConstants.GROUP_ADDITIONS));
        menu.appendToGroup(ICommonMenuConstants.GROUP_NEW, submenu);

        if (selection.isEmpty()) {
            return;
        }

        if (ResourceSelectionUtil.allResourcesAreOfType(selection, IResource.PROJECT))
        {
            projectDeleteAction.selectionChanged(selection);
            menu.appendToGroup(ICommonMenuConstants.GROUP_EDIT, projectDeleteAction);
        }
        else if (allScenarioElementsAreRemovable(selection))
        {
            elementDeleteAction.selectionChanged(selection);
            menu.appendToGroup(ICommonMenuConstants.GROUP_EDIT, elementDeleteAction);
        }
    }

    @Override
    public void updateActionBars()
    {
        IStructuredSelection selection = (IStructuredSelection) getContext().getSelection();
        projectDeleteAction.selectionChanged(selection);
        elementDeleteAction.selectionChanged(selection);
    }

    private boolean allScenarioElementsAreRemovable(IStructuredSelection selection)
    {
        boolean ret = false;
        for (Object element : selection.toList())
        {
            if (!(element instanceof UiGroundstation) &&
                !(element instanceof UiSpacecraft) &&
                !(element instanceof UiTimepoint))
            {
                return false;
            }

            // so far each element has been removable
            ret = true;
        }
        return ret;
    }
}
