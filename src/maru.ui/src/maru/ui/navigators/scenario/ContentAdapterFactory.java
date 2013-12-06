package maru.ui.navigators.scenario;

import maru.ui.model.UiElement;
import maru.ui.navigators.scenario.adapters.UiElementWorkbenchAdapter;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.model.IWorkbenchAdapter;

public class ContentAdapterFactory implements IAdapterFactory
{
    private static final Class<?>[] ADAPTERS = { IWorkbenchAdapter.class };

    private final Object uiElementWorkbenchAdapter = new UiElementWorkbenchAdapter();

    @Override
    public Object getAdapter(Object adaptableObject, @SuppressWarnings("rawtypes") Class adapterType)
    {
        if (IWorkbenchAdapter.class.equals(adapterType))
        {
            if (adaptableObject instanceof UiElement) {
                return uiElementWorkbenchAdapter;
            }
        }
        return null;
    }

    @Override
    public Class<?>[] getAdapterList()
    {
        return ADAPTERS;
    }
}
