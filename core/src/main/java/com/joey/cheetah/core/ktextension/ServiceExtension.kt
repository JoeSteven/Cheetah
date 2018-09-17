package com.joey.cheetah.core.ktextension

import android.accounts.AccountManager
import android.annotation.SuppressLint
import android.app.*
import android.app.admin.DevicePolicyManager
import android.app.job.JobScheduler
import android.appwidget.AppWidgetManager
import android.bluetooth.BluetoothManager
import android.content.ClipboardManager
import android.content.Context
import android.content.RestrictionsManager
import android.content.pm.LauncherApps
import android.hardware.ConsumerIrManager
import android.hardware.SensorManager
import android.hardware.camera2.CameraManager
import android.hardware.display.DisplayManager
import android.hardware.input.InputManager
import android.hardware.usb.UsbManager
import android.location.LocationManager
import android.media.AudioManager
import android.media.MediaRouter
import android.media.projection.MediaProjectionManager
import android.media.session.MediaSessionManager
import android.media.tv.TvInputManager
import android.net.ConnectivityManager
import android.net.nsd.NsdManager
import android.net.wifi.WifiManager
import android.net.wifi.p2p.WifiP2pManager
import android.nfc.NfcManager
import android.os.*
import android.os.storage.StorageManager
import android.print.PrintManager
import android.support.annotation.RequiresApi
import android.telecom.TelecomManager
import android.telephony.TelephonyManager
import android.view.LayoutInflater
import android.view.WindowManager
import android.view.accessibility.AccessibilityManager
import android.view.accessibility.CaptioningManager
import android.view.inputmethod.InputMethodManager
import android.view.textservice.TextServicesManager

/**
 * Description: extension for context to get service manager
 * author:Joey
 * date:2018/9/7
 */
val Context.activityManager
    get() = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?

val Context.displayManager
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    get() = getSystemService(Context.DISPLAY_SERVICE) as DisplayManager?

val Context.accessibilityManager
    get() = getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager?

val Context.clipboardManager
    get() = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?

val Context.telephonyManager
    get() = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager?

val Context.accountManager
    get() = getSystemService(Context.ACCOUNT_SERVICE) as AccountManager?

val Context.alarmManager
    get() = getSystemService(Context.ALARM_SERVICE) as AlarmManager?

val Context.appWidgetManager
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    get() = getSystemService(Context.APPWIDGET_SERVICE) as AppWidgetManager?

val Context.appOpsManager
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    get() = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager?

val Context.audioManager
    get() = getSystemService(Context.AUDIO_SERVICE) as AudioManager?

val Context.batteryManager
    get() = getSystemService(Context.BATTERY_SERVICE) as BatteryManager?

val Context.bluetoothManager
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    get() = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager?

val Context.cameraManager
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    get() = getSystemService(Context.CAMERA_SERVICE) as CameraManager?

val Context.captioningManager
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    get() = getSystemService(Context.CAPTIONING_SERVICE) as CaptioningManager?

val Context.connectivityManager
    get() = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

val Context.consumerIrManager
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    get() = getSystemService(Context.CONSUMER_IR_SERVICE) as ConsumerIrManager?

val Context.devicePolicyManager
    get() = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager?

val Context.downloadManager
    get() = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager?

val Context.dropBoxManager
    get() = getSystemService(Context.DROPBOX_SERVICE) as DropBoxManager?

val Context.inputMethodManager
    get() = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?

val Context.inputManager
    get() = getSystemService(Context.INPUT_SERVICE) as InputManager?

val Context.jobScheduler
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    get() = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler?

val Context.keyguardManager
    get() = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager?

val Context.launcherApps
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    get() = getSystemService(Context.LAUNCHER_APPS_SERVICE) as LauncherApps?

val Context.layoutInflater
    get() = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?

val Context.locationManager
    get() = getSystemService(Context.LOCATION_SERVICE) as LocationManager?

val Context.mediaProjectionManager
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    get() = getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager?

val Context.mediaRouter
    get() = getSystemService(Context.MEDIA_ROUTER_SERVICE) as MediaRouter?

val Context.mediaSessionManager
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    get() = getSystemService(Context.MEDIA_SESSION_SERVICE) as MediaSessionManager?

val Context.nfcManager
    get() = getSystemService(Context.NFC_SERVICE) as NfcManager?

val Context.notificationManager
    get() = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

val Context.nsdManager
    get() = getSystemService(Context.NSD_SERVICE) as NsdManager?

val Context.powerManager
    get() = getSystemService(Context.POWER_SERVICE) as PowerManager?

val Context.printManager
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    get() = getSystemService(Context.PRINT_SERVICE) as PrintManager?

val Context.restrictionsManager
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    get() = getSystemService(Context.RESTRICTIONS_SERVICE) as RestrictionsManager?

val Context.searchManager
    get() = getSystemService(Context.SEARCH_SERVICE) as SearchManager?

val Context.sensorManager
    get() = getSystemService(Context.SENSOR_SERVICE) as SensorManager?

val Context.storageManager
    get() = getSystemService(Context.STORAGE_SERVICE) as StorageManager?

val Context.telecomManager
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    get() = getSystemService(Context.TELECOM_SERVICE) as TelecomManager?

val Context.textServicesManager
    get() = getSystemService(Context.TEXT_SERVICES_MANAGER_SERVICE) as TextServicesManager?

val Context.tvInputManager
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    get() = getSystemService(Context.TV_INPUT_SERVICE) as TvInputManager?

val Context.uiModeManager
    get() = getSystemService(Context.UI_MODE_SERVICE) as UiModeManager?

val Context.usbManager
    get() = getSystemService(Context.USB_SERVICE) as UsbManager?

val Context.userManager
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    get() = getSystemService(Context.USER_SERVICE) as UserManager?

val Context.vibrator
    get() = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?

val Context.wallpaperManager
    @SuppressLint("ServiceCast")
    get() = getSystemService(Context.WALLPAPER_SERVICE) as WallpaperManager?

val Context.wifiP2pManager
    get() = getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager?

val Context.wifiManager
    get() = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager?

val Context.windowManager
    get() = getSystemService(Context.WINDOW_SERVICE) as WindowManager?