package maru.spacecraft.preferences;

import maru.spacecraft.MaruSpacecraftPlugin;
import maru.spacecraft.utils.TleUtils;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

public class PreferenceInitializer extends AbstractPreferenceInitializer
{
    @Override
    public void initializeDefaultPreferences()
    {
        IPreferenceStore store = MaruSpacecraftPlugin.getDefault().getPreferenceStore();
        store.setDefault(PreferenceConstants.P_EXTERNAL_TLE, getDefaultTleSourcesString());
    }

    private String getDefaultTleSourcesString()
    {
        StringBuilder sb = new StringBuilder();

        // default values: http://www.celestrak.com/NORAD/elements/master.asp
        appendTleSource(sb, "http://www.celestrak.com/NORAD/elements/amateur.txt");
        appendTleSource(sb, "http://www.celestrak.com/NORAD/elements/beidou.txt");
        appendTleSource(sb, "http://www.celestrak.com/NORAD/elements/cubesat.txt");
        appendTleSource(sb, "http://www.celestrak.com/NORAD/elements/dmc.txt");
        appendTleSource(sb, "http://www.celestrak.com/NORAD/elements/education.txt");
        appendTleSource(sb, "http://www.celestrak.com/NORAD/elements/engineering.txt");
        appendTleSource(sb, "http://www.celestrak.com/NORAD/elements/galileo.txt");
        appendTleSource(sb, "http://www.celestrak.com/NORAD/elements/geo.txt");
        appendTleSource(sb, "http://www.celestrak.com/NORAD/elements/geodetic.txt");
        appendTleSource(sb, "http://www.celestrak.com/NORAD/elements/glo-ops.txt");
        appendTleSource(sb, "http://www.celestrak.com/NORAD/elements/globalstar.txt");
        appendTleSource(sb, "http://www.celestrak.com/NORAD/elements/goes.txt");
        appendTleSource(sb, "http://www.celestrak.com/NORAD/elements/gorizont.txt");
        appendTleSource(sb, "http://www.celestrak.com/NORAD/elements/gps-ops.txt");
        appendTleSource(sb, "http://www.celestrak.com/NORAD/elements/intelsat.txt");
        appendTleSource(sb, "http://www.celestrak.com/NORAD/elements/iridium.txt");
        appendTleSource(sb, "http://www.celestrak.com/NORAD/elements/military.txt");
        appendTleSource(sb, "http://www.celestrak.com/NORAD/elements/molniya.txt");
        appendTleSource(sb, "http://www.celestrak.com/NORAD/elements/musson.txt");
        appendTleSource(sb, "http://www.celestrak.com/NORAD/elements/nnss.txt");
        appendTleSource(sb, "http://www.celestrak.com/NORAD/elements/noaa.txt");
        appendTleSource(sb, "http://www.celestrak.com/NORAD/elements/orbcomm.txt");
        appendTleSource(sb, "http://www.celestrak.com/NORAD/elements/other.txt");
        appendTleSource(sb, "http://www.celestrak.com/NORAD/elements/other-comm.txt");
        appendTleSource(sb, "http://www.celestrak.com/NORAD/elements/radar.txt");
        appendTleSource(sb, "http://www.celestrak.com/NORAD/elements/raduga.txt");
        appendTleSource(sb, "http://www.celestrak.com/NORAD/elements/resource.txt");
        appendTleSource(sb, "http://www.celestrak.com/NORAD/elements/sarsat.txt");
        appendTleSource(sb, "http://www.celestrak.com/NORAD/elements/sbas.txt");
        appendTleSource(sb, "http://www.celestrak.com/NORAD/elements/science.txt");
        appendTleSource(sb, "http://www.celestrak.com/NORAD/elements/stations.txt");
        appendTleSource(sb, "http://www.celestrak.com/NORAD/elements/tdrss.txt");
        appendTleSource(sb, "http://www.celestrak.com/NORAD/elements/tle-new.txt");
        appendTleSource(sb, "http://www.celestrak.com/NORAD/elements/visual.txt");
        appendTleSource(sb, "http://www.celestrak.com/NORAD/elements/weather.txt");
        appendTleSource(sb, "http://www.celestrak.com/NORAD/elements/x-comm.txt");
        appendTleSource(sb, "http://www.celestrak.com/NORAD/elements/1999-025.txt");
        appendTleSource(sb, "http://www.celestrak.com/NORAD/elements/iridium-33-debris.txt");
        appendTleSource(sb, "http://www.celestrak.com/NORAD/elements/cosmos-2251-debris.txt");
        appendTleSource(sb, "http://www.celestrak.com/NORAD/elements/2012-044.txt");

        return sb.toString();
    }

    private void appendTleSource(StringBuilder sb, String path)
    {
        sb.append(path + TleUtils.TLE_SOURCE_SEPERATOR);
    }
}
