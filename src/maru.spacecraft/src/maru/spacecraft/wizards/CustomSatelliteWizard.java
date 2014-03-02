package maru.spacecraft.wizards;

import maru.core.model.CoreModel;
import maru.core.model.ICentralBody;
import maru.core.model.IScenarioProject;
import maru.core.model.VisibleElementColor;
import maru.core.model.resource.IMaruResource;
import maru.spacecraft.model.SpacecraftResources;
import maru.spacecraft.model.custom.CustomSatellite;
import maru.spacecraft.model.custom.InitialCustomCoordinate;
import maru.spacecraft.model.custom.KeplerPropagator;
import maru.ui.wizards.ScenarioElementWizard;

import org.orekit.errors.OrekitException;
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
        try
        {
            IScenarioProject scenario = getScenarioProjectFromSelection();
            ICentralBody centralBody = scenario.getCentralBody();

            String name = mainPage.getElementName();
            String comment = mainPage.getElementComment();
            VisibleElementColor color = mainPage.getElementColor();
            String imageName = mainPage.getElementImage();
            IMaruResource image = null;
            if ((imageName != null) && !imageName.isEmpty()) {
                image = SpacecraftResources.fromName(imageName);
            }
            Orbit orbit = mainPage.getOrbit();
            InitialCustomCoordinate initialCoordinate = new InitialCustomCoordinate(centralBody, orbit);
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
        catch (OrekitException e)
        {
            e.printStackTrace();
            return false;
        }
    }
}
