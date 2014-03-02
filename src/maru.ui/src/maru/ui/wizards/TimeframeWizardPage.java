package maru.ui.wizards;

import maru.ui.utils.SWTUtils;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.orekit.time.AbsoluteDate;

public class TimeframeWizardPage extends WizardPage
{
    private static final String PAGE_NAME = "Time Period Page";
    private static final String PAGE_TITLE = "Scenario Period";
    private static final String PAGE_DESCRIPTION = "Set the time period for the new scenario in UTC.";

    private DateTime calendarStart;
    private DateTime calendarStop;
    private DateTime timeStart;
    private DateTime timeStop;

    private final SelectionAdapter pageCompleteValidation = new SelectionAdapter()
    {
        @Override
        public void widgetSelected(SelectionEvent event) {
            if (getStop().compareTo(getStart()) > 0) {
                setPageComplete(true);
                setErrorMessage(null);
            } else {
                setPageComplete(false);
                setErrorMessage("Specify a valid start and stop time (UTC).");
            }
        }
    };

    public TimeframeWizardPage()
    {
        super(PAGE_NAME);
        setTitle(PAGE_TITLE);
        setDescription(PAGE_DESCRIPTION);
    }

    @Override
    public void createControl(Composite parent)
    {
        Composite container = new Composite(parent, SWT.NONE);
        GridLayout containerLayout = new GridLayout(2, true);
        container.setLayout(containerLayout);

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

        setControl(container);
        setPageComplete(false);
        setErrorMessage("Specify a valid start and stop time (UTC).");
    }

    public AbsoluteDate getStart()
    {
        return SWTUtils.getAbsoluteDate(calendarStart, timeStart);
    }

    public AbsoluteDate getStop()
    {
        return SWTUtils.getAbsoluteDate(calendarStop, timeStop);
    }
}
