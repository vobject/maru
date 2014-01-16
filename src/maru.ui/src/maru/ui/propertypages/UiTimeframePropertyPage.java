package maru.ui.propertypages;

import maru.core.model.CoreModel;
import maru.core.model.IScenarioProject;
import maru.core.utils.TimeUtils;
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
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.orekit.time.AbsoluteDate;

public class UiTimeframePropertyPage extends UiElementPropertyPage
{
    private DateTime calendarStart;
    private DateTime calendarStop;
    private DateTime timeStart;
    private DateTime timeStop;

    private AbsoluteDate initialStart;
    private AbsoluteDate initialStop;

    private final SelectionAdapter pageCompleteValidation = new SelectionAdapter()
    {
        @Override
        public void widgetSelected(SelectionEvent event) {
            if (getStop().compareTo(getStart()) > 0) {
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

    public AbsoluteDate getStart()
    {
        return TimeUtils.getAbsoluteDate(calendarStart, timeStart);
    }

    public AbsoluteDate getStop()
    {
        return TimeUtils.getAbsoluteDate(calendarStop, timeStop);
    }

    @Override
    public boolean performOk()
    {
        IScenarioProject scenario = getScenario();

        AbsoluteDate newStart = getStart();
        if (newStart.compareTo(initialStart) != 0) {
            CoreModel.getDefault().changeTimepoint(scenario.getStartTime(), newStart, true);
        }

        AbsoluteDate newStop = getStop();
        if (newStop.compareTo(initialStop) != 0) {
            CoreModel.getDefault().changeTimepoint(scenario.getStopTime(), newStop, true);
        }

        initDefaults();
        return true;
    }

    @Override
    protected Composite createContents(Composite parent)
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

        TimeUtils.populateControls(calendarStart, timeStart, initialStart);
        TimeUtils.populateControls(calendarStop, timeStop, initialStop);
    }
}