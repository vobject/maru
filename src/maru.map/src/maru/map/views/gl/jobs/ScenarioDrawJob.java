package maru.map.views.gl.jobs;

import javax.media.opengl.GL2;

import maru.centralbody.model.projection.ICoordinateProjector;
import maru.core.model.IGroundstation;
import maru.core.model.IScenarioProject;
import maru.core.model.ISpacecraft;
import maru.core.model.IVisibleElement;
import maru.map.jobs.gl.GLProjectDrawJob;
import maru.map.jobs.gl.TextureCache;
import maru.map.settings.scenario.ScenarioSettings;
import maru.map.views.MapViewParameters;
import maru.map.views.MapViewSettings;
import maru.map.views.gl.jobs.helper.DrawGroundstationHelper;
import maru.map.views.gl.jobs.helper.DrawSpacecraftHelper;

import com.jogamp.opengl.util.awt.TextRenderer;

public class ScenarioDrawJob extends GLProjectDrawJob
{
    private final DrawGroundstationHelper gsHelper;
    private final DrawSpacecraftHelper scHelper;

    public ScenarioDrawJob()
    {
        this.gsHelper = new DrawGroundstationHelper();
        this.scHelper = new DrawSpacecraftHelper();
    }

    @Override
    public void draw()
    {
        GL2 gl = getGL();
        TextRenderer textRenderer = getTextRenderer();
        TextureCache textureCache = getTextureCache();
        MapViewParameters mapParams = getMapParameters();
        MapViewSettings mapSettings = getMapSettings();
        ICoordinateProjector projector = getProjector();
        IVisibleElement selectedElement = getSelectedElement();
        ScenarioSettings settings = getScenarioSettings();

        IScenarioProject scenario = getScenario();
        projector.setCentralBody(scenario.getCentralBody());
        projector.setMapSize(mapParams.mapWidth, mapParams.mapHeight);

        gsHelper.setGL(gl);
        gsHelper.setTextRenderer(textRenderer);
        gsHelper.setTextureCache(textureCache);
        gsHelper.setMapViewParameters(mapParams);
        gsHelper.setMapViewSettings(mapSettings);
        gsHelper.setProjector(projector);
        gsHelper.setSelectedElement(selectedElement);
        gsHelper.setScenarioSettings(settings);

        scHelper.setGL(gl);
        scHelper.setTextRenderer(textRenderer);
        scHelper.setTextureCache(textureCache);
        scHelper.setMapViewParameters(mapParams);
        scHelper.setMapViewSettings(mapSettings);
        scHelper.setProjector(projector);
        scHelper.setSelectedElement(selectedElement);
        scHelper.setScenarioSettings(settings);

        for (IGroundstation element : scenario.getGroundstations()) {
            gsHelper.draw(element, settings.getGroundstation(element));
        }

        for (ISpacecraft element : scenario.getSpacecrafts()) {
            scHelper.draw(element, settings.getSpacecraft(element));
        }
    }

    @Override
    public void dispose()
    {
        // this job does not own any resources
    }
}