package maru.spacecraft.controls;

import maru.core.model.IScenarioProject;
import maru.core.model.utils.TimeUtils;

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
import org.orekit.orbits.EquinoctialOrbit;
import org.orekit.orbits.Orbit;
import org.orekit.orbits.OrbitType;
import org.orekit.orbits.PositionAngle;
import org.orekit.time.AbsoluteDate;

public class EquinoctialOrbitControls extends OrbitControls
{
    // source (20140124): https://www.orekit.org/static/architecture/orbits.html
    private static final String ORBIT_TYPE_DESCRIPTION = "<a href=\"https://www.orekit.org/forge/projects/orekit/wiki/Orbits\">Orekit:</a> Equinoctial orbit, used to represent equinoctial orbits (almost circular orbit with almost null inclination).";

    private Text semimajorAxis;
    private Text equinoctialEccentricityX;
    private Text equinoctialEccentricityY;
    private Text inclinationX;
    private Text inclinationY;
    private Text longitude;
    private Combo longitudeType;
    private Text date;
    private Text frame;
    private Text attractionCoefficient;

    private String initialSemimajorAxis;
    private String initialEquinoctialEccentricityX;
    private String initialEquinoctialEccentricityY;
    private String initialInclinationX;
    private String initialInclinationY;
    private String initialLongitude;
    private String initialLongitudeType;
    private String initialDate;
    private String initialFrame;
    private String initialAttractionCoefficient;

    public EquinoctialOrbitControls(Composite parent, IScenarioProject scenario)
    {
        super(parent, scenario);

        initDefaults(scenario);
        initControls(scenario);
    }

    public EquinoctialOrbitControls(Composite parent, IScenarioProject scenario, Orbit orbit)
    {
        super(parent, scenario, orbit);

        if (orbit != null) {
            EquinoctialOrbit equinoctial = (EquinoctialOrbit) OrbitType.EQUINOCTIAL.convertType(orbit);
            initDefaults(scenario, equinoctial);
            initControls(scenario, equinoctial);
        } else {
            initDefaults(scenario);
            initControls(scenario);
        }
    }

    @Override
    public EquinoctialOrbit getOrbit()
    {
        return createEquinoctialOrbit();
    }

    @Override
    public boolean isValid()
    {
        try {
            Double.parseDouble(semimajorAxis.getText());
            Double.parseDouble(equinoctialEccentricityX.getText());
            Double.parseDouble(equinoctialEccentricityY.getText());
            Double.parseDouble(inclinationX.getText());
            Double.parseDouble(inclinationY.getText());
            Double.parseDouble(longitude.getText());
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
        EquinoctialOrbit equinoctial = (EquinoctialOrbit) OrbitType.EQUINOCTIAL.convertType(orbit);
        initDefaults(scenario, equinoctial);
        initControls(scenario, equinoctial);
    }

    public double getSemimajorAxis()
    {
        // convert to meters. meters are used internally
        return (Double.parseDouble(semimajorAxis.getText()) * 1000.0);
    }

    public double getCircularEccentricityX()
    {
        return Double.parseDouble(equinoctialEccentricityX.getText());
    }

    public double getCircularEccentricityY()
    {
        return Double.parseDouble(equinoctialEccentricityY.getText());
    }

    public double getInclinationX()
    {
        return Double.parseDouble(inclinationX.getText());
    }

    public double getInclinationY()
    {
        return Double.parseDouble(inclinationY.getText());
    }

    public double getLongitude()
    {
        return Math.toRadians(Double.parseDouble(longitude.getText()));
    }

    public PositionAngle getLongitudeType()
    {
        return PositionAngle.valueOf(longitudeType.getText());
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

        new Label(container, SWT.NONE).setText("Equinoctial Eccentricity Vector X:");
        equinoctialEccentricityX = new Text(container, SWT.BORDER);
        equinoctialEccentricityX.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        new Label(container, SWT.NONE).setText("Equinoctial Eccentricity Vector Y:");
        equinoctialEccentricityY = new Text(container, SWT.BORDER);
        equinoctialEccentricityY.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        new Label(container, SWT.NONE).setText("Inclination Vector X:");
        inclinationX = new Text(container, SWT.BORDER);
        inclinationX.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        new Label(container, SWT.NONE).setText("Inclination Vector Y:");
        inclinationY = new Text(container, SWT.BORDER);
        inclinationY.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        new Label(container, SWT.NONE).setText("Latitude (deg):");
        longitude = new Text(container, SWT.BORDER);
        longitude.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        new Label(container, SWT.NONE).setText("Latitude type:");
        longitudeType = new Combo(container, SWT.BORDER | SWT.READ_ONLY);
        longitudeType.setItems(new String[] { PositionAngle.TRUE.toString(),
                                              PositionAngle.ECCENTRIC.toString(),
                                              PositionAngle.MEAN.toString() });
        longitudeType.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

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
        initialEquinoctialEccentricityX = "0";
        initialEquinoctialEccentricityY = "0";
        initialInclinationX = "0.254";
        initialInclinationY = "0";
        initialLongitude = "0";
        initialLongitudeType = PositionAngle.TRUE.toString();
        initialDate = TimeUtils.asISO8601(scenario.getStartTime());
        initialFrame = FramesFactory.getEME2000().toString();
        initialAttractionCoefficient = Double.toString(scenario.getCentralBody().getGM());
    }

    private void initDefaults(IScenarioProject scenario, EquinoctialOrbit orbit)
    {
        initialSemimajorAxis = Double.toString(orbit.getA() / 1000.0); // convert to km
        initialEquinoctialEccentricityX = Double.toString(orbit.getEquinoctialEx());
        initialEquinoctialEccentricityY = Double.toString(orbit.getEquinoctialEy());
        initialInclinationX = Double.toString(orbit.getHx());
        initialInclinationY = Double.toString(orbit.getHy());
        initialLongitude = Double.toString(Math.toDegrees(orbit.getL(PositionAngle.TRUE)));
        initialLongitudeType = PositionAngle.TRUE.toString();
        initialDate = TimeUtils.asISO8601(orbit.getDate());
        initialFrame = orbit.getFrame().toString();
        initialAttractionCoefficient = Double.toString(orbit.getMu());
    }

    private void initControls(IScenarioProject scenario)
    {
        setDescription(ORBIT_TYPE_DESCRIPTION);

        semimajorAxis.setText(initialSemimajorAxis);
        equinoctialEccentricityX.setText(initialEquinoctialEccentricityX);
        equinoctialEccentricityY.setText(initialEquinoctialEccentricityY);
        inclinationX.setText(initialInclinationX);
        inclinationY.setText(initialInclinationY);
        longitude.setText(initialLongitude);
        longitudeType.select(0); // select PositionAngle.TRUE
        date.setText(initialDate);
        frame.setText(initialFrame);
        attractionCoefficient.setText(initialAttractionCoefficient);
    }

    private void initControls(IScenarioProject scenario, final EquinoctialOrbit orbit)
    {
        setDescription(ORBIT_TYPE_DESCRIPTION);

        semimajorAxis.setText(initialSemimajorAxis);
        equinoctialEccentricityX.setText(initialEquinoctialEccentricityX);
        equinoctialEccentricityY.setText(initialEquinoctialEccentricityY);
        inclinationX.setText(initialInclinationX);
        inclinationY.setText(initialInclinationY);
        longitude.setText(initialLongitude);
        longitudeType.select(0); // select PositionAngle.TRUE
        longitudeType.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                double newLongitude = orbit.getL(getLongitudeType());
                String newLongitudeString = Double.toString(Math.toDegrees(newLongitude));
                longitude.setText(newLongitudeString);
            }
        });
        date.setText(initialDate);
        frame.setText(initialFrame);
        attractionCoefficient.setText(initialAttractionCoefficient);
    }

    private boolean hasOrbitChanged()
    {
        String newSemimajorAxis = semimajorAxis.getText();
        String newEccentricityX = equinoctialEccentricityX.getText();
        String newEccentricityY = equinoctialEccentricityY.getText();
        String newInclinationX = inclinationX.getText();
        String newInclinationY = inclinationY.getText();
        String newLongitude = longitude.getText();
        String newLongitudeType = longitudeType.getText();
        String newDate = date.getText();
        String newFrame = frame.getText();
        String newAttractionCoefficient = attractionCoefficient.getText();

        if (!newSemimajorAxis.equals(initialSemimajorAxis) ||
            !newEccentricityX.equals(initialEquinoctialEccentricityX) ||
            !newEccentricityY.equals(initialEquinoctialEccentricityY) ||
            !newInclinationX.equals(initialInclinationX) ||
            !newInclinationY.equals(initialInclinationY) ||
            !newLongitude.equals(initialLongitude) ||
            !newLongitudeType.equals(initialLongitudeType) ||
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

    private EquinoctialOrbit createEquinoctialOrbit()
    {
        double a = getSemimajorAxis();
        double ex = getCircularEccentricityX();
        double ey = getCircularEccentricityY();
        double hx = getInclinationX();
        double hy = getInclinationY();
        double lon = getLongitude();
        PositionAngle type = getLongitudeType();
        Frame frame = getFrame();
        AbsoluteDate date = getDate();
        double mu = getCentralAttractionCoefficient();

        return new EquinoctialOrbit(a, ex, ey, hx, hy, lon, type, frame, date, mu);
    }
}
