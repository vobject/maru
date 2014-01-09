package maru.ui.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import maru.core.model.CoreModel;
import maru.core.model.ICentralBody;
import maru.core.model.ICoordinate;
import maru.core.model.IGroundstation;
import maru.core.model.IPropagatable;
import maru.core.model.IPropagationListener;
import maru.core.model.IScenarioElement;
import maru.core.model.IScenarioProject;
import maru.core.model.ISpacecraft;
import maru.core.model.ITimepoint;

public class UiProject extends UiParent implements IPropagationListener,
                                                   IUiTimelineSettingsListener
{
    private UiCentralBody uiCentralBody;
    private final UiGroundstationContrainer uiGroundstationContainer;
    private final UiSpacecraftContrainer uiSatelliteContainer;
    private final UiTimepointContrainer uiTimepointContrainer;

    private final Collection<IPropagationListener> propagationListeners;

    // the currently configured step size of the timeline control
    private long stepSize = 20;

    private boolean realtimeMode;

    public UiProject(IScenarioProject project)
    {
        super(null, project);

        uiCentralBody = new UiCentralBody(this, project.getCentralBody());
        uiGroundstationContainer = new UiGroundstationContrainer(this, project.getGroundstationContainer());
        uiSatelliteContainer = new UiSpacecraftContrainer(this, project.getSpacecraftContainer());
        uiTimepointContrainer = new UiTimepointContrainer(this, project);

        propagationListeners = new ArrayList<>();

        // register the UiProject for propagation changes of every
        // element in the newly created project.
        CoreModel.getDefault().addPropagationListener(project, this);

        // we want to get changes done to the timeline
        UiModel.getDefault().addUiTimelineSettingsListener(this);
    }

    @Override
    public UiProject getUiProject()
    {
        return this;
    }

    @Override
    public UiElement getChild(IScenarioElement child)
    {
        if (uiCentralBody.getUnderlyingElement() == child) {
            return uiCentralBody;
        }

        UiElement gsElement = uiGroundstationContainer.getChild(child);
        if (gsElement != null) {
            return gsElement;
        }

        UiElement satElement = uiSatelliteContainer.getChild(child);
        if (satElement != null) {
            return satElement;
        }
        return null;
    }

    @Override
    public UiElement getChild(String childName)
    {
        if (uiCentralBody.getName().equals(childName)) {
            return uiCentralBody;
        }

        UiElement gsElement = uiGroundstationContainer.getChild(childName);
        if (gsElement != null) {
            return gsElement;
        }

        UiElement satElement = uiSatelliteContainer.getChild(childName);
        if (satElement != null) {
            return satElement;
        }
        return null;
    }

    @Override
    public IScenarioProject getUnderlyingElement()
    {
        return (IScenarioProject) super.getUnderlyingElement();
    }

    @Override
    public Collection<UiElement> getChildren()
    {
        Collection<UiElement> children = new ArrayList<>();
        children.add(uiCentralBody);
        children.add(uiGroundstationContainer);
        children.add(uiSatelliteContainer);
        children.add(uiTimepointContrainer);
        return children;
    }

    @Override
    public void addUiElement(IScenarioElement element)
    {
        if (element instanceof ICentralBody)
        {
            ICentralBody centralBody = ((ICentralBody) element);
            uiCentralBody = new UiCentralBody(this, centralBody);
        }
        else if (element instanceof IGroundstation)
        {
            IGroundstation groundstation = (IGroundstation) element;
            uiGroundstationContainer.addUiElement(groundstation);

            // the element is probably interested in the current time.
            // and we are interested in the elements position.
            CoreModel.getDefault().addPropagationListener(groundstation, this);

            groundstation.currentTimeChanged(getCurrentTime());
        }
        else if (element instanceof ISpacecraft)
        {
            ISpacecraft satellite = (ISpacecraft) element;
            uiSatelliteContainer.addUiElement(satellite);

            // the element is probably interested in the current time.
            // and we are interested in the elements position.
            CoreModel.getDefault().addPropagationListener(satellite, this);

            satellite.currentTimeChanged(getCurrentTime());
        }
        else if (element instanceof ITimepoint)
        {
            uiTimepointContrainer.addUiElement(element);
        }
    }

    @Override
    public void updateUiElement(IScenarioElement element)
    {
        if (element instanceof ICentralBody)
        {
            // swap the current central body for the new one.
            uiCentralBody = new UiCentralBody(this, (ICentralBody) element);

            // make all propagatables adapt to the new central body
            CoreModel.getDefault().changePropagatablesCentralBody(getUnderlyingElement(), true);
        }
        else if (element instanceof IGroundstation)
        {
            // the current UiProject was requested to update because one
            // of its underlying groundstation objects changed.
            uiGroundstationContainer.updateUiElement(element);
        }
        else if (element instanceof ISpacecraft)
        {
            // the current UiProject was requested to update because one
            // of its underlying satellite objects changed.
            uiSatelliteContainer.updateUiElement(element);
        }
        else if (element instanceof ITimepoint)
        {
            ITimepoint timepoint = (ITimepoint) element;
            uiTimepointContrainer.updateUiElement(element);

            if (timepoint == getUnderlyingElement().getCurrentTime())
            {
                IScenarioProject project = getUnderlyingElement();
                long time = timepoint.getTime();
                CoreModel.getDefault().changePropagatablesTime(project, time, true);
            }
        }
    }

    @Override
    public void removeUiElement(IScenarioElement element)
    {
        if (element instanceof ICentralBody)
        {
            uiCentralBody = null;
        }
        else if (element instanceof IGroundstation)
        {
            IGroundstation groundstation = (IGroundstation) element;

            // remove all links from and to the element.
            CoreModel.getDefault().removePropagationListener(groundstation, this);

            uiGroundstationContainer.removeUiElement(groundstation);
        }
        else if (element instanceof ISpacecraft)
        {
            ISpacecraft satellite = (ISpacecraft) element;

            // remove all links from and to the element.
            CoreModel.getDefault().removePropagationListener(satellite, this);

            uiSatelliteContainer.removeUiElement(satellite);
        }
        else if (element instanceof ITimepoint)
        {
            uiTimepointContrainer.removeUiElement(element);
        }
    }

    public void addPropagationListener(IPropagationListener listener)
    {
        if (!propagationListeners.contains(listener)) {
            propagationListeners.add(listener);
        }
    }

    public void removePropagationListener(IPropagationListener listener)
    {
        if (propagationListeners.contains(listener)) {
            propagationListeners.remove(listener);
        }
    }

    @Override
    public void propagationChanged(IPropagatable element, ICoordinate position)
    {
        for (IPropagationListener listener : propagationListeners) {
            listener.propagationChanged(element, position);
        }
    }

    @Override
    public void timeStepChanged(UiProject project, long stepSize)
    {
        setStepSize(stepSize);
    }

    public UiGroundstationContrainer getGroundstationContrainer()
    {
        return uiGroundstationContainer;
    }

    public UiSpacecraftContrainer getSatelliteContrainer()
    {
        return uiSatelliteContainer;
    }

    public UiTimepointContrainer getTimepointContrainer()
    {
        return uiTimepointContrainer;
    }

    public long getStartTime()
    {
        return getUnderlyingElement().getStartTime().getTime();
    }

    public void setStartTime(long time)
    {
        ITimepoint tp = getUnderlyingElement().getStartTime();
        CoreModel.getDefault().changeTimepoint(tp, time, true);
    }

    public long getStopTime()
    {
        return getUnderlyingElement().getStopTime().getTime();
    }

    public void setStopTime(long time)
    {
        ITimepoint tp = getUnderlyingElement().getStopTime();
        CoreModel.getDefault().changeTimepoint(tp, time, true);
    }

    public long getCurrentTime()
    {
        if (realtimeMode) {
            return new Date().getTime() / 1000;
        }
        return getUnderlyingElement().getCurrentTime().getTime();
    }

    public void setCurrentTime(long time)
    {
        ITimepoint tp = getUnderlyingElement().getCurrentTime();
        CoreModel.getDefault().changeTimepoint(tp, time, true);
    }

    public void setPropagatablesTime(long time)
    {
        CoreModel.getDefault().changePropagatablesTime(getUnderlyingElement(), time, true);
    }

    public void saveCurrentTimepoint()
    {
        long time = getCurrentTime();
        CoreModel.getDefault().addTimepoint(getUnderlyingElement(), time, true);
    }

    public ITimepoint getPreviousTimepoint()
    {
        long time = getCurrentTime();
        return uiTimepointContrainer.getPreviousTimepoint(time);
    }

    public ITimepoint getNextTimepoint()
    {
        long time = getCurrentTime();
        return uiTimepointContrainer.getNextTimepoint(time);
    }

    public long getStepSize()
    {
        return stepSize;
    }

    public void setStepSize(long stepSize)
    {
        this.stepSize = stepSize;
    }

    public boolean isRealtimeMode()
    {
        return realtimeMode;
    }

    public void setRealtimeMode(boolean realtimeMode)
    {
        this.realtimeMode = realtimeMode;
    }

    public void remove()
    {
        CoreModel.getDefault().removePropagationListener(getUnderlyingElement(), this);
    }
}
