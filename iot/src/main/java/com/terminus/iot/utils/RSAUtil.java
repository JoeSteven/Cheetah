package com.terminus.iot.utils;

import android.text.TextUtils;
import android.util.LruCache;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/*
RSA1024
 */
public class RSAUtil {
    private static final String KEY_ALGORITHM = "RSA";
    private static final String CIPHER_ALGORITHM = "RSA/ECB/PKCS1Padding";
    public static PrivateKey privateKey = null;
    public static PublicKey publicKey = null;
    private static LruCache<String, PublicKey> publicKeys = new LruCache<>(2);

    /** RSA密钥长度必须是64的倍数，在512~65536之间。默认是1024 */
    private static boolean key_init = false;
    /**
     * RSA加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    private static void init(String rsa) throws Exception {
        // 加密
        byte[] keys = ConvertUtil.decryptBASE64(rsa);
        publicKeys.put(rsa, restorePublicKey(keys));
        //解密
//        privateKey = restorePrivateKey(keyMap.get(PRIVATE_KEY));
    }

    public static byte[] EncodeDefault(String rsa, byte[] plain) {
        PublicKey key = publicKeys.get(rsa);
        if (key == null) {
            try {
                init(rsa);
                key = publicKeys.get(rsa);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return RSAEncode(key, plain);
    }


    /**
     * 还原公钥，X509EncodedKeySpec 用于构建公钥的规范
     *
     * @param keyBytes
     * @return
     */
    public static PublicKey restorePublicKey(byte[] keyBytes) {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        try {
            KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
            return factory.generatePublic(x509EncodedKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 还原私钥，PKCS8EncodedKeySpec 用于构建私钥的规范
     *
     * @param keyBytes
     * @return
     */
    public static PrivateKey restorePrivateKey(byte[] keyBytes) {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
                keyBytes);
        try {
            KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
            PrivateKey privateKey = factory
                    .generatePrivate(pkcs8EncodedKeySpec);
            return privateKey;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加密，三步走。
     *
     * @param key
     * @param plainText
     * @return
     */
    public static byte[] RSAEncode(PublicKey key, byte[] plainText) {

        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(plainText);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException
                | InvalidKeyException | IllegalBlockSizeException
                | BadPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 解密，三步走。
     *
     * @param key
     * @param encodedText
     * @return
     */
    public static String RSADecode(PrivateKey key, byte[] encodedText) {

        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(encodedText));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException
                | InvalidKeyException | IllegalBlockSizeException
                | BadPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;

    }

    /** RSA密钥长度必须是64的倍数，在512~65536之间。默认是1024 */
    private static void init2(String publicKeyStr, String privateKeyStr) {
        // 加密
        try{
            if(!TextUtils.isEmpty(publicKeyStr)){
                byte[] key1 = ConvertUtil.decryptBASE64(publicKeyStr);
                publicKey = restorePublicKey(key1);
            }

            if(!TextUtils.isEmpty(privateKeyStr)){
                byte[] key2 = ConvertUtil.decryptBASE64(privateKeyStr);
                privateKey = restorePrivateKey(key2);
            }
        }catch (Exception e){

        }
    }

    /**
     * 加密，三步走。
     *
     * @param key 公钥或者私钥
     * @param plainData 明文二进制
     * @return byte[] 加密后二进制数据
     */
    public static byte[] RSAEncode2(Key key, byte[] plainData) {
        try{
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            int inputLen = plainData.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(plainData, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(plainData, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            byte[] encryptedData = out.toByteArray();
            out.close();
            return encryptedData;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }

    public static byte[] RSADecode2(Key key, byte[] encryptedData) {
        try{
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            int inputLen = encryptedData.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            byte[] decryptedData = out.toByteArray();
            out.close();
            return decryptedData;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
