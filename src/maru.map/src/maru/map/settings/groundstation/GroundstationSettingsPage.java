package maru.map.settings.groundstation;

import maru.core.model.IGroundstation;
import maru.map.MaruMapPlugin;
import maru.map.settings.scenario.ScenarioModelSettings;
import maru.map.settings.scenario.ScenarioSettings;
import maru.ui.model.UiGroundstation;
import maru.ui.propertypages.UiPropertyPage;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class GroundstationSettingsPage extends UiPropertyPage
{
    private GroundstationSettings settings;

    private Button showElementName;
    private Text elementIconSize;

    @Override
    public UiGroundstation getUiElement()
    {
        return (UiGroundstation) super.getUiElement();
    }

    @Override
    public IGroundstation getScenarioElement()
    {
        return getUiElement().getUnderlyingElement();
    }

    @Override
    public boolean performOk()
    {
        settings.setShowElementName(showElementName.getSelection());
        settings.setElementIconSize(Long.parseLong(elementIconSize.getText()));

        MaruMapPlugin.getDefault().redraw();
        return true;
    }

    @Override
    protected void performDefaults()
    {
        showElementName.setSelection(GroundstationSettingsConstants.DEFAULT_SHOW_ELEMENT_NAME);
        elementIconSize.setText(Long.toString(GroundstationSettingsConstants.DEFAULT_ELEMENT_ICON_SIZE));
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

    protected GroundstationSettings getElementSettings()
    {
        IGroundstation element = getScenarioElement();

        ScenarioModelSettings model = MaruMapPlugin.getDefault().getScenarioModelSettings();
        ScenarioSettings scenario = model.getScenario(element.getScenarioProject());
        return scenario.getGroundstation(element);
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
        showElementName = new Button(container, SWT.CHECK | SWT.LEFT);
        GridData showElementIconData = new GridData();
        showElementIconData.horizontalSpan = 2;
        showElementName.setLayoutData(showElementIconData);

        new Label(container, SWT.NONE).setText(GroundstationSettingsConstants.DESCRIPTION_ELEMENT_ICON_SIZE);
        elementIconSize = new Text(container, SWT.BORDER | SWT.SINGLE | SWT.WRAP);
        GridData elementIconSizeData = new GridData();
        elementIconSizeData.grabExcessHorizontalSpace = true;
        elementIconSizeData.horizontalAlignment = SWT.FILL;
        elementIconSize.setLayoutData(elementIconSizeData);

        return container;
    }

    private void initControls()
    {
        showElementName.setText(GroundstationSettingsConstants.DESCRIPTION_SHOW_ELEMENT_NAME);
        showElementName.setSelection(settings.getShowElementName());

        elementIconSize.setText(Long.toString(settings.getElementIconSize()));
    }
}
