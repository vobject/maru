package maru.map.views;

import maru.ui.model.IUiProjectModelListener;
import maru.ui.model.IUiProjectSelectionListener;
import maru.ui.model.UiElement;
import maru.ui.model.UiProject;

import org.eclipse.swt.widgets.Composite;
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
        glWindow.destroy();
        super.dispose();
    }

    @Override
    public void setFocus()
    {
        container.setFocus();
    }

    @Override
    public void projectAdded(UiProject project)
    {

    }

    @Override
    public void projectChanged(UiProject project)
    {

    }

    @Override
    public void projectRemoved(UiProject project)
    {

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
        if (this.glWindow != null) {
            this.glWindow.destroy();
        }
        this.glWindow = window;
    }

    public void redraw()
    {
        if (glWindow != null) {
            glWindow.display();
        }
    }

    protected abstract void addEventListener();
}
