package maru.map.views.gl.jobs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.media.opengl.GL2;

import maru.centralbody.model.projection.FlatMapPosition;
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
import maru.map.jobs.gl.GLProjectDrawJob;
import maru.map.views.GroundtrackPoint;
import maru.map.views.GroundtrackRange;
import maru.map.views.MapViewParameters;
import maru.map.views.MapViewSettings;
import maru.map.views.gl.GLUtils;
import maru.ui.model.UiVisible;

import org.orekit.bodies.GeodeticPoint;
import org.orekit.errors.OrekitException;

import com.jogamp.opengl.util.texture.Texture;

public class ScenarioDrawJob extends GLProjectDrawJob
{
    private class IconSize { int x; int y; };

    private final Map<ISpacecraft, GroundtrackRange> gtBarriers;

    public ScenarioDrawJob()
    {
        gtBarriers = new WeakHashMap<>();
    }

    @Override
    public void draw()
    {
        MapViewParameters area = getParameters();
        MapViewSettings drawing = getSettings();

        IScenarioProject scenario = getProject().getUnderlyingElement();

        getProjector().setCentralBody(scenario.getCentralBody());
        getProjector().setMapSize(area.mapWidth, area.mapHeight);

        for (IGroundstation element : scenario.getGroundstations())
        {
            try
            {
                GeodeticPoint position = element.getGeodeticPosition();
                FlatMapPosition mapPos = getMapPosition(position);
                VisibleElementColor defaultColor = element.getElementColor();

                drawElement(element, mapPos, defaultColor);
                drawVisibilityCircle(element);
            }
            catch (OrekitException e)
            {
                e.printStackTrace();
            }
        }

        for (ISpacecraft element : scenario.getSpacecrafts())
        {
            try
            {
                ICoordinate currentCoordinate = element.getCurrentCoordinate();
                FlatMapPosition mapPos = getMapPosition(currentCoordinate);
                if (mapPos == null) {
                    return;
                }

                GroundtrackRange currentGtBarrier;
                if (!gtBarriers.containsKey(element)) {
                    currentGtBarrier = new GroundtrackRange(currentCoordinate.getDate(), drawing.getGroundtrackLength());
                    gtBarriers.put(element, currentGtBarrier);
                } else {
                    currentGtBarrier = gtBarriers.get(element);
                    currentGtBarrier.update(currentCoordinate.getDate(), drawing.getGroundtrackLength());
                }

                VisibleElementColor defaultColor = element.getElementColor();
                VisibleElementColor nightColor = toNightRGB(element.getElementColor());
                VisibleElementColor currentColor = selectColor(currentCoordinate, defaultColor, nightColor);

                drawGroundtrack(element, currentGtBarrier, defaultColor, nightColor);
                drawElement(element, mapPos, currentColor);
                drawVisibilityCircle(element, mapPos);
                drawSatToGsVisibility(scenario, element);
                drawSatToSatVisibility(scenario, element);
            }
            catch (OrekitException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void dispose()
    {
        // this job does not own any resources
    }

    private void drawGroundtrack(ISpacecraft element, GroundtrackRange barrier, VisibleElementColor day, VisibleElementColor night) throws OrekitException
    {
        MapPrimitives painter = new MapPrimitives(getGL(), getParameters());

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

    private void drawElement(IVisibleElement element, FlatMapPosition mapPos, VisibleElementColor color)
    {
        MapViewParameters area = getParameters();
        IconSize iconSize = getElementIconSize(element);
        IMaruResource resource = element.getElementImage();

        if ((resource != null ) && !resource.getPath().isEmpty())
        {
            drawElementTexture(resource.getPath(), mapPos, color, iconSize);
        }
        else
        {
            // fallback for when we cannot load an image for the element
            iconSize.x /= 2;
            iconSize.y /= 2;
            drawElementFallback(mapPos, color, iconSize);
        }

        // draw the element name no matter if we are in fallback mode or not
        GLUtils.drawText(getTextRenderer(),
                         area.clientAreaWidth,
                         area.clientAreaHeight,
                         area.mapX + mapPos.X + (iconSize.x / 2),
                         area.mapHeight - (mapPos.Y - area.mapY),
                         element.getElementName(),
                         true);
    }

    private void drawSatToGsVisibility(IScenarioProject scenario, ISpacecraft element) throws OrekitException
    {
        MapViewSettings settings = getSettings();
        if (!settings.getShowVisibilitySpacecraftToGroundstation()) {
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

    private void drawSatToSatVisibility(IScenarioProject scenario, ISpacecraft element) throws OrekitException
    {
        MapViewSettings settings = getSettings();
        if (!settings.getShowVisibilitySpacecraftToSpacecraft()) {
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

    private void drawAccessLine(FlatMapPosition pos1,
                                FlatMapPosition pos2,
                                double dist, boolean showDist)
    {
        MapPrimitives painter = new MapPrimitives(getGL(), getParameters());
        painter.drawLine(pos1.X, pos1.Y, pos2.X, pos2.Y, 1.0, new VisibleElementColor(255, 255, 255), 1.0);

        if (!showDist) {
            return;
        }

        MapViewParameters area = getParameters();
        GLUtils.drawText(getTextRenderer(),
                         area.clientAreaWidth,
                         area.clientAreaHeight,
                         area.mapX + ((Math.max(pos1.X, pos2.X) + Math.min(pos1.X, pos2.X)) / 2),
                         area.mapHeight - (((Math.max(pos1.Y, pos2.Y) + Math.min(pos1.Y, pos2.Y)) / 2) - area.mapY),
                         FormatUtils.formatNoDecimalPoint(dist / 1000.0) + " km",
                         true);
    }

    private void drawVisibilityCircle(IGroundstation groundstaion) throws OrekitException
    {
        MapViewSettings settings = getSettings();
        if (!settings.getShowVisibilityCircles()) {
            return;
        }

        UiVisible selectedElement = getSelectedElement();
        if (selectedElement == null) {
            return;
        }

        IVisibleElement selectedUnderlying = selectedElement.getUnderlyingElement();
        if (!(selectedUnderlying instanceof ISpacecraft)) {
            return;
        }

        // draw the ground station visibility circle for the selected spacecraft
        ISpacecraft selectedSpacecraft = (ISpacecraft) selectedUnderlying;
        VisibleElementColor color = selectedSpacecraft.getElementColor();
        List<GeodeticPoint> points = groundstaion.getVisibilityCircle(selectedSpacecraft.getCurrentCoordinate(), 48);
        MapPrimitives painter = new MapPrimitives(getGL(), getParameters());

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

    private void drawVisibilityCircle(ISpacecraft spacecraft, FlatMapPosition scPos) throws OrekitException
    {
        MapViewSettings settings = getSettings();
        if (!settings.getShowVisibilityCircles()) {
            return;
        }

        VisibleElementColor color = spacecraft.getElementColor();
        List<GeodeticPoint> points = spacecraft.getVisibilityCircle(48);
        MapPrimitives painter = new MapPrimitives(getGL(), getParameters());

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

//        for (GeodeticPoint point : points) {
//            EquirectangularCoordinate projectedPoint = getMapPosition(point);
//            painter.drawLine(scPos.X, scPos.Y, projectedPoint.X, projectedPoint.Y, 4.0, spacecraft.getElementColor(), 0.75);
//        }

//        for (List<EquirectangularCoordinate> lineStrips : getVisibilityCircle(points, true))
//        {
//            int i = 0;
//            for (EquirectangularCoordinate lineStrip : lineStrips)
//            {
//                GLUtils.drawText(getTextRenderer(),
//                        area.clientAreaWidth,
//                        area.clientAreaHeight,
//                        area.mapX + lineStrip.X,
//                        area.mapHeight - (lineStrip.Y - area.mapY),
//                        i++ + " (" + lineStrip.toString() + ")",
//                        true);
//            }
//        }
    }

    private FlatMapPosition getMapPosition(GeodeticPoint point)
    {
        return getProjector().project(point);
    }

    private FlatMapPosition getMapPosition(ICoordinate coordinate) throws OrekitException
    {
        return getProjector().project(coordinate);
    }

    private List<List<GroundtrackPoint>> getGroundtrack(ISpacecraft element, GroundtrackRange barrier) throws OrekitException
    {
        MapViewParameters area = getParameters();
        MapViewSettings drawing = getSettings();

        List<List<GroundtrackPoint>> elementLineStrips = new ArrayList<>();

        IPropagator propagator = element.getPropagator();
        FlatMapPosition lastMapPos = null;
        List<GroundtrackPoint> lineStrip = new ArrayList<>();

        for (ICoordinate coordinate : propagator.getCoordinates(barrier.getStart(), barrier.getStop(), drawing.getGroundtrackStepSize(), element))
        {
            FlatMapPosition mapPos = getMapPosition(coordinate);
            boolean inDaylight = !drawing.getShowShadowTimes() ? true : !inShadow(coordinate);

            if (lastMapPos == null)
            {
                lineStrip.add(new GroundtrackPoint(mapPos.X, mapPos.Y, inDaylight));

                lastMapPos = mapPos;
                continue;
            }

            if (Math.abs(mapPos.X - lastMapPos.X) > (area.mapWidth / 2))
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

    private boolean inShadow(ICoordinate coordinate)
    {
        return coordinate.getEclipseState() != EclipseState.None;
    }

    private IconSize getElementIconSize(IVisibleElement element)
    {
        MapViewParameters area = getParameters();
        IconSize size = new IconSize();

        // TODO: automatically scale x/y size of the icon

        if ((getSelectedElement() != null) && element == getSelectedElement().getUnderlyingElement()) {
            // draw the selected element larger
            size.x = (int) (1.5 * area.iconSize);
            size.y = (int) (1.5 * area.iconSize);
            return size;
        } else {
            size.x = area.iconSize;
            size.y = area.iconSize;
            return size;
        }
    }

    private void drawElementTexture(String imagePath, FlatMapPosition mapPos, VisibleElementColor color, IconSize iconSize)
    {
        GL2 gl = getGL();
        MapViewParameters area = getParameters();
        Texture texture = getTextureCache().get(imagePath);

        gl.glColor3ub((byte) color.r, (byte) color.g, (byte) color.b);

        GLUtils.drawTexture(gl, texture,
                            area.mapX + mapPos.X - (iconSize.x / 2),
                            area.mapHeight - (mapPos.Y - area.mapY) - (iconSize.y / 2),
                            iconSize.x, iconSize.y);
    }

    private void drawElementFallback(FlatMapPosition mapPos, VisibleElementColor color, IconSize iconSize)
    {
        MapPrimitives painter = new MapPrimitives(getGL(), getParameters());
        painter.drawPoint(mapPos.X, mapPos.Y, Math.max(iconSize.x, iconSize.y), color, 1.0);
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