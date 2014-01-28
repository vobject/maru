package maru.core.model;

import java.util.List;

import maru.core.utils.EclipseState;

import org.orekit.bodies.GeodeticPoint;
import org.orekit.errors.OrekitException;


public interface ISpacecraft extends IScenarioElement, IVisibleElement, ITimeListener, IPropagationListener, ICentralBodyListener
{
    ICentralBody getCentralBody();
    IPropagator getPropagator();

    ICoordinate getInitialCoordinate();
    ICoordinate getCurrentCoordinate();

    EclipseState getEclipseState() throws OrekitException;

    boolean hasAccessTo(ICoordinate coordinate) throws OrekitException;
    boolean hasAccessTo(IGroundstation groundstation) throws OrekitException;

    /**
     * The line-of-sight distance from the spacecraft instance to the given
     * coordinate in meter or a negative value if there is no
     * visual contact ({@link #hasAccessTo(ICoordinate)}.
     */
    double getDistanceTo(ICoordinate coordinate) throws OrekitException;

    /**
     * The line-of-sight distance from the spacecraft instance to the given
     * ground station in meter or a negative value if there is no
     * visual contact ({@link #hasAccessTo(IGroundstation)}.
     */
    double getDistanceTo(IGroundstation groundstation) throws OrekitException;

    List<GeodeticPoint> getVisibilityCircle(int points) throws OrekitException;
}
