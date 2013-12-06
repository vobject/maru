package maru.core.model.template;

import maru.core.model.ICoordinate;
import maru.core.model.IGroundstation;

public abstract class Groundstation extends Propagatable implements IGroundstation
{
    private static final long serialVersionUID = 1L;

    public Groundstation(String name, ICoordinate initialPosition)
    {
        super(name, initialPosition);
    }
}
