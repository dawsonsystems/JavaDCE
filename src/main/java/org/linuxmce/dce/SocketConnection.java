package org.linuxmce.dce;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * An implementation of the Connection interface using a standard Java Socket.
 */
public class SocketConnection implements Connection {
    private static Logger LOG = LoggerFactory.getLogger(SocketConnection.class);

    private Socket socket;

    public SocketConnection(String host, int port) throws IOException {
        socket = new Socket(host, port);
    }

    public void close() throws IOException {
        socket.close();
    }

    public InputStream getInputStream() throws IOException {
        return socket.getInputStream();
    }

    public OutputStream getOutputStream() throws IOException {
        return socket.getOutputStream();
    }
}
