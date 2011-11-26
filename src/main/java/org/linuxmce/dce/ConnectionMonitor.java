/*
 *  Copyright (C) 2010 David Dawson
 *
 *  david.dawson@dawsonsystems.com
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111 USA
 */
package org.linuxmce.dce;

import org.linuxmce.dce.util.BinaryData;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Monitor a DCE connection.
 *
 * This class has the responsibility of creating message objects from connection byte stream.
 * I spins in it's own thread, and will execute the onMessage whenever it has a message.
 */
public abstract class ConnectionMonitor {

    public final static String MESSAGE_HEADER = "MESSAGE";

    //private static Logger LOG = LoggerFactory.getLogger(ConnectionMonitor.class);

    private String name;

    private Connection connection;

    private MessageDeserializer deserializer;

    public abstract void setup(InputStream read, OutputStream write) throws IOException;

    public abstract void onMessage(Message message) throws IOException;

    public ConnectionMonitor(String name, Connection connection, MessageDeserializer deserializer) {
        this.deserializer = deserializer;
        this.name = name;
        this.connection = connection;
    }

    void connect() throws IOException {
        //LOG.debug("connection connection monitor :" + name);
        //do the initial chat . .
        final OutputStream write = connection.getOutputStream();
        final InputStream read = connection.getInputStream();

        try {
            setup(read, write);
        } catch (Exception ex) {
            ex.printStackTrace();
            connection.close();
            return;
        }
        //LOG.debug("Starting monitor thread :" + name);
        //Then spawn off a thread to monitor the connection
        new Thread() {
            public void run() {
                try {
                    //extract a message
                    while (true) {
                        BinaryData data = readMessage();
                        Message message = deserializer.deserialize(data);
                        if (message != null) {
                            //Return 'OK' or we will be switched off by DCE Router!
                            write.write("OK\n".getBytes());
                            write.flush();
                        }
                        onMessage(message);
                    }
                } catch (Exception ex) {
                    //LOG.error("Exception in ConnectionMonitor [" + name + "], closing down connection", ex);
                    //Shutdown the handler
                    try {
                        onMessage(null);
                    } catch (Exception e) {
                       // LOG.warn("Error in onMessage handler during shutdown sequence", e);
                    }
                } finally {
                    try {
                        connection.close();
                    } catch (Exception ex) {
                    }
                }
            }
        }.start();
    }

    public BinaryData readMessage() throws IOException {
        String header = readLine().trim();

        // MESSAGE number
        if (header.indexOf(MESSAGE_HEADER + " ") > 0) {
            throw new DCEConnectionException("Not a Message Header [" + name + "] - '" + header + "'");
        }

        String sSize = header.substring(MESSAGE_HEADER.length() + 1);
        int messageSize = Integer.parseInt(sSize);
        BinaryData data = new BinaryData();

        byte[] buff = new byte[messageSize];

        int pos = 0;
        try {
            while (pos < messageSize) {
                int c = connection.getInputStream().read();

                if (c == -1) {
                    throw new DCEConnectionException("Not connected to DCERouter - Connection has been closed remotely");
                }

                buff[pos] = (byte) c;
                pos++;
            }
            data.write(buff);
        }
        catch (Exception e) {
            throw new DCEConnectionException("Not connected to DCERouter", e);
        }

        data.resetPosition();

        return data;
    }


    String readLine() throws IOException {
        StringBuilder buf = new StringBuilder();
        char c = (char) -1;
        while (c != '\n' && c != '\0') {
            c = (char) connection.getInputStream().read();
            if (c == -1 || c > 255) {
                throw new DCEConnectionException("Not connected to DCERouter [" + name + "], connection closed remotely");
                //throw new DCEConnectionException("Empty");
            }
            if (c != '\n' && c != '\0') {
                buf.append(c);
            }
        }
        return buf.toString();
    }

    /**
     * Used to dump bytes into the connection output stream.
     *
     * @param bytes
     * @throws IOException
     */
    void writeBytes(byte[] bytes) throws IOException {
        connection.getOutputStream().write(bytes);
        connection.getOutputStream().flush();
    }
}
