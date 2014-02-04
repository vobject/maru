package maru.spacecraft;

import maru.core.model.ICentralBody;
import maru.core.model.ICoordinate;
import maru.core.utils.EclipseState;
import maru.core.utils.EclipseUtils;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.orekit.errors.OrekitException;
import org.orekit.frames.Frame;
import org.orekit.time.AbsoluteDate;
import org.orekit.utils.PVCoordinates;

public class OrekitCoordinate implements ICoordinate
{
    private static final long serialVersionUID = 1L;

    private final ICentralBody centralBody;
    private final PVCoordinates pvCoordinates;
    private final AbsoluteDate date;
    private final Frame frame;
    private final EclipseState eclipseState;

    public OrekitCoordinate(ICentralBody centralBody,
                            PVCoordinates coordinates,
                            AbsoluteDate date,
                            Frame frame) throws OrekitException
    {
        this.centralBody = centralBody;
        this.pvCoordinates = coordinates;
        this.date = date;
        this.frame = frame;
        this.eclipseState = EclipseUtils.getEclipseState(centralBody, this);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (super.equals(obj)) {
            return true;
        }

        if (!(obj instanceof OrekitCoordinate)) {
            return false;
        }
        OrekitCoordinate other = (OrekitCoordinate) obj;

        Vector3D myPos = pvCoordinates.getPosition();
        Vector3D otherPos = other.pvCoordinates.getPosition();

        if ((myPos.getX() != otherPos.getX()) ||
            (myPos.getY() != otherPos.getY()) ||
            (myPos.getZ() != otherPos.getZ()))
        {
            return false;
        }

        if ((this.centralBody != other.centralBody) ||
            (this.date.compareTo(other.date) != 0) ||
            (!this.frame.equals(other.frame))) {
            return false;
        }
        return true;
    }

    @Override
    public ICentralBody getCentralBody()
    {
        return centralBody;
    }

    @Override
    public Vector3D getPosition()
    {
        return pvCoordinates.getPosition();
    }

    @Override
    public Vector3D getVelocity()
    {
        return pvCoordinates.getVelocity();
    }

    @Override
    public Frame getFrame()
    {
        return frame;
    }

    @Override
    public AbsoluteDate getDate()
    {
        return date;
    }

    @Override
    public EclipseState getEclipseState()
    {
        return eclipseState;
    }

    public PVCoordinates getPvCoordinates()
    {
        return pvCoordinates;
    }
}
