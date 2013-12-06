package maru.core.model;

import java.util.Collection;

public interface ISpacecraftContainer extends IScenarioElement, IParent
{
    Collection<ISpacecraft> getSpacecrafts();
}
