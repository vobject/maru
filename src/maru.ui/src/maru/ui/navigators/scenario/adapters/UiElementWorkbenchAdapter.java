package maru.ui.navigators.scenario.adapters;

import maru.ui.model.UiElement;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.model.WorkbenchAdapter;

public class UiElementWorkbenchAdapter extends WorkbenchAdapter
{
    @Override
    public String getLabel(Object object)
    {
        return ((UiElement) object).getName();
    }

    @Override
    public ImageDescriptor getImageDescriptor(Object object)
    {
        return ((UiElement) object).getImageDescriptor();
    }
}
