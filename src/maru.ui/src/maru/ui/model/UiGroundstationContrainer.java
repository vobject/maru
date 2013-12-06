package maru.ui.model;

import maru.core.model.IGroundstation;
import maru.core.model.IGroundstationContainer;
import maru.core.model.IScenarioElement;

public class UiGroundstationContrainer extends UiElementContrainer
{
    public UiGroundstationContrainer(UiParent parent, IGroundstationContainer underlying)
    {
        super(parent, underlying);

        setSortPriority(UiSortPriority.GROUNDSTATIONS);

        for (IGroundstation element : underlying.getGroundstations()) {
            addUiGroundstation(element);
        }
    }

    @Override
    public IGroundstationContainer getUnderlyingElement()
    {
        return (IGroundstationContainer) super.getUnderlyingElement();
    }

    @Override
    public void addUiElement(IScenarioElement element)
    {
        addUiGroundstation((IGroundstation) element);
    }

    @Override
    protected String getImagePath()
    {
        return "icons/Antenna_16.png";
    }

    private void addUiGroundstation(IGroundstation element)
    {
        uiElements.add(new UiGroundstation(this, element));
    }
}
