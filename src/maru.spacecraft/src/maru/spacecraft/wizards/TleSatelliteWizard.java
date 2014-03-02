package maru.spacecraft.wizards;

import maru.core.model.CoreModel;
import maru.core.model.ICentralBody;
import maru.core.model.IScenarioProject;
import maru.core.model.VisibleElementColor;
import maru.core.model.resource.IMaruResource;
import maru.spacecraft.model.SpacecraftResources;
import maru.spacecraft.model.tle.InitialTLECoordinate;
import maru.spacecraft.model.tle.SGP4Propagator;
import maru.spacecraft.model.tle.TLESatellite;
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
            InitialTLECoordinate initialCoordinate = createInitialPosition(centralBody);
            SGP4Propagator propagator = new SGP4Propagator();

            TLESatellite satellite = new TLESatellite(name);
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

    private InitialTLECoordinate createInitialPosition(ICentralBody centralBody) throws OrekitException
    {
        String name = mainPage.getElementName();
        TLE tle = new TLE(mainPage.getLine1(), mainPage.getLine2());
        return new InitialTLECoordinate(centralBody, name, tle);
    }
}
