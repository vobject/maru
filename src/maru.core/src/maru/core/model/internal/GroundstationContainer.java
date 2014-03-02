package maru.core.model.internal;

import java.util.List;

import maru.core.model.AbstractScenarioElement;
import maru.core.model.IGroundstation;
import maru.core.model.IGroundstationContainer;

public class GroundstationContainer extends Parent implements IGroundstationContainer
{
    private static final long serialVersionUID = 1L;

    public GroundstationContainer()
    {
        super("Groundstations", true);
    }

    @Override
    public List<IGroundstation> getGroundstations()
    {
        return this.<IGroundstation>castList(getChildren());
    }

    public void addGroundstation(IGroundstation gs)
    {
        ((AbstractScenarioElement) gs).setParent(this);

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
