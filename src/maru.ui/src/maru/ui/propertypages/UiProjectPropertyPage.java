package maru.ui.propertypages;

import maru.core.model.CoreModel;
import maru.core.model.IScenarioProject;
import maru.ui.model.UiElement;
import maru.ui.model.UiModel;
import maru.ui.model.UiProject;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class UiProjectPropertyPage extends UiElementPropertyPage
{
    @Override
    public UiElement getUiElement()
    {
        // it is a long way to get the corresponding UiElement from an IProject:
        //  1. get the selected IProject (plugin.xml must take care that this is valid)
        //  2. get the IScenarioProject of the IProject using CoreModel
        //  3. get the UiProject for the IScenarioProject using UiModel
        //  4. the UiProject is a subclass of UiElement

        IProject project = getProject();
        IScenarioProject scenario = CoreModel.getDefault().getScenarioProject(project);
        UiProject uiProject = UiModel.getDefault().getUiProject(scenario);
        return uiProject;
    }

    @Override
    public IProject getProject()
    {
        return (IProject) getElement();
    }

    @Override
    protected Control createContents(Composite parent)
    {
        Control container = super.createContents(parent);

        // the project's name may not be changed
        getNameControl().setEnabled(false);

        return container;
    }
}