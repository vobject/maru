package maru.core.model.net;

import java.io.Serializable;

import maru.core.model.IScenarioElement;

public class NetworkMessageWrapper implements Serializable
{
    private static final long serialVersionUID = 1L;

    private final NetworkMessageID msg;
    private final IScenarioElement element;

    public NetworkMessageWrapper(NetworkMessageID msg, IScenarioElement element)
    {
        this.msg = msg;
        this.element = element;
    }

    public NetworkMessageID getMessageID()
    {
        return msg;
    }

    public IScenarioElement getElement()
    {
        return element;
    }

    @Override
    public String toString()
    {
        return "ID=" + msg + " Data=" + element.toString();
    }
}
