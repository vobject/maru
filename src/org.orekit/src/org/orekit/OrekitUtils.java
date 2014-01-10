package org.orekit;

import java.util.Calendar;
import java.util.Date;

import maru.core.model.ITimepoint;
import maru.core.units.Frame;
import maru.core.units.Position;
import maru.core.units.Velocity;
import maru.core.utils.TimeUtil;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.orekit.errors.OrekitException;
import org.orekit.frames.FramesFactory;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScale;
import org.orekit.time.TimeScalesFactory;

public final class OrekitUtils
{
    private static TimeScale utc;

    static
    {
        try {
            // may throw when orekit-data did not get loaded.
            utc = TimeScalesFactory.getUTC();
        } catch (OrekitException e) {
            e.printStackTrace();
        }
    }

    public static AbsoluteDate toAbsoluteDate(ITimepoint timepoint)
    {
        return toAbsoluteDate(timepoint.getTime());
    }

    public static AbsoluteDate toAbsoluteDate(long time)
    {
        return toAbsoluteDate(new Date(time * 1000));
    }

    private static AbsoluteDate toAbsoluteDate(Date time)
    {
        Calendar calendar = TimeUtil.getCalendar();

        calendar.setTime(time);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);
        int milliseconds = calendar.get(Calendar.MILLISECOND);

        double secondsPresision = seconds + (milliseconds / 1000.0);
        return new AbsoluteDate(year, month, day, hours, minutes, secondsPresision, utc);
    }

    public static ITimepoint toTimepoint(AbsoluteDate date)
    {
        return TimeUtil.fromSeconds(date.toDate(utc).getTime() / 1000);
    }

    public static long toSeconds(AbsoluteDate date)
    {
        return toTimepoint(date).getTime();
    }

    public static org.orekit.frames.Frame toOrekitFrame(Frame frame)
    {
        try
        {
            switch (frame)
            {
                case ITRF2005:
                    return FramesFactory.getITRF2005();
                case EME2000:
                    return FramesFactory.getEME2000();
                case TEME:
                    return FramesFactory.getTEME();
                default:
                    throw new RuntimeException("Invalid frame type.");
            }
        }
        catch (OrekitException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static Frame toFrame(org.orekit.frames.Frame frame)
    {
        try
        {
            if (frame == FramesFactory.getITRF2005()) {
                return Frame.ITRF2005;
            } else if (frame == FramesFactory.getEME2000()) {
                return Frame.EME2000;
            } else if (frame == FramesFactory.getTEME()) {
                return Frame.TEME;
            } else {
                throw new RuntimeException("Invalid frame type.");
            }
        }
        catch (OrekitException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static Position toPosition(Vector3D vec)
    {
        return new Position(vec.getX(), vec.getY(), vec.getZ());
    }

    public static Vector3D toOrekitPosition(Position pos)
    {
        return new Vector3D(pos.getX(), pos.getY(), pos.getZ());
    }

    public static Velocity toVelocity(Vector3D vec)
    {
        return new Velocity(vec.getX(), vec.getY(), vec.getZ());
    }

    public static Vector3D toOrekitVelocity(Velocity vel)
    {
        return new Vector3D(vel.getX(), vel.getY(), vel.getZ());
    }
}
