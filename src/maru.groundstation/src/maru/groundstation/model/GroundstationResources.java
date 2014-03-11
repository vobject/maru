package maru.groundstation.model;

import maru.core.model.resource.IMaruPluginResource;
import maru.core.model.resource.MaruPluginResource;
import maru.groundstation.MaruGroundstationPlugin;

public enum GroundstationResources implements IMaruPluginResource
{
    GROUNDSTATION_ANTENNA_1("icons/Antenna_1.png", "Antenna 1"),
    GROUNDSTATION_ANTENNA_2("icons/Antenna_2.png", "Antenna 2"),
    GROUNDSTATION_ANTENNA_3("icons/Antenna_3.png", "Antenna 3"),
    GROUNDSTATION_ANTENNA_4("icons/Antenna_4.png", "Antenna 4"),
    GROUNDSTATION_ANTENNA_5("icons/Antenna_5.png", "Antenna 5"),
    GROUNDSTATION_MARKER_1("icons/Marker_1.png", "Marker 1"),
    GROUNDSTATION_MARKER_2("icons/Marker_2.png", "Marker 2"),

    ; // enum constants end

    private MaruPluginResource resourceImpl;

    GroundstationResources(String path, String name)
    {
        resourceImpl = new MaruPluginResource(path, name) {
            private static final long serialVersionUID = 1L;
            @Override public String getPluginId() {
                return MaruGroundstationPlugin.PLUGIN_ID;
            }
        };
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
        return MaruGroundstationPlugin.PLUGIN_ID;
    }

    public static GroundstationResources fromName(String name)
    {
        return MaruPluginResource.fromName(GroundstationResources.class, name);
    }

    public static GroundstationResources fromPath(String path)
    {
        return MaruPluginResource.fromPath(GroundstationResources.class, path);
    }
}
