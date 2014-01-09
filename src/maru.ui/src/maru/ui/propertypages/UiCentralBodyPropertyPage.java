package maru.ui.propertypages;

import maru.core.model.CoreModel;
import maru.core.model.ICentralBody;
import maru.ui.model.UiCentralBody;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class UiCentralBodyPropertyPage extends UiPropertyPage
{
    private Text gm;
    private Text equatorialRadius;
    private Text flattening;

    // use strings to hold the initial values because they can be compared.
    // the job would be trickier with doubles
    private String initialGM;
    private String initialEquatorialRadius;
    private String initialFlattening;

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
    public boolean performOk()
    {
        ICentralBody element = getScenarioElement();

        String newGM = gm.getText();
        if (!newGM.equals(initialGM)) {
            double value = Double.parseDouble(newGM);
            CoreModel.getDefault().changeCentralBodyGM(element, value, true);
        }

        String newEquatorialRadius = equatorialRadius.getText();
        if (!newEquatorialRadius.equals(initialEquatorialRadius)) {
            double value = Double.parseDouble(newEquatorialRadius) * 1000.0;
            CoreModel.getDefault().changeCentralBodyEquatorialRadius(element, value, true);
        }

        String newFlattening = flattening.getText();
        if (!newFlattening.equals(initialFlattening)) {
            double value = Double.parseDouble(newFlattening);
            CoreModel.getDefault().changeCentralBodyFlattening(element, value, true);
        }

        // we might have been called from performApply(). re-init defaults.
        initDefaults();
        return true;
    }

    @Override
    protected Composite createContents(Composite parent)
    {
        Composite container = createControls(parent);

        initDefaults();
        initControls();

        return container;
    }

    private Composite createControls(Composite parent)
    {
        GridLayout layout = new GridLayout(2, false);
        layout.marginWidth = 0;
        layout.marginHeight = 0;

        GridData data = new GridData();
        data.verticalAlignment = SWT.FILL;
        data.grabExcessVerticalSpace = true;
        data.horizontalSpan = 2;

        Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(layout);
        container.setLayoutData(data);

        new Label(container, SWT.NONE).setText("Equatorial Radius (km):");
        equatorialRadius = new Text(container, SWT.BORDER | SWT.SINGLE | SWT.WRAP);
        equatorialRadius.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        new Label(container, SWT.NONE).setText("Flattening:");
        flattening = new Text(container, SWT.BORDER | SWT.SINGLE | SWT.WRAP);
        flattening.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        new Label(container, SWT.NONE).setText("GM (m\u00b3/s\u00b2):");
        gm = new Text(container, SWT.BORDER | SWT.SINGLE | SWT.WRAP);
        gm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        return container;
    }

    private void initDefaults()
    {
        ICentralBody element = getScenarioElement();
        if (element == null) {
            return;
        }

        initialGM = Double.toString(element.getGM());
        initialEquatorialRadius = Double.toString(element.getEquatorialRadius() / 1000.0);
        initialFlattening = Double.toString(element.getFlattening());
    }

    private void initControls()
    {
        ICentralBody element = getScenarioElement();
        if (element == null) {
            return;
        }

        gm.setText(initialGM);
        equatorialRadius.setText(initialEquatorialRadius);
        flattening.setText(initialFlattening);
    }
}
