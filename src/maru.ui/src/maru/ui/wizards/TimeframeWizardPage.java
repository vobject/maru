package maru.ui.wizards;

import java.util.Calendar;

import maru.core.utils.TimeUtil;

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

    private DateTime calenderBegin;
    private DateTime calenderEnd;
    private DateTime timeBegin;
    private DateTime timeEnd;

    private final SelectionAdapter pageCompleteValidation = new SelectionAdapter() {
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

        calenderBegin = new DateTime(container, SWT.CALENDAR);
        calenderBegin.addSelectionListener(pageCompleteValidation);
        calenderBegin.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        calenderEnd = new DateTime(container, SWT.CALENDAR);
        calenderEnd.addSelectionListener(pageCompleteValidation);
        calenderEnd.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

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
        Calendar calender = TimeUtil.getCalendar();
        calender.set(Calendar.YEAR, calenderBegin.getYear());
        calender.set(Calendar.MONTH, calenderBegin.getMonth());
        calender.set(Calendar.DAY_OF_MONTH, calenderBegin.getDay());
        calender.set(Calendar.HOUR_OF_DAY, timeBegin.getHours());
        calender.set(Calendar.MINUTE, timeBegin.getMinutes());
        calender.set(Calendar.SECOND, timeBegin.getSeconds());
        return calender.getTimeInMillis() / 1000;
    }

    public long getEnd()
    {
        Calendar calender = TimeUtil.getCalendar();
        calender.set(Calendar.YEAR, calenderEnd.getYear());
        calender.set(Calendar.MONTH, calenderEnd.getMonth());
        calender.set(Calendar.DAY_OF_MONTH, calenderEnd.getDay());
        calender.set(Calendar.HOUR_OF_DAY, timeEnd.getHours());
        calender.set(Calendar.MINUTE, timeEnd.getMinutes());
        calender.set(Calendar.SECOND, timeEnd.getSeconds());
        return calender.getTimeInMillis() / 1000;
    }
}
