package maru;

public class MaruPluginResource extends MaruResource implements IMaruPluginResource
{
    private static final long serialVersionUID = 1L;

    private final String bundlePath;

    public MaruPluginResource(String path, String name)
    {
        super(path, name);
        this.bundlePath = createBundlePath(getPluginId(), path);
    }

    @Override
    public String getPath()
    {
        return getBundlePath();
    }

    @Override
    public String getBundlePath()
    {
        return this.bundlePath;
    }

    /**
     * Every plugin that this class is used in must override this method.
     *
     * @return The PLUGIN_ID of the resource's plugin.
     */
    @Override
    public String getPluginId()
    {
        return MaruPlugin.PLUGIN_ID;
    }

    public static <T extends Enum<T>, U extends IMaruPluginResource> U fromBundlePath(Class<T> type, String bundlePath)
    {
        for (T enumConstant : type.getEnumConstants())
        {
            @SuppressWarnings("unchecked")
            U resourceEnumConstant = (U) enumConstant;
            if (resourceEnumConstant.getBundlePath().equals(bundlePath)) {
                return resourceEnumConstant;
            }
        }
        throw new IllegalArgumentException();
    }

    /**
     * Create a path to a resource that can be accessed from other plugins.
     *
     * @param pluginId The ID of the plugin where the resource is located.
     * @param path Path to the resource inside the specified plugin.
     * @return A fully qualified bundle path.
     */
    public static String createBundlePath(String pluginId, String path)
    {
        return "platform:/plugin/" + pluginId + "/" + path;
    }
}
