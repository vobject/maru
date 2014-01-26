package maru.centralbody.bodies;

import maru.IMaruResource;
import maru.core.model.AbstractCentralBody;
import maru.core.model.ICoordinate;

import org.apache.commons.math3.geometry.euclidean.threed.Line;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.orekit.bodies.BodyShape;
import org.orekit.bodies.CelestialBody;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.bodies.OneAxisEllipsoid;
import org.orekit.errors.OrekitException;
import org.orekit.frames.Frame;
import org.orekit.frames.Transform;
import org.orekit.time.AbsoluteDate;

public abstract class OrekitCentralBody extends AbstractCentralBody
{
    private static final long serialVersionUID = 1L;

    private final CelestialBody body;
    private final BodyShape bodyShape;

    public OrekitCentralBody(CelestialBody body, IMaruResource mapImage) throws OrekitException
    {
        super(body.getName());
        setTexture(mapImage);

        this.body = body;
        this.bodyShape = new OneAxisEllipsoid(getEquatorialRadius(),
                                              getFlattening(),
                                              body.getBodyOrientedFrame());
    }

    @Override
    public Frame getFrame()
    {
        return bodyShape.getBodyFrame();
    }

    @Override
    public Vector3D getPosition(Frame frame, AbsoluteDate date) throws OrekitException
    {
        return body.getPVCoordinates(date, frame).getPosition();
    }

    @Override
    public GeodeticPoint getIntersection(ICoordinate from) throws OrekitException
    {
        Transform transform = getFrame().getTransformTo(from.getFrame(), from.getDate());
        Vector3D zeroVecInFromFrame = transform.transformPosition(Vector3D.ZERO);

        return getIntersection(from, zeroVecInFromFrame);
    }

    @Override
    public GeodeticPoint getIntersection(ICoordinate from, ICoordinate to) throws OrekitException
    {
        Transform transform = to.getFrame().getTransformTo(from.getFrame(), from.getDate());
        Vector3D toVecInFromFrame = transform.transformPosition(to.getPosition());

        return getIntersection(from, toVecInFromFrame);
    }

    @Override
    public GeodeticPoint getIntersection(ICoordinate from, Vector3D to) throws OrekitException
    {
        return bodyShape.getIntersectionPoint(
            new Line(from.getPosition(), to),
            from.getPosition(),
            from.getFrame(),
            from.getDate()
        );
    }

    @Override
    public Vector3D getCartesianPoint(GeodeticPoint point)
    {
        return bodyShape.transform(point);
    }

    @Override
    public double getDistanceToHorizon(ICoordinate coordinate) throws OrekitException
    {
        double radius = getEquatorialRadius();
        double radius2 = radius * 2;

        Transform transform = getFrame().getTransformTo(coordinate.getFrame(), coordinate.getDate());
        Vector3D zeroVecInCoordinateFrame = transform.transformPosition(Vector3D.ZERO);

        // calculate the distance from the coordinate to the zero vector.
        // it is the altitude from the central bodies center.

        double coodinateAltitude = coordinate.getPosition().distance(zeroVecInCoordinateFrame);
        double coodinateAltitudeAboveGround = coodinateAltitude - radius;

        return Math.sqrt(coodinateAltitudeAboveGround * (radius2 + coodinateAltitudeAboveGround));
    }

    protected CelestialBody getBody()
    {
        return body;
    }

    protected BodyShape getBodyShape()
    {
        return bodyShape;
    }
}
