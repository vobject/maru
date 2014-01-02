package maru.ui.navigators.scenario.adapters;

import maru.core.utils.TimeUtil;
import maru.ui.model.UiElement;
import maru.ui.model.UiTimepoint;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.model.WorkbenchAdapter;

public class UiElementWorkbenchAdapter extends WorkbenchAdapter
{
    @Override
    public String getLabel(Object object)
    {
        if (object instanceof UiTimepoint) {
            UiTimepoint tp = (UiTimepoint) object;
            return TimeUtil.asISO8601(tp.getTime());
        } else {
            return ((UiElement) object).getName();
        }
    }

    @Override
    public ImageDescriptor getImageDescriptor(Object object)
    {
        return ((UiElement) object).getImageDescriptor();
    }
}
