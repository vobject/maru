package maru.map.views;

/**
 * Simple data structure to hold the measurement of the length of a day
 * that belong to a specific vertical pixel on a map.
 */
public class DayLength
{
    /** the vertical pixel for which the time was calculated */
    public int verticalPixel;

    /** length of the day in hours */
    public double hours;

    public DayLength(int verticalPixel, double hours)
    {
        this.verticalPixel = verticalPixel;
        this.hours = hours;
    }
}
