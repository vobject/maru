package maru.ui.propertypages;

import maru.core.model.IScenarioElement;
import maru.core.model.IScenarioProject;
import maru.core.workspace.WorkspaceModel;
import maru.ui.model.UiElement;
import maru.ui.model.UiProject;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.dialogs.PropertyPage;

public abstract class UiPropertyPage extends PropertyPage
{
    public UiElement getUiElement()
    {
        return (UiElement) getElement().getAdapter(UiElement.class);
    }

    public UiProject getUiProject()
    {
        return getUiElement().getUiProject();
    }

    public IProject getProject()
    {
        IScenarioProject scenario = getUiElement().getUiProject().getUnderlyingElement();
        return WorkspaceModel.getDefault().getProject(scenario);
    }

    public IScenarioProject getScenario()
    {
        return getUiElement().getUiProject().getUnderlyingElement();
    }

    public IScenarioElement getScenarioElement()
    {
        return getUiElement().getUnderlyingElement();
    }

    @Override
    protected void performDefaults()
    {
        setMessage("\"Restore Defaults\" is not available.", WARNING);
    }

    protected void addSeparator(Composite parent)
    {
        GridData data = new GridData();
        data.horizontalAlignment = SWT.FILL;
        data.grabExcessHorizontalSpace = true;
        data.horizontalSpan = 2;

        Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
        separator.setLayoutData(data);
    }
}