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
    status, result = sp.getstatusoutput(path + "gradlew clean -p " + module)
    print(result)
    print("正在执行:build ")
    status, result = sp.getstatusoutput(path + "gradlew build -p " + module)
    print(result)
    print("正在执行:publish ")
    status, result = sp.getstatusoutput(path + "gradlew publish -p " + module)
    print(result)
    print("执行完毕，请自行检查日志是否成！")


if __name__ == '__main__':
    main()
