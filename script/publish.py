# -*- coding: utf-8 -*-
import sys
import subprocess as sp

path = "./"


def main():
    try:
        module = sys.argv[1]
    except IndexError:
        print("python3 publish.py [module], module 为要打包的模块名称")
        return
    if module is None:
        print("python3 publish.py [module], module 为要打包的模块名称")
        return
    module = path + module
    # 检查 gradle.properties 的DEBUG 属性
    properties = open("gradle.properties")
    propertie_lines = properties.readlines()
    for line in propertie_lines:
        if "DEBUG" not in line:
            continue
        print(line)
        if "false" not in line:
            print("gradle.properties 中 DEBUG 属性需要设置为 false 才可以开始打包发布!")
            return
    # 检查当前的branch
    status, result = sp.getstatusoutput("git status")
    print("正在检查当前分支")
    # 当在其他分支时，检查版本号
    version = ""
    # 检查module的版本号
    gradle = open(module + "/build.gradle", "r")
    lines = gradle.readlines()
    for line in lines:
        if "ext.version_name" not in line:
            continue
        version = line
        break
    gradle.close()
    if "On branch master" not in result:
        # 检查module的版本号
        if "SNAPSHOT" not in version:
            print("当前分支不在master分支，只允许发布SNAPSHOT包，检查到版本号为:" + version + ", 请修改为SNAPSHOT包！")
            return
    else:
        if "SNAPSHOT" in version:
            print("禁止在master分支发布SNAPSHOT包,检查到版本号为:" + version + "，请切换到一个新分支进行发布!")
            return

    execute = input("当前要发布的模块为:" + module + ", 版本号为:" + version + ", 请确认是否发布:y/n\n")
    if "y" != execute:
        print("中止发布")
        return
    print("正在执行:clean...")
    status, result = sp.getstatusoutput(path + "gradlew clean -p " + module)
    print(result)
    print("正在执行:build...")
    status, result = sp.getstatusoutput(path + "gradlew build -p " + module)
    print(result)
    print("正在执行:publish...")
    status, result = sp.getstatusoutput(path + "gradlew publish -p " + module)
    print(result)
    print("执行完毕，请自行检查日志是否成功！")


if __name__ == '__main__':
    main()
