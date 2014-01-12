package maru.map.views;

import maru.ui.model.IUiProjectModelListener;
import maru.ui.model.IUiProjectSelectionListener;
import maru.ui.model.UiElement;
import maru.ui.model.UiModel;
import maru.ui.model.UiProject;
import maru.ui.model.UiPropagatable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.part.ViewPart;

public abstract class AbstractMapView extends ViewPart
                                      implements IUiProjectModelListener,
                                                 IUiProjectSelectionListener
{
    // the map will be drawn into this composite. must not be null.
    private Composite mapContainer;

    // the object that draws into the container. must not be null.
    private IMapDrawer mapDrawer;

    @Override
    public void setFocus()
    {
        mapContainer.setFocus();
    }

    @Override
    public void projectAdded(UiProject project)
    {

    }

    @Override
    public void projectChanged(UiProject project)
    {
        redraw();
    }

    @Override
    public void projectRemoved(UiProject project)
    {
        if (project != UiModel.getDefault().getCurrentUiProject()) {
            return;
        }

        // get rid of references to the project that will be deleted
        mapDrawer.setSelectedElement(null);
        redraw();
    }

    @Override
    public void activeProjectChanged(UiProject project, UiElement element)
    {
        mapDrawer.setSelectedElement(null);
        mapDrawer.getParameters().setSettingsChanged(true);
        mapDrawer.getSettings().setSettingsChanged(true);
        redraw();
    }

    @Override
    public void activeElementChanged(UiProject project, UiElement element)
    {
        if (element instanceof UiPropagatable) {
            mapDrawer.setSelectedElement((UiPropagatable) element);
        } else {
            mapDrawer.setSelectedElement(null);
        }
        redraw();
    }

    public Composite getContainer()
    {
        return mapContainer;
    }

    public void setContainer(Composite mapContainer)
    {
        if (this.mapContainer != null) {
            this.mapContainer.dispose();
        }
        this.mapContainer = mapContainer;
    }

    public IMapDrawer getMapDrawer()
    {
        return mapDrawer;
    }

    public void setMapDrawer(IMapDrawer mapDrawer)
    {
        if (this.mapDrawer != null) {
            this.mapDrawer.dispose();
        }
        this.mapDrawer = mapDrawer;
    }

    public void redraw()
    {
        Display.getDefault().syncExec(new Runnable() {
            @Override
            public void run() {
                mapContainer.redraw();
            }
        });
    }

    protected void addMapPaintListener()
    {
        mapContainer.addPaintListener(new PaintListener() {
            @Override
            public void paintControl(PaintEvent e)
            {
                UiProject currentProject = UiModel.getDefault().getCurrentUiProject();
                if (currentProject != null) {
                    mapDrawer.draw(e.gc, currentProject);
                } else {
                    mapDrawer.draw(e.gc);
                }
            }
        });
    }

    protected void addMapResizeListener()
    {
        mapContainer.addListener(SWT.Resize, new Listener() {
            @Override
            public void handleEvent(Event event)
            {
                Rectangle rect = mapContainer.getClientArea();
                MapViewParameters params = mapDrawer.getParameters();
                params.setClientArea(rect.width, rect.height);
            }
        });
    }

    protected void addMouseListener()
    {
        mapContainer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDown(MouseEvent e)
            {
                mapDrawer.mouseEvent(e.button, e.stateMask, e.count, e.x, e.y);
                redraw();
            }
        });
    }
}
