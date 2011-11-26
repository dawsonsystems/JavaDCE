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

import org.linuxmce.dce.DCEConnectionException;
import org.linuxmce.dce.Message;
import org.linuxmce.dce.MessageDeserializer;
import org.linuxmce.dce.MessageFactory;
import org.linuxmce.dce.util.BinaryData;
import org.linuxmce.dce.util.Serialize;

import java.util.Hashtable;
import java.util.Vector;

/**
 * Implement the DCE wire protocol
 * <p/>
 * Wire to object conversion.
 */
public class DCEMessageDeserializer implements MessageDeserializer {

    //private static Logger LOG = LoggerFactory.getLogger(DCEMessageDeserializer.class);

    private MessageFactory messageFactory;

    public DCEMessageDeserializer(MessageFactory messageFactory) {
        this.messageFactory = messageFactory;
    }

    public Message deserialize(BinaryData data) {
        int magicStart = Serialize.readInt(data);
        if (magicStart != Message.MAGIC_START) {
            //LOG.error("Malformated message: magic number for start is not respected!");
            throw new DCEConnectionException("Malformated message: magic number for start is not respected!");
        }
        Message message;
        try {
             message = messageFactory.createMessage(
                    //deviceIdTo
                Serialize.readInt(data),
                    //deviceIdFrom
                Serialize.readInt(data),
                    //groupId
                Serialize.readInt(data),
                    //Message ID
                Serialize.readInt(data),
                    //message priority
                Serialize.readInt(data),
                    //Message type
                Serialize.readInt(data),
                    //category id
                Serialize.readInt(data),
                    //templateID
                Serialize.readInt(data),
                    //include children
                Serialize.readChar(data) != '0',
                    //message broadcast level
                Serialize.readInt(data),
                    //message retry
                Serialize.readInt(data),
                    //relative to sender
                Serialize.readChar(data) != '0',
                    //expected response
                Serialize.readInt(data),
                    //devices list
                Serialize.readString(data),
                    //string params
                getStringParameters(data),
                    //byte params
                getDataParameters(data),
                    //extra messages
                getExtraMessages(data));


        }
        catch (Exception e) {
            //LOG.error("Failed to parse message.", e);
            throw new DCEConnectionException("Failed to parse message.", e);
        }

        int magicEnd = Serialize.readInt(data);
        if (magicEnd != Message.MAGIC_STOP) {
            //LOG.error("Malformated message: magic number for end message is not respected!");
            throw new DCEConnectionException("Malformated message: magic number for end message is not respected!");
        }

         return message;
    }

    private Vector getExtraMessages(BinaryData data) {
        Vector ret = new Vector();
        int nExtraMessages = Serialize.readInt(data);

        for (int i = 0; i < nExtraMessages; ++i) {
            int messageLength = Serialize.readInt(data);
            Message embed = deserialize(data);
            ret.add(embed);
        }
        return ret;
    }

    private Hashtable getDataParameters(BinaryData data) {
        Hashtable ret = new Hashtable();

        int nDataParameters = Serialize.readInt(data);
        for (int i = 0; i < nDataParameters; ++i) {
            int nKey = Serialize.readInt(data);
            byte[] pValue = Serialize.readBlock(data);
            ret.put(new Integer(nKey), pValue);
        }
        return ret;
    }



    private Hashtable getStringParameters(BinaryData data) {
        Hashtable ret = new Hashtable();

        int nStringParameters = Serialize.readInt(data);
        for (int i = 0; i < nStringParameters; ++i) {
            int nKey = Serialize.readInt(data);
            String sValue = Serialize.readString(data);
            ret.put(new Integer(nKey), sValue);
        }
        return ret;
    }
}