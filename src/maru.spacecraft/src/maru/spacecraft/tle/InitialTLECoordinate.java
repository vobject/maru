package maru.spacecraft.tle;

import maru.spacecraft.OrekitCoordinate;

import org.orekit.errors.OrekitException;
import org.orekit.frames.FramesFactory;
import org.orekit.propagation.analytical.tle.TLE;
import org.orekit.propagation.analytical.tle.TLEPropagator;
import org.orekit.utils.PVCoordinates;

public class InitialTLECoordinate extends OrekitCoordinate
{
    private static final long serialVersionUID = 1L;

    // TODO: get rid of the unnecessary 'name' member.
    private final String name;
    private final TLE tle;

    public InitialTLECoordinate(String name, TLE tle) throws OrekitException
    {
        super(coordinatesFromTle(tle), tle.getDate(), FramesFactory.getTEME());

        this.name = name;
        this.tle = tle;
    }

    public String getName()
    {
        return name;
    }

    public TLE getTle()
    {
        return tle;
    }

    private static PVCoordinates coordinatesFromTle(TLE tle) throws OrekitException
    {
        // the TLE class does not provide PVCoordinates by itself.
        // propagate from the initial position to the initial position
        // to get them.

        TLEPropagator propagator = TLEPropagator.selectExtrapolator(tle);
        PVCoordinates coordinates = propagator.getPVCoordinates(tle.getDate());
        return coordinates;
    }
}
