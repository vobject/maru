package maru.groundstation.propertypages;

import maru.IMaruResource;
import maru.core.model.CoreModel;
import maru.core.model.ICoordinate;
import maru.groundstation.MaruGroundstationResources;
import maru.groundstation.earth.GeodeticCoordinate;
import maru.groundstation.earth.GeodeticGroundstation;
import maru.ui.propertypages.UiPropagatablePropertyPage;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class GeodeticGroundstationPropertyPage extends UiPropagatablePropertyPage
{
    private Text latitude;
    private Text longitude;
    private Text altitude;
    private Text elevation;

    private String initialLatitude;
    private String initialLongitude;
    private String initialAltitude;
    private String initialElevation;

    @Override
    protected String[] getImageNames()
    {
        // return an empty array be default
        return new String[] {
            "", // empty string allows to disable element image
            MaruGroundstationResources.GROUNDSTATION_DEFAULT_1.getName(),
            MaruGroundstationResources.GROUNDSTATION_DEFAULT_2.getName(),
            MaruGroundstationResources.GROUNDSTATION_DEFAULT_3.getName(),
            MaruGroundstationResources.GROUNDSTATION_DEFAULT_4.getName(),
            MaruGroundstationResources.GROUNDSTATION_DEFAULT_5.getName(),
        };
    }

    @Override
    protected IMaruResource getImageFromName(String name)
    {
        return MaruGroundstationResources.fromName(name);
    }

    @Override
    public GeodeticGroundstation getScenarioElement()
    {
        return (GeodeticGroundstation) getUiElement().getUnderlyingElement();
    }

    @Override
    public boolean performOk()
    {
        if (!super.performOk()) {
            return false;
        }

        if (hasInitialCoordinateChanged())
        {
            GeodeticGroundstation element = getScenarioElement();
            GeodeticCoordinate newCoordinate = createNewCoordinate(element);
            CoreModel.getDefault().changeInitialCoordinate(element, newCoordinate, true);
        }

        initDefaults();
        return true;
    }

    @Override
    protected Composite createContents(Composite parent)
    {
        Composite container = super.createContents(parent);
        addSeparator(container);

        createControls(container);

        initDefaults();
        initControls();

        return container;
    }

    private Composite createControls(Composite container)
    {
        new Label(container, SWT.NONE).setText("Latitude (deg):");
        latitude = new Text(container, SWT.BORDER | SWT.SINGLE);
        latitude.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        new Label(container, SWT.NONE).setText("Longitude (deg):");
        longitude = new Text(container, SWT.BORDER | SWT.SINGLE);
        longitude.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        new Label(container, SWT.NONE).setText("Altitude (m):");
        altitude = new Text(container, SWT.BORDER | SWT.SINGLE);
        altitude.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        new Label(container, SWT.NONE).setText("Elevation (deg):");
        elevation = new Text(container, SWT.BORDER | SWT.SINGLE);
        elevation.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        return container;
    }

    private void initDefaults()
    {
        GeodeticGroundstation element = getScenarioElement();
        if (element == null) {
            return;
        }

        GeodeticCoordinate coordinate = element.getInitialCoordinate();

        // angles are processes in radiant internally
        // convert them to degrees for user interface interactions

        initialLatitude = Double.toString(Math.toDegrees(coordinate.getLatitude()));
        initialLongitude = Double.toString(Math.toDegrees(coordinate.getLongitude()));
        initialAltitude = Double.toString(coordinate.getAltitude());
        initialElevation = Double.toString(Math.toDegrees(coordinate.getElevation()));
    }

    private void initControls()
    {
        GeodeticGroundstation element = getScenarioElement();
        if (element == null) {
            return;
        }

        latitude.setText(initialLatitude);
        longitude.setText(initialLongitude);
        altitude.setText(initialAltitude);
        elevation.setText(initialElevation);
    }

    private boolean hasInitialCoordinateChanged()
    {
        String newLatitude = latitude.getText();
        String newLongitude = longitude.getText();
        String newAltitude = altitude.getText();
        String newElevation = elevation.getText();

        if (!newLatitude.equals(initialLatitude)   ||
            !newLongitude.equals(initialLongitude) ||
            !newAltitude.equals(initialAltitude)   ||
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

    private GeodeticCoordinate createNewCoordinate(GeodeticGroundstation element)
    {
        // angles are displayed in degree in the user interface
        // convert them to radiant for internal use

        double lat = Math.toRadians(Double.parseDouble(latitude.getText()));
        double lon = Math.toRadians(Double.parseDouble(longitude.getText()));
        double alt = Double.parseDouble(altitude.getText());
        double elev = Math.toRadians(Double.parseDouble(elevation.getText()));
        ICoordinate initialCoordinate = element.getInitialCoordinate();

        return new GeodeticCoordinate(element.getCentralBody(),
                                      lat, lon, alt, elev,
                                      initialCoordinate.getTime());
    }
}
