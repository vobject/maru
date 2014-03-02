package maru.centralbody.model.projection;

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
    private double mapWidthCoeff;
    private double mapHeightCoeff;

    private int cacheSize;
    private Map<ICoordinate, FlatMapPosition> cache;

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
        this.mapWidthCoeff = mapWidth / 360.0;
        this.mapHeightCoeff = mapHeight / 180.0;
        cache.clear();
    }

    @Override
    public void setCacheSize(int coordinateCount)
    {
        this.cacheSize = coordinateCount;
        this.cache = new HashMap<>(coordinateCount);
    }

    @Override
    public FlatMapPosition project(ICoordinate coordinate) throws OrekitException
    {
        if (cache.size() >= cacheSize) {
            cache.clear();
        }

        FlatMapPosition cachedProjection = cache.get(coordinate);
        if (cachedProjection != null) {
            return cachedProjection;
        }

        GeodeticPoint point = centralBody.getIntersection(coordinate);
        FlatMapPosition projected = project(point);

        cache.put(coordinate, projected);
        return projected;
    }

    @Override
    public FlatMapPosition project(GeodeticPoint point)
    {
        return project(point.getLatitude(), point.getLongitude());
    }

    @Override
    public FlatMapPosition project(double latitude, double longitude)
    {
        int x = (int) ((Math.toDegrees(longitude) + 180.0) * mapWidthCoeff);
        int y = (int) (((-1.0 * Math.toDegrees(latitude)) + 90.0) * mapHeightCoeff);

        return new FlatMapPosition(x, y);
    }
}
