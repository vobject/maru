package maru.report.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import maru.core.model.IScenarioProject;
import maru.core.model.ISpacecraft;
import maru.core.utils.TimeUtil;
import maru.ui.model.UiElement;
import maru.ui.model.UiProject;
import maru.ui.model.UiSpacecraft;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

public class AccessReportControl extends ReportTypeControl
{
    private static final int STEP_DEFAULT = 60;
    private static final int STEP_MIN = 1;
    private static final int STEP_MAX = 600;
    private static final int STEP_DIGITS = 0;
    private static final int STEP_INC = 5;
    private static final int STEP_INC_PG = 10;

    private Composite container;
    private Combo reportElement1;
    private Combo reportElement2;
    private Spinner reportStepSize;

    private final StringBuffer outputBuffer = new StringBuffer();

    public AccessReportControl(Composite parentControl)
    {
        super("Access Report", parentControl);
    }

    public AccessReportControl(String name, Composite parentControl)
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
        reportElement1.setEnabled(true);
        reportElement2.setEnabled(true);
        reportStepSize.setEnabled(true);
    }

    @Override
    public void disable()
    {
        reportElement1.setEnabled(false);
        reportElement2.setEnabled(false);
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

        reportElement1.setItems(getPropagatableItems());
        reportElement1.select(0);

        reportElement2.setItems(getPropagatableItems());
        reportElement2.select(0);

        enable();
    }

    @Override
    public void createReport(Text output)
    {
        ISpacecraft element1 = getSelectedElement1();
        ISpacecraft element2 = getSelectedElement2();
        if ((element1 == null) || (element2 == null)) {
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
        outputBuffer.append("\r\n");
    }

    protected ISpacecraft getSelectedElement1()
    {
        return getSelectedElement(reportElement1);
    }

    protected ISpacecraft getSelectedElement2()
    {
        return getSelectedElement(reportElement2);
    }

    protected long getSelectedStepSize()
    {
        return Long.parseLong(reportStepSize.getText());
    }

    protected void createDefaultReportHeader()
    {
        ISpacecraft element1 = getSelectedElement1();
        ISpacecraft element2 = getSelectedElement2();

        long startTime = getCurrentProject().getStartTime();
        long stopTime = getCurrentProject().getStopTime();
        long duration = stopTime - startTime;
        long stepSize = getSelectedStepSize();

        appendln(getReportName());
        appendln("Date: " + TimeUtil.asISO8601(new Date()));
        appendln(element1.getElementName() + "(" + element1.getPropagator().getName() + ") to " +
                 element2.getElementName() + "(" + element2.getPropagator().getName() + ")");
        appendln("Propagation Start: " + TimeUtil.asISO8601(startTime));
        appendln("Propagation Stop: " + TimeUtil.asISO8601(stopTime));
        appendln("Propagation Duration: " + duration + "sec");
        appendln("Step size: " + stepSize + "sec");
    }

    protected void createReportText()
    {
        createDefaultReportHeader();

        append("Tab Separated Columns: ");
        append("Access | ");
        append("Start Time (UTC) | ");
        append("Stop Time (UTC) | ");
        append("Duration (sec)");
        appendln("");
        appendln("");

        createReportBody();
    }

    private void createElementControl(Composite parent)
    {
        GridData reportElementData = new GridData();
        reportElementData.horizontalAlignment = GridData.FILL;
        reportElementData.grabExcessHorizontalSpace = true;

        new Label(parent, SWT.NONE).setText("Element 1:");
        reportElement1 = new Combo(parent, SWT.READ_ONLY);
        reportElement1.setLayoutData(reportElementData);

        new Label(parent, SWT.NONE).setText("Element 2:");
        reportElement2 = new Combo(parent, SWT.READ_ONLY);
        reportElement2.setLayoutData(reportElementData);
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

    private ISpacecraft getSelectedElement(Combo combo)
    {
        String selectedName = combo.getText();
        if (selectedName.isEmpty()) {
            return null;
        }
        UiElement uiElement = getCurrentProject().getChild(selectedName);
        UiSpacecraft uiPropagatable = (UiSpacecraft) uiElement;
        return uiPropagatable.getUnderlyingElement();
    }

    protected String[] getPropagatableItems()
    {
        IScenarioProject scenario = getCurrentProject().getUnderlyingElement();
        Collection<ISpacecraft> spacecrafts = scenario.getSpacecrafts();

        int itemCount = spacecrafts.size();
        List<String> items = new ArrayList<>(itemCount);

        for (ISpacecraft element : spacecrafts) {
            items.add(element.getElementName());
        }
        return items.toArray(new String[itemCount]);
    }

    private void createReportBody()
    {
//        IPropagatable element1 = getSelectedElement1();
//        IPropagator propagator1 = element1.getPropagator();
//
//        long startTime = getCurrentProject().getStartTime();
//        long stopTime = getCurrentProject().getStopTime();
//        long stepSize = getSelectedStepSize();
//        int accessNr = 1;

        // TODO: Implement Me!
        appendln("TODO: Implement Me!");

//        for (ICoordinate coordinate : propagator1.getCoordinates(element1, startTime, stopTime, stepSize))
//        {
//            append(Integer.toString(++accessNr));
//            append("\t");
//
//            append(TimeUtil.asISO8601(coordinate.getTime()));
//            append("\t");
//
//            appendln("");
//        }
    }
}
