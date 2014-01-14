package maru.core.model;

import org.orekit.errors.OrekitException;


public interface ISpacecraft extends IScenarioElement, IVisibleElement, ITimeListener, IPropagationListener, ICentralBodyListener
{
    enum EclipseState
    {
        None,
        Umbra,
        UmbraOrPenumbra
    }

    //enum AccessCriteria
    //{
    //    None,
    //    ConsiderEllipsoid,
    //    ConsiderAltitude,
    //    ConsiderElevation
    //}

    ICentralBody getCentralBody();
    IPropagator getPropagator();

    ICoordinate getInitialCoordinate();
    ICoordinate getCurrentCoordinate();

    EclipseState getEclipseState() throws OrekitException;
    EclipseState getEclipseState(ICoordinate coordinate) throws OrekitException;

    boolean hasAccessTo(IGroundstation groundstation) throws OrekitException;
    boolean hasAccessTo(ISpacecraft spacecraft) throws OrekitException;

    /**
     * The line-of-sight distance from the spacecraft instance to the given
     * ground station in meter or a negative value if there is no
     * visual contact ({@link #hasAccessTo(IGroundstation)}.
     */
    double getDistanceTo(IGroundstation groundstation) throws OrekitException;

    /**
     * The line-of-sight distance from the spacecraft instance to the given
     * spacecraft in meter or a negative value if there is no
     * visual contact ({@link #hasAccessTo(ISpacecraft)}.
     */
    double getDistanceTo(ISpacecraft other) throws OrekitException;
}
