package maru.map.views.swt;

import java.util.ArrayList;
import java.util.Map;
import java.util.WeakHashMap;

import maru.centralbody.projection.EquirectangularCoordinate;
import maru.core.model.ICoordinate;
import maru.core.model.IGroundstation;
import maru.core.model.IPropagator;
import maru.core.model.IScenarioProject;
import maru.core.model.ISpacecraft;
import maru.core.model.IVisibleElement;
import maru.core.utils.EclipseState;
import maru.map.jobs.swt.SWTProjectDrawJob;
import maru.map.views.GroundtrackPoint;
import maru.map.views.GroundtrackRange;
import maru.map.views.MapViewParameters;
import maru.map.views.MapViewSettings;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.RGB;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.errors.OrekitException;

public class ScenarioDrawJob extends SWTProjectDrawJob
{
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
            try
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
                RGB currentColor = selectColor(currentCoordinate, defaultColor, nightColor);

                drawGroundtrack(element, currentGtBarrier, defaultColor, nightColor);
                drawElement(element, mapPos, currentColor);
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

    private void drawGroundtrack(ISpacecraft element, GroundtrackRange barrier, RGB day, RGB night) throws OrekitException
    {
        GC gc = getGC();

        if (getSettings().getAntiAliasing()) {
            gc.setAntialias(SWT.ON);
        }

        Color dayColor = new Color(gc.getDevice(), day);
        Color nightColor = new Color(gc.getDevice(), night);

        gc.setLineWidth(2);

        for (ArrayList<GroundtrackPoint> lineStrip : getGroundtrack(element, barrier))
        {
            GroundtrackPoint lastPoint = null;

            for (GroundtrackPoint gtPoint : lineStrip)
            {
                if (lastPoint == null) {
                    lastPoint = gtPoint;
                    continue;
                }

                if (gtPoint.day) {
                    gc.setForeground(dayColor);
                    gc.setBackground(dayColor);
                } else {
                    gc.setForeground(nightColor);
                    gc.setBackground(nightColor);
                }

                drawLineOnMap(lastPoint.X, lastPoint.Y, gtPoint.X, gtPoint.Y);
                lastPoint = gtPoint;
            }
        }

        nightColor.dispose();
        dayColor.dispose();

        if (getSettings().getAntiAliasing()) {
            gc.setAntialias(SWT.OFF);
        }
    }

    private void drawElement(IVisibleElement element, EquirectangularCoordinate mapPos, RGB color)
    {
        GC gc = getGC();

        if (getSettings().getAntiAliasing()) {
            gc.setAntialias(SWT.ON);
        }

        Color currentColor = new Color(gc.getDevice(), color);

        gc.setForeground(currentColor);
        gc.setBackground(currentColor);

        int iconSize = getElementIconSize(element);
        drawOvalOnMap(mapPos.X, mapPos.Y, iconSize, iconSize);
        drawStringOnMap(mapPos.X + iconSize / 2, mapPos.Y, element.getElementName());

        currentColor.dispose();

        if (getSettings().getAntiAliasing()) {
            gc.setAntialias(SWT.OFF);
        }
    }

    private EquirectangularCoordinate getMapPosition(GeodeticPoint point)
    {
        return getProjector().project(point);
    }

    private EquirectangularCoordinate getMapPosition(ICoordinate coordinate) throws OrekitException
    {
        return getProjector().project(coordinate);
    }

    private ArrayList<ArrayList<GroundtrackPoint>> getGroundtrack(ISpacecraft element, GroundtrackRange barrier) throws OrekitException
    {
        MapViewParameters area = getParameters();
        MapViewSettings drawing = getSettings();

        ArrayList<ArrayList<GroundtrackPoint>> elementLineStrips = new ArrayList<>();

        IPropagator propagator = element.getPropagator();
        EquirectangularCoordinate lastMapPos = null;
        ArrayList<GroundtrackPoint> lineStrip = new ArrayList<>();

        for (ICoordinate coordinate : propagator.getCoordinates(element, barrier.getStart(), barrier.getStop(), drawing.getGroundtrackStepSize()))
        {
            EquirectangularCoordinate mapPos = getMapPosition(coordinate);
            boolean inDaylight = !drawing.getShowShadowTimes() ? true : !inShadow(coordinate);

            if (lastMapPos == null)
            {
                lineStrip.add(new GroundtrackPoint(mapPos.X, mapPos.Y, inDaylight));

                lastMapPos = mapPos;
                continue;
            }

            if (Math.abs(mapPos.X - lastMapPos.X) > (area.mapWidth / 2))
            {
                elementLineStrips.add(lineStrip);

                lineStrip = new ArrayList<>();
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

    private int getElementIconSize(IVisibleElement element)
    {
        MapViewParameters area = getParameters();

        if ((getSelectedElement() != null) && (element == getSelectedElement().getUnderlyingElement())) {
            return (int) (1.5 * area.iconSize);
        } else {
            return area.iconSize;
        }
    }

    private boolean inShadow(ICoordinate coordinate)
    {
        return coordinate.getEclipseState() != EclipseState.None;
    }

    private RGB selectColor(ICoordinate coordinate, final RGB day, final RGB night)
    {
        return inShadow(coordinate) ? night : day;
    }

    private RGB toNightRGB(RGB color)
    {
        return new RGB(color.red / 2, color.green / 2, color.blue / 2);
    }

    private void drawOvalOnMap(int x, int y, int width, int height)
    {
        MapViewParameters params = getParameters();
        getGC().fillOval(x + params.mapX - (width / 2), y + params.mapY - (height / 2), width, height);
    }

    private void drawStringOnMap(int x, int y, String str)
    {
        MapViewParameters params = getParameters();
        getGC().drawString(str, x + params.mapX, y + params.mapY, true);
    }

    private void drawLineOnMap(int x1, int y1, int x2, int y2)
    {
        MapViewParameters params = getParameters();
        getGC().drawLine(x1 + params.mapX, y1 + params.mapY, x2 + params.mapX, y2 + params.mapY);
    }
}