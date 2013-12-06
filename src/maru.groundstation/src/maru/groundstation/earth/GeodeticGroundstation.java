package maru.groundstation.earth;

import maru.core.model.template.Groundstation;

public class GeodeticGroundstation extends Groundstation
{
    private static final long serialVersionUID = 1L;

    public GeodeticGroundstation(String name, GeodeticCoordinate initialPosition)
    {
        super(name, initialPosition);
    }
}
