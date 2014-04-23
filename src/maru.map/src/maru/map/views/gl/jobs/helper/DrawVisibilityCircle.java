package maru.map.views.gl.jobs.helper;

import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUtessellator;
import javax.media.opengl.glu.GLUtessellatorCallbackAdapter;

import maru.centralbody.model.projection.FlatMapPosition;
import maru.core.model.VisibleElementColor;
import maru.map.views.MapViewParameters;

import com.jogamp.opengl.util.awt.TextRenderer;

public class DrawVisibilityCircle
{
    private final GL2 gl;
    private final TextRenderer textRenderer;
    private final MapViewParameters mapParams;
    byte r;
    byte g;
    byte b;
    byte opaque;

    enum Quadrant
    {
        NW,
        SW,
        NE,
        SE
    }

    class Vertex
    {
        int x;
        int y;
        Quadrant quadrant;

        int mapX;
        int mapY;

        public Vertex(int x, int y)
        {
            this.x = x;
            this.y = y;
            this.quadrant = getQuadrant(x, y);

            this.mapX = mapParams.mapX + x;
            this.mapY = mapParams.mapHeight - (y - mapParams.mapY);
        }

        public boolean inNorthernHemisphere()
        {
            return (quadrant == Quadrant.NW) || (quadrant == Quadrant.NE);
        }

        public boolean inSouthernHemisphere()
        {
            return !inNorthernHemisphere();
        }

        public boolean inWesternHemisphere()
        {
            return (quadrant == Quadrant.NW) || (quadrant == Quadrant.SW);
        }

        public boolean inEasternHemisphere()
        {
            return !inWesternHemisphere();
        }

        @Override
        public String toString()
        {
            return "vert(" + x + "," + y + "," + quadrant.toString() + "); map(" + mapX + "," + mapY + ")";
        }
    }

    static class TesselatorCB extends GLUtessellatorCallbackAdapter
    {
        private final GL2 gl;

        public TesselatorCB(GL2 gl)
        {
            this.gl = gl;
        };

        @Override
        public void error(int errorCode)
        {
            throw new RuntimeException("DrawVisibilityCircle.TesselatorCB: Error #" + errorCode);
        }

        @Override
        public void begin(int type)
        {
            gl.glBegin(type);
        }

        @Override
        public void end()
        {
            gl.glEnd();
        }

        @Override
        public void vertex(Object data)
        {
            gl.glVertex3dv((double[]) data, 0);
        }

        @Override
        public void combine(double[] coords, Object[] data, float[] weight, Object[] outData)
        {
            outData[0] = coords;
        }
    }

    public DrawVisibilityCircle(GL2 gl, TextRenderer textRenderer, MapViewParameters mapParams, VisibleElementColor color, double opaque)
    {
        this.gl = gl;
        this.textRenderer = textRenderer;
        this.mapParams = mapParams;
        this.r = (byte) color.r;
        this.g = (byte) color.g;
        this.b = (byte) color.b;
        this.opaque = toGLByte(opaque);
    }

    public void draw(FlatMapPosition element, List<FlatMapPosition> circle)
    {
        List<Vertex> part1 = new ArrayList<>();
        List<Vertex> part2 = new ArrayList<>();

        prepareCircle(element, circle, part1, part2);

        if (!part1.isEmpty()) {
            drawVertices(part1);
        }

        if (!part2.isEmpty()) {
            drawVertices(part2);
        }
    }

    private void prepareCircle(FlatMapPosition element, List<FlatMapPosition> circle, List<Vertex> out1, List<Vertex> out2)
    {
        // convert all visibility circle points into Vertex objects
        List<Vertex> vertices = new ArrayList<>(circle.size());
        for (FlatMapPosition point : circle) {
            vertices.add(new Vertex(point.X, point.Y));
        }

        // we need to know in which hemisphere the current element is located
        Vertex vElement = new Vertex(element.X, element.Y);

        boolean touchesNorthPole = isTouchingNorthPole(vElement, vertices);
        boolean touchesSouthPole = isTouchingSouthPole(vElement, vertices);


        if (!touchesNorthPole && !touchesSouthPole) {
            prepareFreePolygons(vertices, out1, out2);

//            MapPrimitives painter = new MapPrimitives(gl, mapParams);
//            painter.beginLine(1.5, true);
//            for (Vertex p : out1) {
//                painter.addLineVertex(p.x, p.y, new VisibleElementColor(r, g, b), opaque);
//            }
//            painter.endLine();
//
//            painter.beginLine(1.5, true);
//            for (Vertex p : out2) {
//                painter.addLineVertex(p.x, p.y, new VisibleElementColor(r, g, b), opaque);
//            }
//            painter.endLine();
        } else {
            preparePolePolygons(vertices, out1, touchesNorthPole);
        }
    }

    private void preparePolePolygons(List<Vertex> vertices, List<Vertex> out, boolean northPole)
    {
//        MapPrimitives painter = new MapPrimitives(gl, mapParams);
//        painter.beginLine(1.5, false);

        boolean twoPartPolygon = false;

        for (int i = 0; i < vertices.size(); i++)
        {
            Vertex vCurrent = vertices.get(i);

            if (out.isEmpty())
            {
                // make sure there is at least one vertex inside the list
                out.add(vCurrent);
//                painter.addLineVertex(vCurrent.x, vCurrent.y, new VisibleElementColor(r, g, b), opaque);
                continue;
            }

            Vertex vPrev = vertices.get(i - 1);
            if (!isValidXDistance(vPrev, vCurrent))
            {
                Vertex vNew = projectXVertex(vPrev, vCurrent);
                out.add(vNew);
//                painter.addLineVertex(vNew.x, vNew.y, new VisibleElementColor(r, g, b), opaque);


                // close the current polygon and open the second polygon
                if (northPole) {
                    out.add(new Vertex(vNew.x, 0));
//                    painter.addLineVertex(vNew.x, 0, new VisibleElementColor(r, g, b), opaque);
//                    painter.endLine();

//                    painter.beginLine(1.5, false);
//                    painter.addLineVertex(vCurrent.x, 0, new VisibleElementColor(r, g, b), opaque);
                    out.add(new Vertex(vCurrent.x, 0));
                } else {
                    out.add(new Vertex(vNew.x, mapParams.mapHeight));
//                    painter.addLineVertex(vNew.x, mapParams.mapHeight, new VisibleElementColor(r, g, b), opaque);
//                    painter.endLine();

//                    painter.beginLine(1.5, false);
//                    painter.addLineVertex(vCurrent.x, mapParams.mapHeight, new VisibleElementColor(r, g, b), opaque);
                    out.add(new Vertex(vCurrent.x, mapParams.mapHeight));
                }

                twoPartPolygon = true;
            }
            out.add(vCurrent);
//            painter.addLineVertex(vCurrent.x, vCurrent.y, new VisibleElementColor(r, g, b), opaque);
        }
//        painter.endLine();

        if (!twoPartPolygon)
        {
            if (northPole) {
                out.add(0, new Vertex(out.get(0).x, 0));
                out.add(new Vertex(out.get(out.size() - 1).x, 0));
            } else {
                out.add(0, new Vertex(out.get(0).x, mapParams.mapHeight));
                out.add(new Vertex(out.get(out.size() - 1).x, mapParams.mapHeight));
            }

//            painter.beginLine(1.5, false);
//            painter.addLineVertex(out.get(0).x, out.get(0).y, new VisibleElementColor(r, g, b), opaque);
//            painter.addLineVertex(out.get(1).x, out.get(1).y, new VisibleElementColor(r, g, b), opaque);
//            painter.endLine();
//
//            painter.beginLine(1.5, false);
//            painter.addLineVertex(out.get(out.size() - 2).x, out.get(out.size() - 2).y, new VisibleElementColor(r, g, b), opaque);
//            painter.addLineVertex(out.get(out.size() - 1).x, out.get(out.size() - 1).y, new VisibleElementColor(r, g, b), opaque);
//            painter.endLine();
        }
    }

    private void prepareFreePolygons(List<Vertex> vertices, List<Vertex> out1, List<Vertex> out2)
    {
        List<Vertex> currentList = out1;
        for (Vertex v : vertices)
        {
            if (currentList.isEmpty()) {
                currentList.add(v);
                continue;
            }

            Vertex vLast = currentList.get(currentList.size() - 1);
            if (!isValidXDistance(vLast, v))
            {
                Vertex vNew = projectXVertex(vLast, v);
                currentList.add(vNew);

                currentList = (currentList == out1) ? out2 : out1;
            }
            currentList.add(v);
        }
    }

    private boolean isTouchingNorthPole(Vertex element, List<Vertex> circle)
    {
        return element.inNorthernHemisphere() &&
               (element.mapX != circle.get(0).mapX);
//               ((circle.get(0).mapX > circle.get(1).mapX) ||
//                (circle.get(0).mapX < circle.get(circle.size() - 1).mapX));
    }

    private boolean isTouchingSouthPole(Vertex element, List<Vertex> circle)
    {
//        System.out.println("sat=" + element + " mid=" + circle.get(circle.size() / 2));
//        return element.inSouthernHemisphere() &&
//               ((circle.get(circle.size() / 2).mapX > circle.get(circle.size() / 2 - 1).mapX) ||
//                (circle.get(circle.size() / 2).mapX < circle.get(circle.size() / 2 + 1).mapX));

        return element.inSouthernHemisphere() &&
               (element.mapX != circle.get(circle.size() / 2).mapX);
    }

    private boolean isValidXDistance(Vertex v1, Vertex v2)
    {
       return isValidXDistance(getXDistance(v1, v2));
    }

    private boolean isValidXDistance(int distance)
    {
        return distance < (mapParams.mapWidth / 2);
    }

    private Vertex projectXVertex(Vertex vCurrent, Vertex vNew)
    {
        if (vCurrent.inWesternHemisphere() && vNew.inEasternHemisphere())
        {
            // the current vertex is on the left of the map, but the new vertex
            // is on the right edge. adjust the X coordinate.
            return new Vertex(0, vNew.y);
        }

        if (vCurrent.inEasternHemisphere() && vNew.inWesternHemisphere())
        {
            // the current vertex is on the right of the map, but the new vertex
            // is on the left edge. adjust the X coordinate.
            return new Vertex(mapParams.mapWidth, vNew.y);
        }

        return null; // no adjustment necessary
    }

    private int getXDistance(Vertex v1, Vertex v2)
    {
        return Math.max(v1.x, v2.x) - Math.min(v1.x, v2.x);
    }

    private void drawVertices(List<Vertex> vertices)
    {
        double[][] polygon = new double[vertices.size()][3];
        for (int i = 0; i < vertices.size(); i++)
        {
            polygon[i][0] = vertices.get(i).mapX;
            polygon[i][1] = vertices.get(i).mapY;
            polygon[i][2] = 0.0;
        }


        GLUtessellator tessellator = GLU.gluNewTess();
        TesselatorCB tessellatorCB = new TesselatorCB(gl);

        GLU.gluTessCallback(tessellator, GLU.GLU_TESS_VERTEX, tessellatorCB);
        GLU.gluTessCallback(tessellator, GLU.GLU_TESS_BEGIN, tessellatorCB);
        GLU.gluTessCallback(tessellator, GLU.GLU_TESS_END, tessellatorCB);
        GLU.gluTessCallback(tessellator, GLU.GLU_TESS_ERROR, tessellatorCB);
        GLU.gluTessCallback(tessellator, GLU.GLU_TESS_COMBINE, tessellatorCB);

        int startList = gl.glGenLists(2);
        gl.glNewList(startList, GL2.GL_COMPILE);
            GLU.gluTessProperty(tessellator, GLU.GLU_TESS_WINDING_RULE, GLU.GLU_TESS_WINDING_ODD);
            GLU.gluTessBeginPolygon(tessellator, null);
                GLU.gluTessBeginContour(tessellator);
                for (double[] polyVertices : polygon) {
                    GLU.gluTessVertex(tessellator, polyVertices, 0, polyVertices);
                }
                GLU.gluTessEndContour(tessellator);
            GLU.gluTessEndPolygon(tessellator);
        gl.glEndList();


        gl.glNewList(startList + 1, GL2.GL_COMPILE);
            GLU.gluTessProperty(tessellator, GLU.GLU_TESS_WINDING_RULE, GLU.GLU_TESS_WINDING_ODD);
            GLU.gluTessProperty(tessellator, GLU.GLU_TESS_BOUNDARY_ONLY, GLU.GLU_TRUE);
            GLU.gluTessBeginPolygon(tessellator, null);
                GLU.gluTessBeginContour(tessellator);
                for (double[] polyVertices : polygon) {
                    GLU.gluTessVertex(tessellator, polyVertices, 0, polyVertices);
                }
                GLU.gluTessEndContour(tessellator);
            GLU.gluTessEndPolygon(tessellator);
        gl.glEndList();

        gl.glColor4ub(r, g, b, opaque);
        gl.glCallList(startList);

        gl.glColor3ub(r, g, b);
        gl.glLineWidth(0.75f);
        gl.glCallList(startList + 1);

        gl.glDeleteLists(startList, 2);
        GLU.gluDeleteTess(tessellator);


//        MapPrimitives painter = new MapPrimitives(gl, mapParams);
//        painter.beginLine(1.5, false);
//        for (Vertex p : vertices) {
//            painter.addLineVertex(p.x, p.y, new VisibleElementColor(r, g, b), opaque);
//        }
//        painter.endLine();

//        for (Vertex p : vertices) {
//            painter.drawPoint(p.x, p.y, 2.0, new VisibleElementColor(r, g, b), 0.25, true);
//        }

//        int i = 0;
//        for (Vertex p : vertices)
//        {
//            GLUtils.drawText(textRenderer,
//                             mapParams.clientAreaWidth,
//                             mapParams.clientAreaHeight,
//                             p.mapX,
//                             p.mapY,
//                             i++ + "",//" (" + projectedPoint.toString() + ")",
//                             true);
//        }

//        int j = 0;
//        for (Vertex p : vertices2)
//        {
//            GLUtils.drawText(textRenderer,
//                             mapParams.clientAreaWidth,
//                             mapParams.clientAreaHeight,
//                             p.mapX,
//                             p.mapY,
//                             j++ + "",//" (" + projectedPoint.toString() + ")",
//                             true);
//        }
    }

    private Quadrant getQuadrant(int x, int y)
    {
        int westBorder = mapParams.mapWidth / 2;
        int northBorder = mapParams.mapHeight / 2;

        if ((x <= westBorder) && (y <= northBorder)) {
            return Quadrant.NW;
        } else if ((x <= westBorder) && (y > northBorder)) {
            return Quadrant.SW;
        } else if ((x > westBorder) && (y <= northBorder)) {
            return Quadrant.NE;
        } else if ((x > westBorder) && (y > northBorder)) {
            return Quadrant.SE;
        }

        throw new IllegalStateException("Unknown Quadrant.");
    }

    private byte toGLByte(double d)
    {
        return (byte) (d * 255.0);
    }
}
