package maru.centralbody.projection;

import java.util.HashMap;
import java.util.Map;

import maru.MaruException;
import maru.core.model.ICentralBody;
import maru.core.model.ICoordinate;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.orekit.OrekitUtils;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.bodies.OneAxisEllipsoid;
import org.orekit.time.AbsoluteDate;

public class EquirectangularProjector implements ICoordinateProjector
{
    private ICentralBody centralBody;
    private int mapWidth;
    private int mapHeight;

    private int cacheSize;
    private Map<ICoordinate, EquirectangularCoordinate> cache;

    @Override
    public void setCentralBody(ICentralBody centralBody)
    {
        this.centralBody = centralBody;
    }

    @Override
    public void setMapSize(int mapWidth, int mapHeight)
    {
        if ((this.mapWidth == mapWidth) && (this.mapHeight == mapHeight)) {
            return;
        }
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        cache.clear();
    }

    @Override
    public void setCacheSize(int coordinateCount)
    {
        this.cacheSize = coordinateCount;
        this.cache = new HashMap<>(coordinateCount);
    }

    @Override
    public EquirectangularCoordinate project(ICoordinate coordinate) throws MaruException
    {
        if (cache.size() >= cacheSize) {
            cache.clear();
        }

        EquirectangularCoordinate cachedProjection = cache.get(coordinate);
        if (cachedProjection != null) {
            return cachedProjection;
        }

        OneAxisEllipsoid ellipsoid =
            new OneAxisEllipsoid(centralBody.getEquatorialRadius(),
                                 centralBody.getFlattening(),
                                 OrekitUtils.toOrekitFrame(centralBody.getFrame()));

        Vector3D position = new Vector3D(coordinate.getPosition().getX(),
                                         coordinate.getPosition().getY(),
                                         coordinate.getPosition().getZ());
        AbsoluteDate date = OrekitUtils.toAbsoluteDate(coordinate.getTime());
        org.orekit.frames.Frame frame = OrekitUtils.toOrekitFrame(coordinate.getFrame());
        GeodeticPoint point;

        try {
            point = ellipsoid.transform(position, frame, date);
        } catch (Exception e) {
            throw new MaruException(e);
        }

        int x = (int) ((Math.toDegrees(point.getLongitude()) + 180.0) * (mapWidth / 360.0));
        int y = (int) (((-1.0 * Math.toDegrees(point.getLatitude())) + 90.0) * (mapHeight / 180.0));

        EquirectangularCoordinate projected = new EquirectangularCoordinate(x, y);
        cache.put(coordinate, projected);
        return projected;
    }
}
