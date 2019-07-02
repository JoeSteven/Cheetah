package com.terminus.iotextension.event;

/**
 * @author rain
 * @date 2019/7/02
 */
public class UpdateEvent extends BaseEvent {
    //任务ID
    public final long taskId;
    //设备ID
    public final String devId;
    //下发文件大小字节
    public final int fileSize;
    //文件url
    public final String fileUrl;
    //文件md5
    public final String md5;
    //文件类型
    public final int fileType;
    //升级类型
    public final int updateType;
    //升级包版本号
    public final String updateVersion;
    //设备版本
    public final String devVersion;
    //固件ID
    public final int osId;
    //芯片类型
    public final int coreType;

    public UpdateEvent(long taskId, String devId, int fileSize, String fileUrl, String md5,
                       int fileType, int updateType, String updateVersion,
                       String devVersion, int osId, int coreType) {
        this.taskId = taskId;
        this.devId = devId;
        this.fileSize = fileSize;
        this.fileUrl = fileUrl;
        this.md5 = md5;
        this.fileType = fileType;
        this.updateType = updateType;
        this.updateVersion = updateVersion;
        this.devVersion = devVersion;
        this.osId = osId;
        this.coreType = coreType;
    }
}
