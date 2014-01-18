package maru.ui.debug.handlers;

import java.util.Random;

import maru.IMaruPluginResource;
import maru.centralbody.MaruCentralBodyResources;
import maru.centralbody.bodies.Earth;
import maru.core.MaruCorePlugin;
import maru.core.model.CoreModel;
import maru.core.model.ICentralBody;
import maru.core.model.IScenarioProject;
import maru.core.utils.TimeUtils;
import maru.groundstation.MaruGroundstationResources;
import maru.groundstation.earth.Groundstation;
import maru.spacecraft.MaruSpacecraftResources;
import maru.spacecraft.custom.InitialCustomCoordinate;
import maru.spacecraft.custom.KeplerPropagator;
import maru.spacecraft.custom.KeplerSatellite;
import maru.spacecraft.tle.InitialTLECoordinate;
import maru.spacecraft.tle.SGP4Propagator;
import maru.spacecraft.tle.TLESatellite;
import maru.spacecraft.utils.TleUtils;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.graphics.RGB;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.errors.OrekitException;
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
    private static IMaruPluginResource DEFAULT_GROUNDSTATION_GRAPHIC2D = MaruGroundstationResources.GROUNDSTATION_DEFAULT_1;
    private static double DEFAULT_GROUNDSTATION_LATITUDE_DEG = 49.78186646; // degree
    private static double DEFAULT_GROUNDSTATION_LONGITUDE_DEG = 9.97290914; // degree
    private static double DEFAULT_GROUNDSTATION_ALTITUDE = 274.68; // meter

    private static String DEFAULT_KEPLER_SATELLITE_NAME = "DebugKeplerSatellite";
    private static String DEFAULT_KEPLER_SATELLITE_COMMENT = "DebugKeplerSatellite Comment";
    private static RGB DEFAULT_KEPLER_SATELLITE_COLOR = new RGB(64, 255, 64);
    private static IMaruPluginResource DEFAULT_KEPLER_SATELLITE_GRAPHIC2D = MaruSpacecraftResources.SPACECRAFT_DEFAULT_1;

    private static String DEFAULT_TLE_SATELLITE_COMMENT = "DebugTleSatellite Comment";
    private static RGB DEFAULT_TLE_SATELLITE_COLOR = new RGB(64, 96, 255);
    private static IMaruPluginResource DEFAULT_TLE_SATELLITE_GRAPHIC2D = MaruSpacecraftResources.SPACECRAFT_ISS_1;

    public static IScenarioProject createEmpty()
    {
        Random generator = new Random();
        int scenarioId = generator.nextInt(9999);

        try
        {
            String scenarioName = DEFAULT_SCENARIO_NAME + "_" + scenarioId;
            String scenarioComment = DEFAULT_SCENARIO_COMMENT;
            Earth centralBody = new Earth(DEFAULT_SCENARIO_CENTRALBODY_GRAPHIC2D);
            AbsoluteDate startTime = TimeUtils.now();
            AbsoluteDate stopTime = TimeUtils.create(startTime, DEFAULT_SCENARIO_LENGTH);

            IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(scenarioName);

            CoreModel coreModel = MaruCorePlugin.getDefault().getCoreModel();

            IScenarioProject scenarioProject = coreModel.createScenarioProject(
                project, scenarioComment, centralBody, startTime, stopTime
            );

            coreModel.addTimepoint(scenarioProject, TimeUtils.create(startTime, DEFAULT_SCENARIO_TIMEPOINT_1), true);
            coreModel.addTimepoint(scenarioProject, TimeUtils.create(startTime, DEFAULT_SCENARIO_TIMEPOINT_2), true);
            coreModel.addTimepoint(scenarioProject, TimeUtils.create(startTime, DEFAULT_SCENARIO_TIMEPOINT_3), true);

            return scenarioProject;
        }
        catch (CoreException | OrekitException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static void createGroundstation(IScenarioProject scenario)
    {
        String name = DEFAULT_GROUNDSTATION_NAME;
        String comment = DEFAULT_GROUNDSTATION_COMMENT;
        RGB color = DEFAULT_GROUNDSTATION_COLOR;

        double latitude = Math.toRadians(DEFAULT_GROUNDSTATION_LATITUDE_DEG);
        double longitude = Math.toRadians(DEFAULT_GROUNDSTATION_LONGITUDE_DEG);
        double altitude = DEFAULT_GROUNDSTATION_ALTITUDE;
        GeodeticPoint position = new GeodeticPoint(latitude, longitude, altitude);

        ICentralBody centralBody = scenario.getCentralBody();

        Groundstation groundstation = new Groundstation(name, position, centralBody);
        groundstation.setElementComment(comment);
        groundstation.setElementColor(color);
        groundstation.setElementImage(DEFAULT_GROUNDSTATION_GRAPHIC2D);

        CoreModel coreModel = MaruCorePlugin.getDefault().getCoreModel();
        coreModel.addGroundstation(scenario, groundstation, true);
    }

    public static void createKeplerSatellite(IScenarioProject scenario)
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
        PositionAngle type = PositionAngle.MEAN;
        Frame frame = FramesFactory.getEME2000();
        AbsoluteDate date = scenario.getStartTime().getTime();
        double mu = scenario.getCentralBody().getGM();

        KeplerianOrbit initialOrbit = new KeplerianOrbit(a, e, i, pa, raan, anomaly, type, frame, date, mu);
        InitialCustomCoordinate initialCoordinate = new InitialCustomCoordinate(initialOrbit);
        KeplerPropagator propagator = new KeplerPropagator();

        KeplerSatellite satellite = new KeplerSatellite(name);
        satellite.setElementComment(comment);
        satellite.setElementColor(color);
        satellite.setElementImage(DEFAULT_KEPLER_SATELLITE_GRAPHIC2D);
        satellite.setInitialCoordinate(initialCoordinate);
        satellite.setPropagator(propagator);

        CoreModel coreModel = MaruCorePlugin.getDefault().getCoreModel();
        coreModel.addSpacecraft(scenario, satellite, true);
    }

    public static void createTleSatellite(IScenarioProject scenario)
    {
        String comment = DEFAULT_TLE_SATELLITE_COMMENT;
        RGB color = DEFAULT_TLE_SATELLITE_COLOR;

        // the fresh TLE data for the ISS
        String url = "http://www.celestrak.com/NORAD/elements/stations.txt";
        InitialTLECoordinate initialCoordinate = TleUtils.parseTleSource(url).get(0);
        SGP4Propagator propagator = new SGP4Propagator();

        TLESatellite satellite = new TLESatellite(initialCoordinate.getName());
        satellite.setElementComment(comment);
        satellite.setElementColor(color);
        satellite.setElementImage(DEFAULT_TLE_SATELLITE_GRAPHIC2D);
        satellite.setInitialCoordinate(initialCoordinate);
        satellite.setPropagator(propagator);

        CoreModel coreModel = MaruCorePlugin.getDefault().getCoreModel();
        coreModel.addSpacecraft(scenario, satellite, true);
    }
}
