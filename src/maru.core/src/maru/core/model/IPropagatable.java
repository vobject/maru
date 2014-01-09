package maru.core.model;

import maru.IMaruResource;

import org.eclipse.swt.graphics.RGB;

public interface IPropagatable extends IScenarioElement, ITimeListener, IPropagationListener, ICentralBodyListener
{
    RGB getElementColor();
    IMaruResource getElementImage();

    ICentralBody getCentralBody();
    IPropagator getPropagator();

    ICoordinate getInitialCoordinate();
    ICoordinate getCurrentCoordinate();
}
