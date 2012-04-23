package org.linuxmce.dce;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A connection to the DCE Router.
 */
public interface Connection {

    void close() throws IOException;
    InputStream getInputStream() throws IOException;
    OutputStream getOutputStream() throws IOException;
}
