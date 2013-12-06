package maru.map.views.swt;

import maru.map.jobs.swt.SWTProjectDrawJob;
import maru.map.views.MapViewParameters;

import org.eclipse.swt.graphics.Image;

public class MapDrawJob extends SWTProjectDrawJob
{
    private Image mapImage;

    @Override
    public void draw()
    {
        MapViewParameters params = getParameters();
        getGC().drawImage(getMapImage(), params.mapX, params.mapY);
    }

    @Override
    public void dispose()
    {
        // this job does not own any resources
    }

    public Image getMapImage()
    {
        return mapImage;
    }

    public void setMapImage(Image mapImage)
    {
        this.mapImage = mapImage;
    }
}