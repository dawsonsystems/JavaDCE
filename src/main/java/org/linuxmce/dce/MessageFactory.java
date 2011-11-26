package org.linuxmce.dce;

import java.util.Hashtable;
import java.util.Vector;

/**
 * This is the contract for making Message instances.
 *
 * This is compatible with the JavaME subset of the libs.
 */
public interface MessageFactory {

    Message createMessage(int deviceIDFrom,
            int deviceIDTo,
            int groupID,
            int id,
            int messagePriority,
            int messageType,
            int categoryID,
            int templateID,
            boolean includeChildren,
            int messageBroadcastLevel,
            int messageRetry,
            boolean relativeToSender,
            int messageExpectedResponse,
            String devicesList,
            Hashtable parameters,
            Hashtable dataParameters,
            Vector extraMessages);
}
