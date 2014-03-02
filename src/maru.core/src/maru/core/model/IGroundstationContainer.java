package maru.core.model;

import java.util.List;

public interface IGroundstationContainer extends IScenarioElement, IParent
{
    List<IGroundstation> getGroundstations();
}
