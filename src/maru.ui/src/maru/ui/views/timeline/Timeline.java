package maru.ui.views.timeline;

import java.util.ArrayList;
import java.util.Collection;

import maru.core.model.IScenarioProject;
import maru.core.model.ITimepoint;
import maru.ui.internal.model.UiProjectModelManager;
import maru.ui.model.IUiTimelineSettingsListener;
import maru.ui.model.IUiTimelineSettingsProvider;
import maru.ui.model.UiElement;
import maru.ui.model.UiModel;
import maru.ui.model.UiProject;
import maru.ui.model.UiTimepoint;
import maru.ui.views.ScenarioModelViewPart;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.orekit.time.AbsoluteDate;

public class Timeline extends ScenarioModelViewPart
                      implements IUiTimelineSettingsProvider
{
    private TimePanel panel;
    private TimePlayer player;
    private TimeSettings settings;
    private TimeSlider slider;

    private final Collection<IUiTimelineSettingsListener> settingsListeners = new ArrayList<>();

    @Override
    public void createPartControl(Composite parent)
    {
        Composite container = new Composite(parent, SWT.NONE);
        GridLayout containerLayout = new GridLayout(1, false);
        containerLayout.marginWidth = 0;
        containerLayout.marginHeight = 0;
        container.setLayout(containerLayout);

        // time panel and player
        createTopControls(container);

        // player settings and slider
        createBottomControls(container);

        // we need to know which project is currently selected.
        UiModel.getDefault().addUiProjectSelectionListener(this);

        // make the UiProjectModelManager listen to timeline settings changes.
        addTimelineSettingsListener(UiProjectModelManager.getDefault());
    }

    private void createTopControls(Composite container)
    {
        GridLayout subContainerLayout = new GridLayout(2, false);
        subContainerLayout.marginWidth = 0;
        subContainerLayout.marginHeight = 0;

        GridData subContainerData = new GridData();
        subContainerData.horizontalAlignment = GridData.FILL;
        subContainerData.verticalAlignment = GridData.FILL;
        subContainerData.grabExcessHorizontalSpace = true;
        subContainerData.grabExcessVerticalSpace = true;

        Composite subContainer = new Composite(container, SWT.NONE);
        subContainer.setLayout(subContainerLayout);
        subContainer.setLayoutData(subContainerData);

        createTimePanel(subContainer);
        createTimeSlider(subContainer);
    }

    private void createBottomControls(Composite container)
    {
        GridLayout subContainerLayout = new GridLayout(2, false);
        subContainerLayout.marginWidth = 0;
        subContainerLayout.marginHeight = 0;

        GridData subContainerData = new GridData();
        subContainerData.horizontalAlignment = GridData.FILL;
        subContainerData.verticalAlignment = GridData.FILL;
        subContainerData.grabExcessHorizontalSpace = true;
        subContainerData.grabExcessVerticalSpace = true;

        Composite subContainer = new Composite(container, SWT.NONE);
        subContainer.setLayout(subContainerLayout);
        subContainer.setLayoutData(subContainerData);

        createTimePlayer(subContainer);
        createTimeSettings(subContainer);
    }

    @Override
    public void setFocus()
    {
        slider.setFocus();
    }

    @Override
    public void dispose()
    {
        super.dispose();

        shutdownTimePlayer();
        removeTimelineSettingsListener(UiProjectModelManager.getDefault());
        UiModel.getDefault().removeUiProjectSelectionListener(this);
    }

    @Override
    public void addTimelineSettingsListener(IUiTimelineSettingsListener listener)
    {
        if (!settingsListeners.contains(listener)) {
            settingsListeners.add(listener);
        }
    }

    @Override
    public void removeTimelineSettingsListener(IUiTimelineSettingsListener listener)
    {
        if (settingsListeners.contains(listener)) {
            settingsListeners.remove(listener);
        }
    }

    @Override
    public void activeProjectChanged(UiProject project, UiElement element)
    {
        // reconfigure the controls for the new project
        configureControls(project);

        // handle the selected element in the newly selected project
        activeElementChanged(project, element);
    }

    @Override
    public void activeElementChanged(UiProject project, UiElement element)
    {
        // do not allow changing the current time by selection of timepoints
        // when the real-time mode is enabled.
        if (!project.isRealtimeMode() && element instanceof UiTimepoint)
        {
            // jump to the timepoint that was selected in the UI
            getCurrentProject().setCurrentTime(((UiTimepoint) element).getTime());
        }
    }

    @Override
    public void scenarioRemoved(IScenarioProject project)
    {
        if (project != getCurrentScenarioProject()) {
            // we only care about changes to the current project.
            return;
        }

        disableTimePanel();
        disableTimePlayer();
        disableTimeSettings();
        disableTimeSlider();
    }

    @Override
    public void timepointStartChanged(ITimepoint element)
    {
        IScenarioProject currentProject = getCurrentScenarioProject();
        if (currentProject == null) {
            return;
        }

        if (element.getScenarioProject() != currentProject) {
            return;
        }
        changeStartTime(element.getTime());
    }

    @Override
    public void timepointStopChanged(ITimepoint element)
    {
        IScenarioProject currentProject = getCurrentScenarioProject();
        if (currentProject == null) {
            return;
        }

        if (element.getScenarioProject() != currentProject) {
            return;
        }
        changeStopTime(element.getTime());
    }

    @Override
    public void timepointCurrentChanged(ITimepoint element)
    {
        IScenarioProject currentProject = getCurrentScenarioProject();
        if (currentProject == null) {
            return;
        }

        if (element.getScenarioProject() != currentProject) {
            return;
        }

        if (player.getRealtimeEnabled()) {
            return;
        }
        changeCurrentTime(element.getTime());
    }

    public IScenarioProject getCurrentScenarioProject()
    {
        UiProject currentProject = getCurrentProject();
        if (currentProject == null) {
            return null;
        }
        return currentProject.getUnderlyingElement();
    }

    private void changeStartTime(AbsoluteDate startTime)
    {
        setTimeSliderRange(startTime, getStopTime());
        setStartTimePanel(startTime);
    }

    private void changeStopTime(AbsoluteDate stopTime)
    {
        setTimeSliderRange(getStartTime(), stopTime);
        setStopTimePanel(stopTime);
    }

    private void changeCurrentTime(AbsoluteDate currentTime)
    {
        setTimeSlider(currentTime);
        setCurrentTimePanel(currentTime);
    }

    private void notifyTimeStepChanged(long stepSize)
    {
        for (IUiTimelineSettingsListener listener : settingsListeners) {
            listener.timeStepChanged(getCurrentProject(), stepSize);
        }
    }

    private void configureControls(UiProject project)
    {
        // shutdown all sub components
        disableTimePanel();
        disableTimePlayer();
        disableTimeSettings();
        disableTimeSlider();

        AbsoluteDate startTime = project.getStartTime();
        AbsoluteDate stopTime = project.getStopTime();
        AbsoluteDate currentTime = project.getCurrentTime();
        long stepSize = project.getStepSize();

        setTimePanel(startTime, stopTime, currentTime);
        setTimeSliderRange(startTime, stopTime);
        setTimeSlider(currentTime);
        setTimeSettings(stepSize);
        setTimePlayerStepSize(stepSize);

        // enable all sub components for the new project
        enableTimePanel();
        enableTimePlayer();
        enableTimeSettings();
        enableTimeSlider();
    }

    private void createTimePanel(Composite container)
    {
        panel = new TimePanel(container);
        panel.addTimeChangedListener(new ITimeChangedListener() {
            @Override
            public void timeChanged(TimeLabelType label, AbsoluteDate date)
            {
                switch (label)
                {
                    case Start:
                        getCurrentProject().setStartTime(date);
                        break;
                    case Stop:
                        getCurrentProject().setStopTime(date);
                        break;
                    case Current:
                        getCurrentProject().setCurrentTime(date);
                        break;
                }
            }
        });
    }

    private void enableTimePanel()
    {
        panel.enable();
    }

    private void disableTimePanel()
    {
        panel.disable();
    }

    private void setTimePanel(AbsoluteDate start, AbsoluteDate stop, AbsoluteDate current)
    {
        panel.changeStartTimeLabel(start);
        panel.changeStopTimeLabel(stop);
        panel.changeCurrentTimeLabel(current);
    }

    private void setStartTimePanel(AbsoluteDate start)
    {
        panel.changeStartTimeLabel(start);
    }

    private void setStopTimePanel(AbsoluteDate stop)
    {
        panel.changeStopTimeLabel(stop);
    }

    private void setCurrentTimePanel(AbsoluteDate current)
    {
        panel.changeCurrentTimeLabel(current);
    }

    private void shiftCurrentTimePanelBackward(long stepSize)
    {
        panel.shiftCurrentTimeLabel(-stepSize);
    }

    private void shiftCurrentTimePanelForward(long stepSize)
    {
        panel.shiftCurrentTimeLabel(stepSize);
    }

    private void setTimePanelRealtimeMode(boolean realtime)
    {
        if (realtime) {
            panel.disableForRealtime();
        } else {
            panel.enable();
        }
    }

    private AbsoluteDate getStartTime()
    {
        return panel.getStartTime();
    }

    private AbsoluteDate getStopTime()
    {
        return panel.getStopTime();
    }

    private AbsoluteDate getCurrentTime()
    {
        return panel.getCurrentTime();
    }

    private void createTimePlayer(Composite container)
    {
        player = new TimePlayer(container);
        player.addTimePlayerListener(new ITimePlayerListener() {
            @Override
            public void saveCurrentTimepoint()
            {
                getCurrentProject().saveCurrentTimepoint();
            }

            @Override
            public void selectPreviousTimepoint()
            {
                AbsoluteDate previousTime = getCurrentProject().getPreviousTimepoint().getTime();

                setCurrentTimePanel(previousTime);
                setTimeSlider(previousTime);
                getCurrentProject().setCurrentTime(previousTime);
            }

            @Override
            public void selectNextTimepoint()
            {
                AbsoluteDate nextTime = getCurrentProject().getNextTimepoint().getTime();

                setCurrentTimePanel(nextTime);
                setTimeSlider(nextTime);
                getCurrentProject().setCurrentTime(nextTime);
            }

            @Override
            public void playBackward(int stepSize)
            {
                shiftCurrentTimePanelBackward(stepSize);
                shiftTimeSliderBackward(stepSize);
                getCurrentProject().setCurrentTime(getCurrentTime());
            }

            @Override
            public void playForward(int stepSize)
            {
                shiftCurrentTimePanelForward(stepSize);
                shiftTimeSliderForward(stepSize);
                getCurrentProject().setCurrentTime(getCurrentTime());
            }

            @Override
            public void playRealtime(AbsoluteDate date)
            {
                setCurrentTimePanel(date);
                getCurrentProject().setPropagatablesTime(date);
            }
        });
    }

    private void setTimePlayerStepSize(long stepSize)
    {
        player.changeStepSize(stepSize);
    }

    private void setTimePlayerSpeedMultiplier(double multiplicator)
    {
        player.changeSpeedMultiplicator(multiplicator);
    }

    private void setTimePlayerRealtimeMode(boolean realtime)
    {
        if (realtime) {
            player.disableForRealtime();
        } else {
            player.enable();
        }
        player.changeRealtimeMode(realtime);
    }

    private void enableTimePlayer()
    {
        player.enable();
    }

    private void disableTimePlayer()
    {
        player.disable();
    }

    private void shutdownTimePlayer()
    {
        player.stop();
    }

    private void createTimeSettings(Composite parent)
    {
        settings = new TimeSettings(parent);
        settings.addSettingsChangedListener(new ISettingsChangedListener() {
            @Override
            public void stepSizeChanged(long stepSize)
            {
                setTimePlayerStepSize(stepSize);
                notifyTimeStepChanged(stepSize);
            }

            @Override
            public void playbackSpeedChanged(double multiplicator)
            {
                setTimePlayerSpeedMultiplier(multiplicator);
            }

            @Override
            public void realtimeModeChanged(boolean realtime)
            {
                setTimePanelRealtimeMode(realtime);
                setTimePlayerRealtimeMode(realtime);
                setTimeSettingsRealtimeMode(realtime);
                setTimeSliderRealtimeMode(realtime);

                getCurrentProject().setRealtimeMode(realtime);

                if (!realtime) {
                    // refresh when exiting real-time mode
                    getCurrentProject().setCurrentTime(getCurrentTime());
                }
            }
        });
        setTimePlayerSpeedMultiplier(settings.getSpeedMultiplicator());
    }

    private void enableTimeSettings()
    {
        settings.enable();
    }

    private void disableTimeSettings()
    {
        settings.disable();
    }

    private void setTimeSettings(double stepSize)
    {
        settings.changeStepSize(stepSize);
    }

    private void setTimeSettingsRealtimeMode(boolean realtime)
    {
        if (realtime) {
            settings.disableForRealtime();
        } else {
            settings.enable();
        }
    }

    private void createTimeSlider(Composite container)
    {
        slider = new TimeSlider(container);
        slider.addSliderChangedListener(new ISliderChangedListener() {
            @Override
            public void currentTimeChanged(AbsoluteDate date)
            {
                setCurrentTimePanel(date);
                getCurrentProject().setCurrentTime(date);
            }
        });
    }

    private void enableTimeSlider()
    {
        slider.enable();
    }

    private void disableTimeSlider()
    {
        slider.disable();
    }

    private void setTimeSliderRange(AbsoluteDate startTime, AbsoluteDate stopTime)
    {
        slider.changeRange(startTime, stopTime);
    }

    private void setTimeSlider(AbsoluteDate currentTime)
    {
        slider.changeCurrentTime(currentTime);
    }

    private void shiftTimeSliderBackward(long stepSize)
    {
        slider.shiftCurrentTime(-stepSize);
    }

    private void shiftTimeSliderForward(long stepSize)
    {
        slider.shiftCurrentTime(stepSize);
    }

    private void setTimeSliderRealtimeMode(boolean realtime)
    {
        if (realtime) {
            slider.disableForRealtime();
        } else {
            slider.enable();
        }
    }
}
