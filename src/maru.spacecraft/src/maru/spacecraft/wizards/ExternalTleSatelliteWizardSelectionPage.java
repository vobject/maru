package maru.spacecraft.wizards;

import java.util.Collections;
import java.util.Comparator;

import maru.core.model.IScenarioProject;
import maru.spacecraft.tlesatellite.InitialTleCoordinate;
import maru.spacecraft.utils.TleUtils;
import maru.ui.wizards.ScenarioElementWizardPage;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.orekit.errors.OrekitException;
import org.orekit.propagation.analytical.tle.TLE;

public class ExternalTleSatelliteWizardSelectionPage extends ScenarioElementWizardPage
{
    private static final String PAGE_NAME = "New Satellite";
    private static final String PAGE_TITLE = "Satellite";
    private static final String PAGE_DESCRIPTION = "Create a new Satellite.";

    private ExternalTleSatelliteWizardNamingPage namingPage;

    private List tleSources;
    private List sourceSatellites;

    private InitialTleCoordinate selectedPosition;

    public ExternalTleSatelliteWizardSelectionPage(IScenarioProject project)
    {
        super(PAGE_NAME, PAGE_TITLE, PAGE_DESCRIPTION, project);
    }

    @Override
    public void createControl(Composite parent)
    {
        Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(new GridLayout(2, true));

        tleSources = new List(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        tleSources.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        tleSources.setItems(TleUtils.getTleSources());
        tleSources.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                if (getProject() == null) {
                    setErrorMessage("No scenario selected in the workspace.");
                    setPageComplete(false);
                }

                sourceSatellites.removeAll();

                int selection = tleSources.getSelectionIndex();
                String source = tleSources.getItem(selection);
                java.util.List<InitialTleCoordinate> positions = TleUtils.parseTleSource(source);

                Collections.sort(positions, new Comparator<InitialTleCoordinate>() {
                    @Override
                    public int compare(InitialTleCoordinate o1, InitialTleCoordinate o2)
                    {
                        return o1.getName().compareTo(o2.getName());
                    }
                });

                for (InitialTleCoordinate position : positions) {
                    sourceSatellites.add(position.getName());
                    sourceSatellites.setData(position.getName(), position);
                }
            }
        });

        sourceSatellites = new List(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        sourceSatellites.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        sourceSatellites.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                if (getProject() == null) {
                    setErrorMessage("No scenario selected in the workspace.");
                    setPageComplete(false);
                    return;
                }

                int selection = sourceSatellites.getSelectionIndex();
                String satelliteName = sourceSatellites.getItem(selection);

                // check if another satellite with the same name already exits!
                if (getProject().getSpacecraftContainer().hasChild(satelliteName)) {
                    setErrorMessage("An satellite with the same name already exists in the selected scenario.");
                    setPageComplete(false);
                    return;
                }

                selectedPosition = (InitialTleCoordinate) sourceSatellites.getData(satelliteName);
                namingPage.setTleData(selectedPosition.getName(), tleToString(selectedPosition.getTle()));
                namingPage.setPageComplete(true);
                setErrorMessage(null);
                setPageComplete(true);
            }
        });

        setControl(container);
        setPageComplete(false);
    }

    public void setNamingPage(ExternalTleSatelliteWizardNamingPage namingPage)
    {
        this.namingPage = namingPage;
    }

    public InitialTleCoordinate getInitialTlePosition()
    {
        return selectedPosition;
    }

    private String tleToString(TLE tle)
    {
        try
        {
            return tle.getLine1() + "\r\n" + tle.getLine2();
        }
        catch (OrekitException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
