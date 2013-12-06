package maru.ui.model;

import java.util.Collection;

import maru.core.model.IScenarioElement;

public abstract class UiParent extends UiElement
{
    public UiParent(UiParent parent, IScenarioElement underlying)
    {
        super(parent, underlying);
    }

    public abstract UiElement getChild(IScenarioElement child);
    public abstract UiElement getChild(String childName);
    public abstract Collection<UiElement> getChildren();

    public abstract void addUiElement(IScenarioElement element);
    public abstract void updateUiElement(IScenarioElement element);
    public abstract void removeUiElement(IScenarioElement element);
}
