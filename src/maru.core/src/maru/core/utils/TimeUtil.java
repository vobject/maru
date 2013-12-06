package maru.core.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import maru.core.internal.model.Timepoint;
import maru.core.model.ITimepoint;

public final class TimeUtil
{
    public static final String ISO8601_PLACEHOLDER = "0000-00-00 00:00:00";

    private static final TimeZone utc;
    private static final Calendar calendar;
    private static final SimpleDateFormat iso8601DateFormat;

    static {
        // this is the one and only time zone we will use
        utc = TimeZone.getTimeZone("UTC");

        // the calendar that we use will always be in UTC
        calendar = Calendar.getInstance(utc);

        // emulate ISO ISO8601 with SimpleDateFormat
        final String ISO8601_DATETIME_FORMAT = "yyyy-MM-dd' 'HH:mm:ss";
        iso8601DateFormat = new SimpleDateFormat(ISO8601_DATETIME_FORMAT);
        iso8601DateFormat.setTimeZone(utc);
        iso8601DateFormat.setLenient(false);
    }

    public static Calendar getCalendar()
    {
        return calendar;
    }

    public static Calendar getCalendar(long seconds)
    {
        calendar.setTimeInMillis(seconds * 1000);
        return calendar;
    }

    public static String asISO8601(ITimepoint time)
    {
        return asISO8601(time.getTime());
    }

    public static String asISO8601(long time)
    {
        return asISO8601(new Date(time * 1000));
    }

    public static String asISO8601(Date time)
    {
        return iso8601DateFormat.format(time);
    }

    public static ITimepoint fromTimepoint(ITimepoint timepoint)
    {
        return new Timepoint(timepoint.getTime());
    }

    public static ITimepoint fromSeconds(long seconds)
    {
        return new Timepoint(seconds);
    }

    public static ITimepoint fromString(String timeString) throws ParseException
    {
        Date date = iso8601DateFormat.parse(timeString);
        return new Timepoint(date.getTime() / 1000);
    }
}
