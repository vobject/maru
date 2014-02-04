package maru.core.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public final class FormatUtils
{
    private static final DecimalFormatSymbols formatSymbols;

    private static final String DEFAULT_DOUBLE_FORMAT = "###0.000000";

    private static final DecimalFormat defaultDoubleFormat;

    static {
        formatSymbols = new DecimalFormatSymbols(Locale.US);
        formatSymbols.setDecimalSeparator('.');

        defaultDoubleFormat = new DecimalFormat(DEFAULT_DOUBLE_FORMAT, formatSymbols);
    }

    public static String format(double d)
    {
        return defaultDoubleFormat.format(d);
    }

    public static String formatNoDecimalPoint(double d)
    {
        return Integer.toString((int) Math.round(d));
    }
}
