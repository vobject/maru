package maru.core.model;

import java.io.Serializable;

import maru.IMaruResource;

public interface IScenarioElement extends Comparable<IScenarioElement>,
                                          IPropertyProvider,
                                          Serializable
{
    String getElementName();
    String getElementComment();

    IMaruResource getElementGraphic2D();

    IScenarioElement getParent();
    IScenarioProject getScenarioProject();
}
