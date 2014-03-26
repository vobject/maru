package maru.map.views.gl.jobs;

import javax.media.opengl.GL2;

import maru.map.jobs.gl.GLProjectDrawJob;
import maru.map.utils.MapUtils;
import maru.map.views.MapViewParameters;
import maru.map.views.MapViewSettings;
import maru.map.views.gl.GLUtils;

import com.jogamp.opengl.util.awt.TextRenderer;

public class LatLonDrawJob extends GLProjectDrawJob
{
    @Override
    public void draw()
    {
        GL2 gl = getGL();
        TextRenderer text = getTextRenderer();
        MapViewParameters params = getMapParameters();
        MapViewSettings settings = getMapSettings();
        double stepSize = getMapSettings().getLatLonStepSize();

        gl.glPushAttrib(GL2.GL_LINE_BIT);
        gl.glDisable(GL2.GL_LINE_SMOOTH);
        gl.glLineWidth(1.0f);
        gl.glLineStipple(3, (short) 0x8888);
        gl.glEnable(GL2.GL_LINE_STIPPLE);

        for (double lon = -180.0 + stepSize; lon < 180.0; lon += stepSize)
        {
            int x = MapUtils.longitudeToHorizontalPixel(lon, params.mapWidth);

            GLUtils.drawText(text,
                             params.clientAreaWidth,
                             params.clientAreaHeight,
                             params.mapX + x,
                             params.mapY + 1,
                             Integer.toString((int) lon),
                             settings.getOutlineText());

            gl.glColor4f(1.0f, 1.0f, 1.0f, 0.3f);

            gl.glBegin(GL2.GL_LINES);
            gl.glVertex2i(params.mapX + x, params.mapHeight + params.mapY);
            gl.glVertex2i(params.mapX + x, params.mapY);
            gl.glEnd();
        }

        for (double lat = -90.0 + stepSize; lat < 90.0; lat += stepSize)
        {
            int y = MapUtils.latitudeToVerticalPixel(lat, params.mapHeight);

            GLUtils.drawText(text,
                             params.clientAreaWidth,
                             params.clientAreaHeight,
                             params.mapX + 1,
                             params.mapHeight - y + params.mapY,
                             Integer.toString((int) lat),
                             settings.getOutlineText());

            gl.glColor4f(1.0f, 1.0f, 1.0f, 0.3f);

            gl.glBegin(GL2.GL_LINES);
            gl.glVertex2i(params.mapX, params.mapHeight - y + params.mapY);
            gl.glVertex2i(params.mapX + params.mapWidth, params.mapHeight - y + params.mapY);
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