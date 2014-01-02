package maru.groundstation.earth;

import maru.core.model.template.Groundstation;

public class GeodeticGroundstation extends Groundstation
{
    private static final long serialVersionUID = 1L;

    public GeodeticGroundstation(String name, GeodeticCoordinate initialPosition)
    {
        super(name, initialPosition);
    }

    @Override
    public GeodeticCoordinate getInitialCoordinate()
    {
        return (GeodeticCoordinate) super.getInitialCoordinate();
    }

    @Override
    public void centralbodyChanged()
    {
        GeodeticCoordinate newCoordinate = new GeodeticCoordinate(
            getCentralBody(),
            getInitialCoordinate().getLatitude(),
            getInitialCoordinate().getLongitude(),
            getInitialCoordinate().getAltitude(),
            getInitialCoordinate().getElevation(),
            getInitialCoordinate().getTime()
        );

        setInitialCoordinate(newCoordinate);
    }
}
