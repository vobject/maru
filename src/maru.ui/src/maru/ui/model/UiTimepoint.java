package maru.ui.model;

import maru.core.model.CoreModel;
import maru.core.model.ITimepoint;

public class UiTimepoint extends UiElement
{
    // the UiTimepoint type determines which icon to show for the item.
    // the first and last timepoint have different icons than the rest to
    // indicate the beginning and the end of the scenario.
    public enum UiTimepointType
    {
        DEFAULT,
        START,
        STOP
    }

    private UiTimepointType type;

    public UiTimepoint(UiTimepointContrainer parent, ITimepoint underlying, UiTimepointType type)
    {
        super(parent, underlying);
        this.setType(type);
    }

    // convenience method do access the underlying element's date information
    public long getTime()
    {
        return getUnderlyingElement().getTime();
    }

    // convenience method to the underlying element
    public void setTime(long time)
    {
        CoreModel coreModel = CoreModel.getDefault();
        coreModel.changeTimepoint(getUnderlyingElement(), time, true);
    }

    @Override
    public String getName()
    {
        return getUnderlyingElement().getElementName();
    }

    @Override
    public UiTimepointContrainer getUiParent()
    {
        return (UiTimepointContrainer) super.getUiParent();
    }

    @Override
    public ITimepoint getUnderlyingElement()
    {
        return (ITimepoint) super.getUnderlyingElement();
    }

    @Override
    protected String getImagePath()
    {
        switch (type)
        {
            case DEFAULT:
                return "icons/Actions-bookmark-icon.png";
            case START:
                return "icons/Status-user-away-icon.png";
            case STOP:
                return "icons/Status-user-away-extended-icon.png";
        }
        return null;
    }

    public void setType(UiTimepointType type)
    {
        this.type = type;
    }
}
