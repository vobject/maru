package maru.ui.model;

import maru.core.model.ICentralBody;

public class UiCentralBody extends UiElement
{
    public UiCentralBody(UiParent parent, ICentralBody underlying)
    {
        super(parent, underlying);

        setSortPriority(UiSortPriority.CENTRALBODY);
    }

    @Override
    public ICentralBody getUnderlyingElement()
    {
        return (ICentralBody) super.getUnderlyingElement();
    }

    @Override
    protected String getImagePath()
    {
        return "icons/App-package-network-icon.png";
    }
}
