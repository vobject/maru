package maru.map.settings.scenario;

import maru.core.model.CoreModel;
import maru.core.model.IScenarioElement;
import maru.core.model.IScenarioProject;
import maru.core.model.ScenarioModelAdapter;
import maru.ui.MaruUIPlugin;

import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

public class ScenarioModelSettings extends ScenarioModelAdapter
{
    private final Preferences scenarioModelNode;

    public ScenarioModelSettings(Preferences parentNode)
    {
        if (parentNode == null) {
            throw new IllegalArgumentException("ScenarioModel parent node must not be null.");
        }

        this.scenarioModelNode = parentNode.node("ScenarioModel");
    }

    public void attachToModel()
    {
        CoreModel.getDefault().addScenarioModelListener(this);
    }

    public void detachFromModel()
    {
        CoreModel.getDefault().removeScenarioModelListener(this);
    }

    public void save() throws BackingStoreException
    {
        scenarioModelNode.flush();
    }

    public ScenarioSettings getScenario(IScenarioProject scenario)
    {
        return getScenario(scenario.getElementName());
    }

    public ScenarioSettings getScenario(String name)
    {
        Preferences node = scenarioModelNode.node(name);
        return new ScenarioSettings(node);
    }

    public ScenarioSettings getCurrentScenario()
    {
        String name = getCurrentScenarioName();
        Preferences node = scenarioModelNode.node(name);
        return new ScenarioSettings(node);
    }

    @Override
    public void scenarioRemoved(IScenarioProject project)
    {
        getScenario(project).remove();
    }

    @Override
    public void elementRemoved(IScenarioElement element)
    {
        getScenario(element.getScenarioProject()).removeElement(element);
    }

    private String getCurrentScenarioName()
    {
        return MaruUIPlugin.getDefault().getUiModel().getCurrentUiProject()
                                                     .getUnderlyingElement()
                                                     .getElementName();
    }
}
