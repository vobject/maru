package maru.core.model;

import java.util.Map;

import maru.MaruRuntimeException;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.time.AbsoluteDate;

public abstract class AbstractGroundstation extends AbstractVisibleElement implements IGroundstation
{
    private static final long serialVersionUID = 1L;

    /** Position of the ground station as a surface-relative point. */
    private GeodeticPoint geodeticPoint;

    /** Position of the ground station as a Cartesian point. */
    private Vector3D cartesianPoint;

    public AbstractGroundstation(String name, GeodeticPoint position, ICentralBody centralBody)
    {
        super(name);

        // geodeticPoint and cartesianPoint have to be kept in sync
        this.geodeticPoint = new GeodeticPoint(position.getLatitude(),
                                               position.getLongitude(),
                                               position.getAltitude());
        this.cartesianPoint = centralBody.getCartesianPoint(geodeticPoint);
    }

    @Override
    public ICentralBody getCentralBody()
    {
        IScenarioProject scenario = getScenarioProject();
        if (scenario == null) {
            // the object was not yet added to a scenario
            throw new MaruRuntimeException("Invalid call to getCentralBody()");
        }
        return scenario.getCentralBody();
    }

    @Override
    public void startTimeChanged(AbsoluteDate date)
    {

    }

    @Override
    public void stopTimeChanged(AbsoluteDate date)
    {

    }

    @Override
    public void currentTimeChanged(AbsoluteDate date)
    {

    }

    @Override
    public void centralbodyChanged()
    {
        this.cartesianPoint = getCentralBody().getCartesianPoint(geodeticPoint);
    }

    @Override
    public GeodeticPoint getGeodeticPosition()
    {
        return geodeticPoint;
    }

    @Override
    public Vector3D getCartesianPosition()
    {
        return cartesianPoint;
    }

    public void setGeodeticPosition(GeodeticPoint position)
    {
        this.geodeticPoint = new GeodeticPoint(position.getLatitude(),
                                               position.getLongitude(),
                                               position.getAltitude());
        this.cartesianPoint = getCentralBody().getCartesianPoint(geodeticPoint);
    }

    public void addTimeProvider(ITimeProvider provider)
    {
        provider.addTimeListener(this);
    }

    public void removeTimeProvider(ITimeProvider provider)
    {
        provider.removeTimeListener(this);
    }

    @Override
    public Map<String, String> getPropertyMap()
    {
        Map<String, String> props = super.getPropertyMap();

        props.put("Latitude (rad)", Double.toString(geodeticPoint.getLatitude()));
        props.put("Longitude (rad)", Double.toString(geodeticPoint.getLongitude()));
        props.put("Altitude (m)", Double.toString(geodeticPoint.getAltitude()));

        props.put("Zenith", geodeticPoint.getZenith().toString());
        props.put("Nadir", geodeticPoint.getNadir().toString());
        props.put("North", geodeticPoint.getNorth().toString());
        props.put("South", geodeticPoint.getSouth().toString());
        props.put("East", geodeticPoint.getEast().toString());
        props.put("West", geodeticPoint.getWest().toString());

        props.put("X", Double.toString(cartesianPoint.getX()));
        props.put("Y", Double.toString(cartesianPoint.getY()));
        props.put("Z", Double.toString(cartesianPoint.getZ()));

        return props;
    }
}
