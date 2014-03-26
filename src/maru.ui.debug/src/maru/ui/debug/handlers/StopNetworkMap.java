package maru.ui.debug.handlers;

import maru.ui.debug.MaruUiDebugPlugin;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

public class StopNetworkMap extends AbstractHandler
{
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        Thread t = MaruUiDebugPlugin.getDefault().getNetworkMapThread();
        if (t != null)
        {
            t.interrupt();
            MaruUiDebugPlugin.getDefault().setNetworkMapThread(null);
        }
        return null;
    }
}
