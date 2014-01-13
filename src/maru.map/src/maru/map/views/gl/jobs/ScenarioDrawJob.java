package maru.map.views.gl.jobs;

import java.util.ArrayList;
import java.util.Map;
import java.util.WeakHashMap;

import javax.media.opengl.GL2;

import maru.IMaruResource;
import maru.centralbody.projection.EquirectangularCoordinate;
import maru.core.model.ICentralBody;
import maru.core.model.ICoordinate;
import maru.core.model.IGroundstation;
import maru.core.model.IPropagator;
import maru.core.model.IScenarioProject;
import maru.core.model.ISpacecraft;
import maru.core.model.IVisibleElement;
import maru.core.utils.OrekitUtils;
import maru.map.jobs.gl.GLProjectDrawJob;
import maru.map.views.GroundtrackPoint;
import maru.map.views.GroundtrackRange;
import maru.map.views.MapViewParameters;
import maru.map.views.MapViewSettings;
import maru.map.views.gl.GLUtils;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.eclipse.swt.graphics.RGB;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.errors.OrekitException;
import org.orekit.frames.Frame;
import org.orekit.time.AbsoluteDate;

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

        IScenarioProject scenarioProject = getProject().getUnderlyingElement();

        getProjector().setCentralBody(scenarioProject.getCentralBody());
        getProjector().setMapSize(area.mapWidth, area.mapHeight);

        for (IGroundstation element : scenarioProject.getGroundstations())
        {
            GeodeticPoint position = element.getGeodeticPosition();
            EquirectangularCoordinate mapPos = getMapPosition(position);
            RGB defaultColor = element.getElementColor();

            drawElement(element, mapPos, defaultColor);
        }

        for (ISpacecraft element : scenarioProject.getSpacecrafts())
        {
            ICoordinate currentCoordinate = element.getCurrentCoordinate();
            EquirectangularCoordinate mapPos = getMapPosition(currentCoordinate);
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

            RGB defaultColor = element.getElementColor();
            RGB nightColor = toNightRGB(element.getElementColor());
            RGB currentColor = selectColor(element, currentCoordinate, defaultColor, nightColor);

            drawGroundtrack(element, currentGtBarrier, defaultColor, nightColor);
            drawElement(element, mapPos, currentColor);

            // centralbody & currentCoordinate ---> distanceToHorizon
            // groundstation & centralbody & satellite ---> distanceToGroundstation
            // distanceToHorizon & distanceToGroundstation ---> visible
            // centralbody & currentCoordinate & groundstation ---> mapPositions

            try
            {
                ICentralBody centralBody = element.getCentralBody();
                Frame centralBodyFrame = centralBody.getFrame();

                Frame elementFrame = currentCoordinate.getFrame();
                AbsoluteDate elementDate = currentCoordinate.getDate();
                Vector3D elementVec = currentCoordinate.getPosition();

                double distanceToHorizon = centralBody.getDistanceToHorizon(currentCoordinate);

                for (IGroundstation gs : scenarioProject.getGroundstations())
                {
                    Vector3D groundstationVec = gs.getCartesianPosition();

                    Vector3D groundstationVecInSatFrame = centralBodyFrame.getTransformTo(elementFrame, elementDate).transformPosition(groundstationVec);

                    double distanceToGroundstation = elementVec.distance(groundstationVecInSatFrame);

                    if (distanceToGroundstation < distanceToHorizon)
                    {
//                        System.out.println("VISIBLE: " + element.getElementName() + "<->" + gs.getElementName() + "; distanceToHorizon=" + distanceToHorizon + " distanceToGroundstation=" + distanceToGroundstation);

                        EquirectangularCoordinate mp1 = getMapPosition(centralBody.getIntersectionPoint(currentCoordinate));
                        EquirectangularCoordinate mp2 = getMapPosition(gs.getGeodeticPosition());

                        GL2 gl = getGL();
                        gl.glBegin(GL2.GL_LINE_STRIP);
                        gl.glVertex2d(area.mapX + mp1.X, area.mapHeight - (mp1.Y - area.mapY));
                        gl.glVertex2d(area.mapX + mp2.X, area.mapHeight - (mp2.Y - area.mapY));
                        gl.glEnd();
                    }
                }

//                GeodeticPoint p = element.getCentralBody().getIntersectionPoint(new Vector3D(currentCoordinate.getPosition().getX() + x, currentCoordinate.getPosition().getY() + x, currentCoordinate.getPosition().getZ() + x), currentCoordinate.getFrame(), currentCoordinate.getDate());
//                EquirectangularCoordinate mp2 = getMapPosition(p);
//                IconSize i = new IconSize();
//                i.x = 8;
//                i.y = 8;
//                drawElementFallback(mp2, i);
            }
            catch (OrekitException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public void dispose()
    {
        // this job does not own any resources
    }

    private void drawGroundtrack(ISpacecraft element, GroundtrackRange barrier, RGB day, RGB night)
    {
        GL2 gl = getGL();
        MapViewParameters area = getParameters();

        gl.glLineWidth(2.0f);

        for (ArrayList<GroundtrackPoint> lineStrips : getGroundtrack(element, barrier))
        {
            gl.glBegin(GL2.GL_LINE_STRIP);

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

    private void drawElement(IVisibleElement element, EquirectangularCoordinate mapPos, RGB color)
    {
        GL2 gl = getGL();
        MapViewParameters area = getParameters();

        gl.glColor3ub((byte) color.red, (byte) color.green, (byte) color.blue);

        IconSize iconSize = getElementIconSize(element);
        IMaruResource resource = element.getElementImage();

        if ((resource != null ) && !resource.getPath().isEmpty())
        {
            drawElementTexture(resource.getPath(), mapPos, iconSize);
        }
        else
        {
            // fallback for when we cannot load an image for the element
            iconSize.x /= 2;
            iconSize.y /= 2;
            drawElementFallback(mapPos, iconSize);
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

    private EquirectangularCoordinate getMapPosition(GeodeticPoint point)
    {
        return getProjector().project(point);
    }

    private EquirectangularCoordinate getMapPosition(ICoordinate coordinate)
    {
        try {
            return getProjector().project(coordinate);
        } catch (OrekitException e) {
            e.printStackTrace();
            return null;
        }
    }

    private ArrayList<ArrayList<GroundtrackPoint>> getGroundtrack(ISpacecraft element, GroundtrackRange barrier)
    {
        MapViewParameters area = getParameters();
        MapViewSettings drawing = getSettings();

        ArrayList<ArrayList<GroundtrackPoint>> elementLineStrips = new ArrayList<>();

        IPropagator propagator = element.getPropagator();
        EquirectangularCoordinate lastMapPos = null;
        ArrayList<GroundtrackPoint> lineStrip = new ArrayList<>();

        for (ICoordinate coordinate : propagator.getCoordinates(element, OrekitUtils.toSeconds(barrier.getStart()), OrekitUtils.toSeconds(barrier.getStop()), drawing.getGroundtrackStepSize()))
        {
            EquirectangularCoordinate mapPos = getMapPosition(coordinate);
            if (mapPos == null) {
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

    private boolean inShadow(ISpacecraft element, ICoordinate coordinate)
    {
        return element.inUmbraOrPenumbra(coordinate);
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

    private void drawElementTexture(String imagePath, EquirectangularCoordinate mapPos, IconSize iconSize)
    {
        GL2 gl = getGL();
        MapViewParameters area = getParameters();
        Texture texture = getTextureCache().get(imagePath);

        GLUtils.drawTexture(gl, texture,
            area.mapX + mapPos.X - (iconSize.x / 2),
            area.mapHeight - (mapPos.Y - area.mapY) - (iconSize.y / 2),
            iconSize.x, iconSize.y);
    }

    private void drawElementFallback(EquirectangularCoordinate mapPos, IconSize iconSize)
    {
        GL2 gl = getGL();
        MapViewParameters area = getParameters();

        gl.glPointSize(Math.max(iconSize.x, iconSize.y));
        gl.glBegin(GL2.GL_POINTS);
            gl.glVertex2d(area.mapX + mapPos.X, area.mapHeight - (mapPos.Y - area.mapY));
        gl.glEnd();
    }

    private RGB selectColor(ISpacecraft element, ICoordinate coordinate, RGB day, RGB night)
    {
        return inShadow(element, coordinate) ? night : day;
    }

    private RGB toNightRGB(RGB color)
    {
        return new RGB(color.red / 2, color.green / 2, color.blue / 2);
    }
}