package com.terminus.iot.utils;

/**
 * Description:
 * author:Joey
 * date:2018/9/18
 */


import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


/*
 *      hamcsha1(mac, RSA公钥)
 */
public class Sha1Util {
    public static String EncodeDefault(String rsaKey, byte[] data){
        try {
            byte[] key = ConvertUtil.decryptBASE64(rsaKey);
            return hamcsha1(data, key);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] EncodeDefaultForBytes(String rsaKey, byte[] data) {
        try {
            byte[] key = ConvertUtil.decryptBASE64(rsaKey);
            //System.out.println(ConvertUtil.byte2hex(key));
            SecretKeySpec signingKey = new SecretKeySpec(key, "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            byte[] ret = mac.doFinal(data);
            System.out.println(ConvertUtil.byte2hex(ret));
            return ret;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    public static String hamcsha1(byte[] data, byte[] key)
    {
        try {
            SecretKeySpec signingKey = new SecretKeySpec(key, "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            return ConvertUtil.byte2hex(mac.doFinal(data));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

}

