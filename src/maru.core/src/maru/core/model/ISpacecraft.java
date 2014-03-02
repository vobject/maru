package maru.core.model;

import java.util.List;

import org.orekit.bodies.GeodeticPoint;
import org.orekit.errors.OrekitException;

public interface ISpacecraft extends IScenarioElement, IVisibleElement, ITimeListener, IPropagationListener, ICentralBodyListener
{
    ICentralBody getCentralBody();
    IPropagator getPropagator();

    ICoordinate getInitialCoordinate();
    ICoordinate getCurrentCoordinate();

    boolean hasAccessTo(ICoordinate dest) throws OrekitException;
    boolean hasAccessTo(IGroundstation dest) throws OrekitException;

    /**
     * The line-of-sight distance from the spacecraft instance to the given
     * coordinate in meter.
     */
    double getDistanceTo(ICoordinate dest) throws OrekitException;

    /**
     * The line-of-sight distance from the spacecraft instance to the given
     * ground station in meter.
     */
    double getDistanceTo(IGroundstation dest) throws OrekitException;

    List<GeodeticPoint> getVisibilityCircle(int points) throws OrekitException;
}
