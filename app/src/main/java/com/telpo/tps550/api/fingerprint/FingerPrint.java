package com.telpo.tps550.api.fingerprint;

import com.telpo.tps550.api.util.SystemUtil;

/* loaded from: classes.dex */
public class FingerPrint {
    public static native int TPM315ioControl(int i, int i2);

    public static native int TPS537digital(int i, int i2);

    public static native int TPS537nfcReset(int i);

    public static native int TPS537psamReset(int i, int i2);

    public static native int TPS537setLedLight(int i, String str, int i2);

    public static native int TPS550BLedControl(int i, int i2);

    public static native int TPS560LedControl(int i, int i2);

    public static native int TPS711LedControl(int i, int i2);

    public static native int TPS732lockControl(int i);

    public static native int c10GetIOstatus(int i);

    public static native int c10GetVoltage(int i);

    public static native int c10ReadDeviceSN(byte[] bArr);

    public static native int c10ReadOffsetTelpo(int i, byte[] bArr);

    public static native int c10WriteDeviceSN(String str);

    public static native int c10WriteOffsetTelpo(int i, String str);

    public static native int c10ioctl(int i, int i2);

    static native int camPower360C(int i);

    static native int digital_tube(int i);

    static native int fingeric_power(int i);

    static native int fingerprint_power(int i);

    private static native int fingerprint_power450P(int i, int i2);

    static native int fingerprint_power_spi(int i);

    static native int hongmo_power(int i);

    static native int iccard_power(int i);

    private static native int iccard_power450P(int i, int i2);

    static native int idcard_power(int i);

    public static native int money_box(int i);

    static native int printer_power(int i);

    static native int psam_power(int i);

    static native int psam_switch(int i);

    static native int rfid_read();

    public static native int rk3399proUsbPower(int i);

    public static native int rs485_tps530a(int i);

    public static native int test_power(int i, int i2);

    public static native int tps980DoubleCamPower(int i, int i2);

    static native int uhf_power(int i);

    public static native int usb_control(int i);

    static native int usb_port(int i);

    public static boolean fingerPrintPower(int i) {
        return "TPS450P".equals(SystemUtil.getInternalModel()) ? fingerprintPower450P(i) == 0 : fingerprint_power(i) == 0;
    }

    public static boolean UHFPower(int i) {
        return uhf_power(i) == 0;
    }

    public static boolean IrisPower(int i) {
        return hongmo_power(i) == 0;
    }

    public static boolean fingerPrintPowerSPI(int i) {
        return fingerprint_power_spi(i) == 0;
    }

    public static boolean digitalTube(int i) {
        return digital_tube(i) == 0;
    }

    public static boolean fingericPower(int i) {
        return fingeric_power(i) == 0;
    }

    public static boolean usbPort(int i) {
        return usb_port(i) == 0;
    }

    public static boolean idcardPower(int i) {
        return idcard_power(i) == 0;
    }

    public static boolean iccardPower(int i) {
        return iccard_power(i) == 0;
    }

    public static boolean psamSwitch(int i) {
        return psam_switch(i) == 0;
    }

    public static int rfidRead() {
        return rfid_read();
    }

    public static boolean printerPower(int i) {
        return printer_power(i) == 0;
    }

    public static boolean psamPower(int i) {
        return psam_power(i) == 0;
    }

    public static boolean setCamPower360C(int i) {
        return camPower360C(i) == 0;
    }

    public static int fingerprintIccPower450P(int i) {
        if ("on".equals(getProperty("persist.ethernet.config", ""))) {
            return iccard_power450P(i, 0);
        }
        return iccard_power450P(i, 1);
    }

    public static int fingerprintPower450P(int i) {
        if ("on".equals(getProperty("persist.ethernet.config", ""))) {
            return fingerprint_power450P(i, 0);
        }
        return fingerprint_power450P(i, 1);
    }

    private static String getProperty(String str, String str2) {
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            return (String) cls.getMethod("get", String.class, String.class).invoke(cls, str, str2);
        } catch (Exception e) {
            e.printStackTrace();
            return str2;
        }
    }

    static {
        System.loadLibrary("fingerprint");
    }
}
