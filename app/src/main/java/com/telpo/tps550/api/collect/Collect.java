package com.telpo.tps550.api.collect;

import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/* loaded from: classes.dex */
public class Collect {
    private static final String TAG = "Collect";
    public static final int TYPE_FINGERPRINT = 12;
    public static final int TYPE_ICC = 2;
    public static final int TYPE_IDCARD = 4;
    public static final int TYPE_IR = 11;
    public static final int TYPE_MONEYBOX = 7;
    public static final int TYPE_MSR = 0;
    public static final int TYPE_NFC = 5;
    public static final int TYPE_OCR = 9;
    public static final int TYPE_PINPAD = 6;
    public static final int TYPE_PRT = 1;
    public static final int TYPE_PSAM = 3;
    public static final int TYPE_QRCODE = 10;
    public static final int TYPE_SCANGUN = 8;

    private static native int clear_all();

    private static native int collect_info(int i, int i2, byte[] bArr);

    private static native int get_num(int i);

    private static native String get_path(int i);

    public static int clearAll() {
        return clear_all();
    }

    public static int collectInfo(int i, int i2, byte[] bArr) {
        return collect_info(i, i2, bArr);
    }

    public static int getNum(int i) {
        int i2 = get_num(i);
        if (i2 < 0) {
            return 0;
        }
        return i2;
    }

    public static FileInputStream getFileInputStream(int i) {
        String str = get_path(i);
        if (str == null) {
            Log.e(TAG, "get file path of type " + i + " failed");
            return null;
        }
        try {
            return new FileInputStream(new File(str));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    static {
        System.loadLibrary("collect");
    }
}
