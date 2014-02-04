package maru.core.utils;

import maru.core.model.ICentralBody;
import maru.core.model.ICoordinate;
import maru.core.model.ISpacecraft;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.util.FastMath;
import org.orekit.bodies.CelestialBody;
import org.orekit.bodies.CelestialBodyFactory;
import org.orekit.errors.OrekitException;
import org.orekit.frames.Frame;
import org.orekit.time.AbsoluteDate;
import org.orekit.utils.Constants;

public final class EclipseUtils
{
    private static CelestialBody sun;

    static
    {
        try {
            sun = CelestialBodyFactory.getSun();
        } catch (OrekitException e) {
            e.printStackTrace();
        }
    }

    public static EclipseState getEclipseState(ICentralBody centralBody, ICoordinate coordinate) throws OrekitException
    {
        // based on orekit 6.0 EclipseDetector class

        AbsoluteDate date = coordinate.getDate();
        Frame frame = coordinate.getFrame();

        Vector3D pted = sun.getPVCoordinates(date, frame).getPosition();
        Vector3D ping = centralBody.getPosition(frame, date);
        Vector3D psat = coordinate.getPosition();

        Vector3D ps = pted.subtract(psat);
        Vector3D po = ping.subtract(psat);

        double angle = Vector3D.angle(ps, po);
        double rs = FastMath.asin(Constants.SUN_RADIUS / ps.getNorm());
        double ro = FastMath.asin(centralBody.getEquatorialRadius() / po.getNorm());

        if ((angle - ro + rs) < 0) {
            return EclipseState.Umbra;
        } else if ((angle - ro - rs) < 0) {
            return EclipseState.Penumbra;
        } else {
            return EclipseState.None;
        }
    }

    public static EclipseState getEclipseState(ISpacecraft spacecraft) throws OrekitException
    {
        return getEclipseState(spacecraft.getCentralBody(), spacecraft.getCurrentCoordinate());
    }
}
