package maru.report.views;

import maru.core.model.ICoordinate;
import maru.core.model.IPropagator;
import maru.core.model.ISpacecraft;
import maru.core.utils.TimeUtil;

import org.eclipse.swt.widgets.Composite;

public class PropagationReportControl extends AbstractPropagationReportControl
{
    public PropagationReportControl(Composite parentControl)
    {
        super("Propagation Report", parentControl);
    }

    @Override
    protected void createReportText()
    {
        createDefaultReportHeader();

        append("Tab Separated Columns: ");
        append("Time (UTC) | ");
        append("Pos X (km) | ");
        append("Pos Y (km) | ");
        append("Pos Z (km) | ");
        append("Vel X (km/sec) | ");
        append("Vel Y (km/sec) | ");
        append("Vel Z (km/sec)");
        appendln("");
        appendln("");

        createReportBody();
    }

    private void createReportBody()
    {
        ISpacecraft element = getSelectedElement();
        IPropagator propagator = element.getPropagator();

        long startTime = getCurrentProject().getStartTime();
        long stopTime = getCurrentProject().getStopTime();
        long stepSize = getSelectedStepSize();

        for (ICoordinate coordinate : propagator.getCoordinates(element, startTime, stopTime, stepSize))
        {
            append(TimeUtil.asISO8601(coordinate.getTime()));
            append("\t");

            append(toKm(coordinate.getPosition().getX()));
            append("\t");
            append(toKm(coordinate.getPosition().getY()));
            append("\t");
            append(toKm(coordinate.getPosition().getZ()));
            append("\t");

            append(toKm(coordinate.getVelocity().getX()));
            append("\t");
            append(toKm(coordinate.getVelocity().getY()));
            append("\t");
            append(toKm(coordinate.getVelocity().getZ()));
            appendln("");
        }
    }

    private String toKm(double m)
    {
        return doubleToString(m / 1000.0);
    }
}
