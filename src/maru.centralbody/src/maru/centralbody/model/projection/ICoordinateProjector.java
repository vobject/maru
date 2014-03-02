package maru.centralbody.model.projection;

import maru.core.model.ICentralBody;
import maru.core.model.ICoordinate;

import org.orekit.bodies.GeodeticPoint;
import org.orekit.errors.OrekitException;

public interface ICoordinateProjector
{
    void setCentralBody(ICentralBody centralBody);
    void setMapSize(int mapWidth, int mapHeight);
    void setCacheSize(int size);

    FlatMapPosition project(ICoordinate coordinate) throws OrekitException;
    FlatMapPosition project(GeodeticPoint point);
    FlatMapPosition project(double latitude, double longitude);
}
