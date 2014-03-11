package maru.map.views.gl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLContext;

import maru.core.model.ICentralBody;
import maru.core.model.IScenarioProject;
import maru.map.MaruMapPlugin;
import maru.map.jobs.gl.GLProjectAnimationJob;
import maru.map.jobs.gl.GLProjectDrawJob;
import maru.map.jobs.gl.TextureCache;
import maru.map.views.AbstractMapDrawer;
import maru.map.views.gl.jobs.DayNightDrawJob;
import maru.map.views.gl.jobs.LatLonDrawJob;
import maru.map.views.gl.jobs.MapTextureDrawJob;
import maru.map.views.gl.jobs.ScenarioDrawJob;

import org.eclipse.swt.widgets.Display;

import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.opengl.util.texture.Texture;

public class MapGLDrawer extends AbstractMapDrawer implements IGLDrawJobRunner
{
    private static final int DEFAULT_ANIMATION_SPEED = 1000 / 30; // 30 fps

    private GL2 gl;
    private TextRenderer textRenderer;
    private final TextureCache textureCache;

    private final List<GLProjectDrawJob> projectDrawJobs;
    private final List<GLProjectAnimationJob> postAnimationJobs;

    public MapGLDrawer(MapGLView parent, GLContext context)
    {
        super(parent);

        // render with the operating systems default font at 12pt.
        textRenderer = new TextRenderer(new java.awt.Font(getSettings().getFontName(), java.awt.Font.PLAIN, getSettings().getFontSize()), true, false);
        textureCache = new TextureCache(context.getGL().getGL2());

        projectDrawJobs = new ArrayList<>();
        postAnimationJobs = new ArrayList<>();

        addProjectDrawJob(new MapTextureDrawJob());
        addProjectDrawJob(new LatLonDrawJob());
        addProjectDrawJob(new DayNightDrawJob());
        addProjectDrawJob(new ScenarioDrawJob());

        MaruMapPlugin.getDefault().addGLDrawJobRunner(this);
    }

    @Override
    public void fontChanged()
    {
        textRenderer = new TextRenderer(new java.awt.Font(getSettings().getFontName(), java.awt.Font.PLAIN, getSettings().getFontSize()), true, false);
    }

    @Override
    public void addProjectDrawJob(GLProjectDrawJob job)
    {
        if (!projectDrawJobs.contains(job)) {
            projectDrawJobs.add(job);
        }
    }

    @Override
    public void removeProjectDrawJob(GLProjectDrawJob job)
    {
        if (projectDrawJobs.contains(job)) {
            projectDrawJobs.remove(job);
        }
    }

    @Override
    public void addProjectAnimationJob(GLProjectAnimationJob job)
    {
        if (!postAnimationJobs.contains(job)) {
            postAnimationJobs.add(job);
        }
    }

    @Override
    public void removeProjectAnimationJob(GLProjectAnimationJob job)
    {
        if (postAnimationJobs.contains(job)) {
            postAnimationJobs.remove(job);
        }
    }

    @Override
    public void dispose()
    {
        MaruMapPlugin.getDefault().removeGLDrawJobRunner(this);

        for (GLProjectDrawJob job : projectDrawJobs) {
            job.dispose();
        }

        for (GLProjectAnimationJob job : postAnimationJobs) {
            job.dispose();
        }

        projectDrawJobs.clear();
        postAnimationJobs.clear();
        textureCache.dispose();
    }

    @Override
    public void mouseEvent(int btn, int mask, int count, int x, int y)
    {
        // TODO: Select a scenario element from the map or set a marking
    }

    @Override
    protected void updateContext(GLContext context)
    {
        gl = context.getGL().getGL2();

        gl.glClear(GL.GL_COLOR_BUFFER_BIT);

        updateJobs();
    }

    @Override
    protected void updateContext(GLContext context, IScenarioProject project)
    {
        updateContext(context);
    }

    private void updateJobs()
    {
        for (GLProjectDrawJob job : projectDrawJobs) {
            job.setMapParameters(getParameters());
            job.setMapSettings(getSettings());
            job.setGL(gl);
            job.setTextRenderer(textRenderer);
            job.setTextureCache(textureCache);
            job.setProjector(getMapProjector());
            job.setSelectedElement(getSelectedElement());
        }

        for (GLProjectAnimationJob job : postAnimationJobs) {
            job.setMapParameters(getParameters());
            job.setMapSettings(getSettings());
            job.setGL(gl);
            job.setTextRenderer(textRenderer);
            job.setTextureCache(textureCache);
            job.setProjector(getMapProjector());
            job.setSelectedElement(getSelectedElement());
        }
    }

    @Override
    protected void updateMapParameters(IScenarioProject project)
    {
        updateBackground(project.getCentralBody());
        getParameters().update();
    }

    @Override
    protected void updateMapSettings(IScenarioProject project)
    {
        updateAntiAliasing();
        getSettings().update();
    }

    private void updateBackground(ICentralBody centralBody)
    {
        Texture mapTexture = textureCache.load(centralBody.getTexture());
        int mapTextureWidth = mapTexture.getImageWidth();
        int mapTextureHeight = mapTexture.getImageHeight();

        getParameters().setImageSize(mapTextureWidth, mapTextureHeight);
    }

    private void updateAntiAliasing()
    {
        if (getSettings().getAntiAliasing()) {
            gl.glEnable(GL2.GL_LINE_SMOOTH);
            gl.glEnable(GL2.GL_POINT_SMOOTH);
        } else {
            gl.glDisable(GL2.GL_LINE_SMOOTH);
            gl.glDisable(GL2.GL_POINT_SMOOTH);
        }
    }

    @Override
    protected void doProjectDrawJobs(IScenarioProject project)
    {
        for (GLProjectDrawJob job : projectDrawJobs) {
            job.setScenario(project);
            job.draw();
        }
    }

    @Override
    protected void doProjectAnimationJobs(IScenarioProject project)
    {
        Iterator<GLProjectAnimationJob> it = postAnimationJobs.iterator();
        while (it.hasNext())
        {
            GLProjectAnimationJob job = it.next();
            if (!job.isDone()) {
                job.setScenario(project);
                job.draw();
            } else {
                it.remove();
                job.dispose();
            }
        }

        if (!postAnimationJobs.isEmpty())
        {
            Display.getDefault().timerExec(DEFAULT_ANIMATION_SPEED, new Runnable() {
                @Override public void run() {
                    redraw();
                }
            });
        }
    }
}
