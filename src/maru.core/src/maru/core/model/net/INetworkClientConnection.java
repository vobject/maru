package maru.core.model.net;

import java.io.IOException;
import java.net.Socket;

public interface INetworkClientConnection
{
    void open(Socket socket) throws IOException;
    void close() throws IOException;
}
