package maru.groundstation.earth;

import java.util.ArrayList;
import java.util.Collection;

import maru.core.model.ICoordinate;
import maru.core.model.IPropagatable;
import maru.core.model.template.Propagator;

public class GeodeticGroundstationPropagator extends Propagator
{
    private static final long serialVersionUID = 1L;

    // return the cached position only when these variables did not change
    private GeodeticCoordinate lastInitialCoordinate;
    private long lastStartTime;
    private long lastStopTime;
    private long lastStepSize;

    private final Collection<ICoordinate> coordinates = new ArrayList<>();

    @Override
    public GeodeticCoordinate getCoordinate(IPropagatable element, long time)
    {
        GeodeticGroundstation groundstation = (GeodeticGroundstation) element;
        GeodeticCoordinate initialCoordinate = (GeodeticCoordinate) groundstation.getInitialCoordinate();
        GeodeticCoordinate newCoordinate = new GeodeticCoordinate(initialCoordinate);
        newCoordinate.setTime(time);
        return newCoordinate;
    }

    @Override
    public Collection<ICoordinate> getCoordinates(IPropagatable element,
                                                  long start, long stop,
                                                  long stepSize)
    {
        GeodeticGroundstation groundstation = (GeodeticGroundstation) element;
        GeodeticCoordinate initialCoordinate = (GeodeticCoordinate) groundstation.getInitialCoordinate();

        if ((lastInitialCoordinate != null)
                && (initialCoordinate.equals(lastInitialCoordinate))
                && (start == lastStartTime)
                && (stop == lastStopTime)
                && (stepSize == lastStepSize))
        {
            // do not bother to recalculate all positions
            return coordinates;
        }
        coordinates.clear();

        long extrapTime = start;
        while (extrapTime < stop)
        {
            GeodeticCoordinate newCoordinate = new GeodeticCoordinate(initialCoordinate);
            newCoordinate.setTime(extrapTime);

            coordinates.add(new GeodeticCoordinate(newCoordinate));
            extrapTime = extrapTime + stepSize;
        }

        // all external parameters that the position calculation is based
        // on. if these do not change, the outcome does not change.
        lastInitialCoordinate = initialCoordinate;
        lastStartTime = start;
        lastStopTime = stop;
        lastStepSize = stepSize;

        return coordinates;
    }

    @Override
    public String getName()
    {
        return "GeodeticGroundstationPropagator";
    }
}
