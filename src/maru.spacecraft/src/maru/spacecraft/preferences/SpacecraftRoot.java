package maru.spacecraft.preferences;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class SpacecraftRoot extends PreferencePage implements IWorkbenchPreferencePage
{
    public SpacecraftRoot()
    {
        setDescription("Spacecraft Preferences");
    }

    @Override
    public void init(IWorkbench workbench)
    {

    }

    @Override
    protected Control createContents(Composite parent)
    {
        return null;
    }
}
