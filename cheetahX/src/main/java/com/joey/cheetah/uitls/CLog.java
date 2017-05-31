package com.joey.cheetah.uitls;

import android.util.Log;

/**
 * LogCat帮助类，必须传入className以区分log位置
 * Created by Joe on 2016/7/12.
 */
public class CLog {
    private static boolean isDebug;
    private static String sTag = "Cheetah";

    public static void debug() {
        isDebug = true;
    }

    public static void setTag(String tag) {
        sTag = tag;
    }


    public static void e(String className, String s){
        if(isDebug){
            Log.e(sTag,className+"===>"+s);
        }
    }
    public static void d(String className, String s){
        if(isDebug){
            Log.d(sTag,className+"===>"+s);
        }
    }

    public static void w(String className, String s){
        if(isDebug){
            Log.w(sTag,className+" warning:"+s);
        }
    }
    /**打印被包裹的内容*/
    public static void s(String className, String...s){
        if(isDebug){
            Log.d(sTag,"====================================>");
            for (String log : s) {
                Log.d(sTag,className+"===>"+log);
            }
            Log.d(sTag,"====================================>");
        }
    }
}
