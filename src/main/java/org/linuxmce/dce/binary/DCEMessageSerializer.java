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
package org.linuxmce.dce.binary;

import org.linuxmce.dce.Message;
import org.linuxmce.dce.MessageSerializer;
import org.linuxmce.dce.util.BinaryData;
import org.linuxmce.dce.util.Serialize;

import java.util.Enumeration;


/**
 * Implement the DCE wire protocol
 * <p/>
 * Object to wire conversion.
 */
public class DCEMessageSerializer implements MessageSerializer {

    //private static Logger LOG = LoggerFactory.getLogger(DCEMessageSerializer.class);

    public byte[] serialize(Message message) {

        BinaryData data = new BinaryData();

        Serialize.writeInt(data, Message.MAGIC_START);
        Serialize.writeInt(data, message.getDeviceIDFrom());
        Serialize.writeInt(data, message.getDeviceIDTo());
        Serialize.writeInt(data, message.getGroupID());
        Serialize.writeInt(data, message.getId());
        Serialize.writeInt(data, message.getMessagePriority());
        Serialize.writeInt(data, message.getMessageType());
        Serialize.writeInt(data, message.getCategoryID());
        Serialize.writeInt(data, message.getTemplateID());
        Serialize.writeChar(data, message.isIncludeChildren() ? '1' : '0');
        Serialize.writeInt(data, message.getMessageBroadcastLevel());
        Serialize.writeInt(data, message.getMessageRetry());
        Serialize.writeChar(data, message.isRelativeToSender() ? '1' : '0');
        Serialize.writeInt(data, message.getMessageExpectedResponse());
        Serialize.writeString(data, message.getDevicesList());

        Serialize.writeInt(data, message.getParameterCount());
        Enumeration en = message.getParameterIds();
        while(en.hasMoreElements()) {
            Integer i = (Integer) en.nextElement();
            Serialize.writeInt(data, i.intValue());
            Serialize.writeString(data, message.getParameterValue(i.intValue()));
        }

        Serialize.writeInt(data, message.getDataParameterCount());
        en = message.getDataParameterIds();
        while(en.hasMoreElements()) {
            Integer i = (Integer) en.nextElement();
            Serialize.writeInt(data, i.intValue());
            Serialize.writeBlock(data, message.getDataParameterValue(i.intValue()));
        }
        
        Serialize.writeInt(data, message.getExtraMessageCount());
        for (int i = 0; i < message.getExtraMessageCount(); i++) {
             byte[] serialisedEmbeddedMessage = serialize(message.getExtraMessage(i));
            //This will also put the length of the embedded message into the stream.
            Serialize.writeBlock(data, serialisedEmbeddedMessage);
        }

        Serialize.writeInt(data, Message.MAGIC_STOP);

        return data.getData();
    }
}