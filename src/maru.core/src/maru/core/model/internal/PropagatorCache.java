package maru.core.model.internal;

import java.util.ArrayList;
import java.util.List;

import maru.core.model.ICoordinate;
import maru.core.model.IPropagatorCache;

import org.orekit.time.AbsoluteDate;

public class PropagatorCache implements IPropagatorCache
{
    private static final long serialVersionUID = 1L;

    // return the cached position only when these variables do not change
    private transient ICoordinate initialCoordinate;
    private transient AbsoluteDate start;
    private transient AbsoluteDate stop;
    private transient long stepSize;

    // TODO: buffer positions of a few (2-3) different step sizes because
    // it is planned that different components ask for propagated positions
    // using different step sizes: a map might be ok with 60s, but an
    // analysis component might need 1s.
    private transient List<ICoordinate> cachedCoordinates;

    public PropagatorCache()
    {
        initializeMembers();
    }

    @Override
    public List<ICoordinate> getCoordinates(AbsoluteDate start, AbsoluteDate stop,
                                            long stepSize, ICoordinate initialCoordinate)
    {
        if (cachedCoordinates == null)
        {
            // This object has just been deserialized if coordinates is null.
            // Reinitialize its members but return null to tell the calling
            // instance that we do not have cached coordinates because we do not
            // (de)serialize them. Serialization of cached coordinates leads
            // to huge traffic increase when working with data model listeners
            // over the network.
            initializeMembers();
            return null;
        }

        // these are all external parameters that the coordinate propagation is
        // based on. if these do not change, the result does not change.
        if ((this.initialCoordinate != null) &&
            (this.initialCoordinate.equals(initialCoordinate)) &&
            ((this.start == start) || (this.start.compareTo(start) == 0)) &&
            ((this.stop  == stop)  || (this.stop.compareTo(stop)   == 0)) &&
            (this.stepSize == stepSize))
        {
            // we have cached coordinates for the exact parameters provided.
            return cachedCoordinates;
        }
        else
        {
            // the propagation parameters have changed. our cached coordinates
            // are invalid. wait for the real propagator to calculate the new
            // coordinates and populate the cache using setCachedCoordinates().
            cachedCoordinates.clear();
            return null;
        }
    }

    @Override
    public void setCoordinates(AbsoluteDate start, AbsoluteDate stop,
                               long stepSize, ICoordinate initialCoordinate,
                               List<ICoordinate> coordinates)
    {
        this.initialCoordinate = initialCoordinate;
        this.start = start;
        this.stop = stop;
        this.stepSize = stepSize;
        this.cachedCoordinates = coordinates;
    }

    @Override
    public void clearCoordinates()
    {
        initializeMembers();
    }

    private void initializeMembers()
    {
        cachedCoordinates = new ArrayList<>();
        initialCoordinate = null;
        start = new AbsoluteDate();
        stop = new AbsoluteDate();
        stepSize = 0;
    }
}
