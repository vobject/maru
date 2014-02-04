package maru.ui.debug.handlers;

import maru.core.model.IScenarioProject;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.orekit.errors.OrekitException;

public class DebugCreateScenarioHandler2 extends AbstractHandler
{
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        IScenarioProject scenarioProject = CreateScenarioHelper.createEmpty();
        try
        {
            CreateScenarioHelper.createKeplerSatellite(scenarioProject);
        }
        catch (OrekitException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
