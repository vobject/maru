package maru.ui.model;

public interface IUiProjectModelListener
{
    void projectAdded(UiProject project);
    void projectChanged(UiProject project);
    void projectRemoved(UiProject project);
}
