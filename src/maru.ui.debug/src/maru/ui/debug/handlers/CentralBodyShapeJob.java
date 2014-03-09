package maru.ui.debug.handlers;

import javax.media.opengl.GL2;

import maru.core.model.ICentralBody;
import maru.map.jobs.gl.GLProjectDrawJob;
import maru.map.views.MapViewParameters;

public class CentralBodyShapeJob extends GLProjectDrawJob
{
    static final int MAX_SIZE = 32;

    int currentWidth = MAX_SIZE;
    int currentHeight = MAX_SIZE;

    int currentPosX = 0;
    int currentPosY = 0;

    @Override
    public void draw()
    {
        GL2 gl = getGL();
        MapViewParameters area = getMapParameters();

        ICentralBody centralBody = getScenario().getCentralBody();
        double radius_x = centralBody.getEquatorialRadius();
        double radius_y = radius_x - (radius_x * centralBody.getFlattening());

        updateCurrentEllipseSize(radius_x, radius_y);
        updateCurrentEllipsePosition(area);

        pushGlAttribs(gl);
        drawEllipse2(gl);
        popGlAttribs(gl);
    }

    @Override
    public void dispose()
    {

    }

    private void pushGlAttribs(GL2 gl)
    {
        gl.glPushAttrib(GL2.GL_ALL_ATTRIB_BITS);
        gl.glColor3f(1.0f, 1.0f, 1.0f);
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_LINE);
        gl.glEnable(GL2.GL_LINE_SMOOTH);
        gl.glLineWidth(0.75f);
    }

    private void popGlAttribs(GL2 gl)
    {
        gl.glPopAttrib();
    }

    private void drawEllipse2(GL2 gl)
    {
        gl.glBegin(GL2.GL_LINE_LOOP);

        for (double deg = 0.0; deg < 360.0; deg++)
        {
           double rad = Math.toRadians(deg);
           gl.glVertex2d(currentPosX + Math.cos(rad) * currentWidth,
                         currentPosY + Math.sin(rad) * currentHeight);
        }

        gl.glEnd();
    }

    private void updateCurrentEllipseSize(double radius_x, double radius_y)
    {
        double scale = MAX_SIZE / Math.max(radius_x, radius_y);

        currentWidth = (int) (radius_x * scale);
        currentHeight = (int) (radius_y * scale);
    }

    private void updateCurrentEllipsePosition(MapViewParameters area)
    {
        currentPosX = area.clientAreaWidth - MAX_SIZE - 5;
        currentPosY = area.clientAreaHeight - MAX_SIZE - 5;
    }
}
