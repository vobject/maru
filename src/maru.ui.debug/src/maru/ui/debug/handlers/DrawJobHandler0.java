package maru.ui.debug.handlers;

import java.util.Random;

import javax.media.opengl.GL2;

import maru.map.MaruMapPlugin;
import maru.map.jobs.gl.GLProjectDrawJob;
import maru.map.views.MapViewParameters;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

public class DrawJobHandler0 extends AbstractHandler
{
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        System.out.println("DrawJobHandler0");

        MaruMapPlugin.getDefault().registerProjectDrawJob(new GLProjectDrawJob()
        {
            @Override
            public void draw()
            {
                GL2 gl = getGL();
                MapViewParameters area = getMapParameters();
                Random rand = new Random();

                byte r = (byte) rand.nextInt(255);
                byte g = (byte) rand.nextInt(255);
                byte b = (byte) rand.nextInt(255);
                double size = area.getScaledSize(32);
                int x = 40;//rand.nextInt(area.mapWidth);
                int y = 40;//rand.nextInt(area.mapHeight);

                gl.glColor3ub(r, g, b);
                gl.glPointSize((float) size);

                gl.glBegin(GL2.GL_POINTS);
                gl.glVertex2d(area.mapX + x, area.clientAreaHeight - area.mapY - y);
                gl.glEnd();
            }

            @Override
            public void dispose()
            {

            }
        });

        MaruMapPlugin.getDefault().redraw();
        return null;
    }
}
