package maru.map.settings.uiproject;

import maru.ui.MaruUIPlugin;
import maru.ui.model.UiProject;

import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

public class UiProjectsSettings
{
    private final Preferences uiProjectsNode;

    public UiProjectsSettings(Preferences uiProjectsNode)
    {
        this.uiProjectsNode = uiProjectsNode;
    }

    public UiProjectSettings getProject(UiProject project)
    {
        String prjName = project.getName();
        Preferences prjNode = null;

        try
        {
            if (uiProjectsNode.nodeExists(prjName)) {
                prjNode = uiProjectsNode.node(prjName);
            }
        }
        catch (BackingStoreException e)
        {
            e.printStackTrace();
        }

        // it is not an error if prjNode is null
        return new UiProjectSettings(uiProjectsNode, prjNode, prjName);
    }

    public UiProjectSettings getCurrentProject()
    {
        String prjName = getCurrentUiProjectName();
        Preferences prjNode = uiProjectsNode.node(prjName);
        return new UiProjectSettings(uiProjectsNode, prjNode, prjName);
    }

    public void projectAdded(UiProject project)
    {
        UiProjectSettings prjSettings = getProject(project);

        if (!prjSettings.exists()) {
            // a new project was created. initialize it.
            prjSettings.initialize();
        } else {
            // the project was loaded from disk
        }
    }

    public void projectRemoved(UiProject project)
    {
        UiProjectSettings prjSettings = getProject(project);

        if (!prjSettings.exists()) {
            // the project settings do not exist
        } else {
            // remove the project settings because the project itself was removed
            prjSettings.remove();
        }
    }

    private String getCurrentUiProjectName()
    {
        return MaruUIPlugin.getDefault().getUiModel().getCurrentUiProject().getName();
    }
}
