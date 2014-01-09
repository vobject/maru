package maru.ui.propertypages;

import maru.core.model.IGroundstation;
import maru.ui.model.UiGroundstation;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class UiGroundstationPropertyPage extends UiPropertyPage
{
    @Override
    public UiGroundstation getUiElement()
    {
        return (UiGroundstation) getElement().getAdapter(UiGroundstation.class);
    }

    @Override
    public IGroundstation getScenarioElement()
    {
        return getUiElement().getUnderlyingElement();
    }

    @Override
    protected Composite createContents(Composite parent)
    {
        new Label(parent, SWT.NONE).setText("Groundstation specific properties.");
        return null;
    }
}
