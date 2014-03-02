package maru.core.model.resource;

public class MaruResource implements IMaruResource
{
    private static final long serialVersionUID = 1L;

    private final String name;
    private final String path;

    public MaruResource(String path, String name)
    {
        this.name = name;
        this.path = path;
    }

    public MaruResource(String path, String name, String url, String license)
    {
        this(path, name);
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    @Override
    public String getPath()
    {
        return this.path;
    }

    public static <T extends Enum<T>, U extends IMaruResource> U fromName(Class<T> type, String name)
    {
        for (T enumConstant : type.getEnumConstants())
        {
            @SuppressWarnings("unchecked")
            U resourceEnumConstant = (U) enumConstant;
            if (resourceEnumConstant.getName().equals(name)) {
                return resourceEnumConstant;
            }
        }
        throw new IllegalArgumentException();
    }

    public static <T extends Enum<T>, U extends IMaruResource> U fromPath(Class<T> type, String path)
    {
        for (T enumConstant : type.getEnumConstants())
        {
            @SuppressWarnings("unchecked")
            U resourceEnumConstant = (U) enumConstant;
            if (resourceEnumConstant.getPath().equals(path)) {
                return resourceEnumConstant;
            }
        }
        throw new IllegalArgumentException();
    }
}
