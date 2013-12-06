package maru.map.views.gl.jobs;

import javax.media.opengl.GL;

import maru.core.model.ICentralBody;
import maru.map.jobs.gl.GLProjectDrawJob;
import maru.map.views.MapViewParameters;
import maru.map.views.gl.GLUtils;

import com.sun.opengl.util.texture.Texture;

public class MapDrawJob extends GLProjectDrawJob
{
    @Override
    public void draw()
    {
        GL gl = getGL();
        MapViewParameters area = getParameters();

        Texture mapTexture = getMapTexture();
        int x = area.mapX;
        int y = area.mapY;
        int width = area.mapWidth;
        int height = area.mapHeight;

        GLUtils.drawTexture(gl, mapTexture, x, y, width, height);
    }

    @Override
    public void dispose()
    {
        // this job does not own any resources
    }

    private Texture getMapTexture()
    {
        ICentralBody centralBody = getProject().getUnderlyingElement().getCentralBody();
        String mapImage = centralBody.getElementGraphic2D().getPath();
        return getTextureCache().get(mapImage);
    }
}
