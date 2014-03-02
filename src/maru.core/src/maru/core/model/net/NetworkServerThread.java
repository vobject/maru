package maru.core.model.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class NetworkServerThread implements Runnable
{
    private final ServerSocket server;
    private final List<NetworkClientConnection> clientThreads;

    public NetworkServerThread(int port) throws IOException
    {
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
                Socket clientSocket = server.accept();
                clientThreads.add(new NetworkClientConnection(clientSocket));
            }
            catch (IOException e)
            {
                e.printStackTrace();
                break;
            }
        }

        System.out.println("server stopped, closing client connections");
        for (NetworkClientConnection client : clientThreads) {
            client.close();
        }
        System.out.println("server done!");
    }
}
