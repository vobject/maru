package maru.ui.propertypages;

import maru.core.model.ICentralBody;
import maru.ui.model.UiCentralBody;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

public class UiCentralBodyPropertyPage extends UiPropertyPage
{
    @Override
    public UiCentralBody getUiElement()
    {
        return (UiCentralBody) getElement().getAdapter(UiCentralBody.class);
    }

    @Override
    public ICentralBody getScenarioElement()
    {
        return getUiElement().getUnderlyingElement();
    }

    @Override
    protected Control createContents(Composite parent)
    {
        new Label(parent, SWT.NONE).setText("CentralBody specific properties.");
        return null;
    }
}
