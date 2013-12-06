package maru.map.utils;

public final class MapUtils
{
    public static double verticalPixelToLatitude(int y, int heightPixels)
    {
        double lat = (180.0 / heightPixels) * y;
        return -(90.0 - lat);
    }

    public static double horizontalPixelToLongitude(int y, int widthPixels)
    {
        double lat = (360.0 / widthPixels) * y;
        return -(180.0 - lat);
    }

    public static int latitudeToVerticalPixel(double latitude, int mapHeight)
    {
        return (int) ((mapHeight / 2) - ((mapHeight / 180.0) * latitude));
    }

    public static int longitudeToHorizontalPixel(double longitude, int mapWidth)
    {
        return (int) ((mapWidth / 2) + ((mapWidth / 360.0) * longitude));
    }
}
