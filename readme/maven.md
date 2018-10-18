# maven 库发布与依赖指南

[发布maven 库 ](#发布)

[依赖maven 库 ](#依赖)

### 发布

目前需要发布的基础库都以module的形式放在 Cheetah 项目中。

#### 1.配置module的build.gradle

```groovy
// 将 publish.gradle 脚本引入到该模块中
apply from: "$rootDir/publish.gradle"

// 配置相关信息
ext.group_Id('com.joey.cheetah')// group 名称
ext.artifact_id('core')// 库名称
ext.version_name('1.0')// 库版本号，如果需要打snapshot包进行测试，只需要改为 1.0-SNAPSHOT 即可
ext.aar_name('core-release.aar') // build 完成后的 aar 名称，在模块的 build/outputs/aar 中查看
```

#### 2.发布

在**根目录**下执行命令，**发布前先阅读下一节：发布规范！**：

```Shell
# module 为要发布的模块
python3 script/publish.py [module]

#例如
python3 script/publish.py core
```

#### 3.发布规范

**注意：测试应该在测试开发的分支发布 SNAPSHOT 包，禁止在非master分支发布正式包！！！**

发布流程为：

- 从 master 分支切出一个分支进行开发，修改
- 在测试分支可以发布 SNAPSHOT 包进行测试，禁止在该分支发布正式包！
- 测试完成后，提交 merege request 到 master 分支，相关同事review代码后合并到master
- 在最新的master分支上，检查发布模块的版本号信息等是否符合预期
- 由有master权限的开发者执行发布命令
- 执行完毕后提交commit，push到远程仓库
- 在远程仓库基于master分支添加tag，需要说明release note， 该版本的修改人等信息



### 依赖

依赖分为两种方式，目前只搭建了基于重庆办公室局域网的 maven 仓库，因此只有在连接到重庆办公室wifi的情况下可以通过局域网依赖

#### 1.局域网依赖

在全局的 build.gradle 中添加 maven 地址，该地址可能会发生变化，以最新的为准

```groovy
maven { url 'http://192.168.201.176:8081/repository/terminus-snapshot/' }
maven { url 'http://192.168.201.176:8081/repository/terminus-release/' }
```

#### 2.本地依赖

为了解决在局域网外，或者maven仓库无法连接的情况，提供另一种本地依赖的方式

将 `script` 目录下的 `local_compile.py` 和 `maven.path` 文件拷贝到你的项目/电脑中，**修改maven.path 中的路径（绝对路径），该路径为你希望保存maven仓库的路径**

- 保证`local_compile.py` 和 `maven.path` 文件在同一个目录下
- 将 `maven.path` 添加到 .gitignore 文件中，不要提交，因为每个人的路径是不同的
- 执行 `python3 local_compile.py`  ，执行过程中可能需要输入 gitlab的access password
- 更新仓库，也是执行相同的命令

执行完毕后，会出现如下提示：

```
更新仓库完成，请仔细查看日志确定是否更新成功！成功后按找下面提示执行
请将以下代码粘贴到项目 build.gradle 的 repositories 中
maven {url "/Users/joey/AndroidStudioProjects/Terminus_Maven/Cheetah/maven/terminus"}
maven {url "/Users/joey/AndroidStudioProjects/Terminus_Maven/Cheetah/maven/terminus-snapshot"}
```

将提示中的代码粘贴到项目中即可