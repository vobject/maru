package maru.map.views.swt;

import maru.core.model.ICentralBody;
import maru.core.utils.PathUtil;
import maru.map.MaruMapPlugin;
import maru.map.views.AbstractMapDrawer;
import maru.map.views.MapViewParameters;
import maru.ui.model.UiProject;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;

public class SWTMapDrawer extends AbstractMapDrawer
{
    private GC gc;
    private Image mapImage;
    private Image scaledMapImage;

    private final MapDrawJob mapDrawJob;
    private final DayNightDrawJob daynightDrawJob;
    private final ScenarioDrawJob scenarioDrawJob;

    public SWTMapDrawer(SWTMapView parent)
    {
        super(parent);

        mapDrawJob = new MapDrawJob();
        daynightDrawJob = new DayNightDrawJob();
        scenarioDrawJob = new ScenarioDrawJob();
    }

    @Override
    public void dispose()
    {
        if (scaledMapImage != null) {
            scaledMapImage.dispose();
            scaledMapImage = null;
        }

        if (mapImage != null) {
            mapImage.dispose();
            mapImage = null;
        }

        mapDrawJob.dispose();
        daynightDrawJob.dispose();
        scenarioDrawJob.dispose();
    }

    @Override
    protected void updateMapParameters(UiProject project)
    {
        updateBackground(project.getUnderlyingElement().getCentralBody());
        getParameters().update();

        if (scaledMapImage != null) {
            scaledMapImage.dispose();
        }

        MapViewParameters params = getParameters();
        scaledMapImage = new Image(mapImage.getDevice(),
                                   mapImage.getImageData().scaledTo(params.mapWidth,
                                                                    params.mapHeight));
    }

    @Override
    protected void updateMapSettings(UiProject project)
    {
        getSettings().update();
    }

    private void updateBackground(ICentralBody centralBody)
    {
        if (mapImage != null) {
            mapImage.dispose();
        }

        String path = centralBody.getElementGraphic2D().getPath();
        if (PathUtil.isLocalPath(path)) {
            mapImage = new Image(null, path);
        } else {
            // load from plugin bundle
            mapImage = MaruMapPlugin.getImage(path);
        }
        int mapImageWidth = mapImage.getBounds().width;
        int mapImageHeight = mapImage.getBounds().height;
        getParameters().setImageSize(mapImageWidth, mapImageHeight);
    }

    @Override
    protected void updateContext(Object context)
    {
        gc = (GC) context;

        mapDrawJob.setMapAreaSettings(getParameters());
        mapDrawJob.setMapDrawSettings(getSettings());
        mapDrawJob.setGC(gc);
        mapDrawJob.setProjector(getProjector());
        mapDrawJob.setSelectedElement(getSelectedElement());

        daynightDrawJob.setMapAreaSettings(getParameters());
        daynightDrawJob.setMapDrawSettings(getSettings());
        daynightDrawJob.setGC(gc);
        daynightDrawJob.setProjector(getProjector());
        daynightDrawJob.setSelectedElement(getSelectedElement());

        scenarioDrawJob.setMapAreaSettings(getParameters());
        scenarioDrawJob.setMapDrawSettings(getSettings());
        scenarioDrawJob.setGC(gc);
        scenarioDrawJob.setProjector(getProjector());
        scenarioDrawJob.setSelectedElement(getSelectedElement());
    }

    @Override
    protected void doProjectDrawJobs(UiProject project)
    {
        mapDrawJob.setProject(project);
        mapDrawJob.setMapImage(scaledMapImage);
        mapDrawJob.draw();

        daynightDrawJob.setProject(project);
        daynightDrawJob.draw();

        scenarioDrawJob.setProject(project);
        scenarioDrawJob.draw();
    }

    @Override
    protected void doProjectAnimationJobs(UiProject project)
    {

    }
}
