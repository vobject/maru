package maru.ui.propertypages;

import java.text.ParseException;

import maru.core.model.CoreModel;
import maru.core.model.ITimepoint;
import maru.core.utils.TimeUtil;
import maru.ui.model.UiTimepoint;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class UiTimepointPropertyPage extends UiPropertyPage
{
    private Text time;

    private long initialTime;
    private long newTime;

    private final ModifyListener dateValidator = new ModifyListener()
    {
        @Override
        public void modifyText(ModifyEvent event)
        {
            String newText = ((Text) event.widget).getText();

            try {
                newTime = TimeUtil.fromString(newText).getTime();
                setErrorMessage(null);
                setValid(true);
            } catch (ParseException ex) {
                newTime = initialTime;
                setErrorMessage("Invalid time input.");
                setValid(false);
            }
        }
    };

    @Override
    public UiTimepoint getUiElement()
    {
        return (UiTimepoint) getElement().getAdapter(UiTimepoint.class);
    }

    @Override
    public ITimepoint getScenarioElement()
    {
        return getUiElement().getUnderlyingElement();
    }

    @Override
    public boolean performOk()
    {
        ITimepoint element = getScenarioElement();

        if (newTime != initialTime) {
            CoreModel.getDefault().changeTimepoint(element, newTime, true);
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
        GridLayout layout = new GridLayout(2, false);
        layout.marginWidth = 0;
        layout.marginHeight = 0;

        Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(layout);
        container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        new Label(container, SWT.NONE).setText("Time in UTC:");
        time = new Text(container, SWT.BORDER | SWT.SINGLE);
        time.addModifyListener(dateValidator);
        time.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));

        return container;
    }

    private void initDefaults()
    {
        ITimepoint element = getScenarioElement();
        if (element == null) {
            return;
        }

        initialTime = element.getTime();
    }

    private void initControls()
    {
        ITimepoint element = getScenarioElement();
        if (element == null) {
            return;
        }

        time.setText(TimeUtil.asISO8601(initialTime));
    }
}
