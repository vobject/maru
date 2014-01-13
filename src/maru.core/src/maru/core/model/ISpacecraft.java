package maru.core.model;

public interface ISpacecraft extends IScenarioElement, IVisibleElement, ITimeListener, IPropagationListener, ICentralBodyListener
{
    ICentralBody getCentralBody();
    IPropagator getPropagator();

    ICoordinate getInitialCoordinate();
    ICoordinate getCurrentCoordinate();

    boolean inUmbra();
    boolean inUmbraOrPenumbra();

    boolean inUmbra(ICoordinate coordinate);
    boolean inUmbraOrPenumbra(ICoordinate coordinate);
}
