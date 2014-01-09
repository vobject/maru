package maru.spacecraft.wizards;

import maru.core.model.IScenarioProject;
import maru.spacecraft.MaruSpacecraftResources;
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

    private static final int LINE0_LENGTH = 24;
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

        // no need to set the name explicitly because the TLE data
        // will be used to name this satellite.
        getNameControl().setEnabled(false);

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
        if (getProject() == null) {
            setErrorMessage("No scenario selected in the workspace.");
            return false;
        }

        if ((getTleLines().length < 3)
            || (getLine0().length() != LINE0_LENGTH)
            || (getLine1().length() != LINE1_LENGTH)
            || (getLine2().length() != LINE2_LENGTH))
        {
            setErrorMessage("Invalid TLE.");
            return false;
        }
        getNameControl().setText(getLine0());

        // check if another satellite with the same name already exits!
        if (getProject().getSpacecraftContainer().hasChild(getElementName())) {
            setErrorMessage("An satellite with the same name already exists in the selected scenario.");
            return false;
        }

        setErrorMessage(null);
        return true;
    }

    public String getLine0()
    {
        return getTleLines()[0];
    }

    public String getLine1()
    {
        return getTleLines()[1];
    }

    public String getLine2()
    {
        return getTleLines()[2];
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

    // TODO: make this class abstract and create wizards for concrete
    // satellite types! then add additionally needed getter to the subclasses.
}
