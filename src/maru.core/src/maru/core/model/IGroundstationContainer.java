package maru.core.model;

import java.util.Collection;

public interface IGroundstationContainer extends IScenarioElement, IParent
{
    Collection<IGroundstation> getGroundstations();
}
