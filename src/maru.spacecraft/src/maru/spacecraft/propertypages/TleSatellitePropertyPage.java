package maru.spacecraft.propertypages;

import maru.core.model.IScenarioElement;
import maru.spacecraft.tle.InitialTLECoordinate;
import maru.spacecraft.tle.TLESatellite;
import maru.ui.propertypages.UiPropertyPage;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class TleSatellitePropertyPage extends UiPropertyPage
{
    private Text tle;

    @Override
    public TLESatellite getScenarioElement()
    {
        IScenarioElement element = super.getScenarioElement();
        if (element instanceof TLESatellite) {
            return (TLESatellite) element;
        } else {
            return null;
        }
    }

    @Override
    protected Composite createContents(Composite parent)
    {
        TLESatellite element = getScenarioElement();
        if (element == null) {
            new Label(parent, SWT.NONE).setText("The selected element is no TLESatellite.");
            return null;
        }

        tle = new Text(parent, SWT.BORDER | SWT.WRAP);
        tle.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        final Font outputTextFont = new Font(tle.getDisplay(), "Courier New", 9, SWT.NORMAL);
        tle.setFont(outputTextFont);
        tle.addDisposeListener(new DisposeListener() {
            @Override
            public void widgetDisposed(DisposeEvent e) {
                outputTextFont.dispose();
            }
        });

        InitialTLECoordinate coordinate = element.getInitialCoordinate();
        String tleString = coordinate.getTle().toString();
        tle.setText(tleString);

        return null;
    }
}
