package maru.core.model.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class NetworkServerThread<T extends INetworkClientConnection> implements Runnable
{
    private final Class<T> clientClazz;
    private final ServerSocket server;
    private final List<INetworkClientConnection> clientThreads;

    public NetworkServerThread(Class<T> clientClazz, int port) throws IOException
    {
        this.clientClazz = clientClazz;
        this.server = new ServerSocket(port);
        this.clientThreads = new ArrayList<>();
    }

    @Override
    public void run()
    {
        while (!Thread.currentThread().isInterrupted())
        {
            try
            {
                INetworkClientConnection client = clientClazz.newInstance();
                System.out.println("waiting for clients");
                client.open(server.accept());
                System.out.println("client connected");
                clientThreads.add(client);
            }
            catch (IOException | InstantiationException | IllegalAccessException e)
            {
                e.printStackTrace();
                break;
            }
        }

        System.out.println("server stopped, closing client connections");
        for (INetworkClientConnection client : clientThreads)
        {
            try
            {
                client.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        System.out.println("server done!");
    }
}
