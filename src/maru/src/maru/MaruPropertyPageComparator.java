package maru;

import org.eclipse.ui.IPluginContribution;
import org.eclipse.ui.model.ContributionComparator;
import org.eclipse.ui.model.IComparableContribution;

public class MaruPropertyPageComparator extends ContributionComparator
{
    @Override
    public int compare(IComparableContribution c1, IComparableContribution c2)
    {
        IPluginContribution pc1 = (IPluginContribution) c1;
        IPluginContribution pc2 = (IPluginContribution) c2;

        String pc1Id = pc1.getLocalId();
        String pc2Id = pc2.getLocalId();

        String pc1Prio = pc1Id.substring(pc1Id.length() - 4);
        String pc2Prio = pc2Id.substring(pc2Id.length() - 4);

        // the ID of the Maru property pages contain a sorting priority encoded
        // at the end, e.g. ui.maru.propertypages.centralbody.2000

        return super.compare(pc1Prio, pc2Prio);
    }
}
