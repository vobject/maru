package maru.core.model;

import maru.IMaruResource;
import maru.core.internal.model.ScenarioModelManager;
import maru.core.internal.model.ScenarioNature;
import maru.core.internal.model.Timepoint;
import maru.core.model.template.Propagator;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.graphics.RGB;

/**
 * Core model class.
 */
public class CoreModel
{
    private static CoreModel model;
    private static ScenarioModelManager modelManager = ScenarioModelManager.getDefault();

    public static CoreModel getDefault()
    {
        if (model == null) {
            model = new CoreModel();
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

    public void addScenarioModelListener(IScenarioModelListener listener)
    {
        modelManager.addModelListener(listener);
    }

    public void removeScenarioModelListener(IScenarioModelListener listener)
    {
        modelManager.removeModelListener(listener);
    }

    public boolean hasProject(IProject project)
    {
        return modelManager.hasProject(project);
    }

    public IScenarioProject getScenarioProject(IProject project)
    {
        if (!hasProject(project)) {
            IScenarioProject scenarioProject = modelManager.createProject(project);
            modelManager.notifyScenarioAdded(scenarioProject);
        }
        return modelManager.getProject(project);
    }

    public IScenarioProject createScenarioProject(final IProject project,
                                                  final String comment,
                                                  final ICentralBody centralBody,
                                                  final long startTime,
                                                  final long stopTime) throws CoreException
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

                // create scenario storage file and load populate the scenario model
                IScenarioProject scenarioProject =
                    modelManager.createProject(project,
                                               startTimepoint,
                                               stopTimepoint,
                                               comment,
                                               centralBody);

                // save all changes to disk and notify all listeners
                modelManager.notifyScenarioCreated(scenarioProject);
            }
        }, null);

        return getScenarioProject(project);
    }

    public void removeProject(IProject project, boolean update)
    {
        modelManager.removeProject(project, update);
    }

    public void renameElement(IScenarioElement element, String name, boolean update)
    {
        modelManager.renameElement(element, name, update);
    }

    public void removeElement(IScenarioElement element, boolean update)
    {
        modelManager.removeElement(element, update);
    }

    public void commentElement(IScenarioElement element, String comment, boolean update)
    {
        modelManager.commentElement(element, comment, update);
    }

    public void addTimepoint(IScenarioProject project, long time, boolean update)
    {
        modelManager.addTimepoint(project, time, update);
    }

    public void removeTimepoint(ITimepoint timepoint, boolean update)
    {
        modelManager.removeTimepoint(timepoint, update);
    }

    public void changeTimepoint(ITimepoint timepoint, long time, boolean update)
    {
        modelManager.changeTimepoint(timepoint, time, update);
    }

    public void changePropagatablesCentralBody(IScenarioProject project, boolean update)
    {
        modelManager.changePropagatablesCentralBody(project, update);
    }

    public void changePropagatablesTime(IScenarioProject project, long time, boolean update)
    {
        modelManager.changePropagatablesTime(project, time, update);
    }

    public void addGroundstation(IScenarioProject project, IGroundstation groundstation, boolean update)
    {
        modelManager.addGroundstation(project, groundstation, update);
    }

    public void addSpacecraft(IScenarioProject project, ISpacecraft spacecraft, boolean update)
    {
        modelManager.addSpacecraft(project, spacecraft, update);
    }

    public void setElementColor(IPropagatable element, RGB color, boolean update)
    {
        modelManager.setElementColor(element, color, update);
    }

    public void setElementGraphics2D(IScenarioElement element, IMaruResource graphic2d, boolean update)
    {
        modelManager.setElementGraphics2D(element, graphic2d, update);
    }

    public void changeCentralBodyGM(ICentralBody element, double gm, boolean update)
    {
        modelManager.changeCentralBodyGM(element, gm, update);
    }

    public void changeCentralBodyEquatorialRadius(ICentralBody element, double radius, boolean update)
    {
        modelManager.changeCentralBodyEquatorialRadius(element, radius, update);
    }

    public void changeCentralBodyMeanRadius(ICentralBody element, double radius, boolean update)
    {
        modelManager.changeCentralBodyMeanRadius(element, radius, update);
    }

    public void changeCentralBodyPolarRadius(ICentralBody element, double radius, boolean update)
    {
        modelManager.changeCentralBodyPolarRadius(element, radius, update);
    }

    public void changeCentralBodyFlattening(ICentralBody element, double flattening, boolean update)
    {
        modelManager.changeCentralBodyFlattening(element, flattening, update);
    }

    public void addTimeProvider(IScenarioProject project, ITimeProvider provider)
    {
        modelManager.addTimeProvider(project, provider);
    }

    public void removeTimeProvider(IScenarioProject project, ITimeProvider provider)
    {
        modelManager.removeTimeProvider(project, provider);
    }

    public void addTimeProvider(IPropagatable element, ITimeProvider provider)
    {
        modelManager.addTimeProvider(element, provider);
    }

    public void removeTimeProvider(IPropagatable element, ITimeProvider provider)
    {
        modelManager.removeTimeProvider(element, provider);
    }

    public void addPropagationListener(IScenarioProject project, IPropagationListener listener)
    {
        modelManager.addPropagationListener(project, listener);
    }

    public void removePropagationListener(IScenarioProject project, IPropagationListener listener)
    {
        modelManager.removePropagationListener(project, listener);
    }

    public void addPropagationListener(IPropagatable element, IPropagationListener listener)
    {
        modelManager.addPropagationListener(element, listener);
    }

    public void removePropagationListener(IPropagatable element, IPropagationListener listener)
    {
        modelManager.removePropagationListener(element, listener);
    }

    public void setPropagator(IPropagatable element, Propagator propagator)
    {
        modelManager.setPropagator(element, propagator);
    }

    public void notifyScenarioAdded(IScenarioProject project)
    {
        modelManager.notifyScenarioAdded(project);
    }

    public void notifyScenarioRemoved(IScenarioProject project)
    {
        modelManager.notifyScenarioRemoved(project);
    }

    public void notifyElementAdded(IScenarioElement element)
    {
        modelManager.notifyElementAdded(element);
    }

    public void notifyElementRemoved(IScenarioElement element)
    {
        modelManager.notifyElementRemoved(element);
    }
}
