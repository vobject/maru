package maru.core.model.net;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import maru.core.MaruCorePlugin;
import maru.core.model.IScenarioElement;
import maru.core.model.IScenarioModelListener;

public abstract class NetworkScenarioModelListener implements IScenarioModelListener,
                                                              INetworkClientConnection
{
    private Socket client;
    private ObjectOutputStream objOutput;

    @Override
    public void open(Socket socket) throws IOException
    {
        this.client = socket;
        this.objOutput = new ObjectOutputStream(this.client.getOutputStream());

        MaruCorePlugin.getDefault().getCoreModel().addScenarioModelListener(this);
    }

    @Override
    public void close() throws IOException
    {
        // always remove this object from model messages
        MaruCorePlugin.getDefault().getCoreModel().removeScenarioModelListener(this);

        if (objOutput != null)
        {
            objOutput.close();
            objOutput = null;
            client = null;
        }
    }

    protected void sendMessage(NetworkMessageID msg, IScenarioElement element)
    {
        if (objOutput == null) {
            return; // the object is in invalid state
        }

        try
        {
            objOutput.writeObject(new NetworkMessageWrapper(msg, element));
            objOutput.reset();
        }
        catch (IOException e)
        {
            e.printStackTrace();

            try {
                objOutput.close();
            } catch (IOException eAgain) {
                eAgain.printStackTrace();
                objOutput = null;
            }
        }
    }
}
