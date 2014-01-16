package maru.core.utils;

import java.util.Date;

import maru.core.internal.model.Timepoint;
import maru.core.model.ITimepoint;

import org.eclipse.swt.widgets.DateTime;
import org.orekit.errors.OrekitException;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.DateComponents;
import org.orekit.time.DateTimeComponents;
import org.orekit.time.TimeComponents;
import org.orekit.time.TimeScale;
import org.orekit.time.TimeScalesFactory;

public final class TimeUtils
{
    private static TimeScale utc;

    public static final String ISO8601_PLACEHOLDER = "0000-00-00 00:00:00";

    static
    {
        try {
            // this is the one and only time zone we will use.
            // may throw when orekit-data was not loaded.
            utc = TimeScalesFactory.getUTC();
        } catch (OrekitException e) {
            e.printStackTrace();
        }
    }

    public static DateTimeComponents getComponents(AbsoluteDate date)
    {
        return date.getComponents(utc);
    }

    public static DateComponents getDateComponents(AbsoluteDate date)
    {
        return date.getComponents(utc).getDate();
    }

    public static TimeComponents getTimeComponents(AbsoluteDate date)
    {
        return date.getComponents(utc).getTime();
    }

    public static AbsoluteDate now()
    {
        return new AbsoluteDate(new Date(), utc);
    }

    public static AbsoluteDate create(AbsoluteDate date, double offset)
    {
        return new AbsoluteDate(date, offset, utc);
    }

    public static AbsoluteDate copy(AbsoluteDate date)
    {
        return new AbsoluteDate(date, 0, utc);
    }

    public static String asISO8601(ITimepoint time)
    {
        return asISO8601(time.getTime());
    }

    public static String asISO8601(AbsoluteDate date)
    {
        String original = date.toString(utc);
        String noSeparator = original.replace('T', ' ');
        String noSubSecond = noSeparator.substring(0, noSeparator.lastIndexOf('.'));
        return noSubSecond;
    }

    public static ITimepoint fromTimepoint(ITimepoint timepoint)
    {
        return new Timepoint(timepoint.getTime());
    }

    public static ITimepoint fromDate(AbsoluteDate date)
    {
        return new Timepoint(date);
    }

    public static ITimepoint fromString(String dateString)
    {
        String withSeparator = dateString.replace(' ', 'T');
        return new Timepoint(new AbsoluteDate(withSeparator, utc));
    }

    public static AbsoluteDate getAbsoluteDate(DateTime date, DateTime time)
    {
        int year = date.getYear();
        int month = date.getMonth() + 1;
        int day = date.getDay();
        int hours = time.getHours();
        int minutes = time.getMinutes();
        int seconds = time.getSeconds();

        return new AbsoluteDate(year, month, day, hours, minutes, seconds, utc);
    }

    public static void populateControls(DateTime calendar, DateTime time, AbsoluteDate date)
    {
        DateTimeComponents components = date.getComponents(utc);
        DateComponents dateComp = components.getDate();
        TimeComponents timeComp = components.getTime();
        int seconds = (int) Math.round(timeComp.getSecond());

        calendar.setDate(dateComp.getYear(), dateComp.getMonth() - 1, dateComp.getDay());
        time.setTime(timeComp.getHour(), timeComp.getMinute(), seconds);
    }
}
