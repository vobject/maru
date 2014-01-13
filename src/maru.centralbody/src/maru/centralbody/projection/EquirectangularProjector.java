package maru.centralbody.projection;

import java.util.HashMap;
import java.util.Map;

import maru.core.model.ICentralBody;
import maru.core.model.ICoordinate;

import org.orekit.bodies.GeodeticPoint;
import org.orekit.errors.OrekitException;

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
    public EquirectangularCoordinate project(ICoordinate coordinate) throws OrekitException
    {
        if (cache.size() >= cacheSize) {
            cache.clear();
        }

        EquirectangularCoordinate cachedProjection = cache.get(coordinate);
        if (cachedProjection != null) {
            return cachedProjection;
        }

        GeodeticPoint point = centralBody.toGeodeticPoint(coordinate);
        EquirectangularCoordinate projected = project(point);

        cache.put(coordinate, projected);
        return projected;
    }

    @Override
    public EquirectangularCoordinate project(GeodeticPoint point)
    {
        int x = (int) ((Math.toDegrees(point.getLongitude()) + 180.0) * (mapWidth / 360.0));
        int y = (int) (((-1.0 * Math.toDegrees(point.getLatitude())) + 90.0) * (mapHeight / 180.0));

        return new EquirectangularCoordinate(x, y);
    }
}
