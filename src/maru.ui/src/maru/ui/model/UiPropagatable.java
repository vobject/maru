package maru.ui.model;

import maru.core.model.IPropagatable;

public abstract class UiPropagatable extends UiElement
{
    public UiPropagatable(UiParent parent, IPropagatable underlying)
    {
        super(parent, underlying);
    }

    @Override
    public IPropagatable getUnderlyingElement()
    {
        return (IPropagatable) super.getUnderlyingElement();
    }
}
