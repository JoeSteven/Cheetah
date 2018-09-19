package com.terminus.iot.utils;

/**
 * Description:
 * author:Joey
 * date:2018/9/18
 */



import com.terminus.iot.utils.base64.BASE64Decoder;
import com.terminus.iot.utils.base64.BASE64Encoder;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

public class ConvertUtil {
    private static ByteBuffer intBuf = ByteBuffer.allocate(4);
    private static ByteBuffer shortBuf = ByteBuffer.allocate(2);
    public  static ByteOrder order = ByteOrder.LITTLE_ENDIAN;
    static {
        intBuf.order(order);
        shortBuf.order(order);
    }


    public static byte[] byteMerger(byte[] bt1, byte[] bt2){
        byte[] bt3 = new byte[bt1.length+bt2.length];
        System.arraycopy(bt1, 0, bt3, 0, bt1.length);
        System.arraycopy(bt2, 0, bt3, bt1.length, bt2.length);
        return bt3;
    }

    public static byte[] hex2byte(String str){
        if(str == null || str.trim().equals("")) {
            return new byte[0];
        }

        byte[] bytes = new byte[str.length() / 2];
        for(int i = 0; i < str.length() / 2; i++) {
            String subStr = str.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }

        return bytes;
    }

    public static String byte2hex(byte[] b)
    {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b!=null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString().toLowerCase();
    }

    public static byte[] getBytes (char[] chars) {
        Charset cs = Charset.forName ("UTF-8");
        CharBuffer cb = CharBuffer.allocate (chars.length);
        cb.put (chars);
        cb.flip ();
        ByteBuffer bb = cs.encode (cb);

        return bb.array();
    }

    public static char[] getChars (byte[] bytes) {
        Charset cs = Charset.forName ("UTF-8");
        ByteBuffer bb = ByteBuffer.allocate (bytes.length);
        bb.put (bytes);
        bb.flip ();
        CharBuffer cb = cs.decode (bb);

        return cb.array();
    }

    //byte 数组与 int 4的相互转换
    public static byte[] intToBytes(int x) {
        intBuf.clear();
        intBuf.putInt(0, x);
        return intBuf.array();
    }

    public static int bytesToInt(byte[] bytes) {
        intBuf.clear();
        intBuf.put(bytes, 0, bytes.length);
        intBuf.flip();//need flip
        return intBuf.getInt();
    }

    //byte 数组与 short2的相互转换
    public static byte[] shortToBytes(short x) {
        shortBuf.clear();
        shortBuf.putShort(0, x);
        return shortBuf.array();
    }

    public static short bytesToShort(byte[] bytes) {
        shortBuf.clear();
        shortBuf.put(bytes, 0, bytes.length);
        shortBuf.flip();//need flip
        return shortBuf.getShort();
    }


    //解码返回byte
    public static byte[] decryptBASE64(String key) throws Exception {
        return (new BASE64Decoder()).decodeBuffer(key);
    }

    //编码返回字符串
    public static String encryptBASE64(byte[] key) throws Exception {
        return (new BASE64Encoder()).encodeBuffer(key);
    }

    public static byte[] reverse(byte[] b)
    {
        byte[] b1 = new byte[b.length];
        int index = 0;
        for(int i=b.length-1; i>=0; --i)
        {
            b1[index] = b[i];
            ++index;
        }
        return b1;
    }

}

