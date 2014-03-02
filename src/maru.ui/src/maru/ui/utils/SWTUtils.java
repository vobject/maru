package maru.ui.utils;

import maru.core.model.utils.TimeUtils;

import org.eclipse.swt.widgets.DateTime;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.DateComponents;
import org.orekit.time.DateTimeComponents;
import org.orekit.time.TimeComponents;

public final class SWTUtils
{
    public static AbsoluteDate getAbsoluteDate(DateTime date, DateTime time)
    {
        int year = date.getYear();
        int month = date.getMonth() + 1;
        int day = date.getDay();
        int hours = time.getHours();
        int minutes = time.getMinutes();
        int seconds = time.getSeconds();

        return new AbsoluteDate(year, month, day, hours, minutes, seconds, TimeUtils.getTimeScale());
    }

    public static void populateControls(DateTime calendar, DateTime time, AbsoluteDate date)
    {
        DateTimeComponents components = date.getComponents(TimeUtils.getTimeScale());
        DateComponents dateComp = components.getDate();
        TimeComponents timeComp = components.getTime();
        int seconds = (int) Math.round(timeComp.getSecond());

        calendar.setDate(dateComp.getYear(), dateComp.getMonth() - 1, dateComp.getDay());
        time.setTime(timeComp.getHour(), timeComp.getMinute(), seconds);
    }
}
