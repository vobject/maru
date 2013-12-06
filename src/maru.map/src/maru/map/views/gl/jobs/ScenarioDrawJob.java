package maru.map.views.gl.jobs;

import java.util.ArrayList;
import java.util.Map;
import java.util.WeakHashMap;

import javax.media.opengl.GL;

import maru.IMaruResource;
import maru.MaruException;
import maru.centralbody.projection.EquirectangularCoordinate;
import maru.centralbody.projection.ICoordinateProjector;
import maru.core.model.ICoordinate;
import maru.core.model.IPropagatable;
import maru.core.model.IPropagator;
import maru.core.model.IScenarioProject;
import maru.core.model.ISpacecraft;
import maru.map.jobs.gl.GLProjectDrawJob;
import maru.map.views.GroundtrackBarrier;
import maru.map.views.GroundtrackPoint;
import maru.map.views.MapViewParameters;
import maru.map.views.MapViewSettings;
import maru.map.views.gl.GLUtils;
import maru.map.views.gl.TextureCache;

import org.eclipse.swt.graphics.RGB;

import com.sun.opengl.util.texture.Texture;

public class ScenarioDrawJob extends GLProjectDrawJob
{
    private final Map<IPropagatable, GroundtrackBarrier> gtBarriers;

    public ScenarioDrawJob()
    {
        gtBarriers = new WeakHashMap<>();
    }

    @Override
    public void draw()
    {
        MapViewParameters area = getParameters();
        MapViewSettings drawing = getSettings();

        IScenarioProject scenarioProject = getProject().getUnderlyingElement();

        getProjector().setCentralBody(scenarioProject.getCentralBody());
        getProjector().setMapSize(area.mapWidth, area.mapHeight);

        for (IPropagatable element : scenarioProject.getPropagatables())
        {
            ICoordinate currentCoordinate = element.getCurrentCoordinate();

            GroundtrackBarrier currentGtBarrier;
            if (!gtBarriers.containsKey(element)) {
                currentGtBarrier = new GroundtrackBarrier(currentCoordinate.getTime(), drawing.getGroundtrackLength());
                gtBarriers.put(element, currentGtBarrier);
            } else {
                currentGtBarrier = gtBarriers.get(element);
                currentGtBarrier.update(currentCoordinate.getTime(), drawing.getGroundtrackLength());
            }

            RGB defaultColor = element.getElementColor();
            RGB nightColor = toNightRGB(element.getElementColor());
            RGB currentColor = selectColor(element, currentCoordinate, defaultColor, nightColor);

            drawGroundtrack(element, currentGtBarrier, defaultColor, nightColor);
            drawElement(element, currentCoordinate, currentColor);
        }
    }

    @Override
    public void dispose()
    {
        // this job does not own any resources
    }

    private void drawGroundtrack(IPropagatable element, GroundtrackBarrier barrier, RGB day, RGB night)
    {
        GL gl = getGL();
        MapViewParameters area = getParameters();

        gl.glLineWidth(2.0f);

        for (ArrayList<GroundtrackPoint> lineStrips : getGroundtrack(element, barrier))
        {
            gl.glBegin(GL.GL_LINE_STRIP);

            for (GroundtrackPoint lineStrip : lineStrips)
            {
                if (lineStrip.day) {
                    gl.glColor3ub((byte) day.red, (byte) day.green, (byte) day.blue);
                } else {
                    gl.glColor3ub((byte) night.red, (byte) night.green, (byte) night.blue);
                }

                gl.glVertex2d(area.mapX + lineStrip.X, area.mapHeight - (lineStrip.Y - area.mapY));
            }

            gl.glEnd();
        }
    }

    private void drawElement(IPropagatable element, ICoordinate coordinate, RGB color)
    {
        EquirectangularCoordinate mapPos;
        try {
            mapPos = getProjector().project(coordinate);
        } catch (MaruException e) {
            e.printStackTrace();
            return;
        }

        GL gl = getGL();
        MapViewParameters area = getParameters();

        gl.glColor3ub((byte) color.red, (byte) color.green, (byte) color.blue);

        int iconSize = getElementIconSize(element);
        IMaruResource resource = element.getElementGraphic2D();

        if ((resource != null ) && !resource.getPath().isEmpty())
        {
            String graphic2dPath = element.getElementGraphic2D().getPath();
            drawElementTexture(graphic2dPath, mapPos, iconSize);
        }
        else
        {
            // fallback for when we cannot load an image for the element
            iconSize /= 2;
            drawElementFallback(mapPos, iconSize);
        }

        // draw the element name no matter if we are in fallback mode or not
        GLUtils.drawText(getTextRenderer(),
                         area.clientAreaWidth,
                         area.clientAreaHeight,
                         area.mapX + mapPos.X + (iconSize / 2),
                         area.mapHeight - (mapPos.Y - area.mapY),
                         element.getElementName(),
                         true);
    }

    private ArrayList<ArrayList<GroundtrackPoint>> getGroundtrack(IPropagatable element, GroundtrackBarrier barrier)
    {
        MapViewParameters area = getParameters();
        MapViewSettings drawing = getSettings();
        ICoordinateProjector projector = getProjector();

        ArrayList<ArrayList<GroundtrackPoint>> elementLineStrips = new ArrayList<>();

        IPropagator propagator = element.getPropagator();
        EquirectangularCoordinate lastMapPos = null;
        ArrayList<GroundtrackPoint> lineStrip = new ArrayList<>();

        for (ICoordinate coordinate : propagator.getCoordinates(element, barrier.getStart(), barrier.getStop(), drawing.getGroundtrackStepSize()))
        {
            EquirectangularCoordinate mapPos;
            try {
                mapPos = projector.project(coordinate);
            } catch (MaruException e) {
                e.printStackTrace();
                continue;
            }

            if (lastMapPos == null)
            {
                lineStrip.add(new GroundtrackPoint(mapPos.X, mapPos.Y, !drawing.getShowShadowTimes() ? true : !inShadow(element, coordinate)));

                lastMapPos = mapPos;
                continue;
            }

            if (Math.abs(mapPos.X - lastMapPos.X) > (area.mapWidth / 2))
            {
                elementLineStrips.add(lineStrip);

                lineStrip = new ArrayList<>();
                lineStrip.add(new GroundtrackPoint(mapPos.X, mapPos.Y, !drawing.getShowShadowTimes() ? true : !inShadow(element, coordinate)));

                lastMapPos = mapPos;
                continue;
            }

            lineStrip.add(new GroundtrackPoint(mapPos.X, mapPos.Y, !drawing.getShowShadowTimes() ? true : !inShadow(element, coordinate)));
            lastMapPos = mapPos;
        }
        elementLineStrips.add(lineStrip);

        return elementLineStrips;
    }

    private int getElementIconSize(IPropagatable element)
    {
        MapViewParameters area = getParameters();

        if ((getSelectedElement() != null) && element == getSelectedElement().getUnderlyingElement()) {
            return (int) (1.5 * area.iconSize);
        } else {
            return area.iconSize;
        }
    }

    private boolean inShadow(IPropagatable element, ICoordinate coordinate)
    {
        if (element instanceof ISpacecraft) {
            return ((ISpacecraft) element).inUmbraOrPenumbra(coordinate);
        } else {
            // there is currently nothing in the groundstation interface that
            // allows to ask if it is day or night.
            return false;
        }
    }

    private void drawElementTexture(String graphic2dPath, EquirectangularCoordinate mapPos, int iconSize)
    {
        GL gl = getGL();
        MapViewParameters area = getParameters();
        TextureCache textureCache = getTextureCache();

        Texture elementGraphic2D = textureCache.get(graphic2dPath);

        elementGraphic2D.enable();
        elementGraphic2D.bind();

        gl.glBegin(GL.GL_QUADS);
            gl.glTexCoord2d(0.0, 1.0);
            gl.glVertex2d(area.mapX + mapPos.X - (iconSize / 2), area.mapHeight - (mapPos.Y - area.mapY) - (iconSize / 2));

            gl.glTexCoord2d(1.0, 1.0);
            gl.glVertex2d(area.mapX + mapPos.X + (iconSize / 2), area.mapHeight - (mapPos.Y - area.mapY) - (iconSize / 2));

            gl.glTexCoord2d(1.0, 0.0);
            gl.glVertex2d(area.mapX + mapPos.X + (iconSize / 2), area.mapHeight - (mapPos.Y - area.mapY) + (iconSize / 2));

            gl.glTexCoord2d(0.0, 0.0);
            gl.glVertex2d(area.mapX + mapPos.X - (iconSize / 2), area.mapHeight - (mapPos.Y - area.mapY) + (iconSize / 2));
        gl.glEnd();

        elementGraphic2D.disable();
    }

    private void drawElementFallback(EquirectangularCoordinate mapPos, int iconSize)
    {
        GL gl = getGL();
        MapViewParameters area = getParameters();

        gl.glPointSize(iconSize);
        gl.glBegin(GL.GL_POINTS);
            gl.glVertex2d(area.mapX + mapPos.X, area.mapHeight - (mapPos.Y - area.mapY));
        gl.glEnd();
    }

    private RGB selectColor(IPropagatable element, ICoordinate coordinate, RGB day, RGB night)
    {
        return inShadow(element, coordinate) ? night : day;
    }

    private RGB toNightRGB(RGB color)
    {
        return new RGB(color.red / 2, color.green / 2, color.blue / 2);
    }
}