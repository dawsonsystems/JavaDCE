package org.linuxmce.dce.util;

import java.util.Arrays;

/**
 * A generic wrapper around a byte array.
 */
public class BinaryData {

    public static int DATA_BLOCK = 4096;
    byte[] data = null;
    int position = 0;

    public BinaryData() {
        data = new byte[DATA_BLOCK];
    }

    public BinaryData(byte[] pData) {
        data = pData;
    }

    public byte[] getData() {
        return data;
    }

    public int getPosition() {
        return position;
    }

    private void increaseSize() {
        data = Arrays.copyOf(data, data.length + DATA_BLOCK);
    }

    public BinaryData write(byte[] pSmallData) {
        if (position + pSmallData.length > data.length) {
            increaseSize();
        }

        for (int i = 0; i < pSmallData.length; ++i) {
            data[position + i] = pSmallData[i];
        }

        position += pSmallData.length;

        return this;
    }

    public BinaryData writeFront(byte[] pSmallData) {
        int nNewLength = data.length;
        if (position + pSmallData.length > data.length) {
            nNewLength = position + pSmallData.length;
        }

        byte[] pNewData = new byte[nNewLength];

        for (int i = 0; i < pSmallData.length; ++i) {
            pNewData[i] = pSmallData[i];
        }

        for (int j = 0; j < position; ++j) {
            pNewData[pSmallData.length + j] = data[j];
        }

        data = pNewData;
        position += pSmallData.length;

        return this;
    }

    public byte[] read(int nBytes) {
        byte[] pSmallData = new byte[nBytes];

        if (position + nBytes < data.length) {
            for (int i = 0; i < nBytes; ++i) {
                pSmallData[i] = data[position + i];
            }

            position += nBytes;
        } else {
            System.out.println("Cannot serialize object from position " +
                    (new Integer(position)).toString() + " bytes to read " +
                    (new Integer(nBytes)).toString());
        }

        return pSmallData;
    }

    public String readString() {
        String sResult = new String();

        for (int i = position; i < data.length; i++) {
            if (data[i] == '\0') {
                position = i + 1;
                break;
            } else {
                sResult += (char) data[i];
            }
        }

        return sResult;
    }

    public void resetPosition() {
        position = 0;
    }
}