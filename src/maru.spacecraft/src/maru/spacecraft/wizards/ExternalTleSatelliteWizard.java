package maru.spacecraft.wizards;

import maru.core.model.CoreModel;
import maru.core.model.IScenarioProject;
import maru.core.model.VisibleElementColor;
import maru.core.model.resource.IMaruResource;
import maru.spacecraft.model.SpacecraftResources;
import maru.spacecraft.model.tle.InitialTLECoordinate;
import maru.spacecraft.model.tle.SGP4Propagator;
import maru.spacecraft.model.tle.TLESatellite;
import maru.ui.wizards.ScenarioElementWizard;

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
            VisibleElementColor color = satelliteNamingPage.getElementColor();
            String imageName = satelliteNamingPage.getElementImage();
            IMaruResource image = null;
            if ((imageName != null) && !imageName.isEmpty()) {
                image = SpacecraftResources.fromName(imageName);
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
