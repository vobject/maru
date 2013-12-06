package maru.core.model.template;

import maru.core.model.ICentralBody;

public abstract class CentralBody extends ScenarioElement implements ICentralBody
{
    private static final long serialVersionUID = 1L;

    public CentralBody(String name)
    {
        super(name);
    }
}
