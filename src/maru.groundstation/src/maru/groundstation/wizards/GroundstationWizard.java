package maru.groundstation.wizards;

import maru.core.MaruCorePlugin;
import maru.core.model.CoreModel;
import maru.core.model.ICentralBody;
import maru.core.model.IScenarioProject;
import maru.core.model.VisibleElementColor;
import maru.core.model.resource.IMaruResource;
import maru.groundstation.model.Groundstation;
import maru.groundstation.model.GroundstationResources;
import maru.ui.wizards.ScenarioElementWizard;

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

        String name = mainPage.getElementName();
        String comment = mainPage.getElementComment();
        VisibleElementColor color = mainPage.getElementColor();
        String imageName = mainPage.getElementImage();
        IMaruResource image = null;
        if ((imageName != null) && !imageName.isEmpty()) {
            image = GroundstationResources.fromName(imageName);
        }

        GeodeticPoint position = mainPage.getGeodeticPoint();
        double elevationAngle = mainPage.getElevationAngle();

        Groundstation groundstation = new Groundstation(name, position, elevationAngle, centralBody);
        groundstation.setElementComment(comment);
        groundstation.setElementColor(color);
        groundstation.setElementImage(image);

        CoreModel coreModel = MaruCorePlugin.getDefault().getCoreModel();
        coreModel.addGroundstation(scenario, groundstation, true);
        return true;
    }
}
