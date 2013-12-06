package maru.spacecraft.ckesatellite;

import maru.spacecraft.OrekitSpacecraft;

public class KeplerSatellite extends OrekitSpacecraft
{
    private static final long serialVersionUID = 1L;

    public KeplerSatellite(String name, InitialKeplerCoordinate initialPosition)
    {
        super(name, initialPosition);
    }

    @Override
    public InitialKeplerCoordinate getInitialCoordinate()
    {
        return (InitialKeplerCoordinate) super.getInitialCoordinate();
    }
}
