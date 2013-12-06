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

    // TODO: it is still unclear how to communicate the swapping of an
    // element. e.g. if the central body is set during planning (not creation)
    // what happens is that the old central body is removed from the model
    // and a new one is added. currently the central body is not supposed
    // to be swapped at planning time, so there is no notification at all
    // for the described event.

    void elementRenamed(IScenarioElement element);
    void elementCommented(IScenarioElement element);
    void elementColored(IPropagatable element);
    void elementGraphic2DChanged(IPropagatable element);

    void propagatablesTimeChanged(IScenarioProject element);

    void timepointStartChanged(ITimepoint element);
    void timepointStopChanged(ITimepoint element);
    void timepointCurrentChanged(ITimepoint element);

    void timepointAdded(ITimepoint element);
    void timepointRemoved(ITimepoint element);
    void timepointChanged(ITimepoint element);

}
