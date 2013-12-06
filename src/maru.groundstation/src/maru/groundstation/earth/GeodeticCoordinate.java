package maru.groundstation.earth;

import maru.core.model.ICentralBody;
import maru.core.model.ICoordinate;
import maru.core.units.Frame;
import maru.core.units.Position;
import maru.core.units.Velocity;

public class GeodeticCoordinate implements ICoordinate
{
    private static final long serialVersionUID = 1L;

    /** Latitude in radiant */
    private final double latitude;

    /** Longitude in radiant */
    private final double longitude;

    /** Altitude in meters */
    private final double altitude;

    /** Elevation in radiant */
    private final double elevation;

    /** This kind of ground station does not move. */
    private static final Velocity DEFAULT_VELOCITY = new Velocity(0.0, 0.0, 0.0);

    /** Cartesian position of the groundstation on the central body. */
    private Position position;

    /** Time of the groundstation's position. */
    private long time;

    /** The same frame as the central body that the groundstation was initialized with. */
    private Frame frame;

    public GeodeticCoordinate(ICentralBody centralBody,
                              double latitude,
                              double longitude,
                              double altitude,
                              double elevation,
                              long time)
    {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.elevation = elevation;

        this.position = toCartesian(centralBody.getEquatorialRadius(), centralBody.getFlattening());
        this.time = time;
        this.frame = centralBody.getFrame();
    }

    public GeodeticCoordinate(GeodeticCoordinate coordinate)
    {
        this.latitude = coordinate.latitude;
        this.longitude = coordinate.longitude;
        this.altitude = coordinate.altitude;
        this.elevation = coordinate.elevation;

        this.position = new Position(coordinate.position);
        this.time = coordinate.time;
        this.frame = coordinate.frame;
    }

    @Override
    public Position getPosition()
    {
        return position;
    }

    @Override
    public Velocity getVelocity()
    {
        return DEFAULT_VELOCITY;
    }

    @Override
    public long getTime()
    {
        return time;
    }

    @Override
    public Frame getFrame()
    {
        return frame;
    }

    public void setPosition(Position position)
    {
        this.position = position;
    }

    public void setTime(long time)
    {
        this.time = time;
    }

    public void setFrame(Frame frame)
    {
        this.frame = frame;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public double getAltitude()
    {
        return altitude;
    }

    public double getElevation()
    {
        return elevation;
    }

    private Position toCartesian(double equatorialRadius, double flattening)
    {
        // based on orekit OneAxisEllipsoid.transform(GeodeticPoint point)

        double cLambda = Math.cos(longitude);
        double sLambda = Math.sin(longitude);
        double cPhi = Math.cos(latitude);
        double sPhi = Math.sin(latitude);

        double e2 = (flattening * (2.0 - flattening));
        double n = equatorialRadius / Math.sqrt(1.0 - e2 * sPhi * sPhi);
        double r = (n + altitude) * cPhi;
        double g = 1.0 - flattening;

        double x = r * cLambda;
        double y = r * sLambda;
        double z = (g * g * n + altitude) * sPhi;
        return new Position(x, y, z);
    }
}
