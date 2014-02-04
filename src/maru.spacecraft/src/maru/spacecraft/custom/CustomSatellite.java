package maru.spacecraft.custom;

import maru.core.model.ICoordinate;
import maru.spacecraft.OrekitSpacecraft;

import org.orekit.errors.OrekitException;

public class CustomSatellite extends OrekitSpacecraft
{
    private static final long serialVersionUID = 1L;

    public CustomSatellite(String name)
    {
        super(name);
    }

    @Override
    public InitialCustomCoordinate getInitialCoordinate()
    {
        return (InitialCustomCoordinate) super.getInitialCoordinate();
    }

    @Override
    public void setInitialCoordinate(ICoordinate coordinate) throws OrekitException
    {
        // make sure we get coordinates based on the new initial coordinate
        getPropagator().clearCoordinateCache();

        super.setInitialCoordinate(coordinate);
    }

    @Override
    public void centralbodyChanged()
    {
        try
        {
            setInitialCoordinate(InitialCustomCoordinate.recreate(getInitialCoordinate()));
        }
        catch (OrekitException e)
        {
            throw new RuntimeException("Failed to update initial coordinate.", e);
        }
    }
}
