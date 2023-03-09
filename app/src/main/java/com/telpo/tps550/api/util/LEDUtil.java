package com.telpo.tps550.api.util;

import android.content.Context;
import com.telpo.tps550.api.InternalErrorException;
import com.telpo.tps550.api.StringTooLongException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public class LEDUtil {
    private static final String ASC16 = "asc16";
    private static final String ENCODE = "GB2312";
    public static final int LOCATION_LEFT = 0;
    public static final int LOCATION_MIDDLE = 1;
    public static final int LOCATION_NOT_SET = 0;
    public static final int LOCATION_RIGHT = 2;
    public static final int ROW_NUM_1 = 1;
    public static final int ROW_NUM_2 = 2;
    private static final String ZK16 = "hzk16";
    private byte[][] arr;
    Context mContext;
    byte[] bitmapC51 = new byte[512];
    private int all_16_32 = 16;
    private int all_2_4 = 2;
    private int all_32_128 = 32;
    private int font_width = 8;
    private int font_height = 16;
    private int all_16 = 16;

    public LEDUtil(Context context) {
        this.mContext = context;
    }

    private byte[][] resolveString(String str) {
        if (str.charAt(0) < 128) {
            this.arr = (byte[][]) Array.newInstance(byte.class, this.font_height, this.font_width);
            byte[] read_a = read_a(str.charAt(0));
            int i = 0;
            int i2 = 0;
            while (i2 < 16) {
                int i3 = i;
                int i4 = 0;
                for (int i5 = 0; i5 < 1; i5++) {
                    for (int i6 = 0; i6 < 8; i6++) {
                        if (((read_a[i3] >> (7 - i6)) & 1) == 1) {
                            this.arr[i2][i4] = 1;
                        } else {
                            this.arr[i2][i4] = 0;
                        }
                        i4++;
                    }
                    i3++;
                }
                i2++;
                i = i3;
            }
        } else {
            int i7 = this.all_16_32;
            this.arr = (byte[][]) Array.newInstance(byte.class, i7, i7);
            int[] byteCode = getByteCode(str.substring(0, 1));
            byte[] read = read(byteCode[0], byteCode[1]);
            int i8 = 0;
            for (int i9 = 0; i9 < this.all_16_32; i9++) {
                int i10 = 0;
                for (int i11 = 0; i11 < this.all_2_4; i11++) {
                    for (int i12 = 0; i12 < 8; i12++) {
                        if (((read[i8] >> (7 - i12)) & 1) == 1) {
                            this.arr[i9][i10] = 1;
                        } else {
                            this.arr[i9][i10] = 0;
                        }
                        i10++;
                    }
                    i8++;
                }
            }
        }
        return this.arr;
    }

    private byte[] read_a(char c) {
        byte[] bArr = null;
        try {
            bArr = new byte[this.all_16];
            InputStream open = this.mContext.getResources().getAssets().open(ASC16);
            open.skip(c * 16);
            open.read(bArr, 0, this.all_16);
            open.close();
            return bArr;
        } catch (IOException e) {
            e.printStackTrace();
            return bArr;
        }
    }

    private byte[] read(int i, int i2) {
        int i3 = i - 160;
        int i4 = i2 - 160;
        byte[] bArr = null;
        try {
            InputStream open = this.mContext.getResources().getAssets().open(ZK16);
            open.skip(this.all_32_128 * ((((i3 - 1) * 94) + i4) - 1));
            int i5 = this.all_32_128;
            bArr = new byte[i5];
            open.read(bArr, 0, i5);
            open.close();
            return bArr;
        } catch (Exception unused) {
            return bArr;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private int[] getByteCode(String str) {
        int[] iArr = new int[2];
        try {
            byte[] bytes = str.getBytes(ENCODE);
            iArr[0] = bytes[0] < 0 ? bytes[0] + 256 : bytes[0];
            iArr[1] = bytes[1] < 0 ? bytes[1] + 256 : bytes[1];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iArr;
    }

    public byte[] getTextC51(String str, int i, String str2, int i2) throws StringTooLongException, InternalErrorException {
        String str3;
        String str4;
        Object obj;
        Object obj2;
        Object obj3;
        String str5;
        String str6;
        Object obj4;
        String str7;
        String str8;
        Object obj5;
        Object obj6;
        int i3 = i;
        Pattern compile = Pattern.compile("[一-龥]");
        String str9 = str == null ? "" : str;
        String str10 = "©";
        String str11 = "π";
        Object obj7 = "";
        String str12 = str2 == null ? "" : str2;
        Object obj8 = "™";
        int i4 = 128;
        if (!str9.equals("")) {
            Object obj9 = "®";
            String str13 = str10;
            int i5 = 0;
            int i6 = 0;
            while (i5 < str9.length()) {
                String sb = new StringBuilder(String.valueOf(str9.charAt(i5))).toString();
                if (compile.matcher(sb).matches() || sb.equals("√") || sb.equals(str11) || sb.equals("÷") || sb.equals("×") || sb.equals("°") || sb.equals("℅") || sb.equals("¶") || sb.equals("∆")) {
                    obj6 = obj9;
                    str8 = str13;
                } else {
                    str8 = str13;
                    obj6 = obj9;
                    if (!sb.equals(str8) && !sb.equals(obj6)) {
                        str7 = str11;
                        obj5 = obj8;
                        if (!sb.equals(obj5)) {
                            i6 = sb.equals(" ") ? i6 + 1 : i6 + 8;
                            i5++;
                            obj8 = obj5;
                            str13 = str8;
                            str11 = str7;
                            i4 = 128;
                            obj9 = obj6;
                            i3 = i;
                        }
                        i6 += 16;
                        i5++;
                        obj8 = obj5;
                        str13 = str8;
                        str11 = str7;
                        i4 = 128;
                        obj9 = obj6;
                        i3 = i;
                    }
                }
                str7 = str11;
                obj5 = obj8;
                i6 += 16;
                i5++;
                obj8 = obj5;
                str13 = str8;
                str11 = str7;
                i4 = 128;
                obj9 = obj6;
                i3 = i;
            }
            if (i6 > i4) {
                throw new StringTooLongException();
            }
            if (i6 != i4 && i6 < i4) {
                if (i3 == 1) {
                    int i7 = (128 - i6) / 2;
                    for (int i8 = 0; i8 < i7; i8++) {
                        str9 = " " + str9;
                    }
                    for (int i9 = i7 + i6; i9 < i4; i9++) {
                        str9 = String.valueOf(str9) + " ";
                    }
                } else if (i3 == 2) {
                    for (int i10 = 0; i10 < 128 - i6; i10++) {
                        str9 = " " + str9;
                    }
                } else {
                    for (int i11 = 0; i11 < 128 - i6; i11++) {
                        str9 = String.valueOf(str9) + " ";
                    }
                }
            }
            str4 = str12;
            obj = obj7;
            obj3 = obj9;
            str10 = str13;
            str3 = str11;
            obj2 = obj8;
        } else {
            str3 = str11;
            obj3 = "®";
            obj2 = obj8;
            int i12 = 0;
            while (i12 < i4) {
                str9 = " " + str9;
                i12++;
                obj2 = obj2;
                obj7 = obj7;
                obj3 = obj3;
                str3 = str3;
                i4 = i4;
            }
            str4 = str12;
            obj = obj7;
        }
        if (!str4.equals(obj)) {
            Object obj10 = obj2;
            str5 = str9;
            int i13 = 0;
            int i14 = 0;
            while (i14 < str4.length()) {
                int i15 = i4;
                String str14 = str3;
                String str15 = str4;
                String sb2 = new StringBuilder(String.valueOf(str4.charAt(i14))).toString();
                if (compile.matcher(sb2).matches() || sb2.equals("√") || sb2.equals(str14) || sb2.equals("÷") || sb2.equals("×") || sb2.equals("°") || sb2.equals("℅") || sb2.equals("¶") || sb2.equals("∆") || sb2.equals(str10) || sb2.equals(obj3)) {
                    obj4 = obj10;
                } else {
                    obj4 = obj10;
                    if (!sb2.equals(obj4)) {
                        i13 = sb2.equals(" ") ? i13 + 1 : i13 + 8;
                        i14++;
                        str3 = str14;
                        obj10 = obj4;
                        i4 = i15;
                        str4 = str15;
                    }
                }
                i13 += 16;
                i14++;
                str3 = str14;
                obj10 = obj4;
                i4 = i15;
                str4 = str15;
            }
            if (i13 > i4) {
                throw new StringTooLongException();
            }
            if (i13 != i4 && i13 < i4) {
                if (i2 == 1) {
                    int i16 = (128 - i13) / 2;
                    int i17 = 0;
                    while (i17 < i16) {
                        str4 = " " + str4;
                        i17++;
                        i4 = 128;
                    }
                    int i18 = i16 + i13;
                    while (i18 < i4) {
                        str4 = String.valueOf(str4) + " ";
                        i18++;
                        i4 = 128;
                    }
                } else if (i2 == 2) {
                    for (int i19 = 0; i19 < 128 - i13; i19++) {
                        str4 = " " + str4;
                    }
                }
                str6 = str4;
                return getTextC51(String.valueOf(str5) + str6);
            }
        } else {
            str5 = str9;
        }
        str6 = str4;
        return getTextC51(String.valueOf(str5) + str6);
    }

    /* JADX WARN: Removed duplicated region for block: B:111:0x023a  */
    /* JADX WARN: Removed duplicated region for block: B:130:0x027f A[Catch: Exception -> 0x02f9, TryCatch #0 {Exception -> 0x02f9, blocks: (B:3:0x000e, B:4:0x002c, B:6:0x0032, B:9:0x003d, B:12:0x0049, B:15:0x0055, B:17:0x005f, B:19:0x007a, B:21:0x0081, B:22:0x008e, B:23:0x0098, B:25:0x009d, B:26:0x00aa, B:27:0x00b4, B:29:0x00b9, B:30:0x00c7, B:31:0x00d1, B:34:0x00d7, B:35:0x00e5, B:36:0x00ef, B:41:0x012c, B:43:0x0132, B:45:0x0138, B:47:0x013e, B:49:0x0144, B:51:0x014a, B:53:0x0150, B:55:0x0156, B:57:0x015e, B:59:0x0164, B:61:0x016c, B:63:0x0174, B:66:0x017f, B:69:0x0189, B:71:0x0196, B:73:0x019c, B:74:0x01a1, B:76:0x01a7, B:77:0x01ac, B:79:0x01b2, B:80:0x01b7, B:82:0x01bd, B:83:0x01c2, B:85:0x01ca, B:87:0x01d5, B:89:0x01df, B:90:0x01e8, B:92:0x01f2, B:93:0x01f9, B:95:0x01ff, B:96:0x0204, B:99:0x020d, B:103:0x0216, B:105:0x021b, B:106:0x0220, B:108:0x0226, B:109:0x022f, B:110:0x0237, B:114:0x0241, B:118:0x0246, B:120:0x024b, B:121:0x024e, B:123:0x0254, B:124:0x0261, B:125:0x026d, B:126:0x0270, B:128:0x027a, B:130:0x027f, B:132:0x0285, B:134:0x028b, B:136:0x0291, B:138:0x0297, B:140:0x029d, B:142:0x02a5, B:144:0x02ad, B:146:0x02b3, B:148:0x02bb, B:150:0x02c3, B:153:0x02cc), top: B:163:0x000e }] */
    /* JADX WARN: Removed duplicated region for block: B:155:0x02d2  */
    /* JADX WARN: Removed duplicated region for block: B:156:0x02d5  */
    /* JADX WARN: Removed duplicated region for block: B:158:0x02da  */
    /* JADX WARN: Removed duplicated region for block: B:159:0x02df  */
    /* JADX WARN: Removed duplicated region for block: B:98:0x020a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public byte[] getTextC51(java.lang.String r30) throws com.telpo.tps550.api.InternalErrorException {
        /*
            Method dump skipped, instructions count: 767
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.telpo.tps550.api.util.LEDUtil.getTextC51(java.lang.String):byte[]");
    }

    private byte BToH(String str) {
        String hexString = Integer.toHexString(Integer.valueOf(toD(str, 2)).intValue());
        if (hexString.length() == 1) {
            hexString = "0" + hexString;
        }
        return StringUtil.toBytes(hexString)[0];
    }

    private String toD(String str, int i) {
        int i2;
        int i3 = 0;
        for (int i4 = 0; i4 < str.length(); i4++) {
            i3 = (int) (i3 + (formatting(str.substring(i4, i2)) * Math.pow(i, (str.length() - i4) - 1)));
        }
        return String.valueOf(i3);
    }

    private int formatting(String str) {
        int i = 0;
        for (int i2 = 0; i2 < 10; i2++) {
            if (str.equals(String.valueOf(i2))) {
                i = i2;
            }
        }
        if (str.equals("a")) {
            i = 10;
        }
        if (str.equals("b")) {
            i = 11;
        }
        if (str.equals("c")) {
            i = 12;
        }
        if (str.equals("d")) {
            i = 13;
        }
        if (str.equals("e")) {
            i = 14;
        }
        if (str.equals("f")) {
            return 15;
        }
        return i;
    }

    private byte[][] getSpecialChar(String str) {
        int[][] iArr = (int[][]) Array.newInstance(int.class, 16, 8);
        byte[][] resolveString = resolveString(str);
        for (int i = 0; i < resolveString.length; i++) {
            for (int i2 = 0; i2 < resolveString[i].length; i2++) {
                if (resolveString[i][i2] == 1) {
                    resolveString[i][i2] = 0;
                    if (str.equals("f")) {
                        iArr[i][i2 + 2] = 1;
                    } else if (str.equals("j")) {
                        iArr[i][i2 - 1] = 1;
                    }
                }
            }
        }
        for (int i3 = 0; i3 < iArr.length; i3++) {
            for (int i4 = 0; i4 < iArr[i3].length; i4++) {
                if (iArr[i3][i4] == 1) {
                    resolveString[i3][i4] = 1;
                }
            }
        }
        return resolveString;
    }

    private byte[][] getSpecialSign(String str) {
        byte[][] bArr;
        if (str.equals("•")) {
            byte[][] bArr2 = (byte[][]) Array.newInstance(byte.class, 16, 8);
            byte[] bArr3 = bArr2[5];
            byte[] bArr4 = bArr2[5];
            byte[] bArr5 = bArr2[5];
            byte[] bArr6 = bArr2[6];
            byte[] bArr7 = bArr2[6];
            byte[] bArr8 = bArr2[6];
            byte[] bArr9 = bArr2[6];
            byte[] bArr10 = bArr2[6];
            byte[] bArr11 = bArr2[7];
            byte[] bArr12 = bArr2[7];
            byte[] bArr13 = bArr2[7];
            byte[] bArr14 = bArr2[7];
            byte[] bArr15 = bArr2[7];
            byte[] bArr16 = bArr2[8];
            byte[] bArr17 = bArr2[8];
            byte[] bArr18 = bArr2[8];
            byte[] bArr19 = bArr2[8];
            byte[] bArr20 = bArr2[8];
            byte[] bArr21 = bArr2[9];
            byte[] bArr22 = bArr2[9];
            bArr2[9][4] = 1;
            bArr22[3] = 1;
            bArr21[2] = 1;
            bArr20[5] = 1;
            bArr19[4] = 1;
            bArr18[3] = 1;
            bArr17[2] = 1;
            bArr16[1] = 1;
            bArr15[5] = 1;
            bArr14[4] = 1;
            bArr13[3] = 1;
            bArr12[2] = 1;
            bArr11[1] = 1;
            bArr10[5] = 1;
            bArr9[4] = 1;
            bArr8[3] = 1;
            bArr7[2] = 1;
            bArr6[1] = 1;
            bArr5[4] = 1;
            bArr4[3] = 1;
            bArr3[2] = 1;
            return bArr2;
        }
        if (str.equals("∆")) {
            bArr = (byte[][]) Array.newInstance(byte.class, 16, 16);
            byte[] bArr23 = bArr[3];
            byte[] bArr24 = bArr[4];
            byte[] bArr25 = bArr[4];
            byte[] bArr26 = bArr[5];
            byte[] bArr27 = bArr[5];
            byte[] bArr28 = bArr[6];
            byte[] bArr29 = bArr[6];
            byte[] bArr30 = bArr[7];
            byte[] bArr31 = bArr[7];
            byte[] bArr32 = bArr[8];
            byte[] bArr33 = bArr[8];
            byte[] bArr34 = bArr[9];
            bArr[9][13] = 1;
            bArr34[1] = 1;
            bArr33[12] = 1;
            bArr32[2] = 1;
            bArr31[11] = 1;
            bArr30[3] = 1;
            char c = '\n';
            bArr29[10] = 1;
            bArr28[4] = 1;
            bArr27[9] = 1;
            bArr26[5] = 1;
            bArr25[8] = 1;
            bArr24[6] = 1;
            bArr23[7] = 1;
            int i = 0;
            while (i < 15) {
                bArr[c][i] = 1;
                i++;
                c = '\n';
            }
        } else if (str.equals("¥")) {
            bArr = resolveString("Y");
            for (int i2 = 2; i2 < 6; i2++) {
                bArr[7][i2] = 1;
            }
            for (int i3 = 2; i3 < 6; i3++) {
                bArr[9][i3] = 1;
            }
            byte[] bArr35 = bArr[11];
            bArr[11][5] = 0;
            bArr35[2] = 0;
            byte[] bArr36 = bArr[6];
            bArr[6][5] = 0;
            bArr36[2] = 0;
        } else if (str.equals("¢")) {
            bArr = resolveString("c");
            for (int i4 = 2; i4 < 4; i4++) {
                bArr[3][i4] = 1;
                bArr[4][i4] = 1;
                bArr[12][i4] = 1;
                bArr[13][i4] = 1;
            }
        } else if (str.equals("€")) {
            bArr = (byte[][]) Array.newInstance(byte.class, 16, 8);
            for (int i5 = 2; i5 < 11; i5++) {
                bArr[i5][1] = 1;
                bArr[i5][2] = 1;
            }
            byte[] bArr37 = bArr[1];
            byte[] bArr38 = bArr[1];
            bArr[1][5] = 1;
            bArr38[4] = 1;
            bArr37[3] = 1;
            byte[] bArr39 = bArr[11];
            byte[] bArr40 = bArr[11];
            bArr[11][5] = 1;
            bArr40[4] = 1;
            bArr39[3] = 1;
            byte[] bArr41 = bArr[5];
            byte[] bArr42 = bArr[5];
            bArr[5][4] = 1;
            bArr42[3] = 1;
            bArr41[0] = 1;
            byte[] bArr43 = bArr[7];
            byte[] bArr44 = bArr[7];
            bArr[7][4] = 1;
            bArr44[3] = 1;
            bArr43[0] = 1;
        } else if (str.equals("£")) {
            bArr = (byte[][]) Array.newInstance(byte.class, 16, 8);
            int i6 = 2;
            for (int i7 = 10; i6 < i7; i7 = i7) {
                bArr[i6][1] = 1;
                bArr[i6][2] = 1;
                i6++;
            }
            byte[] bArr45 = bArr[1];
            bArr[1][4] = 1;
            bArr45[3] = 1;
            bArr[2][5] = 1;
            byte[] bArr46 = bArr[6];
            byte[] bArr47 = bArr[6];
            bArr[6][4] = 1;
            bArr47[3] = 1;
            bArr46[0] = 1;
            bArr[10][1] = 1;
            for (int i8 = 0; i8 < 6; i8++) {
                bArr[11][i8] = 1;
            }
        } else if (str.equals(" ")) {
            return (byte[][]) Array.newInstance(byte.class, 16, 1);
        } else {
            return null;
        }
        return bArr;
    }
}
