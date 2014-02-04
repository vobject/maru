package maru.core.model;

import java.util.List;

import org.orekit.bodies.GeodeticPoint;
import org.orekit.errors.OrekitException;
import org.orekit.frames.TopocentricFrame;

public interface IGroundstation extends IScenarioElement, IVisibleElement, ITimeListener, ICentralBodyListener
{
    ICentralBody getCentralBody();

    TopocentricFrame getFrame();

    GeodeticPoint getGeodeticPosition();
//    Vector3D getCartesianPosition();
    double getElevationAngle();

    /** return the visibility circle of the ground station from a specific coordinate. */
    List<GeodeticPoint> getVisibilityCircle(ICoordinate coordinate, int points) throws OrekitException;
}
