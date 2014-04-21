package maru.map.views.gl.jobs;

import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL2;

import maru.core.model.VisibleElementColor;
import maru.map.views.MapViewParameters;

public class MapPrimitives
{
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
        VisibleElementColor color;
        byte opaque;
        Quadrant quadrant;

        int mapX;
        int mapY;

        public Vertex(int x, int y, VisibleElementColor color, byte opaque)
        {
            this.x = x;
            this.y = y;
            this.color = color;
            this.opaque = opaque;
            this.quadrant = getQuadrant(x, y);

            this.mapX = params.mapX + x;
            this.mapY = params.mapHeight - (y - params.mapY);
        }

        @Override
        public String toString()
        {
            return "vert(" + x + "," + y + "," + quadrant.toString() + "); map(" + mapX + "," + mapY + ")";
        }
    }

    private final GL2 gl;
    private final MapViewParameters params;

    private final List<Vertex> vertices;

    public MapPrimitives(GL2 gl, MapViewParameters parameters)
    {
        this.gl = gl;
        this.params = parameters;
        this.vertices = new ArrayList<>();
    }

    public void drawPoint(int x, int y, double width, VisibleElementColor color, double opaque, boolean outline)
    {
        gl.glPushAttrib(GL2.GL_POINT_BIT);

        if (outline)
        {
            gl.glPointSize((float) width + 2.0f);
            gl.glColor3f(0.0f, 0.0f, 0.0f);
            drawPoint(x, y);
        }

        gl.glPointSize((float) width);
        gl.glColor4ub((byte) color.r, (byte) color.g, (byte) color.b, toGLByte(opaque));
        drawPoint(x, y);

        gl.glPopAttrib();
    }

    public void drawLine(int srcX, int srcY, int dstX, int dstY, double width, VisibleElementColor color, double opaque)
    {
        gl.glPushAttrib(GL2.GL_LINE_BIT);
        gl.glLineWidth((float) width);
        gl.glColor4ub((byte) color.r, (byte) color.g, (byte) color.b, toGLByte(opaque));

        Vertex vSrc = new Vertex(srcX, srcY, null, (byte) 0);
        Vertex vDst = new Vertex(dstX, dstY, null, (byte) 0);

        gl.glBegin(GL2.GL_LINES);
        gl.glVertex2d(vSrc.mapX, vSrc.mapY);
        gl.glVertex2d(vDst.mapX, vDst.mapY);
        gl.glEnd();

        gl.glPopAttrib();
    }

    public void beginLine(double width, boolean loop)
    {
        gl.glPushAttrib(GL2.GL_LINE_BIT);
        gl.glLineWidth((float) width);

        if (loop) {
            gl.glBegin(GL2.GL_LINE_LOOP);
        } else {
            gl.glBegin(GL2.GL_LINE_STRIP);
        }
    }

    public void beginLine(double width, int pattern, boolean loop)
    {
        gl.glPushAttrib(GL2.GL_LINE_BIT);
        gl.glLineWidth((float) width);

        gl.glLineStipple(1, (short) pattern);
        gl.glEnable(GL2.GL_LINE_STIPPLE);

        if (loop) {
            gl.glBegin(GL2.GL_LINE_LOOP);
        } else {
            gl.glBegin(GL2.GL_LINE_STRIP);
        }
    }

    public void endLine()
    {
        flushVertices();
        gl.glEnd();

        gl.glPopAttrib();
    }

    public void addLineVertex(int x, int y, VisibleElementColor color, double opaque)
    {
        Vertex vNew = new Vertex(x, y, color, toGLByte(opaque));
        Vertex vCurrent = getCurrentVertex();

        if (vCurrent == null) {
            vertices.add(vNew);
            return;
        }

        if ((vNew != null) && !isValidXDistance(vCurrent, vNew)) {
            vNew = projectXVertex(vCurrent, vNew);
        }

        if ((vNew != null) && !isValidYDistance(vCurrent, vNew)) {
            vNew = projectYVertex(vCurrent, vNew);
        }

        if (vNew != null) {
            vertices.add(vNew);
        }
    }

    public void beginPolygon()
    {
        gl.glPushAttrib(GL2.GL_POLYGON_BIT);

        gl.glBegin(GL2.GL_POLYGON);
    }

    public void endPolygon()
    {
        flushVertices();
        gl.glEnd();

        gl.glPopAttrib();
    }

    public void addPolygonVertex(int x, int y, VisibleElementColor color, double opaque)
    {
        Vertex vNew = new Vertex(x, y, color, toGLByte(opaque));
        Vertex vCurrent = getCurrentVertex();

        if (vCurrent == null) {
            vertices.add(vNew);
            return;
        }

        if ((vNew != null) && !isValidXDistance(vCurrent, vNew)) {
            vNew = projectXVertex(vCurrent, vNew);
        }

        if ((vNew != null) && !isValidYDistance(vCurrent, vNew)) {
            vNew = projectYVertex(vCurrent, vNew);
        }

        if (vNew != null) {
            vertices.add(vNew);
        }
    }

    private void flushVertices()
    {
//        if (vertices.size() > 3)
//        {
//            Vertex first = vertices.get(0);
//            Vertex second = vertices.get(1);
//            Vertex last = vertices.get(vertices.size() - 1);
//
//            if (((first.mapX > second.mapX) && (first.mapX > last.mapX)) ||
//                ((first.mapX < second.mapX) && (first.mapX < last.mapX))) {
//                first.mapX = last.mapX + (second.mapX - last.mapX) / 2;
//            }
//        }

        for (Vertex v : vertices) {
            gl.glColor4ub((byte) v.color.r, (byte) v.color.g, (byte) v.color.b, v.opaque);
            gl.glVertex2d(v.mapX, v.mapY);
        }
        vertices.clear();
    }

    private void drawPoint(int x, int y)
    {
        Vertex v = new Vertex(x, y, null, (byte) 0);

        gl.glBegin(GL2.GL_POINTS);
        gl.glVertex2d(v.mapX, v.mapY);
        gl.glEnd();
    }

    private boolean isValidXDistance(Vertex v1, Vertex v2)
    {
       return isValidXDistance(getXDistance(v1, v2));
    }

    private boolean isValidYDistance(Vertex v1, Vertex v2)
    {
        return isValidYDistance(getYDistance(v1, v2));
    }

    private boolean isValidXDistance(int distance)
    {
        return distance < (params.mapWidth / 2);
    }

    private boolean isValidYDistance(int distance)
    {
        return distance < (params.mapHeight / 2);
    }

    private Vertex projectXVertex(Vertex vCurrent, Vertex vNew)
    {
        if (((vCurrent.quadrant == Quadrant.NW) || (vCurrent.quadrant == Quadrant.SW)) &&
            ((    vNew.quadrant == Quadrant.NE) || (    vNew.quadrant == Quadrant.SE)))
        {
            // the current vertex is on the left of the map, but the new vertex
            // is on the right edge. adjust the X coordinate.
            return new Vertex(0, vNew.y, vNew.color, vNew.opaque);
        }

        if (((vCurrent.quadrant == Quadrant.NE) || (vCurrent.quadrant == Quadrant.SE)) &&
            ((    vNew.quadrant == Quadrant.NW) || (    vNew.quadrant == Quadrant.SW)))
        {
            // the current vertex is on the right of the map, but the new vertex
            // is on the left edge. adjust the X coordinate.
            return new Vertex(params.mapWidth, vNew.y, vNew.color, vNew.opaque);
        }

//        System.out.println("ERROR projectXVertex: from{" + vCurrent.toString() + "} to {" + vNew.toString() + "}");
        return null;
    }

    private Vertex projectYVertex(Vertex vCurrent, Vertex vNew)
    {
        if (((vCurrent.quadrant == Quadrant.NW) || (vCurrent.quadrant == Quadrant.NE)) &&
            ((    vNew.quadrant == Quadrant.SW) || (    vNew.quadrant == Quadrant.SE)))
        {
            // the current vertex is on the top of the map, but the new vertex
            // is on the bottom edge. adjust the Y coordinate.
            return new Vertex(vNew.x, 0, vNew.color, vNew.opaque);
        }

        if (((vCurrent.quadrant == Quadrant.SW) || (vCurrent.quadrant == Quadrant.SE)) &&
            ((    vNew.quadrant == Quadrant.NW) || (    vNew.quadrant == Quadrant.NE)))
        {
            // the current vertex is on the bottom of the map, but the new vertex
            // is on the top edge. adjust the Y coordinate.
            return new Vertex(vNew.x, params.mapHeight, vNew.color, vNew.opaque);
        }

//        System.out.println("ERROR projectYVertex: from{" + vCurrent.toString() + "} to {" + vNew.toString() + "}");
        return null;
    }

    private Vertex getCurrentVertex()
    {
        if (vertices.isEmpty()) {
            return null;
        }
        return vertices.get(vertices.size() - 1);
    }

    private Quadrant getQuadrant(int x, int y)
    {
        int westBorder = params.mapWidth / 2;
        int northBorder = params.mapHeight / 2;

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

    private int getXDistance(Vertex v1, Vertex v2)
    {
        return Math.max(v1.x, v2.x) - Math.min(v1.x, v2.x);
    }

    private int getYDistance(Vertex v1, Vertex v2)
    {
        return Math.max(v1.y, v2.y) - Math.min(v1.y, v2.y);
    }

    private byte toGLByte(double d)
    {
        return (byte) (d * 255.0);
    }

//    private int getDistance(Vertex v1, Vertex v2)
//    {
//        return (int) Math.sqrt(Math.pow(v1.x - v1.x, 2) + Math.pow(v2.x - v2.x, 2));
//    }
}
