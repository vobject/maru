package maru.core.model;

public interface IPropagationListener
{
    void propagationChanged(IPropagatable element, ICoordinate position);
}
