package com.joey.cheetah.uitls;

import android.text.TextUtils;

/**
 * description - 转换帮助类
 * <p/>
 * author - Joe.
 * create on 16/7/21.
 * change
 * change on .
 */


public class ParseUtils {

    public static int parseInt(String s){
        int number = 0;
        try {
            number = Integer.parseInt(s);
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            return number;
        }
    }

    public static String trimAll(String s){
        if(TextUtils.isEmpty(s)) return "";
        return s.replaceAll(" ","");
    }
}
