package maru.spacecraft.wizards;

import java.text.ParseException;

import maru.core.model.IScenarioProject;
import maru.core.utils.TimeUtil;
import maru.spacecraft.MaruSpacecraftResources;
import maru.ui.wizards.ScenarioElementWizard;
import maru.ui.wizards.ScenarioElementWizardNamingPage;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.orekit.frames.Frame;
import org.orekit.frames.FramesFactory;
import org.orekit.orbits.PositionAngle;

public class KeplerSatelliteWizardPage extends ScenarioElementWizardNamingPage
{
    private static final String PAGE_NAME = "New Satellite";
    private static final String PAGE_TITLE = "Satellite";
    private static final String PAGE_DESCRIPTION = "Create a new Satellite.";

    private Text semimajorAxisText;
    private Text eccentricityText;
    private Text inclinationText;
    private Text argumentOfPerigeeText;
    private Text raanText;
    private Text anomalyText;
    private Combo anomalyTypeCombo;
    private Text dateText;
    private Text frameText;
    private Text attractionCoefficientText;

    public KeplerSatelliteWizardPage(IScenarioProject project)
    {
        super(PAGE_NAME, PAGE_TITLE, PAGE_DESCRIPTION, project);
    }

    @Override
    public void createControl(Composite parent)
    {
        Composite container = new Composite(parent, SWT.NONE);
        GridLayout containerLayout = new GridLayout(2, false);
        container.setLayout(containerLayout);

        super.createControl(container);
        ScenarioElementWizard.createLine(container, containerLayout.numColumns);

        // default entries so dummy entries can be created quicker
        getNameControl().setText("New CKE Satellite");

        new Label(container, SWT.NONE).setText("Semimajor Axis (km):");
        semimajorAxisText = new Text(container, SWT.BORDER);
        semimajorAxisText.setText("6678.14");
        semimajorAxisText.addKeyListener(inputValidation);
        semimajorAxisText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        new Label(container, SWT.NONE).setText("Eccentricity:");
        eccentricityText = new Text(container, SWT.BORDER);
        eccentricityText.setText("0");
        eccentricityText.addKeyListener(inputValidation);
        eccentricityText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        new Label(container, SWT.NONE).setText("Inclination (deg):");
        inclinationText = new Text(container, SWT.BORDER);
        inclinationText.setText("28.5");
        inclinationText.addKeyListener(inputValidation);
        inclinationText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        new Label(container, SWT.NONE).setText("Argument of Perigee (deg):");
        argumentOfPerigeeText = new Text(container, SWT.BORDER);
        argumentOfPerigeeText.setText("0");
        argumentOfPerigeeText.addKeyListener(inputValidation);
        argumentOfPerigeeText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        new Label(container, SWT.NONE).setText("RAAN (deg):");
        raanText = new Text(container, SWT.BORDER);
        raanText.setText("0");
        raanText.addKeyListener(inputValidation);
        raanText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        new Label(container, SWT.NONE).setText("Anomaly (deg):");
        anomalyText = new Text(container, SWT.BORDER);
        anomalyText.setText("0");
        anomalyText.addKeyListener(inputValidation);
        anomalyText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        new Label(container, SWT.NONE).setText("Anomaly type:");
        anomalyTypeCombo = new Combo(container, SWT.BORDER | SWT.READ_ONLY);
        anomalyTypeCombo.setItems(new String[] { PositionAngle.MEAN.toString(),
                                     PositionAngle.TRUE.toString(),
                                     PositionAngle.ECCENTRIC.toString() });
        anomalyTypeCombo.addKeyListener(inputValidation);
        anomalyTypeCombo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        anomalyTypeCombo.select(0);

        new Label(container, SWT.NONE).setText("Date:");
        dateText = new Text(container, SWT.BORDER);
        if (getProject() != null) {
            dateText.setText(TimeUtil.asISO8601(getProject().getStartTime()));
        }
        dateText.addKeyListener(inputValidation);
        dateText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        dateText.setEnabled(false);

        new Label(container, SWT.NONE).setText("Frame:");
        frameText = new Text(container, SWT.BORDER);
        frameText.setText(getFrame().toString());
        frameText.addKeyListener(inputValidation);
        frameText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        frameText.setEnabled(false);

        new Label(container, SWT.NONE).setText("Central attraction coefficient  (m\u00b3/s\u00b2):");
        attractionCoefficientText = new Text(container, SWT.BORDER);
        attractionCoefficientText.setText(Double.toString(getCentralAttractionCoefficient()));
        attractionCoefficientText.addKeyListener(inputValidation);
        attractionCoefficientText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        attractionCoefficientText.setEnabled(false);

        setControl(container);
        setPageComplete(isInputValid());
    }

    @Override
    protected String[] getElementImages()
    {
        return new String[] {
            MaruSpacecraftResources.SPACECRAFT_DEFAULT_1.getName(),
            MaruSpacecraftResources.SPACECRAFT_DEFAULT_2.getName(),
            MaruSpacecraftResources.SPACECRAFT_DEFAULT_3.getName(),
            MaruSpacecraftResources.SPACECRAFT_ISS_1.getName(),
            MaruSpacecraftResources.SPACECRAFT_ISS_2.getName(),
            MaruSpacecraftResources.SPACECRAFT_ASTRONAUT_1.getName(),
            MaruSpacecraftResources.SPACECRAFT_ROCKET_1.getName(),
            MaruSpacecraftResources.SPACECRAFT_SHUTTLE_1.getName(),
            MaruSpacecraftResources.SPACECRAFT_SHUTTLE_2.getName(),
        };
    }

    @Override
    protected boolean isInputValid()
    {
        // let the parent class check its input first
        if (!super.isInputValid()) {
            return false;
        }

        // check if another satellite with the same name already exits!
        if (getProject().getSpacecraftContainer().hasChild(getElementName())) {
            setErrorMessage("An satellite with the same name already exists in the selected scenario.");
            return false;
        }

        try
        {
            Double.parseDouble(semimajorAxisText.getText());
            Double.parseDouble(eccentricityText.getText());
            Double.parseDouble(inclinationText.getText());
            Double.parseDouble(argumentOfPerigeeText.getText());
            Double.parseDouble(raanText.getText());
            Double.parseDouble(anomalyText.getText());
        }
        catch (NumberFormatException e)
        {
            return false;
        }

        try
        {
            TimeUtil.fromString(dateText.getText());
        }
        catch (ParseException e)
        {
            return false;
        }

        setErrorMessage(null);
        return true;
    }

    public double getSemimajorAxis()
    {
        // convert to meters. meters are used internally
        return (Double.parseDouble(semimajorAxisText.getText()) * 1000.0);
    }

    public double getEccentricity()
    {
        return Double.parseDouble(eccentricityText.getText());
    }

    public double getInclination()
    {
        return Math.toRadians(Double.parseDouble(inclinationText.getText()));
    }

    public double getArgumentOfPerigee()
    {
        return Math.toRadians(Double.parseDouble(argumentOfPerigeeText.getText()));
    }

    public double getRaan()
    {
        return Math.toRadians(Double.parseDouble(raanText.getText()));
    }

    public double getAnomaly()
    {
        return Math.toRadians(Double.parseDouble(anomalyText.getText()));
    }

    public PositionAngle getAnomalyType()
    {
        return PositionAngle.valueOf(anomalyTypeCombo.getText());
    }

    public long getTime()
    {
        try
        {
            return TimeUtil.fromString(dateText.getText()).getTime();
        }
        catch (ParseException e)
        {
            // should never happen because isInputValid() checks for exception.
            return -1;
        }
    }

    public Frame getFrame()
    {
        return FramesFactory.getEME2000();
    }

    public double getCentralAttractionCoefficient()
    {
        if (getProject() == null) {
            return 0;
        }
        return getProject().getCentralBody().getGM();
    }
}
