package com.joey.cheetah.core.permission;

import android.Manifest;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * description:危险权限常量
 * @author rain
 * @date 2018/08/29
 */
@RequiresApi(api = Build.VERSION_CODES.M)
public final class PermissionConstant {

    public static final String CALENDAR   = Manifest.permission_group.CALENDAR;
    public static final String CAMERA     = Manifest.permission_group.CAMERA;
    public static final String CONTACTS   = Manifest.permission_group.CONTACTS;
    public static final String LOCATION   = Manifest.permission_group.LOCATION;
    public static final String MICROPHONE = Manifest.permission_group.MICROPHONE;
    public static final String PHONE      = Manifest.permission_group.PHONE;
    public static final String SENSORS    = Manifest.permission_group.SENSORS;
    public static final String SMS        = Manifest.permission_group.SMS;
    public static final String STORAGE    = Manifest.permission_group.STORAGE;

    private static final String[] CALENDAR_GROUP = {
            Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR
    };

    private static final String[] CAMERA_GROUP = {
            Manifest.permission.CAMERA
    };

    private static final String[] CONTACTS_GROUP = {
            Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.GET_ACCOUNTS
    };

    private static final String[] LOCATION_GROUP = {
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private static final String[] MICROPHONE_GROUP = {
            Manifest.permission.RECORD_AUDIO
    };

    private static final String[] PHONE_GROUP = {
            Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_CALL_LOG, Manifest.permission.WRITE_CALL_LOG,
            Manifest.permission.ADD_VOICEMAIL, Manifest.permission.USE_SIP,
            Manifest.permission.PROCESS_OUTGOING_CALLS
    };

    private static final String[] SENSORS_GROUP = {
            Manifest.permission.BODY_SENSORS
    };

    private static final String[] SMS_GROUP = {
            Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_WAP_PUSH,
            Manifest.permission.RECEIVE_MMS,
    };

    private static final String[] STORAGE_GROUP = {
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @StringDef({CALENDAR,CAMERA,CONTACTS,LOCATION,MICROPHONE,PHONE,SENSORS,SMS,STORAGE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Permission {

    }


    public static String[] getPermissionsGroup(@Permission String permission) {
        switch (permission) {
            case CALENDAR:
                return CALENDAR_GROUP;
            case CAMERA:
                return CAMERA_GROUP;
            case CONTACTS:
                return CONTACTS_GROUP;
            case LOCATION:
                return LOCATION_GROUP;
            case MICROPHONE:
                return MICROPHONE_GROUP;
            case PHONE:
                return PHONE_GROUP;
            case SENSORS:
                return SENSORS_GROUP;
            case SMS:
                return SMS_GROUP;
            case STORAGE:
                return STORAGE_GROUP;
            default:
                break;
        }

        return new String[]{permission};
    }
}
