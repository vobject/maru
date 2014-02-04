package maru.map.views.swt;

import maru.core.utils.DaylengthUtils;
import maru.core.utils.DaylengthDefinition;
import maru.map.jobs.swt.SWTProjectDrawJob;
import maru.map.utils.MapUtils;
import maru.map.views.DayLength;
import maru.map.views.MapViewParameters;
import maru.map.views.MapViewSettings;

import org.eclipse.swt.graphics.GC;
import org.orekit.time.AbsoluteDate;

public class DayNightDrawJob extends SWTProjectDrawJob
{
    @Override
    public void draw()
    {
        MapViewParameters area = getParameters();
        MapViewSettings drawing = getSettings();

        if (!drawing.getShowNight()) {
            return;
        }

        AbsoluteDate currentTime = getProject().getCurrentTime();
        int currentMapX = timeToHorizontalPixel(currentTime);
        DayLength[] currentDayTimes = getDayTimes(currentTime, area.mapHeight, drawing.getNightStepSize(), drawing.getDaylengthDefinition());
        drawDayNightTimes(currentMapX, currentDayTimes);
    }

    @Override
    public void dispose()
    {
        // this job does not own any resources
    }

    private int timeToHorizontalPixel(AbsoluteDate date)
    {
        MapViewParameters area = getParameters();

        double minutesInDay = DaylengthUtils.getMinutesInDay(date);
        double pixelsPerMinute = (double) area.mapWidth / DaylengthUtils.MINUTES_IN_A_DAY;

        double posX;
        if (minutesInDay <= DaylengthUtils.MINUTES_IN_HALF_A_DAY) {
            posX = (area.mapWidth / 2) + (pixelsPerMinute * minutesInDay);
        } else {
            posX = pixelsPerMinute * (minutesInDay - DaylengthUtils.MINUTES_IN_HALF_A_DAY);
        }
        return area.mapWidth - (int) Math.round(posX);
    }

    private static DayLength[] getDayTimes(AbsoluteDate date, int verticalPixels, int vertivalStepSize, final DaylengthDefinition definition)
    {
        int dayTimesCount = verticalPixels / vertivalStepSize;
        DayLength[] dayTimes = new DayLength[dayTimesCount];
        double dayOfYear = DaylengthUtils.getDayOfYear(date);

        for (int i = 0; i < dayTimesCount; i++)
        {
            int verticalPixel = i * vertivalStepSize;
            double latDeg = MapUtils.verticalPixelToLatitude(verticalPixel, verticalPixels);
            double dayLen = DaylengthUtils.getLengthOfDay(dayOfYear, latDeg, definition);
            dayTimes[i] = new DayLength(verticalPixel, dayLen);
        }

        return dayTimes;
    }

    private void drawDayNightTimes(int x, DayLength[] dayTimes)
    {
        GC gc = getGC();
        MapViewParameters params = getParameters();
        MapViewSettings settings = getSettings();

        gc.setAlpha(64);
        gc.setLineWidth(settings.getNightStepSize());

        for (DayLength dayTime : dayTimes)
        {
            if (dayTime.hours == 0.0) {
                continue;
            }

            if (dayTime.hours >= 24.0) {
                drawLineOnMap(0, dayTime.verticalPixel, params.mapWidth, dayTime.verticalPixel);
                continue;
            }

            int xLenHalf = (int) ((params.mapWidth / 24.0) * dayTime.hours / 2.0);
            int leftPx = x - xLenHalf;
            int rightPx = x + xLenHalf;

            drawLineOnMap(x - xLenHalf, dayTime.verticalPixel, x + xLenHalf, dayTime.verticalPixel);

            if (leftPx < 0)
            {
                drawLineOnMap(params.mapWidth - Math.abs(leftPx), dayTime.verticalPixel, params.mapWidth, dayTime.verticalPixel);
            }

            if (rightPx > params.mapWidth)
            {
                drawLineOnMap(0, dayTime.verticalPixel, rightPx - params.mapWidth, dayTime.verticalPixel);
            }
        }

        gc.setAlpha(255);
    }

    private void drawLineOnMap(int x1, int y1, int x2, int y2)
    {
        MapViewParameters params = getParameters();
        getGC().drawLine(x1 + params.mapX, y1 + params.mapY, x2 + params.mapX, y2 + params.mapY);
    }
}