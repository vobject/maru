package maru.centralbody.propertypages;

import maru.centralbody.earth.Earth;
import maru.ui.propertypages.UiPropertyPage;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

public class EarthPropertyPage extends UiPropertyPage
{
    @Override
    public Earth getScenarioElement()
    {
        return (Earth) getUiElement().getUnderlyingElement();
    }

    @Override
    protected Control createContents(Composite parent)
    {
        new Label(parent, SWT.NONE).setText("Earth specific properties.");
        return null;
    }
}
