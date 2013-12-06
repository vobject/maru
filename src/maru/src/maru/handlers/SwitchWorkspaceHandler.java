package maru.handlers;

import maru.PickWorkspaceDialog;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.ui.PlatformUI;

public class SwitchWorkspaceHandler extends AbstractHandler
{
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        PickWorkspaceDialog pwd = new PickWorkspaceDialog(true, null);

        if (pwd.open() != Dialog.CANCEL)
        {
            PlatformUI.getWorkbench().restart();
        }

        return null;
    }
}
