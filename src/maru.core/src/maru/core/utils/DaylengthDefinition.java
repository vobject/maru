package maru.core.utils;

/**
 * The values are used with together with an algorithm to find out how long
 * a day is using an algorithm from "A model comparison for daylength as a
 * function of latitude and day of year".
 * <p>
 * All values are in degree.
 */
public enum DaylengthDefinition
{
    /** sunrise/-set when the center of the sun is even with the horizon */
    SUN_CENTER_EVEN_WITH_HORIZON(0.0),

    /** sunrise/-set when the top of the sun is even with the horizon */
    SUN_TOP_EVEN_WITH_HORIZON(0.26667),

    /** sunrise/-set when the top of the sun is apparently even with the horizon */
    SUN_TOP_APPERANTLY_EVEN_WITH_HORIZON(0.8333),

    /** daylength civil twilight */
    WITH_CIVIL_TWILIGHT(6.0),

    /** daylength nautical twilight */
    WITH_NAUTICAL_TWILIGHT(12.0),

    /** daylength astronomical twilight */
    WITH_ASTRONOMICAL_TWILIGHT(18.0);

    private final double definition;

    DaylengthDefinition(double definition)
    {
        this.definition = definition;
    }

    public double getValue()
    {
        return this.definition;
    }

    public static DaylengthDefinition toDaylengthDefinition(double definition)
    {
        for (DaylengthDefinition def : DaylengthDefinition.values())
        {
            // comparing against double types is OK here, since all of them
            // are hard-coded and not derived from calculations
            if (def.getValue() == definition) {
                return def;
            }
        }
        throw new IllegalArgumentException();
    }

    public static DaylengthDefinition toDaylengthDefinition(String definition)
    {
        return toDaylengthDefinition(Double.parseDouble(definition));
    }
}