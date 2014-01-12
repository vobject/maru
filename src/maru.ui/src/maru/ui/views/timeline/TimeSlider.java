package maru.ui.views.timeline;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Slider;

public class TimeSlider
{
    private class TimeSliderListener implements Listener
    {
        @Override
        public void handleEvent(Event event)
        {
            refreshCurrentTime();
            notifyCurrentTimeChanged();
        }
    }

    private Slider slider;
    private TimeSliderListener sliderListener;

    private long startTime;
    private long stopTime;
    private long currentTime;

    private final List<ISliderChangedListener> sliderChangeListeners = new ArrayList<>();

    /** enable of disable changes to the control. */
    private boolean enabled;

    public TimeSlider(Composite parentControl)
    {
        createPartControl(parentControl);
    }

    public void enable()
    {
        // allow for notifications
        enabled = true;

        Display.getDefault().syncExec(new Runnable() {
            @Override
            public void run() {
                slider.setEnabled(true);
            }
        });
    }

    public void disable()
    {
        Display.getDefault().syncExec(new Runnable() {
            @Override
            public void run() {
                slider.setEnabled(false);
            }
        });

        // disable listener notifications
        enabled = false;
    }

    public void disableForRealtime()
    {
        Display.getDefault().syncExec(new Runnable() {
            @Override
            public void run() {
                slider.setEnabled(false);
            }
        });
    }

    public void setFocus()
    {
        Display.getDefault().syncExec(new Runnable() {
            @Override
            public void run() {
                slider.setFocus();
            }
        });
    }

    public void changeRange(long startTime, long stopTime)
    {
        this.startTime = startTime;
        this.stopTime = stopTime;

        final long periodSec = stopTime - startTime;

        Display.getDefault().syncExec(new Runnable() {
            @Override
            public void run() {
                slider.setMinimum(0);
                slider.setMaximum((int) periodSec);
                slider.setPageIncrement(slider.getMaximum() / 200);
            }
        });
    }

    public void changeCurrentTime(long currentTime)
    {
        this.currentTime = currentTime;
        refreshSliderControl();
    }

    public void shiftCurrentTime(long step)
    {
        long newCurrentTime = currentTime + step;

        if (newCurrentTime < startTime) {
            currentTime = startTime;
        } else if (newCurrentTime > stopTime) {
            currentTime = stopTime;
        } else {
            currentTime = newCurrentTime;
        }
        refreshSliderControl();
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

    public void addSliderChangedListener(ISliderChangedListener listener)
    {
        if (!sliderChangeListeners.contains(listener)) {
            sliderChangeListeners.add(listener);
        }
    }

    public void removeSliderChangedListener(ISliderChangedListener listener)
    {
        if (!sliderChangeListeners.contains(listener)) {
            sliderChangeListeners.remove(listener);
        }
    }

    private void createPartControl(Composite parent)
    {
        GridLayout containerLayout = new GridLayout();
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

        GridData sliderData = new GridData();
        sliderData.horizontalAlignment = GridData.FILL;
        sliderData.verticalAlignment = GridData.FILL;
        sliderData.grabExcessHorizontalSpace = true;
        sliderData.grabExcessVerticalSpace = true;

        slider = new Slider(container, SWT.HORIZONTAL);
        slider.setLayoutData(sliderData);
        slider.setEnabled(false);

        sliderListener = new TimeSliderListener();
        slider.addListener(SWT.Selection, sliderListener);
    }

    private void refreshCurrentTime()
    {
        currentTime = startTime + slider.getSelection();
    }

    private void refreshSliderControl()
    {
        final long advanceSec = currentTime - startTime;

        slider.removeListener(SWT.Selection, sliderListener);
        Display.getDefault().syncExec(new Runnable() {
            @Override
            public void run() {
                slider.setSelection((int) advanceSec);
            }
        });
        slider.addListener(SWT.Selection, sliderListener);
    }

    private void notifyCurrentTimeChanged()
    {
        if (!enabled) {
            return;
        }

        for (ISliderChangedListener listener : sliderChangeListeners) {
            listener.currentTimeChanged(currentTime);
        }
    }
}
