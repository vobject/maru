package maru.centralbody.earth;

import maru.IMaruResource;
import maru.centralbody.OrekitCentralBody;

import org.orekit.bodies.CelestialBodyFactory;
import org.orekit.errors.OrekitException;
import org.orekit.utils.Constants;

public class Earth extends OrekitCentralBody
{
    private static final long serialVersionUID = 1L;

    public Earth(IMaruResource mapImage) throws OrekitException
    {
        super(CelestialBodyFactory.getEarth(), mapImage);
    }

    @Override
    public double getGM()
    {
        return Constants.WGS84_EARTH_MU;
    }

    @Override
    public double getEquatorialRadius()
    {
        return Constants.WGS84_EARTH_EQUATORIAL_RADIUS;
    }

    @Override
    public double getFlattening()
    {
        return Constants.WGS84_EARTH_FLATTENING;
    }
}
