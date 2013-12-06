package maru.preferences;

import maru.MaruPlugin;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * The root node for application preferences. Other plugins are meant to
 * add their preference pages as children of this node.
 */
public class Root extends FieldEditorPreferencePage implements IWorkbenchPreferencePage
{
    public Root()
    {
        super(GRID);
    }

    @Override
    public void init(IWorkbench workbench)
    {
        setPreferenceStore(MaruPlugin.getDefault().getPreferenceStore());
    }

    @Override
    protected void createFieldEditors()
    {
        StringFieldEditor wndTitle =
                new StringFieldEditor(PreferenceConstants.P_APP_WINDOW_TITLE,
                                       "Window title (empty for default title)",
                                       getFieldEditorParent());
        wndTitle.setEmptyStringAllowed(true);
        addField(wndTitle);
    }
}
