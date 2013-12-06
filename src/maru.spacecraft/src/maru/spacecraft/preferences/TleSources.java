package maru.spacecraft.preferences;

import maru.spacecraft.MaruSpacecraftPlugin;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class TleSources extends FieldEditorPreferencePage implements IWorkbenchPreferencePage
{
    public TleSources()
    {
        super(GRID);
    }

    @Override
    public void init(IWorkbench workbench)
    {
        setPreferenceStore(MaruSpacecraftPlugin.getDefault().getPreferenceStore());
    }

    @Override
    protected void createFieldEditors()
    {
        addField(new TleSourceEditor(PreferenceConstants.P_EXTERNAL_TLE,
                                     "External TLE Sources",
                                     getFieldEditorParent()));
    }
}
