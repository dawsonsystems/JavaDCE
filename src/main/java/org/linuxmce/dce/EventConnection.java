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

import org.linuxmce.dce.binary.DCEMessageSerializer;
import org.linuxmce.dce.util.BlockingQueue;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * This is for ISSUING events to the router, it is the outgoing connection.
 * <p/>
 * It can also receive replies to outgoing messages, see receiveReply
 */
public class EventConnection extends ConnectionMonitor {

    //private static Logger LOG = LoggerFactory.getLogger(EventConnection.class);

    private DCEConnectionInformation parent;

    private MessageSerializer messageSerializer = new DCEMessageSerializer();
    /**
     * Here we add a queue to store messages that have arrived on the event socket.
     * These will tend to be replies to queries sent out on the socket.
     */
    private BlockingQueue queue = new BlockingQueue();


    public EventConnection(Connection connection, MessageDeserializer messageDeserializer) {
        super("event", connection, messageDeserializer);
    }

    public void setup(InputStream read, OutputStream write) throws IOException {
        write.write(("EVENT " + parent.getDeviceId() + "\n").getBytes());

        //Get the 'ok'
        String text = readLine();
        if (text.indexOf("OK") > 0) {
            throw new DCEConnectionException("Error during setup of event connection, returned text '" + text + "'");
        }
        //println "EVENT Connection Opened, Device [${parent.deviceId}]"
    }

    public void onMessage(Message message) {
        queue.enqueue(message);
    }

    /**
     * Return the next message, or one that has already arrived.
     */
    public Message waitForMessage(int milliTimeout) {
        return (Message) queue.dequeue(milliTimeout);
    }

    /**
     * Send a message, don't wait for a reply.
     */
    public void sendMessage(Message message) throws IOException {
        byte[] bytes = messageSerializer.serialize(message);
        writeBytes(("MESSAGE " + bytes.length + "\n").toString().getBytes());
        writeBytes(bytes);
        //Read the response . . . ??
    }

    /**
     * Send a message and wait for the given amount of time for a reply.
     */
    public Message sendMessageAndWaitForReply(Message message, int timeout) throws IOException {
        sendMessage(message);
        return waitForMessage(timeout);
    }

    public DCEConnectionInformation getParent() {
        return parent;
    }

    public void setParent(DCEConnectionInformation parent) {
        this.parent = parent;
    }
}
