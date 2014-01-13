package maru.groundstation.wizards;

import maru.IMaruResource;
import maru.core.MaruCorePlugin;
import maru.core.model.CoreModel;
import maru.core.model.ICentralBody;
import maru.core.model.IScenarioProject;
import maru.groundstation.MaruGroundstationResources;
import maru.groundstation.earth.Groundstation;
import maru.ui.wizards.ScenarioElementWizard;

import org.eclipse.swt.graphics.RGB;
import org.orekit.bodies.GeodeticPoint;

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

        double latitude = mainPage.getLatitude();
        double longitude = mainPage.getLongitude();
        double altitude = mainPage.getAltitude();

        GeodeticPoint position = new GeodeticPoint(latitude, longitude, altitude);
        String name = mainPage.getElementName();
        String comment = mainPage.getElementComment();
        RGB color = mainPage.getElementColor();
        String imageName = mainPage.getElementImage();
        IMaruResource image = null;
        if ((imageName != null) && !imageName.isEmpty()) {
            image = MaruGroundstationResources.fromName(imageName);
        }

        Groundstation groundstation = new Groundstation(name, position, centralBody);
        groundstation.setElementComment(comment);
        groundstation.setElementColor(color);
        groundstation.setElementImage(image);

        CoreModel coreModel = MaruCorePlugin.getDefault().getCoreModel();
        coreModel.addGroundstation(scenario, groundstation, true);
        return true;
    }
}
