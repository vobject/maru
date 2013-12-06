package maru.spacecraft.wizards;

import maru.IMaruPluginResource;
import maru.core.model.CoreModel;
import maru.core.model.IScenarioProject;
import maru.spacecraft.MaruSpacecraftResources;
import maru.spacecraft.tlesatellite.InitialTleCoordinate;
import maru.spacecraft.tlesatellite.Sgp4Propagator;
import maru.spacecraft.tlesatellite.TleSatellite;
import maru.ui.wizards.ScenarioElementWizard;

import org.orekit.errors.OrekitException;
import org.orekit.propagation.analytical.tle.TLE;

public class TleSatelliteWizard extends ScenarioElementWizard
{
    private TleSatelliteWizardPage mainPage;

    public TleSatelliteWizard()
    {
        super("Satellite Wizard");
    }

    @Override
    public void addPages()
    {
        // the wizard page will take care of invalid selections,
        // no need to check here.

        IScenarioProject project = getScenarioProjectFromSelection();
        mainPage = new TleSatelliteWizardPage(project);
        addPage(mainPage);
    }

    @Override
    public boolean performFinish()
    {
        CoreModel coreModel = CoreModel.getDefault();
        IScenarioProject scenarioProject = getScenarioProjectFromSelection();

        InitialTleCoordinate position;
        try {
            position = createInitialPosition();
        }
        catch (OrekitException e) {
            e.printStackTrace();
            return false;
        }

        TleSatellite satellite = new TleSatellite(position);
        Sgp4Propagator propagator = new Sgp4Propagator();
        String image = mainPage.getElementImage();

        coreModel.addSpacecraft(scenarioProject, satellite, false);
        coreModel.commentElement(satellite, mainPage.getElementComment(), false);
        coreModel.setElementColor(satellite, mainPage.getElementColor(), false);
        if (!image.isEmpty()) {
            IMaruPluginResource resource = MaruSpacecraftResources.fromName(image);
            coreModel.setElementGraphics2D(satellite, resource, false);
        }
        coreModel.setPropagator(satellite, propagator);
        coreModel.notifyElementAdded(satellite);
        return true;
    }

    private InitialTleCoordinate createInitialPosition() throws OrekitException
    {
        String name = mainPage.getLine0().trim();
        TLE tle = new TLE(mainPage.getLine1(), mainPage.getLine2());
        return new InitialTleCoordinate(name, tle);
    }
}
