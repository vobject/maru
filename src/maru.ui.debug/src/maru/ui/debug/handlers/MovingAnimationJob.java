package maru.ui.debug.handlers;

import java.util.Random;

import javax.media.opengl.GL2;

import maru.map.jobs.gl.GLProjectAnimationJob;
import maru.map.views.MapViewParameters;

public class MovingAnimationJob extends GLProjectAnimationJob
{
    /** duration of the animation in milliseconds */
    private long duration;

    private boolean running;
    private boolean done;

    private long startTime;
    private long stopTime;

    private int startPosX;
    private int startPosY;

    private int stopPosX;
    private int stopPosY;

    public void setDuration(long duration)
    {
        this.duration = duration;
    }

//    public void setStartPosition(int x, int y)
//    {
//        this.startPosX = x;
//        this.startPosY = y;
//    }
//
//    public void setStopPosition(int x, int y)
//    {
//        this.stopPosX = x;
//        this.stopPosY = y;
//    }

    @Override
    public void draw()
    {
        GL2 gl = getGL();
        MapViewParameters area = getMapParameters();
        Random rand = new Random();

        if (isDone()) {
            return;
        }

        if (!running()) {
            start();

            startPosX = rand.nextInt(area.clientAreaWidth);
            startPosY = rand.nextInt(area.clientAreaHeight);
            stopPosX = rand.nextInt(area.clientAreaWidth);
            stopPosY = rand.nextInt(area.clientAreaHeight);
        }

        long currentTime = System.currentTimeMillis();
        if (currentTime >= stopTime) {
            stop();
            done();
            return;
        }

        long elapsedTime = currentTime - startTime;
        int valueChangeX = stopPosX - startPosX;
        int valueChangeY = stopPosY - startPosY;

        int currentPosX = easeInCubic(elapsedTime, duration, startPosX, valueChangeX);
        int currentPosY = linearTween(elapsedTime, duration, startPosY, valueChangeY);

//        System.out.println("elapsed= " + elapsedTime + " duration=" + duration + " startPos={" + startPosX + "," + startPosY + "} stopPos={" + stopPosX + "," + stopPosY + "} currentPosX=" + currentPosX + " currentPosY=" + currentPosY);

        gl.glColor3f(1.0f, 1.0f, 1.0f);
        gl.glPointSize(32);

        gl.glBegin(GL2.GL_POINTS);
        gl.glVertex2d(currentPosX, area.clientAreaHeight - currentPosY);
        gl.glEnd();
    }

    @Override
    public void dispose()
    {

    }

    @Override
    public boolean isDone()
    {
        return done;
    }

    private boolean running()
    {
        return running;
    }

    private void start()
    {
        startTime = System.currentTimeMillis();
        stopTime = startTime + duration;
        running = true;
    }

    private void stop()
    {
        running = true;
    }

    private void done()
    {
        done = true;
    }

    private static int linearTween(long currentTime, long duration, int startPos, int changeInValue)
    {
        return (int) (changeInValue * currentTime / duration + startPos);
    }

    private static int easeInCubic(long currentTime, long duration, int startPos, int changeInValue)
    {
        double t = (double) currentTime / duration;
        return (int) (changeInValue * t * t * t + startPos);
    }
}