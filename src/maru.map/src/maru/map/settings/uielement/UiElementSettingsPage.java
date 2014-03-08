package maru.map.settings.uielement;

import maru.core.model.IVisibleElement;
import maru.map.MaruMapPlugin;
import maru.ui.model.UiVisibleElement;
import maru.ui.propertypages.UiPropertyPage;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class UiElementSettingsPage extends UiPropertyPage
{
    private UiElementSettings settings;

    private Button showElementIcon;
    private Text elementIconSize;

    @Override
    public UiVisibleElement getUiElement()
    {
        return (UiVisibleElement) super.getUiElement();
    }

    @Override
    public IVisibleElement getScenarioElement()
    {
        return (IVisibleElement) super.getScenarioElement();
    }

    @Override
    public boolean performOk()
    {
        settings.setShowElementIcon(showElementIcon.getSelection());
        settings.setElementIconSize(Long.parseLong(elementIconSize.getText()));

        MaruMapPlugin.getDefault().redraw();
        return true;
    }

    @Override
    protected Control createContents(Composite parent)
    {
        settings = getElementSettings();

        Composite container = createContainer(parent);
        Composite controls = createControls(container);

        initControls();

        return controls;
    }

    protected UiElementSettings getElementSettings()
    {
        UiVisibleElement element = getUiElement();
        return MaruMapPlugin.getDefault().getUiProjectsSettings().getProject(element.getUiProject()).getElement(element);
    }

    private Composite createContainer(Composite parent)
    {
        GridLayout layout = new GridLayout(2, false);
        layout.marginWidth = 0;
        layout.marginHeight = 0;

        GridData data = new GridData();
        data.verticalAlignment = SWT.FILL;
        data.grabExcessVerticalSpace = true;
        data.horizontalSpan = 2;

        Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(layout);
        container.setLayoutData(data);

        return container;
    }

    private Composite createControls(Composite container)
    {
        showElementIcon = new Button(container, SWT.CHECK | SWT.LEFT);
        GridData showElementIconData = new GridData();
        showElementIconData.horizontalSpan = 2;
        showElementIcon.setLayoutData(showElementIconData);

        new Label(container, SWT.NONE).setText(UiElementSettingsConstants.DESCRIPTION_ELEMENT_ICON_SIZE);
        elementIconSize = new Text(container, SWT.BORDER | SWT.SINGLE | SWT.WRAP);
        GridData elementIconSizeData = new GridData();
        elementIconSizeData.horizontalAlignment = SWT.FILL;
        elementIconSize.setLayoutData(elementIconSizeData);

        return container;
    }

    private void initControls()
    {
        showElementIcon.setText(UiElementSettingsConstants.DESCRIPTION_SHOW_ELEMENT_ICON);
        showElementIcon.setSelection(settings.getShowElementIcon());

        elementIconSize.setText(Long.toString(settings.getElementIconSize()));
    }
}
