package maru.spacecraft;

import maru.core.model.ICentralBody;
import maru.core.model.ICoordinate;
import maru.core.model.template.AbstractSpacecraft;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.util.FastMath;
import org.orekit.bodies.CelestialBody;
import org.orekit.bodies.CelestialBodyFactory;
import org.orekit.errors.OrekitException;
import org.orekit.frames.Frame;
import org.orekit.time.AbsoluteDate;
import org.orekit.utils.Constants;

public abstract class OrekitSpacecraft extends AbstractSpacecraft
{
    private static final long serialVersionUID = 1L;

    public OrekitSpacecraft(String name)
    {
        super(name);
    }

    @Override
    public EclipseState getEclipseState(ICoordinate coordinate)
    {
        // based on orekit EclipseDetector class

        try
        {
            CelestialBody sun = CelestialBodyFactory.getSun();
            ICentralBody earth = getCentralBody();

            AbsoluteDate date = coordinate.getDate();
            Frame frame = coordinate.getFrame();
            Vector3D position = earth.getPosition(coordinate.getFrame(), date);

            Vector3D pted = sun.getPVCoordinates(date, frame).getPosition();
            Vector3D ping = position;
            Vector3D psat = coordinate.getPosition();

            Vector3D ps = pted.subtract(psat);
            Vector3D po = ping.subtract(psat);

            double angle = Vector3D.angle(ps, po);
            double rs = FastMath.asin(Constants.SUN_RADIUS / ps.getNorm());
            double ro = FastMath.asin(earth.getEquatorialRadius() / po.getNorm());

            if ((angle - ro + rs) < 0) {
                return EclipseState.Umbra;
            } else if ((angle - ro - rs) < 0) {
                return EclipseState.UmbraOrPenumbra;
            } else {
                return EclipseState.None;
            }
        }
        catch (OrekitException e)
        {
            e.printStackTrace();
            return EclipseState.None;
        }
    }
}
