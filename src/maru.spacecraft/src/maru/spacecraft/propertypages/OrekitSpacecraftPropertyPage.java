package maru.spacecraft.propertypages;

import maru.spacecraft.OrekitSpacecraft;
import maru.ui.propertypages.UiPropertyPage;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

public class OrekitSpacecraftPropertyPage extends UiPropertyPage
{
    @Override
    public OrekitSpacecraft getScenarioElement()
    {
        return (OrekitSpacecraft) getUiElement().getUnderlyingElement();
    }

    @Override
    protected Control createContents(Composite parent)
    {
        new Label(parent, SWT.NONE).setText("OrekitSpacecraft specific properties.");
        return null;
    }
}
