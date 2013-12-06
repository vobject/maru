package maru.centralbody.preferences;

import maru.centralbody.MaruCentralBodyPlugin;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class EarthPreferences extends FieldEditorPreferencePage implements IWorkbenchPreferencePage
{
    public EarthPreferences()
    {
        super(GRID);
    }

    @Override
    public void init(IWorkbench workbench)
    {
        setPreferenceStore(MaruCentralBodyPlugin.getDefault().getPreferenceStore());
    }

    @Override
    protected void createFieldEditors()
    {
        addField(new MapImagesEditor(PreferenceConstants.P_EXTERNAL_IMAGES,
                 "External Map Images",
                 getFieldEditorParent()));
    }
}
