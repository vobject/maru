package maru.groundstation.wizards;

import maru.core.model.IScenarioProject;
import maru.groundstation.controls.GroundstationControls;
import maru.groundstation.model.GroundstationResources;
import maru.ui.wizards.ScenarioElementWizard;
import maru.ui.wizards.ScenarioElementWizardNamingPage;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.orekit.bodies.GeodeticPoint;

public class GroundstationWizardPage extends ScenarioElementWizardNamingPage
{
    private static final String PAGE_NAME = "New Geodedic Groundstation";
    private static final String PAGE_TITLE = "Geodedic Groundstation";
    private static final String PAGE_DESCRIPTION = "Create a new geodedic grundstation.";

    private Composite gsControlContainer;
    private GroundstationControls gsControls;

    public GroundstationWizardPage(IScenarioProject project)
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

        // default entries so dummy elements can be created quicker
        getNameControl().setText("New Groundstation");

        gsControlContainer = createControls(container);
        gsControls = createGroundstationControls(gsControlContainer, getProject());

        setControl(container);
        setPageComplete(isInputValid());
    }

    public GeodeticPoint getGeodeticPoint()
    {
        return gsControls.getGeodeticPoint();
    }

    public double getElevationAngle()
    {
        return gsControls.getElevationAngle();
    }

    @Override
    protected String[] getElementImages()
    {
        return new String[] {
            GroundstationResources.GROUNDSTATION_ANTENNA_1.getName(),
            GroundstationResources.GROUNDSTATION_ANTENNA_2.getName(),
            GroundstationResources.GROUNDSTATION_ANTENNA_3.getName(),
            GroundstationResources.GROUNDSTATION_ANTENNA_4.getName(),
            GroundstationResources.GROUNDSTATION_ANTENNA_5.getName(),
            GroundstationResources.GROUNDSTATION_MARKER_1.getName(),
            GroundstationResources.GROUNDSTATION_MARKER_2.getName(),
        };
    }

    @Override
    protected boolean isInputValid()
    {
        // let the parent class check its input first
        if (!super.isInputValid()) {
            return false;
        }

        // check if another groundstation with the same name already exits!
        if (getProject().getGroundstationContainer().hasChild(getName())) {
            setErrorMessage("An groundstation with the same name already exists in the selected scenario.");
            return false;
        }

        // FIXME: is not called because it is only triggered when
        // a control of this page changes, but not one that is located
        // inside the GroundstationControls object.
        if (!gsControls.isValid()) {
            setErrorMessage(gsControls.getErrorMessage());
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

        return container;
    }

    private GroundstationControls createGroundstationControls(Composite container, IScenarioProject scenario)
    {
        return new GroundstationControls(container, scenario);
    }
}
