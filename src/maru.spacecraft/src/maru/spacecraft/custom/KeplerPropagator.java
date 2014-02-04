package maru.spacecraft.custom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import maru.core.model.AbstractPropagator;
import maru.core.model.ICoordinate;
import maru.core.model.ISpacecraft;
import maru.core.utils.TimeUtils;
import maru.spacecraft.OrekitCoordinate;

import org.orekit.errors.OrekitException;
import org.orekit.orbits.Orbit;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.analytical.KeplerianPropagator;
import org.orekit.time.AbsoluteDate;

public class KeplerPropagator extends AbstractPropagator
{
    private static final long serialVersionUID = 1L;

    // return the cached position only when these variables did not change
    private InitialCustomCoordinate lastInitialCoordinate;
    private AbsoluteDate lastStart = new AbsoluteDate();
    private AbsoluteDate lastStop = new AbsoluteDate();
    private long lastStepSize = 0;

    // TODO: buffer positions of a few (2-3) different step sizes because
    // it is planned that different components ask for propagated positions
    // using different step sizes: a map might be ok with 60s, but an
    // analysis component might need 1s.
    private final Collection<ICoordinate> coordinates = new ArrayList<>();

    @Override
    public OrekitCoordinate getCoordinate(ISpacecraft element, AbsoluteDate date)
    {
        CustomSatellite satellite = (CustomSatellite) element;
        InitialCustomCoordinate initialCoordinate = satellite.getInitialCoordinate();

        try
        {
            Orbit initialOrbit = initialCoordinate.getOrbit();
            KeplerianPropagator keplerPropagator = new KeplerianPropagator(initialOrbit);

            SpacecraftState currentState = keplerPropagator.propagate(date);

            return new OrekitCoordinate(initialCoordinate.getCentralBody(),
                                        currentState.getPVCoordinates(),
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
                                                  AbsoluteDate start,
                                                  AbsoluteDate stop,
                                                  long stepSize)
    {
        CustomSatellite satellite = (CustomSatellite) element;
        InitialCustomCoordinate initialCoordinate = satellite.getInitialCoordinate();

        if ((lastInitialCoordinate != null) &&
            (initialCoordinate.equals(lastInitialCoordinate)) &&
            ((start == lastStart) || (start.compareTo(lastStart) == 0)) &&
            ((stop == lastStop) || (stop.compareTo(lastStop) == 0)) &&
            (stepSize == lastStepSize))
        {
            // do not bother to recalculate all positions
            return coordinates;
        }
        coordinates.clear();

        try
        {
            Orbit initialOrbit = initialCoordinate.getOrbit();
            KeplerianPropagator keplerPropagator = new KeplerianPropagator(initialOrbit);

            AbsoluteDate extrapDate = start;
            while (extrapDate.compareTo(stop) <= 0)
            {
                SpacecraftState currentState = keplerPropagator.propagate(extrapDate);
                coordinates.add(new OrekitCoordinate(initialCoordinate.getCentralBody(),
                                                     currentState.getPVCoordinates(),
                                                     currentState.getDate(),
                                                     currentState.getFrame()));
                extrapDate = TimeUtils.create(extrapDate, stepSize);
            }

            // all external parameters that the position calculation is based
            // on. if these do not change, the outcome does not change.
            lastInitialCoordinate = initialCoordinate;
            lastStart = start;
            lastStop = stop;
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
        lastStart = new AbsoluteDate();
        lastStop = new AbsoluteDate();
        lastStepSize = 0;
    }
}
