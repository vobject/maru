package maru.map.views.gl.jobs;

import javax.media.opengl.GL2;

import maru.core.model.ICentralBody;
import maru.core.model.VisibleElementColor;
import maru.map.jobs.gl.GLProjectDrawJob;
import maru.map.settings.scenario.ScenarioSettings;
import maru.map.views.MapViewParameters;
import maru.map.views.gl.GLUtils;

import com.jogamp.opengl.util.texture.Texture;

public class MapTextureDrawJob extends GLProjectDrawJob
{
    @Override
    public void draw()
    {
        GL2 gl = getGL();
        MapViewParameters params = getMapParameters();
        ScenarioSettings settings = getScenarioSettings();

        Texture mapTexture = getMapTexture();
        int x = params.mapX;
        int y = params.mapY;
        int width = params.mapWidth;
        int height = params.mapHeight;

        VisibleElementColor color = !settings.getShowNightMode() ?
                                    new VisibleElementColor(255,  255,  255) :
                                    new VisibleElementColor(255, 0, 0);

        GLUtils.drawTexture(gl, mapTexture, color, x, y, width, height, false);
    }

    @Override
    public void dispose()
    {
        // this job does not own any resources
    }

    private Texture getMapTexture()
    {
        ICentralBody centralBody = getScenario().getCentralBody();
        String mapImage = centralBody.getTexture().getPath();
        return getTextureCache().get(mapImage);
    }
}
