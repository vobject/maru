package maru.ui.model;

import maru.core.model.IScenarioElement;
import maru.core.model.ISpacecraft;
import maru.core.model.ISpacecraftContainer;

public class UiSpacecraftContrainer extends UiElementContrainer
{
    public UiSpacecraftContrainer(UiParent parent, ISpacecraftContainer underlying)
    {
        super(parent, underlying);

        setSortPriority(UiSortPriority.SATELLITES);

        for (ISpacecraft element : underlying.getSpacecrafts()) {
            addUiSpacecraft(element);
        }
    }

    @Override
    public ISpacecraftContainer getUnderlyingElement()
    {
        return (ISpacecraftContainer) super.getUnderlyingElement();
    }

    @Override
    public void addUiElement(IScenarioElement element)
    {
        addUiSpacecraft((ISpacecraft) element);
    }

    @Override
    protected String getImagePath()
    {
        return "icons/Satellite_16.png";
    }

    private void addUiSpacecraft(ISpacecraft element)
    {
        uiElements.add(new UiSpacecraft(this, element));
    }
}
