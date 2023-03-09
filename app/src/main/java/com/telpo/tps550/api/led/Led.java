package com.telpo.tps550.api.led;

/* loaded from: classes.dex */
public class Led {
    public static final int POS_610_GREEN_LED = 0;
    public static final int POS_610_RED_LED = 1;
    public static final int POS_LED_CLOSE = 1;
    public static final int POS_LED_OPEN = 0;
    public static final int POS_W_PRINTER_LED = 3;
    public static final int POS_W_SYSTEM_LED = 2;

    private static native int led_control(int i);

    public static synchronized int ledControl(int i, int i2) {
        synchronized (Led.class) {
            if (i == 0) {
                if (i2 == 0) {
                    return led_control(11);
                } else if (i2 != 1) {
                    return -1;
                } else {
                    return led_control(12);
                }
            } else if (i == 1) {
                if (i2 == 0) {
                    return led_control(27);
                } else if (i2 != 1) {
                    return -1;
                } else {
                    return led_control(28);
                }
            } else if (i == 2) {
                if (i2 == 0) {
                    return led_control(25);
                } else if (i2 != 1) {
                    return -1;
                } else {
                    return led_control(26);
                }
            } else if (i != 3) {
                return -1;
            } else {
                if (i2 == 0) {
                    return led_control(17);
                } else if (i2 != 1) {
                    return -1;
                } else {
                    return led_control(18);
                }
            }
        }
    }

    static {
        System.loadLibrary("led");
    }
}
