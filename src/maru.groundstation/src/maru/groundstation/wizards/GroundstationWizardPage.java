package maru.groundstation.wizards;

import maru.core.model.IScenarioProject;
import maru.groundstation.MaruGroundstationResources;
import maru.ui.wizards.ScenarioElementWizard;
import maru.ui.wizards.ScenarioElementWizardNamingPage;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class GroundstationWizardPage extends ScenarioElementWizardNamingPage
{
    private static final String PAGE_NAME = "New Geodedic Groundstation";
    private static final String PAGE_TITLE = "Geodedic Groundstation";
    private static final String PAGE_DESCRIPTION = "Create a new geodedic grundstation.";

    private Text latitude;
    private Text longitude;
    private Text altitude;
    private Text elevation;

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

        new Label(container, SWT.NONE).setText("Latitude (deg):");
        latitude = new Text(container, SWT.BORDER | SWT.SINGLE);
        latitude.setText("49.78186646");
        latitude.addKeyListener(inputValidation);
        latitude.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        new Label(container, SWT.NONE).setText("Longitude (deg):");
        longitude = new Text(container, SWT.BORDER | SWT.SINGLE);
        longitude.setText("9.97290914");
        longitude.addKeyListener(inputValidation);
        longitude.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        new Label(container, SWT.NONE).setText("Altitude (m):");
        altitude = new Text(container, SWT.BORDER | SWT.SINGLE);
        altitude.setText("274.68");
        altitude.addKeyListener(inputValidation);
        altitude.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        new Label(container, SWT.NONE).setText("Elevation (deg):");
        elevation = new Text(container, SWT.BORDER | SWT.SINGLE);
        elevation.setText("5.0");
        elevation.addKeyListener(inputValidation);
        elevation.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        setControl(container);
        setPageComplete(isInputValid());
    }

    @Override
    protected String[] getElementImages()
    {
        return new String[] {
            MaruGroundstationResources.GROUNDSTATION_DEFAULT_128.getName()
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

        try
        {
            Double.parseDouble(latitude.getText());
            Double.parseDouble(longitude.getText());
            Double.parseDouble(altitude.getText());
            Double.parseDouble(elevation.getText());
        }
        catch (NumberFormatException e)
        {
            setErrorMessage("Invalid coordinates.");
            return false;
        }

        setErrorMessage(null);
        return true;
    }

    public double getLatitude()
    {
        return Math.toRadians(Double.parseDouble(latitude.getText()));
    }

    public double getLongitude()
    {
        return Math.toRadians(Double.parseDouble(longitude.getText()));
    }

    public double getAltitude()
    {
        return Double.parseDouble(altitude.getText());
    }

    public double getElevation()
    {
        return Math.toRadians(Double.parseDouble(elevation.getText()));
    }
}
