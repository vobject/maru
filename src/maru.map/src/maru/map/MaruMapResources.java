package maru.map;

import maru.core.model.resource.IMaruPluginResource;
import maru.core.model.resource.MaruPluginResource;

public enum MaruMapResources implements IMaruPluginResource
{
    // add map resources here, e.g. images for IDrawJobs

    ; // enum constants end

    private MaruPluginResource resourceImpl;

    MaruMapResources(String path, String name)
    {
        resourceImpl = new MaruPluginResource(path, name) {
            private static final long serialVersionUID = 1L;
            @Override public String getPluginId() {
                return MaruMapPlugin.PLUGIN_ID;
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
        return MaruMapPlugin.PLUGIN_ID;
    }

    public static MaruMapResources fromName(String name)
    {
        return MaruPluginResource.fromName(MaruMapResources.class, name);
    }

    public static MaruMapResources fromPath(String path)
    {
        return MaruPluginResource.fromPath(MaruMapResources.class, path);
    }
}
