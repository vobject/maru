package maru.spacecraft.controls;

import maru.core.model.IScenarioProject;
import maru.core.model.utils.TimeUtils;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.orekit.errors.OrekitException;
import org.orekit.frames.Frame;
import org.orekit.frames.FramesFactory;
import org.orekit.frames.Predefined;
import org.orekit.orbits.CartesianOrbit;
import org.orekit.orbits.Orbit;
import org.orekit.orbits.OrbitType;
import org.orekit.time.AbsoluteDate;
import org.orekit.utils.PVCoordinates;

public class CartesianOrbitControls extends OrbitControls
{
    // source (20140124): https://www.orekit.org/static/architecture/orbits.html
    private static final String ORBIT_TYPE_DESCRIPTION = "<a href=\"https://www.orekit.org/forge/projects/orekit/wiki/Orbits\">Orekit:</a> Cartesian orbit, associated to its frame definition.";

    private Text posX;
    private Text posY;
    private Text posZ;
    private Text velX;
    private Text velY;
    private Text velZ;
    private Text date;
    private Text frame;
    private Text attractionCoefficient;

    private String initialPosX;
    private String initialPosY;
    private String initialPosZ;
    private String initialVelX;
    private String initialVelY;
    private String initialVelZ;
    private String initialDate;
    private String initialFrame;
    private String initialAttractionCoefficient;

    public CartesianOrbitControls(Composite parent, IScenarioProject scenario)
    {
        super(parent, scenario);

        initDefaults(scenario);
        initControls(scenario);
    }

    public CartesianOrbitControls(Composite parent, IScenarioProject scenario, Orbit orbit)
    {
        super(parent, scenario, orbit);

        if (orbit != null) {
            CartesianOrbit cartesian = (CartesianOrbit) OrbitType.CARTESIAN.convertType(orbit);
            initDefaults(scenario, cartesian);
            initControls(scenario, cartesian);
        } else {
            initDefaults(scenario);
            initControls(scenario);
        }
    }

    @Override
    public CartesianOrbit getOrbit()
    {
        return createCartesianOrbit();
    }

    @Override
    public boolean isValid()
    {
        try {
            Double.parseDouble(posX.getText());
            Double.parseDouble(posY.getText());
            Double.parseDouble(posZ.getText());
            Double.parseDouble(velX.getText());
            Double.parseDouble(velY.getText());
            Double.parseDouble(velZ.getText());
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
        CartesianOrbit cartesian = (CartesianOrbit) OrbitType.CARTESIAN.convertType(orbit);
        initDefaults(scenario, cartesian);
        initControls(scenario, cartesian);
    }

    public double getPositionX()
    {
        // convert to meters. we use meters internally
        return Double.parseDouble(posX.getText()) * 1000.0;
    }

    public double getPositionY()
    {
        // convert to meters. we use meters internally
        return Double.parseDouble(posY.getText()) * 1000.0;
    }

    public double getPositionZ()
    {
        // convert to meters. we use meters internally
        return Double.parseDouble(posZ.getText()) * 1000.0;
    }

    public double getVelocityX()
    {
        // convert to meters. we use meters internally
        return Double.parseDouble(velX.getText()) * 1000.0;
    }

    public double getVelocityY()
    {
        // convert to meters. we use meters internally
        return Double.parseDouble(velY.getText()) * 1000.0;
    }

    public double getVelocityZ()
    {
        // convert to meters. we use meters internally
        return Double.parseDouble(velZ.getText()) * 1000.0;
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

        new Label(container, SWT.NONE).setText("Position X (km):");
        posX = new Text(container, SWT.BORDER);
        posX.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        new Label(container, SWT.NONE).setText("Position Y (km):");
        posY = new Text(container, SWT.BORDER);
        posY.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        new Label(container, SWT.NONE).setText("Position Z (km):");
        posZ = new Text(container, SWT.BORDER);
        posZ.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        new Label(container, SWT.NONE).setText("Velocity X (km/s):");
        velX = new Text(container, SWT.BORDER);
        velX.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        new Label(container, SWT.NONE).setText("Velocity Y (km/s):");
        velY = new Text(container, SWT.BORDER);
        velY.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        new Label(container, SWT.NONE).setText("Velocity Z (km/s):");
        velZ = new Text(container, SWT.BORDER);
        velZ.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

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
        initialPosX = "6678.14";
        initialPosY = "0";
        initialPosZ = "0";
        initialVelX = "0";
        initialVelY = "6.789529";
        initialVelZ = "3.686413";
        initialDate = TimeUtils.asISO8601(scenario.getStartTime());
        initialFrame = FramesFactory.getEME2000().toString();
        initialAttractionCoefficient = Double.toString(scenario.getCentralBody().getGM());
    }

    private void initDefaults(IScenarioProject scenario, CartesianOrbit orbit)
    {
        Vector3D pos = orbit.getPVCoordinates().getPosition();
        Vector3D vel = orbit.getPVCoordinates().getVelocity();

        // convert vector values from meters to km
        initialPosX = Double.toString(pos.getX() / 1000.0);
        initialPosY = Double.toString(pos.getY() / 1000.0);
        initialPosZ = Double.toString(pos.getZ() / 1000.0);
        initialVelX = Double.toString(vel.getX() / 1000.0);
        initialVelY = Double.toString(vel.getY() / 1000.0);
        initialVelZ = Double.toString(vel.getZ() / 1000.0);
        initialDate = TimeUtils.asISO8601(orbit.getDate());
        initialFrame = orbit.getFrame().toString();
        initialAttractionCoefficient = Double.toString(orbit.getMu());
    }

    private void initControls(IScenarioProject scenario)
    {
        setDescription(ORBIT_TYPE_DESCRIPTION);

        posX.setText(initialPosX);
        posY.setText(initialPosY);
        posZ.setText(initialPosZ);
        velX.setText(initialVelX);
        velY.setText(initialVelY);
        velZ.setText(initialVelZ);
        date.setText(initialDate);
        frame.setText(initialFrame);
        attractionCoefficient.setText(initialAttractionCoefficient);
    }

    private void initControls(IScenarioProject scenario, final CartesianOrbit orbit)
    {
        initControls(scenario);
    }

    private boolean hasOrbitChanged()
    {
        String newPosX = posX.getText();
        String newPosY = posY.getText();
        String newPosZ = posZ.getText();
        String newVelX = velX.getText();
        String newVelY = velY.getText();
        String newVelZ = velZ.getText();
        String newDate = date.getText();
        String newFrame = frame.getText();
        String newAttractionCoefficient = attractionCoefficient.getText();

        if (!newPosX.equals(initialPosX) ||
            !newPosY.equals(initialPosY) ||
            !newPosZ.equals(initialPosZ) ||
            !newVelX.equals(initialVelX) ||
            !newVelY.equals(initialVelY) ||
            !newVelZ.equals(initialVelZ) ||
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

    private CartesianOrbit createCartesianOrbit()
    {
        Vector3D pos = new Vector3D(getPositionX(), getPositionY(), getPositionZ());
        Vector3D vel = new Vector3D(getVelocityX(), getVelocityY(), getVelocityZ());
        PVCoordinates pv = new PVCoordinates(pos, vel);

        return new CartesianOrbit(
            pv,
            getFrame(),
            getDate(),
            getCentralAttractionCoefficient()
        );
    }
}
