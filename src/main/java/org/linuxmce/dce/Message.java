package org.linuxmce.dce;

import java.util.Enumeration;

/**
 * Basic contract of a message, so it can be serialized to a byte stream
 *
 * See jdce-se and jdce-me for implementations.
 *
 * This is to enable differing implementations of Message for the SE and ME platforms.
 */
public interface Message {
    public final static int MAGIC_START = 1234;
    public final static int MAGIC_STOP = 6789;

    int getDeviceIDFrom();
    int getDeviceIDTo();
    int getGroupID();
    int getId();
    int getMessagePriority();
    int getMessageType();
    int getCategoryID();
    int getTemplateID();
    boolean isIncludeChildren();
    int getMessageBroadcastLevel();
    int getMessageRetry();
    boolean isRelativeToSender();
    int getMessageExpectedResponse();
    String getDevicesList();

    int getParameterCount();

    Enumeration getParameterIds();
    String getParameterValue(int index);

    int getDataParameterCount();
    Enumeration getDataParameterIds();
    byte[] getDataParameterValue(int index);

    int getExtraMessageCount();
    Message getExtraMessage(int index);
}
