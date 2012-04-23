package org.linuxmce.dce;

/**
 *
 */
public interface MessageSerializer {

    /**
     * Convert the given message into a byte stream suitable
     * for sending over the wire to the DCERouter.
     *
     * @param message
     * @return
     */
    byte[] serialize(Message message);
}
