package maru.spacecraft.tlesatellite;

import maru.core.model.ICoordinate;
import maru.spacecraft.OrekitSpacecraft;

public class TleSatellite extends OrekitSpacecraft
{
    private static final long serialVersionUID = 1L;

    private final String category;

    public TleSatellite(String name)
    {
        this(name, "Custom");
    }

    public TleSatellite(String name, String category)
    {
        super(name);
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
    public void setInitialCoordinate(ICoordinate coordinate)
    {
        // make sure we get coordinates based on the new initial coordinate
        getPropagator().clearCoordinateCache();

        super.setInitialCoordinate(coordinate);
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
