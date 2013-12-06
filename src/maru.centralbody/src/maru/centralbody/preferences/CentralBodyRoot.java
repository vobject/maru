package maru.centralbody.preferences;

import maru.centralbody.MaruCentralBodyPlugin;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class CentralBodyRoot extends PreferencePage implements IWorkbenchPreferencePage
{
    public CentralBodyRoot()
    {
        setDescription("Central Body Preferences");
    }

    @Override
    public void init(IWorkbench workbench)
    {
        setPreferenceStore(MaruCentralBodyPlugin.getDefault().getPreferenceStore());
    }

    @Override
    protected Control createContents(Composite parent)
    {
        return null;
    }
}
