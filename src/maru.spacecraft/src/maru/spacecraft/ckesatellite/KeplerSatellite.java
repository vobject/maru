package maru.spacecraft.ckesatellite;

import maru.core.model.ICoordinate;
import maru.spacecraft.OrekitSpacecraft;

import org.orekit.orbits.KeplerianOrbit;
import org.orekit.orbits.PositionAngle;

public class KeplerSatellite extends OrekitSpacecraft
{
    private static final long serialVersionUID = 1L;

    public KeplerSatellite(String name)
    {
        super(name);
    }

    @Override
    public InitialKeplerCoordinate getInitialCoordinate()
    {
        return (InitialKeplerCoordinate) super.getInitialCoordinate();
    }

    @Override
    public void setInitialCoordinate(ICoordinate coordinate)
    {
        // make sure we get coordinates based on the new initial coordinate
        getPropagator().clearCoordinateCache();

        super.setInitialCoordinate(coordinate);
    }

    @Override
    public void centralbodyChanged()
    {
        InitialKeplerCoordinate formerCoordinate = getInitialCoordinate();
        KeplerianOrbit formerOrbit = formerCoordinate.getInitialOrbit();

        KeplerianOrbit newInitialOrbit = new KeplerianOrbit(
                formerOrbit.getA(),
                formerOrbit.getE(),
                formerOrbit.getI(),
                formerOrbit.getPerigeeArgument(),
                formerOrbit.getRightAscensionOfAscendingNode(),
                formerOrbit.getAnomaly(PositionAngle.MEAN),
                PositionAngle.MEAN,
                formerOrbit.getFrame(),
                formerOrbit.getDate(),
                getCentralBody().getGM()
        );

        setInitialCoordinate(new InitialKeplerCoordinate(newInitialOrbit));
    }
}
