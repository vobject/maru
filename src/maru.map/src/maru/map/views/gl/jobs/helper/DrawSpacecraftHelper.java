package maru.map.views.gl.jobs.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.media.opengl.GL2;

import maru.centralbody.model.projection.FlatMapPosition;
import maru.centralbody.model.projection.ICoordinateProjector;
import maru.core.model.ICentralBody;
import maru.core.model.ICoordinate;
import maru.core.model.IGroundstation;
import maru.core.model.IPropagator;
import maru.core.model.IScenarioProject;
import maru.core.model.ISpacecraft;
import maru.core.model.IVisibleElement;
import maru.core.model.VisibleElementColor;
import maru.core.model.resource.IMaruResource;
import maru.core.model.utils.EclipseState;
import maru.core.model.utils.FormatUtils;
import maru.map.jobs.gl.TextureCache;
import maru.map.settings.scenario.ScenarioSettings;
import maru.map.settings.spacecraft.SpacecraftSettings;
import maru.map.views.GroundtrackPoint;
import maru.map.views.GroundtrackRange;
import maru.map.views.MapViewParameters;
import maru.map.views.MapViewSettings;
import maru.map.views.gl.GLUtils;
import maru.map.views.gl.jobs.MapPrimitives;

import org.orekit.bodies.GeodeticPoint;
import org.orekit.errors.OrekitException;

import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.opengl.util.texture.Texture;

public final class DrawSpacecraftHelper
{
    private GL2 gl;
    private TextRenderer textRenderer;
    private TextureCache textureCache;
    private MapViewParameters mapParams;
    private MapViewSettings mapSettings;
    private ICoordinateProjector projector;
    private IVisibleElement selectedElement;
    private ScenarioSettings scenarioSettings;

    private final Map<ISpacecraft, GroundtrackRange> gtRanges = new WeakHashMap<>();

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

    public void draw(ISpacecraft element, SpacecraftSettings settings)
    {
        try
        {
            ICoordinate currentCoordinate = element.getCurrentCoordinate();
            FlatMapPosition mapPos = getMapPosition(currentCoordinate);
            if (mapPos == null) {
                return;
            }

            GroundtrackRange currentGtBarrier;
            if (!gtRanges.containsKey(element)) {
                currentGtBarrier = new GroundtrackRange(currentCoordinate.getDate(), scenarioSettings.getGroundtrackLength());
                gtRanges.put(element, currentGtBarrier);
            } else {
                currentGtBarrier = gtRanges.get(element);
                currentGtBarrier.update(currentCoordinate.getDate(), scenarioSettings.getGroundtrackLength());
            }

            IScenarioProject scenario = element.getScenarioProject();
            VisibleElementColor defaultColor = element.getElementColor();
            VisibleElementColor nightColor = toNightRGB(element.getElementColor());
            VisibleElementColor currentColor = selectColor(currentCoordinate, defaultColor, nightColor);

            drawGroundtrack(element, currentGtBarrier, defaultColor, nightColor, settings);
            drawVisibilityCircle(element, settings);
            drawIcon(element, mapPos, currentColor, settings);
            drawName(element, mapPos, currentColor, settings);
            drawSatToGsVisibility(scenario, element, settings);
            drawSatToSatVisibility(scenario, element, settings);
        }
        catch (OrekitException e)
        {
            e.printStackTrace();
        }
    }

    private void drawIcon(ISpacecraft element, FlatMapPosition mapPos, VisibleElementColor color, SpacecraftSettings settings)
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

    private void drawName(ISpacecraft element, FlatMapPosition mapPos, VisibleElementColor color, SpacecraftSettings settings)
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

    private int getIconSize(ISpacecraft element, SpacecraftSettings settings)
    {
        if ((selectedElement != null) && selectedElement == element) {
            // draw the selected element larger
            return (int) (1.5 * settings.getElementIconSize());
        } else {
            return (int) settings.getElementIconSize();
        }
    }

    private void drawVisibilityCircle(ISpacecraft element, SpacecraftSettings settings) throws OrekitException
    {
        if (!scenarioSettings.getShowVisibilityCircles()) {
            return;
        }

        VisibleElementColor color = element.getElementColor();
        int pointCount = mapSettings.getVisibilityCirclePoints();
        List<GeodeticPoint> points = element.getVisibilityCircle(pointCount);
        MapPrimitives painter = new MapPrimitives(gl, mapParams);

        painter.beginPolygon();
        for (GeodeticPoint point : points) {
            FlatMapPosition projectedPoint = getMapPosition(point);
            painter.addPolygonVertex(projectedPoint.X, projectedPoint.Y, color, 0.2);
        }
        painter.endPolygon();

        painter.beginLine(1.5, true);
        for (GeodeticPoint point : points) {
            FlatMapPosition projectedPoint = getMapPosition(point);
            painter.addPolygonVertex(projectedPoint.X, projectedPoint.Y, color, 0.6);
        }
        painter.endPolygon();

//      for (GeodeticPoint point : points) {
//          EquirectangularCoordinate projectedPoint = getMapPosition(point);
//          painter.drawLine(scPos.X, scPos.Y, projectedPoint.X, projectedPoint.Y, 4.0, spacecraft.getElementColor(), 0.75);
//      }

//      for (List<EquirectangularCoordinate> lineStrips : getVisibilityCircle(points, true))
//      {
//          int i = 0;
//          for (EquirectangularCoordinate lineStrip : lineStrips)
//          {
//              GLUtils.drawText(getTextRenderer(),
//                      area.clientAreaWidth,
//                      area.clientAreaHeight,
//                      area.mapX + lineStrip.X,
//                      area.mapHeight - (lineStrip.Y - area.mapY),
//                      i++ + " (" + lineStrip.toString() + ")",
//                      true);
//          }
//      }
    }

    private void drawSatToGsVisibility(IScenarioProject scenario, ISpacecraft element, SpacecraftSettings settings) throws OrekitException
    {
        if (!scenarioSettings.getShowVisibilitySpacecraftToGroundstation()) {
            return;
        }

        ICentralBody centralBody = element.getCentralBody();
        ICoordinate coordinate = element.getCurrentCoordinate();

        for (IGroundstation gs : scenario.getGroundstations())
        {
            if (!element.hasAccessTo(gs)) {
                continue;
            }

            double distToGs = element.getDistanceTo(gs);
            FlatMapPosition satPos = getMapPosition(centralBody.getIntersection(coordinate));
            FlatMapPosition gsPos = getMapPosition(gs.getGeodeticPosition());

            drawAccessLine(satPos, gsPos, distToGs, true);
        }
    }

    private void drawSatToSatVisibility(IScenarioProject scenario, ISpacecraft element, SpacecraftSettings settings) throws OrekitException
    {
        if (!scenarioSettings.getShowVisibilitySpacecraftToSpacecraft()) {
            return;
        }

        ICentralBody centralBody = element.getCentralBody();
        ICoordinate coordinate = element.getCurrentCoordinate();

        for (ISpacecraft sc : scenario.getSpacecrafts())
        {
            if (sc == element) {
                continue;
            }

            if (!element.hasAccessTo(sc.getCurrentCoordinate())) {
                continue;
            }

            double distToSc = element.getDistanceTo(sc.getCurrentCoordinate());
            FlatMapPosition satPos = getMapPosition(centralBody.getIntersection(coordinate));
            FlatMapPosition sat2Pos = getMapPosition(centralBody.getIntersection(sc.getCurrentCoordinate()));

            drawAccessLine(satPos, sat2Pos, distToSc, true);
        }
    }

    private void drawAccessLine(FlatMapPosition pos1, FlatMapPosition pos2,
                                double dist, boolean showDist)
    {
        MapPrimitives painter = new MapPrimitives(gl, mapParams);
        painter.drawLine(pos1.X, pos1.Y, pos2.X, pos2.Y, 1.0, new VisibleElementColor(255, 255, 255), 1.0);

        if (!showDist) {
            return;
        }

        GLUtils.drawText(textRenderer,
                         mapParams.clientAreaWidth,
                         mapParams.clientAreaHeight,
                         mapParams.mapX + ((Math.max(pos1.X, pos2.X) + Math.min(pos1.X, pos2.X)) / 2),
                         mapParams.mapHeight - (((Math.max(pos1.Y, pos2.Y) + Math.min(pos1.Y, pos2.Y)) / 2) - mapParams.mapY),
                         FormatUtils.formatNoDecimalPoint(dist / 1000.0) + " km",
                         true);
    }

    private void drawGroundtrack(ISpacecraft element, GroundtrackRange barrier, VisibleElementColor day, VisibleElementColor night, SpacecraftSettings settings) throws OrekitException
    {
        MapPrimitives painter = new MapPrimitives(gl, mapParams);

        for (List<GroundtrackPoint> lineStrips : getGroundtrack(element, barrier))
        {
            painter.beginLine(2.0, false);
            for (GroundtrackPoint lineStrip : lineStrips)
            {
                VisibleElementColor color = lineStrip.day ? day : night;
                painter.addLineVertex(lineStrip.X, lineStrip.Y, color, 1.0);
            }
            painter.endLine();
        }
    }

    private List<List<GroundtrackPoint>> getGroundtrack(ISpacecraft element, GroundtrackRange barrier) throws OrekitException
    {
        List<List<GroundtrackPoint>> elementLineStrips = new ArrayList<>();

        IPropagator propagator = element.getPropagator();
        FlatMapPosition lastMapPos = null;
        List<GroundtrackPoint> lineStrip = new ArrayList<>();

        for (ICoordinate coordinate : propagator.getCoordinates(barrier.getStart(), barrier.getStop(), scenarioSettings.getGroundtrackStepSize(), element))
        {
            FlatMapPosition mapPos = getMapPosition(coordinate);
            boolean inDaylight = !scenarioSettings.getShowUmbraOnGroundtrack() ? true : !inShadow(coordinate);

            if (lastMapPos == null)
            {
                lineStrip.add(new GroundtrackPoint(mapPos.X, mapPos.Y, inDaylight));

                lastMapPos = mapPos;
                continue;
            }

            if (Math.abs(mapPos.X - lastMapPos.X) > (mapParams.mapWidth / 2))
            {
                //lineStrip.add(new GroundtrackPoint(area.mapWidth, mapPos.Y, inDaylight));
                elementLineStrips.add(lineStrip);

                lineStrip = new ArrayList<>();
                //lineStrip.add(new GroundtrackPoint(0, lastMapPos.Y, inDaylight));
                lineStrip.add(new GroundtrackPoint(mapPos.X, mapPos.Y, inDaylight));

                lastMapPos = mapPos;
                continue;
            }

            lineStrip.add(new GroundtrackPoint(mapPos.X, mapPos.Y, inDaylight));
            lastMapPos = mapPos;
        }
        elementLineStrips.add(lineStrip);

        return elementLineStrips;
    }

    private FlatMapPosition getMapPosition(ICoordinate coordinate) throws OrekitException
    {
        return projector.project(coordinate);
    }

    private FlatMapPosition getMapPosition(GeodeticPoint point)
    {
        return projector.project(point);
    }

    private boolean inShadow(ICoordinate coordinate)
    {
        return coordinate.getEclipseState() != EclipseState.None;
    }

    private VisibleElementColor selectColor(ICoordinate coordinate, VisibleElementColor day, VisibleElementColor night)
    {
        return inShadow(coordinate) ? night : day;
    }

    private VisibleElementColor toNightRGB(VisibleElementColor color)
    {
        return new VisibleElementColor(color.r / 2, color.g / 2, color.b / 2);
    }
}
