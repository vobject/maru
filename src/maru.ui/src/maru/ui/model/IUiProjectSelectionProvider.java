package maru.ui.model;

public interface IUiProjectSelectionProvider
{
    void addProjectSelectionListener(IUiProjectSelectionListener listener);
    void removeProjectSelectionListener(IUiProjectSelectionListener listener);
}
