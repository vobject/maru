package maru.core.model;

import java.util.List;

public interface IParent
{
    boolean hasChildren();
    boolean hasChild(IScenarioElement element);
    boolean hasChild(String name);

    List<IScenarioElement> getChildren();
}
