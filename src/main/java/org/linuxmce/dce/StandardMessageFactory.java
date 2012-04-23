package org.linuxmce.dce;

import java.util.Hashtable;
import java.util.Vector;

/**
 * Will create StandardMessage objects out of the byte stream.
 *
 *
 */
public class StandardMessageFactory implements MessageFactory {
    @Override
    @SuppressWarnings("unchecked")
    public Message createMessage(int deviceIDFrom, int deviceIDTo, int groupID, int id, int messagePriority,
                                 int messageType, int categoryID, int templateID, boolean includeChildren,
                                 int messageBroadcastLevel, int messageRetry, boolean relativeToSender,
                                 int messageExpectedResponse, String devicesList, Hashtable parameters,
                                 Hashtable dataParameters, Vector extraMessages) {

        StandardMessage message = new StandardMessage();

        message.setDeviceIDFrom(deviceIDFrom);
        message.setDeviceIDTo(deviceIDTo);
        message.setGroupID(groupID);
        message.setId(id);
        message.setPriority(StandardMessage.Priority.fromValue(messagePriority));
        message.setType(StandardMessage.Type.fromValue(messageType));
        message.setCategoryID(categoryID);
        message.setTemplateID(templateID);
        message.setIncludeChildren(includeChildren);
        message.setBroadcastLevel(StandardMessage.BroadcastLevel.fromValue(messageBroadcastLevel));
        message.setRetry(StandardMessage.Retry.fromValue(messageRetry));
        message.setRelativeToSender(relativeToSender);
        message.setExpectedResponse(StandardMessage.ExpectedResponse.fromValue(messageExpectedResponse));
        message.setDevicesList(devicesList);

        for(Object obj: parameters.keySet()) {
            Integer num = (Integer) obj;
            message.getParameters().put(DCE.CommandParam.fromValue(num.intValue()), (String) parameters.get(num));
        }

        for(Object obj: dataParameters.keySet()) {
            Integer num = (Integer) obj;
            message.getDataParameters().put(DCE.CommandParam.fromValue(num.intValue()), (byte[]) dataParameters.get(num));
        }

        message.getExtraMessages().addAll(extraMessages);

        return message;
    }
}
