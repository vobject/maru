package maru.ui.navigators.scenario;

import maru.core.model.IScenarioElement;
import maru.ui.model.UiElement;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

public class ContentSorter extends ViewerSorter
{
    @Override
    public int compare(Viewer viewer, Object e1, Object e2)
    {
        if ((!(e1 instanceof UiElement)) || (!(e2 instanceof UiElement))) {
            return super.compare(viewer, e1, e2);
        }

        UiElement uiElement1 = ((UiElement) e1);
        UiElement uiElement2 = ((UiElement) e2);

        if (uiElement1.getSortPriority() == uiElement2.getSortPriority())
        {
            IScenarioElement element1 = uiElement1.getUnderlyingElement();
            IScenarioElement element2 = uiElement2.getUnderlyingElement();

            // sort elements of the same type
            return element1.compareTo(element2);
        }

        // sort different elements based on their type
        return super.compare(viewer, uiElement1.getSortPriority(), uiElement2.getSortPriority());
    }
}
