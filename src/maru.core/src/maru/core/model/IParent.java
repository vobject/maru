package maru.core.model;

import java.util.Collection;

public interface IParent
{
    boolean hasChildren();
    boolean hasChild(IScenarioElement element);
    boolean hasChild(String name);

    Collection<IScenarioElement> getChildren();
}
