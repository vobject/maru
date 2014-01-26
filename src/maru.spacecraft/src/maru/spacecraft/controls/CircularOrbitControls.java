package maru.spacecraft.controls;

import maru.core.model.IScenarioProject;
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
import org.orekit.orbits.CircularOrbit;
import org.orekit.orbits.Orbit;
import org.orekit.orbits.OrbitType;
import org.orekit.orbits.PositionAngle;
import org.orekit.time.AbsoluteDate;

public class CircularOrbitControls extends OrbitControls
{
    // source (20140124): https://www.orekit.org/static/architecture/orbits.html
    private static final String ORBIT_TYPE_DESCRIPTION = "<a href=\"https://www.orekit.org/forge/projects/orekit/wiki/Orbits\">Orekit:</a> Circular orbit, used to represent almost circular orbit, i.e orbit with low eccentricity.";

    private Text semimajorAxis;
    private Text circularEccentricityX;
    private Text circularEccentricityY;
    private Text inclination;
    private Text raan;
    private Text alpha;
    private Combo alphaType;
    private Text date;
    private Text frame;
    private Text attractionCoefficient;

    private String initialSemimajorAxis;
    private String initialCircularEccentricityX;
    private String initialCircularEccentricityY;
    private String initialInclination;
    private String initialRaan;
    private String initialAlpha;
    private String initialAlphaType;
    private String initialDate;
    private String initialFrame;
    private String initialAttractionCoefficient;

    public CircularOrbitControls(Composite parent, IScenarioProject scenario)
    {
        super(parent, scenario);

        initDefaults(scenario);
        initControls(scenario);
    }

    public CircularOrbitControls(Composite parent, IScenarioProject scenario, Orbit orbit)
    {
        super(parent, scenario, orbit);

        if (orbit != null) {
            CircularOrbit circular = (CircularOrbit) OrbitType.CIRCULAR.convertType(orbit);
            initDefaults(scenario, circular);
            initControls(scenario, circular);
        } else {
            initDefaults(scenario);
            initControls(scenario);
        }
    }

    @Override
    public CircularOrbit getOrbit()
    {
        return createCircularOrbit();
    }

    @Override
    public boolean isValid()
    {
        try {
            Double.parseDouble(semimajorAxis.getText());
            Double.parseDouble(circularEccentricityX.getText());
            Double.parseDouble(circularEccentricityY.getText());
            Double.parseDouble(inclination.getText());
            Double.parseDouble(raan.getText());
            Double.parseDouble(alpha.getText());
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
    public void refreshDefaults(IScenarioProject scenario, Orbit orbit)
    {
        CircularOrbit circular = (CircularOrbit) OrbitType.CIRCULAR.convertType(orbit);
        initDefaults(scenario, circular);
        initControls(scenario, circular);
    }

    public double getSemimajorAxis()
    {
        // convert to meters. meters are used internally
        return (Double.parseDouble(semimajorAxis.getText()) * 1000.0);
    }

    public double getCircularEccentricityX()
    {
        return Double.parseDouble(circularEccentricityX.getText());
    }

    public double getCircularEccentricityY()
    {
        return Math.toRadians(Double.parseDouble(circularEccentricityY.getText()));
    }

    public double getInclination()
    {
        return Math.toRadians(Double.parseDouble(inclination.getText()));
    }

    public double getRaan()
    {
        return Math.toRadians(Double.parseDouble(raan.getText()));
    }

    public double getAlpha()
    {
        return Math.toRadians(Double.parseDouble(alpha.getText()));
    }

    public PositionAngle getAlphaType()
    {
        return PositionAngle.valueOf(alphaType.getText());
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
    protected void createControls(IScenarioProject scenario)
    {
        Composite container = getContainer();

        new Label(container, SWT.NONE).setText("Semimajor Axis (km):");
        semimajorAxis = new Text(container, SWT.BORDER);
        semimajorAxis.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        new Label(container, SWT.NONE).setText("Circular Eccentricity Vector X:");
        circularEccentricityX = new Text(container, SWT.BORDER);
        circularEccentricityX.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        new Label(container, SWT.NONE).setText("Circular Eccentricity Vector Y:");
        circularEccentricityY = new Text(container, SWT.BORDER);
        circularEccentricityY.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        new Label(container, SWT.NONE).setText("Inclination (deg):");
        inclination = new Text(container, SWT.BORDER);
        inclination.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        new Label(container, SWT.NONE).setText("RAAN (deg):");
        raan = new Text(container, SWT.BORDER);
        raan.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        new Label(container, SWT.NONE).setText("Alpha (deg):");
        alpha = new Text(container, SWT.BORDER);
        alpha.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        new Label(container, SWT.NONE).setText("Alpha type:");
        alphaType = new Combo(container, SWT.BORDER | SWT.READ_ONLY);
        alphaType.setItems(new String[] { PositionAngle.TRUE.toString(),
                                          PositionAngle.ECCENTRIC.toString(),
                                          PositionAngle.MEAN.toString() });
        alphaType.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

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

    private void initDefaults(IScenarioProject scenario)
    {
        initialSemimajorAxis = "6678.14";
        initialCircularEccentricityX = "0";
        initialCircularEccentricityY = "0";
        initialInclination = "28.5";
        initialRaan = "0";
        initialAlpha = "0";
        initialAlphaType = PositionAngle.TRUE.toString();
        initialDate = TimeUtils.asISO8601(scenario.getStartTime());
        initialFrame = FramesFactory.getEME2000().toString();
        initialAttractionCoefficient = Double.toString(scenario.getCentralBody().getGM());
    }

    private void initDefaults(IScenarioProject scenario, CircularOrbit orbit)
    {
        initialSemimajorAxis = Double.toString(orbit.getA() / 1000.0); // convert to km
        initialCircularEccentricityX = Double.toString(orbit.getCircularEx());
        initialCircularEccentricityY = Double.toString(orbit.getCircularEy());
        initialInclination = Double.toString(Math.toDegrees(orbit.getI()));
        initialRaan = Double.toString(Math.toDegrees(orbit.getRightAscensionOfAscendingNode()));
        initialAlpha = Double.toString(Math.toDegrees(orbit.getAlpha(PositionAngle.TRUE)));
        initialAlphaType = PositionAngle.TRUE.toString();
        initialDate = TimeUtils.asISO8601(orbit.getDate());
        initialFrame = orbit.getFrame().toString();
        initialAttractionCoefficient = Double.toString(orbit.getMu());
    }

    private void initControls(IScenarioProject scenario)
    {
        setDescription(ORBIT_TYPE_DESCRIPTION);

        semimajorAxis.setText(initialSemimajorAxis);
        circularEccentricityX.setText(initialCircularEccentricityX);
        circularEccentricityY.setText(initialCircularEccentricityY);
        inclination.setText(initialInclination);
        raan.setText(initialRaan);
        alpha.setText(initialAlpha);
        alphaType.select(0); // select PositionAngle.TRUE
        date.setText(initialDate);
        frame.setText(initialFrame);
        attractionCoefficient.setText(initialAttractionCoefficient);
    }

    private void initControls(IScenarioProject scenario, final CircularOrbit orbit)
    {
        setDescription(ORBIT_TYPE_DESCRIPTION);

        semimajorAxis.setText(initialSemimajorAxis);
        circularEccentricityX.setText(initialCircularEccentricityX);
        circularEccentricityY.setText(initialCircularEccentricityY);
        inclination.setText(initialInclination);
        raan.setText(initialRaan);
        alpha.setText(initialAlpha);
        alphaType.select(0); // select PositionAngle.TRUE
        alphaType.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                double newAlpha = orbit.getAlpha(getAlphaType());
                String newAlphaString = Double.toString(Math.toDegrees(newAlpha));
                alpha.setText(newAlphaString);
            }
        });
        date.setText(initialDate);
        frame.setText(initialFrame);
        attractionCoefficient.setText(initialAttractionCoefficient);
    }

    private boolean hasOrbitChanged()
    {
        String newSemimajorAxis = semimajorAxis.getText();
        String newEccentricityX = circularEccentricityX.getText();
        String newEccentricityY = circularEccentricityY.getText();
        String newInclination = inclination.getText();
        String newRaan = raan.getText();
        String newAlpha = alpha.getText();
        String newAlphaType = alphaType.getText();
        String newDate = date.getText();
        String newFrame = frame.getText();
        String newAttractionCoefficient = attractionCoefficient.getText();

        if (!newSemimajorAxis.equals(initialSemimajorAxis) ||
            !newEccentricityX.equals(initialCircularEccentricityX) ||
            !newEccentricityY.equals(initialCircularEccentricityY) ||
            !newInclination.equals(initialInclination) ||
            !newRaan.equals(initialRaan) ||
            !newAlpha.equals(initialAlpha) ||
            !newAlphaType.equals(initialAlphaType) ||
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

    private CircularOrbit createCircularOrbit()
    {
        double a = getSemimajorAxis();
        double ex = getCircularEccentricityX();
        double ey = getCircularEccentricityY();
        double i = getInclination();
        double raan = getRaan();
        double alpha = getAlpha();
        PositionAngle type = getAlphaType();
        Frame frame = getFrame();
        AbsoluteDate date = getDate();
        double mu = getCentralAttractionCoefficient();

        return new CircularOrbit(a, ex, ey, i, raan, alpha, type, frame, date, mu);
    }
}
