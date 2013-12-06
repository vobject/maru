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

public class ExternalTleSatelliteWizardNamingPage extends ScenarioElementWizardNamingPage
{
    private static final String PAGE_NAME = "New Satellite";
    private static final String PAGE_TITLE = "Satellite";
    private static final String PAGE_DESCRIPTION = "Create a new Satellite.";

    private Text tleData;

    public ExternalTleSatelliteWizardNamingPage(IScenarioProject project)
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

        // no need to set the name explicitly because the TLE data
        // will be used to name this satellite.
        getNameControl().setEnabled(false);

        new Label(container, SWT.NONE).setText("TLE:");
        tleData = new Text(container, SWT.BORDER | SWT.MULTI);
        tleData.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        tleData.setEnabled(false);

        setControl(container);
    }

    @Override
    protected String[] getElementImages()
    {
        return new String[] {
            MaruSpacecraftResources.SPACECRAFT_DEFAULT_128.getName(),
            MaruSpacecraftResources.SPACECRAFT_ISS_128.getName()
        };
    }

    public void setTleData(String name, String tleDataString)
    {
        getNameControl().setText(name);
        tleData.setText(tleDataString);
    }
}
