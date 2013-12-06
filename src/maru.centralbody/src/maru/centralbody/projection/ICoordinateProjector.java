package maru.centralbody.projection;

import maru.MaruException;
import maru.core.model.ICentralBody;
import maru.core.model.ICoordinate;

public interface ICoordinateProjector
{
    void setCentralBody(ICentralBody centralBody);
    void setMapSize(int mapWidth, int mapHeight);
    void setCacheSize(int size);

    EquirectangularCoordinate project(ICoordinate coordinate) throws MaruException;
}
