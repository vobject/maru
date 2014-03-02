package maru.centralbody.model;

import maru.centralbody.MaruCentralBodyPlugin;
import maru.core.model.resource.IMaruPluginResource;
import maru.core.model.resource.MaruPluginResource;

public enum CentralBodyResources implements IMaruPluginResource
{
    MAP_EARTH_1("images/earthmap_2048.jpg", "James Hastings-Trew's Earth", "http://planetpixelemporium.com/earth.html", ""),
    MAP_EARTH_2("images/land_shallow_topo_2048.jpg", "Blue Marple 2002 (land surface, shallow water, shaded topography)", "http://visibleearth.nasa.gov/view.php?id=57752", ""),
    MAP_EARTH_3("images/dnb_land_ocean_ice.2012.2048.jpg", "Night Lights 2012", "http://visibleearth.nasa.gov/view.php?id=79765", ""),
    MAP_EARTH_4("images/world.topo.200407_2048.jpg", "Blue Marple 2004 (July, topography)", "http://visibleearth.nasa.gov/view.php?id=74393", ""),
    MAP_EARTH_5("images/world.topo.bathy.200407_2048.jpg", "Blue Marple 2004 (July, topography, bathymetry)", "http://visibleearth.nasa.gov/view.php?id=73751", ""),
    MAP_EARTH_6("images/NE2_modis3.jpg", "Natural Earth II (blended ocean depth tints and shading)", "http://www.shadedrelief.com/natural2/index.html", ""),
    MAP_EARTH_7("images/NE2_modis3_adjusted.jpg", "Natural Earth II with colors adjusted", "http://www.shadedrelief.com/natural2/index.html", ""),

//    MAP_MOON_1("images/moonmap2k.jpg", "James Hastings-Trew's Moon", "http://planetpixelemporium.com/earth.html", ""),

    ; // enum constants end

    private MaruPluginResource resourceImpl;
    private String url;
    private String license;

    CentralBodyResources(String path, String name)
    {
        this(path, name, "", "");
    }

    CentralBodyResources(String path, String name, String url, String license)
    {
        resourceImpl = new MaruPluginResource(path, name) {
            private static final long serialVersionUID = 1L;
            @Override public String getPluginId() {
                return MaruCentralBodyPlugin.PLUGIN_ID;
            }
        };
        this.url = url;
        this.license = license;
    }

    @Override
    public String getName()
    {
        return resourceImpl.getName();
    }

    @Override
    public String getPath()
    {
        return resourceImpl.getPath();
    }

    @Override
    public String getBundlePath()
    {
        return resourceImpl.getBundlePath();
    }

    @Override
    public String getPluginId()
    {
        return MaruCentralBodyPlugin.PLUGIN_ID;
    }

    public String getUrl()
    {
        return url;
    }

    public String getLicense()
    {
        return license;
    }

    public static CentralBodyResources fromName(String name)
    {
        return MaruPluginResource.fromName(CentralBodyResources.class, name);
    }

    public static CentralBodyResources fromPath(String path)
    {
        return MaruPluginResource.fromPath(CentralBodyResources.class, path);
    }
}
