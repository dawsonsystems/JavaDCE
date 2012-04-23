package org.linuxmce.dce;

/**
 *
 */
public interface MessageHandler {

    /**
     * Handle the given COommand message.
     * <p/>
     * Throw some type of exception if the message cannot be correctly processed.
     *
     * @param message
     */
    void handleCommand(Message message);
}
