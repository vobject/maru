package maru.spacecraft.tlesatellite;

import maru.spacecraft.OrekitSpacecraft;

public class TleSatellite extends OrekitSpacecraft
{
    private static final long serialVersionUID = 1L;

    private final String category;

    public TleSatellite(InitialTleCoordinate initialPosition)
    {
        this(initialPosition, "Custom");
    }

    public TleSatellite(InitialTleCoordinate initialCoordinate, String category)
    {
        super(initialCoordinate.getName(), initialCoordinate);
        this.category = category;
    }

    public String getCategory()
    {
        return category;
    }

    @Override
    public InitialTleCoordinate getInitialCoordinate()
    {
        return (InitialTleCoordinate) super.getInitialCoordinate();
    }

    @Override
    public Sgp4Propagator getPropagator()
    {
        return (Sgp4Propagator) super.getPropagator();
    }

    @Override
    public void centralbodyChanged()
    {
        // TleSatellite does not rely on any ICentralBody parameters, so its
        // InitialTleCoordinate member does not have to be changed.

        // make sure we get fresh coordinates based on the new central body
        getPropagator().clearCoordinateCache();
    }
}
