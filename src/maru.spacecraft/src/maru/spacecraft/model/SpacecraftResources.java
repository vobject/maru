package maru.spacecraft.model;

import maru.core.model.resource.IMaruPluginResource;
import maru.core.model.resource.MaruPluginResource;
import maru.spacecraft.MaruSpacecraftPlugin;

public enum SpacecraftResources implements IMaruPluginResource
{
    SPACECRAFT_DEFAULT_1("icons/Satellite_1.png", "Satellite 1"),
    SPACECRAFT_DEFAULT_2("icons/Satellite_2.png", "Satellite 2"),
    SPACECRAFT_DEFAULT_3("icons/Satellite_3.png", "Satellite 3"),

    SPACECRAFT_ISS_1("icons/ISS_1.png", "ISS 1"),
    SPACECRAFT_ISS_2("icons/ISS_2.png", "ISS 2"),

    SPACECRAFT_ASTRONAUT_1("icons/Astronaut_1.png", "Astronaut 1"),

    SPACECRAFT_ROCKET_1("icons/Rocket_1.png", "Rocket 1"),

    SPACECRAFT_SHUTTLE_1("icons/Shuttle_1.png", "Shuttle 1"),
    SPACECRAFT_SHUTTLE_2("icons/Shuttle_2.png", "Shuttle 2"),

    ; // enum constants end

    private MaruPluginResource resourceImpl;

    SpacecraftResources(String path, String name)
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

    public static SpacecraftResources fromName(String name)
    {
        return MaruPluginResource.fromName(SpacecraftResources.class, name);
    }

    public static SpacecraftResources fromPath(String path)
    {
        return MaruPluginResource.fromPath(SpacecraftResources.class, path);
    }
}
