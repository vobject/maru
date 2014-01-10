package maru.core.model;

public interface IScenarioModelListener
{
    void scenarioCreated(IScenarioProject project);

    // indicates that a new scenario was either created (using the wizard)
    // or just loaded from a file. this is the first notification that may
    // be sent out mentioning the project.
    // the newly added project must be fully functional, e.g. it must have
    // a valid central body and a beginning and end timepoint,
    void scenarioAdded(IScenarioProject project);

    // the given scenario is about to be removed from the workspace.
    // it is still intact and fully operational but will be removed after
    // all listeners have been notified. listeners are advised to remove
    // pending references to the project and all its elements.
    void scenarioRemoved(IScenarioProject project);

    // the given element was added to a scenario project. listeners
    // should handle the creation of the new element. this is also
    // a good opportunity to register listeners of providers for the element.
    void elementAdded(IScenarioElement element);

    // the given element was removed to a scenario project. listeners should
    // remove all references to the element.
    void elementRemoved(IScenarioElement element);

    void elementRenamed(IScenarioElement element);
    void elementCommented(IScenarioElement element);

    void elementColorChanged(IPropagatable element);
    void elementImageChanged(IPropagatable element);
    void elementInitialCoordinateChanged(IPropagatable element);

    void centralbodyImageChanged(ICentralBody element);
    void centralbodyGmChanged(ICentralBody element);
    void centralbodyEquatorialRadiusChanged(ICentralBody element);
    void centralbodyFlatteningChanged(ICentralBody element);

    void propagatablesTimeChanged(IScenarioProject element);

    void timepointStartChanged(ITimepoint element);
    void timepointStopChanged(ITimepoint element);
    void timepointCurrentChanged(ITimepoint element);

    void timepointAdded(ITimepoint element);
    void timepointRemoved(ITimepoint element);
    void timepointChanged(ITimepoint element);
}
