package com.telpo.tps550.api.util;

import android.util.Log;
import com.telpo.tps550.api.util.StringUtil;
import java.io.File;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;

/* loaded from: classes.dex */
public class SystemUtil {
    public static final int PRINTER_HDX = 0;
    public static final int PRINTER_JX2R22 = 5;
    public static final int PRINTER_JX3R02 = 3;
    public static final int PRINTER_JX3R03 = 4;
    public static final int PRINTER_PRT_BAIDU = 2;
    public static final int PRINTER_PRT_COMMON = 1;
    public static final int PRINTER_PT486F08401MB = 6;
    public static final int PRINTER_PT723F08401 = 7;
    public static final int PRINTER_SY581 = 8;
    public static final int READER_AU9540_GBS = 3;
    public static final int READER_AU9540_GCS = 1;
    public static final int READER_AU9560_GBS = 4;
    public static final int READER_AU9560_GCS = 2;
    public static final int READER_MSR = 5;
    public static final int READER_VPOS3583 = 0;
    public static boolean tps650t_is_sy581 = true;
    private static String version = "20200107";

    private static native int get_device_type();

    private static native int get_icc_reader_type();

    private static native int get_printer581_type();

    private static native int get_printer5880(byte[] bArr);

    private static native int get_printer_type();

    static {
        System.loadLibrary("system_util");
    }

    public static int getDeviceType() {
        return get_device_type();
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

    public static String getInternalModel() {
        return getProperty("ro.internal.model", "");
    }

    public static int getICCReaderType() {
        return get_icc_reader_type();
    }

    public static int getPrinterType() {
        return get_printer_type();
    }

    public static int getPrinter5880() {
        String str;
        byte[] bArr = new byte[128];
        if (getDeviceType() == StringUtil.DeviceModelEnum.C1B.ordinal()) {
            writeTxtToFile("SY581", "/sdcard/tpsdk/printerVersion.txt");
            String checkSerialPort = CheckC1Bprinter.checkSerialPort();
            File file = new File("/sdcard/tpsdk/c1bserial.txt");
            if (file.exists()) {
                file.delete();
            }
            if ("/dev/ttyUSB9".equals(checkSerialPort)) {
                writeTxtToFile("/dev/ttyUSB9", "/sdcard/tpsdk/c1bserial.txt");
            } else if ("/dev/ttyUSB8".equals(checkSerialPort)) {
                writeTxtToFile("/dev/ttyUSB8", "/sdcard/tpsdk/c1bserial.txt");
            }
            return 8;
        }
        if ((getDeviceType() != StringUtil.DeviceModelEnum.TPS650T.ordinal() && getDeviceType() != StringUtil.DeviceModelEnum.TPS680.ordinal() && getDeviceType() != StringUtil.DeviceModelEnum.TPS650P.ordinal()) || (getDeviceType() == StringUtil.DeviceModelEnum.TPS650T.ordinal() && !tps650t_is_sy581)) {
            if (get_printer5880(bArr) == 0) {
                try {
                    str = new String(bArr, 1, bArr[0], "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (str != null && str.contains("ESC/POS")) {
                    writeTxtToFile("PT72", "/sdcard/tpsdk/printerVersion.txt");
                    return 7;
                }
            }
            str = null;
            if (str != null) {
                writeTxtToFile("PT72", "/sdcard/tpsdk/printerVersion.txt");
                return 7;
            }
        }
        if (get_printer581_type() == 8) {
            writeTxtToFile("SY581", "/sdcard/tpsdk/printerVersion.txt");
            return 8;
        }
        writeTxtToFile("5880", "/sdcard/tpsdk/printerVersion.txt");
        return 2;
    }

    public static int checkPrinter581() {
        Log.d("tagg", "into checkPrinter581()");
        if (ShellUtils.execCommand("cat /sys/kernel/debug/usb/devices", false).successMsg.contains("USB Thermal Printer")) {
            return 1;
        }
        return getPrinter5880();
    }

    private static void writeTxtToFile(String str, String str2) {
        try {
            File file = new File(str2);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } else {
                file.delete();
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rwd");
            randomAccessFile.seek(file.length());
            randomAccessFile.write(str.getBytes());
            randomAccessFile.close();
        } catch (Exception unused) {
        }
    }

    public static String getJarVersion() {
        return version;
    }
}
