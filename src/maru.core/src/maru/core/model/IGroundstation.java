package maru.core.model;

import java.util.List;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.errors.OrekitException;
import org.orekit.frames.TopocentricFrame;

public interface IGroundstation extends IScenarioElement, IVisibleElement, ITimeListener, ICentralBodyListener
{
    ICentralBody getCentralBody();

    TopocentricFrame getFrame();

    GeodeticPoint getGeodeticPosition();
    Vector3D getCartesianPosition();
    double getElevationAngle();

    List<GeodeticPoint> getVisibilityCircle(ISpacecraft spacecraft, int points) throws OrekitException;
}
