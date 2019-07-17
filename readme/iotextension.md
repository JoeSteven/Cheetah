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
    							//硬件版本号
                  .hardVersion(xx)
    							//串号
                  .sn(xx)
    							//Mac地址
                  .mac(xx)
    							//第一次初始化默认值为空，需进行随机生成，下次再初始化时传入响应过后固化下来的key
                  .aesKey(xx)
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
              public void onSuccess(boolean reconnect,boolean regis) {
                	if(regis) {
                    //当注册成功后，根据业务需求进行拉取数据、二维码规则、通行规则、服务器配置信息等请求
                  }
              }
  
              @Override
              public void onEvent(BaseEvent baseEvent) {
                  if (baseEvent instanceof AeskeyEvent) {
                    	//生成的AesKey，该key应该保存在本地，下次启动程序时以便直接进行使用
                      AeskeyEvent event = (AeskeyEvent) baseEvent;
                  } else if (baseEvent instanceof PersonInfoEvent) {
                      //下发的人员数据信息,业务层序根据该数据信息进行相应的人员录入或是删除等操作
                      PersonInfoEvent event = (PersonInfoEvent) baseEvent;
                  } else if (baseEvent instanceof CodeEvent) {
                    	//二维码验证规则事件
                      CodeEvent event = (CodeEvent) baseEvent;
                  } else if (baseEvent instanceof RuleEvent) {
                    	//通行规则事件
                      RuleEvent event = (RuleEvent) baseEvent;
                  } else if (baseEvent instanceof ServerEvent) {
                    	//服务器配置信息事件
                      ServerEvent event = (ServerEvent) baseEvent;
                  } else if (baseEvent instanceof TimeEvent) {
                    	//时间轮询结果事件
                      TimeEvent event = (TimeEvent) baseEvent;
                  } else if (baseEvent instanceof PasslogResultEvent) {
                    	//通行日志记录结果事件
                      PasslogResultEvent event = (PasslogResultEvent) baseEvent;
                  } else if (baseEvent instanceof UpdateEvent) {
                    	//应用更新信息事件
                      UpdateEvent updateEvent = (UpdateEvent) baseEvent;
                  }
              }
          });
  ```

2.其余接口

 - 请求所需的数据

   ```java
   /**
    * DataType type 数据类型(PERSON、BLACK、VISITOR、CARD、ROOM、USER)
    */
   mIotController.uploadNeedInfo(DataType... type);
   ```

 - 时间请求

   ```java
   //需定时跟服务器进行同步
   mIotController.requestTime();
   ```

 - 请求服务器配置信息

   ```java
   mIotController.requestSetting();
   ```

 - 请求通行规则

   ```java
   mIotController.requestRule();
   ```

 - 请求二维码规则

   ```java
   mIotController.requestQr();
   ```

 - 应用升级结果回报

   ```java
   mIotController.updateAck();
   ```
   
 - 拉取数据

   ```java
   /**
    * @param type 人员数据类型
    * @param version 当前数据类型的最新版本号,初请求时为0
    */
   mIotController.asylocalData(type,version);
   ```

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
   * @param img 通行图片的Url
   * @param videoUrl 通行视屏的Url
   */
  mIotController.uploadPassLog(personId, feature, type, direction,
                                  time, openStatus, devStatus, openType, cardNo, imgurl, videoUrl);
  ```

3.通行数据类型

* 请求数据类型

  ```java
  public enum DataTyjpe {
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

 * 网络类型

   ```java
   public enum NetType {
       /**
        * GPRS
        */
       GPRS,
   
       /**
        * Wifi
        */
       WIFI,
   
       /**
        * 有线网
        */
       Ethernet,
   
       /**
        * lora
        */
       LoRa,
   
       /**
        * zeta
        */
       ZETA,
   
       /**
        * nb
        */
       NB,
   
       /**
        * 3G
        */
       TG,
   
       /**
        * 4G
        */
       FG;
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
        * 二维码过期
        */
       ERROR_CODE_TIMEOUT,
   
       /**
        * 二维码-用户无权限
        */
       ERROR_CODE_USER,
   
       /**
        * 无效用户
        */
       ERROR_USER,
   
       /**
        * 体重异常
        */
       ERROR_WEIGHT,
   
       /**
        * 人员数量异常
        */
       ERROR_NUM,
   
       /**
        * 人脸比对失败
        */
       ERROR_COMPARE,
   
       /**
        * 人员权限超时
        */
       ERROR_USER_TIMEOUT,
   
       /**
        * 开门失败
        */
       ERROR_DOOR;
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
	    CODE,
	
	    /**
	     * 呼叫开门
	     */
	    CALL;
	}
	```
	
* 下发人员错误描述

  ```java
  public enum PersonError {
     /**
       * 重新处理成功
       */
      SUCCESS,
  
      /**
       * 图片服务器无响应
       */
      NORESPONSE,
      /**
       * 没有照片
       */
      NOPIC,
  
      /**
       * 建模失败
       */
      MODEL,
  
      /**
       * 图片内容为空
       */
      EMPTY,
  
      /**
       * 格式错误
       */
      FORMAT,
  
      /**
       * 检测不到人脸
       */
      NOFACE,
  
      /**
       * 人脸大小不合格
       */
      SIZE,
  
      /**
       * 人脸角度不合格
       */
      ANGLE,
      /**
       * 其他
       */
      OTHER;
  }
  ```

* 人员类型

  ``` java
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
      UNKNOWN;
  }
  ```

  

