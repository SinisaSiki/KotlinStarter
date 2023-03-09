package com.telpo.tps550.api.util;

import android.util.Log;
import com.telpo.tps550.api.fingerprint.FingerPrint;

/* loaded from: classes.dex */
public class PowerUtil {
    public static int h7sIDpower(int i) {
        return FingerPrint.money_box(i);
    }

    public static int TPM315ioControl(int i, int i2) {
        return FingerPrint.TPM315ioControl(i, i2);
    }

    public static int c10ioctl(int i, int i2) {
        return FingerPrint.c10ioctl(i, i2);
    }

    public static float c10getVoltage(int i) {
        if (i < 0 || i > 3) {
            return -1.0f;
        }
        float c10GetVoltage = FingerPrint.c10GetVoltage(i);
        float f = i == 2 ? ((2.0f * c10GetVoltage) / 0.8f) / 1000000.0f : c10GetVoltage / 1000000.0f;
        Log.d("c10getVoltage", "\ntype=" + i + " voltage=" + c10GetVoltage + " voltageV=" + f);
        return f;
    }

    public static int c10GetIOstatus(int i) {
        return FingerPrint.c10GetIOstatus(i);
    }

    public static int c10ReadFlash(int i, byte[] bArr) {
        return FingerPrint.c10ReadOffsetTelpo(i, bArr);
    }

    public static int c10WriteFlash(int i, String str) {
        return FingerPrint.c10WriteOffsetTelpo(i, str);
    }

    public static int c10ReadDeviceSN(byte[] bArr) {
        return FingerPrint.c10ReadDeviceSN(bArr);
    }

    public static int c10WriteDeviceSN(String str) {
        return FingerPrint.c10WriteDeviceSN(str);
    }

    public static int inter02LedControl(int i, int i2) {
        return FingerPrint.TPS560LedControl(i, i2);
    }

    public static int rk3399proResetReader() {
        FingerPrint.rk3399proUsbPower(0);
        return FingerPrint.rk3399proUsbPower(1);
    }

    public static int TPS537psamReset(int i) {
        FingerPrint.TPS537psamReset(i, 0);
        return FingerPrint.TPS537psamReset(i, 1);
    }

    public static int TPS537nfcReset() {
        FingerPrint.TPS537nfcReset(0);
        return FingerPrint.TPS537nfcReset(1);
    }

    public static int TPS537digital(int i, int i2) {
        if (i == 1 || i == 3 || i == 5) {
            if (i2 < -1) {
                i2 = -1;
            }
            if (i2 > 9) {
                i2 = 9;
            }
            if (i2 == -1) {
                i2 = 11;
            }
        }
        if (i == 2 || i == 4 || i == 6) {
            if (i2 < 0) {
                i2 = 0;
            }
            if (i2 > 1) {
                i2 = 1;
            }
        }
        if (i == 7) {
            if (i2 < 1) {
                i2 = 1;
            }
            if (i2 > 8) {
                i2 = 8;
            }
        }
        if (i < 1 || i > 6) {
            return -1;
        }
        return FingerPrint.TPS537digital(i, i2);
    }

    public static int TPS537setLedLight(int i) {
        if (i < 0) {
            i = 0;
        }
        if (i > 255) {
            i = 255;
        }
        String sb = new StringBuilder(String.valueOf(i)).toString();
        return FingerPrint.TPS537setLedLight(1, sb, sb.length());
    }

    public static int TPS711LedControl(int i, int i2) {
        return FingerPrint.TPS711LedControl(i, i2);
    }

    public static int usb_control(int i) {
        return FingerPrint.usb_control(i);
    }

    public static int Iris_control(int i) {
        return FingerPrint.IrisPower(i) ? 0 : -1;
    }

    public static int TPS732lockControl(int i) {
        return FingerPrint.TPS732lockControl(i);
    }

    public static boolean fingerPrintPower(int i) {
        return FingerPrint.fingerPrintPower(i);
    }

    public static boolean UHFPower(int i) {
        return FingerPrint.UHFPower(i);
    }

    public static boolean psamPower(int i) {
        return FingerPrint.psamPower(i);
    }

    public static boolean digitalTube(int i) {
        return FingerPrint.digitalTube(i);
    }

    public static boolean fingericPower(int i) {
        return FingerPrint.fingericPower(i);
    }

    public static boolean usbPort(int i) {
        return FingerPrint.usbPort(i);
    }

    public static boolean idcardPower(int i) {
        return FingerPrint.idcardPower(i);
    }

    public static boolean iccardPower(int i) {
        return FingerPrint.iccardPower(i);
    }

    public static boolean psamSwitch(int i) {
        return FingerPrint.psamSwitch(i);
    }

    public static String rfidRead() {
        int rfidRead = FingerPrint.rfidRead();
        Log.d("TAG", "FingerPrint.rfidRead():" + rfidRead);
        String binaryString = Integer.toBinaryString(rfidRead);
        Log.d("TAG", "binary:" + binaryString);
        int length = binaryString.length();
        if (length < 18) {
            return null;
        }
        return Integer.valueOf(binaryString.substring(1, length - 1), 2).toString();
    }

    public static boolean printerPower(int i) {
        return FingerPrint.printerPower(i);
    }

    public static int tps980DoubleCamPower(int i, int i2) {
        return FingerPrint.tps980DoubleCamPower(i, i2);
    }

    public static int TPS550BLedControl(int i, int i2) {
        return FingerPrint.TPS550BLedControl(i, i2);
    }

    public static int rs485_tps530a(int i) {
        return FingerPrint.rs485_tps530a(i);
    }
}
