package rs.teron.launcher.android;

import android.content.Context;
import android.content.Intent;

/* loaded from: classes.dex */
public class Configurator {
    private static final String APPLICATION_ACTION = "android.intent.action.application";
    private static final String DEVICE_LOGO_ACTION = "android.intent.action.set.logo";
    private static final String DEVICE_LOGO_KEY = "logo_path";
    private static final String DISPLAY_ACTION = "android.intent.action.display";
    private static final String FUNCTION_KEY_ACTION = "android.intent.action.functionkey";
    public static final String FUNCTION_KEY_DISABLED = "off";
    public static final String FUNCTION_KEY_ENABLED = "on";
    private static final String FUNCTION_KEY_ENABLE_KEY = "enable";
    private static final String FUNCTION_KEY_SET_APP_KEY = "functionApp";
    public static final String FUNCTION_KEY_SET_CAMERA = "camera";
    private static final String FUNCTION_KEY_SET_KEY = "function";
    public static final String FUNCTION_KEY_SET_NONE = "reset";
    public static final String FUNCTION_KEY_SET_POWER = "power";
    private static final String LANGUAGE_ACTION = "android.intent.action.language";
    private static final String LANGUAGE_KEY = "language";
    private static final String LANGUAGE_LIST_KEY = "language_choose_list";
    private static final String LAUNCHER_ACTION = "android.intent.action.launcher";
    private static final String LAUNCHER_APPLICATION_KEY = "application";
    public static final String NAVIGATION_BAR_HIDE = "off";
    private static final String NAVIGATION_BAR_KEY = "navigation_bar";
    public static final String NAVIGATION_BAR_SHOW = "on";
    private static final String POWER_ACTION = "android.intent.action.power";
    private static final String QUIET_INSTALL_KEY = "quiet_install";
    public static final String SCREEN_BRIGHTNESS_100P = "5";
    public static final String SCREEN_BRIGHTNESS_16P = "0";
    public static final String SCREEN_BRIGHTNESS_33P = "1";
    public static final String SCREEN_BRIGHTNESS_49P = "2";
    public static final String SCREEN_BRIGHTNESS_66P = "3";
    public static final String SCREEN_BRIGHTNESS_83P = "4";
    private static final String SCREEN_BRIGHTNESS_KEY = "brightness";
    private static final String SCREEN_LOCK_KEY = "screen_lock";
    public static final String SCREEN_LOCK_OFF = "off";
    public static final String SCREEN_LOCK_ON = "on";
    public static final String SCREEN_TIMEOUT_10_MIN = "6";
    public static final String SCREEN_TIMEOUT_15_SEC = "0";
    public static final String SCREEN_TIMEOUT_1_MIN = "2";
    public static final String SCREEN_TIMEOUT_2_MIN = "3";
    public static final String SCREEN_TIMEOUT_30_MIN = "7";
    public static final String SCREEN_TIMEOUT_30_SEC = "1";
    public static final String SCREEN_TIMEOUT_5_MIN = "5";
    private static final String SCREEN_TIMEOUT_KEY = "sleep_time";
    public static final String SCREEN_TIMEOUT_NEVER = "-1";
    public static final String STATUS_BAR_DROP_DOWN_DISABLE = "off";
    public static final String STATUS_BAR_DROP_DOWN_ENABLE = "on";
    private static final String STATUS_BAR_DROP_KEY = "statusbar_drop";
    public static final String STATUS_BAR_HIDE = "off";
    public static final String STATUS_BAR_ICON_HIDE = "off";
    private static final String STATUS_BAR_ICON_KEY = "qsTitle";
    public static final String STATUS_BAR_ICON_SHOW = "on";
    private static final String STATUS_BAR_KEY = "status_bar";
    public static final String STATUS_BAR_SHOW = "on";
    private static final String SYSTEM_UI_ACTION = "android.intent.action.systemui";
    private static final String TOUCH_KEY_ACTION = "android.intent.action.touchkey";
    public static final String TOUCH_KEY_BACK_KEY = "back_key";
    public static final String TOUCH_KEY_BAR_KEY = "bar_key";
    public static final String TOUCH_KEY_DISABLED = "off";
    public static final String TOUCH_KEY_ENABLED = "on";
    public static final String TOUCH_KEY_HOME_KEY = "home_key";
    public static final String TOUCH_KEY_MENU_KEY = "menu_key";
    public static final String TOUCH_KEY_RECENT_KEY = "recent_key";

    public static void setNavigationBar(Context context, String str) {
        broadcastEvent(context, SYSTEM_UI_ACTION, NAVIGATION_BAR_KEY, str);
    }

    public static void setStatusBar(Context context, String str) {
        broadcastEvent(context, SYSTEM_UI_ACTION, STATUS_BAR_KEY, str);
    }

    public static void setStatusBarDropDown(Context context, String str) {
        broadcastEvent(context, SYSTEM_UI_ACTION, STATUS_BAR_DROP_KEY, str);
    }

    public static void setStatusBarIcon(Context context, String str) {
        broadcastEvent(context, SYSTEM_UI_ACTION, STATUS_BAR_ICON_KEY, str);
    }

    public static void setScreenBrightness(Context context, String str) {
        broadcastEvent(context, DISPLAY_ACTION, SCREEN_BRIGHTNESS_KEY, str);
    }

    public static void setScreenTimeout(Context context, String str) {
        broadcastEvent(context, POWER_ACTION, SCREEN_TIMEOUT_KEY, str);
    }

    public static void setScreenLock(Context context, String str) {
        broadcastEvent(context, POWER_ACTION, SCREEN_LOCK_KEY, str);
    }

    public static void setTouchKey(Context context, String str, String str2) {
        broadcastEvent(context, TOUCH_KEY_ACTION, str, str2);
    }

    public static void setFunctionKeyEnabled(Context context, String str) {
        broadcastEvent(context, FUNCTION_KEY_ACTION, FUNCTION_KEY_ENABLE_KEY, str);
    }

    public static void setFunctionKey(Context context, String str) {
        broadcastEvent(context, FUNCTION_KEY_ACTION, FUNCTION_KEY_SET_KEY, str);
    }

    public static void setFunctionAppKey(Context context, String str) {
        broadcastEvent(context, FUNCTION_KEY_ACTION, FUNCTION_KEY_SET_APP_KEY, str);
    }

    public static void setLanguage(Context context, String str) {
        broadcastEvent(context, LANGUAGE_ACTION, LANGUAGE_KEY, str);
    }

    public static void setLanguageList(Context context, String str) {
        broadcastEvent(context, LANGUAGE_ACTION, LANGUAGE_LIST_KEY, str);
    }

    public static void setLauncherApplication(Context context, String str) {
        broadcastEvent(context, LAUNCHER_ACTION, LAUNCHER_APPLICATION_KEY, str);
    }

    public static void setDeviceLogo(Context context, String str) {
        broadcastEvent(context, DEVICE_LOGO_ACTION, DEVICE_LOGO_KEY, str);
    }

    public static void installApplication(Context context, String str) {
        broadcastEvent(context, APPLICATION_ACTION, QUIET_INSTALL_KEY, str);
    }

    private static void broadcastEvent(Context context, String str, String str2, String str3) {
        Intent intent = new Intent();
        intent.setAction(str);
        intent.putExtra(str2, str3);
        context.sendBroadcast(intent);
    }
}
