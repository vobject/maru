package maru.map.views.gl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLContext;

import maru.core.model.ICentralBody;
import maru.map.MaruMapPlugin;
import maru.map.jobs.gl.GLProjectAnimationJob;
import maru.map.jobs.gl.GLProjectDrawJob;
import maru.map.jobs.gl.TextureCache;
import maru.map.views.AbstractMapDrawer;
import maru.map.views.gl.jobs.DayNightDrawJob;
import maru.map.views.gl.jobs.LatLonDrawJob;
import maru.map.views.gl.jobs.MapDrawJob;
import maru.map.views.gl.jobs.ScenarioDrawJob;
import maru.ui.model.UiProject;

import org.eclipse.swt.widgets.Display;

import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.opengl.util.texture.Texture;

public class GLMapDrawer extends AbstractMapDrawer implements IGLDrawJobRunner
{
    private static final int DEFAULT_ANIMATION_SPEED = 1000 / 30; // 30 fps

    private GL2 gl;
    private final TextRenderer text;
    private final TextureCache textureCache;

    private final List<GLProjectDrawJob> projectDrawJobs;
    private final List<GLProjectAnimationJob> postAnimationJobs;

    public GLMapDrawer(GLMapView parent)
    {
        super(parent);

        // render with the operating systems default font at 12pt.
        text = new TextRenderer(new java.awt.Font(Display.getDefault().getSystemFont().getFontData()[0].getName(), java.awt.Font.PLAIN, 12), true, false);
        textureCache = new TextureCache(parent.getGlContext().getGL().getGL2());

        projectDrawJobs = new ArrayList<>();
        postAnimationJobs = new ArrayList<>();

        addProjectDrawJob(new MapDrawJob());
        addProjectDrawJob(new LatLonDrawJob());
        addProjectDrawJob(new DayNightDrawJob());
        addProjectDrawJob(new ScenarioDrawJob());

        MaruMapPlugin.getDefault().addGLDrawJobRunner(this);
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
        for (GLProjectDrawJob job : projectDrawJobs) {
            job.dispose();
        }

        for (GLProjectAnimationJob job : postAnimationJobs) {
            job.dispose();
        }

        projectDrawJobs.clear();
        postAnimationJobs.clear();

        MaruMapPlugin.getDefault().removeGLDrawJobRunner(this);
        textureCache.dispose();
    }

    @Override
    public void mouseEvent(int btn, int mask, int count, int x, int y)
    {

    }

    @Override
    protected void updateMapParameters(UiProject project)
    {
        updateBackground(project.getUnderlyingElement().getCentralBody());
        getParameters().update();
    }

    @Override
    protected void updateMapSettings(UiProject project)
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
    protected void updateContext(Object context)
    {
        GLContext glContext = (GLContext) context;
        gl = glContext.getGL().getGL2();

        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glColor3f(1.0f, 1.0f, 1.0f);

        for (GLProjectDrawJob job : projectDrawJobs) {
            job.setMapAreaSettings(getParameters());
            job.setMapDrawSettings(getSettings());
            job.setGL(gl);
            job.setTextRenderer(text);
            job.setTextureCache(textureCache);
            job.setProjector(getProjector());
            job.setSelectedElement(getSelectedElement());
        }

        for (GLProjectAnimationJob job : postAnimationJobs) {
            job.setMapAreaSettings(getParameters());
            job.setMapDrawSettings(getSettings());
            job.setGL(gl);
            job.setTextRenderer(text);
            job.setTextureCache(textureCache);
        }
    }

    @Override
    protected void doProjectDrawJobs(UiProject project)
    {
        for (GLProjectDrawJob job : projectDrawJobs) {
            job.setProject(project);
            job.draw();
        }
    }

    @Override
    protected void doProjectAnimationJobs(UiProject project)
    {
        Iterator<GLProjectAnimationJob> it = postAnimationJobs.iterator();
        while (it.hasNext())
        {
            GLProjectAnimationJob job = it.next();
            if (!job.isDone()) {
                job.setProject(project);
                job.draw();
            } else {
                it.remove();
                job.dispose();
            }
        }

        if (!postAnimationJobs.isEmpty())
        {
            Display.getDefault().timerExec(DEFAULT_ANIMATION_SPEED, new Runnable() {
                @Override public void run()
                {
                    redraw();
                }
            });
        }
    }
}
