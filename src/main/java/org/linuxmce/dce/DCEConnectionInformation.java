package org.linuxmce.dce;


import java.io.IOException;

/**
 * A connection to the DCE Router.
 */
public interface DCEConnectionInformation {

    public static final int DCE_PORT = 3450;

    public String getDeviceId();
    public void connect() throws IOException ;
}