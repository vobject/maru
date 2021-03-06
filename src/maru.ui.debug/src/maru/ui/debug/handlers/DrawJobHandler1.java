package maru.ui.debug.handlers;

import maru.map.MaruMapPlugin;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

public class DrawJobHandler1 extends AbstractHandler
{
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        System.out.println("DrawJobHandler1");

        MovingAnimationJob job = new MovingAnimationJob();
        job.setDuration(3000);
//        job.setStartPosition(0, 0);
//        job.setStopPosition(640, 480);

        MaruMapPlugin.getDefault().registerProjectAnimationJob(job);
        MaruMapPlugin.getDefault().redraw();
        return null;
    }
}
