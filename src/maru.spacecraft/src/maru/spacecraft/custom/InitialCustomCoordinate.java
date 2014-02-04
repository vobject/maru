package maru.spacecraft.custom;

import maru.MaruRuntimeException;
import maru.core.model.ICentralBody;
import maru.spacecraft.OrekitCoordinate;

import org.orekit.errors.OrekitException;
import org.orekit.orbits.CartesianOrbit;
import org.orekit.orbits.CircularOrbit;
import org.orekit.orbits.EquinoctialOrbit;
import org.orekit.orbits.KeplerianOrbit;
import org.orekit.orbits.Orbit;
import org.orekit.orbits.OrbitType;
import org.orekit.orbits.PositionAngle;

public class InitialCustomCoordinate extends OrekitCoordinate
{
    private static final long serialVersionUID = 1L;

    private final Orbit initialOrbit;

    public InitialCustomCoordinate(ICentralBody centralBody, Orbit orbit) throws OrekitException
    {
        super(centralBody, orbit.getPVCoordinates(), orbit.getDate(), orbit.getFrame());
        this.initialOrbit = orbit;
    }

    public Orbit getOrbit()
    {
        return initialOrbit;
    }

    public OrbitType getOrbitType()
    {
        return initialOrbit.getType();
    }

    /**
     * Recreate an initial coordinate in case the central body changed.
     */
    public static InitialCustomCoordinate recreate(InitialCustomCoordinate coordinate) throws OrekitException
    {
        ICentralBody centralBody = coordinate.getCentralBody();
        Orbit orbit = coordinate.getOrbit();
        return new InitialCustomCoordinate(centralBody, recreate(centralBody, orbit));
    }

    /**
     * Recreate an orbit in case the central body changed.
     */
    public static Orbit recreate(ICentralBody centralBody, Orbit orbit)
    {
        switch (orbit.getType())
        {
            case CARTESIAN:
            {
                CartesianOrbit cartesian = (CartesianOrbit) orbit;
                return new CartesianOrbit(
                    cartesian.getPVCoordinates(),
                    cartesian.getFrame(),
                    cartesian.getDate(),
                    centralBody.getGM()
                );
            }

            case CIRCULAR:
            {
                CircularOrbit circular = (CircularOrbit) orbit;

                return new CircularOrbit(
                    circular.getA(),
                    circular.getCircularEx(),
                    circular.getCircularEy(),
                    circular.getI(),
                    circular.getRightAscensionOfAscendingNode(),
                    circular.getAlpha(PositionAngle.TRUE),
                    PositionAngle.TRUE,
                    circular.getFrame(),
                    circular.getDate(),
                    centralBody.getGM()
                );
            }

            case EQUINOCTIAL:
            {
                EquinoctialOrbit equinoctial = (EquinoctialOrbit) orbit;
                return new EquinoctialOrbit(
                    equinoctial.getA(),
                    equinoctial.getEquinoctialEx(),
                    equinoctial.getEquinoctialEy(),
                    equinoctial.getHx(),
                    equinoctial.getHy(),
                    equinoctial.getL(PositionAngle.TRUE),
                    PositionAngle.TRUE,
                    equinoctial.getFrame(),
                    equinoctial.getDate(),
                    centralBody.getGM()
                );
            }

            case KEPLERIAN:
            {
                KeplerianOrbit keplerian = (KeplerianOrbit) orbit;
                return new KeplerianOrbit(
                    keplerian.getA(),
                    keplerian.getE(),
                    keplerian.getI(),
                    keplerian.getPerigeeArgument(),
                    keplerian.getRightAscensionOfAscendingNode(),
                    keplerian.getAnomaly(PositionAngle.TRUE),
                    PositionAngle.TRUE,
                    keplerian.getFrame(),
                    keplerian.getDate(),
                    centralBody.getGM()
                );
            }
        }

        throw new MaruRuntimeException("Invalid orbit type.");
    }
}
