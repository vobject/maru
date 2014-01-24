package maru.spacecraft.wizards;

import maru.IMaruResource;
import maru.core.model.CoreModel;
import maru.core.model.IScenarioProject;
import maru.spacecraft.MaruSpacecraftResources;
import maru.spacecraft.custom.InitialCustomCoordinate;
import maru.spacecraft.custom.KeplerPropagator;
import maru.spacecraft.custom.CustomSatellite;
import maru.ui.wizards.ScenarioElementWizard;

import org.eclipse.swt.graphics.RGB;
import org.orekit.frames.Frame;
import org.orekit.orbits.KeplerianOrbit;
import org.orekit.orbits.PositionAngle;
import org.orekit.time.AbsoluteDate;

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
        InitialCustomCoordinate initialCoordinate = createInitialCoordinate(scenario);
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

    private InitialCustomCoordinate createInitialCoordinate(IScenarioProject project)
    {
        double a = mainPage.getSemimajorAxis();
        double e = mainPage.getEccentricity();
        double i = mainPage.getInclination();
        double pa = mainPage.getArgumentOfPerigee();
        double raan = mainPage.getRaan();
        double anomaly = mainPage.getAnomaly();
        PositionAngle type = mainPage.getAnomalyType();
        Frame frame = mainPage.getFrame();
        AbsoluteDate date = mainPage.getDate();
        double mu = mainPage.getCentralAttractionCoefficient();

        KeplerianOrbit initialOrbit =
            new KeplerianOrbit(a, e, i, pa, raan, anomaly, type, frame, date, mu);

        return new InitialCustomCoordinate(initialOrbit);
    }
}
