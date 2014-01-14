package maru.report.views;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import maru.core.model.ICoordinate;
import maru.core.model.IPropagator;
import maru.core.model.ISpacecraft;
import maru.core.model.ISpacecraft.EclipseState;
import maru.core.utils.TimeUtils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.orekit.errors.OrekitException;

enum EclipseType
{
    Umbra,
    UmbraAndPenumbra;

    private static final String NAME_UMBRA = "Umbra";
    private static final String NAME_UMBRA_AND_PENUMBRA = "Umbra + Penumbra";

    public static EclipseType fromString(String str)
    {
        if (str.equals(NAME_UMBRA)) {
            return Umbra;
        } else if (str.equals(NAME_UMBRA_AND_PENUMBRA)) {
            return UmbraAndPenumbra;
        }
        throw new IllegalArgumentException();
    }

    @Override
    public String toString()
    {
        switch (this)
        {
            case Umbra:
                return NAME_UMBRA;
            case UmbraAndPenumbra:
                return NAME_UMBRA_AND_PENUMBRA;
        }
        throw new IllegalStateException();
    }
}

public class EclipseReportControl extends AbstractPropagationReportControl
{
    private Combo eclipseType;

    public EclipseReportControl(Composite parentControl)
    {
        super("Eclipse Report", parentControl);
    }

    @Override
    public void enable()
    {
        super.enable();

        if (eclipseType != null) {
            eclipseType.setEnabled(true);
        }
    }

    @Override
    public void disable()
    {
        super.disable();

        if (eclipseType != null) {
            eclipseType.setEnabled(false);
        }
    }

    @Override
    protected void createPartControl(Composite parent)
    {
        super.createPartControl(parent);

        GridData eclipseTypeData = new GridData();
        eclipseTypeData.horizontalAlignment = GridData.FILL;
        eclipseTypeData.grabExcessHorizontalSpace = true;

        new Label(getContainer(), SWT.NONE).setText("Eclipse Type:");
        eclipseType = new Combo(getContainer(), SWT.READ_ONLY);
        eclipseType.setItems(getAvailableEclipseTypes());
        eclipseType.select(0);
        eclipseType.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        getContainer().pack();
        disable();
    }

    @Override
    protected void createReportText()
    {
        createDefaultReportHeader();

        appendln("Eclipse Type: " + eclipseType.getText());
        appendln("Sunlight: Time outside of " + getCurrentType());

        append("Tab Separated Columns: ");
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

    @Override
    protected String[] getPropagatableItems()
    {
        Collection<ISpacecraft> elements = getCurrentProject().getUnderlyingElement().getSpacecrafts();
        List<String> items = new ArrayList<>();

        for (ISpacecraft element : elements) {
            items.add(element.getElementName());
        }

        return items.toArray(new String[0]);
    }

    private void createReportBody() throws OrekitException
    {
        ISpacecraft element = getSelectedElement();
        IPropagator propagator = element.getPropagator();

        long startTime = getCurrentProject().getStartTime();
        long stopTime = getCurrentProject().getStopTime();
        long stepSize = getSelectedStepSize();
        EclipseType type = getCurrentType();

        double minDuration = Double.MAX_VALUE;
        double maxDuration = 0.0;
        double totalDuration = 0.0;
        double currentDuration = Double.NaN;
        double sunDuration = 0.0;
        int count = 0;

        for (ICoordinate coordinate : propagator.getCoordinates(element, startTime, stopTime, stepSize))
        {
            boolean inEclipse = false;

            if (type == EclipseType.Umbra) {
                inEclipse = element.getEclipseState(coordinate) == EclipseState.Umbra;
            } else if (type == EclipseType.UmbraAndPenumbra) {
                inEclipse = element.getEclipseState(coordinate) == EclipseState.UmbraOrPenumbra;
            } else {
                throw new IllegalStateException();
            }

            if (inEclipse)
            {
                if (Double.isNaN(currentDuration))
                {
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
                    sunDuration += stepSize;
                }
            }
        }

        if (!Double.isNaN(currentDuration))
        {
            // the current duration was not yet closed
            append(TimeUtils.asISO8601(stopTime));
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
        appendln("Sunlight duration: " + toDuration(sunDuration));
    }

    private String[] getAvailableEclipseTypes()
    {
        return new String[] {
            EclipseType.Umbra.toString(),
            EclipseType.UmbraAndPenumbra.toString()
        };
    }

    private EclipseType getCurrentType()
    {
        return EclipseType.fromString(eclipseType.getText());
    }

    private String toDuration(double sec)
    {
        return Double.toString(sec);
    }
}
