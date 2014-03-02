package maru.spacecraft.model.custom;

import java.util.ArrayList;
import java.util.List;

import maru.core.model.AbstractPropagator;
import maru.core.model.ICoordinate;
import maru.core.model.ISpacecraft;
import maru.core.model.utils.TimeUtils;
import maru.spacecraft.model.OrekitCoordinate;

import org.orekit.errors.OrekitException;
import org.orekit.orbits.Orbit;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.analytical.KeplerianPropagator;
import org.orekit.time.AbsoluteDate;

public class KeplerPropagator extends AbstractPropagator
{
    private static final long serialVersionUID = 1L;

    @Override
    public OrekitCoordinate getCoordinate(AbsoluteDate date, ISpacecraft element) throws OrekitException
    {
        CustomSatellite satellite = (CustomSatellite) element;
        InitialCustomCoordinate initialCoordinate = satellite.getInitialCoordinate();

        Orbit initialOrbit = initialCoordinate.getOrbit();
        KeplerianPropagator keplerPropagator = new KeplerianPropagator(initialOrbit);

        SpacecraftState currentState = keplerPropagator.propagate(date);

        return new OrekitCoordinate(initialCoordinate.getCentralBody(),
                                    currentState.getPVCoordinates(),
                                    currentState.getDate(),
                                    currentState.getFrame());
    }

    @Override
    public List<ICoordinate> getCoordinates(AbsoluteDate start, AbsoluteDate stop,
                                            long stepSize, ISpacecraft element) throws OrekitException
    {
        CustomSatellite satellite = (CustomSatellite) element;
        InitialCustomCoordinate initialCoordinate = satellite.getInitialCoordinate();

        List<ICoordinate> cache = getCache().getCoordinates(start, stop, stepSize, initialCoordinate);
        if (cache != null) {
            return cache;
        }

        Orbit initialOrbit = initialCoordinate.getOrbit();
        KeplerianPropagator keplerPropagator = new KeplerianPropagator(initialOrbit);

        List<ICoordinate> coordinates = new ArrayList<>();
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

        getCache().setCoordinates(start, stop, stepSize, initialCoordinate, coordinates);
        return coordinates;
    }

    @Override
    public String getName()
    {
        return "KeplerPropagator";
    }
}
