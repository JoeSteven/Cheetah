package com.terminus.iot.utils;

/**
 * Description:
 * author:Joey
 * date:2018/9/18
 */

import java.util.zip.CRC32;

public class Crc32Utils {
    public static long CRC32(byte[] data) {
        CRC32 crc32 = new CRC32();
        crc32.update(data);
        long crc = crc32.getValue();
        return crc;
    }
}

