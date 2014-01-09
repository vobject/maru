package maru.spacecraft.wizards;

import maru.IMaruPluginResource;
import maru.core.model.CoreModel;
import maru.core.model.IScenarioProject;
import maru.spacecraft.MaruSpacecraftResources;
import maru.spacecraft.tlesatellite.InitialTleCoordinate;
import maru.spacecraft.tlesatellite.Sgp4Propagator;
import maru.spacecraft.tlesatellite.TleSatellite;
import maru.ui.wizards.ScenarioElementWizard;

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
        CoreModel coreModel = CoreModel.getDefault();
        IScenarioProject scenarioProject = getScenarioProjectFromSelection();

        InitialTleCoordinate coordinate = tleSelectionPage.getInitialTlePosition();
        TleSatellite satellite = new TleSatellite(coordinate);
        Sgp4Propagator propagator = new Sgp4Propagator();
        String image = satelliteNamingPage.getElementImage();

        coreModel.addSpacecraft(scenarioProject, satellite, false);
        coreModel.commentElement(satellite, satelliteNamingPage.getElementComment(), false);
        coreModel.changeColor(satellite, satelliteNamingPage.getElementColor(), false);
        if (!image.isEmpty()) {
            IMaruPluginResource resource = MaruSpacecraftResources.fromName(image);
            coreModel.changeImage(satellite, resource, false);
        }
        coreModel.setPropagator(satellite, propagator);
        coreModel.notifyElementAdded(satellite);
        return true;
    }
}
