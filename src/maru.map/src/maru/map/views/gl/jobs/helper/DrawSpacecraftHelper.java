package maru.map.views.gl.jobs.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUtessellator;

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
                currentGtBarrier = new GroundtrackRange(currentCoordinate.getDate(), settings.getGroundtrackLength());
                gtRanges.put(element, currentGtBarrier);
            } else {
                currentGtBarrier = gtRanges.get(element);
                currentGtBarrier.update(currentCoordinate.getDate(), settings.getGroundtrackLength());
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
                         mapSettings.getOutlineText());
    }

    private void drawTexturedIcon(String imagePath, FlatMapPosition mapPos, VisibleElementColor color, int size)
    {
        Texture texture = textureCache.get(imagePath);

        GLUtils.drawTexture(gl, texture, color,
                            mapParams.mapX + mapPos.X - (size / 2),
                            mapParams.mapHeight - (mapPos.Y - mapParams.mapY) - (size / 2),
                            size, size, mapSettings.getOutlineIcons());
    }

    private void drawFallbackIcon(FlatMapPosition mapPos, VisibleElementColor color, int size)
    {
        MapPrimitives painter = new MapPrimitives(gl, mapParams);
        painter.drawPoint(mapPos.X, mapPos.Y, size, color, 1.0, mapSettings.getOutlineText());
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

    static class TessCallback extends javax.media.opengl.glu.GLUtessellatorCallbackAdapter
    {
        private final GL2 gl;

        public TessCallback(GL2 gl) { this.gl = gl; };

        @Override
        public void error(int errnum) { throw new RuntimeException("Tessellation Error"); }

        @Override
        public void begin(int type) { gl.glBegin(type); }

        @Override
        public void end() { gl.glEnd(); }

        @Override
        public void vertex(Object data) { gl.glVertex3dv((double[]) data, 0); }

        @Override
        public void combine(double[] coords, Object[] data, float[] weight, Object[] outData) { outData[0] = coords; }
    }

    static void drawPolygon(GL2 gl)
    {
        double[][] star = new double[][]{{250.0, 50.0, 0.0},
                                         {325.0, 200.0, 0.0},
                                         {400.0, 50.0, 0.0},
                                         {250.0, 150.0, 0.0},
                                         {400.0, 150.0, 0.0}};

        GLUtessellator tobj = GLU.gluNewTess();

        TessCallback tessCallback = new TessCallback(gl);

        GLU.gluTessCallback(tobj, GLU.GLU_TESS_VERTEX, tessCallback);
        GLU.gluTessCallback(tobj, GLU.GLU_TESS_BEGIN, tessCallback);
        GLU.gluTessCallback(tobj, GLU.GLU_TESS_END, tessCallback);
        GLU.gluTessCallback(tobj, GLU.GLU_TESS_ERROR, tessCallback);
        GLU.gluTessCallback(tobj, GLU.GLU_TESS_COMBINE, tessCallback);

        int startList = gl.glGenLists(1);
        gl.glNewList(startList, GL2.GL_COMPILE_AND_EXECUTE);
        GLU.gluTessProperty(tobj, GLU.GLU_TESS_WINDING_RULE, GLU.GLU_TESS_WINDING_POSITIVE);
        GLU.gluTessBeginPolygon(tobj, null);
        GLU.gluTessBeginContour(tobj);
        GLU.gluTessVertex(tobj, star[0], 0, star[0]);
        GLU.gluTessVertex(tobj, star[1], 0, star[1]);
        GLU.gluTessVertex(tobj, star[2], 0, star[2]);
        GLU.gluTessVertex(tobj, star[3], 0, star[3]);
        GLU.gluTessVertex(tobj, star[4], 0, star[4]);
        GLU.gluTessEndContour(tobj);
        GLU.gluTessEndPolygon(tobj);
        gl.glEndList();

        GLU.gluDeleteTess(tobj);

        gl.glDeleteLists(startList, 1);
    }

    private void sortByMapX(List<FlatMapPosition> list)
    {
        Collections.sort(list, new Comparator<FlatMapPosition>() {
            @Override
            public int compare(FlatMapPosition o1, FlatMapPosition o2) {
                return o2.X - o1.X;
            }

            @Override
            public Comparator<FlatMapPosition> reversed() {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Comparator<FlatMapPosition> thenComparing(
                    Comparator<? super FlatMapPosition> other) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public <U> Comparator<FlatMapPosition> thenComparing(
                    Function<? super FlatMapPosition, ? extends U> keyExtractor,
                    Comparator<? super U> keyComparator) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public <U extends Comparable<? super U>> Comparator<FlatMapPosition> thenComparing(
                    Function<? super FlatMapPosition, ? extends U> keyExtractor) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Comparator<FlatMapPosition> thenComparingInt(
                    ToIntFunction<? super FlatMapPosition> keyExtractor) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Comparator<FlatMapPosition> thenComparingLong(
                    ToLongFunction<? super FlatMapPosition> keyExtractor) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public Comparator<FlatMapPosition> thenComparingDouble(
                    ToDoubleFunction<? super FlatMapPosition> keyExtractor) {
                // TODO Auto-generated method stub
                return null;
            }
        });
    }

    private boolean isNorth(ISpacecraft element)
    {
        ICoordinate currentCoordinate = element.getCurrentCoordinate();
        FlatMapPosition mapPos = null;
        try {
            mapPos = getMapPosition(currentCoordinate);
        } catch (OrekitException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return mapPos.Y < (mapParams.mapHeight / 2);
    }

    private void drawPolygon(GL2 gl, ISpacecraft element, List<GeodeticPoint> points)
    {
        List<FlatMapPosition> projectedPoints = new ArrayList<>();
        for (GeodeticPoint point : points) {
            projectedPoints.add(getMapPosition(point));
        }

        if (isNorth(element))
        {
            // OR: check if point 0 has the same X as element! if no -> sort!
            if ((projectedPoints.get(0).X > projectedPoints.get(1).X)) {
                System.out.println("case 1");
                sortByMapX(projectedPoints);
                projectedPoints.add(0, new FlatMapPosition(projectedPoints.get(0).X, 0));
                projectedPoints.add(new FlatMapPosition(projectedPoints.get(projectedPoints.size() - 1).X, 0));
            } else if ((projectedPoints.get(0).X < projectedPoints.get(projectedPoints.size() - 1).X)) {
                System.out.println("case 2");
                sortByMapX(projectedPoints);
                projectedPoints.add(0, new FlatMapPosition(projectedPoints.get(0).X, 0));
                projectedPoints.add(new FlatMapPosition(projectedPoints.get(projectedPoints.size() - 1).X, 0));
            }
        }
        else
        {
            if ((projectedPoints.get(projectedPoints.size() / 2).X > projectedPoints.get(projectedPoints.size() / 2 - 1).X)) {
                System.out.println("case 11");
                sortByMapX(projectedPoints);
                projectedPoints.add(0, new FlatMapPosition(projectedPoints.get(0).X, mapParams.mapHeight));
                projectedPoints.add(new FlatMapPosition(projectedPoints.get(projectedPoints.size() - 1).X, mapParams.mapHeight));
            } else if ((projectedPoints.get(projectedPoints.size() / 2).X < projectedPoints.get(projectedPoints.size() / 2 + 1).X)) {
                System.out.println("case 22");
                sortByMapX(projectedPoints);
                projectedPoints.add(0, new FlatMapPosition(projectedPoints.get(0).X, mapParams.mapHeight));
                projectedPoints.add(new FlatMapPosition(projectedPoints.get(projectedPoints.size() - 1).X, mapParams.mapHeight));
            }
        }


        double[][] poly = new double[projectedPoints.size()][3];

        for (int i = 0; i < poly.length; i++)
        {
            poly[i][0] = mapParams.mapX + projectedPoints.get(i).X;
            poly[i][1] = mapParams.mapHeight - (projectedPoints.get(i).Y - mapParams.mapY);
            poly[i][2] = 0.0;
        }

        GLUtessellator tobj = GLU.gluNewTess();

        TessCallback tessCallback = new TessCallback(gl);

        GLU.gluTessCallback(tobj, GLU.GLU_TESS_VERTEX, tessCallback);
        GLU.gluTessCallback(tobj, GLU.GLU_TESS_BEGIN, tessCallback);
        GLU.gluTessCallback(tobj, GLU.GLU_TESS_END, tessCallback);
        GLU.gluTessCallback(tobj, GLU.GLU_TESS_ERROR, tessCallback);
        GLU.gluTessCallback(tobj, GLU.GLU_TESS_COMBINE, tessCallback);

        int startList = gl.glGenLists(1);
        gl.glNewList(startList, GL2.GL_COMPILE_AND_EXECUTE);
        GLU.gluTessProperty(tobj, GLU.GLU_TESS_WINDING_RULE, GLU.GLU_TESS_WINDING_POSITIVE);
        GLU.gluTessBeginPolygon(tobj, null);
        GLU.gluTessBeginContour(tobj);
        for (double[] element1 : poly) {
            GLU.gluTessVertex(tobj, element1, 0, element1);
        }
        GLU.gluTessEndContour(tobj);
        GLU.gluTessEndPolygon(tobj);
        gl.glEndList();

        GLU.gluDeleteTess(tobj);

        gl.glDeleteLists(startList, 1);


        int i = 0;
        for (FlatMapPosition p : projectedPoints)
        {
            GLUtils.drawText(textRenderer,
                             mapParams.clientAreaWidth,
                             mapParams.clientAreaHeight,
                             mapParams.mapX + p.X,
                             mapParams.mapHeight - (p.Y - mapParams.mapY),
                             i++ + "",//" (" + projectedPoint.toString() + ")",
                             mapSettings.getOutlineText());
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

//        boolean b = false;
//        painter.beginPolygon();
//        for (GeodeticPoint point : points) {
//            if (!b) { b = true; continue; }
//            FlatMapPosition projectedPoint = getMapPosition(point);
//            painter.addPolygonVertex(projectedPoint.X, projectedPoint.Y, color, 0.2);
//        }
//        painter.endPolygon();

//        painter.beginLine(1.5, true);
//        for (GeodeticPoint point : points) {
//            FlatMapPosition projectedPoint = getMapPosition(point);
//            painter.addPolygonVertex(projectedPoint.X, projectedPoint.Y, color, 0.6);
//        }
//        painter.endLine();


        for (GeodeticPoint point : points) {
            FlatMapPosition projectedPoint = getMapPosition(point);
            painter.drawPoint(projectedPoint.X, projectedPoint.Y, 2.0, color, 0.75, true);
        }


        drawPolygon(gl, element, points);


//        ICoordinate currentCoordinate = element.getCurrentCoordinate();
//        FlatMapPosition mapPos = getMapPosition(currentCoordinate);
//
//        gl.glPushAttrib(GL2.GL_POLYGON_BIT);
//        gl.glBegin(GL2.GL_POLYGON);
//
//        gl.glColor4ub((byte) color.r, (byte) color.g, (byte) color.b, (byte) (0.2 * 255.0));
//        gl.glVertex2d(mapPos.X, mapPos.Y);
//
//        for (GeodeticPoint point : points) {
//            FlatMapPosition projectedPoint = getMapPosition(point);
//
//            gl.glColor4ub((byte) color.r, (byte) color.g, (byte) color.b, (byte) (0.2 * 255.0));
//            gl.glVertex2d(projectedPoint.X, projectedPoint.Y);
//        }
//
//        gl.glEnd();
//        gl.glPopAttrib();



//      for (GeodeticPoint point : points) {
//          EquirectangularCoordinate projectedPoint = getMapPosition(point);
//          painter.drawLine(scPos.X, scPos.Y, projectedPoint.X, projectedPoint.Y, 4.0, spacecraft.getElementColor(), 0.75);
//      }

//        int i = 0;
//        for (GeodeticPoint point : points)
//        {
//            FlatMapPosition projectedPoint = getMapPosition(point);
//
//            GLUtils.drawText(textRenderer,
//                             mapParams.clientAreaWidth,
//                             mapParams.clientAreaHeight,
//                             mapParams.mapX + projectedPoint.X,
//                             mapParams.mapHeight - (projectedPoint.Y - mapParams.mapY),
//                             i++ + "",//" (" + projectedPoint.toString() + ")",
//                             mapSettings.getOutlineText());
//        }
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
                         mapSettings.getOutlineText());
    }

    private void drawGroundtrack(ISpacecraft element, GroundtrackRange barrier, VisibleElementColor day, VisibleElementColor night, SpacecraftSettings settings) throws OrekitException
    {
        MapPrimitives painter = new MapPrimitives(gl, mapParams);

        for (List<GroundtrackPoint> lineStrips : getGroundtrack(element, barrier))
        {
            painter.beginLine(settings.getGroundtrackLineWidth(), false);
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

        for (ICoordinate coordinate : propagator.getCoordinates(barrier.getStart(), barrier.getStop(), mapSettings.getGroundtrackStepSize(), element))
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
