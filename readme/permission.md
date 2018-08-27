#  权限模块

### 概述

该模块主要是为了简化业务层在系统6.0以上的运行时权限申请操作而产生

### 使用

业务层通过`PermissionUtil` 来使用相关功能

```java
// 申请权限，第三个参数是可变参数，可以同时申请多组权限，在回调中处理业务逻辑
PermissionUtil.requestPermission(this, new PermissionListener() {
            @Override
            public void permissionGranted(@NonNull String[] permission) {
                
            }

            @Override
            public void permissionDenied(@NonNull String[] permission) {

            }
        }, Manifest.permission.CAMERA);

// showTip:是否在被拒绝权限后展示提示弹窗，tip：弹窗中的提示信息，建议使用该方法申请权限，明确告知用户为什么需要权限
PermissionUtil.requestPermission(activity, listener,String[] permission, showTip, tip)

// permission为可变参数，支持多组权限查询
PermissionUtil.hasPermission(activity, permission)

  //跳到权限设置页面
PermissionUtil.gotoSetting(activity);

// 判断授权结果是否通过，如果直接使用Android提供的授权方法，返回结果可以用该方法判断，如果使用本模块不需要使用该方法
PermissionUtil.isGranted(int...result);
```

