package maru.core.internal.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import maru.core.model.ICentralBody;
import maru.core.model.IGroundstation;
import maru.core.model.IScenarioProject;
import maru.core.model.ISpacecraft;
import maru.core.model.ITimepoint;
import maru.core.model.template.ScenarioElement;

import org.eclipse.core.resources.IProject;

public class ScenarioProject extends ScenarioElement implements IScenarioProject
{
    private static final long serialVersionUID = 1L;

    private transient IProject project;
    private final GroundstationContainer groundstationContainer;
    private final SpacecraftContainer spacecraftContainer;

    private ICentralBody centralBody;
    private final List<Timepoint> timepoints;
    private Timepoint currentTimepoint;

    public ScenarioProject(IProject project,
                           Timepoint start,
                           Timepoint stop,
                           String comment,
                           ICentralBody centralBody)
    {
        super(project.getName());

        this.project = project;
        this.timepoints = new ArrayList<>();

        groundstationContainer = new GroundstationContainer();
        spacecraftContainer = new SpacecraftContainer();

        groundstationContainer.setParent(this);
        spacecraftContainer.setParent(this);

        addTimepoint(start);
        addTimepoint(stop);
        setElementComment(comment);
        setCentralBody(centralBody);
    }

    @Override
    public IProject getProject()
    {
        return project;
    }

    @Override
    public GroundstationContainer getGroundstationContainer()
    {
        return groundstationContainer;
    }

    @Override
    public SpacecraftContainer getSpacecraftContainer()
    {
        return spacecraftContainer;
    }

    @Override
    public ICentralBody getCentralBody()
    {
        return centralBody;
    }

    @Override
    public Collection<IGroundstation> getGroundstations()
    {
        return groundstationContainer.getGroundstations();
    }

    @Override
    public Collection<ISpacecraft> getSpacecrafts()
    {
        return spacecraftContainer.getSpacecrafts();
    }

    @Override
    public Timepoint getStartTime()
    {
        return timepoints.get(0);
    }

    @Override
    public Timepoint getStopTime()
    {
        return timepoints.get(timepoints.size() - 1);
    }

    @Override
    public Timepoint getCurrentTime()
    {
        return currentTimepoint;
    }

    @Override
    public Collection<? extends ITimepoint> getTimepoints()
    {
        return timepoints;
    }

    @Override
    public Timepoint getPreviousTimepoint(ITimepoint timepoint)
    {
        Timepoint previous = null;
        for (Timepoint element : timepoints)
        {
            if (!(timepoint.getTime() > element.getTime())) {
                break;
            }
            previous = element;
        }

        if (previous != null) {
            return previous;
        } else {
            // return the start time if the given time
            // is set even earlier than that.
            return getStartTime();
        }
    }

    @Override
    public Timepoint getNextTimepoint(ITimepoint timepoint)
    {
        for (Timepoint element : timepoints)
        {
            if (timepoint.getTime() < element.getTime()) {
                return element;
            }
        }
        return getStopTime();
    }

    public void setProject(IProject project)
    {
        this.project = project;
    }

    public void setCentralBody(ICentralBody centralBody)
    {
        ((ScenarioElement) centralBody).setParent(this);
        this.centralBody = centralBody;
    }

    public void addTimepoint(Timepoint timepoint)
    {
        if (currentTimepoint == null) {
            currentTimepoint = new Timepoint(timepoint);
            currentTimepoint.setParent(this);
        }

        timepoint.setParent(this);
        timepoints.add(timepoint);
        sortTimepoints();
    }

    public void removeTimepoint(Timepoint timepoint)
    {
        if (timepoints.size() <= 2) {
            // we always need at least 2 timepoints (start and stop)!
            return;
        }

        timepoints.remove(timepoint);
        sortTimepoints();
    }

    public void changeTimepoint(Timepoint timepoint, long time)
    {
        timepoint.setTime(time);
        sortTimepoints();
    }

    private void sortTimepoints()
    {
        if (timepoints.size() <= 1) {
            return;
        }

        Collections.sort(timepoints);

        Timepoint startTimepoint = getStartTime();
        Timepoint stopTimepoint = getStopTime();

        // fix the current time point if it turned up beyond the time frame
        // of this scenario
        if (currentTimepoint.getTime() < startTimepoint.getTime()) {
            currentTimepoint = new Timepoint(startTimepoint);
        } else if (currentTimepoint.getTime() > stopTimepoint.getTime()) {
            currentTimepoint = new Timepoint(stopTimepoint);
        }
    }
}
