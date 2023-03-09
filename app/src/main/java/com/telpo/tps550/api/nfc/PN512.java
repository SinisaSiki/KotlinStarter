package com.telpo.tps550.api.nfc;

/* loaded from: classes.dex */
public class PN512 {
    private static native synchronized int readnfc(byte[] bArr);

    public static synchronized String readnfc() {
        byte[] bArr;
        String str;
        synchronized (PN512.class) {
            readnfc(new byte[7]);
            str = "0x" + toHexString(bArr);
        }
        return str;
    }

    private static String toHexString(byte[] bArr) {
        if (bArr == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (byte b : bArr) {
            String hexString = Integer.toHexString(b & 255);
            if (hexString.length() == 1) {
                sb.append("0");
            }
            sb.append(hexString.toUpperCase());
        }
        return sb.toString();
    }

    static {
        System.loadLibrary("telpo_nfc");
    }
}
