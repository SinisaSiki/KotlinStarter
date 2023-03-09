package com.telpo.tps550.api.led;

/* loaded from: classes.dex */
public class LedPowerManager {
    static native int control3399Power(int i);

    static native int led_power_blue(int i);

    static native int led_power_green(int i);

    static native int led_power_red(int i);

    static native int tps575_adjust_backlight(int i);

    static native int tps575_check_lcd();

    static native int tps575_check_mainlcd();

    static native int tps575_close_backlight();

    static native int tps575_fine_contrast(int i);

    static native int tps575_open_backlight();

    static native int tps575_second_lcdclose();

    static native int tps575_second_screen(byte[] bArr);

    static native int tps575_thick_contrast(int i);

    public static native int tps990h_control(byte[] bArr);

    public static boolean control_3399Power(int i) {
        return control3399Power(i) == 0;
    }

    public static boolean power_green(int i) {
        return led_power_green(i) == 0;
    }

    public static boolean power_red(int i) {
        return led_power_red(i) == 0;
    }

    public static boolean power_blue(int i) {
        return led_power_blue(i) == 0;
    }

    public static int tps575_setSecondScreen(byte[] bArr) {
        return tps575_second_screen(bArr);
    }

    public static int tps575_setSecondScreenLcdClose() {
        return tps575_second_lcdclose();
    }

    public static int tps575_openBackLight() {
        return tps575_open_backlight();
    }

    public static int tps575_closeBackLight() {
        return tps575_close_backlight();
    }

    public static int tps575_adjustBackLight(int i) {
        return tps575_adjust_backlight(i);
    }

    public static int tps575_setThickContrast(int i) {
        int i2;
        if (i == 0) {
            i2 = 32;
        } else if (i == 1) {
            i2 = 33;
        } else if (i == 2) {
            i2 = 34;
        } else if (i == 3) {
            i2 = 35;
        } else if (i == 4) {
            i2 = 36;
        } else if (i == 5) {
            i2 = 37;
        } else if (i == 6) {
            i2 = 38;
        } else if (i != 7) {
            return -1;
        } else {
            i2 = 39;
        }
        return tps575_thick_contrast(i2);
    }

    public static int tps575_setFineContrast(int i) {
        if (i > 63 || i < 0) {
            return -1;
        }
        return tps575_fine_contrast(i);
    }

    public static int tps575_checkLCD() {
        return tps575_check_lcd() > 0 ? 1 : -1;
    }

    public static int tps575_checkMainLcd() {
        return tps575_check_mainlcd();
    }

    static {
        System.loadLibrary("ledpower");
    }
}
