package maru.groundstation;

import maru.IMaruPluginResource;
import maru.MaruPluginResource;

public enum MaruGroundstationResources implements IMaruPluginResource
{
    GROUNDSTATION_DEFAULT_1("icons/Antenna_1.png", "Antenna 1"),
    GROUNDSTATION_DEFAULT_2("icons/Antenna_2.png", "Antenna 2"),
    GROUNDSTATION_DEFAULT_3("icons/Antenna_3.png", "Antenna 3"),
    GROUNDSTATION_DEFAULT_4("icons/Antenna_4.png", "Antenna 4"),
    GROUNDSTATION_DEFAULT_5("icons/Antenna_5.png", "Antenna 5"),

    ; // enum constants end

    private MaruPluginResource resourceImpl;

    MaruGroundstationResources(String path, String name)
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

    public static MaruGroundstationResources fromName(String name)
    {
        return MaruPluginResource.fromName(MaruGroundstationResources.class, name);
    }

    public static MaruGroundstationResources fromPath(String path)
    {
        return MaruPluginResource.fromPath(MaruGroundstationResources.class, path);
    }
}
