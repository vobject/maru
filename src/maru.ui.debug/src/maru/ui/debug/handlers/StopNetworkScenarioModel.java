package maru.ui.debug.handlers;

import maru.ui.debug.MaruUiDebugPlugin;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

public class StopNetworkScenarioModel extends AbstractHandler
{
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        Thread t = MaruUiDebugPlugin.getDefault().getNetworkScenarioModelThread();
        if (t != null)
        {
            t.interrupt();
            MaruUiDebugPlugin.getDefault().setNetworkScenarioModelThread(null);
        }
        return null;
    }
}
