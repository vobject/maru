package maru.spacecraft.wizards;

import maru.IMaruPluginResource;
import maru.core.model.CoreModel;
import maru.core.model.IScenarioProject;
import maru.spacecraft.MaruSpacecraftResources;
import maru.spacecraft.ckesatellite.InitialKeplerCoordinate;
import maru.spacecraft.ckesatellite.KeplerPropagator;
import maru.spacecraft.ckesatellite.KeplerSatellite;
import maru.ui.wizards.ScenarioElementWizard;

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
        CoreModel coreModel = CoreModel.getDefault();
        IScenarioProject scenarioProject = getScenarioProjectFromSelection();

        String name = mainPage.getElementName();
        InitialKeplerCoordinate coordinate = createInitialCoordinate(scenarioProject);
        KeplerSatellite satellite = new KeplerSatellite(name, coordinate);
        KeplerPropagator propagator = new KeplerPropagator();
        String image = mainPage.getElementImage();

        coreModel.addSpacecraft(scenarioProject, satellite, false);
        coreModel.commentElement(satellite, mainPage.getElementComment(), false);
        coreModel.changeColor(satellite, mainPage.getElementColor(), false);
        if (!image.isEmpty()) {
            IMaruPluginResource resource = MaruSpacecraftResources.fromName(image);
            coreModel.changeImage(satellite, resource, false);
        }
        coreModel.setPropagator(satellite, propagator);
        coreModel.notifyElementAdded(satellite);
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
