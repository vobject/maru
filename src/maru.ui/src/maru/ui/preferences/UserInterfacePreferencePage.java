package maru.ui.preferences;

import maru.ui.MaruUIPlugin;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class UserInterfacePreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage
{
    public UserInterfacePreferencePage()
    {
        super(GRID);
    }

    @Override
    public void init(IWorkbench workbench)
    {
        setPreferenceStore(MaruUIPlugin.getDefault().getPreferenceStore());
    }

    @Override
    protected void createFieldEditors()
    {
        addField(new BooleanFieldEditor(UserInterfacePreferenceConstants.P_UI_SHOW_DEBUG_MENU,
                                        "Activate the debug menu",
                                        getFieldEditorParent()));
    }
}
