package maru.core.model.net;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import maru.core.model.ICentralBody;
import maru.core.model.IGroundstation;
import maru.core.model.IScenarioElement;
import maru.core.model.IScenarioProject;
import maru.core.model.ISpacecraft;
import maru.core.model.ITimepoint;
import maru.core.model.IVisibleElement;

public abstract class NetworkScenarioModelAdapter implements INetworkScenarioModelListener
{
    private Socket client;
    private ObjectOutputStream objOutput;

    public NetworkScenarioModelAdapter(Socket client) throws IOException
    {
        this.client = client;
        this.objOutput = new ObjectOutputStream(this.client.getOutputStream());
    }

    public void close() throws IOException
    {
        if (objOutput != null)
        {
            objOutput.close();
            objOutput = null;
            client = null;
        }
    }

    @Override
    public void scenarioCreated(IScenarioProject project)
    {

    }

    @Override
    public void scenarioAdded(IScenarioProject project)
    {

    }

    @Override
    public void scenarioRemoved(IScenarioProject project)
    {

    }

    @Override
    public void elementAdded(IScenarioElement element)
    {

    }

    @Override
    public void elementRemoved(IScenarioElement element)
    {

    }

    @Override
    public void elementRenamed(IScenarioElement element)
    {

    }

    @Override
    public void elementCommented(IScenarioElement element)
    {

    }

    @Override
    public void elementColorChanged(IVisibleElement element)
    {

    }

    @Override
    public void elementImageChanged(IVisibleElement element)
    {

    }

    @Override
    public void centralbodyImageChanged(ICentralBody element)
    {

    }

    @Override
    public void centralbodyGmChanged(ICentralBody element)
    {

    }

    @Override
    public void centralbodyEquatorialRadiusChanged(ICentralBody element)
    {

    }

    @Override
    public void centralbodyFlatteningChanged(ICentralBody element)
    {

    }

    @Override
    public void elementInitialCoordinateChanged(IGroundstation element)
    {

    }

    @Override
    public void elementInitialCoordinateChanged(ISpacecraft element)
    {

    }

    @Override
    public void propagatablesTimeChanged(IScenarioProject element)
    {

    }

    @Override
    public void timepointStartChanged(ITimepoint element)
    {

    }

    @Override
    public void timepointStopChanged(ITimepoint element)
    {

    }

    @Override
    public void timepointCurrentChanged(ITimepoint element)
    {

    }

    @Override
    public void timepointAdded(ITimepoint element)
    {

    }

    @Override
    public void timepointRemoved(ITimepoint element)
    {

    }

    @Override
    public void timepointChanged(ITimepoint element)
    {

    }

    protected void sendMessage(NetworkMessageID msg, IScenarioElement element)
    {
        if (objOutput == null) {
            return; // the object is in invalid state
        }

        try {
            objOutput.writeObject(new NetworkMessageWrapper(msg, element));
            objOutput.reset();
        } catch (IOException e) {
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
