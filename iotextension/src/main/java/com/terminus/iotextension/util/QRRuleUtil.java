package com.terminus.iotextension.util;

import com.terminus.iot.utils.AESUtil;
import com.terminus.iot.utils.base64.BASE64Decoder;
import com.terminus.iotextension.bean.QRInfo;

/**
 * description AIOT二维码生成及解析相关规则
 * author      kai.mr
 * create      2019-05-16 19:41
 **/
public class QRRuleUtil {

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
        int lastIndex = 0;
        for(int i=0; i<qrBytes.length; i++){
            byte ch = qrBytes[i];
            if(ch == '-'){
                if(qrTimeBytes == null){
                    qrTimeBytes = new byte[i];
                    System.arraycopy(qrBytes, 0, qrTimeBytes, 0, i);
                    continue;
                }
                lastIndex = i;
            }
        }

        tokenBytes = new byte[lastIndex-1-qrTimeBytes.length];
        System.arraycopy(qrBytes, qrTimeBytes.length+1, tokenBytes, 0, lastIndex-1-qrTimeBytes.length);
        customBytes = new byte[qrBytes.length-1-lastIndex];
        System.arraycopy(qrBytes, lastIndex+1, customBytes, 0, qrBytes.length-1-lastIndex);

        long qrTime = Long.valueOf(new String(qrTimeBytes));
        String customInfo = new String(customBytes);
        System.out.println(qrTime);
        System.out.println(customInfo);

        /*
        byte[] publicKeyBytes = new BASE64Decoder().decodeBuffer(rsaPublicKeyStr);
        PublicKey publicKey = RsaTools.restorePublicKey(publicKeyBytes);
        tokenBytes = RsaTools.RSADecode(publicKey, tokenBytes);
        */
        tokenBytes = OrUtils.decrypt(tokenBytes, rsaPublicKeyStr);
        String token = new String(tokenBytes);
        System.out.println(token);

        QRInfo qrInfo = new QRInfo();
        qrInfo.setCustomInfo(customInfo);
        qrInfo.setQrValidTime(Long.valueOf(qrTime));
        String[] infos = token.split("-");
        if(infos.length != 7)
            throw new Exception("token info num error");
        qrInfo.setPermissionId(Long.valueOf(infos[0]));
        qrInfo.setPersonId(Long.valueOf(infos[1]));
        //0/1/2 //user/visitor/blacklist

        String personType = "unknown";
        switch (infos[2]){
            case "0":
                personType = "user";
                break;
            case "1":
                personType = "visitor";
                break;
            case "2":
                personType = "blacklist";
                break;
        }
        qrInfo.setType(personType);
        qrInfo.setName(infos[3]);
        if(!infos[4].equals("null"))
            qrInfo.setWeight(Integer.valueOf(infos[4]));
        if(!infos[5].equals("null"))
            qrInfo.setHeight(Integer.valueOf(infos[5]));
        qrInfo.setValidTime(Long.valueOf(infos[6]));
        return qrInfo;
    }

}
