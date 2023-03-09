package com.zkteco.android.IDReader;

/* loaded from: classes.dex */
public class WLTService {
    public static int imgWidth = 102;
    public static int imgHeight = 126;
    public static int imgLength = 38556;

    public static native int wlt2Bmp(byte[] bArr, byte[] bArr2);

    static {
        System.loadLibrary("wlt2bmp");
        System.loadLibrary("zkwltdecode");
    }
}
