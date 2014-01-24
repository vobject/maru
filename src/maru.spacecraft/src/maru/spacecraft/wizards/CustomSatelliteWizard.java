package maru.spacecraft.wizards;

import maru.IMaruResource;
import maru.core.model.CoreModel;
import maru.core.model.IScenarioProject;
import maru.spacecraft.MaruSpacecraftResources;
import maru.spacecraft.custom.CustomSatellite;
import maru.spacecraft.custom.InitialCustomCoordinate;
import maru.spacecraft.custom.KeplerPropagator;
import maru.ui.wizards.ScenarioElementWizard;

import org.eclipse.swt.graphics.RGB;
import org.orekit.orbits.Orbit;

public class CustomSatelliteWizard extends ScenarioElementWizard
{
    private CustomSatelliteWizardPage mainPage;

    public CustomSatelliteWizard()
    {
        super("Satellite Wizard");
    }

    @Override
    public void addPages()
    {
        IScenarioProject project = getScenarioProjectFromSelection();
        mainPage = new CustomSatelliteWizardPage(project);
        addPage(mainPage);
    }

    @Override
    public boolean performFinish()
    {
        IScenarioProject scenario = getScenarioProjectFromSelection();

        String name = mainPage.getElementName();
        String comment = mainPage.getElementComment();
        RGB color = mainPage.getElementColor();
        String imageName = mainPage.getElementImage();
        IMaruResource image = null;
        if ((imageName != null) && !imageName.isEmpty()) {
            image = MaruSpacecraftResources.fromName(imageName);
        }
        Orbit orbit = mainPage.getOrbit();
        InitialCustomCoordinate initialCoordinate = new InitialCustomCoordinate(orbit);
        KeplerPropagator propagator = new KeplerPropagator();

        CustomSatellite satellite = new CustomSatellite(name);
        satellite.setElementComment(comment);
        satellite.setElementColor(color);
        satellite.setElementImage(image);
        satellite.setInitialCoordinate(initialCoordinate);
        satellite.setPropagator(propagator);

        CoreModel coreModel = CoreModel.getDefault();
        coreModel.addSpacecraft(scenario, satellite, true);
        return true;
    }
}
