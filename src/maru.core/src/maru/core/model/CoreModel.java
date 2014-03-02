package maru.core.model;

import java.util.List;

import maru.core.model.internal.IScenarioModel;
import maru.core.model.internal.IScenarioProjectStorage;
import maru.core.model.internal.ScenarioModel;
import maru.core.model.internal.ScenarioModelManager;
import maru.core.model.resource.IMaruResource;

import org.orekit.bodies.GeodeticPoint;
import org.orekit.time.AbsoluteDate;

/**
 * Core model class.
 */
public final class CoreModel
{
    private static CoreModel coreModel;

    private final IScenarioModel scenarioModel;
    private final ScenarioModelManager scenarioModelManager;

    public static CoreModel getDefault()
    {
        if (coreModel == null) {
            coreModel = new CoreModel(new ScenarioModel(), new ScenarioModelManager());
        }
        return coreModel;
    }

    private CoreModel(IScenarioModel scenarioModel, ScenarioModelManager scenarioModelManager)
    {
        this.scenarioModel = scenarioModel;
        this.scenarioModelManager = scenarioModelManager;
    }

    public void startup()
    {

    }

    public void shutdown()
    {

    }

    public IScenarioModel getScenarioModel()
    {
        return scenarioModel;
    }

    public void addScenarioModelListener(IScenarioModelListener listener)
    {
        scenarioModelManager.addModelListener(listener);
    }

    public void removeScenarioModelListener(IScenarioModelListener listener)
    {
        scenarioModelManager.removeModelListener(listener);
    }

    public boolean hasScenario(String name)
    {
        return (scenarioModel.getScenarioProject(name) != null);
    }

    public boolean hasScenario(IScenarioProject scenario)
    {
        return (scenarioModel.getScenarioProject(scenario.getElementName()) != null);
    }

    public List<IScenarioProject> getScenarios()
    {
        return scenarioModel.getScenarioProjects();
    }

    public IScenarioProject getScenario(String name)
    {
        return scenarioModel.getScenarioProject(name);
    }

    public IScenarioProjectStorage getStorage(IScenarioProject scenario)
    {
        return scenarioModelManager.getStorage(scenario);
    }

    public void createScenario(IScenarioProject scenario, IScenarioProjectStorage storage, boolean update)
    {
        scenarioModelManager.createScenario(scenario, storage, update);
    }

    public void addScenario(IScenarioProject scenario, IScenarioProjectStorage storage, boolean update)
    {
        scenarioModelManager.addScenario(scenario, storage, update);
    }

    public void removeScenario(IScenarioProject scenario, boolean update)
    {
        scenarioModelManager.removeScenario(scenario, update);
    }

    public void removeElement(IScenarioElement element, boolean update)
    {
        scenarioModelManager.removeElement(element, update);
    }

    public void changeElementName(IScenarioElement element, String name, boolean update)
    {
        scenarioModelManager.changeElementName(element, name, update);
    }

    public void changeElementComment(IScenarioElement element, String comment, boolean update)
    {
        scenarioModelManager.changeElementComment(element, comment, update);
    }

    public void addTimepoint(IScenarioProject project, AbsoluteDate date, boolean update)
    {
        scenarioModelManager.addTimepoint(project, date, update);
    }

    public void removeTimepoint(ITimepoint timepoint, boolean update)
    {
        scenarioModelManager.removeTimepoint(timepoint, update);
    }

    public void changeTimepoint(ITimepoint timepoint, AbsoluteDate date, boolean update)
    {
        scenarioModelManager.changeTimepoint(timepoint, date, update);
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
        scenarioModelManager.changeScenarioElementsTime(project, date, update);
    }

    public void addGroundstation(IScenarioProject project, IGroundstation groundstation, boolean update)
    {
        scenarioModelManager.addGroundstation(project, groundstation, update);
    }

    public void addSpacecraft(IScenarioProject project, ISpacecraft spacecraft, boolean update)
    {
        scenarioModelManager.addSpacecraft(project, spacecraft, update);
    }

    public void changeColor(IVisibleElement element, VisibleElementColor color, boolean update)
    {
        scenarioModelManager.changeElementColor(element, color, update);
    }

    public void changeImage(IVisibleElement element, IMaruResource image, boolean update)
    {
        scenarioModelManager.changeElementImage(element, image, update);
    }

    public void changeInitialCoordinate(IGroundstation element, GeodeticPoint position, double elevation, boolean update)
    {
        scenarioModelManager.changeInitialCoordinate(element, position, elevation, update);
    }

    public void changeInitialCoordinate(ISpacecraft element, ICoordinate coordinate, boolean update)
    {
        scenarioModelManager.changeInitialCoordinate(element, coordinate, update);
    }

    public void changeCentralBodyImage(ICentralBody element, IMaruResource image, boolean update)
    {
        scenarioModelManager.changeCentralBodyImage(element, image, update);
    }

    public void changeCentralBodyGM(ICentralBody element, double gm, boolean update)
    {
        scenarioModelManager.changeCentralBodyGM(element, gm, update);
    }

    public void changeCentralBodyEquatorialRadius(ICentralBody element, double radius, boolean update)
    {
        scenarioModelManager.changeCentralBodyEquatorialRadius(element, radius, update);
    }

    public void changeCentralBodyFlattening(ICentralBody element, double flattening, boolean update)
    {
        scenarioModelManager.changeCentralBodyFlattening(element, flattening, update);
    }

    public void addTimeProvider(IScenarioProject project, ITimeProvider provider)
    {
        scenarioModelManager.addTimeProvider(project, provider);
    }

    public void removeTimeProvider(IScenarioProject project, ITimeProvider provider)
    {
        scenarioModelManager.removeTimeProvider(project, provider);
    }

    public void addTimeProvider(IGroundstation element, ITimeProvider provider)
    {
        scenarioModelManager.addTimeProvider(element, provider);
    }

    public void removeTimeProvider(IGroundstation element, ITimeProvider provider)
    {
        scenarioModelManager.removeTimeProvider(element, provider);
    }

    public void addTimeProvider(ISpacecraft element, ITimeProvider provider)
    {
        scenarioModelManager.addTimeProvider(element, provider);
    }

    public void removeTimeProvider(ISpacecraft element, ITimeProvider provider)
    {
        scenarioModelManager.removeTimeProvider(element, provider);
    }

    public void addPropagationListener(IScenarioProject project, IPropagationListener listener)
    {
        scenarioModelManager.addPropagationListener(project, listener);
    }

    public void removePropagationListener(IScenarioProject project, IPropagationListener listener)
    {
        scenarioModelManager.removePropagationListener(project, listener);
    }

    public void addPropagationListener(ISpacecraft element, IPropagationListener listener)
    {
        scenarioModelManager.addPropagationListener(element, listener);
    }

    public void removePropagationListener(ISpacecraft element, IPropagationListener listener)
    {
        scenarioModelManager.removePropagationListener(element, listener);
    }

    public void changePropagator(ISpacecraft element, AbstractPropagator propagator)
    {
        scenarioModelManager.changePropagator(element, propagator);
    }
}
