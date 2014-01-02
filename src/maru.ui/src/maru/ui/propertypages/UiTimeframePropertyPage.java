package maru.ui.propertypages;

import java.util.Calendar;

import maru.core.model.CoreModel;
import maru.core.model.IScenarioProject;
import maru.core.utils.TimeUtil;
import maru.ui.model.UiElement;
import maru.ui.model.UiModel;
import maru.ui.model.UiProject;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;

public class UiTimeframePropertyPage extends UiElementPropertyPage
{
    private DateTime calendarStart;
    private DateTime calendarStop;
    private DateTime timeStart;
    private DateTime timeStop;

    private long initialStart;
    private long initialStop;

    private final SelectionAdapter pageCompleteValidation = new SelectionAdapter()
    {
        @Override
        public void widgetSelected(SelectionEvent event) {
            if (getStop() > getStart()) {
                setValid(true);
                setErrorMessage(null);
            } else {
                setValid(false);
                setErrorMessage("Specify a valid start and stop time (UTC).");
            }
        }
    };

    @Override
    public UiElement getUiElement()
    {
        // it is a long way to get the corresponding UiElement from an IProject:
        //  1. get the selected IProject (plugin.xml must take care that this is valid)
        //  2. get the IScenarioProject of the IProject using CoreModel
        //  3. get the UiProject for the IScenarioProject using UiModel
        //  4. the UiProject is a subclass of UiElement

        IProject project = getProject();
        IScenarioProject scenario = CoreModel.getDefault().getScenarioProject(project);
        UiProject uiProject = UiModel.getDefault().getUiProject(scenario);
        return uiProject;
    }

    @Override
    public IProject getProject()
    {
        return (IProject) getElement();
    }

    public long getStart()
    {
        Calendar calender = TimeUtil.getCalendar();
        calender.set(Calendar.YEAR, calendarStart.getYear());
        calender.set(Calendar.MONTH, calendarStart.getMonth());
        calender.set(Calendar.DAY_OF_MONTH, calendarStart.getDay());
        calender.set(Calendar.HOUR_OF_DAY, timeStart.getHours());
        calender.set(Calendar.MINUTE, timeStart.getMinutes());
        calender.set(Calendar.SECOND, timeStart.getSeconds());
        return calender.getTimeInMillis() / 1000;
    }

    public long getStop()
    {
        Calendar calender = TimeUtil.getCalendar();
        calender.set(Calendar.YEAR, calendarStop.getYear());
        calender.set(Calendar.MONTH, calendarStop.getMonth());
        calender.set(Calendar.DAY_OF_MONTH, calendarStop.getDay());
        calender.set(Calendar.HOUR_OF_DAY, timeStop.getHours());
        calender.set(Calendar.MINUTE, timeStop.getMinutes());
        calender.set(Calendar.SECOND, timeStop.getSeconds());
        return calender.getTimeInMillis() / 1000;
    }

    @Override
    public boolean performOk()
    {
        IScenarioProject scenario = getScenario();

        long newStart = getStart();
        if (newStart != initialStart) {
            CoreModel.getDefault().changeTimepoint(scenario.getStartTime(), newStart, true);
        }

        long newStop = getStop();
        if (newStop != initialStop) {
            CoreModel.getDefault().changeTimepoint(scenario.getStopTime(), newStop, true);
        }

        initDefaults();
        return true;
    }

    @Override
    protected Control createContents(Composite parent)
    {
        Composite container = createControls(parent);

        initDefaults();
        initControls();

        return container;
    }

    private Composite createControls(Composite parent)
    {
        GridLayout layout = new GridLayout(2, true);
        layout.marginWidth = 0;
        layout.marginHeight = 0;

        Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(layout);
        container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        new Label(container, SWT.NONE).setText("Start time in UTC:");
        new Label(container, SWT.NONE).setText("Stop time in UTC:");

        calendarStart = new DateTime(container, SWT.CALENDAR);
        calendarStart.addSelectionListener(pageCompleteValidation);
        calendarStart.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        calendarStop = new DateTime(container, SWT.CALENDAR);
        calendarStop.addSelectionListener(pageCompleteValidation);
        calendarStop.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        timeStart = new DateTime(container, SWT.TIME);
        timeStart.addSelectionListener(pageCompleteValidation);
        timeStart.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

        timeStop = new DateTime(container, SWT.TIME);
        timeStop.addSelectionListener(pageCompleteValidation);
        timeStop.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

        return container;
    }

    private void initDefaults()
    {
        IScenarioProject scenario = getScenario();
        if (scenario == null) {
            return;
        }

        initialStart = scenario.getStartTime().getTime();
        initialStop = scenario.getStopTime().getTime();
    }

    private void initControls()
    {
        IScenarioProject scenario = getScenario();
        if (scenario == null) {
            return;
        }

        Calendar startCalendar = TimeUtil.getCalendar(initialStart);
        calendarStart.setDate(startCalendar.get(Calendar.YEAR),
                              startCalendar.get(Calendar.MONTH),
                              startCalendar.get(Calendar.DAY_OF_MONTH));
        timeStart.setTime(startCalendar.get(Calendar.HOUR_OF_DAY),
                          startCalendar.get(Calendar.MINUTE),
                          startCalendar.get(Calendar.SECOND));

        Calendar stopCalendar = TimeUtil.getCalendar(initialStop);
        calendarStop.setDate(stopCalendar.get(Calendar.YEAR),
                             stopCalendar.get(Calendar.MONTH),
                             stopCalendar.get(Calendar.DAY_OF_MONTH));
        timeStop.setTime(stopCalendar.get(Calendar.HOUR_OF_DAY),
                         stopCalendar.get(Calendar.MINUTE),
                         stopCalendar.get(Calendar.SECOND));
    }
}