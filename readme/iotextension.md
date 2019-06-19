# Mqtt业务扩展层

## 概述

​	该模块是在Mqtt核心层的基础上，结合公司通用Iot协议编写设计而成，包含了连接，重连，事件响应等相关操作。该模块的目的在于让业务层更专注于上层业务的开发，同时也便于后期对于多个项目的快速及时响应。

## 使用

1.快速使用

- 基础参数信息配置

  ```java
  //devID和productKey可通过文档提供的方式进行获取
  IotController mIotController = new IotController.Bulider()
                  .devID(devId)
                  .clientID(MD5Util.getMD5(productKey + devId))
                  .userName(productKey + devId)
                  .passWord(Sha1Util.EncodeDefault(IoTConstant.RSA_KEY, devId.getBytes()))
                  //Iot的后台地址
    			  .address(xx)
    			  //category、devName、type和model根据文档和设备选取相应的数值传入
                  .category(xx)
                  .devName(xx)
                  .type(xx)
                  .model(xx)
    			  //软件版本号
                  .softVersion(xx)
    			  /硬件版本号
                  .hardVersion(xx)
    			  //串号
                  .sn(xx)
    			  //Mac地址
                  .mac(xx)
    			  //第一次初始化默认值为空，需进行随机生成，下次再初始化时传入响应过后固化下来的key
                  .aesKey(xx)
    			  //该数据类型为DataType,传入当前设备需求的数据类型信息
                  .needItems(xx)
                  .subTopic("/down/" + productKey + "/" + devId)
                  .pubTopic("/up/" + productKey + "/" + devId)
                  .build();
  ```

- 初始化

  ``` java
  mIotController.initClient();
  ```

- 连接

  ```java
  mIotController.connect();
  ```

- 事件响应

  ```java
  mIotController.setMessageCallback(new MqttImpl.IotMessageCallback<BaseEvent>() {
  
              @Override
              public void onError(Throwable throwable) {
  
              }
  
              @Override
              public void onSuccess() {
                  //设备注册成功时上报当前设备需求的数据信息
                	mIotController.uploadNeedInfo(DataType.PERSON);
                /**
  							 * 本地与后端同步请求更新数据
  							 * type 数据类型
  							 * version 当前数据类型的最新版本号,初请求时为0
  							 */
                	mIotController.asylocalData(type,version);
              }
  
              @Override
              public void onEvent(BaseEvent baseEvent) {
                  if (baseEvent instanceof AeskeyEvent) {
                    	//生成的AesKey，该key应该保存在本地，下次启动程序时以便直接进行使用
                      AeskeyEvent event = (AeskeyEvent) baseEvent;
                  } else if (baseEvent instanceof PersonInfoEvent) {
                      //下发的人员数据信息,业务层序根据该数据信息进行相应的人员录入或是删除等操作
                      PersonInfoEvent event = (PersonInfoEvent) baseEvent;
                  }...
              }
          });
  ```

2.其余接口

* 停止连接

  ```java
  mIotController.disConnect();
  ```

* 重新连接

  ```java
  mIotController.reConnect();
  ```

* 通行日志上报

  ```java
  /**
   * 通行日志上报
   * @param personId 用户Id
   * @param feature 人员特征描述(json)
   * @param personType 人员类型
   * @param direction 进出方向
   * @param time 开门时间
   * @param openStatus 开门结果
   * @param devStatus 设备状态
   * @param openType 开门方式
   * @param cardNo 卡编号
   * @param imgurl 通行图片的Url
   * @param videoUrl 通行视屏的Url
   */
  mIotController.uploadPassLog(personId, feature, personType, direction,
                                  time, openStatus, devStatus, openType, cardNo, imgurl, videoUrl);
  ```

3.通行数据类型

* 请求数据类型

```java
public enum DataType {
    /**
     * user+visitor
     */
    PERSON,
    /**
     * user
     */
    USER,
    /**
     * visitor
     */
    VISITOR,
    /**
     * 黑名单
     */
    BLACK,
    /**
     * 发卡数据
     */
    CARD,
    /**
     * 房间信息
     */
    ROOM;
}
```

 * 设备状态

   ```java
   public enum DevStatus {
       /**
        * 关闭
        */
       CLOSED,
       /**
        * 开启
        */
       OPEN,
       /**
        * 异常
        */
       ERROR;
   }
   ```

 * 进出方向

   ```java
   public enum Direction {
       /**
        * 进
        */
       IN,
       /**
        * 出
        */
       OUT,
       /**
        * 未知
        */
       ERROR;
   }
   ```

 * 开关门结果

   ```java
   public enum OpenStatus {
       /**
        * 成功
        */
       SUCCESS,
       /**
        * 失败
        */
       FAIL,
       /**
        * 无效二维码
        */
       ERROR_CODE,
       /**
        * 无效用户
        */
       ERROR_USER;
   }
   ```

 * 开门方式

   ```java
   public enum OpenType {
       /**
        * 刷卡
        */
       CARD,
       /**
        * 一次性密码
        */
       ONCE_PASSWORD,
       /**
        * APP远程
        */
       APP_REMOTE,
       /**
        * 门内开门
        */
       INDOOR,
       /**
        * 人脸识别
        */
       FACE,
       /**
        * 指纹识别
        */
       FINGER,
       /**
        * 普通密码
        */
       NORMAL_PASSWORD,
       /**
        * DTMF
        */
       DTMF,
       /**
        * APP蓝牙
        */
       APP_BLUETOOTH,
       /**
        * 二维码开门
        */
       CODE;
   }
   ```

 * 人员类型

```java
public enum PersonType {
    /**
     * 用户
     */
    USER,
    /**
     * 黑名单
     */
    BLACK,
    /**
     * 访客
     */
    VISITOR,
    /**
     * 陌生人
     */
    UNKNOWN(;
}
```

