package maru.core.model.net;

import java.io.IOException;
import java.net.Socket;

import maru.core.MaruCorePlugin;

public class NetworkClientConnection extends NetworkScenarioModelAdapterEx
{
    public NetworkClientConnection(Socket client) throws IOException
    {
        super(client);

        MaruCorePlugin.getDefault().getCoreModel().addScenarioModelListener(this);
    }

    @Override
    public void close()
    {
        try {
            super.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            MaruCorePlugin.getDefault().getCoreModel().removeScenarioModelListener(this);
        }
    }
}
