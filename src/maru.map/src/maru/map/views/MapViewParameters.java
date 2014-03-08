package maru.map.views;

public class MapViewParameters
{
    /** Indicates if one of the values has changed. */
    private boolean settingsChanged;

    private int mapImageWidth;
    private int mapImageHeight;

    public int clientAreaWidth;
    public int clientAreaHeight;

    private double mapScaleX;
    private double mapScaleY;
    public double mapScale;

    public int mapWidth;
    public int mapHeight;
    public int mapX;
    public int mapY;

    public boolean getSettingsChanged()
    {
        return settingsChanged;
    }

    public void setSettingsChanged(boolean changed)
    {
        settingsChanged = changed;
    }

    public void setImageSize(int width, int height)
    {
        this.mapImageWidth = width;
        this.mapImageHeight = height;

        setSettingsChanged(true);
    }

    public void setClientArea(int width, int height)
    {
        this.clientAreaWidth = width;
        this.clientAreaHeight = height;

        setSettingsChanged(true);
    }

    public void update()
    {
        this.mapScaleX = (double) clientAreaWidth / mapImageWidth;
        this.mapScaleY = (double) clientAreaHeight / mapImageHeight;
        this.mapScale = Math.min(mapScaleX, mapScaleY);

        this.mapWidth = (int) (mapImageWidth * mapScale);
        this.mapHeight = (int) (mapImageHeight * mapScale);

        this.mapX = (clientAreaWidth - mapWidth) / 2;
        this.mapY = (clientAreaHeight - mapHeight) / 2;

        setSettingsChanged(false);
    }

    /**
     * Scales an input value using the current map image scale.
     *
     * The output depends on the size of the map image used (aside from the
     * input value). It should therefore be chosen with regard to the original
     * map image size.
     */
    public int getScaledSize(int size)
    {
        return (int) (size * mapScale);
    }
}