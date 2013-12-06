package maru.ui.model;

public interface IUiProjectSelectionListener
{
    void activeProjectChanged(UiProject project, UiElement element);
    void activeElementChanged(UiProject project, UiElement element);
}
