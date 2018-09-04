package com.joey.cheetah.core.net.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Description:
 * author:Joey
 * date:2018/9/4
 */
public class NetworkUtil {

    @SuppressLint("MissingPermission")
    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager manager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
             NetworkInfo info = manager.getActiveNetworkInfo();
            return (info != null && info.isAvailable());
        } catch (Exception e) {
            // ignore
        }
        return false;
    }

}
