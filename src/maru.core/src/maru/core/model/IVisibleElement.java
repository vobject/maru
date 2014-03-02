package maru.core.model;

import maru.core.model.resource.IMaruResource;

public interface IVisibleElement extends IScenarioElement
{
    VisibleElementColor getElementColor();
    IMaruResource getElementImage();
}
