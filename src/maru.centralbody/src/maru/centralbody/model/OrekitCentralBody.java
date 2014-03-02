package maru.centralbody.model;

import maru.core.model.AbstractCentralBody;
import maru.core.model.ICoordinate;
import maru.core.model.resource.IMaruResource;
import maru.core.model.utils.CentralBodyUtils;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.orekit.bodies.BodyShape;
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
    private BodyShape bodyShape;

    public OrekitCentralBody(CelestialBody body,
                             double equatorialRadius,
                             double flattening,
                             double gm,
                             IMaruResource mapImage) throws OrekitException
    {
        super(body.getName());

        this.body = body;

        setProperties(equatorialRadius, flattening, gm);
        setTexture(mapImage);
    }

    @Override
    public BodyShape getBodyShape()
    {
        return bodyShape;
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
    public GeodeticPoint getIntersection(ICoordinate src) throws OrekitException
    {
        return CentralBodyUtils.getIntersection(this, src);
    }

    @Override
    public GeodeticPoint getIntersection(ICoordinate src, ICoordinate dest) throws OrekitException
    {
        return CentralBodyUtils.getIntersection(this, src, dest);
    }

    @Override
    public void setProperties(double equatorialRadius, double flattening, double gm)
    {
        try
        {
            this.bodyShape = new OneAxisEllipsoid(equatorialRadius,
                                                  flattening,
                                                  body.getBodyOrientedFrame());
        }
        catch (OrekitException e)
        {
            throw new RuntimeException("Unable to change the equatorial radius.", e);
        }

        // no exception thrown. change the properties.
        super.setProperties(equatorialRadius, flattening, gm);
    }
}
