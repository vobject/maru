package maru.centralbody.earth;

import maru.IMaruResource;
import maru.core.model.template.CentralBody;
import maru.core.units.Frame;
import maru.core.units.Position;

import org.orekit.OrekitUtils;
import org.orekit.bodies.CelestialBody;
import org.orekit.bodies.CelestialBodyFactory;
import org.orekit.errors.OrekitException;
import org.orekit.time.AbsoluteDate;
import org.orekit.utils.PVCoordinates;

public class Earth extends CentralBody
{
    private static final long serialVersionUID = 1L;

    /** Standard gravitational parameter (m³/s²); JGM3 */
    private static final double DEFAULT_EARTH_GM = 3.986004415e+14;

    /** Earth equatorial radius (m); WGS84. */
    private static final double DEFAULT_EARTH_EQUATORIAL_RADIUS = 6378137.0;

    /** Earth flattening; WGS84 */
    private static final double DEFAULT_EARTH_FLATTENING = (1.0 / 298.257223563);

    public Earth(IMaruResource mapImage)
    {
        super("Earth");
        setTexture(mapImage);

        setGM(DEFAULT_EARTH_GM);
        setEquatorialRadius(DEFAULT_EARTH_EQUATORIAL_RADIUS);
        setFlattening(DEFAULT_EARTH_FLATTENING);
    }

    @Override
    public Frame getFrame()
    {
        return Frame.ITRF2005;
    }

    @Override
    public Position getPosition(long time, Frame frame)
    {
        try
        {
            CelestialBody earthBody = CelestialBodyFactory.getEarth();
            AbsoluteDate orekitDate = OrekitUtils.toAbsoluteDate(time);
            org.orekit.frames.Frame orekitFrame = OrekitUtils.toOrekitFrame(frame);
            PVCoordinates orekitCoordinates = earthBody.getPVCoordinates(orekitDate, orekitFrame);
            return OrekitUtils.toPosition(orekitCoordinates.getPosition());
        }
        catch (OrekitException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
