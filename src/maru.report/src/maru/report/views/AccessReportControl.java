package maru.report.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import maru.core.model.ICoordinate;
import maru.core.model.IGroundstation;
import maru.core.model.IPropagator;
import maru.core.model.IScenarioElement;
import maru.core.model.IScenarioProject;
import maru.core.model.ISpacecraft;
import maru.core.utils.NumberUtils;
import maru.core.utils.TimeUtils;
import maru.core.utils.VisibilityUtils;
import maru.ui.model.UiElement;
import maru.ui.model.UiProject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.orekit.errors.OrekitException;
import org.orekit.time.AbsoluteDate;

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
        IScenarioElement element1 = getSelectedElement1();
        IScenarioElement element2 = getSelectedElement2();

        if ((element1 == null) || (element2 == null)) {
            // both elements must have been selected
            return;
        }

        if (element1 == element2) {
            // both selections must not be the same element
            return;
        }

        if ((element1 instanceof IGroundstation) && (element2 instanceof IGroundstation)) {
            // there must be at least one spacecraft selected
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

    protected IScenarioElement getSelectedElement1()
    {
        return getSelectedElement(reportElement1);
    }

    protected IScenarioElement getSelectedElement2()
    {
        return getSelectedElement(reportElement2);
    }

    protected long getSelectedStepSize()
    {
        return Long.parseLong(reportStepSize.getText());
    }

    protected void createDefaultReportHeader()
    {
        IScenarioElement element1 = getSelectedElement1();
        IScenarioElement element2 = getSelectedElement2();

        AbsoluteDate start = getCurrentProject().getStartTime();
        AbsoluteDate stop = getCurrentProject().getStopTime();
        long duration = (long) stop.durationFrom(start);
        long stepSize = getSelectedStepSize();

        appendln(getReportName());
        appendln("Date: " + TimeUtils.asISO8601(TimeUtils.now()));
        appendln(element1.getElementName() + " <---> " + element2.getElementName());
        appendln("Propagation Start: " + TimeUtils.asISO8601(start));
        appendln("Propagation Stop: " + TimeUtils.asISO8601(stop));
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

        try {
            createReportBody();
        } catch (OrekitException e) {
            appendln("ERROR: " + e.getMessage());
        }
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

    private IScenarioElement getSelectedElement(Combo combo)
    {
        String selectedName = combo.getText();
        if (selectedName.isEmpty()) {
            return null;
        }
        UiElement uiElement = getCurrentProject().getChild(selectedName);
        return uiElement.getUnderlyingElement();
    }

    protected String[] getPropagatableItems()
    {
        IScenarioProject scenario = getCurrentProject().getUnderlyingElement();
        Collection<ISpacecraft> spacecrafts = scenario.getSpacecrafts();
        Collection<IGroundstation> groundstations = scenario.getGroundstations();

        int itemCount = spacecrafts.size() + groundstations.size();
        List<String> items = new ArrayList<>(itemCount);

        for (ISpacecraft element : spacecrafts) {
            items.add(element.getElementName());
        }
        for (IGroundstation element : groundstations) {
            items.add(element.getElementName());
        }
        return items.toArray(new String[itemCount]);
    }

    private void createReportBody() throws OrekitException
    {
        ISpacecraft alpha;
        IScenarioElement beta;

        if (getSelectedElement1() instanceof ISpacecraft) {
            alpha = (ISpacecraft) getSelectedElement1();
            beta = getSelectedElement2();
        } else {
            alpha = (ISpacecraft) getSelectedElement2();
            beta = getSelectedElement1();
        }
        IPropagator propagator = alpha.getPropagator();

        AbsoluteDate start = getCurrentProject().getStartTime();
        AbsoluteDate stop = getCurrentProject().getStopTime();
        long stepSize = getSelectedStepSize();

        double minDuration = Double.MAX_VALUE;
        double maxDuration = 0.0;
        double totalDuration = 0.0;
        double currentDuration = Double.NaN;
        double separationDuration = 0.0;
        int count = 0;

        for (ICoordinate coordinate : propagator.getCoordinates(alpha, start, stop, stepSize))
        {
            double distance = -1.0;
            if (beta instanceof ISpacecraft)
            {
                ISpacecraft betaSc = (ISpacecraft) beta;
                IPropagator betaProp = betaSc.getPropagator();
                ICoordinate betaCoord = betaProp.getCoordinate(betaSc, coordinate.getDate());

                if (VisibilityUtils.hasAccessTo(alpha.getCentralBody(), coordinate, betaCoord)) {
                    distance = VisibilityUtils.getDistanceTo(alpha.getCentralBody(), coordinate, betaCoord);
                }
            }
            else if (beta instanceof IGroundstation)
            {
                IGroundstation betaGs = (IGroundstation) beta;

                if (VisibilityUtils.hasAccessTo(coordinate, betaGs)) {
                    distance = VisibilityUtils.getDistanceTo(coordinate, betaGs);
                }
            }
            else
            {
                throw new IllegalStateException();
            }

            if (distance > 0.0)
            {
                if (Double.isNaN(currentDuration))
                {
                    append(Integer.toString(count + 1));
                    append("\t");

                    append(TimeUtils.asISO8601(coordinate.getDate()));
                    append("\t");

                    currentDuration = 0.0;
                }
                currentDuration += stepSize;
            }
            else
            {
                if (!Double.isNaN(currentDuration))
                {
                    append(TimeUtils.asISO8601(coordinate.getDate()));
                    append("\t");
                    appendln(toDuration(currentDuration));

                    if (currentDuration < minDuration) {
                        minDuration = currentDuration;
                    }
                    if (currentDuration > maxDuration) {
                        maxDuration = currentDuration;
                    }
                    totalDuration += currentDuration;
                    count++;
                    currentDuration = Double.NaN;
                }
                else
                {
                    separationDuration += stepSize;
                }
            }
        }

        if (!Double.isNaN(currentDuration))
        {
            // the current duration was not yet closed
            append(TimeUtils.asISO8601(stop));
            append("\t");
            appendln(toDuration(currentDuration));
            if (currentDuration < minDuration) {
                minDuration = currentDuration;
            }
            if (currentDuration > maxDuration) {
                maxDuration = currentDuration;
            }
            totalDuration += currentDuration;
            count++;
        }
        double meanDuration = totalDuration / count;

        appendln("");
        appendln("Min duration: " + toDuration((minDuration != Double.MAX_VALUE) ? minDuration : 0.0));
        appendln("Max duration: " + toDuration(maxDuration));
        appendln("Mean duration: " + toDuration((Double.isNaN(meanDuration) ? 0.0 : meanDuration)));
        appendln("Total duration: " + toDuration(totalDuration));
        appendln("Separation duration: " + toDuration(separationDuration));
    }

    private String toDuration(double sec)
    {
        return NumberUtils.formatNoDecimalPoint(sec);
    }
}
