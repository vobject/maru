package maru.spacecraft.wizards;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

import maru.core.model.IScenarioProject;
import maru.spacecraft.tle.InitialTLECoordinate;
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

    private String selectedName;
    private TLE selectedPosition;

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
                java.util.List<Map.Entry<String, TLE>> positions = TleUtils.parseTleSource(source);

                Collections.sort(positions, new Comparator<Map.Entry<String, TLE>>() {
                    @Override
                    public int compare(Map.Entry<String, TLE> o1, Map.Entry<String, TLE> o2)
                    {
                        return o1.getKey().compareTo(o2.getKey());
                    }
                });

                for (Map.Entry<String, TLE> position : positions) {
                    sourceSatellites.add(position.getKey());
                    sourceSatellites.setData(position.getKey(), position.getValue());
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

                selectedName = satelliteName;
                selectedPosition = (TLE) sourceSatellites.getData(satelliteName);
                namingPage.setTleData(selectedName, tleToString(selectedPosition));
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

    public InitialTLECoordinate getInitialTlePosition()
    {
        try
        {
            return new InitialTLECoordinate(getProject().getCentralBody(),
                                            selectedName,
                                            selectedPosition);
        }
        catch (OrekitException e)
        {
            e.printStackTrace();
            return null;
        }
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
