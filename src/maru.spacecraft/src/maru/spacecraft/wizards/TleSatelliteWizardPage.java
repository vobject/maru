package maru.spacecraft.wizards;

import maru.core.model.IScenarioProject;
import maru.spacecraft.model.SpacecraftResources;
import maru.ui.wizards.ScenarioElementWizard;
import maru.ui.wizards.ScenarioElementWizardNamingPage;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class TleSatelliteWizardPage extends ScenarioElementWizardNamingPage
{
    private static final String PAGE_NAME = "New Satellite";
    private static final String PAGE_TITLE = "Satellite";
    private static final String PAGE_DESCRIPTION = "Create a new Satellite.";

    private static final int LINE1_LENGTH = 69;
    private static final int LINE2_LENGTH = 69;

    private Text tleData;

    public TleSatelliteWizardPage(IScenarioProject project)
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
        getNameControl().setText("New Satellite");

        new Label(container, SWT.NONE).setText("TLE:");
        tleData = new Text(container, SWT.BORDER | SWT.MULTI);
        tleData.addKeyListener(inputValidation);
        tleData.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        setControl(container);
        setPageComplete(isInputValid());
    }

    @Override
    protected String[] getElementImages()
    {
        return new String[] {
            SpacecraftResources.SPACECRAFT_DEFAULT_1.getName(),
            SpacecraftResources.SPACECRAFT_DEFAULT_2.getName(),
            SpacecraftResources.SPACECRAFT_DEFAULT_3.getName(),
            SpacecraftResources.SPACECRAFT_ISS_1.getName(),
            SpacecraftResources.SPACECRAFT_ISS_2.getName(),
            SpacecraftResources.SPACECRAFT_ASTRONAUT_1.getName(),
            SpacecraftResources.SPACECRAFT_ROCKET_1.getName(),
            SpacecraftResources.SPACECRAFT_SHUTTLE_1.getName(),
            SpacecraftResources.SPACECRAFT_SHUTTLE_2.getName(),
        };
    }

    @Override
    protected boolean isInputValid()
    {
        // let the parent class check its input first
        if (!super.isInputValid()) {
            return false;
        }

        if ((getTleLines().length != 2) ||
            (getLine1().length() != LINE1_LENGTH) ||
            (getLine2().length() != LINE2_LENGTH))
        {
            setErrorMessage("Invalid TLE.");
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

    public String getLine1()
    {
        return getTleLines()[0];
    }

    public String getLine2()
    {
        return getTleLines()[1];
    }

    private String[] getTleLines()
    {
        String[] lines = tleData.getText().split("\n");

        if (lines.length > 0) {
            lines[0] = lines[0].replaceAll("\r", "");
            lines[0] = lines[0].replaceAll("\n", "");
        }
        if (lines.length > 1) {
            lines[1] = lines[1].replaceAll("\r", "");
            lines[1] = lines[1].replaceAll("\n", "");
        }
        return lines;
    }
}
