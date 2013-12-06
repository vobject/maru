package maru.ui.navigators.scenario;

import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.navigator.IDescriptionProvider;

public class LabelProvider extends WorkbenchLabelProvider implements IDescriptionProvider
{
    @Override
    public String getDescription(Object element)
    {
        return getText(element);
    }
}
