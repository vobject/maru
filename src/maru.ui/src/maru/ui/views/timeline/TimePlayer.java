package maru.ui.views.timeline;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import maru.ui.MaruUIPlugin;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

class TimePlayer
{
    private enum PlaybackState
    {
        BACKWARD,
        FORWARD,
        REALTIME,
        PAUSE
    }

    private class PlaybackTimer implements Runnable
    {
        @Override
        public void run()
        {
            if (currentState == PlaybackState.PAUSE) {
                return;
            }
            notifyPlayback();

            Display display = Display.getDefault();
            if (!display.isDisposed()) {
                double waitTime = 1000.0 * currentStepSize / currentMultiplicator;
                display.timerExec((int) waitTime, this);
            }
        }
    };

    private static final String BUTTON_IMAGE_RECORD = "icons/Actions-media-record-icon.png";
    private static final String BUTTON_IMAGE_PREV_TP = "icons/Actions-media-skip-backward-icon.png";
    private static final String BUTTON_IMAGE_PLAY_BACKWARD = "icons/Actions-media-playback-start--backward-icon.png";
    private static final String BUTTON_IMAGE_PAUSE = "icons/Actions-media-playback-pause-icon.png";
    private static final String BUTTON_IMAGE_PLAY_FORWARD = "icons/Actions-media-playback-start-icon.png";
    private static final String BUTTON_IMAGE_NEXT_TP = "icons/Actions-media-skip-forward-icon.png";

    private static final String BUTTON_TOOLTIP_RECORD = "Save current time";
    private static final String BUTTON_TOOLTIP_PREV_TP = "Previous timepoint";
    private static final String BUTTON_TOOLTIP_PLAY_BACKWARD = "Play backward";
    private static final String BUTTON_TOOLTIP_PAUSE = "Pause";
    private static final String BUTTON_TOOLTIP_PLAY_FORWARD = "Play forward";
    private static final String BUTTON_TOOLTIP_NEXT_TP = "Next timepoint";

    private Button record;
    private Button previousTimepoint;
    private Button playBackward;
    private Button pause;
    private Button playForward;
    private Button nextTimepoint;

    private final List<ITimePlayerListener> playerListeners = new ArrayList<>();

    private PlaybackState currentState = PlaybackState.PAUSE;
    private double currentMultiplicator;
    private long currentStepSize;
    private final PlaybackTimer playbackTimer;

    private boolean realtimeEnabled = false;
    private double lastMultiplicator;
    private long lastStepSize;

    public TimePlayer(Composite parentControl)
    {
        playbackTimer = new PlaybackTimer();
        createPartControl(parentControl);
    }

    public void enable()
    {
        stop();
        setButtonStatePause();
    }

    public void disable()
    {
        stop();

        Display.getDefault().syncExec(new Runnable() {
            @Override
            public void run() {
                record.setEnabled(false);
                previousTimepoint.setEnabled(false);
                playBackward.setEnabled(false);
                pause.setEnabled(false);
                playForward.setEnabled(false);
                nextTimepoint.setEnabled(false);
            }
        });

        if (realtimeEnabled)
        {
            currentMultiplicator = lastMultiplicator;
            currentStepSize = lastStepSize;
            realtimeEnabled = false;
        }
    }

    public void disableForRealtime()
    {
        stop();

        Display.getDefault().syncExec(new Runnable() {
            @Override
            public void run() {
                record.setEnabled(false);
                previousTimepoint.setEnabled(false);
                playBackward.setEnabled(false);
                pause.setEnabled(false);
                playForward.setEnabled(false);
                nextTimepoint.setEnabled(false);
            }
        });
    }

    public void stop()
    {
        currentState = PlaybackState.PAUSE;

        Display.getDefault().syncExec(new Runnable() {
            @Override
            public void run() {
                Display.getDefault().timerExec(-1, playbackTimer);
            }
        });
    }

    public void changeSpeedMultiplicator(double multiplicator)
    {
        currentMultiplicator = multiplicator;
    }

    public void changeStepSize(long stepSize)
    {
        currentStepSize = stepSize;
    }

    public void changeRealtimeMode(boolean realtime)
    {
        if (realtime)
        {
            // save the current settings
            lastMultiplicator = currentMultiplicator;
            lastStepSize = currentStepSize;

            currentState = PlaybackState.REALTIME;
            currentMultiplicator = 1.0;
            currentStepSize = 1;

            startPlayback();
            realtimeEnabled = true;
        }
        else
        {
            stop();
            realtimeEnabled = false;

            currentMultiplicator = lastMultiplicator;
            currentStepSize = lastStepSize;
        }
    }

    public double getSpeedMultiplicator()
    {
        return currentMultiplicator;
    }

    public double getStepSize()
    {
        return currentStepSize;
    }

    public boolean getRealtimeEnabled()
    {
        return realtimeEnabled;
    }

    public void addTimePlayerListener(ITimePlayerListener listener)
    {
        if (!playerListeners.contains(listener)) {
            playerListeners.add(listener);
        }
    }

    public void removeTimePlayerListener(ITimePlayerListener listener)
    {
        if (!playerListeners.contains(listener)) {
            playerListeners.remove(listener);
        }
    }

    private void createPartControl(Composite parent)
    {
        GridLayout containerLayout = new GridLayout(6, true);
        containerLayout.marginWidth = 0;
        containerLayout.marginHeight = 0;

        GridData containerData = new GridData();
        containerData.horizontalAlignment = GridData.FILL;
        containerData.verticalAlignment = GridData.FILL;
        containerData.grabExcessHorizontalSpace = true;
        containerData.grabExcessVerticalSpace = true;

        Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(containerLayout);
        container.setLayoutData(containerData);

        GridData buttonData = new GridData();
        buttonData.horizontalAlignment = GridData.FILL;
        buttonData.verticalAlignment = GridData.FILL;
        buttonData.grabExcessHorizontalSpace = true;
        buttonData.grabExcessVerticalSpace = true;

        record = createButton(container, buttonData, BUTTON_IMAGE_RECORD, BUTTON_TOOLTIP_RECORD);
        record.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                saveCurrentTimepoint();
            }
        });

        previousTimepoint = createButton(container, buttonData, BUTTON_IMAGE_PREV_TP, BUTTON_TOOLTIP_PREV_TP);
        previousTimepoint.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                previousTimepoint();
            }
        });

        playBackward = createButton(container, buttonData, BUTTON_IMAGE_PLAY_BACKWARD, BUTTON_TOOLTIP_PLAY_BACKWARD);
        playBackward.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                playBackward();
            }
        });

        pause = createButton(container, buttonData, BUTTON_IMAGE_PAUSE, BUTTON_TOOLTIP_PAUSE);
        pause.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                pausePlayback();
            }
        });

        playForward = createButton(container, buttonData, BUTTON_IMAGE_PLAY_FORWARD, BUTTON_TOOLTIP_PLAY_FORWARD);
        playForward.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                playForward();
            }
        });

        nextTimepoint = createButton(container, buttonData, BUTTON_IMAGE_NEXT_TP, BUTTON_TOOLTIP_NEXT_TP);
        nextTimepoint.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                nextTimepoint();
            }
        });
    }

    private Button createButton(Composite container, GridData data, String imgPath, String tooltip)
    {
        Button button = new Button(container, SWT.PUSH);
        button.setImage(MaruUIPlugin.getImage(imgPath));
        button.setToolTipText(tooltip);
        button.setLayoutData(data);
        button.setEnabled(false);
        return button;
    }

    private void saveCurrentTimepoint()
    {
        notifySaveCurrentTimepoint();
    }

    private void previousTimepoint()
    {
        notifySelectPreviousTimepoint();
    }

    private void playBackward()
    {
        currentState = PlaybackState.BACKWARD;

        setButtonStatePlaying();
        startPlayback();
    }

    private void pausePlayback()
    {
        stop();
        setButtonStatePause();
    }

    private void playForward()
    {
        currentState = PlaybackState.FORWARD;

        setButtonStatePlaying();
        startPlayback();
    }

    private void nextTimepoint()
    {
        notifySelectNextTimepoint();
    }

    private void setButtonStatePause()
    {
        Display.getDefault().syncExec(new Runnable() {
            @Override
            public void run() {
                record.setEnabled(true);
                previousTimepoint.setEnabled(true);
                playBackward.setEnabled(true);
                pause.setEnabled(false);
                playForward.setEnabled(true);
                nextTimepoint.setEnabled(true);
            }
        });
    }

    private void setButtonStatePlaying()
    {
        Display.getDefault().syncExec(new Runnable() {
            @Override
            public void run() {
                playBackward.setEnabled(false);
                pause.setEnabled(true);
                playForward.setEnabled(false);
            }
        });
    }

    private void startPlayback()
    {
        Display.getDefault().syncExec(new Runnable() {
            @Override
            public void run() {
                Display.getDefault().timerExec(100, playbackTimer);
            }
        });
    }

    private void notifySaveCurrentTimepoint()
    {
        for (ITimePlayerListener listener : playerListeners) {
            listener.saveCurrentTimepoint();
        }
    }

    private void notifySelectPreviousTimepoint()
    {
        for (ITimePlayerListener listener : playerListeners) {
            listener.selectPreviousTimepoint();
        }
    }

    private void notifySelectNextTimepoint()
    {
        for (ITimePlayerListener listener : playerListeners) {
            listener.selectNextTimepoint();
        }
    }

    private void notifyPlayback()
    {
        for (ITimePlayerListener listener : playerListeners)
        {
            if (currentState == PlaybackState.BACKWARD ) {
                listener.playBackward((int) currentStepSize);
            } else if (currentState == PlaybackState.FORWARD) {
                listener.playForward((int) currentStepSize);
            } else if (currentState == PlaybackState.REALTIME) {
                listener.playRealtime(new Date().getTime() / 1000);
            }
        }
    }
}
