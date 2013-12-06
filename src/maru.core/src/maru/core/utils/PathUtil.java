package maru.core.utils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public final class PathUtil
{
    public static URL getUrlFromPath(String path) throws MalformedURLException
    {
        if (isLocalPath(path)) {
            return new File(path).toURI().toURL();
        } else {
            return new URL(path);
        }
    }

    public static boolean isLocalPath(String path)
    {
        try {
            new URL(path);
            return false;
        } catch (MalformedURLException e) {
            return true;
        }
    }

    public static String getSuffixFromPath(String path)
    {
        int lastDot = path.lastIndexOf('.');
        if (lastDot < 0) {
            return null;
        }
        return path.substring(lastDot + 1).toLowerCase();
    }
}
