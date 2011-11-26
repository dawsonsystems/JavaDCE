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


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Represent the command connection to the DCE Router.
 * This is the inbound message socket, and will asynchronously
 * process messages and hand them off to the MessageHandler.
 */
public class CommandConnection extends ConnectionMonitor {

//    private static Logger LOG = LoggerFactory.getLogger(CommandConnection.class);

    private MessageHandler commandHandler;
    private DCEConnectionInformation parent;

    public CommandConnection(Connection connection, MessageDeserializer messageDeserializer) {
        super("event", connection, messageDeserializer);
    }

    public void setup(InputStream read, OutputStream write) throws IOException {
        String mesg = "COMMAND " + parent.getDeviceId() + "\n";

        //LOG.debug("Setting up Command Connection : " + mesg);
        write.write(mesg.getBytes());

        //Get the 'ok'
        String text = readLine();
        if (!text.equals("OK")) {
            throw new IllegalArgumentException("Return text is no 'OK', is " + text);
        }
        //LOG.info("Command Connected : " + text);
    }

    /**
     * Called whenever we receive a message
     *
     * @param message
     */
    public void onMessage(Message message) {
        //if (LOG.isDebugEnabled()) {
        //    LOG.debug("Command Handler : " + message.toString());
        //}
        commandHandler.handleCommand(message);
    }

    public void setParent(DCEConnectionInformation parent) {
        this.parent = parent;
    }

    public void setCommandHandler(MessageHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

}
