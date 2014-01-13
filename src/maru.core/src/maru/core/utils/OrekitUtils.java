package maru.core.utils;

import java.util.Calendar;
import java.util.Date;

import maru.core.model.ITimepoint;

import org.orekit.errors.OrekitException;
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
}
