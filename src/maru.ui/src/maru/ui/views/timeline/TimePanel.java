package maru.ui.views.timeline;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import maru.core.utils.TimeUtils;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

enum TimeLabelType
{
    Start,
    Stop,
    Current;

    String getDescription()
    {
        switch (this)
        {
            case Start:
                return "Start: ";
            case Stop:
                return "Stop: ";
            case Current:
                return "Current: ";
        }
        throw new IllegalStateException();
    }

    RGB getRgb()
    {
        switch (this)
        {
            case Start:
                return new RGB(192, 240, 192);
            case Stop:
                return new RGB(255, 224, 240);
            case Current:
                return new RGB(240, 255, 192);
        }
        throw new IllegalStateException();
    }
}

class TimePanel
{
    class TimeLabel
    {
        private final Label label;

        private final IInputValidator dateValidator = new IInputValidator()
        {
            @Override
            public String isValid(String newText)
            {
                try
                {
                    TimeUtils.fromString(newText);
                    return null;
                }
                catch (ParseException e)
                {
                    return "Invalid time input.";
                }
            }
        };

        TimeLabel(Composite parent, int style, final TimeLabelType type)
        {
            this.label = new Label(parent, style);
            label.setText(TimeUtils.ISO8601_PLACEHOLDER);

            label.addMouseListener(new MouseListener() {
                @Override
                public void mouseUp(MouseEvent e)
                {
                    if (!enabled) {
                        return;
                    }

                    InputDialog dlg = new InputDialog(Display.getCurrent().getActiveShell(),
                                                      "Change Time",
                                                      "Enter " + type.toString() + " Time in UTC",
                                                      label.getText(),
                                                      dateValidator);
                    if (dlg.open() != Window.OK) {
                        return;
                    }

                    try
                    {
                        notifyTimeChanged(type, TimeUtils.fromString(dlg.getValue()).getTime());
                    }
                    catch (ParseException ex)
                    {
                        // should never happen because isValid() handles exceptions
                    }
                }

                @Override
                public void mouseDown(MouseEvent e) { }

                @Override
                public void mouseDoubleClick(MouseEvent e) { }
            });

            final Color color = new Color(null, type.getRgb());
            label.setBackground(color);
            label.addDisposeListener(new DisposeListener() {
                @Override
                public void widgetDisposed(DisposeEvent e)
                {
                    color.dispose();
                }
            });
        }

        void setText(final String string)
        {
            Display.getDefault().syncExec(new Runnable() {
                @Override
                public void run() {
                    label.setText(string);
                }
            });
        }

        void reset()
        {
            setText(TimeUtils.ISO8601_PLACEHOLDER);
        }
    }

    private TimeLabel startLabel;
    private TimeLabel stopLabel;
    private TimeLabel currentLabel;

    private long startTime;
    private long stopTime;
    private long currentTime;

    private long lastCurrentTime;

    private final List<ITimeChangedListener> timeChangeListeners = new ArrayList<>();

    /** enable of disable changes to the control. */
    private boolean enabled;

    public TimePanel(Composite parentControl)
    {
        createPartControl(parentControl);
    }

    public void enable()
    {
        // allow for time changes and notifications
        enabled = true;

        if (lastCurrentTime != 0)
        {
            // we were just re-enabled after a real-time session
            currentTime = lastCurrentTime;
            lastCurrentTime = 0;
        }
    }

    public void disable()
    {
        startLabel.reset();
        stopLabel.reset();
        currentLabel.reset();

        startTime = 0;
        stopTime = 0;
        currentTime = 0;
        lastCurrentTime = 0;

        // disable changes to the controls and  listener notifications
        enabled = false;
    }

    public void disableForRealtime()
    {
        lastCurrentTime = currentTime;

        // disable changes to the controls and  listener notifications
        enabled = false;
    }

    public void changeStartTimeLabel(long time)
    {
        startLabel.setText(TimeUtils.asISO8601(time));
        startTime = time;
    }

    public void changeStopTimeLabel(long time)
    {
        stopLabel.setText(TimeUtils.asISO8601(time));
        stopTime = time;
    }

    public void changeCurrentTimeLabel(long time)
    {
        currentLabel.setText(TimeUtils.asISO8601(time));
        currentTime = time;
    }

    public void shiftCurrentTimeLabel(long step)
    {
        long newCurrentTime = currentTime + step;

        if (newCurrentTime < startTime) {
            changeCurrentTimeLabel(startTime);
        } else if (newCurrentTime > stopTime) {
            changeCurrentTimeLabel(stopTime);
        } else {
            changeCurrentTimeLabel(newCurrentTime);
        }
    }

    public long getStartTime()
    {
        return startTime;
    }

    public long getStopTime()
    {
        return stopTime;
    }

    public long getCurrentTime()
    {
        return currentTime;
    }

    public void addTimeChangedListener(ITimeChangedListener listener)
    {
        if (!timeChangeListeners.contains(listener)) {
            timeChangeListeners.add(listener);
        }
    }

    public void removeTimeChangedListener(ITimeChangedListener listener)
    {
        if (!timeChangeListeners.contains(listener)) {
            timeChangeListeners.remove(listener);
        }
    }

    private void createPartControl(Composite parent)
    {
        GridLayout containerLayout = new GridLayout(6, false);
        containerLayout.marginWidth = 2;
        containerLayout.marginHeight = 0;

        Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(containerLayout);

        startLabel = createTimeLabel(container, TimeLabelType.Start);
        stopLabel = createTimeLabel(container, TimeLabelType.Stop);
        currentLabel = createTimeLabel(container, TimeLabelType.Current);
    }

    private TimeLabel createTimeLabel(Composite parent, TimeLabelType type)
    {
        Label labelDescription = new Label(parent, SWT.NONE);
        labelDescription.setText(type.getDescription());

        return new TimeLabel(parent, SWT.BORDER, type);
    }

    private void notifyTimeChanged(TimeLabelType label, long time)
    {
        if (!enabled) {
            return;
        }

        for (ITimeChangedListener listener : timeChangeListeners) {
            listener.timeChanged(label, time);
        }
    }
}
