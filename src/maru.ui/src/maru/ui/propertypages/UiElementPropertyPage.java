package maru.ui.propertypages;

import maru.core.model.IScenarioProject;
import maru.ui.model.UiElement;
import maru.ui.model.UiProject;

import org.eclipse.core.resources.IProject;
import org.eclipse.ui.dialogs.PropertyPage;

public abstract class UiElementPropertyPage extends PropertyPage
{
    public UiElement getUiElement()
    {
        return (UiElement) getElement();
    }

    public UiProject getUiProject()
    {
        return getUiElement().getUiProject();
    }

    public IProject getProject()
    {
        return getUiElement().getUiProject().getUnderlyingElement().getProject();
    }

    public IScenarioProject getScenario()
    {
        return getUiElement().getUiProject().getUnderlyingElement();
    }

//  protected void addSeparator(Composite parent)
//  {
//      GridData data = new GridData();
//      data.horizontalAlignment = GridData.FILL;
//      data.grabExcessHorizontalSpace = true;
//      data.horizontalSpan = 2;
//
//      Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
//      separator.setLayoutData(data);
//  }
}