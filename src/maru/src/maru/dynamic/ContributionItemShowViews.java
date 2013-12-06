package maru.dynamic;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.CompoundContributionItem;
import org.eclipse.ui.actions.ContributionItemFactory;

public class ContributionItemShowViews extends CompoundContributionItem
{
    /**
     * Makes views show up under Window -> Show View.
     */
    @Override
    protected IContributionItem[] getContributionItems()
    {
        IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        IContributionItem item = ContributionItemFactory.VIEWS_SHORTLIST.create(window);

        List<IContributionItem> menuContributionList = new ArrayList<>();
        menuContributionList.add(item);
        return menuContributionList.toArray(new IContributionItem[menuContributionList.size()]);
    }
}
