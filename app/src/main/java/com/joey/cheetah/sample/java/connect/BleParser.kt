package com.joey.cheetah.sample.java.connect

/**
 * Description:
 * author:Joey
 * date:2018/11/5
 */

class BleParser {
    /**
     *  返回 ***M12301$$$28@@@000076C8@@@260039001450465252333720100061390D20FE8434F7CB8D565CC000123456MSUCCV6217
     *  ***M12301$$$28@@@000076C8@@@ 为头
     *  260039001450465252333720100061390D20FE8434F7CB8D565CC000123456MSUCCV6217 为内容
     *  有用的部分
     *  头M12301 解析设备的型号M-123-01, 有用的为中间的三位暂计为a b c
     *  a-读头：0-ST95,1-ST25,2-NXP
     *  b-蓝牙模块：0-BM77,1-BM78,2-0906
     *  c-网络模块：0-GPRS,1-WIFI,2-ETH
     *
     *  内容0位为开始
     *  0-23位 位 序列号
     *  第28为设备类型，6-touch，else 其他
     *
     *  文档：
     *  1、260039001450465252333720 芯片Chip id
     *  2、24位是电池1（高）,3（中）,5（低）,9（没电）
     *  3、25到26位（0x00到0xFF）表示单次授权的index
     *  4、28位代表锁类型，可以参考锁编号
     *  5、29到52位代表授权ID
     *  6、56位这6位代表密码123456
     *  7、MSUCCV6  成功
     *  8、217 门锁类型
     */
    fun parse08(response:String) {
        val head = response.substring(0, response.lastIndexOf("@@@"))
        val content = response.substring(response.lastIndexOf("@@@")+2, response.length)
        // 解析头类型
        val deviceModel = head.substring(head.indexOf("***")+3,head.indexOf("$$$"))
    }
}