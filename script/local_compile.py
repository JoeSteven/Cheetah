# -*- coding: utf-8 -*-
import sys
import subprocess as sp
import os

repo_root = "Cheetah"
repo_release_maven = "Cheetah/maven/terminus"
repo_snapshot_maven = "Cheetah/maven/terminus-snapshot"


def main():
    maven_path = None
    try:
        maven_path = sys.argv[1]
    except IndexError:
        print("未输入本地maven仓库保存地址")

    if maven_path is None:
        with open("maven.path", "r+") as f:
            maven_path = f.read()
            f.close()

    if not os.path.exists(maven_path):
        os.mkdir(maven_path)
    current_dir = os.getcwd()
    print("本地maven仓库保存地址：" + maven_path)
    print("cd to " + maven_path)
    os.chdir(maven_path)
    print("current dir " + os.getcwd())

    # 判断目录是否存在，存在的话直接切到 master pull
    if os.path.exists(repo_root):
        print("cd to " + repo_root)
        os.chdir(repo_root)
        print("current dir:" + os.getcwd())

        print("checkout to master")
        status, result = sp.getstatusoutput("git checkout master")
        print(result)

        print("checkout to master")
        status, result = sp.getstatusoutput("git pull origin master")
        print(result)

    else:
        # 1.拉取maven的git仓库，只拉仓库
        print("clone repo")
        status, result = sp.getstatusoutput("git clone git@47.105.40.167:root/cheetah.git " + maven_path + repo_root)
        print(result)
    print("cd to origin dir " + current_dir)
    os.chdir(current_dir)
    print("back to dir:" + os.getcwd())
    print("更新仓库完成，请仔细查看日志确定是否更新成功！成功后按找下面提示执行")
    print("请将以下代码粘贴到项目 build.gradle 的 repositories 中")
    print("maven {url \"" + maven_path + repo_release_maven + "\"}")
    print("maven {url \"" + maven_path + repo_snapshot_maven + "\"}")


if __name__ == '__main__':
    main()
