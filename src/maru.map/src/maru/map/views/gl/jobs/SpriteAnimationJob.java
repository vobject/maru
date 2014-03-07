package maru.map.views.gl.jobs;

import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL2;

import maru.map.MaruMapResources;
import maru.map.jobs.gl.GLProjectAnimationJob;
import maru.map.views.MapViewParameters;
import maru.map.views.gl.GLUtils;

import com.jogamp.opengl.util.texture.Texture;

/**
 * Experimental animated sprite draw job class.
 */
public class SpriteAnimationJob extends GLProjectAnimationJob
{
    /** duration of the animation in milliseconds */
    private long duration;

    /** duration of the each frame in milliseconds */
    private long durationPerFrame;

    private boolean running;
    private boolean done;

    private long startTime;
    private long stopTime;

    private int posX;
    private int posY;

    private int width;
    private int height;

    private final List<MaruMapResources> frames;

    public SpriteAnimationJob()
    {
        frames = new ArrayList<>();
    }

    public void setDuration(long duration)
    {
        this.duration = duration;
    }

    public void setPosition(int x, int y)
    {
        this.posX = x;
        this.posY = y;
    }

    public void setPosition(int x, int y, int width, int height)
    {
        setPosition(x, y);

        this.width = width;
        this.height = height;
    }

    public void addFrame(MaruMapResources frameRes)
    {
        frames.add(frameRes);

        durationPerFrame = duration / frames.size();
    }

    public void addFrameMap(MaruMapResources frameMapRes, int spriteWidth, int spriteHeight)
    {
        // TODO: parse sprite map
    }

    @Override
    public void draw()
    {
        GL2 gl = getGL();
        MapViewParameters area = getMapParameters();

        if (isDone()) {
            return;
        }

        if (!running()) {
            start();
        }

        long currentTime = System.currentTimeMillis();
        if (currentTime >= stopTime) {
            stop();
            done();
            return;
        }

//        System.out.println("start=" + startTime + " current=" + currentTime + " stop=" + stopTime + " elapsed=" + (currentTime - startTime) + " todo=" + (stopTime - currentTime));

        int currentFrameIndex = (int) ((currentTime - startTime) / durationPerFrame);
        int realCurrentFrameIndex = Math.min(currentFrameIndex, frames.size() - 1);
        MaruMapResources currentFrameRes = frames.get(realCurrentFrameIndex);
        Texture currentFrame = getTextureCache().get(currentFrameRes);

        if ((width == 0) && (height == 0))
        {
            // use the default image size when no other was specified
            width = currentFrame.getImageWidth();
            height = currentFrame.getImageHeight();
        }

        int scaledWidth = area.getScaledSize(width);
        int scaledHeight = area.getScaledSize(height);
        int scaledWidthHalf = scaledWidth / 2;
        int scaledHeightHalf = scaledHeight / 2;

        gl.glColor3f(1.0f, 1.0f, 1.0f);
        GLUtils.drawTexture(gl, currentFrame, posX - scaledWidthHalf, area.clientAreaHeight - posY - scaledHeightHalf, scaledWidth, scaledHeight);
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
}