package maru.ui.model;

import maru.core.model.IVisibleElement;

public abstract class UiVisibleElement extends UiElement
{
    public UiVisibleElement(UiParent parent, IVisibleElement underlying)
    {
        super(parent, underlying);
    }

    @Override
    public IVisibleElement getUnderlyingElement()
    {
        return (IVisibleElement) super.getUnderlyingElement();
    }
}
