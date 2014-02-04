package maru.spacecraft.wizards;

import maru.IMaruResource;
import maru.core.model.CoreModel;
import maru.core.model.IScenarioProject;
import maru.spacecraft.MaruSpacecraftResources;
import maru.spacecraft.tle.InitialTLECoordinate;
import maru.spacecraft.tle.SGP4Propagator;
import maru.spacecraft.tle.TLESatellite;
import maru.ui.wizards.ScenarioElementWizard;

import org.eclipse.swt.graphics.RGB;
import org.orekit.errors.OrekitException;

public class ExternalTleSatelliteWizard extends ScenarioElementWizard
{
    private ExternalTleSatelliteWizardSelectionPage tleSelectionPage;
    private ExternalTleSatelliteWizardNamingPage satelliteNamingPage;

    public ExternalTleSatelliteWizard()
    {
        super("Satellite Wizard");
    }

    @Override
    public void addPages()
    {
        IScenarioProject project = getScenarioProjectFromSelection();
        tleSelectionPage = new ExternalTleSatelliteWizardSelectionPage(project);
        satelliteNamingPage = new ExternalTleSatelliteWizardNamingPage(project);

        // the tle page needs to manipulate the naming page based on the
        // selection done in the tle page.
        tleSelectionPage.setNamingPage(satelliteNamingPage);

        addPage(tleSelectionPage);
        addPage(satelliteNamingPage);
    }

    @Override
    public boolean performFinish()
    {
        try
        {
            IScenarioProject scenario = getScenarioProjectFromSelection();

            String comment = satelliteNamingPage.getElementComment();
            RGB color = satelliteNamingPage.getElementColor();
            String imageName = satelliteNamingPage.getElementImage();
            IMaruResource image = null;
            if ((imageName != null) && !imageName.isEmpty()) {
                image = MaruSpacecraftResources.fromName(imageName);
            }
            InitialTLECoordinate initialCoordinate = tleSelectionPage.getInitialTlePosition();
            SGP4Propagator propagator = new SGP4Propagator();

            TLESatellite satellite = new TLESatellite(initialCoordinate.getName());
            satellite.setElementComment(comment);
            satellite.setElementColor(color);
            satellite.setElementImage(image);
            satellite.setInitialCoordinate(initialCoordinate);
            satellite.setPropagator(propagator);

            CoreModel coreModel = CoreModel.getDefault();
            coreModel.addSpacecraft(scenario, satellite, true);
            return true;
        }
        catch (OrekitException e)
        {
            e.printStackTrace();
            return false;
        }
    }
}
