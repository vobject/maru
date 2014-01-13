package maru.ui.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import maru.core.model.IScenarioProject;
import maru.core.model.ITimepoint;
import maru.core.utils.TimeUtils;
import maru.ui.model.UiTimepoint.UiTimepointType;

public class UiTimepointContrainer extends UiElementContrainer
{
    public UiTimepointContrainer(UiParent parent, IScenarioProject project)
    {
        super(parent, project);

        setSortPriority(UiSortPriority.TIMEPOINTS);
    }

    @Override
    public String getName()
    {
        return "Timepoints";
    }

    @Override
    public IScenarioProject getUnderlyingElement()
    {
        return (IScenarioProject) super.getUnderlyingElement();
    }

    @Override
    public Collection<UiElement> getChildren()
    {
        // create the timepoint items
        List<UiElement> timepoints = new ArrayList<UiElement>();
        for (ITimepoint timepoint : getUnderlyingElement().getTimepoints()) {
            timepoints.add(new UiTimepoint(this, timepoint, UiTimepointType.DEFAULT));
        }

        // assign the start/stop to the first and last item respectively
        ((UiTimepoint) timepoints.get(0)).setType(UiTimepointType.START);
        ((UiTimepoint) timepoints.get(timepoints.size() - 1)).setType(UiTimepointType.STOP);
        return timepoints;
    }

    @Override
    protected String getImagePath()
    {
        return "icons/Actions-chronometer-icon.png";
    }

    public ITimepoint getStartTimepoint()
    {
        return getUnderlyingElement().getStartTime();
    }

    public ITimepoint getStopTimepoint()
    {
        return getUnderlyingElement().getStopTime();
    }

    public ITimepoint getPreviousTimepoint(long time)
    {
        ITimepoint timepoint = TimeUtils.fromSeconds(time);
        return getUnderlyingElement().getPreviousTimepoint(timepoint);
    }

    public ITimepoint getNextTimepoint(long time)
    {
        ITimepoint timepoint = TimeUtils.fromSeconds(time);
        return getUnderlyingElement().getNextTimepoint(timepoint);
    }
}
