package maru.core.model;

import java.io.Serializable;

public interface IScenarioElement extends Comparable<IScenarioElement>,
                                          IPropertyProvider,
                                          Serializable
{
    String getElementName();
    String getElementComment();

    IScenarioElement getParent();
    IScenarioProject getScenarioProject();
}
