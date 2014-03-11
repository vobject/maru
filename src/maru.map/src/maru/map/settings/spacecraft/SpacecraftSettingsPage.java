package maru.map.settings.spacecraft;

import maru.core.model.ISpacecraft;
import maru.map.MaruMapPlugin;
import maru.map.settings.scenario.ScenarioModelSettings;
import maru.map.settings.scenario.ScenarioSettings;
import maru.ui.model.UiSpacecraft;
import maru.ui.propertypages.UiPropertyPage;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class SpacecraftSettingsPage extends UiPropertyPage
{
    private SpacecraftSettings settings;

    private Button showElementName;
    private Text elementIconSize;
    private Text groundtrackLength;
    private Text groundtrackLineWidth;

    @Override
    public UiSpacecraft getUiElement()
    {
        return (UiSpacecraft) super.getUiElement();
    }

    @Override
    public ISpacecraft getScenarioElement()
    {
        return getUiElement().getUnderlyingElement();
    }

    @Override
    public boolean performOk()
    {
        settings.setShowElementName(showElementName.getSelection());
        settings.setElementIconSize(Long.parseLong(elementIconSize.getText()));
        settings.setGroundtrackLength(Long.parseLong(groundtrackLength.getText()) * (60 * 60)); // hours -> seconds
        settings.setGroundtrackLineWidth(Long.parseLong(groundtrackLineWidth.getText()));

        MaruMapPlugin.getDefault().redraw();
        return true;
    }

    @Override
    protected void performDefaults()
    {
        showElementName.setSelection(SpacecraftSettingsConstants.DEFAULT_SHOW_ELEMENT_NAME);
        elementIconSize.setText(Long.toString(SpacecraftSettingsConstants.DEFAULT_ELEMENT_ICON_SIZE));
        groundtrackLength.setText(Long.toString(SpacecraftSettingsConstants.DEFAULT_GROUNDTRACK_LENGTH / (60 * 60))); // seconds -> hours
        groundtrackLineWidth.setText(Long.toString(SpacecraftSettingsConstants.DEFAULT_GROUNDTRACK_LINE_WIDTH));
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

    protected SpacecraftSettings getElementSettings()
    {
        ISpacecraft element = getScenarioElement();

        ScenarioModelSettings model = MaruMapPlugin.getDefault().getScenarioModelSettings();
        ScenarioSettings scenario = model.getScenario(element.getScenarioProject());
        return scenario.getSpacecraft(element);
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

        new Label(container, SWT.NONE).setText(SpacecraftSettingsConstants.DESCRIPTION_ELEMENT_ICON_SIZE);
        elementIconSize = new Text(container, SWT.BORDER | SWT.SINGLE | SWT.WRAP);
        GridData elementIconSizeData = new GridData();
        elementIconSizeData.grabExcessHorizontalSpace = true;
        elementIconSizeData.horizontalAlignment = SWT.FILL;
        elementIconSize.setLayoutData(elementIconSizeData);

        new Label(container, SWT.NONE).setText(SpacecraftSettingsConstants.DESCRIPTION_GROUNDTRACK_LENGTH);
        groundtrackLength = new Text(container, SWT.BORDER | SWT.SINGLE | SWT.WRAP);
        GridData groundtrackLengthData = new GridData();
        groundtrackLengthData.grabExcessHorizontalSpace = true;
        groundtrackLengthData.horizontalAlignment = SWT.FILL;
        groundtrackLength.setLayoutData(groundtrackLengthData);

        new Label(container, SWT.NONE).setText(SpacecraftSettingsConstants.DESCRIPTION_GROUNDTRACK_LINE_WIDTH);
        groundtrackLineWidth = new Text(container, SWT.BORDER | SWT.SINGLE | SWT.WRAP);
        GridData groundtrackLineWidthData = new GridData();
        groundtrackLineWidthData.grabExcessHorizontalSpace = true;
        groundtrackLineWidthData.horizontalAlignment = SWT.FILL;
        groundtrackLineWidth.setLayoutData(groundtrackLineWidthData);

        return container;
    }

    private void initControls()
    {
        showElementName.setText(SpacecraftSettingsConstants.DESCRIPTION_SHOW_ELEMENT_NAME);
        showElementName.setSelection(settings.getShowElementName());

        elementIconSize.setText(Long.toString(settings.getElementIconSize()));

        groundtrackLength.setText(Long.toString(settings.getGroundtrackLength() / (60 * 60))); // seconds -> hours

        groundtrackLineWidth.setText(Long.toString(settings.getGroundtrackLineWidth()));
    }
}
