package maru.core.model;

import java.util.List;

import maru.core.model.AbstractCentralBody;
import maru.core.model.AbstractGroundstation;
import maru.core.model.AbstractPropagator;
import maru.core.model.AbstractSpacecraft;
import maru.core.model.ICentralBody;
import maru.core.model.ICoordinate;
import maru.core.model.ISpacecraft;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.orekit.bodies.BodyShape;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.errors.OrekitException;
import org.orekit.frames.Frame;
import org.orekit.time.AbsoluteDate;

public class CoreModelMock
{
    public static class DummyCentralBody extends AbstractCentralBody
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
        public Vector3D getPosition(Frame frame, AbsoluteDate date) throws OrekitException
        {
            return null;
        }

        @Override
        public GeodeticPoint getIntersection(ICoordinate from) throws OrekitException
        {
            return null;
        }

        @Override
        public GeodeticPoint getIntersection(ICoordinate from, ICoordinate to) throws OrekitException
        {
            return null;
        }

        @Override
        public BodyShape getBodyShape()
        {
            return null;
        }
    }

    public static class DummyGroundstation extends AbstractGroundstation
    {
        private static final long serialVersionUID = 1L;

        public DummyGroundstation(GeodeticPoint point, ICentralBody centralBody)
        {
            super("DummyGroundstation", point, 0.0, centralBody);
        }
    }

    public static class DummySatellite extends AbstractSpacecraft
    {
        private static final long serialVersionUID = 1L;

        public DummySatellite()
        {
            super("DummySatellite");
        }

        @Override
        public void centralbodyChanged()
        {

        }
    }

    public static class DummyPropagator extends AbstractPropagator
    {
        private static final long serialVersionUID = 1L;

        @Override
        public String getName()
        {
            return "DummyPropagator";
        }

        @Override
        public ICoordinate getCoordinate(AbsoluteDate date, ISpacecraft element)
        {
            return null;
        }

        @Override
        public List<ICoordinate> getCoordinates(AbsoluteDate start, AbsoluteDate stop, long stepSize, ISpacecraft element)
        {
            return null;
        }
    }
}
