package maru.ui.debug.handlers;

import maru.map.MaruMapPlugin;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

public class CentralBodyShapeHandler extends AbstractHandler
{
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        CentralBodyShapeJob job = new CentralBodyShapeJob();

        MaruMapPlugin.getDefault().registerProjectDrawJob(job);
        MaruMapPlugin.getDefault().redraw();
        return null;
    }
}
