package maru.spacecraft.propertypages;

import maru.core.model.CoreModel;
import maru.core.model.IScenarioElement;
import maru.core.utils.TimeUtil;
import maru.spacecraft.ckesatellite.InitialKeplerCoordinate;
import maru.spacecraft.ckesatellite.KeplerSatellite;
import maru.ui.propertypages.UiPropertyPage;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.orekit.frames.Frame;
import org.orekit.orbits.KeplerianOrbit;
import org.orekit.orbits.PositionAngle;
import org.orekit.time.AbsoluteDate;

public class KeplerSatellitePropertyPage extends UiPropertyPage
{
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

    @Override
    public KeplerSatellite getScenarioElement()
    {
        IScenarioElement element = super.getScenarioElement();
        if (element instanceof KeplerSatellite) {
            return (KeplerSatellite) element;
        } else {
            return null;
        }
    }

    @Override
    public boolean performOk()
    {
        if (!super.performOk()) {
            return false;
        }

        if (hasInitialCoordinateChanged())
        {
            KeplerSatellite element = getScenarioElement();
            InitialKeplerCoordinate newCoordinate = createNewCoordinate(element);
            CoreModel.getDefault().changeInitialCoordinate(element, newCoordinate, true);
        }

        initDefaults();
        return true;
    }

    @Override
    protected Composite createContents(Composite parent)
    {
        if (getScenarioElement() == null) {
            // this case should not happen if the property page filter
            // in the plugin.xml is configured correctly
            new Label(parent, SWT.NONE).setText("The selected element is no KeplerSatellite.");
            return null;
        }

        Composite container = createControls(parent);

        initDefaults();
        initControls();

        return container;
    }

    private Composite createControls(Composite parent)
    {
        GridLayout layout = new GridLayout(2, false);
        layout.marginWidth = 0;
        layout.marginHeight = 0;

        GridData data = new GridData();
        data.verticalAlignment = SWT.FILL;
        data.grabExcessVerticalSpace = true;
        data.horizontalSpan = 2;

        Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(layout);
        container.setLayoutData(data);

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
        anomalyType.setItems(new String[] { PositionAngle.MEAN.toString(),
                                            PositionAngle.TRUE.toString(),
                                            PositionAngle.ECCENTRIC.toString() });
        anomalyType.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        new Label(container, SWT.NONE).setText("Date:");
        date = new Text(container, SWT.BORDER);
        date.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        date.setEnabled(false);

        new Label(container, SWT.NONE).setText("Frame:");
        frame = new Text(container, SWT.BORDER);
        frame.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        frame.setEnabled(false);

        new Label(container, SWT.NONE).setText("Central attraction coefficient  (m\u00b3/s\u00b2):");
        attractionCoefficient = new Text(container, SWT.BORDER);
        attractionCoefficient.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        attractionCoefficient.setEnabled(false);

        return container;
    }

    private void initDefaults()
    {
        KeplerSatellite element = getScenarioElement();
        if (element == null) {
            return;
        }

        InitialKeplerCoordinate coordinate = element.getInitialCoordinate();
        KeplerianOrbit orbit = coordinate.getInitialOrbit();

        // angles are processes in radiant internally
        // convert them to degrees for user interface interactions

        initialSemimajorAxis = Double.toString(orbit.getA() / 1000.0); // convert to km
        initialEccentricity = Double.toString(orbit.getE());
        initialInclination = Double.toString(Math.toDegrees(orbit.getI()));
        initialArgumentOfPerigee = Double.toString(Math.toDegrees(orbit.getPerigeeArgument()));
        initialRaan = Double.toString(Math.toDegrees(orbit.getRightAscensionOfAscendingNode()));
        initialAnomaly = Double.toString(Math.toDegrees(orbit.getAnomaly(PositionAngle.MEAN)));
        initialAnomalyType = PositionAngle.MEAN.toString();
    }

    private void initControls()
    {
        KeplerSatellite element = getScenarioElement();
        if (element == null) {
            return;
        }

        InitialKeplerCoordinate coordinate = element.getInitialCoordinate();

        semimajorAxis.setText(initialSemimajorAxis);
        eccentricity.setText(initialEccentricity);
        inclination.setText(initialInclination);
        argumentOfPerigee.setText(initialArgumentOfPerigee);
        raan.setText(initialRaan);
        anomaly.setText(initialAnomaly);
        anomalyType.select(0); // select PositionAngle.MEAN
        date.setText(TimeUtil.asISO8601(coordinate.getTime()));
        frame.setText(coordinate.getFrame().toString());
        attractionCoefficient.setText(Double.toString(element.getCentralBody().getGM()));
    }

    private boolean hasInitialCoordinateChanged()
    {
        String newSemimajorAxis = semimajorAxis.getText();
        String newEccentricity = eccentricity.getText();
        String newInclination = inclination.getText();
        String newArgumentOfPerigee = argumentOfPerigee.getText();
        String newRaan = raan.getText();
        String newAnomaly = anomaly.getText();
        String newAnomalyType = anomalyType.getText();

        if (!newSemimajorAxis.equals(initialSemimajorAxis)   ||
            !newEccentricity.equals(initialEccentricity) ||
            !newInclination.equals(initialInclination)   ||
            !newArgumentOfPerigee.equals(initialArgumentOfPerigee) ||
            !newRaan.equals(initialRaan)   ||
            !newAnomaly.equals(initialAnomaly) ||
            !newAnomalyType.equals(initialAnomalyType))
        {
            return true;
        }
        else
        {
            // the user did not change the values in the text controls
            return false;
        }
    }

    private InitialKeplerCoordinate createNewCoordinate(KeplerSatellite element)
    {
        InitialKeplerCoordinate coordinate = element.getInitialCoordinate();
        KeplerianOrbit orbit = coordinate.getInitialOrbit();

        // angles are displayed in degree in the user interface
        // convert them to radiant for internal use

        double a = Double.parseDouble(semimajorAxis.getText()) * 1000.0; // convert to meters
        double e = Double.parseDouble(eccentricity.getText());
        double i = Math.toRadians(Double.parseDouble(inclination.getText()));
        double pa = Math.toRadians(Double.parseDouble(argumentOfPerigee.getText()));
        double raan_ = Math.toRadians(Double.parseDouble(raan.getText()));
        double anomaly_ = Math.toRadians(Double.parseDouble(anomaly.getText()));
        PositionAngle type = PositionAngle.valueOf(anomalyType.getText());
        Frame frame = orbit.getFrame();
        AbsoluteDate date = orbit.getDate();
        double mu = orbit.getMu();

        KeplerianOrbit initialOrbit =
            new KeplerianOrbit(a, e, i, pa, raan_, anomaly_, type, frame, date, mu);

        return new InitialKeplerCoordinate(initialOrbit);
    }
}
