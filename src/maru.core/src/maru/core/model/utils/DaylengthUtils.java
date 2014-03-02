package maru.core.model.utils;

import org.orekit.time.AbsoluteDate;

public final class DaylengthUtils
{
    public static final long SECONDS_IN_A_DAY = 24 * 60 * 60;
    public static final long MINUTES_IN_A_DAY = 24 * 60;
    public static final long MINUTES_IN_HALF_A_DAY = MINUTES_IN_A_DAY / 2;

    /**
     * Get the day of the year.
     */
    public static double getDayOfYear(AbsoluteDate date)
    {
        double dayOfYear = TimeUtils.getDateComponents(date).getDayOfYear();
        double secondsInDay = TimeUtils.getTimeComponents(date).getSecondsInDay();
        return dayOfYear + (secondsInDay / SECONDS_IN_A_DAY);
    }

    /**
     * Get the minutes passed on the day of a given date.
     */
    public static double getMinutesInDay(AbsoluteDate date)
    {
        return getSecondsInDay(date) / 60;
    }

    /**
     * Get the seconds passed on the day of a given date.
     */
    public static double getSecondsInDay(AbsoluteDate date)
    {
        return TimeUtils.getTimeComponents(date).getSecondsInDay();
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
