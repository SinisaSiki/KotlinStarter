package com.telpo.nfc;

import android.util.Log;

/* loaded from: classes.dex */
public class SpliceM1cmd {
    public static byte[] spliceM1cmd_CLOSE_CARD() {
        return new byte[]{-18, -18, -18, -120, 4, 0, 18, 2, 3, 27};
    }

    private static void setLog(String str) {
        Log.d("tagg", str);
    }

    public static byte[] spliceM1cmd_AUTHENTICATE(int i, int i2, byte[] bArr) {
        byte[] bArr2 = new byte[18];
        int i3 = 0;
        bArr2[0] = -18;
        bArr2[1] = -18;
        bArr2[2] = -18;
        bArr2[3] = -120;
        bArr2[4] = 9;
        bArr2[5] = 0;
        bArr2[6] = 18;
        bArr2[7] = 2;
        bArr2[8] = 0;
        bArr2[9] = (byte) i;
        bArr2[10] = (byte) i2;
        int i4 = 11;
        for (byte b : bArr) {
            bArr2[i4] = b;
            i4++;
        }
        for (int i5 = 4; i5 < 17; i5++) {
            i3 += bArr2[i5];
        }
        bArr2[17] = (byte) i3;
        return bArr2;
    }

    public static byte[] spliceM1cmd_WRITE_BLOCK(int i, byte[] bArr) {
        byte[] bArr2 = new byte[27];
        int i2 = 0;
        bArr2[0] = -18;
        bArr2[1] = -18;
        bArr2[2] = -18;
        bArr2[3] = -120;
        bArr2[4] = 20;
        bArr2[5] = 0;
        bArr2[6] = 18;
        bArr2[7] = 2;
        bArr2[8] = 1;
        bArr2[9] = (byte) i;
        int i3 = 10;
        for (byte b : bArr) {
            bArr2[i3] = b;
            i3++;
        }
        for (int i4 = 4; i4 < 26; i4++) {
            i2 += bArr2[i4];
        }
        bArr2[26] = (byte) i2;
        return bArr2;
    }

    public static byte[] spliceM1cmd_READ_BLOCK(int i) {
        byte[] bArr = new byte[11];
        int i2 = 0;
        bArr[0] = -18;
        bArr[1] = -18;
        bArr[2] = -18;
        bArr[3] = -120;
        bArr[4] = 4;
        bArr[5] = 0;
        bArr[6] = 18;
        bArr[7] = 2;
        bArr[8] = 2;
        bArr[9] = (byte) i;
        for (int i3 = 4; i3 < 10; i3++) {
            i2 += bArr[i3];
        }
        bArr[10] = (byte) i2;
        return bArr;
    }

    public static byte[] spliceM1cmd_INIT_WALLET(int i, int i2) {
        byte[] bArr = new byte[15];
        int i3 = 0;
        bArr[0] = -18;
        bArr[1] = -18;
        bArr[2] = -18;
        bArr[3] = -120;
        int i4 = 8;
        bArr[4] = 8;
        bArr[5] = 0;
        bArr[6] = 18;
        bArr[7] = 2;
        bArr[8] = 4;
        bArr[9] = (byte) i;
        String hexString = Integer.toHexString(i2);
        while (hexString.length() < 8) {
            hexString = "0" + hexString;
        }
        int i5 = 10;
        while (i5 < 14) {
            bArr[i5] = toBytes(hexString.substring(i4 - 2, i4))[0];
            i5++;
            i4 -= 2;
        }
        for (int i6 = 4; i6 < 14; i6++) {
            i3 += bArr[i6];
        }
        bArr[14] = (byte) i3;
        return bArr;
    }

    public static byte[] spliceM1cmd_READ_WALLET(int i) {
        byte[] bArr = new byte[11];
        int i2 = 0;
        bArr[0] = -18;
        bArr[1] = -18;
        bArr[2] = -18;
        bArr[3] = -120;
        bArr[4] = 4;
        bArr[5] = 0;
        bArr[6] = 18;
        bArr[7] = 2;
        bArr[8] = 5;
        bArr[9] = (byte) i;
        for (int i3 = 4; i3 < 10; i3++) {
            i2 += bArr[i3];
        }
        bArr[10] = (byte) i2;
        return bArr;
    }

    public static byte[] spliceM1cmd_WRITE_WALLET(int i, int i2, int i3) {
        byte[] bArr = new byte[16];
        int i4 = 0;
        bArr[0] = -18;
        bArr[1] = -18;
        bArr[2] = -18;
        bArr[3] = -120;
        bArr[4] = 9;
        bArr[5] = 0;
        bArr[6] = 18;
        bArr[7] = 2;
        int i5 = 8;
        bArr[8] = 6;
        bArr[9] = (byte) i;
        bArr[10] = (byte) i2;
        String hexString = Integer.toHexString(i3);
        while (hexString.length() < 8) {
            hexString = "0" + hexString;
        }
        int i6 = 11;
        while (i6 < 15) {
            bArr[i6] = toBytes(hexString.substring(i5 - 2, i5))[0];
            i6++;
            i5 -= 2;
        }
        for (int i7 = 4; i7 < 15; i7++) {
            i4 += bArr[i7];
        }
        bArr[15] = (byte) i4;
        return bArr;
    }

    public static byte[] spliceM1cmd_TRANSFER(int i) {
        byte[] bArr = new byte[11];
        int i2 = 0;
        bArr[0] = -18;
        bArr[1] = -18;
        bArr[2] = -18;
        bArr[3] = -120;
        bArr[4] = 4;
        bArr[5] = 0;
        bArr[6] = 18;
        bArr[7] = 2;
        bArr[8] = 7;
        bArr[9] = (byte) i;
        for (int i3 = 4; i3 < 10; i3++) {
            i2 += bArr[i3];
        }
        bArr[10] = (byte) i2;
        return bArr;
    }

    public static byte[] spliceM1cmd_RESTORE(int i) {
        byte[] bArr = new byte[11];
        int i2 = 0;
        bArr[0] = -18;
        bArr[1] = -18;
        bArr[2] = -18;
        bArr[3] = -120;
        bArr[4] = 4;
        bArr[5] = 0;
        bArr[6] = 18;
        bArr[7] = 2;
        bArr[8] = 8;
        bArr[9] = (byte) i;
        for (int i3 = 4; i3 < 10; i3++) {
            i2 += bArr[i3];
        }
        bArr[10] = (byte) i2;
        return bArr;
    }

    public static CardTypeEnum getCardType(byte[] bArr) {
        if (bArr[0] == 1) {
            return CardTypeEnum.M0;
        }
        if (bArr[0] == 2) {
            return CardTypeEnum.TYPE_A;
        }
        if (bArr[0] == 3) {
            return CardTypeEnum.ISO15693;
        }
        if (bArr[0] == 4) {
            return CardTypeEnum.FELICA;
        }
        if (bArr[0] == 5) {
            return CardTypeEnum.ID_CARD;
        }
        if (bArr[0] == 6) {
            return CardTypeEnum.M1;
        }
        if (bArr[0] != 7) {
            return null;
        }
        return CardTypeEnum.TYPE_B;
    }

    public static byte[] spliceAPDUcmd(byte[] bArr) {
        int i;
        int length = bArr.length + 10;
        byte[] bArr2 = new byte[length];
        String hexString = Integer.toHexString(bArr.length + 3);
        while (true) {
            i = 4;
            if (hexString.length() >= 4) {
                break;
            }
            hexString = "0" + hexString;
        }
        int i2 = 0;
        bArr2[0] = -18;
        bArr2[1] = -18;
        bArr2[2] = -18;
        bArr2[3] = -120;
        bArr2[4] = toBytes(hexString.substring(2, 4))[0];
        bArr2[5] = toBytes(hexString.substring(0, 2))[0];
        bArr2[6] = 17;
        bArr2[7] = 1;
        bArr2[8] = 16;
        System.arraycopy(bArr, 0, bArr2, 9, bArr.length);
        while (true) {
            int i3 = length - 1;
            if (i < i3) {
                i2 += bArr2[i];
                i++;
            } else {
                bArr2[i3] = (byte) i2;
                return bArr2;
            }
        }
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
}
