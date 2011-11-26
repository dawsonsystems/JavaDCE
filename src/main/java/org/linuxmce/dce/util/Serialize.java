/*
 *  Copyright (C) 2007-2008 Pluto, Inc., a Delaware Corporation
 *
 *  http://www.plutohome.com
 *
 *  Phone: +1 (877) 758-8648
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

package org.linuxmce.dce.util;

public class Serialize {
    //private static Logger LOG = LoggerFactory.getLogger(Serialize.class);

    public static BinaryData writeInt(BinaryData data, int nValue) {
        byte[] pSmallData = new byte[4];

        pSmallData[3] = (byte) ((nValue & 0xff000000) >> 24);
        pSmallData[2] = (byte) ((nValue & 0x00ff0000) >> 16);
        pSmallData[1] = (byte) ((nValue & 0x0000ff00) >> 8);
        pSmallData[0] = (byte) ((nValue & 0x000000ff));

        //System.out.println("Integer " + (new Integer(nValue).toString()) + " : " + DumpByteArray(pSmallData));
        return data.write(pSmallData);
    }

    public static BinaryData writeString(BinaryData data, String sValue) {
        //System.out.println("String '" + sValue + "' : " + DumpByteArray(sValue.getBytes()));

        //write the string
        data.write(sValue.getBytes());

        //write EOS
        byte[] pSmallData = new byte[1];
        pSmallData[0] = '\0';
        return data.write(pSmallData);
    }

    public static BinaryData writeChar(BinaryData data, char sValue) {
        //System.out.println("Char '" + sValue + "' : " + DumpByteArray(String.valueOf(sValue).getBytes()));
        return data.write(String.valueOf(sValue).getBytes());
    }

    public static BinaryData writeStringFront(BinaryData data, String sValue) {
        //System.out.println("String (front) '" + sValue + "' : " + DumpByteArray(sValue.getBytes()));
        return data.writeFront(sValue.getBytes());
    }

    public static BinaryData writeBlock(BinaryData data, byte[] pValue) {
        //System.out.println("Block: " + dumpByteArray(pValue));

        //write the length of the block
        writeInt(data, pValue.length);

        //write the block
        return data.write(pValue);
    }

    public static int readInt(BinaryData data) {
        byte[] pSmallData = data.read(4);

        int value =
                ((256 + pSmallData[3]) % 256 << 24) +
                        ((256 + pSmallData[2]) % 256 << 16) +
                        ((256 + pSmallData[1]) % 256 << 8) +
                        ((256 + pSmallData[0]) % 256);

        return value;
    }

    public static char readChar(BinaryData data) {
        byte[] pSmallData = data.read(1);
        return (char) pSmallData[0];
    }

    public static String readString(BinaryData data) {
        return data.readString();
    }

    public static byte[] readBlock(BinaryData data) {
        int len = readInt(data);
        byte[] pSmallData = data.read(len);
        return pSmallData;
    }

    public static String dumpByteArray(byte[] bytes) {
        StringBuilder buf = new StringBuilder();

        for (int i = 0; i < bytes.length; ++i) {
            buf.append("0x");
            buf.append(Integer.toHexString((256 + bytes[i]) % 256));
            buf.append(", ");
        }

        return buf.toString();
    }
}