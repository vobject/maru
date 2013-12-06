package maru;

/**
 * Base class for exceptions thrown by public interfaces of maru classes.
 */
public class MaruException extends Exception
{
    private static final long serialVersionUID = 1L;

    public MaruException()
    {

    }

    public MaruException(String message)
    {
        super(message);
    }

    public MaruException(Throwable cause)
    {
        super(cause);
    }

    public MaruException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
