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

    public TleSatellite(InitialTleCoordinate initialPosition, String category)
    {
        super(initialPosition.getName(), initialPosition);
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
}
