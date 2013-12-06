package maru.groundstation;

import maru.IMaruPluginResource;
import maru.MaruPluginResource;

public enum MaruGroundstationResources implements IMaruPluginResource
{
    GROUNDSTATION_DEFAULT_16("icons/Antenna_16.png", "Antenna 16px"),
    GROUNDSTATION_DEFAULT_128("icons/Antenna_128.png", "Antenna 128px"),

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
