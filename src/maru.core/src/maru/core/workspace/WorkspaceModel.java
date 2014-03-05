package maru.core.workspace;

import java.util.ArrayList;

import maru.core.model.CoreModel;
import maru.core.model.ICentralBody;
import maru.core.model.IScenarioProject;
import maru.core.model.internal.IScenarioModel;
import maru.core.model.internal.Timepoint;
import maru.core.workspace.internal.ScenarioNature;
import maru.core.workspace.internal.ScenarioProjectStorage;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.orekit.time.AbsoluteDate;

public final class WorkspaceModel implements IResourceChangeListener
{
    private static WorkspaceModel workspaceModel;

    private final CoreModel coreModel;

    /**
     * Remember the project mentioned by the PRE_DELETE workspace message.
     * As soon as the post-delete message arrives, they are removed from
     * the data model and the scenario model listener notified.
     */
    private final ArrayList<IProject> projectsToBeDeleted = new ArrayList<>();

    public static WorkspaceModel getDefault()
    {
        if (workspaceModel == null) {
            workspaceModel = new WorkspaceModel(CoreModel.getDefault());
        }
        return workspaceModel;
    }

    private WorkspaceModel(CoreModel coreModel)
    {
        this.coreModel = coreModel;
    }

    public void startup()
    {
        coreModel.startup();

        ResourcesPlugin.getWorkspace().addResourceChangeListener(this, IResourceChangeEvent.PRE_DELETE |
                                                                       IResourceChangeEvent.POST_CHANGE);
    }

    public void shutdown()
    {
        ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);

        coreModel.shutdown();
    }

    public static IWorkspace getWorkspace()
    {
        return ResourcesPlugin.getWorkspace();
    }

    public CoreModel getCoreModel()
    {
        return coreModel;
    }

    public boolean hasProject(IProject project)
    {
        return coreModel.hasScenario(project.getName());
    }

    public IScenarioProject getProject(IProject project)
    {
        if (!hasProject(project)) {
            loadProject(project, true);
        }
        return coreModel.getScenario(project.getName());
    }

    public IProject getProject(IScenarioProject project)
    {
        // casting the IScenarioProjectStorage reference from CoreModel to
        // ScenarioProjectStorage to get to the eclipse-workspace-specific
        // IProject reference. not pretty, but it works well enough for now
        // since there is only this IScenarioProjectStorage implementation.
        return ((ScenarioProjectStorage) coreModel.getStorage(project)).getProject();
    }

    public IScenarioProject createProject(final IProject project,
                                          final String comment,
                                          final ICentralBody centralBody,
                                          final AbsoluteDate startTime,
                                          final AbsoluteDate stopTime) throws CoreException
    {
        getWorkspace().run(new IWorkspaceRunnable()
        {
            @Override
            public void run(IProgressMonitor monitor) throws CoreException
            {
                if (!project.exists()) {
                    project.create(null);
                }
                project.open(IResource.BACKGROUND_REFRESH, null);

                // get a project description (.project) which will hold the nature
                IProjectDescription description = project.getWorkspace().newProjectDescription(project.getName());
                ScenarioNature.addNature(description);
                project.setDescription(description, null);

                Timepoint startTimepoint = new Timepoint("Start", startTime);
                Timepoint stopTimepoint = new Timepoint("Stop", stopTime);

                // create scenario storage file and load the scenario model
                createProject(project, startTimepoint, stopTimepoint,
                              comment, centralBody, true);
            }
        }, null);

        return getProject(project);
    }

    public void createProject(IProject project, Timepoint start, Timepoint stop,
                              String comment, ICentralBody centralBody, boolean update)
    {
        IScenarioModel scenarioModel = coreModel.getScenarioModel();
        ScenarioProjectStorage storage = new ScenarioProjectStorage(
            scenarioModel, project, start, stop, comment, centralBody
        );
        IScenarioProject scenario = storage.getScenarioProject();

        scenarioModel.addScenarioProject(scenario);
        coreModel.createScenario(scenario, storage, update);
    }

    public void loadProject(IProject project, boolean update)
    {
        IScenarioModel scenarioModel = coreModel.getScenarioModel();
        ScenarioProjectStorage scenarioStorage = new ScenarioProjectStorage(scenarioModel, project);
        IScenarioProject scenario = scenarioStorage.getScenarioProject();

        scenarioModel.addScenarioProject(scenario);
        coreModel.addScenario(scenario, scenarioStorage, update);
    }

    public void removeProject(IProject project, boolean update)
    {
        IScenarioModel scenarioModel = coreModel.getScenarioModel();
        IScenarioProject scenario = scenarioModel.getScenarioProject(project.getName());

        // throw away the reference to the storage file
        coreModel.removeScenario(scenario, update);

        // remove the project from the model
        scenarioModel.removeScenarioProject(scenario);
    }

    @Override
    public void resourceChanged(IResourceChangeEvent event)
    {
        handlePreDeleteProjectEvent(event);
        handlePostDeleteProjectEvent(event);
    }

    private void handlePreDeleteProjectEvent(IResourceChangeEvent event)
    {
        if (event.getType() != IResourceChangeEvent.PRE_DELETE) {
            return;
        }

        if (event.getResource().getType() != IResource.PROJECT) {
            return;
        }

        // eclipse is telling us that a project is about to be deleted.
        // we save a reference to the affected project and wait for the
        // post-delete message which does not come with an IProject reference.
        projectsToBeDeleted.add((IProject) event.getResource());
    }

    private void handlePostDeleteProjectEvent(IResourceChangeEvent event)
    {
        if (event.getType() != IResourceChangeEvent.POST_CHANGE) {
            return;
        }

        IResourceDelta delta = event.getDelta();
        if (delta == null) {
            return;
        }

        int kind = delta.getKind();
        IResourceDelta[] children = delta.getAffectedChildren(IResourceDelta.REMOVED);

        if ((kind != IResourceDelta.CHANGED) || (children.length == 0)) {
            return;
        }

        for (IProject project : projectsToBeDeleted)
        {
            removeProject(project, true);
        }
        projectsToBeDeleted.clear();
    }
}
