package com.example.s10.test;

import android.content.Context;
import android.util.Log;
import com.telpo.tps550.api.InternalErrorException;
import com.telpo.tps550.api.TelpoException;
import java.lang.reflect.InvocationTargetException;

/* loaded from: classes.dex */
public class UsbSession {
    private static final String TAG = "PowerUtil";

    private native int switch_adb();

    private native int switch_camerapower_off();

    private native int switch_camerapower_on();

    private native int switch_dc_led_off();

    private native int switch_dc_led_on();

    private native int switch_ethernet_off();

    private native int switch_ethernet_on();

    private native int switch_fingerpower_off();

    private native int switch_fingerpower_on();

    private native int switch_iris_off();

    private native int switch_iris_on();

    private native int switch_net_led_off();

    private native int switch_net_led_on();

    private native int switch_otg();

    private native int switch_power_led_off();

    private native int switch_power_led_on();

    private native int switch_psp_off();

    private native int switch_psp_on();

    private native int switch_usb_off();

    private native int switch_usb_on();

    private native int switch_vbus_off();

    private native int switch_vbus_on();

    private native int switch_zink_off();

    private native int switch_zink_on();

    static {
        System.loadLibrary("usbtest");
    }

    public synchronized int brightnessAdjustment(Context context, int i) throws TelpoException {
        int i2;
        try {
            try {
                Class<?> cls = Class.forName("com.common.sdk.led.LEDServiceManager");
                Object systemService = context.getSystemService("LED");
                try {
                    i2 = 0;
                    try {
                        i2 = ((Integer) cls.getMethod("brightnessAdjustment", Integer.TYPE).invoke(systemService, Integer.valueOf(i))).intValue();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new InternalErrorException();
                    } catch (IllegalArgumentException e2) {
                        e2.printStackTrace();
                        throw new InternalErrorException();
                    } catch (InvocationTargetException e3) {
                        e3.printStackTrace();
                        Exception exc = (Exception) e3.getTargetException();
                        if (exc instanceof TelpoException) {
                            throw ((TelpoException) exc);
                        }
                    }
                } catch (NoSuchMethodException e4) {
                    e4.printStackTrace();
                    throw new InternalErrorException();
                }
            } catch (ClassNotFoundException e5) {
                e5.printStackTrace();
                throw new InternalErrorException();
            }
        } catch (Throwable th) {
            throw th;
        }
        return i2;
    }

    public int pspPower(int i) {
        Log.w(TAG, "pspPower==================");
        if (i == 1) {
            return switch_psp_on();
        }
        return switch_psp_off();
    }

    public int irisPower(int i) {
        Log.w(TAG, "irisPower==================");
        if (i == 1) {
            return switch_iris_on();
        }
        return switch_iris_off();
    }

    public int usbPower(int i) {
        Log.w(TAG, "usbPower==================");
        if (i == 1) {
            return switch_usb_on();
        }
        return switch_usb_off();
    }

    public int netLedPower(int i) {
        Log.w(TAG, "netLedPower==================");
        if (i == 1) {
            return switch_net_led_on();
        }
        return switch_net_led_off();
    }

    public int powerLedPower(int i) {
        Log.w(TAG, "powerLedPower==================");
        if (i == 1) {
            return switch_power_led_on();
        }
        return switch_power_led_off();
    }

    public int dcLedPower(int i) {
        Log.w(TAG, "dcLedPower==================");
        if (i == 1) {
            return switch_dc_led_on();
        }
        return switch_dc_led_off();
    }

    public int vbusPower(int i) {
        Log.w(TAG, "vbusPower==================");
        if (i == 1) {
            return switch_vbus_on();
        }
        return switch_vbus_off();
    }

    public int fingerPower(int i) {
        Log.w(TAG, "fingerPower==================");
        if (i == 1) {
            return switch_fingerpower_on();
        }
        return switch_fingerpower_off();
    }

    public int cameraPower(int i) {
        Log.w(TAG, "cameraPower==================");
        if (i == 1) {
            return switch_camerapower_on();
        }
        return switch_camerapower_off();
    }

    public int changeUsbMode(int i) {
        Log.w(TAG, "changeUsbMode==================");
        if (i == 1) {
            return switch_adb();
        }
        return switch_otg();
    }

    public int zinkPower(int i) {
        Log.w(TAG, "cameraPower==================");
        if (i == 1) {
            return switch_zink_on();
        }
        return switch_zink_off();
    }
}
