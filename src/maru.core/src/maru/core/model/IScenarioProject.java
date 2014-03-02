package maru.core.model;

import java.util.List;

public interface IScenarioProject extends IScenarioElement
{
    IGroundstationContainer getGroundstationContainer();
    ISpacecraftContainer getSpacecraftContainer();

    ICentralBody getCentralBody();
    List<IGroundstation> getGroundstations();
    List<ISpacecraft> getSpacecrafts();

    ITimepoint getStartTime();
    ITimepoint getStopTime();
    ITimepoint getCurrentTime();
    List<? extends ITimepoint> getTimepoints();
    ITimepoint getPreviousTimepoint(ITimepoint timepoint);
    ITimepoint getNextTimepoint(ITimepoint timepoint);
}
