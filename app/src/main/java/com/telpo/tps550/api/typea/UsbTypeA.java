package com.telpo.tps550.api.typea;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.util.Log;
import androidx.vectordrawable.graphics.drawable.PathInterpolatorCompat;
import com.telpo.tps550.api.TelpoException;
import com.telpo.tps550.api.idcard.Utils;
import com.telpo.tps550.api.util.StringUtil;
import java.util.Arrays;

/* loaded from: classes.dex */
public class UsbTypeA {
    private static final String TAG = "TELPO_SDK";
    private static final byte[] TSAM_HEADER = {-86, -86, -86, -106, 105};
    private byte[] Block;
    private byte[] Newwritedata;
    private byte[] Password;
    private int block_;
    private int section_;
    private UsbManager tUsbManager;
    private UsbDevice tcard_reader;

    public UsbTypeA(UsbManager usbManager, UsbDevice usbDevice) throws TelpoException {
        this.tUsbManager = usbManager;
        this.tcard_reader = usbDevice;
    }

    public TAInfo checkTACard() throws TelpoException {
        Utils.setStatusRecord("into checkTACard\n", true);
        byte[] requestUid = requestUid(new byte[]{-86, -86, -86, -106, 105, 0, 4, Byte.MIN_VALUE, 5, 16, -111}, new byte[]{5, 0, -112});
        byte[] copyOfRange = (requestUid != null && requestUid.length > 9 && requestUid[7] == 5 && requestUid[8] == 0 && requestUid[9] == -112) ? Arrays.copyOfRange(requestUid, 10, 14) : null;
        if (copyOfRange == null) {
            return null;
        }
        String decodetcarduid = decodetcarduid(copyOfRange);
        TAInfo tAInfo = new TAInfo();
        tAInfo.setNum(decodetcarduid);
        Utils.setStatusRecord("num[" + tAInfo + "]\n", true);
        return tAInfo;
    }

    /* JADX WARN: Code restructure failed: missing block: B:28:0x00be, code lost:
        if (r2 < r12.length) goto L31;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x00cd, code lost:
        if (r4[(com.telpo.tps550.api.typea.UsbTypeA.TSAM_HEADER.length + r2) + 2] == r12[r2]) goto L35;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x00cf, code lost:
        r7.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x00d2, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x00d3, code lost:
        r2 = r2 + 1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private byte[] requestUid(byte[] r11, byte[] r12) {
        /*
            r10 = this;
            android.hardware.usb.UsbDevice r0 = r10.tcard_reader
            r1 = 0
            if (r0 == 0) goto Le3
            int r2 = r12.length
            r3 = 3
            if (r2 != r3) goto Le3
            r2 = 0
            android.hardware.usb.UsbInterface r0 = r0.getInterface(r2)
            android.hardware.usb.UsbEndpoint r4 = r0.getEndpoint(r2)
            r5 = 1
            android.hardware.usb.UsbEndpoint r6 = r0.getEndpoint(r5)
            android.hardware.usb.UsbManager r7 = r10.tUsbManager
            android.hardware.usb.UsbDevice r8 = r10.tcard_reader
            android.hardware.usb.UsbDeviceConnection r7 = r7.openDevice(r8)
            r7.claimInterface(r0, r5)
            int r0 = r11.length
            r8 = 3000(0xbb8, float:4.204E-42)
            int r0 = r7.bulkTransfer(r6, r11, r0, r8)
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            java.lang.String r9 = "send["
            r6.<init>(r9)
            java.lang.String r11 = com.telpo.tps550.api.util.StringUtil.toHexString(r11)
            java.lang.StringBuilder r11 = r6.append(r11)
            java.lang.String r6 = "] >>> out["
            java.lang.StringBuilder r11 = r11.append(r6)
            java.lang.StringBuilder r11 = r11.append(r0)
            java.lang.String r0 = "]\n"
            java.lang.StringBuilder r11 = r11.append(r0)
            java.lang.String r11 = r11.toString()
            com.telpo.tps550.api.idcard.Utils.setStatusRecord(r11, r5)
            r11 = 1024(0x400, float:1.435E-42)
            byte[] r9 = new byte[r11]
            int r11 = r7.bulkTransfer(r4, r9, r11, r8)
            if (r11 <= 0) goto Le3
            byte[] r4 = java.util.Arrays.copyOfRange(r9, r2, r11)
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            java.lang.String r9 = "receive["
            r8.<init>(r9)
            java.lang.String r9 = com.telpo.tps550.api.util.StringUtil.toHexString(r4)
            java.lang.StringBuilder r8 = r8.append(r9)
            java.lang.StringBuilder r6 = r8.append(r6)
            java.lang.StringBuilder r11 = r6.append(r11)
            java.lang.StringBuilder r11 = r11.append(r0)
            java.lang.String r11 = r11.toString()
            com.telpo.tps550.api.idcard.Utils.setStatusRecord(r11, r5)
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            java.lang.String r0 = "receive:"
            r11.<init>(r0)
            java.lang.String r0 = com.telpo.tps550.api.util.StringUtil.toHexString(r4)
            java.lang.StringBuilder r11 = r11.append(r0)
            java.lang.String r11 = r11.toString()
            java.lang.String r0 = "tagg"
            android.util.Log.d(r0, r11)
            r11 = 2
            if (r4 == 0) goto Lb7
            int r0 = r4.length
            r6 = 5
            if (r0 <= r6) goto Lb7
            r0 = r4[r2]
            if (r0 != 0) goto Lb7
            r0 = r4[r5]
            if (r0 != 0) goto Lb7
            r0 = r4[r11]
            if (r0 != 0) goto Lb7
            r0 = r4[r3]
            if (r0 != 0) goto Lb7
            r0 = 4
            r0 = r4[r0]
            if (r0 != 0) goto Lb7
            r0 = r4[r6]
            if (r0 == 0) goto Lc0
        Lb7:
            r0 = r2
        Lb8:
            byte[] r3 = com.telpo.tps550.api.typea.UsbTypeA.TSAM_HEADER
            int r5 = r3.length
            if (r0 < r5) goto Ld6
        Lbd:
            int r0 = r12.length
            if (r2 < r0) goto Lc4
        Lc0:
            r7.close()
            return r4
        Lc4:
            byte[] r0 = com.telpo.tps550.api.typea.UsbTypeA.TSAM_HEADER
            int r0 = r0.length
            int r0 = r0 + r2
            int r0 = r0 + r11
            r0 = r4[r0]
            r3 = r12[r2]
            if (r0 == r3) goto Ld3
            r7.close()
            return r1
        Ld3:
            int r2 = r2 + 1
            goto Lbd
        Ld6:
            r5 = r4[r0]
            r3 = r3[r0]
            if (r5 == r3) goto Le0
            r7.close()
            return r1
        Le0:
            int r0 = r0 + 1
            goto Lb8
        Le3:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.telpo.tps550.api.typea.UsbTypeA.requestUid(byte[], byte[]):byte[]");
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

    public void transmitpassword(byte[] bArr) {
        this.Password = bArr;
    }

    public void transmitblock(byte[] bArr) {
        this.Block = bArr;
    }

    /* JADX WARN: Type inference failed for: r15v1 */
    /* JADX WARN: Type inference failed for: r15v8 */
    public Boolean checkPW() throws TelpoException {
        String str;
        byte[] bArr;
        Boolean.valueOf(false);
        byte[] bArr2 = new byte[17];
        int i = 11;
        System.arraycopy(new byte[]{-86, -86, -86, -106, 105, 0, 10, Byte.MIN_VALUE, 11}, 0, bArr2, 0, 9);
        bArr2[9] = this.Block[0];
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
        byte[] bArr4 = new byte[i];
        System.arraycopy(new byte[]{0, 10, Byte.MIN_VALUE, i}, 0, bArr4, 0, 4);
        byte[] bArr5 = this.Block;
        if (bArr5 != null) {
            bArr4[4] = bArr5[0];
        }
        int i3 = 0;
        while (true) {
            byte[] bArr6 = this.Password;
            if (i3 >= bArr6.length) {
                break;
            }
            bArr4[i3 + 5] = bArr6[i3];
            i3++;
        }
        bArr2[16] = crc(bArr4);
        String hexString = StringUtil.toHexString(this.Block);
        if (hexString.substring(0, 1).equals("0")) {
            hexString = hexString.substring(1, 2);
        }
        this.block_ = Integer.valueOf(hexString).intValue() / 4;
        this.section_ = Integer.valueOf(hexString).intValue() % 4;
        int i4 = this.block_;
        if (i4 < 10 || Integer.toHexString(i4).length() < 2) {
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
        byte[] bArr7 = this.Password;
        return requestpw(new byte[]{-86, -86, -86, -106, 105, 0, 16, Byte.MIN_VALUE, 11, b, bArr7[0], bArr7[1], bArr7[2], bArr7[3], bArr7[4], bArr7[5], xor}, new byte[]{11, 0, -112});
    }

    private Boolean requestpw(byte[] bArr, byte[] bArr2) {
        UsbDevice usbDevice = this.tcard_reader;
        if (usbDevice == null || bArr2.length != 3) {
            return null;
        }
        UsbInterface usbInterface = usbDevice.getInterface(0);
        UsbEndpoint endpoint = usbInterface.getEndpoint(0);
        UsbEndpoint endpoint2 = usbInterface.getEndpoint(1);
        UsbDeviceConnection openDevice = this.tUsbManager.openDevice(this.tcard_reader);
        openDevice.claimInterface(usbInterface, true);
        openDevice.bulkTransfer(endpoint2, bArr, bArr.length, PathInterpolatorCompat.MAX_NUM_POINTS);
        byte[] bArr3 = new byte[1024];
        openDevice.bulkTransfer(endpoint, bArr3, 1024, PathInterpolatorCompat.MAX_NUM_POINTS);
        for (int i = 0; i < 30; i++) {
            Log.e("pwcheck", new StringBuilder().append((int) bArr3[i]).toString());
        }
        int i2 = 0;
        while (true) {
            byte[] bArr4 = TSAM_HEADER;
            if (i2 < bArr4.length) {
                if (bArr3[i2] != bArr4[i2]) {
                    openDevice.close();
                    return null;
                }
                i2++;
            } else {
                for (int i3 = 0; i3 < bArr2.length; i3++) {
                    if (bArr3[TSAM_HEADER.length + i3 + 2] != bArr2[i3]) {
                        openDevice.close();
                        return false;
                    }
                }
                openDevice.close();
                return true;
            }
        }
    }

    public TASectorInfo readData() throws TelpoException {
        String str;
        byte[] bArr;
        byte[] bArr2 = new byte[13];
        System.arraycopy(new byte[]{-86, -86, -86, -106, 105, 0, 6, Byte.MIN_VALUE, 13}, 0, bArr2, 0, 9);
        bArr2[9] = this.Block[0];
        bArr2[10] = 16;
        byte[] bArr3 = new byte[7];
        System.arraycopy(new byte[]{0, 6, Byte.MIN_VALUE, 13}, 0, bArr3, 0, 4);
        bArr3[4] = this.Block[0];
        bArr3[5] = 16;
        bArr2[12] = crc(bArr3);
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
        byte[] requestData = requestData(new byte[]{-86, -86, -86, -106, 105, 0, 6, Byte.MIN_VALUE, 13, bArr[0], hexStringToBytes("0" + Integer.toHexString(Integer.valueOf(this.section_).intValue()))[0], 16, xor}, new byte[]{13, 0, -112});
        if (requestData == null) {
            return null;
        }
        String decodetcarduid = decodetcarduid(requestData);
        TASectorInfo tASectorInfo = new TASectorInfo();
        tASectorInfo.setSectorData(decodetcarduid);
        return tASectorInfo;
    }

    public void sendAPDU(byte[] bArr, byte[] bArr2) {
        requestData(bArr, bArr2);
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x007c, code lost:
        if (r2 < r10.length) goto L13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:11:0x007e, code lost:
        r6.close();
        r9 = com.telpo.tps550.api.typea.UsbTypeA.TSAM_HEADER;
     */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x008d, code lost:
        return java.util.Arrays.copyOfRange(r5, r9.length + 5, r9.length + 21);
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x0098, code lost:
        if (r5[(com.telpo.tps550.api.typea.UsbTypeA.TSAM_HEADER.length + r2) + 2] == r10[r2]) goto L17;
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x009a, code lost:
        r6.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x009d, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x009e, code lost:
        r2 = r2 + 1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private byte[] requestData(byte[] r9, byte[] r10) {
        /*
            r8 = this;
            android.hardware.usb.UsbDevice r0 = r8.tcard_reader
            r1 = 0
            if (r0 == 0) goto Lae
            int r2 = r10.length
            r3 = 3
            if (r2 != r3) goto Lae
            r2 = 0
            android.hardware.usb.UsbInterface r0 = r0.getInterface(r2)
            android.hardware.usb.UsbEndpoint r3 = r0.getEndpoint(r2)
            r4 = 1
            android.hardware.usb.UsbEndpoint r5 = r0.getEndpoint(r4)
            android.hardware.usb.UsbManager r6 = r8.tUsbManager
            android.hardware.usb.UsbDevice r7 = r8.tcard_reader
            android.hardware.usb.UsbDeviceConnection r6 = r6.openDevice(r7)
            r6.claimInterface(r0, r4)
            int r0 = r9.length
            r7 = 3000(0xbb8, float:4.204E-42)
            r6.bulkTransfer(r5, r9, r0, r7)
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r5 = "out:"
            r0.<init>(r5)
            java.lang.String r9 = com.telpo.tps550.api.util.StringUtil.toHexString(r9)
            java.lang.StringBuilder r9 = r0.append(r9)
            java.lang.String r9 = r9.toString()
            java.lang.String r0 = "TAG"
            android.util.Log.d(r0, r9)
            r9 = 1024(0x400, float:1.435E-42)
            byte[] r5 = new byte[r9]
            int r9 = r6.bulkTransfer(r3, r5, r9, r7)
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            java.lang.String r7 = "in size:"
            r3.<init>(r7)
            java.lang.StringBuilder r3 = r3.append(r9)
            java.lang.String r3 = r3.toString()
            android.util.Log.d(r0, r3)
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            java.lang.String r7 = "in:"
            r3.<init>(r7)
            int r9 = r9 - r4
            byte[] r9 = java.util.Arrays.copyOfRange(r5, r2, r9)
            java.lang.String r9 = com.telpo.tps550.api.util.StringUtil.toHexString(r9)
            java.lang.StringBuilder r9 = r3.append(r9)
            java.lang.String r9 = r9.toString()
            android.util.Log.d(r0, r9)
            r9 = r2
        L76:
            byte[] r0 = com.telpo.tps550.api.typea.UsbTypeA.TSAM_HEADER
            int r3 = r0.length
            if (r9 < r3) goto La1
        L7b:
            int r9 = r10.length
            if (r2 < r9) goto L8e
            r6.close()
            byte[] r9 = com.telpo.tps550.api.typea.UsbTypeA.TSAM_HEADER
            int r10 = r9.length
            int r10 = r10 + 5
            int r9 = r9.length
            int r9 = r9 + 21
            byte[] r9 = java.util.Arrays.copyOfRange(r5, r10, r9)
            return r9
        L8e:
            byte[] r9 = com.telpo.tps550.api.typea.UsbTypeA.TSAM_HEADER
            int r9 = r9.length
            int r9 = r9 + r2
            int r9 = r9 + 2
            r9 = r5[r9]
            r0 = r10[r2]
            if (r9 == r0) goto L9e
            r6.close()
            return r1
        L9e:
            int r2 = r2 + 1
            goto L7b
        La1:
            r3 = r5[r9]
            r0 = r0[r9]
            if (r3 == r0) goto Lab
            r6.close()
            return r1
        Lab:
            int r9 = r9 + 1
            goto L76
        Lae:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.telpo.tps550.api.typea.UsbTypeA.requestData(byte[], byte[]):byte[]");
    }

    public void transmitdata(byte[] bArr) {
        this.Newwritedata = bArr;
    }

    public Boolean writeInData(String str) {
        byte[] bArr;
        byte[] bArr2;
        byte[] bArr3;
        String str2;
        byte[] hexStringToBytes = hexStringToBytes(str);
        byte[] bArr4 = new byte[29];
        byte b = 1;
        char c = 3;
        char c2 = 6;
        int i = 7;
        char c3 = '\n';
        System.arraycopy(new byte[]{-86, -86, -86, -106, 105, 0, 22, Byte.MIN_VALUE, 14, 1, 1, 16}, 0, bArr4, 0, 12);
        if (hexStringToBytes == null || hexStringToBytes == null) {
            return false;
        }
        int i2 = 0;
        while (i2 < hexStringToBytes.length) {
            bArr4[i2 + 12] = hexStringToBytes[i2];
            i2++;
            b = b;
            c3 = c3;
            c = c;
            c2 = c2;
            i = i;
        }
        if (hexStringToBytes.length < 16) {
            int length = 16 - hexStringToBytes.length;
            for (int i3 = 0; i3 < length; i3++) {
                bArr4[hexStringToBytes.length + 12 + i3] = 0;
            }
        }
        byte[] bArr5 = new byte[23];
        byte[] bArr6 = new byte[i];
        bArr6[b] = 22;
        bArr6[2] = Byte.MIN_VALUE;
        bArr6[c] = 14;
        bArr6[4] = b;
        bArr6[5] = b;
        bArr6[c2] = 16;
        System.arraycopy(bArr6, 0, bArr5, 0, i);
        int i4 = 0;
        while (i4 < hexStringToBytes.length) {
            bArr5[i4 + 7] = hexStringToBytes[i4];
            i4++;
            c = c;
            b = b;
            c2 = c2;
            i = i;
        }
        bArr4[28] = crc(bArr5);
        String hexString = StringUtil.toHexString(hexStringToBytes);
        if (Integer.toHexString(Integer.valueOf(this.block_).intValue()).length() < 2) {
            bArr = hexStringToBytes("0" + Integer.toHexString(Integer.valueOf(this.block_).intValue()));
        } else {
            bArr = hexStringToBytes(Integer.toHexString(Integer.valueOf(this.block_).intValue()));
        }
        byte[] hexStringToBytes2 = hexStringToBytes("0" + Integer.toHexString(Integer.valueOf(this.section_).intValue()));
        byte b2 = bArr[0];
        byte b3 = hexStringToBytes2[0];
        int length2 = hexString.length() / 2;
        int i5 = length2 + 6;
        if (Integer.toHexString(Integer.valueOf(i5).intValue()).length() < 2) {
            bArr2 = hexStringToBytes("0" + Integer.toHexString(Integer.valueOf(i5).intValue()));
        } else {
            bArr2 = hexStringToBytes(Integer.toHexString(Integer.valueOf(i5).intValue()));
        }
        if (Integer.toHexString(Integer.valueOf(length2).intValue()).length() < 2) {
            bArr3 = hexStringToBytes("0" + Integer.toHexString(Integer.valueOf(length2).intValue()));
        } else {
            bArr3 = hexStringToBytes(Integer.toHexString(Integer.valueOf(length2).intValue()));
        }
        int i6 = this.block_;
        if (i6 < 10 || Integer.toHexString(i6).length() < 2) {
            if (Integer.toHexString(Integer.valueOf(i5).intValue()).length() < 2) {
                str2 = "00" + StringUtil.toHexString(bArr2) + "800E0" + Integer.toHexString(this.block_).toUpperCase() + "0" + this.section_ + "0" + Integer.toHexString(Integer.valueOf(length2).intValue()) + hexString;
            } else {
                str2 = "00" + StringUtil.toHexString(bArr2) + "800E0" + Integer.toHexString(this.block_).toUpperCase() + "0" + this.section_ + Integer.toHexString(Integer.valueOf(length2).intValue()) + hexString;
            }
        } else if (Integer.toHexString(Integer.valueOf(i5).intValue()).length() < 2) {
            str2 = "00" + StringUtil.toHexString(bArr2) + "800E" + Integer.toHexString(this.block_).toUpperCase() + "0" + this.section_ + "0" + Integer.toHexString(Integer.valueOf(length2).intValue()) + hexString;
        } else {
            str2 = "00" + StringUtil.toHexString(bArr2) + "800E" + Integer.toHexString(this.block_).toUpperCase() + "0" + this.section_ + Integer.toHexString(Integer.valueOf(length2).intValue()) + hexString;
        }
        byte xor = getXor(hexStringToBytes(str2));
        int i7 = length2 + 13;
        byte[] bArr7 = new byte[i7];
        bArr7[0] = -86;
        bArr7[1] = -86;
        bArr7[2] = -86;
        bArr7[3] = -106;
        bArr7[4] = 105;
        bArr7[5] = 0;
        bArr7[6] = bArr2[0];
        bArr7[7] = Byte.MIN_VALUE;
        bArr7[8] = 14;
        bArr7[9] = b2;
        bArr7[10] = b3;
        bArr7[11] = bArr3[0];
        bArr7[i7 - 1] = xor;
        for (int i8 = 0; i8 < hexStringToBytes.length; i8++) {
            bArr7[i8 + 12] = hexStringToBytes[i8];
        }
        return requestwrite(bArr7, new byte[]{14, 0, -112});
    }

    private Boolean requestwrite(byte[] bArr, byte[] bArr2) {
        UsbDevice usbDevice = this.tcard_reader;
        if (usbDevice == null || bArr2.length != 3) {
            return null;
        }
        UsbInterface usbInterface = usbDevice.getInterface(0);
        UsbEndpoint endpoint = usbInterface.getEndpoint(0);
        UsbEndpoint endpoint2 = usbInterface.getEndpoint(1);
        UsbDeviceConnection openDevice = this.tUsbManager.openDevice(this.tcard_reader);
        openDevice.claimInterface(usbInterface, true);
        openDevice.bulkTransfer(endpoint2, bArr, bArr.length, PathInterpolatorCompat.MAX_NUM_POINTS);
        byte[] bArr3 = new byte[1024];
        openDevice.bulkTransfer(endpoint, bArr3, 1024, PathInterpolatorCompat.MAX_NUM_POINTS);
        int i = 0;
        while (true) {
            byte[] bArr4 = TSAM_HEADER;
            if (i < bArr4.length) {
                if (bArr3[i] != bArr4[i]) {
                    openDevice.close();
                    return null;
                }
                i++;
            } else {
                for (int i2 = 0; i2 < bArr2.length; i2++) {
                    if (bArr3[TSAM_HEADER.length + i2 + 2] != bArr2[i2]) {
                        openDevice.close();
                        return false;
                    }
                }
                openDevice.close();
                return true;
            }
        }
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
