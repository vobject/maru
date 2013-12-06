package maru.core.model.template;

import maru.core.model.ICoordinate;
import maru.core.model.ISpacecraft;

public abstract class Spacecraft extends Propagatable implements ISpacecraft
{
    private static final long serialVersionUID = 1L;

    public Spacecraft(String name, ICoordinate initialPosition)
    {
        super(name, initialPosition);
    }

    @Override
    public boolean inUmbra()
    {
        return inUmbra(getCurrentCoordinate());
    }

    @Override
    public boolean inUmbraOrPenumbra()
    {
        return inUmbraOrPenumbra(getCurrentCoordinate());
    }
}
