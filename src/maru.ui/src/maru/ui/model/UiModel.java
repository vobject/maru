package maru.ui.model;

import maru.core.model.IScenarioElement;
import maru.core.model.IScenarioProject;
import maru.ui.internal.model.UiProjectModelManager;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;

public class UiModel
{
    private static UiModel model;
    private static UiProjectModelManager modelManager;

    public static UiModel getDefault()
    {
        if (model == null) {
            model = new UiModel();
            modelManager = UiProjectModelManager.getDefault();
        }
        return model;
    }

    public static IWorkspace getWorkspace()
    {
        return ResourcesPlugin.getWorkspace();
    }

    public void startup()
    {
        modelManager.startup();
    }

    public void shutdown()
    {
        modelManager.shutdown();
    }

    public UiProject getUiProject(IScenarioProject project)
    {
        return modelManager.getProject(project);
    }

    public UiElement getUiElement(IScenarioElement element)
    {
        return modelManager.getElement(element);
    }

    public UiProject getCurrentUiProject()
    {
        return modelManager.getCurrentProject();
    }

    public UiElement getCurrentUiElement()
    {
        return modelManager.getCurrentElement();
    }

    public void addUiProjectModelListener(IUiProjectModelListener listener)
    {
        modelManager.addProjectModelListener(listener);
    }

    public void removeUiProjectModelListener(IUiProjectModelListener listener)
    {
        modelManager.removeProjectModelListener(listener);
    }

    public void addUiTimelineSettingsListener(IUiTimelineSettingsListener listener)
    {
        modelManager.addTimelineSettingsListener(listener);
    }

    public void removeUiTimelineSettingsListener(IUiTimelineSettingsListener listener)
    {
        modelManager.removeTimelineSettingsListener(listener);
    }

    public void addUiProjectSelectionListener(IUiProjectSelectionListener listener)
    {
        modelManager.addProjectSelectionListener(listener);
    }

    public void removeUiProjectSelectionListener(IUiProjectSelectionListener listener)
    {
        modelManager.removeProjectSelectionListener(listener);
    }
}
