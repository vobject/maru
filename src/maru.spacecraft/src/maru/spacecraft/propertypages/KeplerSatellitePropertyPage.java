package maru.spacecraft.propertypages;

import maru.core.model.IScenarioElement;
import maru.spacecraft.ckesatellite.KeplerSatellite;
import maru.ui.propertypages.UiPropertyPage;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

public class KeplerSatellitePropertyPage extends UiPropertyPage
{
    @Override
    public KeplerSatellite getScenarioElement()
    {
        IScenarioElement element = super.getScenarioElement();
        if (element instanceof KeplerSatellite) {
            return (KeplerSatellite) element;
        } else {
            return null;
        }
    }

    @Override
    protected Control createContents(Composite parent)
    {
        if (getScenarioElement() == null) {
            new Label(parent, SWT.NONE).setText("The selected element is no KeplerSatellite.");
            return null;
        }

        new Label(parent, SWT.NONE).setText("KeplerSatellite specific properties.");
        return null;
    }
}
