package com.terminus.iotextension.util;

import com.terminus.iot.utils.AESUtil;
import com.terminus.iot.utils.RSAUtil;
import com.terminus.iot.utils.base64.BASE64Decoder;
import com.terminus.iot.utils.base64.BASE64Encoder;
import com.terminus.iotextension.bean.QRInfo;

import java.security.PublicKey;

/**
 * description AIOT二维码生成及解析相关规则
 * author      kai.mr
 * create      2019-05-16 19:41
 **/
public class QRRuleUtil {
    /**
     * 生成二维码字符串
     * @param timeMs 二维码有效期的截止时间 (汇付传入 timeNowMs+5minMs)
     * @param token 用户信息生成的token
     * @param customInfo 二维码自定义信息部分(example 汇付的手机信息)
     * @param aesKey 二维码加密函数 length=8 // 预定义设置/ 接口提供
     * @return 二维码字符串
     */
    public static String generateQRStr(long timeMs, String token, String customInfo, String aesKey) throws Exception{
        byte[] timeBytes = String.valueOf(timeMs).getBytes();
        byte[] tokenBytes = (new BASE64Decoder()).decodeBuffer(token);
        byte[] customBytes = customInfo.getBytes();
        byte[] splitChar = {'-'};
        byte[] qrBytes = byteMerger(timeBytes, splitChar);
        qrBytes = byteMerger(qrBytes, tokenBytes);
        qrBytes = byteMerger(qrBytes, splitChar);
        qrBytes = byteMerger(qrBytes, customBytes);
        qrBytes = AESUtil.encrypt(qrBytes, aesKey.getBytes());
        return new BASE64Encoder().encode(qrBytes);
    }

    /**
     * 合并字符数组为新的字符数组
     * @param bt1 字符数组1
     * @param bt2 字符数组2
     * @return 合并后的字符数组
     */
    private static byte[] byteMerger(byte[] bt1, byte[] bt2){
        byte[] bt3 = new byte[bt1.length+bt2.length];
        System.arraycopy(bt1, 0, bt3, 0, bt1.length);
        System.arraycopy(bt2, 0, bt3, bt1.length, bt2.length);
        return bt3;
    }

    /**
     * 解析二维码到可识别数据
     * @param qrStr 二维码字符串
     * @param aesKey 对称加密aesKey
     * @param rsaPublicKeyStr rsa公钥
     * @return
     * @throws Exception
     */
    public static QRInfo parseFromQrStr(String qrStr, String aesKey, String rsaPublicKeyStr) throws Exception{
        byte[] qrBytes = new BASE64Decoder().decodeBuffer(qrStr);
        qrBytes = AESUtil.decrypt(qrBytes, aesKey.getBytes());
        byte[] qrTimeBytes = null;
        byte[] tokenBytes = null;
        byte[] customBytes = null;
        for(int i=0; i<qrBytes.length; i++){
            byte ch = qrBytes[i];
            if(ch == '-'){
                if(qrTimeBytes == null){
                    qrTimeBytes = new byte[i];
                    System.arraycopy(qrBytes, 0, qrTimeBytes, 0, i);
                    continue;
                }
                if(tokenBytes == null){
                    tokenBytes = new byte[i-1-qrTimeBytes.length];
                    System.arraycopy(qrBytes, qrTimeBytes.length+1, tokenBytes, 0, i-1-qrTimeBytes.length);
                    customBytes = new byte[qrBytes.length-1-i];
                    System.arraycopy(qrBytes, i+1, customBytes, 0, qrBytes.length-1-i);
                    break;
                }
            }
        }
        long qrTime = Long.valueOf(new String(qrTimeBytes));
        String customInfo = new String(customBytes);

        byte[] publicKeyBytes = new BASE64Decoder().decodeBuffer(rsaPublicKeyStr);
        PublicKey publicKey = RSAUtil.restorePublicKey(publicKeyBytes);
        tokenBytes = RSAUtil.RSADecode2(publicKey, tokenBytes);
        String token = new String(tokenBytes);

        QRInfo qrInfo = new QRInfo();
        qrInfo.setCustomInfo(customInfo);
        qrInfo.setQrValidTime(Long.valueOf(qrTime));
        String[] infos = token.split("-");
        if(infos.length != 7)
            throw new Exception("token info num error");
        qrInfo.setPermissionId(Long.valueOf(infos[0]));
        qrInfo.setPersonId(Long.valueOf(infos[1]));
        qrInfo.setType(infos[2]);
        qrInfo.setName(infos[3]);
        if(!infos[4].equals("null"))
            qrInfo.setWeight(Integer.valueOf(infos[4]));
        if(!infos[5].equals("null"))
            qrInfo.setHeight(Integer.valueOf(infos[5]));
        qrInfo.setValidTime(Long.valueOf(infos[6]));
        return qrInfo;
    }

}
