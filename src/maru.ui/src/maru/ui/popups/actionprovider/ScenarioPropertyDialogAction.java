package maru.ui.popups.actionprovider;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.ui.actions.SelectionListenerAction;
import org.eclipse.ui.internal.dialogs.PropertyDialog;

/**
 * This class is based on org.eclipse.ui.dialogs.PropertyDialogAction.
 */
@SuppressWarnings("restriction")
public class ScenarioPropertyDialogAction extends SelectionListenerAction
{
    private final IShellProvider shellProvider;

    public ScenarioPropertyDialogAction(IShellProvider provider)
    {
        super("P&roperties");
        Assert.isNotNull(provider);

        this.shellProvider = provider;
    }

    @Override
    public void run()
    {
        PreferenceDialog dialog = createDialog();
        if (dialog != null) {
            dialog.open();
        }
    }

    public PreferenceDialog createDialog()
    {
        if (getStructuredSelection().isEmpty()) {
            return null;
        }

        return PropertyDialog.createDialogOn(shellProvider.getShell(),
                                             null,
                                             getStructuredSelection());
    }
}
