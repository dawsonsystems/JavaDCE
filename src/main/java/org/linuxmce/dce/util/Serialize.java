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