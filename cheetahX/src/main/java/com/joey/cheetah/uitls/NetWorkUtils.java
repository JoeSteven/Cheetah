package com.joey.cheetah.uitls;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


/**
 * description - 网络工具类
 *
 * author - Joe.
 * create on 16/7/13.
 * change
 * change on .
 */
public class NetWorkUtils {
    public static final int TYPE_INVALID = -1;
    public static final int TYPE_WIFI = 0;
    public static final int TYPE_4G = 1;
    public static boolean isNetAvailable(Context context){
        Boolean isOn=false;
        ConnectivityManager cm= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net=cm.getActiveNetworkInfo();
        if(net!=null){
            isOn=cm.getActiveNetworkInfo().isAvailable();
        }
        return isOn;
    }

    public static int getNetType(Context context){
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        int mNetWorkType=0;
        if (networkInfo != null && networkInfo.isConnected()) {
            int type = networkInfo.getType();

            if (type == ConnectivityManager.TYPE_WIFI) {
                mNetWorkType = TYPE_WIFI;
            } else if (type == ConnectivityManager.TYPE_MOBILE) {
                mNetWorkType = TYPE_4G;
            }
        } else {
            mNetWorkType = TYPE_INVALID;
        }

        return mNetWorkType;
    }
}
