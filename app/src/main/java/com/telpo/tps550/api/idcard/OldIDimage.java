package com.telpo.tps550.api.idcard;

/* loaded from: classes.dex */
public class OldIDimage {
    public static native int get_imageusb(byte[] bArr, String str);

    static {
        System.loadLibrary("oldidimage");
    }
}
