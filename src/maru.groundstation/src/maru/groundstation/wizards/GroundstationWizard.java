package maru.groundstation.wizards;

import maru.IMaruResource;
import maru.core.MaruCorePlugin;
import maru.core.model.CoreModel;
import maru.core.model.ICentralBody;
import maru.core.model.IScenarioProject;
import maru.groundstation.MaruGroundstationResources;
import maru.groundstation.earth.GeodeticCoordinate;
import maru.groundstation.earth.GeodeticGroundstation;
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
        IScenarioProject scenario = getScenarioProjectFromSelection();

        ICentralBody centralBody = scenario.getCentralBody();
        long time = scenario.getCurrentTime().getTime();

        double latitude = mainPage.getLatitude();
        double longitude = mainPage.getLongitude();
        double altitude = mainPage.getAltitude();
        double elevation = mainPage.getElevation();

        String name = mainPage.getElementName();
        String comment = mainPage.getElementComment();
        RGB color = mainPage.getElementColor();
        String imageName = mainPage.getElementImage();
        IMaruResource image = null;
        if ((imageName != null) && !imageName.isEmpty()) {
            image = MaruGroundstationResources.fromName(imageName);
        }
        GeodeticCoordinate initialCoordinate =
            new GeodeticCoordinate(centralBody, latitude, longitude,
                                   altitude, elevation, time);
        GeodeticGroundstationPropagator propagator = new GeodeticGroundstationPropagator();

        GeodeticGroundstation groundstation = new GeodeticGroundstation(name);
        groundstation.setElementComment(comment);
        groundstation.setElementColor(color);
        groundstation.setElementImage(image);
        groundstation.setInitialCoordinate(initialCoordinate);
        groundstation.setPropagator(propagator);

        CoreModel coreModel = MaruCorePlugin.getDefault().getCoreModel();
        coreModel.addGroundstation(scenario, groundstation, true);
        return true;
    }
}
