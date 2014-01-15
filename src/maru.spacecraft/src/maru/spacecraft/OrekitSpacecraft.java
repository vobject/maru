package maru.spacecraft;

import maru.core.model.template.AbstractSpacecraft;

public abstract class OrekitSpacecraft extends AbstractSpacecraft
{
    private static final long serialVersionUID = 1L;

    public OrekitSpacecraft(String name)
    {
        super(name);
    }
}
