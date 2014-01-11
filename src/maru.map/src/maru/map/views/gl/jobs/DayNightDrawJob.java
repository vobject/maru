package maru.map.views.gl.jobs;

import javax.media.opengl.GL2;

import maru.core.units.DaylengthDefinition;
import maru.core.utils.DayLengthUtil;
import maru.map.jobs.gl.GLProjectDrawJob;
import maru.map.utils.MapUtils;
import maru.map.views.DayLength;
import maru.map.views.MapViewParameters;
import maru.map.views.MapViewSettings;

public class DayNightDrawJob extends GLProjectDrawJob
{
    @Override
    public void draw()
    {
        MapViewParameters area = getParameters();
        MapViewSettings drawing = getSettings();

        if (!drawing.getShowNight()) {
            return;
        }

        long currentTime = getProject().getCurrentTime();
        int currentMapX = timeToHorizontalPixel(currentTime);
        DayLength[] currentDayTimes = getDayTimes(currentTime, area.mapHeight, drawing.getNightStepSize(), drawing.getDaylengthDefinition());
        drawDayNightTimes(currentMapX, currentDayTimes);
    }

    @Override
    public void dispose()
    {
        // this job does not own any resources
    }

    private int timeToHorizontalPixel(long time)
    {
        MapViewParameters area = getParameters();

        long minutesOfDay = DayLengthUtil.getMinutesOfDay(time);
        double pixelsPerMinute = (double) area.mapWidth / DayLengthUtil.MINUTES_IN_A_DAY;

        double posX;
        if (minutesOfDay <= DayLengthUtil.MINUTES_IN_HALF_A_DAY) {
            posX = (area.mapWidth / 2) + (pixelsPerMinute * minutesOfDay);
        } else {
            posX = pixelsPerMinute * (minutesOfDay - DayLengthUtil.MINUTES_IN_HALF_A_DAY);
        }
        return area.mapWidth - (int) Math.round(posX);
    }

    private static DayLength[] getDayTimes(long time, int verticalPixels, int verticalStepSize, DaylengthDefinition definition)
    {
        int dayTimesCount = verticalPixels / verticalStepSize;
        DayLength[] dayTimes = new DayLength[dayTimesCount];
        double dayOfYear = DayLengthUtil.getDayOfYear(time);

        for (int i = 0; i < dayTimesCount; i++)
        {
            int verticalPixel = i * verticalStepSize;
            double latDeg = MapUtils.verticalPixelToLatitude(verticalPixel, verticalPixels);
            double dayLen = DayLengthUtil.getLengthOfDay(dayOfYear, latDeg, definition);
            dayTimes[i] = new DayLength(verticalPixel, dayLen);
        }

        return dayTimes;
    }

    private void drawDayNightTimes(int x, DayLength[] dayTimes)
    {
        GL2 gl = getGL();
        MapViewParameters params = getParameters();
        MapViewSettings settings = getSettings();

        gl.glColor4f(0.0f, 0.0f, 0.0f, 0.25f);
        gl.glLineWidth(settings.getNightStepSize());

        for (DayLength dayTime : dayTimes)
        {
            if (dayTime.hours == 0.0) {
                continue;
            }

            if (dayTime.hours >= 24.0) {
                gl.glBegin(GL2.GL_LINES);
                gl.glVertex2i(params.mapX, params.mapHeight - dayTime.verticalPixel + params.mapY);
                gl.glVertex2i(params.mapX + params.mapWidth, params.mapHeight - dayTime.verticalPixel + params.mapY);
                gl.glEnd();
                continue;
            }

            int xLenHalf = (int) ((params.mapWidth / 24.0) * dayTime.hours / 2.0);
            int leftPx = x - xLenHalf;
            int rightPx = x + xLenHalf;

            gl.glBegin(GL2.GL_LINES);
            gl.glVertex2i(params.mapX + x - xLenHalf, params.mapHeight - dayTime.verticalPixel + params.mapY);
            gl.glVertex2i(params.mapX + x + xLenHalf, params.mapHeight - dayTime.verticalPixel + params.mapY);
            gl.glEnd();

            if (leftPx < 0)
            {
                gl.glBegin(GL2.GL_LINES);
                gl.glVertex2i(params.mapX + params.mapWidth - Math.abs(leftPx), params.mapHeight - dayTime.verticalPixel + params.mapY);
                gl.glVertex2i(params.mapX + params.mapWidth, params.mapHeight - dayTime.verticalPixel + params.mapY);
                gl.glEnd();
            }

            if (rightPx > params.mapWidth)
            {
                gl.glBegin(GL2.GL_LINES);
                gl.glVertex2i(params.mapX, params.mapHeight - dayTime.verticalPixel + params.mapY);
                gl.glVertex2i(params.mapX + rightPx - params.mapWidth, params.mapHeight - dayTime.verticalPixel + params.mapY);
                gl.glEnd();
            }
        }
    }
}