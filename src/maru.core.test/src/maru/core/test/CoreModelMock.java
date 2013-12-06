package maru.core.test;

import java.util.Collection;

import maru.core.model.ICoordinate;
import maru.core.model.IPropagatable;
import maru.core.model.template.CentralBody;
import maru.core.model.template.Groundstation;
import maru.core.model.template.Propagator;
import maru.core.model.template.Spacecraft;
import maru.core.units.Frame;
import maru.core.units.Position;

public class CoreModelMock
{
    public static class DummyCentralBody extends CentralBody
    {
        private static final long serialVersionUID = 1L;

        public DummyCentralBody()
        {
            super("DummyCentralBody");
        }

        @Override
        public double getGM()
        {
            return 0;
        }

        @Override
        public double getEquatorialRadius()
        {
            return 0;
        }

        @Override
        public double getPolarRadius()
        {
            return 0;
        }

        @Override
        public double getMeanRadius()
        {
            return 0;
        }

        @Override
        public double getFlattening()
        {
            return 0;
        }

        @Override
        public Frame getFrame()
        {
            return null;
        }

        @Override
        public Position getPosition(long time, Frame frame)
        {
            return null;
        }
    }

    public static class DummyGroundstation extends Groundstation
    {
        private static final long serialVersionUID = 1L;

        public DummyGroundstation()
        {
            super("DummyGroundstation", null);
        }
    }

    public static class DummySatellite extends Spacecraft
    {
        private static final long serialVersionUID = 1L;

        public DummySatellite()
        {
            super("DummySatellite", null);
        }

        @Override
        public boolean inUmbra(ICoordinate coordinate)
        {
            return false;
        }

        @Override
        public boolean inUmbraOrPenumbra(ICoordinate coordinate)
        {
            return false;
        }
    }

    public static class DummyPropagator extends Propagator
    {
        private static final long serialVersionUID = 1L;

        @Override
        public String getName()
        {
            return "DummyPropagator";
        }

        @Override
        public ICoordinate getCoordinate(IPropagatable element, long time)
        {
            return null;
        }

        @Override
        public Collection<ICoordinate> getCoordinates(IPropagatable element, long start, long stop, long stepSize)
        {
            return null;
        }
    }
}
