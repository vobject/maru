package maru.map.settings.scenario;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import maru.core.model.IScenarioProject;
import maru.core.model.utils.DaylengthDefinition;
import maru.core.workspace.WorkspaceModel;
import maru.map.MaruMapPlugin;
import maru.ui.model.UiElement;
import maru.ui.model.UiModel;
import maru.ui.model.UiProject;
import maru.ui.propertypages.UiPropertyPage;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

public class ScenarioSettingsPage extends UiPropertyPage
{
    private ScenarioSettings settings;

    private Button showVisibilityCircles;
    private Button showVisibilityScToSc;
    private Button showVisibilityScToGs;
    private Button showUmbraOnGroundtrack;
    private Button showNightMode;
    private Button showNightOverlay;
    private Combo daylengthDefinition;

    // mapping helper for daylength definitions name <-> value
    private final Map<String, String> daylengthDefinitionMap = new LinkedHashMap<>();

    @Override
    public UiElement getUiElement()
    {
        // it is a long way to get the corresponding UiElement from an IProject:
        //  1. get the selected IProject (plugin.xml must take care that this is valid)
        //  2. get the IScenarioProject of the IProject using CoreModel
        //  3. get the UiProject for the IScenarioProject using UiModel
        //  4. the UiProject is a subclass of UiElement

        IProject project = getProject();
        IScenarioProject scenario = WorkspaceModel.getDefault().getProject(project);
        UiProject uiProject = UiModel.getDefault().getUiProject(scenario);
        return uiProject;
    }

    @Override
    public IProject getProject()
    {
        return (IProject) getElement();
    }

    @Override
    public boolean performOk()
    {
        settings.setShowVisibilityCircles(showVisibilityCircles.getSelection());
        settings.setShowVisibilitySpacecraftToSpacecraft(showVisibilityScToSc.getSelection());
        settings.setShowVisibilitySpacecraftToGroundstation(showVisibilityScToGs.getSelection());
        settings.setShowUmbraOnGroundtrack(showUmbraOnGroundtrack.getSelection());
        settings.setShowNightMode(showNightMode.getSelection());
        settings.setShowNightOverlay(showNightOverlay.getSelection());
        settings.setDaylengthDefinition(DaylengthDefinition.toDaylengthDefinition(daylengthDefinitionMap.get(daylengthDefinition.getText())));

        MaruMapPlugin.getDefault().redraw();
        return true;
    }

    @Override
    protected void performDefaults()
    {
        showVisibilityCircles.setSelection(ScenarioSettingsConstants.DEFAULT_SHOW_VISIBILITY_CIRCLES);
        showVisibilityScToSc.setSelection(ScenarioSettingsConstants.DEFAULT_SHOW_VISIBILITY_SC_TO_SC);
        showVisibilityScToGs.setSelection(ScenarioSettingsConstants.DEFAULT_SHOW_VISIBILITY_SC_TO_GS);
        showUmbraOnGroundtrack.setSelection(ScenarioSettingsConstants.DEFAULT_SHOW_UMBRA_ON_GROUNDTRACK);
        showNightMode.setSelection(ScenarioSettingsConstants.DEFAULT_SHOW_NIGHT_MODE);
        showNightOverlay.setSelection(ScenarioSettingsConstants.DEFAULT_SHOW_NIGHT_OVERLAY);

        List<String> daylengthDefinitionValues = new ArrayList<>(daylengthDefinitionMap.values());
        daylengthDefinition.select(daylengthDefinitionValues.indexOf(ScenarioSettingsConstants.DEFAULT_DAYLENGTH_DEFINITION));
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

    protected ScenarioSettings getElementSettings()
    {
        IScenarioProject selection = getScenario();
        return MaruMapPlugin.getDefault().getScenarioModelSettings().getScenario(selection);
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
        showVisibilityCircles = new Button(container, SWT.CHECK | SWT.LEFT);
        GridData showVisibilityCirclesData = new GridData();
        showVisibilityCirclesData.horizontalSpan = 2;
        showVisibilityCircles.setLayoutData(showVisibilityCirclesData);

        showVisibilityScToSc = new Button(container, SWT.CHECK | SWT.LEFT);
        GridData showVisibilityScToScData = new GridData();
        showVisibilityScToScData.horizontalSpan = 2;
        showVisibilityScToSc.setLayoutData(showVisibilityScToScData);

        showVisibilityScToGs = new Button(container, SWT.CHECK | SWT.LEFT);
        GridData showVisibilityScToGsData = new GridData();
        showVisibilityScToGsData.horizontalSpan = 2;
        showVisibilityScToGs.setLayoutData(showVisibilityScToGsData);

        showUmbraOnGroundtrack = new Button(container, SWT.CHECK | SWT.LEFT);
        GridData showUmbraOnGroundtrackData = new GridData();
        showUmbraOnGroundtrackData.horizontalSpan = 2;
        showUmbraOnGroundtrack.setLayoutData(showUmbraOnGroundtrackData);

        showNightMode = new Button(container, SWT.CHECK | SWT.LEFT);
        GridData showNightModeData = new GridData();
        showNightModeData.horizontalSpan = 2;
        showNightMode.setLayoutData(showNightModeData);

        showNightOverlay = new Button(container, SWT.CHECK | SWT.LEFT);
        GridData showNightOverlayData = new GridData();
        showNightOverlayData.horizontalSpan = 2;
        showNightOverlay.setLayoutData(showNightOverlayData);

        new Label(container, SWT.NONE).setText(ScenarioSettingsConstants.DESCRIPTION_DAYLENGTH_DEFINITION);
        daylengthDefinition = new Combo(container, SWT.READ_ONLY);
        GridData daylengthDefinitionData = new GridData();
        daylengthDefinitionData.horizontalAlignment = SWT.FILL;
        daylengthDefinition.setLayoutData(daylengthDefinitionData);

        daylengthDefinitionMap.put(DaylightDefinitionChoices.DEF_CHOICE_0_NAME, DaylightDefinitionChoices.DEF_CHOICE_0_VALUE);
        daylengthDefinitionMap.put(DaylightDefinitionChoices.DEF_CHOICE_1_NAME, DaylightDefinitionChoices.DEF_CHOICE_1_VALUE);
        daylengthDefinitionMap.put(DaylightDefinitionChoices.DEF_CHOICE_2_NAME, DaylightDefinitionChoices.DEF_CHOICE_2_VALUE);
        daylengthDefinitionMap.put(DaylightDefinitionChoices.DEF_CHOICE_3_NAME, DaylightDefinitionChoices.DEF_CHOICE_3_VALUE);
        daylengthDefinitionMap.put(DaylightDefinitionChoices.DEF_CHOICE_4_NAME, DaylightDefinitionChoices.DEF_CHOICE_4_VALUE);
        daylengthDefinitionMap.put(DaylightDefinitionChoices.DEF_CHOICE_5_NAME, DaylightDefinitionChoices.DEF_CHOICE_5_VALUE);

        return container;
    }

    private void initControls()
    {
        showVisibilityCircles.setText(ScenarioSettingsConstants.DESCRIPTION_SHOW_VISIBILITY_CIRCLES);
        showVisibilityCircles.setSelection(settings.getShowVisibilityCircles());

        showVisibilityScToSc.setText(ScenarioSettingsConstants.DESCRIPTION_SHOW_VISIBILITY_SC_TO_SC);
        showVisibilityScToSc.setSelection(settings.getShowVisibilitySpacecraftToSpacecraft());

        showVisibilityScToGs.setText(ScenarioSettingsConstants.DESCRIPTION_SHOW_VISIBILITY_SC_TO_GS);
        showVisibilityScToGs.setSelection(settings.getShowVisibilitySpacecraftToGroundstation());

        showUmbraOnGroundtrack.setText(ScenarioSettingsConstants.DESCRIPTION_SHOW_UMBRA_ON_GROUNDTRACK);
        showUmbraOnGroundtrack.setSelection(settings.getShowUmbraOnGroundtrack());

        showNightMode.setText(ScenarioSettingsConstants.DESCRIPTION_SHOW_NIGHT_MODE);
        showNightMode.setSelection(settings.getShowNightMode());

        showNightOverlay.setText(ScenarioSettingsConstants.DESCRIPTION_SHOW_NIGHT_OVERLAY);
        showNightOverlay.setSelection(settings.getShowNightOverlay());

        List<String> daylengthDefinitionKeys = new ArrayList<>(daylengthDefinitionMap.keySet());
        List<String> daylengthDefinitionValues = new ArrayList<>(daylengthDefinitionMap.values());
        daylengthDefinition.setItems(daylengthDefinitionKeys.toArray(new String[0]));
        daylengthDefinition.select(daylengthDefinitionValues.indexOf(Double.toString(settings.getDaylengthDefinition().getValue())));
    }
}
