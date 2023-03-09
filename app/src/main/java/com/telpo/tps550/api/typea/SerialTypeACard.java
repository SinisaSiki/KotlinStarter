package com.telpo.tps550.api.typea;

import android.util.Log;
import com.telpo.tps550.api.TelpoException;
import com.telpo.tps550.api.TimeoutException;
import com.telpo.tps550.api.util.StringUtil;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

/* loaded from: classes.dex */
public class SerialTypeACard {
    private static final String TAG = "TELPO_SDK";
    private static final byte[] TSAM_HEADER = {-86, -86, -86, -106, 105};
    private byte[] Block;
    private byte[] Newwritedata;
    private byte[] Password;
    private int block_;
    private OutputStream mOutputStream;
    private int section_;
    private byte[] tbuffer = new byte[20];
    private Boolean writeright = false;
    private byte[] test = {-86, -86, -86, -106, 105, 0, 10, 5, 0, -112, -80, 13, 56, 30, 0, 0, 4, -86, -86, -86, -106, 105, 0, 3, 11, 0, -112, -104};
    private byte[] currect = {-86, -86, -86, -106, 105, 0, 3, 11, 0, -112, -104};

    public SerialTypeACard(OutputStream outputStream) throws TelpoException {
        this.mOutputStream = outputStream;
    }

    public void transmitBlock(byte[] bArr) throws TelpoException {
        this.Block = bArr;
    }

    public void transmittbuffer(byte[] bArr) throws TelpoException {
        this.tbuffer = bArr;
    }

    public void transmitpassword(byte[] bArr) throws TelpoException {
        this.Password = bArr;
    }

    public void transmitNewwritedata(byte[] bArr) throws TelpoException {
        this.Newwritedata = bArr;
    }

    public void checkTACard() throws TelpoException {
        byte[] bArr = {-86, -86, -86, -106, 105, 0, 4, Byte.MIN_VALUE, 5, 16, -111};
        for (int i = 0; i < 11; i++) {
            try {
                OutputStream outputStream = this.mOutputStream;
                if (outputStream != null) {
                    outputStream.write(bArr[i]);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0053, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:11:0x0054, code lost:
        r2 = r2 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:5:0x0013, code lost:
        if (r2 < 3) goto L8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:6:0x0015, code lost:
        r0 = r9.tbuffer;
        r1 = com.telpo.tps550.api.typea.SerialTypeACard.TSAM_HEADER;
        r0 = java.util.Arrays.copyOfRange(r0, r1.length + 5, r1.length + 9);
        new java.lang.String();
        r0 = decodetcarduid(r0);
        android.util.Log.e("newuid2", r0);
        r1 = new com.telpo.tps550.api.typea.TAInfo();
        r1.setNum(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:7:0x0045, code lost:
        return r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x0051, code lost:
        if (r9.tbuffer[(com.telpo.tps550.api.typea.SerialTypeACard.TSAM_HEADER.length + r2) + 2] == r1[r2]) goto L11;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public com.telpo.tps550.api.typea.TAInfo requestTACard() throws com.telpo.tps550.api.TelpoException {
        /*
            r9 = this;
            r0 = 3
            byte[] r1 = new byte[r0]
            r2 = 0
            r3 = 5
            r1[r2] = r3
            r4 = -112(0xffffffffffffff90, float:NaN)
            r5 = 2
            r1[r5] = r4
            r4 = r2
        Ld:
            byte[] r6 = com.telpo.tps550.api.typea.SerialTypeACard.TSAM_HEADER
            int r7 = r6.length
            r8 = 0
            if (r4 < r7) goto L57
        L13:
            if (r2 < r0) goto L46
            byte[] r0 = r9.tbuffer
            byte[] r1 = com.telpo.tps550.api.typea.SerialTypeACard.TSAM_HEADER
            int r2 = r1.length
            int r2 = r2 + r3
            int r1 = r1.length
            int r1 = r1 + 9
            byte[] r0 = java.util.Arrays.copyOfRange(r0, r2, r1)
            java.lang.String r1 = new java.lang.String
            r1.<init>()
            java.lang.String r0 = decodetcarduid(r0)
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.StringBuilder r1 = r1.append(r0)
            java.lang.String r1 = r1.toString()
            java.lang.String r2 = "newuid2"
            android.util.Log.e(r2, r1)
            com.telpo.tps550.api.typea.TAInfo r1 = new com.telpo.tps550.api.typea.TAInfo
            r1.<init>()
            r1.setNum(r0)
            return r1
        L46:
            byte[] r4 = r9.tbuffer
            byte[] r6 = com.telpo.tps550.api.typea.SerialTypeACard.TSAM_HEADER
            int r6 = r6.length
            int r6 = r6 + r2
            int r6 = r6 + r5
            r4 = r4[r6]
            r6 = r1[r2]
            if (r4 == r6) goto L54
            return r8
        L54:
            int r2 = r2 + 1
            goto L13
        L57:
            byte[] r7 = r9.tbuffer
            r7 = r7[r4]
            r6 = r6[r4]
            if (r7 == r6) goto L60
            return r8
        L60:
            int r4 = r4 + 1
            goto Ld
        */
        throw new UnsupportedOperationException("Method not decompiled: com.telpo.tps550.api.typea.SerialTypeACard.requestTACard():com.telpo.tps550.api.typea.TAInfo");
    }

    public TAInfo checkTACard(int i) throws TelpoException {
        long currentTimeMillis = System.currentTimeMillis();
        byte[] bArr = {-86, -86, -86, -106, 105, 0, 4, Byte.MIN_VALUE, 5, 16, -111};
        TAInfo tAInfo = null;
        for (long currentTimeMillis2 = System.currentTimeMillis(); tAInfo == null && currentTimeMillis2 - currentTimeMillis < i; currentTimeMillis2 = System.currentTimeMillis()) {
            for (int i2 = 0; i2 < 11; i2++) {
                try {
                    this.mOutputStream.write(bArr[i2]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                tAInfo = requestTACard();
            } catch (TelpoException e2) {
                e2.printStackTrace();
            }
        }
        if (tAInfo != null) {
            return tAInfo;
        }
        throw new TimeoutException();
    }

    private static String decodetcarduid(byte[] bArr) {
        StringBuilder sb = new StringBuilder("");
        for (byte b : bArr) {
            String hexString = Integer.toHexString(b & 255);
            if (hexString.length() == 1) {
                hexString = String.valueOf('0') + hexString;
            }
            sb.append(hexString);
        }
        return sb.toString();
    }

    public void checkPW() throws TelpoException {
        String str;
        byte[] bArr;
        byte[] bArr2 = new byte[17];
        int i = 10;
        System.arraycopy(new byte[]{-86, -86, -86, -106, 105, 0, 16, Byte.MIN_VALUE, 11, 1}, 0, bArr2, 0, 10);
        int i2 = 0;
        while (true) {
            byte[] bArr3 = this.Password;
            if (i2 >= bArr3.length) {
                break;
            }
            bArr2[i2 + 10] = bArr3[i2];
            i2++;
            i = i;
        }
        bArr2[16] = -102;
        Log.d("TAG", "Block:" + StringUtil.toHexString(this.Block));
        String hexString = StringUtil.toHexString(this.Block);
        if (hexString.substring(0, 1).equals("0")) {
            hexString = hexString.substring(1, 2);
        }
        this.block_ = Integer.valueOf(hexString).intValue() / 4;
        this.section_ = Integer.valueOf(hexString).intValue() % 4;
        int i3 = this.block_;
        if (i3 < i || Integer.toHexString(i3).length() < 2) {
            str = "0010800B0" + Integer.toHexString(this.block_).toUpperCase() + StringUtil.toHexString(this.Password);
        } else {
            str = "0010800B" + Integer.toHexString(this.block_).toUpperCase() + StringUtil.toHexString(this.Password);
        }
        byte xor = getXor(hexStringToBytes(str));
        if (Integer.toHexString(Integer.valueOf(this.block_).intValue()).length() < 2) {
            bArr = hexStringToBytes("0" + Integer.toHexString(Integer.valueOf(this.block_).intValue()));
        } else {
            bArr = hexStringToBytes(Integer.toHexString(Integer.valueOf(this.block_).intValue()));
        }
        byte b = bArr[0];
        Log.d("TAG", "psw_check_block:" + StringUtil.toHexString(new byte[]{b}));
        byte[] bArr4 = this.Password;
        byte[] bArr5 = {-86, -86, -86, -106, 105, 0, 16, Byte.MIN_VALUE, 11, b, bArr4[0], bArr4[1], bArr4[2], bArr4[3], bArr4[4], bArr4[5], xor};
        Log.d("TAG", "cmd_pwcheck:" + StringUtil.toHexString(bArr5));
        for (int i4 = 0; i4 < 17; i4++) {
            try {
                this.mOutputStream.write(bArr5[i4]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.e("checkPW", "success");
    }

    private boolean isSame() {
        Log.e("TAG", "tbuffer is" + StringUtil.toHexString(this.tbuffer));
        for (int i = 17; i < 28; i++) {
            if (this.tbuffer[i] != this.test[i]) {
                Log.d("TAG", "isSame false");
                return false;
            }
        }
        return true;
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x003f, code lost:
        if (r2 < 3) goto L14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x0046, code lost:
        return true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x0052, code lost:
        if (r9.tbuffer[(com.telpo.tps550.api.typea.SerialTypeACard.TSAM_HEADER.length + r2) + 2] == r4[r2]) goto L18;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x0054, code lost:
        android.util.Log.d("TAG", "false2");
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x0059, code lost:
        return false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x005a, code lost:
        r2 = r2 + 1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.Boolean requestpw() {
        /*
            r9 = this;
            java.lang.String r0 = "TAG"
            java.lang.String r1 = "before isSame"
            android.util.Log.e(r0, r1)
            boolean r1 = r9.isSame()
            r2 = 0
            java.lang.Boolean r3 = java.lang.Boolean.valueOf(r2)
            if (r1 == 0) goto L2d
            java.lang.String r1 = "isSame true"
            android.util.Log.d(r0, r1)
            r1 = 512(0x200, float:7.175E-43)
            byte[] r1 = new byte[r1]
            r9.tbuffer = r1
            r1 = r2
        L1e:
            byte[] r4 = r9.currect
            int r5 = r4.length
            if (r1 < r5) goto L24
            goto L2d
        L24:
            byte[] r5 = r9.tbuffer
            r4 = r4[r1]
            r5[r1] = r4
            int r1 = r1 + 1
            goto L1e
        L2d:
            r1 = 3
            byte[] r4 = new byte[r1]
            r5 = 11
            r4[r2] = r5
            r5 = -112(0xffffffffffffff90, float:NaN)
            r6 = 2
            r4[r6] = r5
            r5 = r2
        L3a:
            byte[] r7 = com.telpo.tps550.api.typea.SerialTypeACard.TSAM_HEADER
            int r8 = r7.length
            if (r5 < r8) goto L5d
        L3f:
            if (r2 < r1) goto L47
            r0 = 1
            java.lang.Boolean r0 = java.lang.Boolean.valueOf(r0)
            return r0
        L47:
            byte[] r5 = r9.tbuffer
            byte[] r7 = com.telpo.tps550.api.typea.SerialTypeACard.TSAM_HEADER
            int r7 = r7.length
            int r7 = r7 + r2
            int r7 = r7 + r6
            r5 = r5[r7]
            r7 = r4[r2]
            if (r5 == r7) goto L5a
            java.lang.String r1 = "false2"
            android.util.Log.d(r0, r1)
            return r3
        L5a:
            int r2 = r2 + 1
            goto L3f
        L5d:
            byte[] r8 = r9.tbuffer
            r8 = r8[r5]
            r7 = r7[r5]
            if (r8 == r7) goto L6b
            java.lang.String r1 = "false1"
            android.util.Log.d(r0, r1)
            return r3
        L6b:
            int r5 = r5 + 1
            goto L3a
        */
        throw new UnsupportedOperationException("Method not decompiled: com.telpo.tps550.api.typea.SerialTypeACard.requestpw():java.lang.Boolean");
    }

    public void readData() throws TelpoException {
        String str;
        byte[] bArr;
        int i = this.block_;
        if (i < 10 || Integer.toHexString(i).length() < 2) {
            str = "0006800D0" + Integer.toHexString(this.block_).toUpperCase() + "0" + this.section_ + "10";
        } else {
            str = "0006800D" + Integer.toHexString(this.block_).toUpperCase() + "0" + this.section_ + "10";
        }
        byte xor = getXor(hexStringToBytes(str));
        if (Integer.toHexString(Integer.valueOf(this.block_).intValue()).length() < 2) {
            bArr = hexStringToBytes("0" + Integer.toHexString(Integer.valueOf(this.block_).intValue()));
        } else {
            bArr = hexStringToBytes(Integer.toHexString(Integer.valueOf(this.block_).intValue()));
        }
        byte[] bArr2 = {-86, -86, -86, -106, 105, 0, 6, Byte.MIN_VALUE, 13, bArr[0], hexStringToBytes("0" + Integer.toHexString(Integer.valueOf(this.section_).intValue()))[0], 16, xor};
        for (int i2 = 0; i2 < 13; i2++) {
            try {
                this.mOutputStream.write(bArr2[i2]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public TASectorInfo requestData() {
        byte[] bArr = {13, 0, -112};
        Boolean bool = true;
        int i = 0;
        while (true) {
            byte[] bArr2 = TSAM_HEADER;
            if (i < bArr2.length) {
                if (this.tbuffer[i] != bArr2[i]) {
                    Boolean.valueOf(false);
                    return null;
                }
                i++;
            } else {
                for (int i2 = 0; i2 < 3; i2++) {
                    if (this.tbuffer[TSAM_HEADER.length + i2 + 2] != bArr[i2]) {
                        Boolean.valueOf(false);
                        return null;
                    }
                }
                if (!bool.booleanValue()) {
                    return null;
                }
                byte[] bArr3 = this.tbuffer;
                byte[] bArr4 = TSAM_HEADER;
                byte[] copyOfRange = Arrays.copyOfRange(bArr3, bArr4.length + 5, bArr4.length + 21);
                new String();
                String decodetcarduid = decodetcarduid(copyOfRange);
                Log.e("requestData", decodetcarduid);
                TASectorInfo tASectorInfo = new TASectorInfo();
                tASectorInfo.setSectorData(decodetcarduid);
                return tASectorInfo;
            }
        }
    }

    public Boolean writeInData() throws TelpoException {
        byte[] bArr;
        byte[] bArr2 = new byte[29];
        System.arraycopy(new byte[]{-86, -86, -86, -106, 105, 0, 22, Byte.MIN_VALUE, 14, 1, 1, 16}, 0, bArr2, 0, 12);
        byte[] bArr3 = this.Newwritedata;
        if (bArr3 == null) {
            this.writeright = false;
            return false;
        } else if (bArr3 == null) {
            return null;
        } else {
            int i = 0;
            while (true) {
                bArr = this.Newwritedata;
                if (i >= bArr.length) {
                    break;
                }
                bArr2[i + 12] = bArr[i];
                i++;
            }
            if (bArr.length < 16) {
                int length = 16 - bArr.length;
                for (int i2 = 0; i2 < length; i2++) {
                    bArr2[this.Newwritedata.length + 12 + i2] = 0;
                }
            }
            byte[] bArr4 = new byte[23];
            System.arraycopy(new byte[]{0, 22, Byte.MIN_VALUE, 14, 1, 1, 16}, 0, bArr4, 0, 7);
            int i3 = 0;
            while (true) {
                byte[] bArr5 = this.Newwritedata;
                if (i3 >= bArr5.length) {
                    break;
                }
                bArr4[i3 + 7] = bArr5[i3];
                i3++;
            }
            bArr2[28] = crc(bArr4);
            for (int i4 = 0; i4 < 29; i4++) {
                try {
                    this.mOutputStream.write(bArr2[i4]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            this.writeright = true;
            return true;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x002c, code lost:
        return false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:11:0x002d, code lost:
        r3 = r3 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:5:0x0017, code lost:
        if (r3 < 3) goto L8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:7:0x001e, code lost:
        return true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x002a, code lost:
        if (r8.tbuffer[(com.telpo.tps550.api.typea.SerialTypeACard.TSAM_HEADER.length + r3) + 2] == r1[r3]) goto L11;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.Boolean requestwrite() {
        /*
            r8 = this;
            r0 = 3
            byte[] r1 = new byte[r0]
            r2 = 14
            r3 = 0
            java.lang.Boolean r4 = java.lang.Boolean.valueOf(r3)
            r1[r3] = r2
            r2 = -112(0xffffffffffffff90, float:NaN)
            r5 = 2
            r1[r5] = r2
            r2 = r3
        L12:
            byte[] r6 = com.telpo.tps550.api.typea.SerialTypeACard.TSAM_HEADER
            int r7 = r6.length
            if (r2 < r7) goto L30
        L17:
            if (r3 < r0) goto L1f
            r0 = 1
            java.lang.Boolean r0 = java.lang.Boolean.valueOf(r0)
            return r0
        L1f:
            byte[] r2 = r8.tbuffer
            byte[] r6 = com.telpo.tps550.api.typea.SerialTypeACard.TSAM_HEADER
            int r6 = r6.length
            int r6 = r6 + r3
            int r6 = r6 + r5
            r2 = r2[r6]
            r6 = r1[r3]
            if (r2 == r6) goto L2d
            return r4
        L2d:
            int r3 = r3 + 1
            goto L17
        L30:
            byte[] r7 = r8.tbuffer
            r7 = r7[r2]
            r6 = r6[r2]
            if (r7 == r6) goto L39
            return r4
        L39:
            int r2 = r2 + 1
            goto L12
        */
        throw new UnsupportedOperationException("Method not decompiled: com.telpo.tps550.api.typea.SerialTypeACard.requestwrite():java.lang.Boolean");
    }

    private byte crc(byte[] bArr) {
        byte b = 0;
        for (byte b2 : bArr) {
            b = (byte) (b ^ b2);
        }
        return b;
    }

    private static byte[] hexStringToBytes(String str) {
        if (str == null || str.equals("")) {
            return null;
        }
        String replace = str.toUpperCase().replace(" ", "");
        int length = replace.length() / 2;
        char[] charArray = replace.toCharArray();
        byte[] bArr = new byte[length];
        for (int i = 0; i < length; i++) {
            int i2 = i * 2;
            bArr[i] = (byte) (charToByte(charArray[i2 + 1]) | (charToByte(charArray[i2]) << 4));
        }
        return bArr;
    }

    private static String bytesToHexString(byte[] bArr) {
        StringBuilder sb = new StringBuilder("");
        if (bArr == null || bArr.length <= 0) {
            return null;
        }
        for (byte b : bArr) {
            String hexString = Integer.toHexString(b & 255);
            if (hexString.length() < 2) {
                sb.append(0);
            }
            sb.append(hexString);
        }
        return sb.toString();
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    private static byte getXor(byte[] bArr) {
        byte b = bArr[0];
        for (int i = 1; i < bArr.length; i++) {
            b = (byte) (b ^ bArr[i]);
        }
        return b;
    }
}
