package com.terminus.iotextension.util;

import com.terminus.iot.utils.base64.BASE64Decoder;

/**
 * description 异或加密
 * author      kai.mr
 * create      2019-06-05 15:50
 **/
public class OrUtils {
    /**
     * 不固定key的方式
     * <p>
     * 加密实现
     *
     * @param bytes
     * @return
     */
    public static byte[] encrypt(byte[] bytes, String privateKey) {
        byte[] keyBytes;
        try{
            keyBytes = (new BASE64Decoder()).decodeBuffer(privateKey);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

        if (bytes == null || keyBytes == null || keyBytes.length<1) {
            return null;
        }
        int len = bytes.length;
        int key = keyBytes[0] & 0xF1;
        //int key = 0x12;
        for (int i = 0; i < len; i++) {
            bytes[i] = (byte) (bytes[i] ^ key);
            key = bytes[i];
        }
        return bytes;
    }

    /**
     * 不固定key的方式
     * <p>
     * 解密实现
     *
     * @param bytes
     * @return
     */
    public static byte[] decrypt(byte[] bytes, String publicKey) {
        byte[] keyBytes;
        try{
            keyBytes = (new BASE64Decoder()).decodeBuffer(publicKey);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }


        if (bytes == null || keyBytes == null || keyBytes.length<1) {
            return null;
        }
        int len = bytes.length;
        int key = keyBytes[0] & 0xF1;
        for (int i = len - 1; i > 0; i--) {
            bytes[i] = (byte) (bytes[i] ^ bytes[i - 1]);
        }
        bytes[0] = (byte) (bytes[0] ^ key);
        return bytes;
    }
}