package maru.ui.debug.handlers;

import java.io.IOException;

import maru.core.model.net.NetworkServerThread;
import maru.map.net.NetworkMap;
import maru.ui.debug.MaruUiDebugPlugin;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

public class StartNetworkMap extends AbstractHandler
{
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException
    {
        try {
            Thread t = new Thread(new NetworkServerThread<NetworkMap>(NetworkMap.class, 8469));
            MaruUiDebugPlugin.getDefault().setNetworkMapThread(t);

            t.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
