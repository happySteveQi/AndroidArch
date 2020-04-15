//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.steve.commonarch.aes;

public class ByteConvertor {
    private static final String HEX = "0123456789ABCDEF";

    public ByteConvertor() {
    }

    public static String toHex(byte[] buf) {
        if(buf == null) {
            return "";
        } else {
            StringBuffer result = new StringBuffer(2 * buf.length);

            for(int i = 0; i < buf.length; ++i) {
                appendHex(result, buf[i]);
            }

            return result.toString();
        }
    }

    private static void appendHex(StringBuffer sb, byte b) {
        sb.append("0123456789ABCDEF".charAt(b >> 4 & 15)).append("0123456789ABCDEF".charAt(b & 15));
    }

    public static int toInt(byte[] byteArray4) {
        int intValue = 0;
        intValue = intValue | byteArray4[3] & 255;
        intValue <<= 8;
        intValue |= byteArray4[2] & 255;
        intValue <<= 8;
        intValue |= byteArray4[1] & 255;
        intValue <<= 8;
        intValue |= byteArray4[0] & 255;
        return intValue;
    }

    public static long toLong(byte[] byteArray8) {
        long longValue = 0L;
        longValue |= (long)(byteArray8[7] & 255);
        longValue <<= 8;
        longValue |= (long)(byteArray8[6] & 255);
        longValue <<= 8;
        longValue |= (long)(byteArray8[5] & 255);
        longValue <<= 8;
        longValue |= (long)(byteArray8[4] & 255);
        longValue <<= 8;
        longValue |= (long)(byteArray8[3] & 255);
        longValue <<= 8;
        longValue |= (long)(byteArray8[2] & 255);
        longValue <<= 8;
        longValue |= (long)(byteArray8[1] & 255);
        longValue <<= 8;
        longValue |= (long)(byteArray8[0] & 255);
        return longValue;
    }

    public static byte[] toBytes(int intValue) {
        byte[] byteValue = new byte[]{(byte)(intValue & 255), (byte)((intValue & '\uff00') >> 8), (byte)((intValue & 16711680) >> 16), (byte)((intValue & -16777216) >> 24)};
        return byteValue;
    }

    public static byte[] toBytes(long longValue) {
        byte[] byteValue = new byte[]{(byte)((int)(longValue & 255L)), (byte)((int)((longValue & 65280L) >> 8)), (byte)((int)((longValue & 16711680L) >> 16)), (byte)((int)((longValue & 4278190080L) >> 24)), (byte)((int)((longValue & 1095216660480L) >> 32)), (byte)((int)((longValue & 280375465082880L) >> 40)), (byte)((int)((longValue & 71776119061217280L) >> 48)), (byte)((int)((longValue & -72057594037927936L) >> 56))};
        return byteValue;
    }

    public static byte[] subBytes(byte[] buf, int from, int len) {
        byte[] subBuf = new byte[len];

        for(int i = 0; i < len; ++i) {
            subBuf[i] = buf[from + i];
        }

        return subBuf;
    }

    public static String bytesToHexString(byte[] bytes) {
        if(bytes == null) {
            return null;
        } else {
            String table = "0123456789abcdef";
            StringBuilder ret = new StringBuilder(2 * bytes.length);

            for(int i = 0; i < bytes.length; ++i) {
                int b = 15 & bytes[i] >> 4;
                ret.append(table.charAt(b));
                b = 15 & bytes[i];
                ret.append(table.charAt(b));
            }

            return ret.toString();
        }
    }

    public static byte[] hexStringToBytes(String s) {
        if(s == null) {
            return null;
        } else {
            int sz = s.length();
            byte[] ret = new byte[sz / 2];

            for(int i = 0; i < sz; i += 2) {
                ret[i / 2] = (byte)(hexCharToInt(s.charAt(i)) << 4 | hexCharToInt(s.charAt(i + 1)));
            }

            return ret;
        }
    }

    public static int hexCharToInt(char c) {
        if(c >= 48 && c <= 57) {
            return c - 48;
        } else if(c >= 65 && c <= 70) {
            return c - 65 + 10;
        } else if(c >= 97 && c <= 102) {
            return c - 97 + 10;
        } else {
            throw new RuntimeException("invalid hex char '" + c + "'");
        }
    }
}
