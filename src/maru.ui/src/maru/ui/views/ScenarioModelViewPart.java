package maru.ui.views;

import maru.core.model.CoreModel;
import maru.core.model.ICentralBody;
import maru.core.model.IPropagatable;
import maru.core.model.IScenarioElement;
import maru.core.model.IScenarioModelListener;
import maru.core.model.IScenarioProject;
import maru.core.model.ITimepoint;
import maru.ui.model.IUiProjectSelectionListener;
import maru.ui.model.UiElement;
import maru.ui.model.UiModel;
import maru.ui.model.UiProject;

import org.eclipse.ui.part.ViewPart;

public abstract class ScenarioModelViewPart extends ViewPart
                                            implements IScenarioModelListener,
                                                       IUiProjectSelectionListener
{
    public ScenarioModelViewPart()
    {
        CoreModel.getDefault().addScenarioModelListener(this);
    }

    public UiProject getCurrentProject()
    {
        return UiModel.getDefault().getCurrentUiProject();
    }

    public UiElement getCurrentElement()
    {
        return UiModel.getDefault().getCurrentUiElement();
    }

    @Override
    public void dispose()
    {
        super.dispose();

        CoreModel.getDefault().removeScenarioModelListener(this);
    }

    @Override
    public void scenarioCreated(IScenarioProject project)
    {

    }

    @Override
    public void scenarioAdded(IScenarioProject project)
    {

    }

    @Override
    public void scenarioRemoved(IScenarioProject project)
    {

    }

    @Override
    public void elementAdded(IScenarioElement element)
    {

    }

    @Override
    public void elementRemoved(IScenarioElement element)
    {

    }

    @Override
    public void elementRenamed(IScenarioElement element)
    {

    }

    @Override
    public void elementCommented(IScenarioElement element)
    {

    }

    @Override
    public void elementColorChanged(IPropagatable element)
    {

    }

    @Override
    public void elementImageChanged(IPropagatable element)
    {

    }

    @Override
    public void elementInitialCoordinateChanged(IPropagatable element)
    {

    }

    @Override
    public void centralbodyImageChanged(ICentralBody element)
    {

    }

    @Override
    public void centralbodyGmChanged(ICentralBody element)
    {

    }

    @Override
    public void centralbodyEquatorialRadiusChanged(ICentralBody element)
    {

    }

    @Override
    public void centralbodyFlatteningChanged(ICentralBody element)
    {

    }

    @Override
    public void propagatablesTimeChanged(IScenarioProject element)
    {

    }

    @Override
    public void timepointStartChanged(ITimepoint element)
    {

    }

    @Override
    public void timepointStopChanged(ITimepoint element)
    {

    }

    @Override
    public void timepointCurrentChanged(ITimepoint element)
    {

    }

    @Override
    public void timepointAdded(ITimepoint element)
    {

    }

    @Override
    public void timepointRemoved(ITimepoint element)
    {

    }

    @Override
    public void timepointChanged(ITimepoint element)
    {

    }
}
