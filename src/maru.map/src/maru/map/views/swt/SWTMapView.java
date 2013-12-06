package maru.map.views.swt;

import maru.centralbody.projection.EquirectangularProjector;
import maru.map.views.AbstractMapView;
import maru.ui.MaruUIPlugin;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class SWTMapView extends AbstractMapView
{
    @Override
    public void createPartControl(final Composite parent)
    {
        setContainer(new Canvas(parent, SWT.DOUBLE_BUFFERED));
        getContainer().setLayout(new FillLayout());
        getContainer().setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));

        setMapProjector(new EquirectangularProjector());
        getMapProjector().setCacheSize(DEFAULT_PROJECTOR_CACHE_SIZE);

        setMapDrawer(new SWTMapDrawer(this));
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
    }
}
