package maru.report.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import maru.core.model.IPropagator;
import maru.core.model.IScenarioProject;
import maru.core.model.ISpacecraft;
import maru.core.utils.FormatUtils;
import maru.core.utils.TimeUtils;
import maru.ui.model.UiElement;
import maru.ui.model.UiProject;
import maru.ui.model.UiSpacecraft;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.orekit.time.AbsoluteDate;

public abstract class AbstractPropagationReportControl extends ReportTypeControl
{
    private static final int STEP_DEFAULT = 60;
    private static final int STEP_MIN = 1;
    private static final int STEP_MAX = 600;
    private static final int STEP_DIGITS = 0;
    private static final int STEP_INC = 5;
    private static final int STEP_INC_PG = 10;

    private Composite container;
    private Combo reportElement;
    private Spinner reportStepSize;

    private final StringBuffer outputBuffer = new StringBuffer();

    public AbstractPropagationReportControl(Composite parentControl)
    {
        this("Propagation Report", parentControl);
    }

    public AbstractPropagationReportControl(String name, Composite parentControl)
    {
        super(name, parentControl);
    }

    @Override
    public void dispose()
    {
        container.dispose();
    }

    @Override
    public void enable()
    {
        Display.getDefault().syncExec(new Runnable() {
            @Override
            public void run() {
                reportElement.setEnabled(true);
                reportStepSize.setEnabled(true);
            }
        });
    }

    @Override
    public void disable()
    {
        Display.getDefault().syncExec(new Runnable() {
            @Override
            public void run() {
                reportElement.setEnabled(false);
                reportStepSize.setEnabled(false);
            }
        });
    }

    @Override
    public void setCurrentProject(UiProject project)
    {
        super.setCurrentProject(project);

        if (getCurrentProject() == null) {
            disable();
            return;
        }

        reportElement.setItems(getPropagatableItems());
        reportElement.select(0);

        enable();
    }

    @Override
    public void createReport(Text output)
    {
        ISpacecraft element = getSelectedElement();
        if (element == null) {
            return;
        }

        outputBuffer.setLength(0);
        createReportText();
        output.setText(outputBuffer.toString());
    }

    @Override
    protected void createPartControl(Composite parent)
    {
        GridData containerData = new GridData();
        containerData.verticalAlignment = GridData.FILL;
        containerData.grabExcessVerticalSpace = true;
        containerData.horizontalSpan = 2;

        container = new Composite(parent, SWT.NONE);
        container.setLayout(new GridLayout(2, false));
        container.setLayoutData(containerData);

        createElementControl(container);
        createStepSizeControl(container);
        container.pack();
        disable();
    }

    @Override
    protected Composite getContainer()
    {
        return container;
    }

    protected void append(String str)
    {
        outputBuffer.append(str);
    }

    protected void appendln(String str)
    {
        outputBuffer.append(str);
        outputBuffer.append("\n");
    }

    protected ISpacecraft getSelectedElement()
    {
        String selectedName = reportElement.getText();
        if (selectedName.isEmpty()) {
            return null;
        }
        UiElement uiElement = getCurrentProject().getChild(selectedName);
        UiSpacecraft uiPropagatable = (UiSpacecraft) uiElement;
        return uiPropagatable.getUnderlyingElement();
    }

    protected long getSelectedStepSize()
    {
        return Long.parseLong(reportStepSize.getText());
    }

    protected void createDefaultReportHeader()
    {
        ISpacecraft element = getSelectedElement();
        IPropagator propagator = element.getPropagator();
        AbsoluteDate start = getCurrentProject().getStartTime();
        AbsoluteDate stop = getCurrentProject().getStopTime();
        long duration = (long) stop.durationFrom(start);
        long stepSize = getSelectedStepSize();

        appendln(getReportName());
        appendln("Date: " + TimeUtils.asISO8601(TimeUtils.now()));
        appendln("Element: " + element.getElementName());
        appendln("Frame: " + element.getInitialCoordinate().getFrame().toString());
        appendln("Propagator: " + propagator.getName());
        appendln("Propagation Start: " + TimeUtils.asISO8601(start));
        appendln("Propagation Stop: " + TimeUtils.asISO8601(stop));
        appendln("Propagation Duration: " + duration + "sec");
        appendln("Step size: " + stepSize + "sec");
    }

    private void createElementControl(Composite parent)
    {
        GridData reportElementData = new GridData();
        reportElementData.horizontalAlignment = GridData.FILL;
        reportElementData.grabExcessHorizontalSpace = true;

        new Label(parent, SWT.NONE).setText("Element:");
        reportElement = new Combo(parent, SWT.READ_ONLY);
        reportElement.setLayoutData(reportElementData);
    }

    private void createStepSizeControl(Composite parent)
    {
        GridData stepSizeData = new GridData();
        stepSizeData.horizontalAlignment = GridData.FILL;
        stepSizeData.grabExcessHorizontalSpace = true;

        new Label(parent, SWT.NONE).setText("Step size (s):");
        reportStepSize = new Spinner(parent, SWT.BORDER);
        reportStepSize.setLayoutData(stepSizeData);
        reportStepSize.setValues(STEP_DEFAULT, STEP_MIN, STEP_MAX, STEP_DIGITS, STEP_INC, STEP_INC_PG);
    }

    protected String[] getPropagatableItems()
    {
        IScenarioProject scenario = getCurrentProject().getUnderlyingElement();

        // do not use scenario.getPropagatables() because we want the spacecrafts
        // to appear in the list before the groundstations.
        Collection<ISpacecraft> spacecrafts = scenario.getSpacecrafts();
        List<String> items = new ArrayList<>(spacecrafts.size());

        for (ISpacecraft element : spacecrafts) {
            items.add(element.getElementName());
        }

        return items.toArray(new String[0]);
    }

    protected abstract void createReportText();

    protected String doubleToString(double d)
    {
        return FormatUtils.format(d);
    }
}
