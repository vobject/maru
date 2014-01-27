package maru.spacecraft.wizards;

import maru.IMaruResource;
import maru.core.model.CoreModel;
import maru.core.model.IScenarioProject;
import maru.spacecraft.MaruSpacecraftResources;
import maru.spacecraft.tle.InitialTLECoordinate;
import maru.spacecraft.tle.SGP4Propagator;
import maru.spacecraft.tle.TLESatellite;
import maru.ui.wizards.ScenarioElementWizard;

import org.eclipse.swt.graphics.RGB;
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
        IScenarioProject scenario = getScenarioProjectFromSelection();

        InitialTLECoordinate initialCoordinate;
        try {
            initialCoordinate = createInitialPosition();
        } catch (OrekitException e) {
            e.printStackTrace();
            return false;
        }

        String name = mainPage.getElementName();
        String comment = mainPage.getElementComment();
        RGB color = mainPage.getElementColor();
        String imageName = mainPage.getElementImage();
        IMaruResource image = null;
        if ((imageName != null) && !imageName.isEmpty()) {
            image = MaruSpacecraftResources.fromName(imageName);
        }
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

    private InitialTLECoordinate createInitialPosition() throws OrekitException
    {
        String name = mainPage.getElementName();
        TLE tle = new TLE(mainPage.getLine1(), mainPage.getLine2());
        return new InitialTLECoordinate(name, tle);
    }
}
