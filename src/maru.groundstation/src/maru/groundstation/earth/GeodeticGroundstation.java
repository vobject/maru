package maru.groundstation.earth;

import maru.core.model.ICoordinate;
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
    public void setInitialCoordinate(ICoordinate coordinate)
    {
        // make sure we get coordinates based on the new initial coordinate
        getPropagator().clearCoordinateCache();

        super.setInitialCoordinate(coordinate);
    }

    @Override
    public GeodeticGroundstationPropagator getPropagator()
    {
        return (GeodeticGroundstationPropagator) super.getPropagator();
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
