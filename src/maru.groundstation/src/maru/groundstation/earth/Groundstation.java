package maru.groundstation.earth;

import maru.core.model.AbstractGroundstation;
import maru.core.model.ICentralBody;

import org.orekit.bodies.GeodeticPoint;

public class Groundstation extends AbstractGroundstation
{
    private static final long serialVersionUID = 1L;

    public Groundstation(String name, GeodeticPoint position, ICentralBody centralBody)
    {
        super(name, position, centralBody);
    }
}
