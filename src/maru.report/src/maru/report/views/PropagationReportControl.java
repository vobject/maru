package maru.report.views;

import maru.core.model.ICoordinate;
import maru.core.model.IPropagator;
import maru.core.model.ISpacecraft;
import maru.core.model.utils.TimeUtils;

import org.eclipse.swt.widgets.Composite;
import org.orekit.errors.OrekitException;
import org.orekit.time.AbsoluteDate;

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

        try {
            createReportBody();
        } catch (OrekitException e) {
            appendln("ERROR: Exception thrown!");
            append(e.toString());
        }
    }

    private void createReportBody() throws OrekitException
    {
        ISpacecraft element = getSelectedElement();
        IPropagator propagator = element.getPropagator();

        AbsoluteDate start = getCurrentProject().getStartTime();
        AbsoluteDate stop = getCurrentProject().getStopTime();
        long stepSize = getSelectedStepSize();

        for (ICoordinate coordinate : propagator.getCoordinates(start, stop, stepSize, element))
        {
            append(TimeUtils.asISO8601(coordinate.getDate()));
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
