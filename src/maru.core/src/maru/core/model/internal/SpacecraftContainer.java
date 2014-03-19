package maru.core.model.internal;

import java.util.List;

import maru.core.model.AbstractScenarioElement;
import maru.core.model.ISpacecraft;
import maru.core.model.ISpacecraftContainer;

public class SpacecraftContainer extends Parent implements ISpacecraftContainer
{
    private static final long serialVersionUID = 1L;

    public SpacecraftContainer()
    {
        super("Spacecrafts", true);
    }

    @Override
    public List<ISpacecraft> getSpacecrafts()
    {
        return Parent.<ISpacecraft>castList(getChildren());
    }

    public void addSpacecraft(ISpacecraft sc)
    {
        ((AbstractScenarioElement) sc).setParent(this);

        sc.startTimeChanged(getScenarioProject().getStartTime().getTime());
        sc.stopTimeChanged(getScenarioProject().getStopTime().getTime());
        sc.currentTimeChanged(getScenarioProject().getCurrentTime().getTime());

        addChild(sc);
    }

    public void removeSpacecraft(ISpacecraft sc)
    {
        removeChild(sc);
    }
}
