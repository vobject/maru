package maru.core.model;

import java.util.Collection;

import org.eclipse.core.resources.IProject;

public interface IScenarioProject extends IScenarioElement
{
    IProject getProject();

    IGroundstationContainer getGroundstationContainer();
    ISpacecraftContainer getSpacecraftContainer();

    ICentralBody getCentralBody();
    Collection<IGroundstation> getGroundstations();
    Collection<ISpacecraft> getSpacecrafts();

    ITimepoint getStartTime();
    ITimepoint getStopTime();
    ITimepoint getCurrentTime();
    Collection<? extends ITimepoint> getTimepoints();
    ITimepoint getPreviousTimepoint(ITimepoint timepoint);
    ITimepoint getNextTimepoint(ITimepoint timepoint);
}
