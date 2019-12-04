package com.terminus.iot.utils;

import android.util.Log;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author ngh
 * AES128 算法
 *
 * CBC 模式
 *
 * PKCS7Padding 填充模式
 *
 * CBC模式需要添加一个参数iv
 *
 * 介于java 不支持PKCS7Padding，只支持PKCS5Padding 但是PKCS7Padding 和 PKCS5Padding 没有什么区别
 * 要实现在java端用PKCS7Padding填充，需要用到bouncycastle组件来实现
 */
public class AESUtil {
    // 算法名称
    private static final String KEY_ALGORITHM = "AES";
    // 加解密算法/模式/填充方式
    private static final String algorithmStr = "AES/CBC/PKCS7Padding";
//    private static final String algorithmStrDe = "AES/CBC/NoPadding";
    //
    private static Key key;
    private static Cipher cipherDe,cipherEn,cipherQr;
    private static Map<String,Cipher> cipherMap = new HashMap<>();
    private static Map<String,Key> keyMap = new HashMap<>();
    public static final String QR = "qr";
    public static final String NORMAL = "normal";

    //byte[] iv = { 0x30, 0x31, 0x30, 0x32, 0x30, 0x33, 0x30, 0x34, 0x30, 0x35, 0x30, 0x36, 0x30, 0x37, 0x30, 0x38 };
    private static final byte[] iv = {0xA, 1, 0xB, 5, 4, 0xF, 7, 9, 0x17, 3, 1, 6, 8, 0xC, 0xD, 91};

    private static void init(byte[] keyBytes,String type) {
        try {
            // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
            int base = 16;
            if (keyBytes.length % base != 0) {
                int groups = keyBytes.length / base + (keyBytes.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyBytes, 0, temp, 0, keyBytes.length);
                keyBytes = temp;
            }
            // 初始化
            Security.addProvider(new BouncyCastleProvider());
            // 转化成JAVA的密钥格式
            key = new SecretKeySpec(keyBytes, KEY_ALGORITHM);

            switch (type) {
                case QR:
                    if (cipherMap.get("cipherQr") == null) {
                        cipherQr = Cipher.getInstance(algorithmStr,"BC");
                        cipherQr.init(Cipher.DECRYPT_MODE,key, new IvParameterSpec(iv));
                        cipherMap.put("cipherQr",cipherQr);
                        keyMap.put("keyQr",key);
                    } else {
                        if (!Arrays.equals(keyMap.get("keyQr").getEncoded(), key.getEncoded())) {
                            keyMap.remove("keyQr");
                            keyMap.put("keyQr",key);
                            cipherQr.init(Cipher.DECRYPT_MODE,key, new IvParameterSpec(iv));
                            cipherMap.remove("cipherQr");
                            cipherMap.put("cipherQr",cipherQr);
                        }
                        cipherQr = cipherMap.get("cipherQr");
                    }
                    break;
                case NORMAL:
                default:
                    if (cipherMap.get("cipherDe") == null && cipherMap.get("cipherEn") == null) {
                        cipherDe = Cipher.getInstance(algorithmStr,"BC");
                        cipherDe.init(Cipher.DECRYPT_MODE,key, new IvParameterSpec(iv));
                        cipherMap.put("cipherDe",cipherDe);

                        cipherEn = Cipher.getInstance(algorithmStr,"BC");
                        cipherEn.init(Cipher.ENCRYPT_MODE,key, new IvParameterSpec(iv));
                        cipherMap.put("cipherEn",cipherEn);

                        keyMap.put("key",key);
                    } else {
                        if (!Arrays.equals(keyMap.get("key").getEncoded(), key.getEncoded())) {
                            keyMap.remove("key");
                            keyMap.put("key",key);
                            cipherDe.init(Cipher.DECRYPT_MODE,key, new IvParameterSpec(iv));
                            cipherEn.init(Cipher.ENCRYPT_MODE,key, new IvParameterSpec(iv));
                            cipherMap.remove("cipherDe");
                            cipherMap.remove("cipherEn");
                            cipherMap.put("cipherDe",cipherDe);
                            cipherMap.put("cipherEn",cipherEn);
                        }

                        cipherDe = cipherMap.get("cipherDe");
                        cipherEn = cipherMap.get("cipherEn");
                    }
                    break;
            }
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public static byte[] encrypt(byte[] content, byte[] keyBytes) {
        return encrypt(content, keyBytes,"");
    }

    /**
     * 加密方法
     *
     * @param content  要加密的字符串
     * @param keyBytes 加密密钥
     * @return
     */
    public static byte[] encrypt(byte[] content, byte[] keyBytes,String type) {
        byte[] encryptedText = null;
        init(keyBytes,type);
        try {
            encryptedText = cipherEn.doFinal(content);
        } catch (Exception e) {
            Log.e(AESUtil.class.getSimpleName(),content + "," + keyBytes + "," + e.getMessage());
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return encryptedText;
    }

    public static synchronized byte[] decrypt(byte[] encryptedData, byte[] keyBytes) {
        return decrypt(encryptedData, keyBytes,"");
    }

    /**
     * 解密方法
     *
     * @param encryptedData 要解密的字符串
     * @param keyBytes      解密密钥
     * @return
     */
    public static byte[] decrypt(byte[] encryptedData, byte[] keyBytes,String type) {
        byte[] encryptedText = null;
        init(keyBytes,type);
        Log.i("MQTT", "encryptedData:" + Arrays.toString(encryptedData) + "  " +
                "encryptedData length:" + encryptedData.length + "  " +
                "key:" + Arrays.toString(keyBytes) + "  " + "type:" + type);
        try {
            if (QR.equals(type)) {
                encryptedText = cipherQr.doFinal(encryptedData);
            } else {
                encryptedText = cipherDe.doFinal(encryptedData);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return encryptedText;
    }

    public static byte[] getNewAesKey() {
        //生成新的密钥
        try {
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            SecretKey deskey = keygen.generateKey();
            byte[] aes_key = new byte[16];
            System.arraycopy(deskey.getEncoded(), 0, aes_key, 0, 16);
            return aes_key;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "1234567890123456".getBytes();
        }
    }
}
