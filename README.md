# Cheetah
Cheetah 是一个Android应用快速开发框架，让开发者在开发全新应用的时候更专注于业务层开发。

###  概述

Cheetah 目前划分为两层，基础功能层 (core) 和架构层 (mvp)。

- 基础功能层 - 提供应用开发所需要的基础能力，例如并发，事件分发，应用初始化，网络，多媒体，蓝牙，权限，列表，数据库等等
- 架构层提供了应用开发所需的基础架构，例如MVP，这一层封装好了MVP的架构模式，尽可能的减少业务层的模版代码，同时处理了View 和 Presenter 的双向绑定和生命周期同步等

目前基础功能层都放到 core module 中，后续会将各个基础功能独立为模块，可以根据应用需要去自由组合基础功能。

架构层的设计是基础功能的上层，因此架构层完全可以根据应用的需要去进行更改或者重新设计

### 使用

- 可以将项目clone到本地后，导入需要的module进行开发
- 也可以通过 gradle 直接依赖进行开发

### 模块说明

使用的时候请优先阅读初始化模块的文档，然后再是架构文档

#### 架构

[MVP](./readme/mvp.md)

#### 功能

[初始化](./readme/init.md) : init

[并发](./readme/async.md) ：async

[网络](./readme/net.md) : net

[权限](./readme/permission.md) : permission

[工具](./readme/utils.md) : utils

[列表](./readme/list.md) : list

[koltin 扩展](./readme/extension.md) : ktextension...待开发

[WebView](./readme/web.md) : web

[数据库](./readme/database.md) : database…待开发

蓝牙 : 蓝牙模块并没有直接包含在该框架中，可以依赖我的一个封装库[RxBle](https://github.com/JoeSteven/RxBle)

