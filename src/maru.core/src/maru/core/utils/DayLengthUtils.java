package maru.core.utils;

import java.util.Calendar;

import maru.core.units.DaylengthDefinition;

public final class DayLengthUtils
{
    public static final long SECONDS_IN_A_DAY = 24 * 60 * 60;
    public static final long MINUTES_IN_A_DAY = 24 * 60;
    public static final long MINUTES_IN_HALF_A_DAY = MINUTES_IN_A_DAY / 2;

    /**
     * Get the day of the year.
     *
     * @param time Seconds since start of the epoch.
     * @return The day of the year as a fraction.
     */
    public static double getDayOfYear(long time)
    {
        Calendar calendar = TimeUtils.getCalendar(time);

        // get the day of the year from the calendar
        double dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);

        // get the fraction of the day by looking at the seconds passed
        double secondsOfDay = getSecondsOfDay(time);

        return dayOfYear + (secondsOfDay / SECONDS_IN_A_DAY);
    }

    /**
     * Get the minutes passed on the day of the given time.
     *
     * @param time Seconds since start of the epoch.
     * @return The number of minutes passed on the day of the given time.
     */
    public static long getMinutesOfDay(long time)
    {
        return getSecondsOfDay(time) / 60;
    }

    /**
     * Get the seconds passed on the day of the given time.
     *
     * @param time Seconds since start of the epoch.
     * @return The number of seconds passed on the day of the given time.
     */
    public static long getSecondsOfDay(long time)
    {
        // Is this OK for stuff like leap-seconds?
        return (time % SECONDS_IN_A_DAY);
    }

    /**
     * Get the length of a day with default daylength definition.
     *
     * @param dayOfYear The fractional day of the year.
     * @param latitude The latitude for which to calculate the daylength.
     * @return The Length of a day as a fraction of hours.
     */
    public static double getLengthOfDay(double dayOfYear, double latitude)
    {
        return getLengthOfDay(dayOfYear, latitude, DaylengthDefinition.SUN_TOP_APPERANTLY_EVEN_WITH_HORIZON);
    }

    /**
     * Get the length of a day.
     *
     * @param dayOfYear The fractional day of the year.
     * @param latitude The latitude in degrees for which to calculate the daylength.
     * @param definition The daylength definition to apply to the calculation.
     * @return The Length of a day as a fraction of hours.
     */
    public static double getLengthOfDay(double dayOfYear, double latitude, DaylengthDefinition definition)
    {
        // BCM model from "A model comparison for daylength as a function
        // of latitude and day of year" (1994)

        // the daylength definition is "sunrise/sunset when the top of the sun
        // is apparently even with horizon"

        // the revolution angle θ for the day of the year
        double revolutionAngle = 0.2163108 + 2.0 * Math.atan(0.9671396 * Math.tan(0.00860 * (dayOfYear - 186.0)));

        // the sun's declination angle Φ, or the angular distance at solar noon
        // between the sun and the equator, from the earth orbit revolution angle
        double distanceAngle = Math.asin(0.39795 * Math.cos(revolutionAngle));

        double intermediate = (-Math.sin(definition.getValue() * Math.PI / 180.0) + Math.sin(Math.toRadians(latitude)) * Math.sin(distanceAngle)) / (Math.cos(Math.toRadians(latitude)) * Math.cos(distanceAngle));

        double dayLength;
        if (intermediate < -1.0)
        {
            // the day is continuous dark when the intermediate
            // return value is less than -1.0.
            dayLength = 0.0;
        }
        else if (intermediate > 1.0)
        {
            // the day is continuous light when the intermediate
            // return value is greater than 1.0.
            // set the daylength to 24 hours plus its daily leap fraction.
            dayLength = 24.0 + (24.0 * 0.25 / 365.0);
        }
        else
        {
            // finish the calculation of the daylength
            dayLength = 24.0 - (24.0 / Math.PI) * Math.acos(intermediate);
        }
        return dayLength;
    }
}
