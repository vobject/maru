package maru.spacecraft.model.custom;

import maru.spacecraft.model.OrekitSpacecraft;

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
