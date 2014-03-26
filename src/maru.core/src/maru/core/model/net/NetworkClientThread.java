package maru.core.model.net;

import java.io.IOException;

public abstract class NetworkClientThread<T extends INetworkServerConnection> implements Runnable
{
    private final String ip;
    private final int port;
    private final T connection;

    public NetworkClientThread(Class<T> clazz, String serverIP, int serverPort) throws InstantiationException, IllegalAccessException
    {
        this.ip = serverIP;
        this.port = serverPort;
        this.connection = clazz.newInstance();
    }

    @Override
    public void run()
    {
        if (!openConnection()) {
            return;
        }

        while (!Thread.currentThread().isInterrupted())
        {
            if (!readFromServer()) {
                break;
            }
        }

        closeConnection();
    }

    boolean openConnection()
    {
        try {
            connection.open(ip, port);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    boolean readFromServer()
    {
        try {
            connection.read();
            return true;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    void closeConnection()
    {
        try {
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
