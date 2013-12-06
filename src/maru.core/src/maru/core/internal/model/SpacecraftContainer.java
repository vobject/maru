package maru.core.internal.model;

import java.util.Collection;

import maru.core.model.ISpacecraft;
import maru.core.model.ISpacecraftContainer;
import maru.core.model.template.Spacecraft;

public class SpacecraftContainer extends Parent implements ISpacecraftContainer
{
    private static final long serialVersionUID = 1L;

    public SpacecraftContainer()
    {
        super("Spacecrafts", true);
    }

    @Override
    public Collection<ISpacecraft> getSpacecrafts()
    {
        return this.<ISpacecraft>castCollection(getChildren());
    }

    public void addSpacecraft(ISpacecraft sat)
    {
        ((Spacecraft) sat).setParent(this);
        addChild(sat);
    }

    public void removeSpacecraft(ISpacecraft sat)
    {
        removeChild(sat);
    }
}
