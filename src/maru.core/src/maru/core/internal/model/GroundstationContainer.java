package maru.core.internal.model;

import java.util.Collection;

import maru.core.model.IGroundstation;
import maru.core.model.IGroundstationContainer;
import maru.core.model.template.Groundstation;

public class GroundstationContainer extends Parent implements IGroundstationContainer
{
    private static final long serialVersionUID = 1L;

    public GroundstationContainer()
    {
        super("Groundstations", true);
    }

    @Override
    public Collection<IGroundstation> getGroundstations()
    {
        return this.<IGroundstation>castCollection(getChildren());
    }

    public void addGroundstation(IGroundstation gs)
    {
        ((Groundstation) gs).setParent(this);
        addChild(gs);
    }

    public void removeGroundstation(IGroundstation gs)
    {
        removeChild(gs);
    }
}
