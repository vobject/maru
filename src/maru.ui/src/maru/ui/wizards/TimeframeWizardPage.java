package maru.ui.wizards;

import java.util.Calendar;

import maru.core.utils.TimeUtils;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;

public class TimeframeWizardPage extends WizardPage
{
    private static final String PAGE_NAME = "Time Period Page";
    private static final String PAGE_TITLE = "Scenario Period";
    private static final String PAGE_DESCRIPTION = "Set the time period for the new scenario in UTC.";

    private DateTime calendarBegin;
    private DateTime calendarEnd;
    private DateTime timeBegin;
    private DateTime timeEnd;

    private final SelectionAdapter pageCompleteValidation = new SelectionAdapter()
    {
        @Override
        public void widgetSelected(SelectionEvent event) {
            if (getEnd() > getBegin()) {
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

        calendarBegin = new DateTime(container, SWT.CALENDAR);
        calendarBegin.addSelectionListener(pageCompleteValidation);
        calendarBegin.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        calendarEnd = new DateTime(container, SWT.CALENDAR);
        calendarEnd.addSelectionListener(pageCompleteValidation);
        calendarEnd.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        timeBegin = new DateTime(container, SWT.TIME);
        timeBegin.addSelectionListener(pageCompleteValidation);
        timeBegin.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

        timeEnd = new DateTime(container, SWT.TIME);
        timeEnd.addSelectionListener(pageCompleteValidation);
        timeEnd.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

        setControl(container);
        setPageComplete(false);
        setErrorMessage("Specify a valid start and stop time (UTC).");
    }

    public long getBegin()
    {
        Calendar calendar = TimeUtils.getCalendar();
        calendar.set(Calendar.YEAR, calendarBegin.getYear());
        calendar.set(Calendar.MONTH, calendarBegin.getMonth());
        calendar.set(Calendar.DAY_OF_MONTH, calendarBegin.getDay());
        calendar.set(Calendar.HOUR_OF_DAY, timeBegin.getHours());
        calendar.set(Calendar.MINUTE, timeBegin.getMinutes());
        calendar.set(Calendar.SECOND, timeBegin.getSeconds());
        return calendar.getTimeInMillis() / 1000;
    }

    public long getEnd()
    {
        Calendar calendar = TimeUtils.getCalendar();
        calendar.set(Calendar.YEAR, calendarEnd.getYear());
        calendar.set(Calendar.MONTH, calendarEnd.getMonth());
        calendar.set(Calendar.DAY_OF_MONTH, calendarEnd.getDay());
        calendar.set(Calendar.HOUR_OF_DAY, timeEnd.getHours());
        calendar.set(Calendar.MINUTE, timeEnd.getMinutes());
        calendar.set(Calendar.SECOND, timeEnd.getSeconds());
        return calendar.getTimeInMillis() / 1000;
    }
}
