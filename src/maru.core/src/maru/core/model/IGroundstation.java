package maru.core.model;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.frames.TopocentricFrame;

public interface IGroundstation extends IScenarioElement, IVisibleElement, ITimeListener, ICentralBodyListener
{
    ICentralBody getCentralBody();

    TopocentricFrame getFrame();

    GeodeticPoint getGeodeticPosition();
    Vector3D getCartesianPosition();
    double getElevationAngle();
}
