package com.terminus.iotextension.bean;

/**
 * description 二维码中所包含信息
 * author      kai.mr
 * create      2019-05-16 20:13
 **/
public class QRInfo {
    private Long permissionId;
    private Long personId;
    private String type;      //"user"  / "visitor"
    private String name;
    private Integer weight;
    private Integer height;
    private Long validTime;
    private Long qrValidTime;
    private String customInfo;

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Long getValidTime() {
        return validTime;
    }

    public void setValidTime(Long validTime) {
        this.validTime = validTime;
    }

    public Long getQrValidTime() {
        return qrValidTime;
    }

    public void setQrValidTime(Long qrValidTime) {
        this.qrValidTime = qrValidTime;
    }

    public String getCustomInfo() {
        return customInfo;
    }

    public void setCustomInfo(String customInfo) {
        this.customInfo = customInfo;
    }
}
