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
