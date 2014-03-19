package maru.core.model.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import maru.core.model.AbstractScenarioElement;
import maru.core.model.ICentralBody;
import maru.core.model.IGroundstation;
import maru.core.model.IScenarioProject;
import maru.core.model.ISpacecraft;
import maru.core.model.ITimepoint;

import org.orekit.time.AbsoluteDate;

public class ScenarioProject extends AbstractScenarioElement implements IScenarioProject
{
    private static final long serialVersionUID = 1L;

    private final GroundstationContainer groundstationContainer;
    private final SpacecraftContainer spacecraftContainer;

    private ICentralBody centralBody;
    private final List<Timepoint> timepoints;
    private Timepoint currentTimepoint;

    public ScenarioProject(String name, Timepoint start, Timepoint stop,
                           String comment, ICentralBody centralBody)
    {
        super(name);

        this.timepoints = new ArrayList<>();
        this.groundstationContainer = new GroundstationContainer();
        this.spacecraftContainer = new SpacecraftContainer();

        this.groundstationContainer.setParent(this);
        this.spacecraftContainer.setParent(this);

        addTimepoint(start);
        addTimepoint(stop);
        setElementComment(comment);
        setCentralBody(centralBody);
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
    public List<IGroundstation> getGroundstations()
    {
        return groundstationContainer.getGroundstations();
    }

    @Override
    public List<ISpacecraft> getSpacecrafts()
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
    public List<ITimepoint> getTimepoints()
    {
        return Parent.<ITimepoint>castList(timepoints);
    }

    @Override
    public Timepoint getPreviousTimepoint(ITimepoint timepoint)
    {
        Timepoint previous = null;
        for (Timepoint element : timepoints)
        {
            if (!(timepoint.compareTo(element) > 0)) {
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
            if (timepoint.compareTo(element) < 0) {
                return element;
            }
        }
        return getStopTime();
    }

    public void setCentralBody(ICentralBody centralBody)
    {
        ((AbstractScenarioElement) centralBody).setParent(this);
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

    public void changeTimepoint(Timepoint timepoint, AbsoluteDate time)
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
        if (currentTimepoint.compareTo(startTimepoint) < 0) {
            currentTimepoint = new Timepoint(startTimepoint);
        } else if (currentTimepoint.compareTo(stopTimepoint) > 0) {
            currentTimepoint = new Timepoint(stopTimepoint);
        }
    }
}
