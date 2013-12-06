package maru.dynamic;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.CompoundContributionItem;
import org.eclipse.ui.actions.ContributionItemFactory;

public class ContributionItemOpenPerspective extends CompoundContributionItem
{
    /**
     * Makes perspectives show up under Window -> Open Perspective.
     */
    @Override
    protected IContributionItem[] getContributionItems()
    {
        IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        IContributionItem item = ContributionItemFactory.PERSPECTIVES_SHORTLIST.create(window);

        List<IContributionItem> menuContributionList = new ArrayList<>();
        menuContributionList.add(item);
        return menuContributionList.toArray(new IContributionItem[menuContributionList.size()]);
    }
}
