package maru.core.model;

import maru.IMaruResource;
import maru.core.internal.model.ScenarioModelManager;
import maru.core.internal.model.ScenarioNature;
import maru.core.internal.model.Timepoint;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.graphics.RGB;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.time.AbsoluteDate;

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
            modelManager.loadProject(project, true);
        }
        return modelManager.getProject(project);
    }

    public IScenarioProject createScenarioProject(final IProject project,
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
                modelManager.createProject(project,
                                           startTimepoint,
                                           stopTimepoint,
                                           comment,
                                           centralBody,
                                           true);
            }
        }, null);

        return getScenarioProject(project);
    }

    public void removeProject(IProject project, boolean update)
    {
        modelManager.removeProject(project, update);
    }

    public void removeElement(IScenarioElement element, boolean update)
    {
        modelManager.removeElement(element, update);
    }

    public void changeElementName(IScenarioElement element, String name, boolean update)
    {
        modelManager.changeElementName(element, name, update);
    }

    public void changeElementComment(IScenarioElement element, String comment, boolean update)
    {
        modelManager.changeElementComment(element, comment, update);
    }

    public void addTimepoint(IScenarioProject project, AbsoluteDate date, boolean update)
    {
        modelManager.addTimepoint(project, date, update);
    }

    public void removeTimepoint(ITimepoint timepoint, boolean update)
    {
        modelManager.removeTimepoint(timepoint, update);
    }

    public void changeTimepoint(ITimepoint timepoint, AbsoluteDate date, boolean update)
    {
        modelManager.changeTimepoint(timepoint, date, update);
    }

    /**
     * Change the current time for all propagatable element in a scenario.
     * <br><br>
     * This is not the same as changing the current time of the scenario. The
     * scenario's current time will not be affected by this message, but only
     * the propagatable elements such as ground stations and spacecrafts.
     * <br><br>
     * This message is usually used when real-time mode is active where the
     * scenario may not be modified but its propagatable elements must appear
     * as if its current time were.
     */
    public void changeScenarioElementsTime(IScenarioProject project, AbsoluteDate date, boolean update)
    {
        modelManager.changeScenarioElementsTime(project, date, update);
    }

    public void addGroundstation(IScenarioProject project, IGroundstation groundstation, boolean update)
    {
        modelManager.addGroundstation(project, groundstation, update);
    }

    public void addSpacecraft(IScenarioProject project, ISpacecraft spacecraft, boolean update)
    {
        modelManager.addSpacecraft(project, spacecraft, update);
    }

    public void changeColor(IVisibleElement element, RGB color, boolean update)
    {
        modelManager.changeElementColor(element, color, update);
    }

    public void changeImage(IVisibleElement element, IMaruResource image, boolean update)
    {
        modelManager.changeElementImage(element, image, update);
    }

    public void changeInitialCoordinate(IGroundstation element, GeodeticPoint position, double elevation, boolean update)
    {
        modelManager.changeInitialCoordinate(element, position, elevation, update);
    }

    public void changeInitialCoordinate(ISpacecraft element, ICoordinate coordinate, boolean update)
    {
        modelManager.changeInitialCoordinate(element, coordinate, update);
    }

    public void changeCentralBodyImage(ICentralBody element, IMaruResource image, boolean update)
    {
        modelManager.changeCentralBodyImage(element, image, update);
    }

    public void changeCentralBodyGM(ICentralBody element, double gm, boolean update)
    {
        modelManager.changeCentralBodyGM(element, gm, update);
    }

    public void changeCentralBodyEquatorialRadius(ICentralBody element, double radius, boolean update)
    {
        modelManager.changeCentralBodyEquatorialRadius(element, radius, update);
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

    public void addTimeProvider(IGroundstation element, ITimeProvider provider)
    {
        modelManager.addTimeProvider(element, provider);
    }

    public void removeTimeProvider(IGroundstation element, ITimeProvider provider)
    {
        modelManager.removeTimeProvider(element, provider);
    }

    public void addTimeProvider(ISpacecraft element, ITimeProvider provider)
    {
        modelManager.addTimeProvider(element, provider);
    }

    public void removeTimeProvider(ISpacecraft element, ITimeProvider provider)
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

    public void addPropagationListener(ISpacecraft element, IPropagationListener listener)
    {
        modelManager.addPropagationListener(element, listener);
    }

    public void removePropagationListener(ISpacecraft element, IPropagationListener listener)
    {
        modelManager.removePropagationListener(element, listener);
    }

    public void changePropagator(ISpacecraft element, AbstractPropagator propagator)
    {
        modelManager.changePropagator(element, propagator);
    }
}
