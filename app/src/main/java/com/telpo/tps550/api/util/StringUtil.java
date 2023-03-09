package com.telpo.tps550.api.util;

import android.util.Log;
import java.util.Arrays;

/* loaded from: classes.dex */
public class StringUtil {

    /* loaded from: classes.dex */
    public enum DeviceModelEnum {
        FFP2,
        TPM301,
        TPS200,
        TPS320,
        TPS350,
        TPS350_5800,
        TPS350_4G,
        TPS350L,
        TPS358,
        TPS360,
        TPS360A,
        TPS360C,
        TPS360IC,
        TPS360U,
        TPS365,
        TPS390,
        TPS390A,
        TPS390F,
        TPS390L,
        TPS390P,
        TPS390U,
        TPS400,
        TPS400A,
        TPS400B,
        TPS400C,
        TPS450,
        TPS450CIC,
        TPS450C,
        TPS462,
        TPS464,
        TPS465,
        TPS468,
        TPS470,
        TPS480,
        TPS481,
        TPS506,
        TPS510,
        TPS510A,
        TPS510C,
        TPS510A_NHW,
        TPS510D,
        TPS510P,
        TPS513,
        TPS515,
        TPS515A,
        TPS515B,
        TPS515C,
        TPS515D,
        TPS520,
        TPS520A,
        TPS550,
        TPS550A,
        TPS550MTK,
        TPS550P,
        TPS550S,
        TPS570,
        TPS570A,
        TPS570L,
        TPS573,
        TPS574,
        TPS575,
        TPS580,
        TPS580A,
        TPS580ACRM,
        TPS586,
        TPS586A,
        TPS590,
        TPS610,
        TPS611,
        TPS612,
        TPS613,
        TPS615,
        TPS616,
        TPS617,
        TPS618,
        TPS650,
        TPS651,
        TPS650P,
        TPS650T,
        TPS656,
        TPS680,
        TPS681,
        TPS700,
        TPS716,
        TPS721,
        TPS732,
        TPS781,
        TPS900,
        TPS900B,
        TPS900MB,
        TPS910,
        TPS950,
        TPS980P,
        TPS981,
        C1B
    }

    public static String toHexString(byte[] bArr) {
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

    public void print(byte[] bArr) {
        Log.d("tagg", "0~1024[" + toHexString(Arrays.copyOfRange(bArr, 0, 1024)) + "]");
        Log.d("tagg", "1024~end[" + toHexString(Arrays.copyOfRange(bArr, 1024, bArr.length)) + "]");
    }

    public static byte[] toBytes(String str) {
        String upperCase = str.toUpperCase();
        int length = upperCase.length();
        if (length % 2 == 1) {
            upperCase = String.valueOf(upperCase) + "0";
            length++;
        }
        int i = length >> 1;
        byte[] bArr = new byte[i];
        int i2 = 0;
        int i3 = 0;
        while (i2 < i) {
            bArr[i2] = (byte) (((byte) ("0123456789ABCDEF".indexOf(upperCase.charAt(i3)) << 4)) | ((byte) "0123456789ABCDEF".indexOf(upperCase.charAt(i3 + 1))));
            i2++;
            i3 += 2;
        }
        return bArr;
    }
}
