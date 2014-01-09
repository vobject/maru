package maru.spacecraft;

import maru.core.model.ICentralBody;
import maru.core.model.ICoordinate;
import maru.core.model.template.Spacecraft;
import maru.core.units.Position;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.util.FastMath;
import org.orekit.OrekitUtils;
import org.orekit.bodies.CelestialBody;
import org.orekit.bodies.CelestialBodyFactory;
import org.orekit.errors.OrekitException;
import org.orekit.frames.Frame;
import org.orekit.time.AbsoluteDate;

public abstract class OrekitSpacecraft extends Spacecraft
{
    private static final long serialVersionUID = 1L;

    /** Sun radius in meters. From org.orekit.utils.Constants. */
    private static final double SUN_RADIUS_METERS = 695500000.0;

    public OrekitSpacecraft(String name, OrekitCoordinate initialPosition)
    {
        super(name, initialPosition);
    }

    @Override
    public boolean inUmbra(ICoordinate coordinate)
    {
        return (inEclipse(coordinate, true)) < 0;
    }

    @Override
    public boolean inUmbraOrPenumbra(ICoordinate coordinate)
    {
        return (inEclipse(coordinate, false)) < 0;
    }

    private double inEclipse(ICoordinate sat, boolean total)
    {
        // based on orekit EclipseDetector class

        try
        {
            CelestialBody sun = CelestialBodyFactory.getSun();
            ICentralBody earth = getCentralBody();

            AbsoluteDate date = OrekitUtils.toAbsoluteDate(sat.getTime());
            Frame frame = OrekitUtils.toOrekitFrame(sat.getFrame());
            Position position = earth.getPosition(sat.getTime(), sat.getFrame());

            Vector3D pted = sun.getPVCoordinates(date, frame).getPosition();
            Vector3D ping = OrekitUtils.toOrekitPosition(position);
            Vector3D psat = OrekitUtils.toOrekitPosition(sat.getPosition());

            Vector3D ps = pted.subtract(psat);
            Vector3D po = ping.subtract(psat);

            double angle = Vector3D.angle(ps, po);
            double rs = FastMath.asin(SUN_RADIUS_METERS / ps.getNorm());
            double ro = FastMath.asin(earth.getEquatorialRadius() / po.getNorm());

            return total ? (angle - ro + rs) : (angle - ro - rs);
        }
        catch (OrekitException e)
        {
            e.printStackTrace();
            return 0;
        }
    }
}
