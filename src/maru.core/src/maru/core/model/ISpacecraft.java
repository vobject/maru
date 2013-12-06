package maru.core.model;

public interface ISpacecraft extends IScenarioElement, IPropagatable
{
    boolean inUmbra();
    boolean inUmbraOrPenumbra();

    boolean inUmbra(ICoordinate coordinate);
    boolean inUmbraOrPenumbra(ICoordinate coordinate);
}
