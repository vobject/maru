package maru.map.preferences;

import maru.map.MaruMapPlugin;

import org.eclipse.jface.preference.BooleanFieldEditor;
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

        IntegerFieldEditor visibilityCirclesPoints =
            new IntegerFieldEditor(PreferenceConstants.P_MAP_VISIBILITY_CIRCLE_POINTS,
                                   "Number of points to use for visibility circles",
                                   getFieldEditorParent());
        visibilityCirclesPoints.setValidRange(8, 360);
        addField(visibilityCirclesPoints);
    }
}
