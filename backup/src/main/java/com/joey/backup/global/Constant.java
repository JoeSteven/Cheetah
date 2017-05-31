package com.joey.backup.global;

/**
 * Created by Joe on 16/7/12.
 */
public class Constant {
    public static final boolean IS_DEBUG =true;

    /**扫码广播相关常量*/
    public static final String SCN_SUCCEED = "com.android.server.scannerservice.broadcast";//扫码成功
    public static final String SCN_EX_DATA = "scannerdata";//扫码获取的数据
    //下面两条是扫码广播相关其他action,注释就行,不要删除
    public static final String SCN_ACTION_START = "android.intent.action.SCANNER_BUTTON_DOWN";//开始扫码
    public static final String SCN_ACTION_CANCEL = "android.intent.action.SCANNER_BUTTON_UP";//扫码取消

    /**SharedPreference*/
    public static final String SHARED_CONFIG = "config";//config

    /**数据库*/
    public static final String DATABASE_ERP = "erp.db";

    /**Config*/
    public static final String CONFIG_TOKEN = "access_token";
    public static final String CONFIG_TOKEN_TYPE = "access_token_type";

    /**跳转*/
    public static final String JUMP_SKU = "jump_sku";//跳转单品查询成功页
    public static final String JUMP_SCAN_TYPE = "jump_scan_type";//跳转扫描页，传type
    public static final String JUMP_BATCH_SHEET = "jump_batch_sheet";//跳转拣货页，分拣页，波次单信息
    public static final String JUMP_CHECK_INFO = "jump_check_info";//跳转盘点详情页

    /**Fragment传参*/
    public static final String FRAG_PICK_BATCH = "frag_pick_batch";//拣货波次单
    public static final String FRAG_SORT_BATCH = "frag_sort_batch";//分拣波次单
    public static final String FRAG_SOURCE_SWAPINVENTORY = "frag_source_swapinventory";//源头属性
    public static final String FRAG_STRING_TARGET_STORAGELOC = "frag_string_target_storageloc";//目标库位

    /**传参数**/
    public static final String EXTRA_STRING_TARGET_STORAGELOC = "extra_string_target_storageloc";//移动目标库位
    public static final String EXTRA_STRING_STORAGE_TYPE_ID = "extra_string_storage_type_id";//移动目标库位

    /**HTML spanned**/
    public static final String FONT_START_TAG_BEGIN = "<FONT color = ";
    public static final String FONT_START_TAG_END = ">";
    public static final String FONT_END_TAG = "</FONT>";
    public static final String FONT_BOLD_START_TAG = "<b><tt>";
    public static final String FONT_BOLD_END_TAG = "</tt></b>";
}
