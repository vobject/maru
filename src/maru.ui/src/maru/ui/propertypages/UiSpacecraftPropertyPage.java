package maru.ui.propertypages;

import maru.core.model.ISpacecraft;
import maru.ui.model.UiSpacecraft;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class UiSpacecraftPropertyPage extends UiPropertyPage
{
    @Override
    public UiSpacecraft getUiElement()
    {
        return (UiSpacecraft) getElement().getAdapter(UiSpacecraft.class);
    }

    @Override
    public ISpacecraft getScenarioElement()
    {
        return getUiElement().getUnderlyingElement();
    }

    @Override
    protected Composite createContents(Composite parent)
    {
        new Label(parent, SWT.NONE).setText("Spacecraft specific properties.");
        return null;
    }
}
