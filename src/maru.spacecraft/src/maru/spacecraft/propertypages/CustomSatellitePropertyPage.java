package maru.spacecraft.propertypages;

import maru.core.model.CoreModel;
import maru.core.model.ICentralBody;
import maru.core.model.IScenarioElement;
import maru.core.model.IScenarioProject;
import maru.core.model.MaruRuntimeException;
import maru.spacecraft.controls.CartesianOrbitControls;
import maru.spacecraft.controls.CircularOrbitControls;
import maru.spacecraft.controls.EquinoctialOrbitControls;
import maru.spacecraft.controls.KeplerianOrbitControls;
import maru.spacecraft.controls.OrbitControls;
import maru.spacecraft.model.custom.CustomSatellite;
import maru.spacecraft.model.custom.InitialCustomCoordinate;
import maru.ui.propertypages.UiPropertyPage;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.orekit.errors.OrekitException;
import org.orekit.orbits.Orbit;
import org.orekit.orbits.OrbitType;

public class CustomSatellitePropertyPage extends UiPropertyPage
{
    private Composite orbitControlContainer;
    private Combo orbitType;
    private OrbitControls orbitControls;

    private String initialOrbitType;

    @Override
    public CustomSatellite getScenarioElement()
    {
        IScenarioElement element = super.getScenarioElement();
        if (element instanceof CustomSatellite) {
            return (CustomSatellite) element;
        } else {
            return null;
        }
    }

    @Override
    public boolean performOk()
    {
        if (!super.performOk()) {
            return false;
        }

        if (orbitControls.isValid()) {
            setErrorMessage(null);
        } else {
            setErrorMessage(orbitControls.getErrorMessage());
            return false;
        }

        if (hasOrbitTypeChanged() || orbitControls.isModified())
        {
            Orbit newOrbit = orbitControls.getOrbit();

            CustomSatellite element = getScenarioElement();
            IScenarioProject scenario = element.getScenarioProject();
            ICentralBody centralBody = scenario.getCentralBody();

            try
            {
                InitialCustomCoordinate newCoordinate = new InitialCustomCoordinate(centralBody, newOrbit);
                CoreModel.getDefault().changeInitialCoordinate(element, newCoordinate, true);
            }
            catch (OrekitException e)
            {
                e.printStackTrace();
                setErrorMessage("Error while trying to create new orbit.");
            }

            orbitControls.refreshDefaults(scenario, newOrbit);
        }

        initDefaults();
        return true;
    }

    @Override
    protected Composite createContents(Composite parent)
    {
        CustomSatellite element = getScenarioElement();
        if (element == null) {
            // this case should not happen if the property page filter
            // in the plugin.xml is configured correctly
            new Label(parent, SWT.NONE).setText("The selected element is no CustomSatellite.");
            return null;
        }
        IScenarioProject scenario = element.getScenarioProject();
        InitialCustomCoordinate coordinate = element.getInitialCoordinate();
        Orbit orbit = coordinate.getOrbit();
        OrbitType type = coordinate.getOrbitType();

        orbitControlContainer = createControls(parent);
        initDefaults();
        initControls();

        orbitControls = createOrbitControls(orbitControlContainer, scenario, type, orbit);

        return orbitControlContainer;
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

        new Label(container, SWT.NONE).setText("Change orbit type:");
        orbitType = new Combo(container, SWT.BORDER | SWT.READ_ONLY);
        orbitType.setItems(getOrbitTypes());
        orbitType.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        return container;
    }

    private void initDefaults()
    {
        CustomSatellite element = getScenarioElement();
        if (element == null) {
            return;
        }
        InitialCustomCoordinate coordinate = element.getInitialCoordinate();

        initialOrbitType = coordinate.getOrbitType().toString();
    }

    private void initControls()
    {
        CustomSatellite element = getScenarioElement();
        if (element == null) {
            return;
        }
        InitialCustomCoordinate coordinate = element.getInitialCoordinate();

        orbitType.select(getOrbitTypeIndex(coordinate.getOrbitType()));
        orbitType.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                CustomSatellite element = getScenarioElement();
                InitialCustomCoordinate coordinate = element.getInitialCoordinate();

                orbitControls.dispose();
                orbitControls = createOrbitControls(orbitControlContainer,
                                                    element.getScenarioProject(),
                                                    getSelectedOrbitType(),
                                                    coordinate.getOrbit());
                orbitControlContainer.getParent().getParent().layout(true, true);
                orbitControlContainer.getParent().layout(true, true);
                orbitControlContainer.layout(true, true);
            }
        });
    }

    private OrbitControls createOrbitControls(Composite container, IScenarioProject scenario, OrbitType type, Orbit orbit)
    {
        switch (type)
        {
            case CARTESIAN:
                return new CartesianOrbitControls(container, scenario, orbit);
            case CIRCULAR:
                return new CircularOrbitControls(container, scenario, orbit);
            case EQUINOCTIAL:
                return new EquinoctialOrbitControls(container, scenario, orbit);
            case KEPLERIAN:
                return new KeplerianOrbitControls(container, scenario, orbit);
        }

        throw new MaruRuntimeException("Invalid orbit type.");
    }

    private String[] getOrbitTypes()
    {
        return new String[] {
            OrbitType.CARTESIAN.toString(),
            OrbitType.CIRCULAR.toString(),
            OrbitType.EQUINOCTIAL.toString(),
            OrbitType.KEPLERIAN.toString()
        };
    }

    private int getOrbitTypeIndex(OrbitType type)
    {
        switch (type)
        {
            case CARTESIAN:
                return 0;
            case CIRCULAR:
                return 1;
            case EQUINOCTIAL:
                return 2;
            case KEPLERIAN:
                return 3;
        }

        throw new MaruRuntimeException("Invalid orbit type.");
    }

    private OrbitType getOrbitTypeFromString(String orbitType)
    {
        return OrbitType.valueOf(orbitType);
    }

    private OrbitType getSelectedOrbitType()
    {
        return getOrbitTypeFromString(orbitType.getText());
    }

    private boolean hasOrbitTypeChanged()
    {
        String newOrbitType = orbitType.getText();
        if (!newOrbitType.equals(initialOrbitType)) {
            return true;
        }

        return false;
    }
}
