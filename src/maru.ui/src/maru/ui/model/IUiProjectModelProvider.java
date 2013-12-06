package maru.ui.model;

public interface IUiProjectModelProvider
{
    void addProjectModelListener(IUiProjectModelListener listener);
    void removeProjectModelListener(IUiProjectModelListener listener);
}
