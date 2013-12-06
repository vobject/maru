package maru.ui.views.timeline;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

class TimeSettings
{
    private class StepSizeListener extends SelectionAdapter
    {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
            Combo combo = (Combo) e.getSource();
            notifyStepSizeChanged(parseStepSize(combo.getText()));
        }
    }

    private class SpeedMultiplicatorListener extends SelectionAdapter
    {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
            Combo combo = (Combo) e.getSource();
            notifySpeedMultiplicatorChanged(parseMultiplicator(combo.getText()));
        }
    }

    private class RealtimeModeListener extends SelectionAdapter
    {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
            Button button = (Button) e.getSource();
            notifyRealtimeModeChanged(button.getSelection());
        }
    }

    private static final String[] MULTIPLICATOR_ITEMS = new String[] {
        "1x", "5x", "10x", "20x", "50x", "100x", "200x",
        "300x", "500x", "1000x", "1500x", "2000x"
    };
    private static final int MULTIPLICATOR_DEFAULT_ITEM = 7; // 300x

    private static final String[] SETEPSIZE_ITEMS = new String[] {
        "1sec", "2sec", "5sec", "10sec", "15sec", "20sec", "30sec", "40sec", "50sec",
        "60sec", "90sec", "120sec", "180sec", "240sec", "300sec", "600sec"
    };
    private static final int STEPSIZE_DEFAULT_ITEM = 5; // 20sec

    private Combo stepSizeCombo;
    private StepSizeListener stepListener;
    private Button realtimeButton;

    private Combo speedMultiplicatorCombo;
    private SpeedMultiplicatorListener speedListener;
    private RealtimeModeListener realtimeListener;

    private final List<ISettingsChangedListener> settingsChangeListeners = new ArrayList<>();

    /** enable of disable changes to the control. */
    private boolean enabled;

    public TimeSettings(Composite parentControl)
    {
        createPartControl(parentControl);
    }

    public void enable()
    {
        // allow for notifications
        enabled = true;

        speedMultiplicatorCombo.setEnabled(true);
        stepSizeCombo.setEnabled(true);
        realtimeButton.setEnabled(true);
    }

    public void disable()
    {
        speedMultiplicatorCombo.setEnabled(false);
        stepSizeCombo.setEnabled(false);
        realtimeButton.setEnabled(false);
        realtimeButton.setSelection(false);

        // disable listener notifications
        enabled = false;
    }

    public void disableForRealtime()
    {
        speedMultiplicatorCombo.setEnabled(false);
        stepSizeCombo.setEnabled(false);
    }

    public void changeStepSize(final double stepSize)
    {
        stepSizeCombo.removeSelectionListener(stepListener);

        Display.getCurrent().syncExec(new Runnable() {
            @Override
            public void run() {
                stepSizeCombo.setText(toStepSizeItem(stepSize));
            }
        });

        stepSizeCombo.addSelectionListener(stepListener);
    }

    public void changeSpeedMultiplicator(final double multiplicator)
    {
        speedMultiplicatorCombo.removeSelectionListener(speedListener);

        Display.getCurrent().syncExec(new Runnable() {
            @Override
            public void run() {
                speedMultiplicatorCombo.setText(toMultiplicatorItem(multiplicator));
            }
        });

        speedMultiplicatorCombo.addSelectionListener(speedListener);
    }

    public double getSpeedMultiplicator()
    {
        return parseMultiplicator(speedMultiplicatorCombo.getText());
    }

    public int getStepSize()
    {
        return parseStepSize(stepSizeCombo.getText());
    }

    public void addSettingsChangedListener(ISettingsChangedListener listener)
    {
        if (!settingsChangeListeners.contains(listener)) {
            settingsChangeListeners.add(listener);
        }
    }

    public void removeSettingsChangedListener(ISettingsChangedListener listener)
    {
        if (!settingsChangeListeners.contains(listener)) {
            settingsChangeListeners.remove(listener);
        }
    }

    private void createPartControl(Composite parent)
    {
        GridLayout containerLayout = new GridLayout(5, false);
        containerLayout.marginWidth = 2;
        containerLayout.marginHeight = 0;

        Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(containerLayout);

        // playback speed combo box
        new Label(container, SWT.NONE).setText("Speed:");
        speedMultiplicatorCombo = new Combo(container, SWT.READ_ONLY);
        speedMultiplicatorCombo.setLayoutData(new GridData());
        speedMultiplicatorCombo.setEnabled(false);
        speedMultiplicatorCombo.setItems(MULTIPLICATOR_ITEMS);
        speedMultiplicatorCombo.select(MULTIPLICATOR_DEFAULT_ITEM);
        speedListener = new SpeedMultiplicatorListener();
        speedMultiplicatorCombo.addSelectionListener(speedListener);

        // step size combo box
        new Label(container, SWT.NONE).setText("Steps size:");
        stepSizeCombo = new Combo(container, SWT.READ_ONLY);
        stepSizeCombo.setLayoutData(new GridData());
        stepSizeCombo.setEnabled(false);
        stepSizeCombo.setItems(SETEPSIZE_ITEMS);
        stepSizeCombo.select(STEPSIZE_DEFAULT_ITEM);
        stepListener = new StepSizeListener();
        stepSizeCombo.addSelectionListener(stepListener);

        // real-time toggle button
        realtimeButton = new Button(container, SWT.CHECK);
        realtimeButton.setText("Real-time Mode");
        realtimeButton.setLayoutData(new GridData());
        realtimeButton.setEnabled(false);
        realtimeListener = new RealtimeModeListener();
        realtimeButton.addSelectionListener(realtimeListener);
    }

    private void notifyStepSizeChanged(int stepSize)
    {
        if (!enabled) {
            return;
        }

        for (ISettingsChangedListener listener : settingsChangeListeners) {
            listener.stepSizeChanged(stepSize);
        }
    }

    private void notifySpeedMultiplicatorChanged(double multiplicator)
    {
        if (!enabled) {
            return;
        }

        for (ISettingsChangedListener listener : settingsChangeListeners) {
            listener.playbackSpeedChanged(multiplicator);
        }
    }

    private void notifyRealtimeModeChanged(boolean realtime)
    {
        if (!enabled) {
            return;
        }

        for (ISettingsChangedListener listener : settingsChangeListeners) {
            listener.realtimeModeChanged(realtime);
        }
    }

    private String toMultiplicatorItem(double multiplicator)
    {
        return Double.toString(multiplicator) + "x";
    }

    private double parseMultiplicator(String text)
    {
        String multiplicatorString = text.substring(0, text.length() - 1);
        return Double.parseDouble(multiplicatorString);
    }

    private String toStepSizeItem(double stepSize)
    {
        return Integer.toString((int) stepSize) + "sec";
    }

    private int parseStepSize(String text)
    {
        String stepSizeString = text.substring(0, text.length() - 3);
        return Integer.parseInt(stepSizeString);
    }
}
