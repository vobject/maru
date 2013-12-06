package maru.ui.debug.handlers;

import java.util.Date;
import java.util.Random;

import maru.IMaruPluginResource;
import maru.centralbody.MaruCentralBodyResources;
import maru.centralbody.earth.Earth;
import maru.core.MaruCorePlugin;
import maru.core.model.CoreModel;
import maru.core.model.ICentralBody;
import maru.core.model.IScenarioProject;
import maru.groundstation.MaruGroundstationResources;
import maru.groundstation.earth.GeodeticGroundstation;
import maru.groundstation.earth.GeodeticCoordinate;
import maru.groundstation.earth.GeodeticGroundstationPropagator;
import maru.spacecraft.MaruSpacecraftResources;
import maru.spacecraft.ckesatellite.InitialKeplerCoordinate;
import maru.spacecraft.ckesatellite.KeplerPropagator;
import maru.spacecraft.ckesatellite.KeplerSatellite;
import maru.spacecraft.tlesatellite.InitialTleCoordinate;
import maru.spacecraft.tlesatellite.Sgp4Propagator;
import maru.spacecraft.tlesatellite.TleSatellite;
import maru.spacecraft.utils.TleUtils;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.graphics.RGB;
import org.orekit.OrekitUtils;
import org.orekit.frames.Frame;
import org.orekit.frames.FramesFactory;
import org.orekit.orbits.KeplerianOrbit;
import org.orekit.orbits.PositionAngle;
import org.orekit.time.AbsoluteDate;

public final class CreateScenarioHelper
{
    private static String DEFAULT_SCENARIO_NAME = "DebugScenario";
    private static String DEFAULT_SCENARIO_COMMENT = "DebugScenario Comment";
    private static IMaruPluginResource DEFAULT_SCENARIO_CENTRALBODY_GRAPHIC2D = MaruCentralBodyResources.MAP_EARTH_2;
    private static int DEFAULT_SCENARIO_LENGTH = 8 * 60 * 60; // 8 hours
    private static int DEFAULT_SCENARIO_TIMEPOINT_1 = 2 * 60 * 60;
    private static int DEFAULT_SCENARIO_TIMEPOINT_2 = 4 * 60 * 60;
    private static int DEFAULT_SCENARIO_TIMEPOINT_3 = 6 * 60 * 60;

    private static String DEFAULT_GROUNDSTATION_NAME = "DebugGroundstation";
    private static String DEFAULT_GROUNDSTATION_COMMENT = "DebugGroundstation Comment";
    private static RGB DEFAULT_GROUNDSTATION_COLOR = new RGB(192, 192, 224);
    private static IMaruPluginResource DEFAULT_GROUNDSTATION_GRAPHIC2D = MaruGroundstationResources.GROUNDSTATION_DEFAULT_128;
    private static double DEFAULT_GROUNDSTATION_LATITUDE_DEG = 49.78186646; // degree
    private static double DEFAULT_GROUNDSTATION_LONGITUDE_DEG = 9.97290914; // degree
    private static double DEFAULT_GROUNDSTATION_ALTITUDE = 274.68; // meter
    private static double DEFAULT_GROUNDSTATION_ELEVATION_DEG = 5.0; // degree

    private static String DEFAULT_KEPLER_SATELLITE_NAME = "DebugKeplerSatellite";
    private static String DEFAULT_KEPLER_SATELLITE_COMMENT = "DebugKeplerSatellite Comment";
    private static RGB DEFAULT_KEPLER_SATELLITE_COLOR = new RGB(64, 255, 64);
    private static IMaruPluginResource DEFAULT_KEPLER_SATELLITE_GRAPHIC2D = MaruSpacecraftResources.SPACECRAFT_DEFAULT_128;

    private static String DEFAULT_TLE_SATELLITE_COMMENT = "DebugTleSatellite Comment";
    private static RGB DEFAULT_TLE_SATELLITE_COLOR = new RGB(64, 96, 255);
    private static IMaruPluginResource DEFAULT_TLE_SATELLITE_GRAPHIC2D = MaruSpacecraftResources.SPACECRAFT_ISS_128;

    public static IScenarioProject createEmpty()
    {
        Random generator = new Random();
        int scenarioId = generator.nextInt(9999);

        String scenarioName = DEFAULT_SCENARIO_NAME + "_" + scenarioId;
        String scenarioComment = DEFAULT_SCENARIO_COMMENT;
        Earth centralBody = new Earth(DEFAULT_SCENARIO_CENTRALBODY_GRAPHIC2D);
        long startTime = (new Date()).getTime() / 1000;
        long stopTime = ((new Date()).getTime() / 1000) + DEFAULT_SCENARIO_LENGTH;

        IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(scenarioName);

        try
        {
            CoreModel coreModel = MaruCorePlugin.getDefault().getCoreModel();

            IScenarioProject scenarioProject = coreModel.createScenarioProject(
                project, scenarioComment, centralBody, startTime, stopTime
            );

            coreModel.addTimepoint(scenarioProject, startTime + DEFAULT_SCENARIO_TIMEPOINT_1, true);
            coreModel.addTimepoint(scenarioProject, startTime + DEFAULT_SCENARIO_TIMEPOINT_2, true);
            coreModel.addTimepoint(scenarioProject, startTime + DEFAULT_SCENARIO_TIMEPOINT_3, true);

            return scenarioProject;
        }
        catch (CoreException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static void createGroundstation(IScenarioProject scenarioProject)
    {
        String name = DEFAULT_GROUNDSTATION_NAME;
        String comment = DEFAULT_GROUNDSTATION_COMMENT;
        RGB color = DEFAULT_GROUNDSTATION_COLOR;

        double latitude = Math.toRadians(DEFAULT_GROUNDSTATION_LATITUDE_DEG);
        double longitude = Math.toRadians(DEFAULT_GROUNDSTATION_LONGITUDE_DEG);
        double altitude = DEFAULT_GROUNDSTATION_ALTITUDE;
        double elevation = Math.toRadians(DEFAULT_GROUNDSTATION_ELEVATION_DEG);

        ICentralBody centralBody = scenarioProject.getCentralBody();
        long time = scenarioProject.getCurrentTime().getTime();

        GeodeticCoordinate position =
            new GeodeticCoordinate(centralBody, latitude, longitude,
                                                altitude, elevation, time);

        GeodeticGroundstation groundstation = new GeodeticGroundstation(name, position);
        GeodeticGroundstationPropagator propagator = new GeodeticGroundstationPropagator();

        CoreModel coreModel = MaruCorePlugin.getDefault().getCoreModel();
        coreModel.addGroundstation(scenarioProject, groundstation, false);
        coreModel.commentElement(groundstation, comment, false);
        coreModel.setElementColor(groundstation, color, false);
        coreModel.setElementGraphics2D(groundstation, DEFAULT_GROUNDSTATION_GRAPHIC2D, false);
        coreModel.setPropagator(groundstation, propagator);
        coreModel.notifyElementAdded(groundstation);
    }

    public static void createKeplerSatellite(IScenarioProject scenarioProject)
    {
        String name = DEFAULT_KEPLER_SATELLITE_NAME;
        String comment = DEFAULT_KEPLER_SATELLITE_COMMENT;
        RGB color = DEFAULT_KEPLER_SATELLITE_COLOR;

        double a = 6878.14 * 1000.0;
        double e = 1.96956e-016;
        double i = Math.toRadians(37.4609);
        double pa = Math.toRadians(0);
        double raan = Math.toRadians(97.3437);
        double anomaly = Math.toRadians(359.948);
        PositionAngle type = PositionAngle.TRUE;
        Frame frame = FramesFactory.getEME2000();
        AbsoluteDate date = OrekitUtils.toAbsoluteDate(scenarioProject.getStartTime());
        double mu = scenarioProject.getCentralBody().getGM();

        KeplerianOrbit initialOrbit = new KeplerianOrbit(a, e, i, pa, raan, anomaly, type, frame, date, mu);
        InitialKeplerCoordinate initialCoordinate = new InitialKeplerCoordinate(initialOrbit);

        KeplerSatellite satellite = new KeplerSatellite(name, initialCoordinate);
        KeplerPropagator propagator = new KeplerPropagator();

        CoreModel coreModel = MaruCorePlugin.getDefault().getCoreModel();
        coreModel.addSpacecraft(scenarioProject, satellite, false);
        coreModel.commentElement(satellite, comment, false);
        coreModel.setElementColor(satellite, color, false);
        coreModel.setElementGraphics2D(satellite, DEFAULT_KEPLER_SATELLITE_GRAPHIC2D, false);
        coreModel.setPropagator(satellite, propagator);
        coreModel.notifyElementAdded(satellite);
    }

    public static void createTleSatellite(IScenarioProject scenarioProject)
    {
        String comment = DEFAULT_TLE_SATELLITE_COMMENT;
        RGB color = DEFAULT_TLE_SATELLITE_COLOR;

        // the fresh TLE data for the ISS
        String url = "http://www.celestrak.com/NORAD/elements/stations.txt";
        InitialTleCoordinate initialCoordinate = TleUtils.parseTleSource(url).get(0);

        TleSatellite satellite = new TleSatellite(initialCoordinate);
        Sgp4Propagator propagator = new Sgp4Propagator();

        CoreModel coreModel = MaruCorePlugin.getDefault().getCoreModel();
        coreModel.addSpacecraft(scenarioProject, satellite, false);
        coreModel.commentElement(satellite, comment, false);
        coreModel.setElementColor(satellite, color, false);
        coreModel.setElementGraphics2D(satellite, DEFAULT_TLE_SATELLITE_GRAPHIC2D, false);
        coreModel.setPropagator(satellite, propagator);
        coreModel.notifyElementAdded(satellite);
    }
}
