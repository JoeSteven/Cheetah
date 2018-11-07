package com.joey.cheetah.core.media.ble;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Description:
 * author:Joey
 * date:2018/10/31
 */
public class BleSplitData {
    private byte[] data;
    private byte[] endFrame;
    private List<byte[]> dataList;


    public BleSplitData(byte[] data, byte[] endFrame) {
        this.data = data;
        this.endFrame = endFrame;
    }

    public List<byte[]> getDataList() {
        if (dataList == null) {
            generateList();
        }
        return dataList;
    }

    private void generateList() {
        dataList = new ArrayList<>();
        int number = data.length/20;
        if (number <= 0) {
            dataList.add(data);
        } else {
            for (int i = 0; i < number; i++) {
                dataList.add(Arrays.copyOfRange(data, i*20, i*20+20));
            }
            int last = data.length%20;
            dataList.add(Arrays.copyOfRange(data, data.length -last, data.length));
        }
    }

    public byte[] getFullData() {
        return data;
    }

    public byte[] getEndFrame() {
        return endFrame;
    }
}
