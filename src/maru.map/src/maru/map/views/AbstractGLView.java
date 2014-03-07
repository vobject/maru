package maru.map.views;

import maru.map.MaruMapPlugin;
import maru.ui.model.IUiProjectModelListener;
import maru.ui.model.IUiProjectSelectionListener;
import maru.ui.model.UiElement;
import maru.ui.model.UiProject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.part.ViewPart;

import com.jogamp.newt.opengl.GLWindow;

public abstract class AbstractGLView extends ViewPart
                                     implements IUiProjectModelListener,
                                                IUiProjectSelectionListener
{
    private Composite container;
    private GLWindow glWindow;

    @Override
    public void dispose()
    {
        getWindow().destroy();

        super.dispose();
    }

    @Override
    public void setFocus()
    {
        getContainer().setFocus();
    }

    @Override
    public void projectAdded(UiProject project)
    {
        MaruMapPlugin.getDefault().getUiProjectsSettings().projectAdded(project);
    }

    @Override
    public void projectChanged(UiProject project)
    {

    }

    @Override
    public void projectRemoved(UiProject project)
    {
        MaruMapPlugin.getDefault().getUiProjectsSettings().projectRemoved(project);
    }

    @Override
    public void activeProjectChanged(UiProject project, UiElement element)
    {

    }

    @Override
    public void activeElementChanged(UiProject project, UiElement element)
    {

    }

    public Composite getContainer()
    {
        return container;
    }

    public GLWindow getWindow()
    {
        return glWindow;
    }

    public void setContainer(Composite container)
    {
        this.container = container;
    }

    public void setWindow(GLWindow window)
    {
        this.glWindow = window;
    }

    public void redraw()
    {
//        Display.getDefault().syncExec(new Runnable() {
//            @Override
//            public void run() {
//                getContainer().redraw();
//            }
//        });
        getWindow().display();
    }

    protected void addResizeListener()
    {
        getContainer().addListener(SWT.Resize, new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                Rectangle rect = container.getClientArea();
                getWindow().setSize(rect.width, rect.height);
            }
        });
    }

    protected abstract void addEventListener();
}
