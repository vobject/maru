package maru.map.views.gl.jobs.helper;

import java.util.List;

import javax.media.opengl.GL2;

import maru.centralbody.model.projection.FlatMapPosition;
import maru.centralbody.model.projection.ICoordinateProjector;
import maru.core.model.IGroundstation;
import maru.core.model.ISpacecraft;
import maru.core.model.IVisibleElement;
import maru.core.model.VisibleElementColor;
import maru.core.model.resource.IMaruResource;
import maru.map.jobs.gl.TextureCache;
import maru.map.settings.groundstation.GroundstationSettings;
import maru.map.settings.scenario.ScenarioSettings;
import maru.map.views.MapViewParameters;
import maru.map.views.MapViewSettings;
import maru.map.views.gl.GLUtils;
import maru.map.views.gl.jobs.MapPrimitives;

import org.orekit.bodies.GeodeticPoint;
import org.orekit.errors.OrekitException;

import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.opengl.util.texture.Texture;

public final class DrawGroundstationHelper
{
    private GL2 gl;
    private TextRenderer textRenderer;
    private TextureCache textureCache;
    private MapViewParameters mapParams;
    private MapViewSettings mapSettings;
    private ICoordinateProjector projector;
    private IVisibleElement selectedElement;
    private ScenarioSettings scenarioSettings;

    public void setGL(GL2 gl)
    {
        this.gl = gl;
    }

    public void setTextRenderer(TextRenderer textRenderer)
    {
        this.textRenderer = textRenderer;
    }

    public void setTextureCache(TextureCache textureCache)
    {
        this.textureCache = textureCache;
    }

    public void setMapViewParameters(MapViewParameters mapParams)
    {
        this.mapParams = mapParams;
    }

    public void setMapViewSettings(MapViewSettings mapSettings)
    {
        this.mapSettings = mapSettings;
    }

    public void setProjector(ICoordinateProjector projector)
    {
        this.projector = projector;
    }

    public void setSelectedElement(IVisibleElement selectedElement)
    {
        this.selectedElement = selectedElement;
    }

    public void setScenarioSettings(ScenarioSettings scenarioSettings)
    {
        this.scenarioSettings = scenarioSettings;
    }

    public void draw(IGroundstation element, GroundstationSettings settings)
    {
        try
        {
            GeodeticPoint position = element.getGeodeticPosition();
            FlatMapPosition mapPos = getMapPosition(position);
            VisibleElementColor color = element.getElementColor();

            drawVisibilityCircle(element, settings);
            drawIcon(element, mapPos, color, settings);
            drawName(element, mapPos, color, settings);
        }
        catch (OrekitException e)
        {
            e.printStackTrace();
        }
    }

    private void drawIcon(IGroundstation element, FlatMapPosition mapPos, VisibleElementColor color, GroundstationSettings settings)
    {
        if (!settings.getShowElementIcon()) {
            return;
        }

        int iconSize = getIconSize(element, settings);
        IMaruResource resource = element.getElementImage();

        if ((resource != null ) && !resource.getPath().isEmpty())
        {
            drawTexturedIcon(resource.getPath(), mapPos, color, iconSize);
        }
        else
        {
            // fallback for when we cannot load an image for the element
            iconSize /= 2;
            drawFallbackIcon(mapPos, color, iconSize);
        }
    }

    private void drawName(IGroundstation element, FlatMapPosition mapPos, VisibleElementColor color, GroundstationSettings settings)
    {
        if (!settings.getShowElementName()) {
            return;
        }

        int iconSize = settings.getShowElementIcon() ? getIconSize(element, settings) : 0;

        GLUtils.drawText(textRenderer,
                         mapParams.clientAreaWidth,
                         mapParams.clientAreaHeight,
                         mapParams.mapX + mapPos.X + (iconSize / 2),
                         mapParams.mapHeight - (mapPos.Y - mapParams.mapY),
                         element.getElementName(),
                         true);
    }

    private void drawTexturedIcon(String imagePath, FlatMapPosition mapPos, VisibleElementColor color, int size)
    {
        Texture texture = textureCache.get(imagePath);

        gl.glColor3ub((byte) color.r, (byte) color.g, (byte) color.b);

        GLUtils.drawTexture(gl, texture,
                            mapParams.mapX + mapPos.X - (size / 2),
                            mapParams.mapHeight - (mapPos.Y - mapParams.mapY) - (size / 2),
                            size, size);
    }

    private void drawFallbackIcon(FlatMapPosition mapPos, VisibleElementColor color, int size)
    {
        MapPrimitives painter = new MapPrimitives(gl, mapParams);
        painter.drawPoint(mapPos.X, mapPos.Y, size, color, 1.0);
    }

    private int getIconSize(IGroundstation element, GroundstationSettings settings)
    {
        if ((selectedElement != null) && selectedElement == element) {
            // draw the selected element larger
            return (int) (1.5 * settings.getElementIconSize());
        } else {
            return (int) settings.getElementIconSize();
        }
    }

    private void drawVisibilityCircle(IGroundstation groundstaion, GroundstationSettings settings) throws OrekitException
    {
        if (!scenarioSettings.getShowVisibilityCircles()) {
            return;
        }

        if ((selectedElement == null) || !(selectedElement instanceof ISpacecraft)) {
            return;
        }

        // draw the ground station visibility circle for the selected spacecraft
        ISpacecraft selectedSpacecraft = (ISpacecraft) selectedElement;
        VisibleElementColor color = selectedSpacecraft.getElementColor();
        int pointCount = mapSettings.getVisibilityCirclePoints();
        List<GeodeticPoint> points = groundstaion.getVisibilityCircle(selectedSpacecraft.getCurrentCoordinate(), pointCount);
        MapPrimitives painter = new MapPrimitives(gl, mapParams);

        painter.beginPolygon();
        for (GeodeticPoint point : points) {
            FlatMapPosition projectedPoint = getMapPosition(point);
            painter.addPolygonVertex(projectedPoint.X, projectedPoint.Y, color, 0.05);
        }
        painter.endPolygon();

        painter.beginLine(2.0, 0x4444, true);
        for (GeodeticPoint point : points) {
            FlatMapPosition projectedPoint = getMapPosition(point);
            painter.addLineVertex(projectedPoint.X, projectedPoint.Y, color, 1.0);
        }
        painter.endLine();
    }

    private FlatMapPosition getMapPosition(GeodeticPoint point)
    {
        return projector.project(point);
    }
}
