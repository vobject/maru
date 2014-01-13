//package maru.groundstation.earth;
//
//import maru.core.model.ICentralBody;
//import maru.core.model.ICoordinate;
//
//import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
//import org.orekit.bodies.GeodeticPoint;
//import org.orekit.frames.Frame;
//
//public class GeodeticCoordinate implements ICoordinate
//{
//    private static final long serialVersionUID = 1L;
//
//    /** Position of the coordinate as a surface-relative point. */
//    private final GeodeticPoint geodeticPoint;
//
//    /** Elevation in radiant */
//    private final double elevation;
//
//    /** Cartesian position of the groundstation on the central body. */
//    private Vector3D cartesianPoint;
//
//    /** This kind of ground station does not move. */
//    private static final Vector3D DEFAULT_VELOCITY = Vector3D.ZERO;
//
//    /** Time of the groundstation's position. */
//    private long time;
//
//    /** The same frame as the central body that the groundstation was initialized with. */
//    private Frame frame;
//
//    public GeodeticCoordinate(ICentralBody centralBody,
//                              double latitude,
//                              double longitude,
//                              double altitude,
//                              double elevation,
//                              long time)
//    {
//        this.geodeticPoint = new GeodeticPoint(latitude, longitude, altitude);
//        this.elevation = elevation;
//
//        this.cartesianPoint = centralBody.toCartesianPoint(geodeticPoint);
//        this.time = time;
//        this.frame = centralBody.getFrame();
//    }
//
//    public GeodeticCoordinate(GeodeticCoordinate coordinate)
//    {
//        this.geodeticPoint = new GeodeticPoint(coordinate.getLatitude(),
//                                               coordinate.getLongitude(),
//                                               coordinate.getAltitude());
//        this.elevation = coordinate.elevation;
//
//        this.cartesianPoint = new Vector3D(coordinate.cartesianPoint.getX(),
//                                     coordinate.cartesianPoint.getY(),
//                                     coordinate.cartesianPoint.getZ());
//        this.time = coordinate.time;
//        this.frame = coordinate.frame;
//    }
//
//    @Override
//    public Vector3D getPosition()
//    {
//        return cartesianPoint;
//    }
//
//    @Override
//    public Vector3D getVelocity()
//    {
//        return DEFAULT_VELOCITY;
//    }
//
//    @Override
//    public long getTime()
//    {
//        return time;
//    }
//
//    @Override
//    public Frame getFrame()
//    {
//        return frame;
//    }
//
//    public void setPosition(Vector3D position)
//    {
//        this.cartesianPoint = position;
//    }
//
//    public void setTime(long time)
//    {
//        this.time = time;
//    }
//
//    public void setFrame(Frame frame)
//    {
//        this.frame = frame;
//    }
//
//    public double getLatitude()
//    {
//        return geodeticPoint.getLatitude();
//    }
//
//    public double getLongitude()
//    {
//        return geodeticPoint.getLongitude();
//    }
//
//    public double getAltitude()
//    {
//        return geodeticPoint.getAltitude();
//    }
//
//    public double getElevation()
//    {
//        return elevation;
//    }
//}
