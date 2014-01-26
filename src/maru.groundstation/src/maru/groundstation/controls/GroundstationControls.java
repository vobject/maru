package maru.groundstation.controls;

import maru.core.model.IGroundstation;
import maru.core.model.IScenarioProject;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.orekit.bodies.GeodeticPoint;

public class GroundstationControls
{
    private Composite container;
    private String errorMsg = "";

    private Text latitude;
    private Text longitude;
    private Text altitude;
    private Text elevation;

    private String initialLatitude;
    private String initialLongitude;
    private String initialAltitude;
    private String initialElevation;

    public GroundstationControls(Composite parent, IScenarioProject scenario)
    {
        createContainer(parent);
        createControls(scenario);

        initDefaults(scenario);
        initControls(scenario);
    }

    public GroundstationControls(Composite parent, IScenarioProject scenario, IGroundstation element)
    {
        createContainer(parent);
        createControls(scenario);

        if (element != null) {
            initDefaults(scenario, element);
            initControls(scenario, element);
        } else {
            initDefaults(scenario);
            initControls(scenario);
        }
    }

    public Composite getContainer()
    {
        return container;
    }

    public void setContainer(Composite container)
    {
        this.container = container;
    }

    public String getErrorMessage()
    {
        return errorMsg;
    }

    public void setErrorMessage(String msg)
    {
        this.errorMsg = msg;
    }

    public boolean isValid()
    {
        try {
            Double.parseDouble(latitude.getText());
            Double.parseDouble(longitude.getText());
            Double.parseDouble(altitude.getText());
            Double.parseDouble(elevation.getText());
        } catch (NumberFormatException e) {
            setErrorMessage("Invalid number format.");
            return false;
        }

        setErrorMessage(null);
        return true;
    }

    public boolean isModified()
    {
        return haveParametersChanged();
    }

    public void refreshDefaults(IScenarioProject scenario, IGroundstation element)
    {
        initDefaults(scenario, element);
        initControls(scenario, element);
    }

    public GeodeticPoint getGeodeticPoint()
    {
        return createGeodeticPoint();
    }

    public double getLatitude()
    {
        return getGeodeticPoint().getLatitude();
    }

    public double getLongitude()
    {
        return getGeodeticPoint().getLongitude();
    }

    public double getAltitude()
    {
        return getGeodeticPoint().getAltitude();
    }

    public double getElevationAngle()
    {
        return Math.toRadians(Double.parseDouble(elevation.getText()));
    }

    private void createContainer(Composite parent)
    {
        GridLayout layout = new GridLayout(2, false);
        layout.marginWidth = 0;
        layout.marginHeight = 0;

        GridData data = new GridData();
        data.horizontalAlignment = SWT.FILL;
        data.verticalAlignment = SWT.FILL;
        data.grabExcessHorizontalSpace = true;
        data.grabExcessVerticalSpace = true;
        data.horizontalSpan = 2;

        Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(layout);
        container.setLayoutData(data);

        setContainer(container);
    }

    private void createControls(IScenarioProject scenario)
    {
        Composite container = getContainer();

        new Label(container, SWT.NONE).setText("Latitude (deg):");
        latitude = new Text(container, SWT.BORDER);
        latitude.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        new Label(container, SWT.NONE).setText("Longitude (deg):");
        longitude = new Text(container, SWT.BORDER);
        longitude.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        new Label(container, SWT.NONE).setText("Altitude (m):");
        altitude = new Text(container, SWT.BORDER);
        altitude.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        new Label(container, SWT.NONE).setText("Elevation angle (deg):");
        elevation = new Text(container, SWT.BORDER);
        elevation.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
    }

    private void initDefaults(IScenarioProject scenario)
    {
        initialLatitude = "49.78186646";
        initialLongitude = "9.97290914";
        initialAltitude = "274.68";
        initialElevation = "0.0";
    }

    private void initDefaults(IScenarioProject scenario, IGroundstation element)
    {
        GeodeticPoint position = element.getGeodeticPosition();
        double angle = element.getElevationAngle();

        initialLatitude = Double.toString(Math.toDegrees(position.getLatitude()));
        initialLongitude = Double.toString(Math.toDegrees(position.getLongitude()));
        initialAltitude = Double.toString(position.getAltitude());
        initialElevation = Double.toString(Math.toDegrees(angle));
    }

    private void initControls(IScenarioProject scenario)
    {
        latitude.setText(initialLatitude);
        longitude.setText(initialLongitude);
        altitude.setText(initialAltitude);
        elevation.setText(initialElevation);
    }

    private void initControls(IScenarioProject scenario, IGroundstation element)
    {
        initControls(scenario);
    }

    private boolean haveParametersChanged()
    {
        String newLatitude = latitude.getText();
        String newLongitude = longitude.getText();
        String newAltitude = altitude.getText();
        String newElevation = elevation.getText();

        if (!newLatitude.equals(initialLatitude) ||
            !newLongitude.equals(initialLongitude) ||
            !newAltitude.equals(initialAltitude) ||
            !newElevation.equals(initialElevation))
        {
            return true;
        }
        else
        {
            // the user did not change the values in the text controls
            return false;
        }
    }

    private GeodeticPoint createGeodeticPoint()
    {
        // angles are displayed in degree in the user interface
        // convert them to radiant for internal use

        double lat = Math.toRadians(Double.parseDouble(latitude.getText()));
        double lon = Math.toRadians(Double.parseDouble(longitude.getText()));
        double alt = Double.parseDouble(altitude.getText());

        return new GeodeticPoint(lat, lon, alt);
    }
}
