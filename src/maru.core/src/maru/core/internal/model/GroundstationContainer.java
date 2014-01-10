package maru.core.internal.model;

import java.util.Collection;

import maru.core.model.IGroundstation;
import maru.core.model.IGroundstationContainer;
import maru.core.model.template.ScenarioElement;

public class GroundstationContainer extends Parent implements IGroundstationContainer
{
    private static final long serialVersionUID = 1L;

    public GroundstationContainer()
    {
        super("Groundstations", true);
    }

    @Override
    public Collection<IGroundstation> getGroundstations()
    {
        return this.<IGroundstation>castCollection(getChildren());
    }

    public void addGroundstation(IGroundstation gs)
    {
        ((ScenarioElement) gs).setParent(this);

        gs.startTimeChanged(getScenarioProject().getStartTime().getTime());
        gs.stopTimeChanged(getScenarioProject().getStopTime().getTime());
        gs.currentTimeChanged(getScenarioProject().getCurrentTime().getTime());

        addChild(gs);
    }

    public void removeGroundstation(IGroundstation gs)
    {
        removeChild(gs);
    }
}
