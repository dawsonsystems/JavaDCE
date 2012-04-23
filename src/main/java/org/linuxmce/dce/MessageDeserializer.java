package org.linuxmce.dce;

import org.linuxmce.dce.util.BinaryData;

/**
 *
 */
public interface MessageDeserializer {

    /**
     *
     */
    Message deserialize(BinaryData source);
}
