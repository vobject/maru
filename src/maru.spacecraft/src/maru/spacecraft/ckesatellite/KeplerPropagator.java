package maru.spacecraft.ckesatellite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import maru.core.model.ICoordinate;
import maru.core.model.ISpacecraft;
import maru.core.model.template.AbstractPropagator;
import maru.core.utils.OrekitUtils;
import maru.spacecraft.OrekitCoordinate;

import org.orekit.errors.OrekitException;
import org.orekit.orbits.KeplerianOrbit;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.analytical.KeplerianPropagator;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScalesFactory;

public class KeplerPropagator extends AbstractPropagator
{
    private static final long serialVersionUID = 1L;

    // return the cached position only when these variables did not change
    private InitialKeplerCoordinate lastInitialCoordinate;
    private long lastStartTime;
    private long lastStopTime;
    private long lastStepSize;

    // TODO: buffer positions of a few (2-3) different step sizes because
    // it is planned that different components ask for propagated positions
    // using different step sizes: a map might be ok with 60s, but an
    // analysis component might need 1s.
    private final Collection<ICoordinate> coordinates = new ArrayList<>();

    @Override
    public OrekitCoordinate getCoordinate(ISpacecraft element, long time)
    {
        KeplerSatellite satellite = (KeplerSatellite) element;
        InitialKeplerCoordinate initialCoordinate = satellite.getInitialCoordinate();

        try
        {
            KeplerianOrbit initialOrbit = initialCoordinate.getInitialOrbit();
            KeplerianPropagator keplerPropagator = new KeplerianPropagator(initialOrbit);

            AbsoluteDate currentDate = OrekitUtils.toAbsoluteDate(time);
            SpacecraftState currentState = keplerPropagator.propagate(currentDate);

            return new OrekitCoordinate(currentState.getPVCoordinates(),
                                        currentState.getDate(),
                                        currentState.getFrame());
        }
        catch (OrekitException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Collection<ICoordinate> getCoordinates(ISpacecraft element,
                                                  long start, long stop,
                                                  long stepSize)
    {
        KeplerSatellite satellite = (KeplerSatellite) element;
        InitialKeplerCoordinate initialCoordinate = satellite.getInitialCoordinate();

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

        try
        {
            KeplerianOrbit initialOrbit = initialCoordinate.getInitialOrbit();
            KeplerianPropagator keplerPropagator = new KeplerianPropagator(initialOrbit);

            AbsoluteDate endDate = OrekitUtils.toAbsoluteDate(stop);
            AbsoluteDate extrapDate = OrekitUtils.toAbsoluteDate(start);

            while (extrapDate.compareTo(endDate) <= 0)
            {
                SpacecraftState currentState = keplerPropagator.propagate(extrapDate);
                coordinates.add(new OrekitCoordinate(currentState.getPVCoordinates(),
                                                     currentState.getDate(),
                                                     currentState.getFrame()));

                extrapDate = new AbsoluteDate(extrapDate, stepSize, TimeScalesFactory.getUTC());
            }

            // all external parameters that the position calculation is based
            // on. if these do not change, the outcome does not change.
            lastInitialCoordinate = initialCoordinate;
            lastStartTime = start;
            lastStopTime = stop;
            lastStepSize = stepSize;

            return coordinates;
        }
        catch (OrekitException e)
        {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public String getName()
    {
        return "KeplerPropagator";
    }

    @Override
    public void clearCoordinateCache()
    {
        coordinates.clear();
        lastInitialCoordinate = null;
        lastStartTime = 0;
        lastStopTime = 0;
        lastStepSize = 0;
    }
}
