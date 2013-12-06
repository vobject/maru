package maru.map.views.gl;

import javax.media.opengl.GL;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLDrawableFactory;

import maru.centralbody.projection.EquirectangularProjector;
import maru.map.views.AbstractMapView;
import maru.ui.MaruUIPlugin;
import maru.ui.model.UiModel;
import maru.ui.model.UiProject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class GLMapView extends AbstractMapView
{
    private GLContext glContext;

    @Override
    public void createPartControl(Composite parent)
    {
        GLData glData = new GLData();
        glData.doubleBuffer = true;

        setContainer(new GLCanvas(parent, SWT.NO_BACKGROUND, glData));
        getContainer().setLayout(new FillLayout());
        getContainer().setCurrent();

        setGlContext(GLDrawableFactory.getFactory().createExternalGLContext());
        getGlContext().makeCurrent();
        initGLContext();

        setMapProjector(new EquirectangularProjector());
        getMapProjector().setCacheSize(DEFAULT_PROJECTOR_CACHE_SIZE);

        setMapDrawer(new GLMapDrawer(this));
        initMapDrawer();

        addMapPaintListener();
        addMapResizeListener();
        addMouseListener();

        MaruUIPlugin.getDefault().getUiModel().addUiProjectModelListener(this);
        MaruUIPlugin.getDefault().getUiModel().addUiProjectSelectionListener(this);
    }

    @Override
    public void dispose()
    {
        MaruUIPlugin.getDefault().getUiModel().removeUiProjectSelectionListener(this);
        MaruUIPlugin.getDefault().getUiModel().removeUiProjectModelListener(this);

        super.dispose();

        glContext.release();
        glContext.destroy();
    }

    @Override
    public GLCanvas getContainer()
    {
        return (GLCanvas) super.getContainer();
    }

    public GLContext getGlContext()
    {
        return glContext;
    }

    public void setGlContext(GLContext glContext)
    {
        this.glContext = glContext;
    }

    private void initGLContext()
    {
        GL gl = glContext.getGL();

        gl.glDisable(GL.GL_LIGHTING);
        gl.glDisable(GL.GL_DEPTH_TEST);
        gl.glDisable(GL.GL_MULTISAMPLE);
        gl.glEnable(GL.GL_BLEND);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glHint(GL.GL_LINE_SMOOTH_HINT, GL.GL_FASTEST);
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    }

    @Override
    protected void addMapPaintListener()
    {
        getContainer().addPaintListener(new PaintListener() {
            @Override
            public void paintControl(PaintEvent e)
            {
                UiProject currentProject = UiModel.getDefault().getCurrentUiProject();
                if (currentProject != null) {
                    getMapDrawer().draw(glContext, currentProject);
                }

                getContainer().swapBuffers();
            }
        });
    }

    @Override
    protected void addMapResizeListener()
    {
        getContainer().addListener(SWT.Resize, new Listener() {
            @Override
            public void handleEvent(Event event)
            {
                Rectangle rect = getContainer().getClientArea();

                GLUtils.setupGL(glContext.getGL(), rect.width, rect.height);
                getMapDrawer().getParameters().setClientArea(rect.width, rect.height);
            }
        });
    }
}
