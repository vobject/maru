package maru.spacecraft.controls;

import maru.core.utils.TimeUtils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.orekit.errors.OrekitException;
import org.orekit.frames.Frame;
import org.orekit.frames.FramesFactory;
import org.orekit.frames.Predefined;
import org.orekit.orbits.KeplerianOrbit;
import org.orekit.orbits.Orbit;
import org.orekit.orbits.OrbitType;
import org.orekit.orbits.PositionAngle;
import org.orekit.time.AbsoluteDate;

public class KeplerianOrbitControls extends OrbitControls
{
    // source (20140124): https://www.orekit.org/static/architecture/orbits.html
    private static final String ORBIT_TYPE_DESCRIPTION = "Classic elliptical keplerian orbit.";

    private Text semimajorAxis;
    private Text eccentricity;
    private Text inclination;
    private Text argumentOfPerigee;
    private Text raan;
    private Text anomaly;
    private Combo anomalyType;
    private Text date;
    private Text frame;
    private Text attractionCoefficient;

    private String initialSemimajorAxis;
    private String initialEccentricity;
    private String initialInclination;
    private String initialArgumentOfPerigee;
    private String initialRaan;
    private String initialAnomaly;
    private String initialAnomalyType;
    private String initialDate;
    private String initialFrame;
    private String initialAttractionCoefficient;

    public KeplerianOrbitControls(Composite parent)
    {
        super(parent);

        initDefaults();
        initControls();
    }

    public KeplerianOrbitControls(Composite parent, Orbit orbit)
    {
        super(parent, orbit);

        refreshDefaults(orbit);
    }

    @Override
    public KeplerianOrbit getOrbit()
    {
        return createKeplerianOrbit();
    }

    @Override
    public boolean isValid()
    {
        try {
            Double.parseDouble(semimajorAxis.getText());
            Double.parseDouble(eccentricity.getText());
            Double.parseDouble(inclination.getText());
            Double.parseDouble(argumentOfPerigee.getText());
            Double.parseDouble(raan.getText());
            Double.parseDouble(anomaly.getText());
            Double.parseDouble(attractionCoefficient.getText());
        } catch (NumberFormatException e) {
            setErrorMessage("Invalid number format.");
            return false;
        }

        try {
            TimeUtils.fromString(date.getText());
        } catch (IllegalArgumentException e) {
            setErrorMessage("Invalid date format.");
            return false;
        }

        setErrorMessage(null);
        return true;
    }

    @Override
    public boolean isModified()
    {
        return hasOrbitChanged();
    }

    @Override
    public void refreshDefaults(Orbit orbit)
    {
        if (orbit != null)
        {
            KeplerianOrbit keplerian = (KeplerianOrbit) OrbitType.KEPLERIAN.convertType(orbit);
            initDefaults(keplerian);
            initControls(keplerian);
        }
        else
        {
            initDefaults();
            initControls();
        }
    }

    public double getSemimajorAxis()
    {
        // convert to meters. meters are used internally
        return Double.parseDouble(semimajorAxis.getText()) * 1000.0;
    }

    public double getEccentricity()
    {
        return Double.parseDouble(eccentricity.getText());
    }

    public double getInclination()
    {
        return Math.toRadians(Double.parseDouble(inclination.getText()));
    }

    public double getArgumentOfPerigee()
    {
        return Math.toRadians(Double.parseDouble(argumentOfPerigee.getText()));
    }

    public double getRaan()
    {
        return Math.toRadians(Double.parseDouble(raan.getText()));
    }

    public double getAnomaly()
    {
        return Math.toRadians(Double.parseDouble(anomaly.getText()));
    }

    public PositionAngle getAnomalyType()
    {
        return PositionAngle.valueOf(anomalyType.getText());
    }

    public AbsoluteDate getDate()
    {
        return TimeUtils.fromString(date.getText()).getTime();
    }

    public Frame getFrame()
    {
        try
        {
            return FramesFactory.getFrame(Predefined.valueOf(frame.getText()));
        }
        catch (OrekitException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public double getCentralAttractionCoefficient()
    {
        return Double.parseDouble(attractionCoefficient.getText());
    }

    @Override
    protected void createControls()
    {
        Composite container = getContainer();

        new Label(container, SWT.NONE).setText("Semimajor Axis (km):");
        semimajorAxis = new Text(container, SWT.BORDER);
        semimajorAxis.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        new Label(container, SWT.NONE).setText("Eccentricity:");
        eccentricity = new Text(container, SWT.BORDER);
        eccentricity.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        new Label(container, SWT.NONE).setText("Inclination (deg):");
        inclination = new Text(container, SWT.BORDER);
        inclination.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        new Label(container, SWT.NONE).setText("Argument of Perigee (deg):");
        argumentOfPerigee = new Text(container, SWT.BORDER);
        argumentOfPerigee.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        new Label(container, SWT.NONE).setText("RAAN (deg):");
        raan = new Text(container, SWT.BORDER);
        raan.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        new Label(container, SWT.NONE).setText("Anomaly (deg):");
        anomaly = new Text(container, SWT.BORDER);
        anomaly.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        new Label(container, SWT.NONE).setText("Anomaly type:");
        anomalyType = new Combo(container, SWT.BORDER | SWT.READ_ONLY);
        anomalyType.setItems(new String[] { PositionAngle.TRUE.toString(),
                                            PositionAngle.ECCENTRIC.toString(),
                                            PositionAngle.MEAN.toString() });
        anomalyType.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        new Label(container, SWT.NONE).setText("Date:");
        date = new Text(container, SWT.BORDER);
        date.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        new Label(container, SWT.NONE).setText("Frame:");
        frame = new Text(container, SWT.BORDER);
        frame.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        frame.setEnabled(false);

        new Label(container, SWT.NONE).setText("Central attraction coefficient  (m\u00b3/s\u00b2):");
        attractionCoefficient = new Text(container, SWT.BORDER);
        attractionCoefficient.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        attractionCoefficient.setEnabled(false);
    }

    private void initDefaults()
    {
        initialSemimajorAxis = "0";
        initialEccentricity = "0";
        initialInclination = "0";
        initialArgumentOfPerigee = "0";
        initialRaan = "0";
        initialAnomaly = "0";
        initialAnomalyType = PositionAngle.TRUE.toString();
        initialDate = TimeUtils.asISO8601(TimeUtils.now());
        initialFrame = FramesFactory.getEME2000().toString();
        initialAttractionCoefficient = "0";
    }

    private void initDefaults(KeplerianOrbit orbit)
    {
        initialSemimajorAxis = Double.toString(orbit.getA() / 1000.0); // convert to km
        initialEccentricity = Double.toString(orbit.getE());
        initialInclination = Double.toString(Math.toDegrees(orbit.getI()));
        initialArgumentOfPerigee = Double.toString(Math.toDegrees(orbit.getPerigeeArgument()));
        initialRaan = Double.toString(Math.toDegrees(orbit.getRightAscensionOfAscendingNode()));
        initialAnomaly = Double.toString(Math.toDegrees(orbit.getAnomaly(PositionAngle.TRUE)));
        initialAnomalyType = PositionAngle.TRUE.toString();
        initialDate = TimeUtils.asISO8601(orbit.getDate());
        initialFrame = orbit.getFrame().toString();
        initialAttractionCoefficient = Double.toString(orbit.getMu());
    }

    private void initControls()
    {
        setDescription(ORBIT_TYPE_DESCRIPTION);

        semimajorAxis.setText(initialSemimajorAxis);
        eccentricity.setText(initialEccentricity);
        inclination.setText(initialInclination);
        argumentOfPerigee.setText(initialArgumentOfPerigee);
        raan.setText(initialRaan);
        anomaly.setText(initialAnomaly);
        anomalyType.select(0); // select PositionAngle.TRUE
        date.setText(initialDate);
        frame.setText(initialFrame);
        attractionCoefficient.setText(initialAttractionCoefficient);
    }

    private void initControls(final KeplerianOrbit orbit)
    {
        setDescription(ORBIT_TYPE_DESCRIPTION);

        semimajorAxis.setText(initialSemimajorAxis);
        eccentricity.setText(initialEccentricity);
        inclination.setText(initialInclination);
        argumentOfPerigee.setText(initialArgumentOfPerigee);
        raan.setText(initialRaan);
        anomaly.setText(initialAnomaly);
        anomalyType.select(0); // select PositionAngle.TRUE
        anomalyType.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                double newAnomlay = orbit.getAnomaly(getAnomalyType());
                String newAnomalyString = Double.toString(Math.toDegrees(newAnomlay));
                anomaly.setText(newAnomalyString);
            }
        });
        date.setText(initialDate);
        frame.setText(initialFrame);
        attractionCoefficient.setText(initialAttractionCoefficient);
    }

    private boolean hasOrbitChanged()
    {
        String newSemimajorAxis = semimajorAxis.getText();
        String newEccentricity = eccentricity.getText();
        String newInclination = inclination.getText();
        String newArgumentOfPerigee = argumentOfPerigee.getText();
        String newRaan = raan.getText();
        String newAnomaly = anomaly.getText();
        String newAnomalyType = anomalyType.getText();
        String newDate = date.getText();
        String newFrame = frame.getText();
        String newAttractionCoefficient = attractionCoefficient.getText();

        if (!newSemimajorAxis.equals(initialSemimajorAxis) ||
            !newEccentricity.equals(initialEccentricity) ||
            !newInclination.equals(initialInclination) ||
            !newArgumentOfPerigee.equals(initialArgumentOfPerigee) ||
            !newRaan.equals(initialRaan) ||
            !newAnomaly.equals(initialAnomaly) ||
            !newAnomalyType.equals(initialAnomalyType) ||
            !newDate.equals(initialDate) ||
            !newFrame.equals(initialFrame) ||
            !newAttractionCoefficient.equals(initialAttractionCoefficient))
        {
            return true;
        }
        else
        {
            // the user did not change the values in the text controls
            return false;
        }
    }

    private KeplerianOrbit createKeplerianOrbit()
    {
        double a = getSemimajorAxis();
        double e = getEccentricity();
        double i = getInclination();
        double pa = getArgumentOfPerigee();
        double raan = getRaan();
        double anomaly = getAnomaly();
        PositionAngle type = getAnomalyType();
        Frame frame = getFrame();
        AbsoluteDate date = getDate();
        double mu = getCentralAttractionCoefficient();

        return new KeplerianOrbit(a, e, i, pa, raan, anomaly, type, frame, date, mu);
    }
}
