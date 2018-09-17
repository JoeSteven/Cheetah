package com.joey.cheetah.core.net.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;

/**
 * Description: An util for Network
 * author:Joey
 * date:2018/9/4
 */
public class NetworkUtil {

    /**
     * check if the network is available
     */
    @SuppressLint("MissingPermission")
    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager manager =
                    (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
             NetworkInfo info = manager.getActiveNetworkInfo();
            return (info != null && info.isAvailable());
        } catch (Exception e) {
            // ignore
        }
        return false;
    }

    @SuppressLint("MissingPermission")
    public static boolean isconnected(Context context) {
        try {
            ConnectivityManager manager =
                    (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
             NetworkInfo info = manager.getActiveNetworkInfo();
            return (info != null && info.isConnected());
        } catch (Exception e) {
            // ignore
        }
        return false;
    }

    /**
     * open net work setting activity
     */
    public static void openNetworkSetting(Context context) {
        context.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
    }

    /**
     * get mac address
     * @param context
     * @return
     */
    @SuppressLint("MissingPermission")
    public static String getMacAddress(Context context) {
        try {
            WifiManager mgr = (WifiManager) context.getApplicationContext().getSystemService(
                    Context.WIFI_SERVICE);
           WifiInfo info = mgr.getConnectionInfo();
            if (info != null)
                return info.getMacAddress();
        } catch (Exception ex) {
            // ignore
        }
        return null;
    }

    public static boolean isWifi(Context context) {
        NetworkType networkType = getNetworkType(context);
        return NetworkType.WIFI == networkType;
    }

    public static boolean is4G(Context context) {
        NetworkType networkType = getNetworkType(context);
        return NetworkType.MOBILE_4G == networkType;
    }

    @SuppressLint("MissingPermission")
    public static NetworkType getNetworkType(Context context) {
        try {
            ConnectivityManager manager = (ConnectivityManager) context.getApplicationContext().getSystemService(
                    Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = manager.getActiveNetworkInfo();
            if (info == null || !info.isAvailable()) {
                return NetworkType.NONE;
            }
            int type = info.getType();
            if (ConnectivityManager.TYPE_WIFI == type) {
                return NetworkType.WIFI;
            } else if (ConnectivityManager.TYPE_MOBILE == type) {
                TelephonyManager mgr = (TelephonyManager) context.getSystemService(
                        Context.TELEPHONY_SERVICE);
                switch (mgr.getNetworkType()) {
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                        return NetworkType.MOBILE_3G;
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        return NetworkType.MOBILE_4G;
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        return NetworkType.MOBILE_2G;
                    default:
                        return NetworkType.MOBILE;
                }
            } else {
                return NetworkType.MOBILE;
            }
        } catch (Throwable e) {
            return NetworkType.NONE;
        }
    }

    public enum NetworkType {
        NONE("unKnow"),
        MOBILE("mobile"),
        MOBILE_2G("mobile_2G"),
        MOBILE_3G("mobile_3G"),
        MOBILE_4G("mobile_4G"),
        WIFI("wifi");

        final String name;

        NetworkType(String name) {
            this.name = name;
        }
    }
}
