package maru.spacecraft.custom;

import maru.spacecraft.OrekitCoordinate;

import org.orekit.orbits.KeplerianOrbit;

public class InitialCustomCoordinate extends OrekitCoordinate
{
    private static final long serialVersionUID = 1L;

    private final KeplerianOrbit initialOrbit;

    public InitialCustomCoordinate(KeplerianOrbit initialOrbit)
    {
        super(initialOrbit.getPVCoordinates(), initialOrbit.getDate(), initialOrbit.getFrame());
        this.initialOrbit = initialOrbit;
    }

    public KeplerianOrbit getInitialOrbit()
    {
        return initialOrbit;
    }
}
