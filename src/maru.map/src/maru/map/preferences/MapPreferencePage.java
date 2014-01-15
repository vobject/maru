package maru.map.preferences;

import maru.map.MaruMapPlugin;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class MapPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage
{
    public MapPreferencePage()
    {
        super(GRID);
    }

    @Override
    public void init(IWorkbench workbench)
    {
        setPreferenceStore(MaruMapPlugin.getDefault().getPreferenceStore());
    }

    @Override
    public void createFieldEditors()
    {
        addField(new BooleanFieldEditor(PreferenceConstants.P_MAP_ANTI_ALIASING,
                                        "Enable Anti-aliasing for ground tracks",
                                        getFieldEditorParent()));

        addField(new BooleanFieldEditor(PreferenceConstants.P_MAP_SHOW_UMBRA,
                                        "Show umbra and penumbra on ground tracks",
                                        getFieldEditorParent()));

        IntegerFieldEditor groundtrackStepSize =
            new IntegerFieldEditor(PreferenceConstants.P_MAP_GROUNDTRACK_STEP_SIZE,
                                   "Ground track step size in seconds",
                                   getFieldEditorParent());
        groundtrackStepSize.setValidRange(10, 1200);
        addField(groundtrackStepSize);

        IntegerFieldEditor groundtrackMaxLength =
            new IntegerFieldEditor(PreferenceConstants.P_MAP_GROUNDTRACK_LENGTH,
                                   "Ground track length in hours",
                                   getFieldEditorParent());
        groundtrackMaxLength.setValidRange(1, 96);
        addField(groundtrackMaxLength);

        addField(new BooleanFieldEditor(PreferenceConstants.P_MAP_SHOW_ACCESS_SC_TO_SC,
                                        "Show access between spacecrafts",
                                        getFieldEditorParent()));
        addField(new BooleanFieldEditor(PreferenceConstants.P_MAP_SHOW_ACCESS_SC_TO_GS,
                                        "Show access between spacecrafts and ground stations",
                                        getFieldEditorParent()));

        addField(new BooleanFieldEditor(PreferenceConstants.P_MAP_NIGHT,
                                        "Show night times on map",
                                        getFieldEditorParent()));

        IntegerFieldEditor nightStepSize =
            new IntegerFieldEditor(PreferenceConstants.P_MAP_NIGHT_STEPSIZE,
                                   "Night indicator step size in pixels",
                                   getFieldEditorParent());
        nightStepSize.setValidRange(1, 32);
        addField(nightStepSize);

        IntegerFieldEditor latlonLineStepSize =
            new IntegerFieldEditor(PreferenceConstants.P_MAP_LAT_LON_LINE_STEPSIZE,
                                   "Latitude/Longitude lines step size",
                                   getFieldEditorParent());
        latlonLineStepSize.setValidRange(10, 90);
        addField(latlonLineStepSize);

        String[][] daylengthDefinitionValues = new String[][] {
            { DaylightDefinitionChoices.DEF_CHOICE_0_NAME, DaylightDefinitionChoices.DEF_CHOICE_0_VALUE },
            { DaylightDefinitionChoices.DEF_CHOICE_1_NAME, DaylightDefinitionChoices.DEF_CHOICE_1_VALUE },
            { DaylightDefinitionChoices.DEF_CHOICE_2_NAME, DaylightDefinitionChoices.DEF_CHOICE_2_VALUE },
            { DaylightDefinitionChoices.DEF_CHOICE_3_NAME, DaylightDefinitionChoices.DEF_CHOICE_3_VALUE },
            { DaylightDefinitionChoices.DEF_CHOICE_4_NAME, DaylightDefinitionChoices.DEF_CHOICE_4_VALUE },
            { DaylightDefinitionChoices.DEF_CHOICE_5_NAME, DaylightDefinitionChoices.DEF_CHOICE_5_VALUE }
        };

        ComboFieldEditor daylengthDefinition =
            new ComboFieldEditor(PreferenceConstants.P_MAP_DAYLENGTH_DEFINITION,
                                 "Day light definition",
                                 daylengthDefinitionValues,
                                 getFieldEditorParent());
        addField(daylengthDefinition);
    }
}