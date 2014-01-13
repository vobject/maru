package maru.centralbody;

import maru.IMaruResource;
import maru.core.model.ICoordinate;
import maru.core.model.template.AbstractCentralBody;
import maru.core.utils.OrekitUtils;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.orekit.bodies.CelestialBody;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.bodies.OneAxisEllipsoid;
import org.orekit.errors.OrekitException;
import org.orekit.frames.Frame;
import org.orekit.time.AbsoluteDate;

public abstract class OrekitCentralBody extends AbstractCentralBody
{
    private static final long serialVersionUID = 1L;

    private final CelestialBody body;
    private final OneAxisEllipsoid ellipsoid;

    public OrekitCentralBody(CelestialBody body, IMaruResource mapImage) throws OrekitException
    {
        super(body.getName());
        setTexture(mapImage);

        this.body = body;
        setFrame(body.getInertiallyOrientedFrame());

        this.ellipsoid = new OneAxisEllipsoid(getEquatorialRadius(),
                                              getFlattening(),
                                              getFrame());
    }

    @Override
    public Vector3D getPosition(Frame frame, long time) throws OrekitException
    {
        AbsoluteDate orekitDate = OrekitUtils.toAbsoluteDate(time);
        return body.getPVCoordinates(orekitDate, frame).getPosition();
    }

    @Override
    public GeodeticPoint toGeodeticPoint(Vector3D position, Frame frame, long time) throws OrekitException
    {
        return ellipsoid.transform(position, frame, OrekitUtils.toAbsoluteDate(time));
    }

    @Override
    public GeodeticPoint toGeodeticPoint(ICoordinate coordinate) throws OrekitException
    {
        return ellipsoid.transform(coordinate.getPosition(),
                                   coordinate.getFrame(),
                                   OrekitUtils.toAbsoluteDate(coordinate.getTime()));
    }

    @Override
    public Vector3D toCartesianPoint(GeodeticPoint point)
    {
        return ellipsoid.transform(point);
    }
}
