package maru.spacecraft.propertypages;

import maru.core.model.IScenarioElement;
import maru.spacecraft.tlesatellite.TleSatellite;
import maru.ui.propertypages.UiPropertyPage;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class TleSatellitePropertyPage extends UiPropertyPage
{
    @Override
    public TleSatellite getScenarioElement()
    {
        IScenarioElement element = super.getScenarioElement();
        if (element instanceof TleSatellite) {
            return (TleSatellite) element;
        } else {
            return null;
        }
    }

    @Override
    protected Composite createContents(Composite parent)
    {
        if (getScenarioElement() == null) {
            new Label(parent, SWT.NONE).setText("The selected element is no TLESatellite.");
            return null;
        }

        new Label(parent, SWT.NONE).setText("TleSatellite specific properties.");
        return null;
    }
}
