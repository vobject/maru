package maru.core.model;

import org.eclipse.swt.graphics.RGB;

public interface IPropagatable extends IScenarioElement, ITimeListener, IPropagationListener, ICentralBodyListener
{
    RGB getElementColor();

    ICentralBody getCentralBody();
    IPropagator getPropagator();

    ICoordinate getInitialCoordinate();
    ICoordinate getCurrentCoordinate();
}
