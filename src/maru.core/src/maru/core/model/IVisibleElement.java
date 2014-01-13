package maru.core.model;

import maru.IMaruResource;

import org.eclipse.swt.graphics.RGB;

public interface IVisibleElement extends IScenarioElement
{
    RGB getElementColor();
    IMaruResource getElementImage();
}
