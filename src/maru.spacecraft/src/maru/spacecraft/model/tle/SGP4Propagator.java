package maru.spacecraft.model.tle;

import java.util.ArrayList;
import java.util.List;

import maru.core.model.AbstractPropagator;
import maru.core.model.ICoordinate;
import maru.core.model.ISpacecraft;
import maru.core.model.utils.TimeUtils;
import maru.spacecraft.model.OrekitCoordinate;

import org.orekit.errors.OrekitException;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.analytical.tle.TLE;
import org.orekit.propagation.analytical.tle.TLEPropagator;
import org.orekit.time.AbsoluteDate;

public class SGP4Propagator extends AbstractPropagator
{
    private static final long serialVersionUID = 1L;

    private String propagatorName = "SGP4/SDP4";

    @Override
    public OrekitCoordinate getCoordinate(AbsoluteDate date, ISpacecraft element) throws OrekitException
    {
        TLESatellite satellite = (TLESatellite) element;
        InitialTLECoordinate initialCoordinate = satellite.getInitialCoordinate();

        TLE tle = initialCoordinate.getTle();
        TLEPropagator tlePropagator = TLEPropagator.selectExtrapolator(tle);
        propagatorName = tlePropagator.getClass().getSimpleName();

        SpacecraftState currentState = tlePropagator.propagate(date);

        return new OrekitCoordinate(initialCoordinate.getCentralBody(),
                                    currentState.getPVCoordinates(),
                                    currentState.getDate(),
                                    currentState.getFrame());
    }

    @Override
    public List<ICoordinate> getCoordinates(AbsoluteDate start, AbsoluteDate stop,
                                            long stepSize, ISpacecraft element) throws OrekitException
    {
        TLESatellite satellite = (TLESatellite) element;
        InitialTLECoordinate initialCoordinate = satellite.getInitialCoordinate();

        List<ICoordinate> cache = getCache().getCoordinates(start, stop, stepSize, initialCoordinate);
        if (cache != null) {
            return cache;
        }

        // select the right propagator. might be SGP4 or SDP4, based on the TLE.
        TLE tle = initialCoordinate.getTle();
        TLEPropagator tlePropagator = TLEPropagator.selectExtrapolator(tle);
        propagatorName = tlePropagator.getClass().getSimpleName();

        List<ICoordinate> coordinates = new ArrayList<>();
        AbsoluteDate extrapDate = start;
        while (extrapDate.compareTo(stop) <= 0)
        {
            SpacecraftState currentState = tlePropagator.propagate(extrapDate);
            coordinates.add(new OrekitCoordinate(initialCoordinate.getCentralBody(),
                                                 currentState.getPVCoordinates(),
                                                 currentState.getDate(),
                                                 currentState.getFrame()));
            extrapDate = TimeUtils.create(extrapDate, stepSize);
        }

        getCache().setCoordinates(start, stop, stepSize, initialCoordinate, coordinates);
        return coordinates;
    }

    @Override
    public String getName()
    {
        return propagatorName;
    }
}
