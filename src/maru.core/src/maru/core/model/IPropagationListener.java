package maru.core.model;

public interface IPropagationListener
{
    void propagationChanged(ISpacecraft element, ICoordinate coordinate);
}
