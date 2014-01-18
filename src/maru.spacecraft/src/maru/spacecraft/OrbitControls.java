package maru.spacecraft;

import org.orekit.orbits.Orbit;

public abstract class OrbitControls
{
    public abstract boolean isModified();
    public abstract void refreshDefaults(Orbit orbit);

    public abstract Orbit getOrbit();

//    public abstract Composite createControls(Composite parent);
//
//    protected abstract void initDefaults();
//    protected abstract void initDefaults(Orbit orbit);

//    private void initControls()
//    {
//        semimajorAxis.setText(initialSemimajorAxis);
//        eccentricity.setText(initialEccentricity);
//        inclination.setText(initialInclination);
//        argumentOfPerigee.setText(initialArgumentOfPerigee);
//        raan.setText(initialRaan);
//        anomaly.setText(initialAnomaly);
//        anomalyType.select(0); // select PositionAngle.MEAN
//        date.setText(initialDate);
//        frame.setText(initialFrame);
//        attractionCoefficient.setText(initialAttractionCoefficient);
//    }
//
//    private void initControls(KeplerianOrbit orbit)
//    {
//        semimajorAxis.setText(initialSemimajorAxis);
//        eccentricity.setText(initialEccentricity);
//        inclination.setText(initialInclination);
//        argumentOfPerigee.setText(initialArgumentOfPerigee);
//        raan.setText(initialRaan);
//        anomaly.setText(initialAnomaly);
//        anomalyType.select(0); // select PositionAngle.MEAN
//        date.setText(initialDate);
//        frame.setText(initialFrame);
//        attractionCoefficient.setText(initialAttractionCoefficient);
//    }
//
//    private boolean hasCoordinateChanged()
//    {
//        String newSemimajorAxis = semimajorAxis.getText();
//        String newEccentricity = eccentricity.getText();
//        String newInclination = inclination.getText();
//        String newArgumentOfPerigee = argumentOfPerigee.getText();
//        String newRaan = raan.getText();
//        String newAnomaly = anomaly.getText();
//        String newAnomalyType = anomalyType.getText();
//        String newDate = date.getText();
//        String newFrame = frame.getText();
//        String newAttractionCoefficient = attractionCoefficient.getText();
//
//        if (!newSemimajorAxis.equals(initialSemimajorAxis) ||
//            !newEccentricity.equals(initialEccentricity) ||
//            !newInclination.equals(initialInclination) ||
//            !newArgumentOfPerigee.equals(initialArgumentOfPerigee) ||
//            !newRaan.equals(initialRaan) ||
//            !newAnomaly.equals(initialAnomaly) ||
//            !newAnomalyType.equals(initialAnomalyType) ||
//            !newDate.equals(initialDate) ||
//            !newFrame.equals(initialFrame) ||
//            !newAttractionCoefficient.equals(initialAttractionCoefficient))
//        {
//            return true;
//        }
//        else
//        {
//            // the user did not change the values in the text controls
//            return false;
//        }
//    }
//
//    private KeplerianOrbit createKeplerianOrbit()
//    {
//        double a = getSemimajorAxis();
//        double e = getEccentricity();
//        double i = getInclination();
//        double pa = getArgumentOfPerigee();
//        double raan = getRaan();
//        double anomaly = getAnomaly();
//        PositionAngle type = getAnomalyType();
//        Frame frame = getFrame();
//        AbsoluteDate date = getDate();
//        double mu = getCentralAttractionCoefficient();
//
//        return new KeplerianOrbit(a, e, i, pa, raan, anomaly, type, frame, date, mu);
//    }
}
