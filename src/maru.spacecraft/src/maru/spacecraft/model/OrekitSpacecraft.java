package maru.spacecraft.model;

import maru.core.model.AbstractSpacecraft;

public abstract class OrekitSpacecraft extends AbstractSpacecraft
{
    private static final long serialVersionUID = 1L;

    public OrekitSpacecraft(String name)
    {
        super(name);
    }
}
