package maru.spacecraft.wizards;

import maru.IMaruResource;
import maru.core.model.CoreModel;
import maru.core.model.IScenarioProject;
import maru.spacecraft.MaruSpacecraftResources;
import maru.spacecraft.ckesatellite.InitialKeplerCoordinate;
import maru.spacecraft.ckesatellite.KeplerPropagator;
import maru.spacecraft.ckesatellite.KeplerSatellite;
import maru.ui.wizards.ScenarioElementWizard;

import org.eclipse.swt.graphics.RGB;
import org.orekit.OrekitUtils;
import org.orekit.frames.Frame;
import org.orekit.orbits.KeplerianOrbit;
import org.orekit.orbits.PositionAngle;
import org.orekit.time.AbsoluteDate;

public class KeplerSatelliteWizard extends ScenarioElementWizard
{
    private KeplerSatelliteWizardPage mainPage;

    public KeplerSatelliteWizard()
    {
        super("Satellite Wizard");
    }

    @Override
    public void addPages()
    {
        IScenarioProject project = getScenarioProjectFromSelection();
        mainPage = new KeplerSatelliteWizardPage(project);
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
        InitialKeplerCoordinate initialCoordinate = createInitialCoordinate(scenario);
        KeplerPropagator propagator = new KeplerPropagator();

        KeplerSatellite satellite = new KeplerSatellite(name);
        satellite.setElementComment(comment);
        satellite.setElementColor(color);
        satellite.setElementImage(image);
        satellite.setInitialCoordinate(initialCoordinate);
        satellite.setPropagator(propagator);

        CoreModel coreModel = CoreModel.getDefault();
        coreModel.addSpacecraft(scenario, satellite, true);
        return true;
    }

    private InitialKeplerCoordinate createInitialCoordinate(IScenarioProject project)
    {
        double a = mainPage.getSemimajorAxis();
        double e = mainPage.getEccentricity();
        double i = mainPage.getInclination();
        double pa = mainPage.getArgumentOfPerigee();
        double raan = mainPage.getRaan();
        double anomaly = mainPage.getAnomaly();
        PositionAngle type = mainPage.getAnomalyType();
        Frame frame = OrekitUtils.toOrekitFrame(mainPage.getFrame());
        AbsoluteDate date = OrekitUtils.toAbsoluteDate(mainPage.getTime());
        double mu = mainPage.getCentralAttractionCoefficient();

        KeplerianOrbit initialOrbit =
            new KeplerianOrbit(a, e, i, pa, raan, anomaly, type, frame, date, mu);

        return new InitialKeplerCoordinate(initialOrbit);
    }
}
