package org.linuxmce.dce;

/**
 * Indicate that something has gone wrong during the parsing or interpreting of a DCE
 * Message.
 */
public class DCEConnectionException extends RuntimeException {

    public DCEConnectionException(String message) {
        super(message);
    }

    public DCEConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
