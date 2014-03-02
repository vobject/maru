package maru.core.workspace.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import maru.core.MaruCorePlugin;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

public class ScenarioNature implements IProjectNature
{
    public static final String NATURE_ID = MaruCorePlugin.PLUGIN_ID + ".ScenarioNature"; //$NON-NLS-1$

    private IProject project;

    @Override
    public void configure() throws CoreException
    {
        // should only happen on project creation. setProject() will take
        // care about important initialization elements.
    }

    @Override
    public void deconfigure() throws CoreException
    {
        // should never happen since the only thing that is supported are
        // projects using this nature.
    }

    @Override
    public IProject getProject()
    {
        return project;
    }

    @Override
    public void setProject(IProject project)
    {
        this.project = project;
    }

    public static ScenarioNature getScenarioNature(IProject project)
    {
        if (project == null) {
            return null;
        }

        try
        {
            IProjectNature nature = project.getNature(NATURE_ID);
            if (nature instanceof ScenarioNature)
            {
                return (ScenarioNature) nature;
            }
        }
        catch (CoreException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static void addNature(IProjectDescription description)
    {
        String[] oldNatures = description.getNatureIds();
        for (String oldNature : oldNatures) {
            if (ScenarioNature.NATURE_ID.equals(oldNature)) {
                return; // do not add nature multiple times
            }
        }
        String[] newNatures = new String[oldNatures.length + 1];

        // set the nature for the new project
        System.arraycopy(oldNatures, 0, newNatures, 0, oldNatures.length);
        newNatures[oldNatures.length] = ScenarioNature.NATURE_ID;
        description.setNatureIds(newNatures);
    }

    public static void removeNature(IProjectDescription description)
    {
        String[] oldNatures = description.getNatureIds();
        List<String> newNatures = new ArrayList<>(Arrays.asList(oldNatures));
        newNatures.remove(ScenarioNature.NATURE_ID);
        description.setNatureIds(newNatures.toArray(new String[newNatures.size()]));
    }
}
