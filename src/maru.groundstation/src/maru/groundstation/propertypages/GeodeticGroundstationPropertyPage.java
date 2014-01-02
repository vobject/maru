package maru.groundstation.propertypages;

import maru.groundstation.earth.GeodeticGroundstation;
import maru.ui.propertypages.UiPropertyPage;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

public class GeodeticGroundstationPropertyPage extends UiPropertyPage
{
    @Override
    public GeodeticGroundstation getScenarioElement()
    {
        return (GeodeticGroundstation) getUiElement().getUnderlyingElement();
    }

    @Override
    protected Control createContents(Composite parent)
    {
        new Label(parent, SWT.NONE).setText("GeodeticGroundstation specific properties.");
        return null;
    }
}
