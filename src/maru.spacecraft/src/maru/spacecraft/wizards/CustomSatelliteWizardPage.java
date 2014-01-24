package maru.spacecraft.wizards;

import maru.MaruRuntimeException;
import maru.core.model.IScenarioProject;
import maru.spacecraft.MaruSpacecraftResources;
import maru.spacecraft.controls.CartesianOrbitControls;
import maru.spacecraft.controls.CircularOrbitControls;
import maru.spacecraft.controls.EquinoctialOrbitControls;
import maru.spacecraft.controls.KeplerianOrbitControls;
import maru.spacecraft.controls.OrbitControls;
import maru.ui.wizards.ScenarioElementWizard;
import maru.ui.wizards.ScenarioElementWizardNamingPage;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.orekit.orbits.Orbit;
import org.orekit.orbits.OrbitType;

public class CustomSatelliteWizardPage extends ScenarioElementWizardNamingPage
{
    private static final String PAGE_NAME = "New Satellite";
    private static final String PAGE_TITLE = "Satellite";
    private static final String PAGE_DESCRIPTION = "Create a new Satellite.";

    private Composite orbitControlContainer;
    private Combo orbitType;
    private OrbitControls orbitControls;

    public CustomSatelliteWizardPage(IScenarioProject project)
    {
        super(PAGE_NAME, PAGE_TITLE, PAGE_DESCRIPTION, project);
    }

    @Override
    public void createControl(Composite parent)
    {
        Composite container = new Composite(parent, SWT.NONE);
        GridLayout containerLayout = new GridLayout(2, false);
        container.setLayout(containerLayout);

        super.createControl(container);
        ScenarioElementWizard.createLine(container, containerLayout.numColumns);

        // default entries so dummy entries can be created quicker
        getNameControl().setText("New Custom Satellite");

        orbitControlContainer = createControls(container);
        orbitControls = createOrbitControls(orbitControlContainer, getProject(), OrbitType.KEPLERIAN, null);

        setControl(container);
        setPageComplete(isInputValid());
    }

    public Orbit getOrbit()
    {
        return orbitControls.getOrbit();
    }

    @Override
    protected String[] getElementImages()
    {
        return new String[] {
            MaruSpacecraftResources.SPACECRAFT_DEFAULT_1.getName(),
            MaruSpacecraftResources.SPACECRAFT_DEFAULT_2.getName(),
            MaruSpacecraftResources.SPACECRAFT_DEFAULT_3.getName(),
            MaruSpacecraftResources.SPACECRAFT_ISS_1.getName(),
            MaruSpacecraftResources.SPACECRAFT_ISS_2.getName(),
            MaruSpacecraftResources.SPACECRAFT_ASTRONAUT_1.getName(),
            MaruSpacecraftResources.SPACECRAFT_ROCKET_1.getName(),
            MaruSpacecraftResources.SPACECRAFT_SHUTTLE_1.getName(),
            MaruSpacecraftResources.SPACECRAFT_SHUTTLE_2.getName(),
        };
    }

    @Override
    protected boolean isInputValid()
    {
        // let the parent class check its input first
        if (!super.isInputValid()) {
            return false;
        }

        // check if another satellite with the same name already exits!
        if (getProject().getSpacecraftContainer().hasChild(getElementName())) {
            setErrorMessage("An satellite with the same name already exists in the selected scenario.");
            return false;
        }

        setErrorMessage(null);
        return true;
    }

    private Composite createControls(Composite parent)
    {
        GridLayout layout = new GridLayout(2, false);
        layout.marginWidth = 0;
        layout.marginHeight = 0;

        GridData data = new GridData();
        data.verticalAlignment = SWT.FILL;
        data.horizontalAlignment = SWT.FILL;
        data.grabExcessVerticalSpace = true;
        data.grabExcessHorizontalSpace = true;
        data.horizontalSpan = 2;

        Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(layout);
        container.setLayoutData(data);

        new Label(container, SWT.NONE).setText("Change orbit type:");
        orbitType = new Combo(container, SWT.BORDER | SWT.READ_ONLY);
        orbitType.setItems(getOrbitTypes());
        orbitType.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        orbitType.select(getOrbitTypeIndex(OrbitType.KEPLERIAN));
        orbitType.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                orbitControls.dispose();
                orbitControls = createOrbitControls(orbitControlContainer,
                                                    getProject(),
                                                    getSelectedOrbitType(),
                                                    null);
                orbitControlContainer.getParent().getParent().getParent().layout(true, true);
            }
        });

        return container;
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
}
