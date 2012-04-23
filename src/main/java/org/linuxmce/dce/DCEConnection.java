package org.linuxmce.dce;

import org.linuxmce.dce.binary.DCEMessageDeserializer;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Connect to a DCE Router, using Java SE as the platform.
 */
public class DCEConnection implements DCEConnectionInformation {

    public static final int DCE_PORT = 3450;

    private static Logger LOG = Logger.getLogger(DCEConnectionInformation.class.getName());

    private CommandConnection commandConnection;
    private EventConnection eventConnection;
    private String deviceId;


    public String getDeviceId() {
        return deviceId;
    }


    public void connect() throws IOException {
        commandConnection.connect();
        eventConnection.connect();
    }

    /**
     * Create a connection the the DCE router at the given host name
     *
     * Uses a java.io.Socket to make the connection.
     *
     * @param routerName The hostname/ ip of the router.
     * @param deviceId THe current Device ID
     * @param commandHandler The handler that incoming command message will be routed.
     * @return
     * @throws java.io.IOException
     */
    public static DCEConnection create(String routerName, String deviceId, MessageHandler commandHandler) throws IOException {
        return create(deviceId, commandHandler,
                new SocketConnection(routerName, DCE_PORT),
                new SocketConnection(routerName, DCE_PORT));
    }

    /**
     * Use this if you want to create your own special connections to the router
     * @param deviceId
     * @param commandHandler
     * @param eventConnection
     * @param commandConnection
     * @return
     */
    public static DCEConnection create(String deviceId, MessageHandler commandHandler,
                                       Connection eventConnection, Connection commandConnection) {
        DCEConnection dce = new DCEConnection();
        dce.deviceId = deviceId;

        MessageDeserializer messageDeserializer = new DCEMessageDeserializer(new StandardMessageFactory());
        
        dce.eventConnection = new EventConnection(eventConnection, messageDeserializer);
        dce.eventConnection.setParent(dce);

        dce.commandConnection = new CommandConnection(commandConnection, messageDeserializer);
        dce.commandConnection.setParent(dce);
        dce.commandConnection.setCommandHandler(commandHandler);

        LOG.info("Setup connection to DCE Router");

        return dce;
    }

    /**
     * Return the next message, or one that has already arrived.
     */
    public StandardMessage waitForMessage(int milliTimeout) {
        return (StandardMessage) eventConnection.waitForMessage(milliTimeout);
    }

    /**
     * Send a message, don't wait for a reply.
     */
    public void sendMessage(Message message) throws IOException {
        eventConnection.sendMessage(message);
    }

    /**
     * Send a message and wait for the given amount of time for a reply.
     */
    public StandardMessage sendMessageAndWaitForReply(Message message, int timeout) throws IOException {
        return (StandardMessage) eventConnection.sendMessageAndWaitForReply(message, timeout);
    }
}
