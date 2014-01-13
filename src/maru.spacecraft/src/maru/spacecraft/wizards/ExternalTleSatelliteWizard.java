package maru.spacecraft.wizards;

import maru.IMaruResource;
import maru.core.model.CoreModel;
import maru.core.model.IScenarioProject;
import maru.spacecraft.MaruSpacecraftResources;
import maru.spacecraft.tlesatellite.InitialTleCoordinate;
import maru.spacecraft.tlesatellite.SGP4Propagator;
import maru.spacecraft.tlesatellite.TleSatellite;
import maru.ui.wizards.ScenarioElementWizard;

import org.eclipse.swt.graphics.RGB;

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
        IScenarioProject scenario = getScenarioProjectFromSelection();

        String comment = satelliteNamingPage.getElementComment();
        RGB color = satelliteNamingPage.getElementColor();
        String imageName = satelliteNamingPage.getElementImage();
        IMaruResource image = null;
        if ((imageName != null) && !imageName.isEmpty()) {
            image = MaruSpacecraftResources.fromName(imageName);
        }
        InitialTleCoordinate initialCoordinate = tleSelectionPage.getInitialTlePosition();
        SGP4Propagator propagator = new SGP4Propagator();

        TleSatellite satellite = new TleSatellite(initialCoordinate.getName());
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
