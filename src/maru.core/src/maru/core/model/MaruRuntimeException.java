package maru.core.model;

/**
 * Base class for runtime exceptions thrown by maru classes.
 */
public class MaruRuntimeException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    public MaruRuntimeException()
    {

    }

    public MaruRuntimeException(String message)
    {
        super(message);
    }

    public MaruRuntimeException(Throwable cause)
    {
        super(cause);
    }

    public MaruRuntimeException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
