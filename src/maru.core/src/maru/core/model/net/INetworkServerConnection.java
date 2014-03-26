package maru.core.model.net;

import java.io.IOException;

public interface INetworkServerConnection
{
    void open(String ip, int port) throws IOException;
    void read() throws IOException, ClassNotFoundException;
    void close() throws IOException;
}
