package maru.spacecraft;

import maru.IMaruPluginResource;
import maru.MaruPluginResource;

public enum MaruSpacecraftResources implements IMaruPluginResource
{
    SPACECRAFT_DEFAULT_16("icons/Satellite_16.png", "Satellite 16px"),
    SPACECRAFT_DEFAULT_128("icons/Satellite_128.png", "Satellite 128px"),
    SPACECRAFT_ISS_128("icons/ISS_128.png", "ISS 128px"),

    ; // enum constants end

    private MaruPluginResource resourceImpl;

    MaruSpacecraftResources(String path, String name)
    {
        resourceImpl = new MaruPluginResource(path, name) {
            private static final long serialVersionUID = 1L;
            @Override public String getPluginId() {
                return MaruSpacecraftPlugin.PLUGIN_ID;
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
        return MaruSpacecraftPlugin.PLUGIN_ID;
    }

    public static MaruSpacecraftResources fromName(String name)
    {
        return MaruPluginResource.fromName(MaruSpacecraftResources.class, name);
    }

    public static MaruSpacecraftResources fromPath(String path)
    {
        return MaruPluginResource.fromPath(MaruSpacecraftResources.class, path);
    }
}
