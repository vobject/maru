package maru.report.views;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import maru.core.model.IGroundstation;
import maru.core.model.IPropagatable;
import maru.core.model.IPropagator;
import maru.core.model.IScenarioProject;
import maru.core.model.ISpacecraft;
import maru.core.utils.TimeUtil;
import maru.ui.model.UiElement;
import maru.ui.model.UiProject;
import maru.ui.model.UiPropagatable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

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

    private static final String DEFAULT_DOUBLE_FORMAT = "###0.000000";
    private final DecimalFormat doubleFormat;

    private final StringBuffer outputBuffer = new StringBuffer();

    public AbstractPropagationReportControl(Composite parentControl)
    {
        this("Propagation Report", parentControl);
    }

    public AbstractPropagationReportControl(String name, Composite parentControl)
    {
        super(name, parentControl);

        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.US);
        otherSymbols.setDecimalSeparator('.');
        doubleFormat = new DecimalFormat(DEFAULT_DOUBLE_FORMAT, otherSymbols);
    }

    @Override
    public void dispose()
    {
        container.dispose();
    }

    @Override
    public void enable()
    {
        reportElement.setEnabled(true);
        reportStepSize.setEnabled(true);
    }

    @Override
    public void disable()
    {
        reportElement.setEnabled(false);
        reportStepSize.setEnabled(false);
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
        IPropagatable element = getSelectedElement();
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

    protected IPropagatable getSelectedElement()
    {
        String selectedName = reportElement.getText();
        if (selectedName.isEmpty()) {
            return null;
        }
        UiElement uiElement = getCurrentProject().getChild(selectedName);
        UiPropagatable uiPropagatable = (UiPropagatable) uiElement;
        return uiPropagatable.getUnderlyingElement();
    }

    protected long getSelectedStepSize()
    {
        return Long.parseLong(reportStepSize.getText());
    }

    protected void createDefaultReportHeader()
    {
        IPropagatable element = getSelectedElement();
        IPropagator propagator = element.getPropagator();
        long startTime = getCurrentProject().getStartTime();
        long stopTime = getCurrentProject().getStopTime();
        long duration = stopTime - startTime;
        long stepSize = getSelectedStepSize();

        appendln(getReportName());
        appendln("Date: " + TimeUtil.asISO8601(new Date()));
        appendln("Element: " + element.getElementName());
        appendln("Frame: " + element.getInitialCoordinate().getFrame().toString());
        appendln("Propagator: " + propagator.getName());
        appendln("Propagation Start: " + TimeUtil.asISO8601(startTime));
        appendln("Propagation Stop: " + TimeUtil.asISO8601(stopTime));
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
        Collection<IGroundstation> groundstations = scenario.getGroundstations();

        int itemCount = spacecrafts.size() + groundstations.size();
        List<String> items = new ArrayList<>(itemCount);

        for (IPropagatable element : spacecrafts) {
            items.add(element.getElementName());
        }

        for (IPropagatable element : groundstations) {
            items.add(element.getElementName());
        }

        return items.toArray(new String[itemCount]);
    }

    protected abstract void createReportText();

    protected String doubleToString(double d)
    {
        return doubleFormat.format(d);
    }
}
