package maru.core.model;

import java.util.List;

public interface ISpacecraftContainer extends IScenarioElement, IParent
{
    List<ISpacecraft> getSpacecrafts();
}
