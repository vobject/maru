package maru.core.model.net;

import maru.core.model.ICentralBody;
import maru.core.model.IGroundstation;
import maru.core.model.IScenarioElement;
import maru.core.model.IScenarioProject;
import maru.core.model.ISpacecraft;
import maru.core.model.ITimepoint;
import maru.core.model.IVisibleElement;

public class NetworkScenarioModelAdapter extends NetworkScenarioModelListener
{
    @Override
    public void scenarioCreated(IScenarioProject element)
    {
        sendMessage(NetworkMessageID.SCENARIO_CREATED, element);
    }

    @Override
    public void scenarioAdded(IScenarioProject element)
    {
        sendMessage(NetworkMessageID.SCENARIO_ADDED, element);
    }

    @Override
    public void scenarioRemoved(IScenarioProject element)
    {
        sendMessage(NetworkMessageID.SCENARIO_REMOVED, element);
    }

    @Override
    public void elementAdded(IScenarioElement element)
    {
        sendMessage(NetworkMessageID.ELEMENT_ADDED, element);
    }

    @Override
    public void elementRemoved(IScenarioElement element)
    {
        sendMessage(NetworkMessageID.ELEMENT_REMOVED, element);
    }

    @Override
    public void elementRenamed(IScenarioElement element)
    {
        sendMessage(NetworkMessageID.ELEMENT_RENAMED, element);
    }

    @Override
    public void elementCommented(IScenarioElement element)
    {
        sendMessage(NetworkMessageID.ELEMENT_COMMENTED, element);
    }

    @Override
    public void elementColorChanged(IVisibleElement element)
    {
        sendMessage(NetworkMessageID.ELEMENT_COLOR_CHANGED, element);
    }

    @Override
    public void elementImageChanged(IVisibleElement element)
    {
        sendMessage(NetworkMessageID.ELEMENT_IMAGE_CHANGED, element);
    }

    @Override
    public void centralbodyImageChanged(ICentralBody element)
    {
        sendMessage(NetworkMessageID.CENTRAL_BODY_IMAGE_CHANGED, element);
    }

    @Override
    public void centralbodyGmChanged(ICentralBody element)
    {
        sendMessage(NetworkMessageID.CENTRAL_BODY_GM_CHANGED, element);
    }

    @Override
    public void centralbodyEquatorialRadiusChanged(ICentralBody element)
    {
        sendMessage(NetworkMessageID.CENTRAL_BODY_EQUATORIAL_RADIUS_CHANGED, element);
    }

    @Override
    public void centralbodyFlatteningChanged(ICentralBody element)
    {
        sendMessage(NetworkMessageID.CENTRAL_BODY_FLATTENING_CHANGED, element);
    }

    @Override
    public void elementInitialCoordinateChanged(IGroundstation element)
    {
        sendMessage(NetworkMessageID.GROUNDSTATION_INITIAL_COORDINATE_CHANGED, element);
    }

    @Override
    public void elementInitialCoordinateChanged(ISpacecraft element)
    {
        sendMessage(NetworkMessageID.SPACECRAFT_INITIAL_COORDINATE_CHANGED, element);
    }

    @Override
    public void propagatablesTimeChanged(IScenarioProject element)
    {
        sendMessage(NetworkMessageID.PROPAGATABLES_TIME_CHANGED, element);
    }

    @Override
    public void timepointStartChanged(ITimepoint element)
    {
        sendMessage(NetworkMessageID.TIMEPOINT_START_CHANGED, element);
    }

    @Override
    public void timepointStopChanged(ITimepoint element)
    {
        sendMessage(NetworkMessageID.TIMEPOINT_STOP_CHANGED, element);
    }

    @Override
    public void timepointCurrentChanged(ITimepoint element)
    {
        sendMessage(NetworkMessageID.TIMEPOINT_CURRENT_CHANGED, element);
    }

    @Override
    public void timepointAdded(ITimepoint element)
    {
        sendMessage(NetworkMessageID.TIMEPOINT_ADDED, element);
    }

    @Override
    public void timepointRemoved(ITimepoint element)
    {
        sendMessage(NetworkMessageID.TIMEPOINT_REMOVED, element);
    }

    @Override
    public void timepointChanged(ITimepoint element)
    {
        sendMessage(NetworkMessageID.TIMEPOINT_CHANGED, element);
    }
}
