package com.telpo.tps550.api.typea;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.util.Log;
import androidx.vectordrawable.graphics.drawable.PathInterpolatorCompat;
import com.telpo.tps550.api.util.StringUtil;

/* loaded from: classes.dex */
public class UsbTACard {
    private final byte[] TSAM_HEADER = {-86, -86, -86, -106, 105};
    private UsbManager tUsbManager;
    private UsbDevice tcard_reader;

    public UsbTACard(UsbDevice usbDevice, UsbManager usbManager) {
        this.tcard_reader = usbDevice;
        this.tUsbManager = usbManager;
    }

    public TAInfo checkTACard() {
        byte[] requestUid = requestUid(new byte[]{-86, -86, -86, -106, 105, 0, 4, Byte.MIN_VALUE, 5, 16, -111}, new byte[]{5, 0, -112});
        if (requestUid == null) {
            return null;
        }
        String decodetcarduid = decodetcarduid(requestUid);
        TAInfo tAInfo = new TAInfo();
        tAInfo.setNum(decodetcarduid);
        return tAInfo;
    }

    public TAInfo checkTACard(int i) {
        long currentTimeMillis = System.currentTimeMillis();
        byte[] bArr = {-86, -86, -86, -106, 105, 0, 4, Byte.MIN_VALUE, 5, 16, -111};
        byte[] bArr2 = null;
        for (long currentTimeMillis2 = System.currentTimeMillis(); bArr2 == null && currentTimeMillis2 - currentTimeMillis < i; currentTimeMillis2 = System.currentTimeMillis()) {
            bArr2 = requestUid(bArr, new byte[]{5, 0, -112});
            try {
                Thread.sleep(50L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (bArr2 == null) {
            return null;
        }
        String decodetcarduid = decodetcarduid(bArr2);
        TAInfo tAInfo = new TAInfo();
        tAInfo.setNum(decodetcarduid);
        return tAInfo;
    }

    /* JADX WARN: Code restructure failed: missing block: B:17:0x0043, code lost:
        if (r2 < r11.length) goto L20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0045, code lost:
        r6.close();
        r10 = r9.TSAM_HEADER;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0054, code lost:
        return java.util.Arrays.copyOfRange(r0, r10.length + 5, r10.length + 9);
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x005f, code lost:
        if (r0[(r9.TSAM_HEADER.length + r2) + 2] == r11[r2]) goto L24;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0061, code lost:
        r6.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0064, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0065, code lost:
        r2 = r2 + 1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public byte[] requestUid(byte[] r10, byte[] r11) {
        /*
            r9 = this;
            android.hardware.usb.UsbDevice r0 = r9.tcard_reader
            r1 = 0
            if (r0 == 0) goto L75
            int r2 = r11.length
            r3 = 3
            if (r2 != r3) goto L75
            r2 = 0
            android.hardware.usb.UsbInterface r0 = r0.getInterface(r2)
            android.hardware.usb.UsbEndpoint r3 = r0.getEndpoint(r2)
            r4 = 1
            android.hardware.usb.UsbEndpoint r5 = r0.getEndpoint(r4)
            android.hardware.usb.UsbManager r6 = r9.tUsbManager
            android.hardware.usb.UsbDevice r7 = r9.tcard_reader
            android.hardware.usb.UsbDeviceConnection r6 = r6.openDevice(r7)
            if (r6 != 0) goto L22
            return r1
        L22:
            r6.claimInterface(r0, r4)
            int r0 = r10.length
            r4 = 3000(0xbb8, float:4.204E-42)
            r6.bulkTransfer(r5, r10, r0, r4)
            r7 = 500(0x1f4, double:2.47E-321)
            java.lang.Thread.sleep(r7)     // Catch: java.lang.InterruptedException -> L31
            goto L35
        L31:
            r10 = move-exception
            r10.printStackTrace()
        L35:
            r10 = 1024(0x400, float:1.435E-42)
            byte[] r0 = new byte[r10]
            r6.bulkTransfer(r3, r0, r10, r4)
            r10 = r2
        L3d:
            byte[] r3 = r9.TSAM_HEADER
            int r4 = r3.length
            if (r10 < r4) goto L68
        L42:
            int r10 = r11.length
            if (r2 < r10) goto L55
            r6.close()
            byte[] r10 = r9.TSAM_HEADER
            int r11 = r10.length
            int r11 = r11 + 5
            int r10 = r10.length
            int r10 = r10 + 9
            byte[] r10 = java.util.Arrays.copyOfRange(r0, r11, r10)
            return r10
        L55:
            byte[] r10 = r9.TSAM_HEADER
            int r10 = r10.length
            int r10 = r10 + r2
            int r10 = r10 + 2
            r10 = r0[r10]
            r3 = r11[r2]
            if (r10 == r3) goto L65
            r6.close()
            return r1
        L65:
            int r2 = r2 + 1
            goto L42
        L68:
            r4 = r0[r10]
            r3 = r3[r10]
            if (r4 == r3) goto L72
            r6.close()
            return r1
        L72:
            int r10 = r10 + 1
            goto L3d
        L75:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.telpo.tps550.api.typea.UsbTACard.requestUid(byte[], byte[]):byte[]");
    }

    public String decodetcarduid(byte[] bArr) {
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

    public Boolean checkPW(byte[] bArr) {
        if (bArr.length > 7) {
            return false;
        }
        Boolean.valueOf(false);
        byte[] bArr2 = new byte[17];
        System.arraycopy(new byte[]{-86, -86, -86, -106, 105, 0, 16, Byte.MIN_VALUE, 11, 1}, 0, bArr2, 0, 10);
        for (int i = 0; i < bArr.length; i++) {
            bArr2[i + 10] = bArr[i];
        }
        bArr2[16] = -102;
        Log.d("idcard demo", "cmd_pwcheck UsbTACard: " + StringUtil.toHexString(bArr2));
        return requestpw(bArr2, new byte[]{11, 0, -112});
    }

    public Boolean requestpw(byte[] bArr, byte[] bArr2) {
        UsbDevice usbDevice = this.tcard_reader;
        if (usbDevice == null || bArr2.length != 3) {
            return null;
        }
        UsbInterface usbInterface = usbDevice.getInterface(0);
        UsbEndpoint endpoint = usbInterface.getEndpoint(0);
        UsbEndpoint endpoint2 = usbInterface.getEndpoint(1);
        UsbDeviceConnection openDevice = this.tUsbManager.openDevice(this.tcard_reader);
        if (openDevice == null) {
            return null;
        }
        openDevice.claimInterface(usbInterface, true);
        openDevice.bulkTransfer(endpoint2, bArr, bArr.length, PathInterpolatorCompat.MAX_NUM_POINTS);
        try {
            Thread.sleep(500L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        byte[] bArr3 = new byte[1024];
        openDevice.bulkTransfer(endpoint, bArr3, 1024, PathInterpolatorCompat.MAX_NUM_POINTS);
        int i = 0;
        while (true) {
            byte[] bArr4 = this.TSAM_HEADER;
            if (i < bArr4.length) {
                if (bArr3[i] != bArr4[i]) {
                    openDevice.close();
                    return null;
                }
                i++;
            } else {
                for (int i2 = 0; i2 < bArr2.length; i2++) {
                    if (bArr3[this.TSAM_HEADER.length + i2 + 2] != bArr2[i2]) {
                        openDevice.close();
                        return false;
                    }
                }
                openDevice.close();
                return true;
            }
        }
    }

    public TASectorInfo readData() {
        byte[] bArr = {-86, -86, -86, -106, 105, 0, 6, Byte.MIN_VALUE, 13, 1, 1, 16, -101};
        Log.d("idcard demo", "readData UsbTACard: " + StringUtil.toHexString(bArr));
        byte[] requestData = requestData(bArr, new byte[]{13, 0, -112});
        if (requestData == null) {
            Log.d("idcard demo", "readData data is null");
            return null;
        }
        String decodetcarduid = decodetcarduid(requestData);
        TASectorInfo tASectorInfo = new TASectorInfo();
        tASectorInfo.setSectorData(decodetcarduid);
        return tASectorInfo;
    }

    /* JADX WARN: Code restructure failed: missing block: B:17:0x0061, code lost:
        if (r2 < r12.length) goto L20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0063, code lost:
        r6.close();
        r11 = r10.TSAM_HEADER;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0072, code lost:
        return java.util.Arrays.copyOfRange(r0, r11.length + 5, r11.length + 21);
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x007d, code lost:
        if (r0[(r10.TSAM_HEADER.length + r2) + 2] == r12[r2]) goto L24;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x007f, code lost:
        r6.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0082, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0083, code lost:
        r2 = r2 + 1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public byte[] requestData(byte[] r11, byte[] r12) {
        /*
            r10 = this;
            android.hardware.usb.UsbDevice r0 = r10.tcard_reader
            r1 = 0
            if (r0 == 0) goto L93
            int r2 = r12.length
            r3 = 3
            if (r2 != r3) goto L93
            r2 = 0
            android.hardware.usb.UsbInterface r0 = r0.getInterface(r2)
            android.hardware.usb.UsbEndpoint r3 = r0.getEndpoint(r2)
            r4 = 1
            android.hardware.usb.UsbEndpoint r5 = r0.getEndpoint(r4)
            android.hardware.usb.UsbManager r6 = r10.tUsbManager
            android.hardware.usb.UsbDevice r7 = r10.tcard_reader
            android.hardware.usb.UsbDeviceConnection r6 = r6.openDevice(r7)
            if (r6 != 0) goto L22
            return r1
        L22:
            r6.claimInterface(r0, r4)
            int r0 = r11.length
            r7 = 3000(0xbb8, float:4.204E-42)
            r6.bulkTransfer(r5, r11, r0, r7)
            r8 = 500(0x1f4, double:2.47E-321)
            java.lang.Thread.sleep(r8)     // Catch: java.lang.InterruptedException -> L31
            goto L35
        L31:
            r11 = move-exception
            r11.printStackTrace()
        L35:
            r11 = 1024(0x400, float:1.435E-42)
            byte[] r0 = new byte[r11]
            int r11 = r6.bulkTransfer(r3, r0, r11, r7)
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            java.lang.String r5 = "requestData is: "
            r3.<init>(r5)
            int r11 = r11 - r4
            byte[] r11 = java.util.Arrays.copyOfRange(r0, r2, r11)
            java.lang.String r11 = com.telpo.tps550.api.util.StringUtil.toHexString(r11)
            java.lang.StringBuilder r11 = r3.append(r11)
            java.lang.String r11 = r11.toString()
            java.lang.String r3 = "idcard demo"
            android.util.Log.d(r3, r11)
            r11 = r2
        L5b:
            byte[] r3 = r10.TSAM_HEADER
            int r4 = r3.length
            if (r11 < r4) goto L86
        L60:
            int r11 = r12.length
            if (r2 < r11) goto L73
            r6.close()
            byte[] r11 = r10.TSAM_HEADER
            int r12 = r11.length
            int r12 = r12 + 5
            int r11 = r11.length
            int r11 = r11 + 21
            byte[] r11 = java.util.Arrays.copyOfRange(r0, r12, r11)
            return r11
        L73:
            byte[] r11 = r10.TSAM_HEADER
            int r11 = r11.length
            int r11 = r11 + r2
            int r11 = r11 + 2
            r11 = r0[r11]
            r3 = r12[r2]
            if (r11 == r3) goto L83
            r6.close()
            return r1
        L83:
            int r2 = r2 + 1
            goto L60
        L86:
            r4 = r0[r11]
            r3 = r3[r11]
            if (r4 == r3) goto L90
            r6.close()
            return r1
        L90:
            int r11 = r11 + 1
            goto L5b
        L93:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.telpo.tps550.api.typea.UsbTACard.requestData(byte[], byte[]):byte[]");
    }

    public Boolean writeInData(String str) {
        byte[] hexStringToBytes = hexStringToBytes(str);
        byte[] bArr = new byte[29];
        System.arraycopy(new byte[]{-86, -86, -86, -106, 105, 0, 22, Byte.MIN_VALUE, 14, 1, 1, 16}, 0, bArr, 0, 12);
        if (hexStringToBytes == null || hexStringToBytes == null) {
            return false;
        }
        for (int i = 0; i < hexStringToBytes.length; i++) {
            bArr[i + 12] = hexStringToBytes[i];
        }
        if (hexStringToBytes.length < 16) {
            int length = 16 - hexStringToBytes.length;
            for (int i2 = 0; i2 < length; i2++) {
                bArr[hexStringToBytes.length + 12 + i2] = 0;
            }
        }
        byte[] bArr2 = new byte[23];
        System.arraycopy(new byte[]{0, 22, Byte.MIN_VALUE, 14, 1, 1, 16}, 0, bArr2, 0, 7);
        for (int i3 = 0; i3 < hexStringToBytes.length; i3++) {
            bArr2[i3 + 7] = hexStringToBytes[i3];
        }
        bArr[28] = crc(bArr2);
        Log.d("idcard demo", "cmd write data is: " + StringUtil.toHexString(bArr));
        return requestwrite(bArr, new byte[]{14, 0, -112});
    }

    private byte[] hexStringToBytes(String str) {
        if (str == null || str.equals("")) {
            return null;
        }
        String upperCase = str.toUpperCase();
        int length = upperCase.length() / 2;
        char[] charArray = upperCase.toCharArray();
        byte[] bArr = new byte[length];
        for (int i = 0; i < length; i++) {
            int i2 = i * 2;
            bArr[i] = (byte) (charToByte(charArray[i2 + 1]) | (charToByte(charArray[i2]) << 4));
        }
        return bArr;
    }

    private byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    private byte crc(byte[] bArr) {
        byte b = 0;
        for (byte b2 : bArr) {
            b = (byte) (b ^ b2);
        }
        return b;
    }

    public Boolean requestwrite(byte[] bArr, byte[] bArr2) {
        UsbDevice usbDevice = this.tcard_reader;
        if (usbDevice == null || bArr2.length != 3) {
            return null;
        }
        UsbInterface usbInterface = usbDevice.getInterface(0);
        UsbEndpoint endpoint = usbInterface.getEndpoint(0);
        UsbEndpoint endpoint2 = usbInterface.getEndpoint(1);
        UsbDeviceConnection openDevice = this.tUsbManager.openDevice(this.tcard_reader);
        if (openDevice == null) {
            return null;
        }
        openDevice.claimInterface(usbInterface, true);
        openDevice.bulkTransfer(endpoint2, bArr, bArr.length, PathInterpolatorCompat.MAX_NUM_POINTS);
        try {
            Thread.sleep(500L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        byte[] bArr3 = new byte[1024];
        openDevice.bulkTransfer(endpoint, bArr3, 1024, PathInterpolatorCompat.MAX_NUM_POINTS);
        int i = 0;
        while (true) {
            byte[] bArr4 = this.TSAM_HEADER;
            if (i < bArr4.length) {
                if (bArr3[i] != bArr4[i]) {
                    openDevice.close();
                    return null;
                }
                i++;
            } else {
                for (int i2 = 0; i2 < bArr2.length; i2++) {
                    if (bArr3[this.TSAM_HEADER.length + i2 + 2] != bArr2[i2]) {
                        openDevice.close();
                        return false;
                    }
                }
                openDevice.close();
                return true;
            }
        }
    }
}
