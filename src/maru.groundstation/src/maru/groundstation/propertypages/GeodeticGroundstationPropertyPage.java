package maru.groundstation.propertypages;

import maru.IMaruResource;
import maru.core.model.CoreModel;
import maru.groundstation.MaruGroundstationResources;
import maru.groundstation.earth.Groundstation;
import maru.ui.propertypages.UiVisiblePropertyPage;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.orekit.bodies.GeodeticPoint;

public class GeodeticGroundstationPropertyPage extends UiVisiblePropertyPage
{
    private Text latitude;
    private Text longitude;
    private Text altitude;

    private String initialLatitude;
    private String initialLongitude;
    private String initialAltitude;

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
    public Groundstation getScenarioElement()
    {
        return (Groundstation) getUiElement().getUnderlyingElement();
    }

    @Override
    public boolean performOk()
    {
        if (!super.performOk()) {
            return false;
        }

        if (hasPositionChanged())
        {
            Groundstation element = getScenarioElement();
            GeodeticPoint newPosition = createNewPosition(element);

            CoreModel.getDefault().changeInitialCoordinate(element, newPosition, true);
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

        return container;
    }

    private void initDefaults()
    {
        Groundstation element = getScenarioElement();
        if (element == null) {
            return;
        }

        GeodeticPoint position = element.getGeodeticPosition();

        // angles are processes in radiant internally
        // convert them to degrees for user interface interactions

        initialLatitude = Double.toString(Math.toDegrees(position.getLatitude()));
        initialLongitude = Double.toString(Math.toDegrees(position.getLongitude()));
        initialAltitude = Double.toString(position.getAltitude());
    }

    private void initControls()
    {
        Groundstation element = getScenarioElement();
        if (element == null) {
            return;
        }

        latitude.setText(initialLatitude);
        longitude.setText(initialLongitude);
        altitude.setText(initialAltitude);
    }

    private boolean hasPositionChanged()
    {
        String newLatitude = latitude.getText();
        String newLongitude = longitude.getText();
        String newAltitude = altitude.getText();

        if (!newLatitude.equals(initialLatitude)   ||
            !newLongitude.equals(initialLongitude) ||
            !newAltitude.equals(initialAltitude))
        {
            return true;
        }
        else
        {
            // the user did not change the values in the text controls
            return false;
        }
    }

    private GeodeticPoint createNewPosition(Groundstation element)
    {
        // angles are displayed in degree in the user interface
        // convert them to radiant for internal use

        double lat = Math.toRadians(Double.parseDouble(latitude.getText()));
        double lon = Math.toRadians(Double.parseDouble(longitude.getText()));
        double alt = Double.parseDouble(altitude.getText());

        return new GeodeticPoint(lat, lon, alt);
    }
}
