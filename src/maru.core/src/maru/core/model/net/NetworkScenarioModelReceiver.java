package maru.core.model.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;

import maru.core.model.ICentralBody;
import maru.core.model.IGroundstation;
import maru.core.model.IScenarioElement;
import maru.core.model.IScenarioProject;
import maru.core.model.ISpacecraft;
import maru.core.model.ITimepoint;
import maru.core.model.IVisibleElement;
import maru.core.model.ScenarioModelAdapter;

import org.orekit.data.DataProvidersManager;

public abstract class NetworkScenarioModelReceiver extends ScenarioModelAdapter
                                                   implements INetworkServerConnection
{
    private Socket server;
    private ObjectInputStream objInput;

    public NetworkScenarioModelReceiver()
    {
        initOrekitDataPath();
    }

    @Override
    public void open(String serverIP, int serverPort) throws IOException
    {
        InetAddress serverAddr = InetAddress.getByName(serverIP);

        this.server = new Socket(serverAddr, serverPort);
        this.objInput = new ObjectInputStream(this.server.getInputStream());
    }

    @Override
    public void read() throws IOException, ClassNotFoundException
    {
        processMessage(readMessage());
    }

    @Override
    public void close() throws IOException
    {
        if (objInput != null)
        {
            objInput.close();
            objInput = null;
            server = null;
        }
    }

    protected NetworkMessageWrapper readMessage() throws ClassNotFoundException, IOException
    {
        Object obj = objInput.readObject();
        return (NetworkMessageWrapper) obj;
    }

    protected void processMessage(NetworkMessageWrapper msg)
    {
        processMessage(msg.getMessageID(), msg.getElement());
    }

    protected void processMessage(NetworkMessageID id, IScenarioElement element)
    {
        switch (id)
        {
            case SCENARIO_CREATED:
                scenarioCreated((IScenarioProject) element);
                break;
            case SCENARIO_ADDED:
                scenarioAdded((IScenarioProject) element);
                break;
            case SCENARIO_REMOVED:
                scenarioRemoved((IScenarioProject) element);
                break;

            case ELEMENT_ADDED:
                elementAdded(element);
                break;
            case ELEMENT_REMOVED:
                elementRemoved(element);
                break;
            case ELEMENT_RENAMED:
                elementRenamed(element);
                break;
            case ELEMENT_COMMENTED:
                elementCommented(element);
                break;
            case ELEMENT_COLOR_CHANGED:
                elementColorChanged((IVisibleElement) element);
                break;
            case ELEMENT_IMAGE_CHANGED:
                elementImageChanged((IVisibleElement) element);
                break;

            case CENTRAL_BODY_IMAGE_CHANGED:
                centralbodyImageChanged((ICentralBody) element);
                break;
            case CENTRAL_BODY_GM_CHANGED:
                centralbodyGmChanged((ICentralBody) element);
                break;
            case CENTRAL_BODY_EQUATORIAL_RADIUS_CHANGED:
                centralbodyEquatorialRadiusChanged((ICentralBody) element);
                break;
            case CENTRAL_BODY_FLATTENING_CHANGED:
                centralbodyFlatteningChanged((ICentralBody) element);
                break;

            case GROUNDSTATION_INITIAL_COORDINATE_CHANGED:
                elementInitialCoordinateChanged((IGroundstation) element);
                break;
            case SPACECRAFT_INITIAL_COORDINATE_CHANGED:
                elementInitialCoordinateChanged((ISpacecraft) element);
                break;

            case PROPAGATABLES_TIME_CHANGED:
                propagatablesTimeChanged((IScenarioProject) element);
                break;

            case TIMEPOINT_START_CHANGED:
                timepointStartChanged((ITimepoint) element);
                break;
            case TIMEPOINT_STOP_CHANGED:
                timepointStopChanged((ITimepoint) element);
                break;
            case TIMEPOINT_CURRENT_CHANGED:
                timepointCurrentChanged((ITimepoint) element);
                break;

            case TIMEPOINT_ADDED:
                timepointAdded((ITimepoint) element);
                break;
            case TIMEPOINT_REMOVED:
                timepointRemoved((ITimepoint) element);
                break;
            case TIMEPOINT_CHANGED:
                timepointChanged((ITimepoint) element);
                break;

            default:
                throw new RuntimeException("Unknown server message.");
        }
    }

    protected void  initOrekitDataPath()
    {
        System.setProperty(DataProvidersManager.OREKIT_DATA_PATH, getOrekitDataPath());
    }

    protected abstract String getOrekitDataPath();
}
