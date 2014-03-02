package maru.core.model.net;

import java.io.IOException;

public abstract class NetworkClientThread extends NetworkScenarioModelReceiver
                                          implements Runnable
{
    public NetworkClientThread(String serverIP, int serverPort) throws IOException
    {
        super(serverIP, serverPort);
    }

    @Override
    public void run()
    {
        while (!Thread.currentThread().isInterrupted())
        {
            try
            {
                processMessage(readMessage());
            }
            catch (ClassNotFoundException | IOException e)
            {
                e.printStackTrace();

                try {
                    close();
                } catch (IOException eAgain) {
                    eAgain.printStackTrace();
                }
                break;
            }
        }

        System.out.println("client done!");
    }
}
