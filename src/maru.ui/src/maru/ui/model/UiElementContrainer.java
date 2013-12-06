package maru.ui.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import maru.core.model.IScenarioElement;

public abstract class UiElementContrainer extends UiParent
{
    protected final Collection<UiElement> uiElements;

    public UiElementContrainer(UiParent parent, IScenarioElement underlying)
    {
        super(parent, underlying);
        uiElements = new ArrayList<UiElement>();
    }

    @Override
    public UiElement getChild(IScenarioElement child)
    {
        for (UiElement element : getChildren())
        {
            if (element.getUnderlyingElement() == child) {
                return element;
            }
        }
        return null;
    }

    @Override
    public UiElement getChild(String childName)
    {
        for (UiElement element : getChildren())
        {
            if (element.getName().equals(childName)) {
                return element;
            }
        }
        return null;
    }

    @Override
    public Collection<UiElement> getChildren()
    {
        return uiElements;
    }

    @Override
    public void addUiElement(IScenarioElement element)
    {
        // override in a subclass if needed
    }

    @Override
    public void updateUiElement(IScenarioElement element)
    {
        // no information from the underlying element is cached in this
        // class, so nothing needs to be updated.
    }

    @Override
    public void removeUiElement(IScenarioElement element)
    {
        Iterator<UiElement> iter = uiElements.iterator();
        while (iter.hasNext())
        {
            if (iter.next().getUnderlyingElement() == element)
            {
                iter.remove();
                break;
            }
        }
    }
}
