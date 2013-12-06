package maru.groundstation.wizards;

import maru.IMaruPluginResource;
import maru.core.MaruCorePlugin;
import maru.core.model.CoreModel;
import maru.core.model.ICentralBody;
import maru.core.model.IScenarioProject;
import maru.groundstation.MaruGroundstationResources;
import maru.groundstation.earth.GeodeticGroundstation;
import maru.groundstation.earth.GeodeticCoordinate;
import maru.groundstation.earth.GeodeticGroundstationPropagator;
import maru.ui.wizards.ScenarioElementWizard;

import org.eclipse.swt.graphics.RGB;

public class GroundstationWizard extends ScenarioElementWizard
{
    private GroundstationWizardPage mainPage;

    public GroundstationWizard()
    {
        super("Groundstation Wizard");
    }

    @Override
    public void addPages()
    {
        // the wizard page will take care of invalid selections,
        // no need to check here.

        IScenarioProject project = getScenarioProjectFromSelection();
        mainPage = new GroundstationWizardPage(project);
        addPage(mainPage);
    }

    @Override
    public boolean performFinish()
    {
        CoreModel coreModel = MaruCorePlugin.getDefault().getCoreModel();
        IScenarioProject scenarioProject = getScenarioProjectFromSelection();

        ICentralBody centralBody = scenarioProject.getCentralBody();
        long time = scenarioProject.getCurrentTime().getTime();

        double latitude = mainPage.getLatitude();
        double longitude = mainPage.getLongitude();
        double altitude = mainPage.getAltitude();
        double elevation = mainPage.getElevation();

        GeodeticCoordinate position =
            new GeodeticCoordinate(centralBody, latitude, longitude,
                                                altitude, elevation, time);

        String name = mainPage.getElementName();
        String comment = mainPage.getElementComment();
        RGB color = mainPage.getElementColor();
        String image = mainPage.getElementImage();

        GeodeticGroundstation groundstation = new GeodeticGroundstation(name, position);
        GeodeticGroundstationPropagator propagator = new GeodeticGroundstationPropagator();

        coreModel.addGroundstation(scenarioProject, groundstation, false);
        coreModel.commentElement(groundstation, comment, false);
        coreModel.setElementColor(groundstation, color, false);
        if (!image.isEmpty()) {
            IMaruPluginResource resource = MaruGroundstationResources.fromName(image);
            coreModel.setElementGraphics2D(groundstation, resource, false);
        }
        coreModel.setPropagator(groundstation, propagator);
        coreModel.notifyElementAdded(groundstation);
        return true;
    }
}
