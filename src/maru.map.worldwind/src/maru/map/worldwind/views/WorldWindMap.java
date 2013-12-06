package maru.map.worldwind.views;

import gov.nasa.worldwind.Model;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.globes.Earth;
import gov.nasa.worldwind.globes.EarthFlat;
import maru.ui.MaruUIPlugin;
import maru.ui.model.IUiProjectModelListener;
import maru.ui.model.IUiProjectSelectionListener;
import maru.ui.model.UiElement;
import maru.ui.model.UiProject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public abstract class WorldWindMap extends ViewPart
                                   implements IUiProjectModelListener,
                                              IUiProjectSelectionListener
{
    private final boolean is3DGlobe;
    protected WorldWindowGLCanvas world;

    public WorldWindMap(boolean is3DGlobe)
    {
        this.is3DGlobe = is3DGlobe;

        MaruUIPlugin.getDefault().getUiModel().addUiProjectModelListener(this);
        MaruUIPlugin.getDefault().getUiModel().addUiProjectSelectionListener(this);
    }

    @Override
    public void createPartControl(Composite parent)
    {
        initWorldWindLayerModel();

        Composite mapComposite = new Composite(parent, SWT.EMBEDDED);
        mapComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

        java.awt.Frame worldFrame = SWT_AWT.new_Frame(mapComposite);
        java.awt.Panel panel = new java.awt.Panel(new java.awt.BorderLayout());

        worldFrame.add(panel);
        panel.add(world, java.awt.BorderLayout.CENTER);

        // max parent widget
        parent.setLayoutData(new GridData(GridData.FILL_BOTH));
    }

    @Override
    public void setFocus()
    {

    }

    @Override
    public void dispose()
    {
        MaruUIPlugin.getDefault().getUiModel().removeUiProjectSelectionListener(this);
        MaruUIPlugin.getDefault().getUiModel().removeUiProjectModelListener(this);
        super.dispose();
    }

    @Override
    public void activeProjectChanged(UiProject project, UiElement element)
    {

    }

    @Override
    public void activeElementChanged(UiProject project, UiElement element)
    {

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

    private void initWorldWindLayerModel()
    {
        Model model = (Model) WorldWind.createConfigurationComponent(AVKey.MODEL_CLASS_NAME);
        model.setShowWireframeExterior(false);
        model.setShowWireframeInterior(false);
        model.setShowTessellationBoundingVolumes(false);

        if (is3DGlobe) {
            model.setGlobe(new Earth());
        } else {
            model.setGlobe(new EarthFlat());
        }

        world = new WorldWindowGLCanvas();
        world.setModel(model);
    }
}
