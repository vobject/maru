package maru.spacecraft.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import maru.core.utils.PathUtils;
import maru.spacecraft.MaruSpacecraftPlugin;
import maru.spacecraft.preferences.PreferenceConstants;
import maru.spacecraft.preferences.TleSourceEditor;
import maru.spacecraft.tle.InitialTLECoordinate;

import org.eclipse.jface.preference.IPreferenceStore;
import org.orekit.errors.OrekitException;
import org.orekit.propagation.analytical.tle.TLE;

public final class TleUtils
{
    /*
     * A TLE source can be a local path or a URL. The list of TLE sources is
     * saved as one string and must be parsed when its needed. This string
     * separates the source strings (path or URL) from one another. It must be
     * a string that does not appear in local paths or URLs.
     *
     * FIXME:
     * This string ';' does not fulfill the requirement but is an improvement
     * over the last approach File.pathSeperator which used ':' under Linux and
     * therefore did not work with URLs (e.g. http://...). Linux file systems
     * usually allow the ';' character in paths but they are rather unlikely to
     * be used.
     */
    public static final String TLE_SOURCE_SEPERATOR = ";";

    public static String[] getTleSources()
    {
        IPreferenceStore store = MaruSpacecraftPlugin.getDefault().getPreferenceStore();
        String stringList = store.getString(PreferenceConstants.P_EXTERNAL_TLE);

        Collection<String> elements = TleSourceEditor.parsePreferenceString(stringList);
        return elements.toArray(new String[elements.size()]);
    }

    public static List<InitialTLECoordinate> parseTleSource(String source)
    {
        List<InitialTLECoordinate> tleList = new ArrayList<>();

        try
        {
            URL url = PathUtils.getUrlFromPath(source);
            InputStreamReader stream = new InputStreamReader(url.openStream());
            BufferedReader reader = new BufferedReader(stream);

            String line;
            while ((line = reader.readLine()) != null)
            {
                String name = line.trim();
                String line1 = reader.readLine();
                String line2 = reader.readLine();

                TLE tle = new TLE(line1, line2);
                tleList.add(new InitialTLECoordinate(name, tle));
            }
            reader.close();
        }
        catch (IOException | OrekitException e)
        {
            e.printStackTrace();
        }

        return tleList;
    }
}
