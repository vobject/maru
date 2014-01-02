package maru.ui.propertypages;

import maru.core.model.ITimepoint;
import maru.ui.model.UiTimepoint;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

public class UiTimepointPropertyPage extends UiPropertyPage
{
    @Override
    public UiTimepoint getUiElement()
    {
        return (UiTimepoint) getElement().getAdapter(UiTimepoint.class);
    }

    @Override
    public ITimepoint getScenarioElement()
    {
        return getUiElement().getUnderlyingElement();
    }

    @Override
    protected Control createContents(Composite parent)
    {
        new Label(parent, SWT.NONE).setText("Timepoint specific properties.");
        return null;
    }
}
