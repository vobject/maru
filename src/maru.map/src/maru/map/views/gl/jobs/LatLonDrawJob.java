package maru.map.views.gl.jobs;

import javax.media.opengl.GL2;

import maru.map.jobs.gl.GLProjectDrawJob;
import maru.map.utils.MapUtils;
import maru.map.views.MapViewParameters;
import maru.map.views.gl.GLUtils;

import com.jogamp.opengl.util.awt.TextRenderer;

public class LatLonDrawJob extends GLProjectDrawJob
{
    @Override
    public void draw()
    {
        GL2 gl = getGL();
        TextRenderer text = getTextRenderer();
        MapViewParameters area = getMapParameters();
        double stepSize = getScenarioSettings().getLatLonStepSize();

        gl.glPushAttrib(GL2.GL_LINE_BIT);
        gl.glDisable(GL2.GL_LINE_SMOOTH);
        gl.glLineWidth(1.0f);
        gl.glLineStipple(3, (short) 0x8888);
        gl.glEnable(GL2.GL_LINE_STIPPLE);

        for (double lon = -180.0 + stepSize; lon < 180.0; lon += stepSize)
        {
            int x = MapUtils.longitudeToHorizontalPixel(lon, area.mapWidth);

            GLUtils.drawText(text,
                             area.clientAreaWidth,
                             area.clientAreaHeight,
                             area.mapX + x,
                             area.mapY + 1,
                             Double.toString(lon),
                             true);

            gl.glColor4f(1.0f, 1.0f, 1.0f, 0.3f);

            gl.glBegin(GL2.GL_LINES);
            gl.glVertex2i(area.mapX + x, area.mapHeight + area.mapY);
            gl.glVertex2i(area.mapX + x, area.mapY);
            gl.glEnd();
        }

        for (double lat = -90.0 + stepSize; lat < 90.0; lat += stepSize)
        {
            int y = MapUtils.latitudeToVerticalPixel(lat, area.mapHeight);

            GLUtils.drawText(text,
                             area.clientAreaWidth,
                             area.clientAreaHeight,
                             area.mapX + 1,
                             area.mapHeight - y + area.mapY,
                             Double.toString(lat),
                             true);

            gl.glColor4f(1.0f, 1.0f, 1.0f, 0.3f);

            gl.glBegin(GL2.GL_LINES);
            gl.glVertex2i(area.mapX, area.mapHeight - y + area.mapY);
            gl.glVertex2i(area.mapX + area.mapWidth, area.mapHeight - y + area.mapY);
            gl.glEnd();
        }

        gl.glPopAttrib();
    }

    @Override
    public void dispose()
    {
     // this job does not own any resources
    }
}