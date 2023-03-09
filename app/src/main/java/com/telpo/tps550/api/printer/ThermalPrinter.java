package com.telpo.tps550.api.printer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Build;
import android.util.Log;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.telpo.tps550.api.DeviceAlreadyOpenException;
import com.telpo.tps550.api.DeviceNotOpenException;
import com.telpo.tps550.api.ErrorCode;
import com.telpo.tps550.api.InternalErrorException;
import com.telpo.tps550.api.TelpoException;
import com.telpo.tps550.api.iccard.NotEnoughBufferException;
import com.telpo.tps550.api.serial.Serial;
import com.telpo.tps550.api.util.CheckC1Bprinter;
import com.telpo.tps550.api.util.ReaderUtils;
import com.telpo.tps550.api.util.ShellUtils;
import com.telpo.tps550.api.util.StringUtil;
import com.telpo.tps550.api.util.SystemUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;

/* loaded from: classes.dex */
public class ThermalPrinter {
    public static final int ALGIN_LEFT = 0;
    public static final int ALGIN_MIDDLE = 1;
    public static final int ALGIN_RIGHT = 2;
    private static final int ARGB_MASK_BLUE = 255;
    private static final int ARGB_MASK_GREEN = 65280;
    private static final int ARGB_MASK_RED = 16711680;
    public static final int BARCODE_TYPE_CODABAR = 71;
    public static final int BARCODE_TYPE_CODE128 = 73;
    public static final int BARCODE_TYPE_CODE39 = 69;
    public static final int BARCODE_TYPE_CODE93 = 72;
    public static final int BARCODE_TYPE_EAN13 = 67;
    public static final int BARCODE_TYPE_EAN8 = 68;
    public static final int BARCODE_TYPE_ITF = 70;
    public static final int BARCODE_TYPE_UPCA = 65;
    public static final int BARCODE_TYPE_UPCE = 66;
    public static final int DIRECTION_BACK = 1;
    public static final int DIRECTION_FORWORD = 0;
    private static final String FILE_NAME = "/sdcard/tpsdk/printerVersion.txt";
    private static final int RGB565_MASK_BLUE = 31;
    private static final int RGB565_MASK_GREEN = 2016;
    private static final int RGB565_MASK_RED = 63488;
    public static final int STATUS_NO_PAPER = 1;
    public static final int STATUS_OK = 0;
    public static final int STATUS_OVER_FLOW = 3;
    public static final int STATUS_OVER_HEAT = 2;
    public static final int STATUS_UNKNOWN = 4;
    private static final String TAG = "ThermalPrinter";
    private static final String TPS550A_FILE_NAME = "/sdcard/tpsdk/tps550APrinterVersion.txt";
    public static final int WALK_DOTLINE = 0;
    public static final int WALK_LINE = 1;
    private static final int color = 128;
    public static boolean errorStop = false;
    private static UsbThermalPrinter mUsbThermalPrinter = null;
    private static boolean openFlag = false;
    private static OutputStream outputStream650t = null;
    private static int printerCheck = -1;
    private static byte[] sy581CMD = null;
    private static String sy581String = "";
    private static int tps550aPrinterCheck = -1;
    private static boolean tps650t_print_logo_complete = false;
    private static byte[] waitingsend = null;
    private static int writeCount = 0;
    public static boolean xon = true;

    protected static native int add_barcode(byte[] bArr, int i);

    protected static native int add_string(byte[] bArr, int i);

    protected static native int algin(int i);

    protected static native int check_status();

    protected static native int clear_string();

    protected static native int device_close(boolean z);

    protected static native int device_open(boolean z);

    protected static native int enlarge(int i, int i2);

    protected static native int get_printer_type();

    protected static native int get_version(byte[] bArr);

    protected static native int gray(int i);

    protected static native int highlight(boolean z);

    protected static native int indent(int i);

    protected static native int init();

    protected static native int line_space(int i);

    protected static native int paper_cut();

    protected static native int paper_cut_all();

    protected static native int print_and_walk(int i, int i2, int i3);

    protected static native int print_barcode(int i, byte[] bArr, int i2);

    protected static native int print_logo(int i, int i2, byte[] bArr);

    protected static native int printer_powerOff();

    protected static native int printer_powerOn();

    protected static native int search_mark(int i, int i2, int i3);

    protected static native int send_command(byte[] bArr, int i);

    protected static native int set_bold(int i);

    protected static native int set_font(int i);

    protected static native void sleep_ms(int i);

    protected static native int walk_paper(int i);

    protected static TelpoException getException(int i) {
        switch (i) {
            case ErrorCode.ERR_SYS_NO_INIT /* 61443 */:
                return new DeviceNotOpenException();
            case ErrorCode.ERR_SYS_ALREADY_INIT /* 61444 */:
                return new DeviceAlreadyOpenException();
            case ErrorCode.ERR_SYS_OVER_FLOW /* 61445 */:
                return new NotEnoughBufferException();
            case ErrorCode.ERR_SYS_UNEXPECT /* 61448 */:
                return new InternalErrorException();
            case ErrorCode.ERR_PRN_NO_PAPER /* 61697 */:
                return new NoPaperException();
            case ErrorCode.ERR_PRN_OVER_TEMP /* 61698 */:
                return new OverHeatException();
            case ErrorCode.ERR_PRN_GATE_OPEN /* 61701 */:
                return new GateOpenException();
            case ErrorCode.ERR_PRN_NOT_CUT /* 61702 */:
                return new PaperCutException();
            default:
                return new InternalErrorException();
        }
    }

    public ThermalPrinter() {
    }

    public ThermalPrinter(Context context) {
        if (!new File(TPS550A_FILE_NAME).exists() || getFileContent(TPS550A_FILE_NAME) == null) {
            UsbThermalPrinter usbThermalPrinter = new UsbThermalPrinter(context);
            mUsbThermalPrinter = usbThermalPrinter;
            try {
                usbThermalPrinter.start(1);
            } catch (TelpoException e) {
                e.printStackTrace();
            }
            if (ShellUtils.execCommand("cat /sys/kernel/debug/usb/devices", false).successMsg.contains("USB Thermal Printer")) {
                tps550aPrinterCheck = 0;
            } else {
                tps550aPrinterCheck = 1;
            }
            writeData();
            try {
                mUsbThermalPrinter.start(0);
            } catch (TelpoException e2) {
                e2.printStackTrace();
            }
            mUsbThermalPrinter = null;
        }
        String fileContent = getFileContent(TPS550A_FILE_NAME);
        Log.d("tagg", "fileContent[" + fileContent + "]");
        if (fileContent != null) {
            if (fileContent.equals("USB_PRINTER")) {
                mUsbThermalPrinter = new UsbThermalPrinter(context);
                Log.d("tagg", "USB_PRINTER > new UsbThermalPrinter");
                return;
            }
            mUsbThermalPrinter = null;
            Log.d("tagg", "mUsbThermalPrinter == null");
        }
    }

    private static boolean isSY581Printer() {
        if (SystemUtil.getDeviceType() == StringUtil.DeviceModelEnum.TPS680.ordinal() || SystemUtil.getDeviceType() == StringUtil.DeviceModelEnum.C1B.ordinal() || SystemUtil.getDeviceType() == StringUtil.DeviceModelEnum.TPS650P.ordinal()) {
            return true;
        }
        if (SystemUtil.getDeviceType() == StringUtil.DeviceModelEnum.TPS650T.ordinal() && SystemUtil.tps650t_is_sy581) {
            return true;
        }
        return getFileContent(FILE_NAME) != null && getFileContent(FILE_NAME).equals("SY581");
    }

    public boolean isUsbType(Context context) {
        try {
            new UsbThermalPrinter(context).start(1);
        } catch (TelpoException e) {
            e.printStackTrace();
        }
        return ShellUtils.execCommand("cat /sys/kernel/debug/usb/devices", false).successMsg.contains("USB Thermal Printer");
    }

    private static void writeData() {
        Log.d(TAG, "printerCheck:" + printerCheck);
        Log.d(TAG, "tps550aPrinterCheck:" + tps550aPrinterCheck);
        int i = printerCheck;
        if (i == 8) {
            writeTxtToFile("SY581", FILE_NAME);
        } else if (i == 7) {
            writeTxtToFile("PT72", FILE_NAME);
        } else {
            int i2 = tps550aPrinterCheck;
            if (i2 == 0) {
                writeTxtToFile("USB_PRINTER", TPS550A_FILE_NAME);
            } else if (i2 != 1) {
            } else {
                writeTxtToFile("SERIAL_PRINTER", TPS550A_FILE_NAME);
            }
        }
    }

    private static void writeTxtToFile(String str, String str2) {
        try {
            File file = new File(str2);
            if (!file.exists()) {
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

    private static synchronized String getVersionNum(String str) {
        String str2;
        int parseInt;
        synchronized (ThermalPrinter.class) {
            try {
                String substring = str.substring(str.indexOf("14") + 2, str.indexOf("14") + 4);
                Log.d(TAG, "realVersion:" + substring);
                Log.d(TAG, "nowVersion:" + (Integer.parseInt(substring) - Integer.parseInt("91")));
                str2 = "1." + (parseInt + 45);
            } catch (Exception unused) {
                str2 = null;
            }
        }
        return str2;
    }

    public static synchronized void start() throws TelpoException {
        synchronized (ThermalPrinter.class) {
            UsbThermalPrinter usbThermalPrinter = mUsbThermalPrinter;
            if (usbThermalPrinter != null) {
                usbThermalPrinter.start(1);
            } else if (openFlag) {
                throw new DeviceAlreadyOpenException();
            } else {
                int device_open = device_open(true);
                if (device_open == 0) {
                    openFlag = true;
                } else {
                    throw getException(device_open);
                }
            }
        }
    }

    public static synchronized void start(boolean z) throws TelpoException {
        synchronized (ThermalPrinter.class) {
            UsbThermalPrinter usbThermalPrinter = mUsbThermalPrinter;
            if (usbThermalPrinter != null) {
                usbThermalPrinter.start(1);
            } else if (openFlag) {
                throw new DeviceAlreadyOpenException();
            } else {
                int device_open = device_open(z);
                if (device_open == 0) {
                    openFlag = true;
                } else {
                    throw getException(device_open);
                }
            }
        }
    }

    public static synchronized void start(Context context) throws TelpoException {
        synchronized (ThermalPrinter.class) {
            UsbThermalPrinter usbThermalPrinter = mUsbThermalPrinter;
            if (usbThermalPrinter != null) {
                usbThermalPrinter.start(1);
            } else if (openFlag) {
                throw new DeviceAlreadyOpenException();
            } else {
                int device_open = device_open(true);
                if (device_open == 0) {
                    Log.d("tagg", "printer start success");
                    openFlag = true;
                    context.sendBroadcast(new Intent("com.telpo.printer.thermalprinter.start"));
                } else {
                    Log.d("tagg", "printer start fail");
                    throw getException(device_open);
                }
            }
        }
    }

    public static synchronized void start(Context context, boolean z) throws TelpoException {
        synchronized (ThermalPrinter.class) {
            UsbThermalPrinter usbThermalPrinter = mUsbThermalPrinter;
            if (usbThermalPrinter != null) {
                usbThermalPrinter.start(1);
            } else if (openFlag) {
                throw new DeviceAlreadyOpenException();
            } else {
                int device_open = device_open(z);
                if (device_open == 0) {
                    openFlag = true;
                    context.sendBroadcast(new Intent("com.telpo.printer.thermalprinter.start"));
                } else {
                    throw getException(device_open);
                }
            }
        }
    }

    public static synchronized void printerPowerOn() {
        synchronized (ThermalPrinter.class) {
            printer_powerOn();
        }
    }

    public static synchronized void reset() throws TelpoException {
        synchronized (ThermalPrinter.class) {
            UsbThermalPrinter usbThermalPrinter = mUsbThermalPrinter;
            if (usbThermalPrinter != null) {
                usbThermalPrinter.reset();
            } else if (!openFlag) {
                throw new DeviceNotOpenException();
            } else {
                if (isSY581Printer()) {
                    sy581CMD = ReaderUtils.merge(sy581CMD, ThermalPrinterSY581.resetCMD());
                } else {
                    int init = init();
                    if (init != 0) {
                        Log.d("tagg", "printer start fail");
                        throw getException(init);
                    }
                    Log.d("tagg", "printer reset success");
                }
            }
        }
    }

    public static synchronized void clearSerialSY581() throws TelpoException {
        synchronized (ThermalPrinter.class) {
            if (!openFlag) {
                throw new DeviceNotOpenException();
            }
            if (isSY581Printer()) {
                ThermalPrinterSY581.clearSerial();
            }
        }
    }

    public static synchronized void walkPaper(int i) throws TelpoException {
        synchronized (ThermalPrinter.class) {
            UsbThermalPrinter usbThermalPrinter = mUsbThermalPrinter;
            if (usbThermalPrinter != null) {
                usbThermalPrinter.walkPaper(i);
            } else if (!openFlag) {
                throw new DeviceNotOpenException();
            } else {
                if (i <= 0) {
                    throw new IllegalArgumentException();
                }
                if (SystemUtil.getDeviceType() == StringUtil.DeviceModelEnum.TPS681.ordinal()) {
                    walkPaper681(i);
                } else if (isSY581Printer()) {
                    ThermalPrinterSY581.walkPaper(i);
                } else {
                    int walk_paper = walk_paper(i);
                    if (walk_paper != 0) {
                        throw getException(walk_paper);
                    }
                }
            }
        }
    }

    public static synchronized void walkPaper681(int i) {
        int i2;
        synchronized (ThermalPrinter.class) {
            try {
                StringBuffer stringBuffer = new StringBuffer();
                if (i >= 10) {
                    i2 = i / 10;
                } else {
                    i2 = i % 10;
                }
                for (int i3 = 0; i3 < i2; i3++) {
                    stringBuffer.append(" \n");
                }
                reset();
                addString(stringBuffer.toString());
                printString();
            } catch (TelpoException e) {
                e.printStackTrace();
            }
        }
    }

    public static synchronized void stop() {
        synchronized (ThermalPrinter.class) {
            UsbThermalPrinter usbThermalPrinter = mUsbThermalPrinter;
            if (usbThermalPrinter != null) {
                usbThermalPrinter.stop();
            } else if (openFlag) {
                device_close(true);
                openFlag = false;
            }
        }
    }

    public static synchronized void stop(boolean z) {
        synchronized (ThermalPrinter.class) {
            UsbThermalPrinter usbThermalPrinter = mUsbThermalPrinter;
            if (usbThermalPrinter != null) {
                usbThermalPrinter.stop();
            } else if (openFlag) {
                device_close(z);
                openFlag = false;
            }
        }
    }

    public static synchronized void stop(Context context) {
        synchronized (ThermalPrinter.class) {
            UsbThermalPrinter usbThermalPrinter = mUsbThermalPrinter;
            if (usbThermalPrinter != null) {
                usbThermalPrinter.stop();
            } else if (openFlag) {
                device_close(true);
                openFlag = false;
                context.sendBroadcast(new Intent("com.telpo.printer.thermalprinter.stop"));
            }
        }
    }

    public static synchronized void stop(Context context, boolean z) {
        synchronized (ThermalPrinter.class) {
            UsbThermalPrinter usbThermalPrinter = mUsbThermalPrinter;
            if (usbThermalPrinter != null) {
                usbThermalPrinter.stop();
            } else if (openFlag) {
                device_close(z);
                openFlag = false;
                context.sendBroadcast(new Intent("com.telpo.printer.thermalprinter.stop"));
            }
        }
    }

    public static synchronized void printerPowerOff() {
        synchronized (ThermalPrinter.class) {
            printer_powerOff();
        }
    }

    public static synchronized int checkStatus() throws TelpoException {
        int i;
        synchronized (ThermalPrinter.class) {
            UsbThermalPrinter usbThermalPrinter = mUsbThermalPrinter;
            if (usbThermalPrinter != null) {
                return usbThermalPrinter.checkStatus();
            } else if (!openFlag) {
                throw new DeviceNotOpenException();
            } else {
                if (isSY581Printer()) {
                    sy581CMD = ReaderUtils.merge(sy581CMD, ThermalPrinterSY581.checkStatusCMD());
                    i = 0;
                } else {
                    i = check_status();
                }
                if (i == 0) {
                    return 0;
                }
                if (i == 61445) {
                    return 3;
                }
                switch (i) {
                    case ErrorCode.ERR_PRN_NO_PAPER /* 61697 */:
                        return 1;
                    case ErrorCode.ERR_PRN_OVER_TEMP /* 61698 */:
                        return 2;
                    default:
                        return 4;
                }
            }
        }
    }

    public static synchronized void enlargeFontSize(int i, int i2) throws TelpoException {
        int enlarge;
        synchronized (ThermalPrinter.class) {
            UsbThermalPrinter usbThermalPrinter = mUsbThermalPrinter;
            if (usbThermalPrinter != null) {
                usbThermalPrinter.enlargeFontSize(i, i2);
            } else if (!openFlag) {
                throw new DeviceNotOpenException();
            } else {
                if (!isSY581Printer() && (enlarge = enlarge(i, i2)) != 0) {
                    throw getException(enlarge);
                }
            }
        }
    }

    public static synchronized void setFontSize(int i) throws TelpoException {
        synchronized (ThermalPrinter.class) {
            UsbThermalPrinter usbThermalPrinter = mUsbThermalPrinter;
            if (usbThermalPrinter != null) {
                usbThermalPrinter.setFontSize(i);
            } else if (!openFlag) {
                throw new DeviceNotOpenException();
            } else {
                if (isSY581Printer()) {
                    sy581CMD = ReaderUtils.merge(sy581CMD, ThermalPrinterSY581.setFontCMD(i));
                } else {
                    int i2 = set_font(i);
                    if (i2 != 0) {
                        throw getException(i2);
                    }
                }
            }
        }
    }

    public static synchronized void setTem(int i) throws TelpoException {
        synchronized (ThermalPrinter.class) {
            if (!openFlag) {
                throw new DeviceNotOpenException();
            }
            if (isSY581Printer()) {
                ThermalPrinterSY581.setTem(i);
            }
        }
    }

    public static synchronized void setHighlight(boolean z) throws TelpoException {
        int highlight;
        synchronized (ThermalPrinter.class) {
            UsbThermalPrinter usbThermalPrinter = mUsbThermalPrinter;
            if (usbThermalPrinter != null) {
                usbThermalPrinter.setHighlight(z);
            } else if (!openFlag) {
                throw new DeviceNotOpenException();
            } else {
                if (!isSY581Printer() && (highlight = highlight(z)) != 0) {
                    throw getException(highlight);
                }
            }
        }
    }

    public static synchronized void setGray(int i) throws TelpoException {
        synchronized (ThermalPrinter.class) {
            UsbThermalPrinter usbThermalPrinter = mUsbThermalPrinter;
            if (usbThermalPrinter != null) {
                usbThermalPrinter.setGray(i);
            } else if (!openFlag) {
                throw new DeviceNotOpenException();
            } else {
                if (isSY581Printer()) {
                    sy581CMD = ReaderUtils.merge(sy581CMD, ThermalPrinterSY581.setGrayCMD(i));
                } else {
                    int gray = gray(i);
                    if (gray != 0) {
                        throw getException(gray);
                    }
                }
            }
        }
    }

    public static synchronized void setAlgin(int i) throws TelpoException {
        synchronized (ThermalPrinter.class) {
            UsbThermalPrinter usbThermalPrinter = mUsbThermalPrinter;
            if (usbThermalPrinter != null) {
                usbThermalPrinter.setAlgin(i);
            } else if (!openFlag) {
                throw new DeviceNotOpenException();
            } else {
                if (isSY581Printer()) {
                    sy581CMD = ReaderUtils.merge(sy581CMD, ThermalPrinterSY581.setAlignCMD(i));
                } else {
                    int algin = algin(i);
                    if (algin != 0) {
                        throw getException(algin);
                    }
                }
            }
        }
    }

    public static synchronized void addString(String str) throws TelpoException {
        synchronized (ThermalPrinter.class) {
            UsbThermalPrinter usbThermalPrinter = mUsbThermalPrinter;
            if (usbThermalPrinter != null) {
                usbThermalPrinter.addString(str);
            } else if (isSY581Printer()) {
                sy581String = String.valueOf(sy581String) + str;
            } else if (!openFlag) {
                throw new DeviceNotOpenException();
            } else {
                if (str == null || str.length() == 0) {
                    throw new NullPointerException();
                }
                if (isSY581Printer()) {
                    str = " " + str;
                }
                try {
                    byte[] bytes = str.getBytes("GBK");
                    int add_string = add_string(bytes, bytes.length);
                    if (add_string != 0) {
                        throw getException(add_string);
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    throw new InternalErrorException();
                }
            }
        }
    }

    public static synchronized void addBarcode(String str) throws TelpoException {
        int add_barcode;
        synchronized (ThermalPrinter.class) {
            if (!openFlag) {
                throw new DeviceNotOpenException();
            }
            if (str == null || str.length() == 0) {
                throw new NullPointerException();
            }
            byte[] bArr = new byte[53];
            bArr[0] = 29;
            bArr[1] = 104;
            bArr[2] = 84;
            bArr[3] = 29;
            bArr[4] = 108;
            Bitmap CreateCode = CreateCode(str, BarcodeFormat.CODE_128, 360, 108);
            int i = 5;
            int width = CreateCode.getWidth() / 8;
            int i2 = 0;
            int i3 = 0;
            while (i2 < width) {
                int i4 = 0;
                for (int i5 = 0; i5 < 8; i5++) {
                    int pixel = CreateCode.getPixel(i5 + i3, 0);
                    i4 = (((ARGB_MASK_RED & pixel) >> 16) <= 128 || ((65280 & pixel) >> 8) <= 128 || (pixel & 255) <= 128) ? (i4 << 1) + 1 : i4 << 1;
                }
                bArr[i] = (byte) i4;
                i++;
                i2++;
                i3 += 8;
            }
            if (!isSY581Printer() && (add_barcode = add_barcode(bArr, 53)) != 0) {
                throw getException(add_barcode);
            }
        }
    }

    public static synchronized void clearString() throws TelpoException {
        int clear_string;
        synchronized (ThermalPrinter.class) {
            UsbThermalPrinter usbThermalPrinter = mUsbThermalPrinter;
            if (usbThermalPrinter != null) {
                usbThermalPrinter.clearString();
            } else if (!openFlag) {
                throw new DeviceNotOpenException();
            } else {
                if (!isSY581Printer() && (clear_string = clear_string()) != 0) {
                    throw getException(clear_string);
                }
            }
        }
    }

    public static synchronized void printString() throws TelpoException {
        synchronized (ThermalPrinter.class) {
            UsbThermalPrinter usbThermalPrinter = mUsbThermalPrinter;
            if (usbThermalPrinter != null) {
                usbThermalPrinter.printString();
            } else if (isSY581Printer()) {
                printStringSY581();
                sy581String = "";
                sy581CMD = null;
            } else if (!openFlag) {
                throw new DeviceNotOpenException();
            } else {
                int print_and_walk = print_and_walk(0, 0, 0);
                if (print_and_walk != 0) {
                    throw getException(print_and_walk);
                }
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:29:0x0085  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x00cc  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x00fe  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x00ba A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:74:0x0093 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:81:0x00f7 A[EDGE_INSN: B:81:0x00f7->B:50:0x00f7 ?: BREAK  , SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static void printStringSY581() {
        /*
            Method dump skipped, instructions count: 266
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.telpo.tps550.api.printer.ThermalPrinter.printStringSY581():void");
    }

    /* JADX WARN: Code restructure failed: missing block: B:16:0x001d, code lost:
        if (r3 != 0) goto L18;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0025, code lost:
        throw new java.lang.IllegalArgumentException();
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static synchronized void printStringAndWalk(int r2, int r3, int r4) throws com.telpo.tps550.api.TelpoException {
        /*
            java.lang.Class<com.telpo.tps550.api.printer.ThermalPrinter> r0 = com.telpo.tps550.api.printer.ThermalPrinter.class
            monitor-enter(r0)
            com.telpo.tps550.api.printer.UsbThermalPrinter r1 = com.telpo.tps550.api.printer.ThermalPrinter.mUsbThermalPrinter     // Catch: java.lang.Throwable -> L39
            if (r1 == 0) goto Lb
            r1.printStringAndWalk(r2, r3, r4)     // Catch: java.lang.Throwable -> L39
            goto L2c
        Lb:
            boolean r1 = com.telpo.tps550.api.printer.ThermalPrinter.openFlag     // Catch: java.lang.Throwable -> L39
            if (r1 == 0) goto L33
            r1 = 1
            if (r2 == r1) goto L1b
            if (r2 != 0) goto L15
            goto L1b
        L15:
            java.lang.IllegalArgumentException r2 = new java.lang.IllegalArgumentException     // Catch: java.lang.Throwable -> L39
            r2.<init>()     // Catch: java.lang.Throwable -> L39
            throw r2     // Catch: java.lang.Throwable -> L39
        L1b:
            if (r3 == r1) goto L26
            if (r3 != 0) goto L20
            goto L26
        L20:
            java.lang.IllegalArgumentException r2 = new java.lang.IllegalArgumentException     // Catch: java.lang.Throwable -> L39
            r2.<init>()     // Catch: java.lang.Throwable -> L39
            throw r2     // Catch: java.lang.Throwable -> L39
        L26:
            int r2 = print_and_walk(r2, r3, r4)     // Catch: java.lang.Throwable -> L39
            if (r2 != 0) goto L2e
        L2c:
            monitor-exit(r0)
            return
        L2e:
            com.telpo.tps550.api.TelpoException r2 = getException(r2)     // Catch: java.lang.Throwable -> L39
            throw r2     // Catch: java.lang.Throwable -> L39
        L33:
            com.telpo.tps550.api.DeviceNotOpenException r2 = new com.telpo.tps550.api.DeviceNotOpenException     // Catch: java.lang.Throwable -> L39
            r2.<init>()     // Catch: java.lang.Throwable -> L39
            throw r2     // Catch: java.lang.Throwable -> L39
        L39:
            r2 = move-exception
            monitor-exit(r0)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.telpo.tps550.api.printer.ThermalPrinter.printStringAndWalk(int, int, int):void");
    }

    public static synchronized void setLineSpace(int i) throws TelpoException {
        synchronized (ThermalPrinter.class) {
            UsbThermalPrinter usbThermalPrinter = mUsbThermalPrinter;
            if (usbThermalPrinter != null) {
                usbThermalPrinter.setLineSpace(i);
            } else if (!openFlag) {
                throw new DeviceNotOpenException();
            } else {
                if (i < 0 || i > 255) {
                    throw new IllegalArgumentException();
                }
                if (isSY581Printer()) {
                    sy581CMD = ReaderUtils.merge(sy581CMD, ThermalPrinterSY581.setLineSpaceCMD(i));
                } else {
                    int line_space = line_space(i);
                    if (line_space != 0) {
                        throw getException(line_space);
                    }
                }
            }
        }
    }

    public static synchronized void setLeftIndent(int i) throws TelpoException {
        synchronized (ThermalPrinter.class) {
            UsbThermalPrinter usbThermalPrinter = mUsbThermalPrinter;
            if (usbThermalPrinter != null) {
                usbThermalPrinter.setLeftIndent(i);
            } else if (!openFlag) {
                throw new DeviceNotOpenException();
            } else {
                if (i < 0 || i > 255) {
                    throw new IllegalArgumentException();
                }
                if (isSY581Printer()) {
                    sy581CMD = ReaderUtils.merge(sy581CMD, ThermalPrinterSY581.setLeftDistanceCMD(i));
                } else {
                    int indent = indent(i);
                    if (indent != 0) {
                        throw getException(indent);
                    }
                }
            }
        }
    }

    public static synchronized void printLogo581Left(int i, Bitmap bitmap) {
        synchronized (ThermalPrinter.class) {
            printLogo581SetLeft(i, bitmap);
        }
    }

    /* JADX WARN: Can't wrap try/catch for region: R(27:4|(6:86|5|(1:7)(2:8|(1:10)(2:11|(1:13)(1:14)))|90|94|100)|25|(1:27)(1:28)|88|29|(1:31)(2:32|(2:34|(1:36)(1:37))(1:(2:39|(1:41)(1:42))(2:(1:44)|45)))|46|(1:48)|49|(1:51)|52|(1:54)|55|(1:57)|58|(2:61|62)|102|60|65|(7:96|79|104|(4:103|81|82|107)(1:106)|105|66|67)|92|69|(1:73)|98|74|77) */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x01a9, code lost:
        r12 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x01aa, code lost:
        r12.printStackTrace();
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x01bf, code lost:
        r13 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x01c0, code lost:
        r13.printStackTrace();
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x01cc, code lost:
        r12 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x01cd, code lost:
        r12.printStackTrace();
     */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0067 A[Catch: all -> 0x0046, TRY_LEAVE, TryCatch #2 {, blocks: (B:5:0x0005, B:7:0x0014, B:8:0x001c, B:10:0x0024, B:11:0x002e, B:13:0x0036, B:14:0x003e, B:17:0x004a, B:19:0x004f, B:21:0x0054, B:23:0x0059, B:25:0x005d, B:27:0x0067, B:29:0x0078, B:31:0x008b, B:34:0x0094, B:36:0x009d, B:37:0x00aa, B:39:0x00c0, B:41:0x00c8, B:42:0x00d5, B:45:0x00eb, B:46:0x00f7, B:48:0x012a, B:49:0x0139, B:51:0x013f, B:52:0x014e, B:54:0x0154, B:55:0x0163, B:57:0x0169, B:58:0x0178, B:61:0x01a0, B:64:0x01aa, B:65:0x01ad, B:66:0x01b3, B:69:0x01b9, B:71:0x01c0, B:73:0x01c5, B:74:0x01c8, B:76:0x01cd, B:79:0x01d2, B:81:0x01d6, B:84:0x01dd), top: B:86:0x0005, inners: #3, #4, #5, #6, #7, #8, #9 }] */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0077  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x008b A[Catch: all -> 0x0046, TelpoException -> 0x01a9, TryCatch #3 {TelpoException -> 0x01a9, blocks: (B:29:0x0078, B:31:0x008b, B:34:0x0094, B:36:0x009d, B:37:0x00aa, B:39:0x00c0, B:41:0x00c8, B:42:0x00d5, B:45:0x00eb, B:46:0x00f7, B:48:0x012a, B:49:0x0139, B:51:0x013f, B:52:0x014e, B:54:0x0154, B:55:0x0163, B:57:0x0169, B:58:0x0178, B:61:0x01a0), top: B:88:0x0078, outer: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0090  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x012a A[Catch: all -> 0x0046, TelpoException -> 0x01a9, TryCatch #3 {TelpoException -> 0x01a9, blocks: (B:29:0x0078, B:31:0x008b, B:34:0x0094, B:36:0x009d, B:37:0x00aa, B:39:0x00c0, B:41:0x00c8, B:42:0x00d5, B:45:0x00eb, B:46:0x00f7, B:48:0x012a, B:49:0x0139, B:51:0x013f, B:52:0x014e, B:54:0x0154, B:55:0x0163, B:57:0x0169, B:58:0x0178, B:61:0x01a0), top: B:88:0x0078, outer: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:51:0x013f A[Catch: all -> 0x0046, TelpoException -> 0x01a9, TryCatch #3 {TelpoException -> 0x01a9, blocks: (B:29:0x0078, B:31:0x008b, B:34:0x0094, B:36:0x009d, B:37:0x00aa, B:39:0x00c0, B:41:0x00c8, B:42:0x00d5, B:45:0x00eb, B:46:0x00f7, B:48:0x012a, B:49:0x0139, B:51:0x013f, B:52:0x014e, B:54:0x0154, B:55:0x0163, B:57:0x0169, B:58:0x0178, B:61:0x01a0), top: B:88:0x0078, outer: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:54:0x0154 A[Catch: all -> 0x0046, TelpoException -> 0x01a9, TryCatch #3 {TelpoException -> 0x01a9, blocks: (B:29:0x0078, B:31:0x008b, B:34:0x0094, B:36:0x009d, B:37:0x00aa, B:39:0x00c0, B:41:0x00c8, B:42:0x00d5, B:45:0x00eb, B:46:0x00f7, B:48:0x012a, B:49:0x0139, B:51:0x013f, B:52:0x014e, B:54:0x0154, B:55:0x0163, B:57:0x0169, B:58:0x0178, B:61:0x01a0), top: B:88:0x0078, outer: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0169 A[Catch: all -> 0x0046, TelpoException -> 0x01a9, TryCatch #3 {TelpoException -> 0x01a9, blocks: (B:29:0x0078, B:31:0x008b, B:34:0x0094, B:36:0x009d, B:37:0x00aa, B:39:0x00c0, B:41:0x00c8, B:42:0x00d5, B:45:0x00eb, B:46:0x00f7, B:48:0x012a, B:49:0x0139, B:51:0x013f, B:52:0x014e, B:54:0x0154, B:55:0x0163, B:57:0x0169, B:58:0x0178, B:61:0x01a0), top: B:88:0x0078, outer: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:61:0x01a0 A[Catch: all -> 0x0046, TelpoException -> 0x01a9, TRY_LEAVE, TryCatch #3 {TelpoException -> 0x01a9, blocks: (B:29:0x0078, B:31:0x008b, B:34:0x0094, B:36:0x009d, B:37:0x00aa, B:39:0x00c0, B:41:0x00c8, B:42:0x00d5, B:45:0x00eb, B:46:0x00f7, B:48:0x012a, B:49:0x0139, B:51:0x013f, B:52:0x014e, B:54:0x0154, B:55:0x0163, B:57:0x0169, B:58:0x0178, B:61:0x01a0), top: B:88:0x0078, outer: #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:73:0x01c5 A[Catch: all -> 0x0046, TRY_LEAVE, TryCatch #2 {, blocks: (B:5:0x0005, B:7:0x0014, B:8:0x001c, B:10:0x0024, B:11:0x002e, B:13:0x0036, B:14:0x003e, B:17:0x004a, B:19:0x004f, B:21:0x0054, B:23:0x0059, B:25:0x005d, B:27:0x0067, B:29:0x0078, B:31:0x008b, B:34:0x0094, B:36:0x009d, B:37:0x00aa, B:39:0x00c0, B:41:0x00c8, B:42:0x00d5, B:45:0x00eb, B:46:0x00f7, B:48:0x012a, B:49:0x0139, B:51:0x013f, B:52:0x014e, B:54:0x0154, B:55:0x0163, B:57:0x0169, B:58:0x0178, B:61:0x01a0, B:64:0x01aa, B:65:0x01ad, B:66:0x01b3, B:69:0x01b9, B:71:0x01c0, B:73:0x01c5, B:74:0x01c8, B:76:0x01cd, B:79:0x01d2, B:81:0x01d6, B:84:0x01dd), top: B:86:0x0005, inners: #3, #4, #5, #6, #7, #8, #9 }] */
    /* JADX WARN: Removed duplicated region for block: B:96:0x01d2 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static synchronized void printLogo581SetLeft(int r12, android.graphics.Bitmap r13) {
        /*
            Method dump skipped, instructions count: 483
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.telpo.tps550.api.printer.ThermalPrinter.printLogo581SetLeft(int, android.graphics.Bitmap):void");
    }

    public static Bitmap toGrayscale(Bitmap bitmap) {
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0.0f);
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
        return createBitmap;
    }

    private static synchronized void printLogo581CMD_650T(Bitmap bitmap) {
        Serial serial;
        synchronized (ThermalPrinter.class) {
            Log.d("PrinterActivitySY581", "printLogo581CMD_650T");
            try {
                tps650t_print_logo_complete = false;
                writeCount = 0;
                if ("TPS650T".equals(SystemUtil.getInternalModel())) {
                    serial = new Serial("/dev/ttyS0", 460800, 0);
                } else if ("C1B".equals(SystemUtil.getInternalModel())) {
                    serial = new Serial(CheckC1Bprinter.checkSerialFromFile(), 460800, 0);
                } else {
                    serial = new Serial("/dev/ttyS4", 460800, 0);
                }
                outputStream650t = serial.getOutputStream();
                if (bitmap.getWidth() % 8 != 0) {
                    bitmap = zoomImg(bitmap, ((bitmap.getWidth() / 8) + 1) * 8, bitmap.getHeight());
                }
                String hexString = Integer.toHexString((bitmap.getWidth() / 8) % 256);
                String hexString2 = Integer.toHexString((bitmap.getWidth() / 8) / 256);
                String hexString3 = Integer.toHexString(bitmap.getHeight() % 256);
                String hexString4 = Integer.toHexString(bitmap.getHeight() / 256);
                if (hexString.length() == 1) {
                    hexString = "0" + hexString;
                }
                if (hexString2.length() == 1) {
                    hexString2 = "0" + hexString2;
                }
                if (hexString3.length() == 1) {
                    hexString3 = "0" + hexString3;
                }
                if (hexString4.length() == 1) {
                    hexString4 = "0" + hexString4;
                }
                outputStream650t.write(new byte[]{29, 118, 48, 0, parseHexStr2Byte(hexString)[0], parseHexStr2Byte(hexString2)[0], parseHexStr2Byte(hexString3)[0], parseHexStr2Byte(hexString4)[0]});
                changeBitmap(bitmap, new BitmapDecodeCallback() { // from class: com.telpo.tps550.api.printer.ThermalPrinter.1
                    @Override // com.telpo.tps550.api.printer.BitmapDecodeCallback
                    public void decoding(byte b) {
                    }

                    @Override // com.telpo.tps550.api.printer.BitmapDecodeCallback
                    public void decodeComplete() {
                        ThermalPrinter.tps650t_print_logo_complete = true;
                    }

                    @Override // com.telpo.tps550.api.printer.BitmapDecodeCallback
                    public void decodingByte(byte[] bArr) {
                        while (!ThermalPrinter.xon) {
                            if (ThermalPrinter.tps650t_print_logo_complete) {
                                return;
                            }
                        }
                        try {
                            ThermalPrinter.writeCount++;
                            ThermalPrinter.outputStream650t.write(bArr);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                serial.close();
                OutputStream outputStream = outputStream650t;
                if (outputStream != null) {
                    outputStream.close();
                    outputStream650t = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* JADX WARN: Can't wrap try/catch for region: R(29:12|(7:99|13|(1:15)(2:16|(1:18)(2:19|(1:21)(1:22)))|98|107|111|113)|32|(1:34)|35|36|109|37|(1:39)|40|(1:42)|43|(1:45)|46|(1:48)|49|(2:52|53)|115|51|56|(3:57|(5:103|63|(3:122|65|(3:120|67|118)(4:123|68|69|125))(2:119|(1:1)(1:126))|124|58)|121)|130|(2:73|(2:76|(1:129))(2:128|75))|84|(1:86)|101|87|90|91) */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x01a0, code lost:
        r15 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x01a1, code lost:
        r15.printStackTrace();
     */
    /* JADX WARN: Code restructure failed: missing block: B:79:0x023a, code lost:
        if (com.telpo.tps550.api.printer.ThermalPrinter.errorStop == false) goto L105;
     */
    /* JADX WARN: Code restructure failed: missing block: B:80:0x023c, code lost:
        android.util.Log.d("PrinterActivitySY581", "errorStop[" + com.telpo.tps550.api.printer.ThermalPrinter.errorStop + "] break");
     */
    /* JADX WARN: Code restructure failed: missing block: B:81:0x0259, code lost:
        r1 = com.telpo.tps550.api.printer.ThermalPrinter.sy581CMD;
        r15.write(r1, r2, r1.length - r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:82:0x0261, code lost:
        r1 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:83:0x0262, code lost:
        r1.printStackTrace();
     */
    /* JADX WARN: Code restructure failed: missing block: B:88:0x0275, code lost:
        r15 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:89:0x0276, code lost:
        r15.printStackTrace();
     */
    /* JADX WARN: Removed duplicated region for block: B:119:0x0222 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:122:0x01f9 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:128:0x0233 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:34:0x00bc A[Catch: all -> 0x0280, TryCatch #0 {, blocks: (B:4:0x0003, B:6:0x000f, B:8:0x001b, B:10:0x0027, B:13:0x0037, B:15:0x0046, B:16:0x004e, B:18:0x0056, B:19:0x0060, B:21:0x0068, B:22:0x0070, B:24:0x0079, B:26:0x007e, B:28:0x0083, B:30:0x0088, B:32:0x008c, B:34:0x00bc, B:35:0x00cb, B:37:0x00d4, B:39:0x0122, B:40:0x0131, B:42:0x0137, B:43:0x0146, B:45:0x014c, B:46:0x015b, B:48:0x0161, B:49:0x0170, B:52:0x0197, B:55:0x01a1, B:56:0x01a4, B:58:0x01e1, B:60:0x01eb, B:63:0x01f5, B:65:0x01f9, B:67:0x01fd, B:68:0x021a, B:72:0x0226, B:73:0x022a, B:76:0x0234, B:78:0x0238, B:80:0x023c, B:81:0x0259, B:83:0x0262, B:84:0x0265, B:86:0x026e, B:87:0x0271, B:89:0x0276, B:92:0x027b), top: B:97:0x0003, inners: #1, #2, #3, #4, #5, #6, #7, #8 }] */
    /* JADX WARN: Removed duplicated region for block: B:39:0x0122 A[Catch: TelpoException -> 0x01a0, all -> 0x0280, TryCatch #6 {TelpoException -> 0x01a0, blocks: (B:37:0x00d4, B:39:0x0122, B:40:0x0131, B:42:0x0137, B:43:0x0146, B:45:0x014c, B:46:0x015b, B:48:0x0161, B:49:0x0170, B:52:0x0197), top: B:109:0x00d4, outer: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:42:0x0137 A[Catch: TelpoException -> 0x01a0, all -> 0x0280, TryCatch #6 {TelpoException -> 0x01a0, blocks: (B:37:0x00d4, B:39:0x0122, B:40:0x0131, B:42:0x0137, B:43:0x0146, B:45:0x014c, B:46:0x015b, B:48:0x0161, B:49:0x0170, B:52:0x0197), top: B:109:0x00d4, outer: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:45:0x014c A[Catch: TelpoException -> 0x01a0, all -> 0x0280, TryCatch #6 {TelpoException -> 0x01a0, blocks: (B:37:0x00d4, B:39:0x0122, B:40:0x0131, B:42:0x0137, B:43:0x0146, B:45:0x014c, B:46:0x015b, B:48:0x0161, B:49:0x0170, B:52:0x0197), top: B:109:0x00d4, outer: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:48:0x0161 A[Catch: TelpoException -> 0x01a0, all -> 0x0280, TryCatch #6 {TelpoException -> 0x01a0, blocks: (B:37:0x00d4, B:39:0x0122, B:40:0x0131, B:42:0x0137, B:43:0x0146, B:45:0x014c, B:46:0x015b, B:48:0x0161, B:49:0x0170, B:52:0x0197), top: B:109:0x00d4, outer: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:52:0x0197 A[Catch: TelpoException -> 0x01a0, all -> 0x0280, TRY_LEAVE, TryCatch #6 {TelpoException -> 0x01a0, blocks: (B:37:0x00d4, B:39:0x0122, B:40:0x0131, B:42:0x0137, B:43:0x0146, B:45:0x014c, B:46:0x015b, B:48:0x0161, B:49:0x0170, B:52:0x0197), top: B:109:0x00d4, outer: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:60:0x01eb A[Catch: all -> 0x0280, TRY_LEAVE, TryCatch #0 {, blocks: (B:4:0x0003, B:6:0x000f, B:8:0x001b, B:10:0x0027, B:13:0x0037, B:15:0x0046, B:16:0x004e, B:18:0x0056, B:19:0x0060, B:21:0x0068, B:22:0x0070, B:24:0x0079, B:26:0x007e, B:28:0x0083, B:30:0x0088, B:32:0x008c, B:34:0x00bc, B:35:0x00cb, B:37:0x00d4, B:39:0x0122, B:40:0x0131, B:42:0x0137, B:43:0x0146, B:45:0x014c, B:46:0x015b, B:48:0x0161, B:49:0x0170, B:52:0x0197, B:55:0x01a1, B:56:0x01a4, B:58:0x01e1, B:60:0x01eb, B:63:0x01f5, B:65:0x01f9, B:67:0x01fd, B:68:0x021a, B:72:0x0226, B:73:0x022a, B:76:0x0234, B:78:0x0238, B:80:0x023c, B:81:0x0259, B:83:0x0262, B:84:0x0265, B:86:0x026e, B:87:0x0271, B:89:0x0276, B:92:0x027b), top: B:97:0x0003, inners: #1, #2, #3, #4, #5, #6, #7, #8 }] */
    /* JADX WARN: Removed duplicated region for block: B:76:0x0234 A[Catch: all -> 0x0280, TryCatch #0 {, blocks: (B:4:0x0003, B:6:0x000f, B:8:0x001b, B:10:0x0027, B:13:0x0037, B:15:0x0046, B:16:0x004e, B:18:0x0056, B:19:0x0060, B:21:0x0068, B:22:0x0070, B:24:0x0079, B:26:0x007e, B:28:0x0083, B:30:0x0088, B:32:0x008c, B:34:0x00bc, B:35:0x00cb, B:37:0x00d4, B:39:0x0122, B:40:0x0131, B:42:0x0137, B:43:0x0146, B:45:0x014c, B:46:0x015b, B:48:0x0161, B:49:0x0170, B:52:0x0197, B:55:0x01a1, B:56:0x01a4, B:58:0x01e1, B:60:0x01eb, B:63:0x01f5, B:65:0x01f9, B:67:0x01fd, B:68:0x021a, B:72:0x0226, B:73:0x022a, B:76:0x0234, B:78:0x0238, B:80:0x023c, B:81:0x0259, B:83:0x0262, B:84:0x0265, B:86:0x026e, B:87:0x0271, B:89:0x0276, B:92:0x027b), top: B:97:0x0003, inners: #1, #2, #3, #4, #5, #6, #7, #8 }] */
    /* JADX WARN: Removed duplicated region for block: B:86:0x026e A[Catch: all -> 0x0280, TRY_LEAVE, TryCatch #0 {, blocks: (B:4:0x0003, B:6:0x000f, B:8:0x001b, B:10:0x0027, B:13:0x0037, B:15:0x0046, B:16:0x004e, B:18:0x0056, B:19:0x0060, B:21:0x0068, B:22:0x0070, B:24:0x0079, B:26:0x007e, B:28:0x0083, B:30:0x0088, B:32:0x008c, B:34:0x00bc, B:35:0x00cb, B:37:0x00d4, B:39:0x0122, B:40:0x0131, B:42:0x0137, B:43:0x0146, B:45:0x014c, B:46:0x015b, B:48:0x0161, B:49:0x0170, B:52:0x0197, B:55:0x01a1, B:56:0x01a4, B:58:0x01e1, B:60:0x01eb, B:63:0x01f5, B:65:0x01f9, B:67:0x01fd, B:68:0x021a, B:72:0x0226, B:73:0x022a, B:76:0x0234, B:78:0x0238, B:80:0x023c, B:81:0x0259, B:83:0x0262, B:84:0x0265, B:86:0x026e, B:87:0x0271, B:89:0x0276, B:92:0x027b), top: B:97:0x0003, inners: #1, #2, #3, #4, #5, #6, #7, #8 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static synchronized void printLogo581CMD(android.graphics.Bitmap r15) {
        /*
            Method dump skipped, instructions count: 643
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.telpo.tps550.api.printer.ThermalPrinter.printLogo581CMD(android.graphics.Bitmap):void");
    }

    private static void threadSleep(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static synchronized byte[] parseHexStr2Byte(String str) {
        synchronized (ThermalPrinter.class) {
            if (str.length() < 1) {
                return null;
            }
            byte[] bArr = new byte[str.length() / 2];
            for (int i = 0; i < str.length() / 2; i++) {
                int i2 = i * 2;
                int i3 = i2 + 1;
                bArr[i] = (byte) ((Integer.parseInt(str.substring(i2, i3), 16) * 16) + Integer.parseInt(str.substring(i3, i2 + 2), 16));
            }
            return bArr;
        }
    }

    private static synchronized Bitmap zoomImg(Bitmap bitmap, int i, int i2) {
        Bitmap createBitmap;
        synchronized (ThermalPrinter.class) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            Matrix matrix = new Matrix();
            matrix.postScale(i / width, i2 / height);
            createBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
            Log.d(TAG, "zoomImg width:" + width + ";height:" + height);
        }
        return createBitmap;
    }

    private static synchronized byte[] changeBitmap(Bitmap bitmap, BitmapDecodeCallback bitmapDecodeCallback) throws TelpoException {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        synchronized (ThermalPrinter.class) {
            try {
                if (bitmap == null) {
                    throw new NullPointerException();
                }
                char c = 65535;
                int i6 = 8;
                if (isSY581Printer()) {
                    c = '\b';
                }
                int i7 = 30;
                int i8 = RGB565_MASK_RED;
                int i9 = ARGB_MASK_RED;
                int i10 = 5;
                int i11 = 15;
                if (c != 5 && c != 3 && c != 4 && c != '\b') {
                    if (bitmap.getWidth() > 384 || bitmap.getHeight() < 1) {
                        throw new IllegalArgumentException("The width or the height of the image to print is illegal!");
                    }
                    if (bitmap.getHeight() % 8 != 0) {
                        i5 = ((bitmap.getHeight() / 8) + 1) * 8;
                    } else {
                        i5 = bitmap.getHeight();
                    }
                    byte[] bArr = new byte[(bitmap.getWidth() * i5) / 8];
                    if (bitmap.getConfig().equals(Bitmap.Config.ARGB_8888)) {
                        int i12 = 0;
                        for (int i13 = 0; i13 < bitmap.getHeight() / 8; i13++) {
                            for (int i14 = 0; i14 < bitmap.getWidth(); i14++) {
                                int i15 = i13 * 8;
                                int i16 = 0;
                                for (int i17 = i15; i17 < i15 + 8; i17++) {
                                    int pixel = bitmap.getPixel(i14, i17);
                                    i16 = (((pixel & ARGB_MASK_RED) >> 16) <= 128 || ((pixel & 65280) >> 8) <= 128 || (pixel & 255) <= 128) ? (i16 << 1) + 1 : i16 << 1;
                                }
                                bArr[i12] = (byte) i16;
                                i12++;
                            }
                        }
                    } else if (bitmap.getConfig().equals(Bitmap.Config.ALPHA_8)) {
                        int i18 = 0;
                        for (int i19 = 0; i19 < bitmap.getHeight() / 8; i19++) {
                            for (int i20 = 0; i20 < bitmap.getWidth(); i20++) {
                                int i21 = i19 * 8;
                                int i22 = 0;
                                for (int i23 = i21; i23 < i21 + 8; i23++) {
                                    i22 = (bitmap.getPixel(i20, i23) & 255) > 128 ? (i22 * 2) << 1 : (i22 << 1) + 1;
                                }
                                bArr[i18] = (byte) i22;
                                i18++;
                            }
                        }
                    } else if (bitmap.getConfig().equals(Bitmap.Config.RGB_565)) {
                        int i24 = 0;
                        for (int i25 = 0; i25 < bitmap.getHeight() / 8; i25++) {
                            for (int i26 = 0; i26 < bitmap.getWidth(); i26++) {
                                int i27 = i25 * 8;
                                int i28 = i27;
                                int i29 = 0;
                                while (i28 < i27 + 8) {
                                    int pixel2 = bitmap.getPixel(i26, i28);
                                    i29 = (((pixel2 & i8) >> 11) <= 15 || ((pixel2 & RGB565_MASK_GREEN) >> 5) <= 30 || (pixel2 & 31) <= 15) ? (i29 << 1) + 1 : i29 << 1;
                                    i28++;
                                    i8 = RGB565_MASK_RED;
                                }
                                bArr[i24] = (byte) i29;
                                i24++;
                            }
                        }
                    }
                    return bArr;
                }
                Log.d(TAG, "changeBitmap width:" + bitmap.getWidth() + ";height:" + bitmap.getHeight());
                if (bitmap.getWidth() > 576 || bitmap.getHeight() < 1) {
                    throw new IllegalArgumentException("The width or the height of the image to print is illegal!");
                }
                int width = bitmap.getWidth() % 8;
                if (width != 0) {
                    i = (bitmap.getWidth() - width) + 8;
                } else {
                    i = bitmap.getWidth();
                }
                byte[] bArr2 = new byte[(i / 8) * bitmap.getHeight()];
                if (bitmap.getConfig().equals(Bitmap.Config.ARGB_8888)) {
                    int width2 = bitmap.getWidth() / 8;
                    int i30 = 0;
                    int i31 = 0;
                    while (i30 < bitmap.getHeight()) {
                        byte[] bArr3 = new byte[width2];
                        int i32 = 0;
                        int i33 = 0;
                        while (i32 < width2) {
                            int i34 = 0;
                            int i35 = 0;
                            while (i34 < i6) {
                                int pixel3 = bitmap.getPixel(i34 + i33, i30);
                                int i36 = width2;
                                i35 = (((pixel3 & ARGB_MASK_RED) >> 16) <= 128 || ((pixel3 & 65280) >> 8) <= 128 || (pixel3 & 255) <= 128) ? (i35 << 1) + 1 : i35 << 1;
                                i34++;
                                width2 = i36;
                                i6 = 8;
                            }
                            bArr2[i31] = (byte) i35;
                            bArr3[i32] = bArr2[i31];
                            i31++;
                            i32++;
                            i33 += 8;
                            i9 = ARGB_MASK_RED;
                        }
                        if (bitmapDecodeCallback != null && width == 0) {
                            bitmapDecodeCallback.decodingByte(bArr3);
                        }
                        if (width != 0) {
                            int i37 = 0;
                            int i38 = 0;
                            while (i37 < width) {
                                int pixel4 = bitmap.getPixel(i37 + i33, i30);
                                i38 = (((pixel4 & i9) >> 16) <= 128 || ((pixel4 & 65280) >> i6) <= 128 || (pixel4 & 255) <= 128) ? (i38 << 1) + 1 : i38 << 1;
                                i37++;
                                i9 = ARGB_MASK_RED;
                            }
                            bArr2[i31] = (byte) (i38 << (8 - width));
                            i31++;
                        }
                        i30++;
                        i9 = ARGB_MASK_RED;
                    }
                    if (bitmapDecodeCallback != null) {
                        bitmapDecodeCallback.decodeComplete();
                    }
                    return bArr2;
                } else if (bitmap.getConfig().equals(Bitmap.Config.ALPHA_8)) {
                    int width3 = bitmap.getWidth() / 8;
                    int i39 = 0;
                    for (int i40 = 0; i40 < bitmap.getHeight(); i40++) {
                        byte[] bArr4 = new byte[width3];
                        int i41 = 0;
                        int i42 = 0;
                        while (i41 < width3) {
                            int i43 = 0;
                            for (int i44 = 0; i44 < 8; i44++) {
                                i43 = (bitmap.getPixel(i44 + i42, i40) & 255) > 128 ? i43 << 1 : (i43 << 1) + 1;
                            }
                            bArr2[i39] = (byte) i43;
                            bArr4[i41] = bArr2[i39];
                            i39++;
                            i41++;
                            i42 += 8;
                        }
                        if (bitmapDecodeCallback != null && width == 0) {
                            bitmapDecodeCallback.decodingByte(bArr4);
                        }
                        if (width != 0) {
                            int i45 = 0;
                            for (int i46 = 0; i46 < width; i46++) {
                                i45 = (bitmap.getPixel(i46 + i42, i40) & 255) > 128 ? i45 << 1 : (i45 << 1) + 1;
                            }
                            bArr2[i39] = (byte) (i45 << (8 - width));
                            i39++;
                        }
                    }
                    if (bitmapDecodeCallback != null) {
                        bitmapDecodeCallback.decodeComplete();
                    }
                    return bArr2;
                } else if (!bitmap.getConfig().equals(Bitmap.Config.RGB_565)) {
                    return bArr2;
                } else {
                    int width4 = bitmap.getWidth() / 8;
                    int i47 = 0;
                    for (int i48 = 0; i48 < bitmap.getHeight(); i48++) {
                        byte[] bArr5 = new byte[width4];
                        int i49 = 0;
                        int i50 = 0;
                        while (i49 < width4) {
                            int i51 = 0;
                            int i52 = 0;
                            for (int i53 = 8; i51 < i53; i53 = 8) {
                                int pixel5 = bitmap.getPixel(i51 + i50, i48);
                                if (((pixel5 & RGB565_MASK_RED) >> 11) > i11) {
                                    i2 = 5;
                                    if (((pixel5 & RGB565_MASK_GREEN) >> 5) > 30) {
                                        i3 = 15;
                                        if ((pixel5 & 31) > 15) {
                                            i4 = i52 << 1;
                                            i51++;
                                            i52 = i4;
                                            i11 = i3;
                                            i10 = i2;
                                        }
                                    } else {
                                        i3 = 15;
                                    }
                                } else {
                                    i3 = i11;
                                    i2 = 5;
                                }
                                i4 = (i52 << 1) + 1;
                                i51++;
                                i52 = i4;
                                i11 = i3;
                                i10 = i2;
                            }
                            bArr2[i47] = (byte) i52;
                            bArr5[i49] = bArr2[i47];
                            i47++;
                            i49++;
                            i50 += 8;
                            i7 = 30;
                        }
                        if (bitmapDecodeCallback != null && width == 0) {
                            bitmapDecodeCallback.decodingByte(bArr5);
                        }
                        if (width != 0) {
                            int i54 = 0;
                            for (int i55 = 0; i55 < width; i55++) {
                                int pixel6 = bitmap.getPixel(i55 + i50, i48);
                                i54 = (((pixel6 & RGB565_MASK_RED) >> 11) <= i11 || ((pixel6 & RGB565_MASK_GREEN) >> i10) <= i7 || (pixel6 & 31) <= i11) ? (i54 << 1) + 1 : i54 << 1;
                            }
                            bArr2[i47] = (byte) (i54 << (8 - width));
                            i47++;
                        }
                    }
                    if (bitmapDecodeCallback != null) {
                        bitmapDecodeCallback.decodeComplete();
                    }
                    return bArr2;
                }
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    private static synchronized byte[] changeBitmap(Bitmap bitmap) throws TelpoException {
        byte[] changeBitmap;
        synchronized (ThermalPrinter.class) {
            changeBitmap = changeBitmap(bitmap, null);
        }
        return changeBitmap;
    }

    /* JADX WARN: Removed duplicated region for block: B:29:0x003b A[DONT_GENERATE] */
    /* JADX WARN: Removed duplicated region for block: B:31:0x003d A[Catch: all -> 0x005a, TRY_ENTER, TryCatch #0 {, blocks: (B:4:0x0003, B:6:0x0007, B:15:0x0019, B:18:0x001e, B:19:0x0025, B:22:0x002a, B:24:0x002e, B:25:0x0032, B:27:0x0035, B:31:0x003d, B:32:0x0041, B:33:0x0042, B:34:0x004a, B:35:0x0051, B:36:0x0052, B:37:0x0059), top: B:40:0x0003 }] */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0042 A[Catch: all -> 0x005a, LOOP:0: B:25:0x0032->B:33:0x0042, LOOP_END, TryCatch #0 {, blocks: (B:4:0x0003, B:6:0x0007, B:15:0x0019, B:18:0x001e, B:19:0x0025, B:22:0x002a, B:24:0x002e, B:25:0x0032, B:27:0x0035, B:31:0x003d, B:32:0x0041, B:33:0x0042, B:34:0x004a, B:35:0x0051, B:36:0x0052, B:37:0x0059), top: B:40:0x0003 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static synchronized void printLogo(int r4, int r5, char[] r6) throws com.telpo.tps550.api.TelpoException {
        /*
            java.lang.Class<com.telpo.tps550.api.printer.ThermalPrinter> r0 = com.telpo.tps550.api.printer.ThermalPrinter.class
            monitor-enter(r0)
            boolean r1 = com.telpo.tps550.api.printer.ThermalPrinter.openFlag     // Catch: java.lang.Throwable -> L5a
            if (r1 == 0) goto L52
            int r1 = get_printer_type()     // Catch: java.lang.Throwable -> L5a
            r2 = 3
            if (r1 == r2) goto L26
            r2 = 4
            if (r1 == r2) goto L26
            r2 = 5
            if (r1 != r2) goto L15
            goto L26
        L15:
            r1 = 384(0x180, float:5.38E-43)
            if (r4 > r1) goto L1e
            int r1 = r5 % 8
            if (r1 != 0) goto L1e
            goto L2e
        L1e:
            java.lang.IllegalArgumentException r4 = new java.lang.IllegalArgumentException     // Catch: java.lang.Throwable -> L5a
            java.lang.String r5 = "The width or the height of the image to print is illegal!"
            r4.<init>(r5)     // Catch: java.lang.Throwable -> L5a
            throw r4     // Catch: java.lang.Throwable -> L5a
        L26:
            r1 = 576(0x240, float:8.07E-43)
            if (r4 > r1) goto L4a
            int r1 = r4 % 8
            if (r1 != 0) goto L4a
        L2e:
            int r1 = r6.length     // Catch: java.lang.Throwable -> L5a
            byte[] r1 = new byte[r1]     // Catch: java.lang.Throwable -> L5a
            r2 = 0
        L32:
            int r3 = r6.length     // Catch: java.lang.Throwable -> L5a
            if (r2 < r3) goto L42
            int r4 = print_logo(r4, r5, r1)     // Catch: java.lang.Throwable -> L5a
            if (r4 != 0) goto L3d
            monitor-exit(r0)
            return
        L3d:
            com.telpo.tps550.api.TelpoException r4 = getException(r4)     // Catch: java.lang.Throwable -> L5a
            throw r4     // Catch: java.lang.Throwable -> L5a
        L42:
            char r3 = r6[r2]     // Catch: java.lang.Throwable -> L5a
            byte r3 = (byte) r3     // Catch: java.lang.Throwable -> L5a
            r1[r2] = r3     // Catch: java.lang.Throwable -> L5a
            int r2 = r2 + 1
            goto L32
        L4a:
            java.lang.IllegalArgumentException r4 = new java.lang.IllegalArgumentException     // Catch: java.lang.Throwable -> L5a
            java.lang.String r5 = "The width of the image to print is illegal!"
            r4.<init>(r5)     // Catch: java.lang.Throwable -> L5a
            throw r4     // Catch: java.lang.Throwable -> L5a
        L52:
            com.telpo.tps550.api.DeviceNotOpenException r4 = new com.telpo.tps550.api.DeviceNotOpenException     // Catch: java.lang.Throwable -> L5a
            java.lang.String r5 = "The printer has not been init!"
            r4.<init>(r5)     // Catch: java.lang.Throwable -> L5a
            throw r4     // Catch: java.lang.Throwable -> L5a
        L5a:
            r4 = move-exception
            monitor-exit(r0)
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.telpo.tps550.api.printer.ThermalPrinter.printLogo(int, int, char[]):void");
    }

    /* JADX WARN: Removed duplicated region for block: B:222:0x03fc  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static synchronized void printLogo(android.graphics.Bitmap r24) throws com.telpo.tps550.api.TelpoException {
        /*
            Method dump skipped, instructions count: 1162
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.telpo.tps550.api.printer.ThermalPrinter.printLogo(android.graphics.Bitmap):void");
    }

    public static synchronized String getVersion() throws TelpoException {
        byte[] bArr;
        UnsupportedEncodingException e;
        synchronized (ThermalPrinter.class) {
            UsbThermalPrinter usbThermalPrinter = mUsbThermalPrinter;
            if (usbThermalPrinter != null) {
                return usbThermalPrinter.getVersion();
            } else if (!openFlag) {
                throw new DeviceNotOpenException();
            } else {
                String str = null;
                if (isSY581Printer()) {
                    bArr = new byte[16];
                    for (int i = 0; i < 16; i++) {
                        bArr[i] = -1;
                    }
                } else {
                    bArr = new byte[128];
                }
                if (SystemUtil.getDeviceType() == StringUtil.DeviceModelEnum.C1B.ordinal()) {
                    bArr = CheckC1Bprinter.checkVersion();
                } else {
                    int i2 = get_version(bArr);
                    if (i2 != 0) {
                        Log.d("tagg", "printer get_version success");
                        throw getException(i2);
                    }
                    Log.d("tagg", "printer get_version fail");
                }
                try {
                } catch (UnsupportedEncodingException e2) {
                    e = e2;
                }
                if (isSY581Printer()) {
                    String hexString = StringUtil.toHexString(bArr);
                    Log.d("tagg", "581 version tohex:" + hexString);
                    if ("14".equals(hexString.substring(2, 4))) {
                        return getVersionNum(hexString);
                    } else if (bArr != null && bArr.length == 2 && bArr[0] == 10) {
                        return new StringBuilder().append(Integer.valueOf(StringUtil.toHexString(new byte[]{bArr[1]}), 16)).toString();
                    } else {
                        int indexOf = hexString.indexOf("FF");
                        String str2 = "";
                        String str3 = "";
                        if (indexOf == 4) {
                            String sb = new StringBuilder().append(Integer.valueOf(hexString.substring(0, 2), 16)).toString();
                            String sb2 = new StringBuilder().append(Integer.valueOf(hexString.substring(2, 4), 16)).toString();
                            str2 = String.valueOf(sb.substring(0, 1)) + "." + sb.substring(1, 2);
                            str3 = String.valueOf(sb2.substring(0, 1)) + "." + sb2.substring(1, 3);
                        } else if (indexOf == 6) {
                            String sb3 = new StringBuilder().append(Integer.valueOf(hexString.substring(2, 4), 16)).toString();
                            String sb4 = new StringBuilder().append(Integer.valueOf(hexString.substring(4, 6), 16)).toString();
                            str2 = String.valueOf(sb3.substring(0, 1)) + "." + sb3.substring(1, 2);
                            str3 = String.valueOf(sb4.substring(0, 1)) + "." + sb4.substring(1, 3);
                        }
                        Log.d("tagg", "581 halVer:" + str2 + " softVer:" + str3);
                        return String.valueOf(str2) + "+" + str3;
                    }
                }
                String str4 = new String(bArr, 1, bArr[0], "UTF-8");
                try {
                    Log.d("tagg", "version[" + str4 + "]");
                } catch (UnsupportedEncodingException e3) {
                    e = e3;
                    str = str4;
                    e.printStackTrace();
                    str4 = str;
                    return str4;
                }
                return str4;
            }
        }
    }

    public static synchronized void printLogo(Bitmap bitmap, int i) throws TelpoException {
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        synchronized (ThermalPrinter.class) {
            if (!openFlag) {
                throw new DeviceNotOpenException("The printer has not been init!");
            }
            if (bitmap == null) {
                throw new NullPointerException();
            }
            int printerType = SystemUtil.getPrinterType();
            if (Build.MODEL.equals("MTDP-618A") || Build.MODEL.equals("TPS650M") || (getFileContent(FILE_NAME) != null && getFileContent(FILE_NAME).equals("5880"))) {
                printerType = 2;
            }
            byte b = 29;
            byte b2 = 27;
            int i12 = 5;
            int i13 = 0;
            if (printerType == 7) {
                int height = bitmap.getHeight();
                int width = bitmap.getWidth();
                int i14 = (width + 7) >> 3;
                int i15 = (height * i14) + 8 + 3;
                byte[] bArr = new byte[i15];
                bArr[0] = 27;
                bArr[1] = 97;
                if (i == 1) {
                    bArr[2] = 1;
                } else if (i == 2) {
                    bArr[2] = 2;
                } else {
                    bArr[2] = 0;
                }
                bArr[3] = 29;
                bArr[4] = 118;
                bArr[5] = 48;
                bArr[6] = 0;
                bArr[7] = (byte) i14;
                bArr[8] = (byte) (i14 >> 8);
                bArr[9] = (byte) height;
                bArr[10] = (byte) (height >> 8);
                int i16 = 11;
                for (int i17 = 0; i17 < height; i17++) {
                    int i18 = i13;
                    int i19 = i18;
                    while (i18 < i14) {
                        int i20 = i13;
                        int i21 = i20;
                        int i22 = 8;
                        while (true) {
                            if (i20 < i22) {
                                int i23 = i20 + i19;
                                if (i23 >= width) {
                                    i21 <<= 8 - i20;
                                    break;
                                }
                                int pixel = bitmap.getPixel(i23, i17);
                                i21 = (((pixel & ARGB_MASK_RED) >> 16) <= 128 || ((pixel & 65280) >> 8) <= 128 || (pixel & 255) <= 128) ? (i21 << 1) + 1 : i21 << 1;
                                i20++;
                                i13 = 0;
                                i22 = 8;
                            }
                        }
                        bArr[i16] = (byte) i21;
                        i18++;
                        i19 += 8;
                        i16++;
                    }
                }
                i2 = print_logo(i15, i13, bArr);
            } else if (printerType == 6) {
                Bitmap adjustBitmap = adjustBitmap(bitmap, i);
                int height2 = adjustBitmap.getHeight();
                int width2 = adjustBitmap.getWidth();
                int i24 = (height2 + 7) >> 3;
                byte[] bArr2 = {27, 42, 1, (byte) (width2 & 255), (byte) ((width2 >> 8) & 255)};
                int i25 = 6;
                int i26 = (width2 * i24) + i24 + (5 * i24) + 6 + 3 + 2;
                byte[] bArr3 = new byte[i26];
                bArr3[0] = 29;
                bArr3[1] = 69;
                bArr3[2] = 14;
                bArr3[3] = 27;
                bArr3[4] = 51;
                bArr3[5] = 0;
                int i27 = 0;
                int i28 = 0;
                while (i27 < i24) {
                    int i29 = 0;
                    System.arraycopy(bArr2, 0, bArr3, i25, i12);
                    int i30 = i25 + 5;
                    int i31 = 0;
                    while (i31 < width2) {
                        int i32 = i29;
                        int i33 = i32;
                        while (i32 < 8 && (i9 = i32 + i28) < height2) {
                            int pixel2 = adjustBitmap.getPixel(i31, i9);
                            Bitmap bitmap2 = adjustBitmap;
                            if (((pixel2 & ARGB_MASK_RED) >> 16) > 128) {
                                i10 = height2;
                                if (((pixel2 & 65280) >> 8) > 128 && (pixel2 & 255) > 128) {
                                    i11 = i33 << 1;
                                    i33 = i11;
                                    i32++;
                                    adjustBitmap = bitmap2;
                                    height2 = i10;
                                }
                            } else {
                                i10 = height2;
                            }
                            i11 = (i33 << 1) + 1;
                            i33 = i11;
                            i32++;
                            adjustBitmap = bitmap2;
                            height2 = i10;
                        }
                        bArr3[i30] = (byte) i33;
                        i31++;
                        b = 29;
                        b2 = 27;
                        i12 = 5;
                        i29 = 0;
                        height2 = height2;
                        i30++;
                        adjustBitmap = adjustBitmap;
                    }
                    i25 = i30 + 1;
                    bArr3[i30] = 10;
                    i27++;
                    i28 += 8;
                }
                int i34 = i25 + 1;
                bArr3[i25] = b;
                int i35 = i34 + 1;
                bArr3[i34] = 69;
                int i36 = i35 + 1;
                bArr3[i35] = 1;
                bArr3[i36] = b2;
                bArr3[i36 + 1] = 64;
                i2 = print_logo(i26, 0, bArr3);
            } else {
                int i37 = RGB565_MASK_RED;
                int i38 = 15;
                if (printerType != 5 && printerType != 3 && printerType != 4) {
                    if (bitmap.getHeight() % 8 != 0) {
                        i7 = ((bitmap.getHeight() / 8) + 1) * 8;
                    } else {
                        i7 = bitmap.getHeight();
                    }
                    int i39 = 384;
                    if (bitmap.getWidth() > 384) {
                        throw new IllegalArgumentException("The width or the height of the image to print is illegal!");
                    }
                    if (i == 0) {
                        i39 = bitmap.getWidth();
                        i8 = 0;
                    } else if (i == 1) {
                        int width3 = ((384 - bitmap.getWidth()) / 2) + bitmap.getWidth();
                        i8 = (384 - bitmap.getWidth()) / 2;
                        i39 = width3;
                    } else if (i == 2) {
                        i8 = 384 - bitmap.getWidth();
                    } else {
                        throw new IllegalArgumentException("The mode algin of the image to print is illegal!");
                    }
                    byte[] bArr4 = new byte[(i39 * i7) / 8];
                    Log.e(TAG, ":" + i8 + ":" + i39 + ":" + i7);
                    if (bitmap.getConfig().equals(Bitmap.Config.ARGB_8888)) {
                        int i40 = i8;
                        for (int i41 = 0; i41 < i7 / 8; i41++) {
                            for (int i42 = i8; i42 < bitmap.getWidth() + i8; i42++) {
                                int i43 = i41 * 8;
                                int i44 = 0;
                                for (int i45 = i43; i45 < Math.min(i43 + 8, bitmap.getHeight()); i45++) {
                                    int pixel3 = bitmap.getPixel(i42 - i8, i45);
                                    i44 = (((pixel3 & ARGB_MASK_RED) >> 16) <= 128 || ((pixel3 & 65280) >> 8) <= 128 || (pixel3 & 255) <= 128) ? (i44 << 1) + 1 : i44 << 1;
                                }
                                bArr4[i40] = (byte) i44;
                                i40++;
                            }
                            i40 += i8;
                        }
                        Log.i(TAG, "dealing ARGB_8888 image");
                    } else if (bitmap.getConfig().equals(Bitmap.Config.ALPHA_8)) {
                        int i46 = i8;
                        for (int i47 = 0; i47 < i7 / 8; i47++) {
                            for (int i48 = i8; i48 < bitmap.getWidth() + i8; i48++) {
                                int i49 = i47 * 8;
                                int i50 = 0;
                                for (int i51 = i49; i51 < Math.min(i49 + 8, bitmap.getHeight()); i51++) {
                                    i50 = (bitmap.getPixel(i48 - i8, i51) & 255) > 128 ? (i50 * 2) << 1 : (i50 << 1) + 1;
                                }
                                bArr4[i46] = (byte) i50;
                                i46++;
                            }
                            i46 += i8;
                        }
                        Log.i(TAG, "dealing ALPHA_8 image");
                    } else if (bitmap.getConfig().equals(Bitmap.Config.RGB_565)) {
                        int i52 = i8;
                        for (int i53 = 0; i53 < i7 / 8; i53++) {
                            int i54 = i53 * 8;
                            int i55 = i54;
                            while (true) {
                                int i56 = i54 + 8;
                                if (i55 >= Math.min(i56, bitmap.getHeight())) {
                                    break;
                                }
                                int i57 = i54;
                                int i58 = 0;
                                while (i57 < i56) {
                                    int pixel4 = bitmap.getPixel(0 - i8, i57);
                                    i58 = (((pixel4 & i37) >> 11) <= 15 || ((pixel4 & RGB565_MASK_GREEN) >> 5) <= 30 || (pixel4 & 31) <= 15) ? (i58 << 1) + 1 : i58 << 1;
                                    i57++;
                                    i37 = RGB565_MASK_RED;
                                }
                                bArr4[i52] = (byte) i58;
                                i52++;
                                i55 = i57 + 1;
                            }
                            i52 += i8;
                        }
                        Log.i(TAG, "dealing RGB_565 image");
                    } else {
                        Log.e(TAG, "unsupport image formate!");
                    }
                    i2 = print_logo(i39, i7, bArr4);
                }
                int i59 = 576;
                if (bitmap.getWidth() > 576 || bitmap.getHeight() < 1) {
                    throw new IllegalArgumentException("The width or the height of the image to print is illegal!");
                }
                int width4 = bitmap.getWidth() % 8;
                if (width4 != 0) {
                    i3 = (bitmap.getWidth() - width4) + 8;
                } else {
                    i3 = bitmap.getWidth();
                }
                if (i == 0) {
                    i59 = i3;
                    i4 = 0;
                } else if (i == 1) {
                    int i60 = (576 - i3) / 2;
                    int i61 = i60 % 8;
                    if (i61 != 0) {
                        i60 = (i60 - i61) + 8;
                    }
                    i4 = i60;
                    i59 = i3 + i4;
                } else if (i != 2) {
                    throw new IllegalArgumentException("The mode algin of the image to print is illegal!");
                } else {
                    i4 = 576 - i3;
                }
                Log.i(TAG, "printWidth: " + i59);
                byte[] bArr5 = new byte[(i59 / 8) * bitmap.getHeight()];
                int i62 = i4 / 8;
                if (bitmap.getConfig().equals(Bitmap.Config.ARGB_8888)) {
                    int width5 = bitmap.getWidth() / 8;
                    int i63 = i62;
                    for (int i64 = 0; i64 < bitmap.getHeight(); i64++) {
                        int i65 = 0;
                        int i66 = 0;
                        while (i65 < width5) {
                            int i67 = 0;
                            for (int i68 = 0; i68 < 8; i68++) {
                                int pixel5 = bitmap.getPixel(i68 + i66, i64);
                                i67 = (((pixel5 & ARGB_MASK_RED) >> 16) <= 128 || ((pixel5 & 65280) >> 8) <= 128 || (pixel5 & 255) <= 128) ? (i67 << 1) + 1 : i67 << 1;
                            }
                            bArr5[i63] = (byte) i67;
                            i63++;
                            i65++;
                            i66 += 8;
                        }
                        if (width4 != 0) {
                            int i69 = 0;
                            for (int i70 = 0; i70 < width4; i70++) {
                                int pixel6 = bitmap.getPixel(i70 + i66, i64);
                                i69 = (((pixel6 & ARGB_MASK_RED) >> 16) <= 128 || ((pixel6 & 65280) >> 8) <= 128 || (pixel6 & 255) <= 128) ? (i69 << 1) + 1 : i69 << 1;
                            }
                            bArr5[i63] = (byte) (i69 << (8 - width4));
                            i63++;
                        }
                        i63 += i62;
                    }
                    Log.i(TAG, "dealing ARGB_8888 image");
                } else if (bitmap.getConfig().equals(Bitmap.Config.ALPHA_8)) {
                    int width6 = bitmap.getWidth() / 8;
                    int i71 = i62;
                    for (int i72 = 0; i72 < bitmap.getHeight(); i72++) {
                        int i73 = 0;
                        int i74 = 0;
                        while (i73 < width6) {
                            int i75 = 0;
                            for (int i76 = 0; i76 < 8; i76++) {
                                i75 = (bitmap.getPixel(i76 + i74, i72) & 255) > 128 ? i75 << 1 : (i75 << 1) + 1;
                            }
                            bArr5[i71] = (byte) i75;
                            i71++;
                            i73++;
                            i74 += 8;
                        }
                        if (width4 != 0) {
                            int i77 = 0;
                            for (int i78 = 0; i78 < width4; i78++) {
                                i77 = (bitmap.getPixel(i78 + i74, i72) & 255) > 128 ? i77 << 1 : (i77 << 1) + 1;
                            }
                            bArr5[i71] = (byte) (i77 << (8 - width4));
                            i71++;
                        }
                        i71 += i62;
                    }
                    Log.i(TAG, "dealing ALPHA_8 image");
                } else if (bitmap.getConfig().equals(Bitmap.Config.RGB_565)) {
                    int width7 = bitmap.getWidth() / 8;
                    int i79 = i62;
                    for (int i80 = 0; i80 < bitmap.getHeight(); i80++) {
                        int i81 = 0;
                        int i82 = 0;
                        while (i81 < width7) {
                            int i83 = 0;
                            int i84 = 0;
                            while (i83 < 8) {
                                int pixel7 = bitmap.getPixel(i83 + i82, i80);
                                if (((pixel7 & RGB565_MASK_RED) >> 11) <= i38) {
                                    i5 = i38;
                                } else if (((pixel7 & RGB565_MASK_GREEN) >> 5) > 30) {
                                    i5 = 15;
                                    if ((pixel7 & 31) > 15) {
                                        i6 = i84 << 1;
                                        i83++;
                                        i84 = i6;
                                        i38 = i5;
                                    }
                                } else {
                                    i5 = 15;
                                }
                                i6 = (i84 << 1) + 1;
                                i83++;
                                i84 = i6;
                                i38 = i5;
                            }
                            bArr5[i79] = (byte) i84;
                            i79++;
                            i81++;
                            i82 += 8;
                        }
                        if (width4 != 0) {
                            int i85 = 0;
                            for (int i86 = 0; i86 < width4; i86++) {
                                int pixel8 = bitmap.getPixel(i86 + i82, i80);
                                i85 = (((pixel8 & RGB565_MASK_RED) >> 11) <= i38 || ((pixel8 & RGB565_MASK_GREEN) >> 5) <= 30 || (pixel8 & 31) <= i38) ? (i85 << 1) + 1 : i85 << 1;
                            }
                            bArr5[i79] = (byte) (i85 << (8 - width4));
                            i79++;
                        }
                        i79 += i62;
                    }
                    Log.i(TAG, "dealing RGB_565 image");
                } else {
                    Log.e(TAG, "unsupport image formate!");
                }
                i2 = print_logo(i59, bitmap.getHeight(), bArr5);
            }
            if (i2 != 0) {
                throw getException(i2);
            }
        }
    }

    private static Bitmap CreateCode(String str, BarcodeFormat barcodeFormat, int i, int i2) throws InternalErrorException {
        try {
            BitMatrix encode = new MultiFormatWriter().encode(str, barcodeFormat, i, i2);
            int width = encode.getWidth();
            int height = encode.getHeight();
            int[] iArr = new int[width * height];
            for (int i3 = 0; i3 < height; i3++) {
                for (int i4 = 0; i4 < width; i4++) {
                    if (encode.get(i4, i3)) {
                        iArr[(i3 * width) + i4] = -16777216;
                    } else {
                        iArr[(i3 * width) + i4] = -1;
                    }
                }
            }
            Bitmap createBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            createBitmap.setPixels(iArr, 0, width, 0, 0, width, height);
            return createBitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            throw new InternalErrorException("Failed to encode bitmap");
        }
    }

    public static void searchMark(int i, int i2) throws TelpoException {
        int search_mark;
        UsbThermalPrinter usbThermalPrinter = mUsbThermalPrinter;
        if (usbThermalPrinter != null) {
            usbThermalPrinter.searchMark(i, i2);
        } else if (!openFlag) {
            throw new DeviceNotOpenException();
        } else {
            if (!isSY581Printer() && (search_mark = search_mark(0, i, i2)) != 0) {
                throw getException(search_mark);
            }
        }
    }

    public static void paperCut() throws TelpoException {
        UsbThermalPrinter usbThermalPrinter = mUsbThermalPrinter;
        if (usbThermalPrinter != null) {
            usbThermalPrinter.paperCut();
        } else if (!openFlag) {
            throw new DeviceNotOpenException();
        } else {
            if (isSY581Printer()) {
                ThermalPrinterSY581.cutPaper();
                return;
            }
            int paper_cut = paper_cut();
            if (paper_cut != 0) {
                throw getException(paper_cut);
            }
        }
    }

    public static void paperCutAll() throws TelpoException {
        if (!openFlag) {
            throw new DeviceNotOpenException();
        }
        if (isSY581Printer()) {
            ThermalPrinterSY581.cutPaper();
            return;
        }
        int paper_cut_all = paper_cut_all();
        if (paper_cut_all != 0) {
            throw getException(paper_cut_all);
        }
    }

    public static void sendCommand(String str) throws TelpoException {
        if (openFlag) {
            if (str == null) {
                throw new IllegalArgumentException();
            }
            byte[] str2BCD = str2BCD(str.replace(" ", ""));
            int send_command = send_command(str2BCD, str2BCD.length);
            if (send_command != 0) {
                throw getException(send_command);
            }
            return;
        }
        throw new DeviceNotOpenException();
    }

    public static void sendCommand(byte[] bArr, int i) throws TelpoException {
        if (openFlag) {
            if (bArr == null) {
                throw new IllegalArgumentException();
            }
            int send_command = send_command(bArr, i);
            if (send_command != 0) {
                throw getException(send_command);
            }
            return;
        }
        throw new DeviceNotOpenException();
    }

    public static void setBold(boolean z) throws TelpoException {
        if (!openFlag) {
            throw new DeviceNotOpenException();
        }
        if (isSY581Printer()) {
            return;
        }
        if (z) {
            set_bold(1);
        } else {
            set_bold(0);
        }
    }

    private static byte[] str2BCD(String str) {
        int length = str.length();
        if (length % 2 == 1) {
            str = String.valueOf(str) + "0";
            length++;
        }
        int i = length >> 1;
        byte[] bArr = new byte[i];
        int i2 = 0;
        int i3 = 0;
        while (i2 < i) {
            bArr[i2] = (byte) (((byte) ("0123456789ABCDEF".indexOf(str.charAt(i3)) << 4)) | ((byte) "0123456789ABCDEF".indexOf(str.charAt(i3 + 1))));
            i2++;
            i3 += 2;
        }
        return bArr;
    }

    private static int getGreyLevel(int i, float f) {
        int red = (int) (((float) (((Color.red(i) + Color.green(i)) + Color.blue(i)) / 3.0d)) * f);
        if (red > 255) {
            return 255;
        }
        return red;
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x0027, code lost:
        if (r12 != 0) goto L9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x0018, code lost:
        if (r12 != 0) goto L9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x001a, code lost:
        r1 = r1 + (8 - r12);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static android.graphics.Bitmap adjustBitmap(android.graphics.Bitmap r11, int r12) {
        /*
            r0 = 0
            if (r11 != 0) goto L4
            return r0
        L4:
            int r1 = r11.getWidth()
            int r2 = r11.getHeight()
            r3 = 0
            r4 = 1
            r5 = 2
            if (r12 != r4) goto L1e
            int r12 = 384 - r1
            int r3 = r12 / 2
            int r1 = r1 + r3
            int r12 = r1 % 8
            if (r12 == 0) goto L2a
        L1a:
            int r12 = 8 - r12
            int r1 = r1 + r12
            goto L2a
        L1e:
            if (r12 != r5) goto L25
            int r3 = 384 - r1
            r1 = 384(0x180, float:5.38E-43)
            goto L2a
        L25:
            int r12 = r1 % 8
            if (r12 == 0) goto L2a
            goto L1a
        L2a:
            android.graphics.Bitmap$Config r12 = r11.getConfig()
            android.graphics.Bitmap r12 = android.graphics.Bitmap.createBitmap(r1, r2, r12)
            android.graphics.Paint r9 = new android.graphics.Paint
            r9.<init>()
            r4 = -1
            r9.setColor(r4)
            android.graphics.Canvas r10 = new android.graphics.Canvas
            r10.<init>(r12)
            r5 = 0
            r6 = 0
            float r7 = (float) r1
            float r8 = (float) r2
            r4 = r10
            r4.drawRect(r5, r6, r7, r8, r9)
            float r1 = (float) r3
            r2 = 0
            r10.drawBitmap(r11, r1, r2, r0)
            return r12
        */
        throw new UnsupportedOperationException("Method not decompiled: com.telpo.tps550.api.printer.ThermalPrinter.adjustBitmap(android.graphics.Bitmap, int):android.graphics.Bitmap");
    }

    private static String getFileContent(String str) {
        String str2 = null;
        try {
            File file = new File(str);
            if (file.isFile() && file.exists()) {
                InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file));
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                while (true) {
                    String readLine = bufferedReader.readLine();
                    if (readLine == null) {
                        break;
                    }
                    str2 = readLine;
                }
                inputStreamReader.close();
                bufferedReader.close();
            } else {
                Log.e(TAG, "can not find file");
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str2;
    }

    static {
        if (Build.MODEL.equals("MTDP-618A") || Build.MODEL.equals("TPS650M")) {
            System.loadLibrary("telpo_printer_48");
        } else if (SystemUtil.getDeviceType() == StringUtil.DeviceModelEnum.TPS650.ordinal()) {
            if (getFileContent(FILE_NAME) != null && getFileContent(FILE_NAME).equals("SY581")) {
                System.loadLibrary("telpo_printer_581");
            } else if (getFileContent(FILE_NAME) != null && getFileContent(FILE_NAME).equals("5880")) {
                System.loadLibrary("telpo_printer5880");
            } else {
                System.loadLibrary("telpo_printer");
            }
        } else {
            System.loadLibrary("telpo_printer");
        }
    }
}
