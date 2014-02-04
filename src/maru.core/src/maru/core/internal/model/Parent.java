package maru.core.internal.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import maru.core.model.AbstractScenarioElement;
import maru.core.model.IParent;
import maru.core.model.IScenarioElement;

public abstract class Parent extends AbstractScenarioElement implements IParent
{
    private static final long serialVersionUID = 1L;

    private final List<IScenarioElement> elements;
    private final boolean uniqueElements;

    public Parent(String name, boolean unique)
    {
        super(name);
        elements = new ArrayList<>();
        uniqueElements = unique;
    }

    @Override
    public boolean hasChildren()
    {
        return !elements.isEmpty();
    }

    @Override
    public boolean hasChild(IScenarioElement element)
    {
        return elements.contains(element);
    }

    @Override
    public boolean hasChild(String name)
    {
        for (IScenarioElement element : getChildren())
        {
            if (element.getElementName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Collection<IScenarioElement> getChildren()
    {
        return elements;
    }

    protected void addChild(IScenarioElement element)
    {
        if (uniqueElements && elements.contains(element)) {
            return;
        }
        elements.add(element);
    }

    protected void removeChild(IScenarioElement element)
    {
        elements.remove(element);
    }

    protected void removeAllChildren()
    {
        elements.clear();
    }

    protected void sortChildren()
    {
        Collections.sort(elements);
    }

    protected IScenarioElement getFirstElement()
    {
        if (!elements.isEmpty()) {
            return elements.get(0);
        }
        return null;
    }

    protected IScenarioElement getLastElement()
    {
        if (!elements.isEmpty()) {
            return elements.get(elements.size() - 1);
        }
        return null;
    }

    protected <E extends IScenarioElement> Collection<E> castCollection(Collection<IScenarioElement> collection)
    {
        @SuppressWarnings("unchecked")
        Collection<E> castCollection = (Collection<E>) collection;
        return castCollection;
    }
}
