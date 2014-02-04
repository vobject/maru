package maru.centralbody.bodies;

import maru.IMaruResource;

import org.orekit.bodies.CelestialBodyFactory;
import org.orekit.errors.OrekitException;
import org.orekit.utils.Constants;

public class Earth extends OrekitCentralBody
{
    private static final long serialVersionUID = 1L;

    public Earth(IMaruResource mapImage) throws OrekitException
    {
        super(CelestialBodyFactory.getEarth(),
              Constants.WGS84_EARTH_EQUATORIAL_RADIUS,
              Constants.WGS84_EARTH_FLATTENING,
              Constants.WGS84_EARTH_MU,
              mapImage);
    }
}
