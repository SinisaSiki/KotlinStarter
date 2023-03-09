package com.telpo.tps550.api.idcard;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Environment;
import android.util.Log;
import androidx.vectordrawable.graphics.drawable.PathInterpolatorCompat;
import com.telpo.tps550.api.TelpoException;
import com.telpo.tps550.api.util.StringUtil;
import com.zkteco.android.IDReader.IDPhotoHelper;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Pattern;

/* loaded from: classes.dex */
public class UsbIdCard {
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private static final String TAG = "UsbIdCard";
    private static int contentLength;
    private static int fplength;
    private static int imageDatalength;
    private Context mContext;
    private PendingIntent mPermissionIntent;
    private static final byte[] SAM_HEADER = {-86, -86, -86, -106, 105};
    static String[] nation_list = {"汉", "蒙古", "回", "藏", "维吾尔", "苗", "彝", "壮", "布依", "朝鲜", "满", "侗", "瑶", "白", "土家", "哈尼", "哈萨克", "傣", "黎", "傈僳", "佤", "畲", "高山", "拉祜", "水", "东乡", "纳西", "景颇", "柯尔克孜", "土", "达斡尔", "仫佬", "羌", "布朗", "撒拉", "毛南", "仡佬", "锡伯", "阿昌", "普米", "塔吉克", "怒", "乌孜别克", "俄罗斯", "鄂温克", "德昂", "保安", "裕固", "京", "塔塔尔", "独龙", "鄂伦春", "赫哲", "门巴", "珞巴", "基诺", "其他", "外国血统中国籍人士"};
    private static UsbDevice idcard_reader = null;
    private static UsbManager mUsbManager = null;
    private static UsbDevice updater = null;
    private byte[] resultCommand = null;
    private final int REQUEST = 0;
    private final int CHECK = 1;
    private final int DOWNLOAD_1 = 2;
    private final int DOWNLOAD_FINISH = 3;
    private final int NEW_PID = 649;
    private final int NEW_VID = IdCard.READER_VID_WINDOWS;
    private int checkCount = 0;
    private boolean finalRet = false;
    private long[] crc_16tab = {0, 4129, 8258, 12387, 16516, 20645, 24774, 28903, 33032, 37161, 41290, 45419, 49548, 53677, 57806, 61935, 4657, 528, 12915, 8786, 21173, 17044, 29431, 25302, 37689, 33560, 45947, 41818, 54205, 50076, 62463, 58334, 9314, 13379, 1056, 5121, 25830, 29895, 17572, 21637, 42346, 46411, 34088, 38153, 58862, 62927, 50604, 54669, 13907, 9842, 5649, 1584, 30423, 26358, 22165, 18100, 46939, 42874, 38681, 34616, 63455, 59390, 55197, 51132, 18628, 22757, 26758, 30887, 2112, 6241, 10242, 14371, 51660, 55789, 59790, 63919, 35144, 39273, 43274, 47403, 23285, 19156, 31415, 27286, 6769, 2640, 14899, 10770, 56317, 52188, 64447, 60318, 39801, 35672, 47931, 43802, 27814, 31879, 19684, 23749, 11298, 15363, 3168, 7233, 60846, 64911, 52716, 56781, 44330, 48395, 36200, 40265, 32407, 28342, 24277, 20212, 15891, 11826, 7761, 3696, 65439, 61374, 57309, 53244, 48923, 44858, 40793, 36728, 37256, 33193, 45514, 41451, 53516, 49453, 61774, 57711, 4224, 161, 12482, 8419, 20484, 16421, 28742, 24679, 33721, 37784, 41979, 46042, 49981, 54044, 58239, 62302, 689, 4752, 8947, 13010, 16949, 21012, 25207, 29270, 46570, 42443, 38312, 34185, 62830, 58703, 54572, 50445, 13538, 9411, 5280, 1153, 29798, 25671, 21540, 17413, 42971, 47098, 34713, 38840, 59231, 63358, 50973, 55100, 9939, 14066, 1681, 5808, 26199, 30326, 17941, 22068, 55628, 51565, 63758, 59695, 39368, 35305, 47498, 43435, 22596, 18533, 30726, 26663, 6336, 2273, 14466, 10403, 52093, 56156, 60223, 64286, 35833, 39896, 43963, 48026, 19061, 23124, 27191, 31254, 2801, 6864, 10931, 14994, 64814, 60687, 56684, 52557, 48554, 44427, 40424, 36297, 31782, 27655, 23652, 19525, 15522, 11395, 7392, 3265, 61215, 65342, 53085, 57212, 44955, 49082, 36825, 40952, 28183, 32310, 20053, 24180, 11923, 16050, 3793, 7920};

    public UsbIdCard(Context context) throws TelpoException {
        this.mPermissionIntent = null;
        this.mContext = context;
        this.mPermissionIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);
        UsbManager usbManager = (UsbManager) context.getSystemService("usb");
        mUsbManager = usbManager;
        for (UsbDevice usbDevice : usbManager.getDeviceList().values()) {
            int productId = usbDevice.getProductId();
            int vendorId = usbDevice.getVendorId();
            Log.d(TAG, "pid:" + productId + ";vid:" + vendorId);
            if ((productId == 50010 && vendorId == 1024) || ((productId == 22352 && vendorId == 1155) || (productId == 650 && vendorId == 10473))) {
                idcard_reader = usbDevice;
                if (mUsbManager.hasPermission(usbDevice)) {
                    break;
                }
                mUsbManager.requestPermission(usbDevice, this.mPermissionIntent);
            }
        }
        if (mUsbManager == null || idcard_reader == null) {
            throw new IdCardInitFailException("Failed to open usb device");
        }
    }

    public synchronized IdentityMsg checkIdCard() throws TelpoException {
        requestUSBDataBtn(new byte[]{-86, -86, -86, -106, 105, 0, 3, 32, 1, 34}, new byte[]{0, 0, -97});
        requestUSBDataBtn(new byte[]{-86, -86, -86, -106, 105, 0, 3, 32, 2, 33}, new byte[]{0, 0, -112});
        return decodeIdCardBaseInfo(requestUSBDataBtn(new byte[]{-86, -86, -86, -106, 105, 0, 3, 48, 16, 35}, new byte[]{0, 0, -112}));
    }

    public synchronized byte[] findCard() throws TelpoException {
        byte[] requestUSBDataBtn = requestUSBDataBtn(new byte[]{-86, -86, -86, -106, 105, 0, 3, 32, 1, 34}, new byte[]{0, 0, -97}, 1);
        if (requestUSBDataBtn == null) {
            return null;
        }
        return requestUSBDataBtn;
    }

    public synchronized byte[] selectCard() throws TelpoException {
        byte[] requestUSBDataBtn = requestUSBDataBtn(new byte[]{-86, -86, -86, -106, 105, 0, 3, 32, 2, 33}, new byte[]{0, 0, -112}, 1);
        if (requestUSBDataBtn == null) {
            return null;
        }
        return requestUSBDataBtn;
    }

    public synchronized byte[] readCard() throws TelpoException {
        byte[] requestUSBDataBtn = requestUSBDataBtn(new byte[]{-86, -86, -86, -106, 105, 0, 3, 48, 16, 35}, new byte[]{0, 0, -112});
        if (requestUSBDataBtn == null) {
            return null;
        }
        return requestUSBDataBtn;
    }

    public static synchronized byte[] getIdCardImage(IdentityMsg identityMsg) throws TelpoException {
        byte[] head_photo;
        synchronized (UsbIdCard.class) {
            head_photo = identityMsg.getHead_photo();
            if (head_photo == null) {
                throw new IdCardNotCheckException();
            }
        }
        return head_photo;
    }

    public static synchronized byte[] getFringerPrint(IdentityMsg identityMsg) throws TelpoException {
        byte[] copyOfRange;
        synchronized (UsbIdCard.class) {
            byte[] idCardImage = getIdCardImage(identityMsg);
            if (idCardImage == null) {
                throw new IdCardNotCheckException();
            }
            try {
                copyOfRange = Arrays.copyOfRange(idCardImage, imageDatalength, idCardImage.length);
                if (copyOfRange == null) {
                    throw new IdCardNotCheckException();
                }
            } catch (Exception unused) {
                throw new IdCardNotCheckException();
            }
        }
        return copyOfRange;
    }

    public static Bitmap decodeIdCardImage(byte[] bArr) throws TelpoException {
        if (bArr == null) {
            throw new ImageDecodeException();
        }
        byte[] bArr2 = new byte[38556];
        if (1 == IdCard.getimage(bArr, bArr2)) {
            return IDPhotoHelper.Bgr2Bitmap(bArr2);
        }
        throw new ImageDecodeException();
    }

    public synchronized String getSAM() throws TelpoException {
        byte[] requestUSBDataBtn = requestUSBDataBtn(new byte[]{-86, -86, -86, -106, 105, 0, 3, 18, -1, -18}, new byte[]{0, 0, -112});
        if (requestUSBDataBtn == null) {
            return null;
        }
        if (requestUSBDataBtn.length != 16) {
            return null;
        }
        return bytearray2Str(requestUSBDataBtn, 0, 2, 2) + bytearray2Str(requestUSBDataBtn, 2, 2, 2) + bytearray2Str(requestUSBDataBtn, 4, 4, 8) + bytearray2Str(requestUSBDataBtn, 8, 4, 10) + bytearray2Str(requestUSBDataBtn, 12, 4, 10);
    }

    public synchronized byte[] getPhyAddr() throws TelpoException {
        byte[] requestAddr = requestAddr(new byte[]{0, 54, 0, 0, 8});
        if (requestAddr == null) {
            return null;
        }
        return requestAddr;
    }

    public String getVersion() {
        byte b;
        byte[] requestVersion = requestVersion(new byte[]{102, 1, 2, 3, -1, 107, 22});
        Log.d(TAG, "version:" + StringUtil.toHexString(requestVersion));
        byte b2 = 0;
        if (requestVersion != null) {
            byte b3 = requestVersion[1];
            b = requestVersion[0];
            b2 = b3;
        } else {
            b = 0;
        }
        return String.valueOf((int) b2) + "." + ((int) b);
    }

    public byte[] requestVersion(byte[] bArr) {
        UsbDevice usbDevice = idcard_reader;
        if (usbDevice != null) {
            UsbInterface usbInterface = usbDevice.getInterface(0);
            UsbEndpoint endpoint = usbInterface.getEndpoint(0);
            UsbEndpoint endpoint2 = usbInterface.getEndpoint(1);
            UsbDeviceConnection openDevice = mUsbManager.openDevice(idcard_reader);
            if (openDevice == null) {
                return null;
            }
            openDevice.claimInterface(usbInterface, true);
            openDevice.bulkTransfer(endpoint2, bArr, bArr.length, PathInterpolatorCompat.MAX_NUM_POINTS);
            byte[] bArr2 = new byte[9];
            openDevice.bulkTransfer(endpoint, bArr2, 9, PathInterpolatorCompat.MAX_NUM_POINTS);
            return Arrays.copyOfRange(bArr2, 5, 7);
        }
        return null;
    }

    public byte[] requestAddr(byte[] bArr) {
        UsbDevice usbDevice = idcard_reader;
        if (usbDevice != null) {
            UsbInterface usbInterface = usbDevice.getInterface(0);
            UsbEndpoint endpoint = usbInterface.getEndpoint(0);
            UsbEndpoint endpoint2 = usbInterface.getEndpoint(1);
            UsbDeviceConnection openDevice = mUsbManager.openDevice(idcard_reader);
            if (openDevice == null) {
                return null;
            }
            openDevice.claimInterface(usbInterface, true);
            openDevice.bulkTransfer(endpoint2, bArr, bArr.length, PathInterpolatorCompat.MAX_NUM_POINTS);
            byte[] bArr2 = new byte[128];
            int bulkTransfer = openDevice.bulkTransfer(endpoint, bArr2, 128, PathInterpolatorCompat.MAX_NUM_POINTS);
            if (bulkTransfer != -1) {
                return Arrays.copyOfRange(bArr2, 0, bulkTransfer);
            }
        }
        return null;
    }

    /* JADX WARN: Code restructure failed: missing block: B:22:0x00aa, code lost:
        if (r4 < r18.length) goto L25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x00ac, code lost:
        r9.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x00b9, code lost:
        return java.util.Arrays.copyOfRange(r8, com.telpo.tps550.api.idcard.UsbIdCard.SAM_HEADER.length + 5, r0 - 1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x00c3, code lost:
        if (r8[(com.telpo.tps550.api.idcard.UsbIdCard.SAM_HEADER.length + 2) + r4] == r18[r4]) goto L29;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x00c5, code lost:
        r9.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x00c8, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x00c9, code lost:
        r4 = r4 + 1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public byte[] requestUSBDataBtn(byte[] r17, byte[] r18) {
        /*
            Method dump skipped, instructions count: 234
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.telpo.tps550.api.idcard.UsbIdCard.requestUSBDataBtn(byte[], byte[]):byte[]");
    }

    public byte[] requestUSBDataBtn(byte[] bArr, byte[] bArr2, int i) {
        UsbDeviceConnection usbDeviceConnection;
        try {
            UsbDevice usbDevice = idcard_reader;
            if (usbDevice != null && bArr2.length == 3) {
                UsbInterface usbInterface = usbDevice.getInterface(0);
                UsbEndpoint endpoint = usbInterface.getEndpoint(0);
                UsbEndpoint endpoint2 = usbInterface.getEndpoint(1);
                try {
                    usbDeviceConnection = mUsbManager.openDevice(idcard_reader);
                } catch (Exception e) {
                    e.printStackTrace();
                    usbDeviceConnection = null;
                }
                if (usbDeviceConnection == null) {
                    return null;
                }
                usbDeviceConnection.claimInterface(usbInterface, true);
                usbDeviceConnection.bulkTransfer(endpoint2, bArr, bArr.length, PathInterpolatorCompat.MAX_NUM_POINTS);
                byte[] bArr3 = new byte[5120];
                int bulkTransfer = usbDeviceConnection.bulkTransfer(endpoint, bArr3, 5120, PathInterpolatorCompat.MAX_NUM_POINTS);
                int i2 = 0;
                while (true) {
                    byte[] bArr4 = SAM_HEADER;
                    if (i2 < bArr4.length) {
                        if (bArr3[i2] != bArr4[i2]) {
                            usbDeviceConnection.close();
                            return null;
                        }
                        i2++;
                    } else {
                        for (int i3 = 0; i3 < bArr2.length; i3++) {
                            if (bArr3[SAM_HEADER.length + 2 + i3] != bArr2[i3]) {
                                usbDeviceConnection.close();
                                return null;
                            }
                        }
                        usbDeviceConnection.close();
                        return Arrays.copyOfRange(bArr3, 0, bulkTransfer);
                    }
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return null;
    }

    public static String toHexString1(byte b) {
        String hexString = Integer.toHexString(b & 255);
        return hexString.length() == 1 ? "0" + hexString : hexString;
    }

    private static String formatDate(String str) {
        return str.substring(0, 4) + "." + str.substring(4, 6) + "." + str.substring(6, 8);
    }

    private static String bytearray2Str(byte[] bArr, int i, int i2, int i3) {
        int i4;
        if (bArr.length < i + i2) {
            return "";
        }
        long j = 0;
        for (int i5 = 1; i5 <= i2; i5++) {
            j = (j * 256) + (bArr[i4 - i5] & 255);
        }
        return String.format("%0" + i3 + "d", Long.valueOf(j));
    }

    private static int countChinese(String str) {
        int i = 0;
        while (Pattern.compile("[\\u4e00-\\u9fa5]").matcher(str).find()) {
            i++;
        }
        return i;
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x00dd  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x01be  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x01c4  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x01e1  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x01f6  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x022b  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x018f A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public com.telpo.tps550.api.idcard.IdentityMsg decodeIdCardBaseInfo(byte[] r17) {
        /*
            Method dump skipped, instructions count: 561
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.telpo.tps550.api.idcard.UsbIdCard.decodeIdCardBaseInfo(byte[]):com.telpo.tps550.api.idcard.IdentityMsg");
    }

    public static boolean isNumeric(String str) {
        int length = str.length();
        do {
            length--;
            if (length < 0) {
                return true;
            }
        } while (Character.isDigit(str.charAt(length)));
        return false;
    }

    public boolean moduleUpdate() {
        if (connectUsb()) {
            if (getUpdate()) {
                Log.d(TAG, "update success");
                return true;
            }
            Log.d(TAG, "update failed");
            return false;
        }
        return false;
    }

    private static void replaceIndex(int i, String str, String str2) {
        String str3 = String.valueOf(str.substring(0, i)) + str2 + str.substring(i + 1);
    }

    private static boolean isFileExists(String str) {
        try {
            return new File(str).exists();
        } catch (Exception unused) {
            return false;
        }
    }

    private static boolean copyFile(String str, String str2) {
        boolean z = false;
        try {
            File file = new File(str);
            if (file.exists() && file.canRead()) {
                FileInputStream fileInputStream = new FileInputStream(file);
                File file2 = new File(str2);
                file2.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(file2);
                byte[] bArr = new byte[1024];
                while (true) {
                    int read = fileInputStream.read(bArr);
                    if (read > 0) {
                        fileOutputStream.write(bArr, 0, read);
                    } else {
                        fileOutputStream.close();
                        fileInputStream.close();
                        z = true;
                        return true;
                    }
                }
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return z;
        }
    }

    private boolean getUpdate() {
        requestOrCheck(0);
        sleep();
        while (this.checkCount < 5 && !downloadFile()) {
            sleep();
            requestOrCheck(1);
            this.checkCount++;
            sleep();
        }
        return this.finalRet;
    }

    private void sleep() {
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean connectUsb() {
        return connectUsb(-1, -1);
    }

    private boolean connectUsb(int i, int i2) {
        Log.d(TAG, "update-connectUSB");
        boolean z = false;
        this.mPermissionIntent = PendingIntent.getBroadcast(this.mContext, 0, new Intent(ACTION_USB_PERMISSION), 0);
        UsbManager usbManager = (UsbManager) this.mContext.getSystemService("usb");
        mUsbManager = usbManager;
        for (UsbDevice usbDevice : usbManager.getDeviceList().values()) {
            int productId = usbDevice.getProductId();
            int vendorId = usbDevice.getVendorId();
            if ((productId == 50010 && vendorId == 1024) || ((productId == 22352 && vendorId == 1155) || ((productId == 650 && vendorId == 10473) || (productId == i && vendorId == i2)))) {
                Log.d(TAG, "pid:" + productId + ";vid:" + vendorId);
                updater = usbDevice;
                if (mUsbManager.hasPermission(usbDevice)) {
                    return true;
                }
                mUsbManager.requestPermission(usbDevice, this.mPermissionIntent);
                z = true;
            }
        }
        return z;
    }

    private boolean requestUpdateDataBtn(byte[] bArr, int i) {
        UsbDevice usbDevice = updater;
        if (usbDevice != null) {
            UsbInterface usbInterface = usbDevice.getInterface(0);
            UsbEndpoint endpoint = usbInterface.getEndpoint(0);
            UsbEndpoint endpoint2 = usbInterface.getEndpoint(1);
            try {
                UsbDeviceConnection openDevice = mUsbManager.openDevice(updater);
                if (openDevice == null) {
                    return false;
                }
                openDevice.claimInterface(usbInterface, true);
                openDevice.bulkTransfer(endpoint2, bArr, bArr.length, PathInterpolatorCompat.MAX_NUM_POINTS);
                byte[] bArr2 = new byte[5120];
                int bulkTransfer = openDevice.bulkTransfer(endpoint, bArr2, 5120, PathInterpolatorCompat.MAX_NUM_POINTS);
                openDevice.close();
                this.resultCommand = null;
                if (bulkTransfer != -1) {
                    this.resultCommand = Arrays.copyOfRange(bArr2, 0, bulkTransfer);
                }
                if (i == 0) {
                    return true;
                }
                if (i == 1) {
                    if (bulkTransfer != -1 && this.resultCommand[9] == 1) {
                        return true;
                    }
                } else if (i == 3 && bulkTransfer != -1 && this.resultCommand[9] == 0) {
                    this.finalRet = true;
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private boolean requestOrCheck(int i) {
        String str;
        if (i == 0) {
            connectUsb();
        } else if (i == 1) {
            connectUsb(649, IdCard.READER_VID_WINDOWS);
        }
        byte[] bArr = null;
        try {
            bArr = getBytes4File(String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath()) + "/project.bin");
        } catch (IOException e) {
            e.printStackTrace();
        }
        String hexString = Integer.toHexString(bArr.length);
        if (hexString.length() == 4) {
            hexString = "0000" + hexString;
        } else if (hexString.length() == 6) {
            hexString = "00" + hexString;
        }
        int length = hexString.length();
        int i2 = length - 2;
        StringBuilder sb = new StringBuilder(String.valueOf(hexString.substring(i2, length)));
        int i3 = length - 4;
        int i4 = length - 6;
        String sb2 = sb.append(hexString.substring(i3, i2)).append(hexString.substring(i4, i3)).append(hexString.substring(0, i4)).toString();
        String hexString2 = Long.toHexString(getCRC(bArr, bArr.length));
        return requestUpdateDataBtn(hexStringToBytes("AAAAAA966900078020" + sb2 + (String.valueOf(hexString2.substring(hexString2.length() - 2, hexString2.length())) + hexString2.substring(hexString2.length() - 4, hexString2.length() - 2)) + StringUtil.toHexString(new byte[]{getXor(hexStringToBytes("00078020" + sb2 + str))})), i);
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x0046  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean downloadFile() {
        /*
            r6 = this;
            r0 = 649(0x289, float:9.1E-43)
            r1 = 10473(0x28e9, float:1.4676E-41)
            boolean r0 = r6.connectUsb(r0, r1)
            r1 = 0
            if (r0 == 0) goto L50
            r0 = 0
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch: java.io.IOException -> L3e
            java.io.File r3 = android.os.Environment.getExternalStorageDirectory()     // Catch: java.io.IOException -> L3e
            java.lang.String r3 = r3.getAbsolutePath()     // Catch: java.io.IOException -> L3e
            java.lang.String r3 = java.lang.String.valueOf(r3)     // Catch: java.io.IOException -> L3e
            r2.<init>(r3)     // Catch: java.io.IOException -> L3e
            java.lang.String r3 = "/project.bin"
            java.lang.StringBuilder r2 = r2.append(r3)     // Catch: java.io.IOException -> L3e
            java.lang.String r2 = r2.toString()     // Catch: java.io.IOException -> L3e
            byte[] r2 = getBytes4File(r2)     // Catch: java.io.IOException -> L3e
            r3 = 16385(0x4001, float:2.296E-41)
            byte[] r3 = java.util.Arrays.copyOfRange(r2, r1, r3)     // Catch: java.io.IOException -> L3b
            r4 = 16384(0x4000, float:2.2959E-41)
            int r5 = r2.length     // Catch: java.io.IOException -> L39
            byte[] r0 = java.util.Arrays.copyOfRange(r2, r4, r5)     // Catch: java.io.IOException -> L39
            goto L44
        L39:
            r4 = move-exception
            goto L41
        L3b:
            r4 = move-exception
            r3 = r0
            goto L41
        L3e:
            r4 = move-exception
            r2 = r0
            r3 = r2
        L41:
            r4.printStackTrace()
        L44:
            if (r2 == 0) goto L50
            r1 = 2
            r6.requestUpdateDataBtn(r3, r1)
            r1 = 3
            boolean r0 = r6.requestUpdateDataBtn(r0, r1)
            return r0
        L50:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.telpo.tps550.api.idcard.UsbIdCard.downloadFile():boolean");
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0073  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x0078  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x007d  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x0082  */
    /* JADX WARN: Removed duplicated region for block: B:51:0x0087  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x008e  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0093  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x0098  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x009d  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x00a2  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static byte[] getBytes4File(java.lang.String r9) throws java.io.IOException {
        /*
            r0 = 0
            java.io.FileInputStream r1 = new java.io.FileInputStream     // Catch: java.lang.Throwable -> L60 java.lang.Exception -> L68
            r1.<init>(r9)     // Catch: java.lang.Throwable -> L60 java.lang.Exception -> L68
            java.io.BufferedInputStream r9 = new java.io.BufferedInputStream     // Catch: java.lang.Throwable -> L59 java.lang.Exception -> L5c
            r9.<init>(r1)     // Catch: java.lang.Throwable -> L59 java.lang.Exception -> L5c
            java.io.DataInputStream r2 = new java.io.DataInputStream     // Catch: java.lang.Throwable -> L50 java.lang.Exception -> L56
            r2.<init>(r9)     // Catch: java.lang.Throwable -> L50 java.lang.Exception -> L56
            java.io.ByteArrayOutputStream r3 = new java.io.ByteArrayOutputStream     // Catch: java.lang.Throwable -> L48 java.lang.Exception -> L4d
            r3.<init>()     // Catch: java.lang.Throwable -> L48 java.lang.Exception -> L4d
            java.io.DataOutputStream r4 = new java.io.DataOutputStream     // Catch: java.lang.Throwable -> L3f java.lang.Exception -> L45
            r4.<init>(r3)     // Catch: java.lang.Throwable -> L3f java.lang.Exception -> L45
            r5 = 1024(0x400, float:1.435E-42)
            byte[] r5 = new byte[r5]     // Catch: java.lang.Exception -> L3d java.lang.Throwable -> L8b
        L1e:
            int r6 = r2.read(r5)     // Catch: java.lang.Exception -> L3d java.lang.Throwable -> L8b
            if (r6 >= 0) goto L38
            byte[] r0 = r3.toByteArray()     // Catch: java.lang.Exception -> L3d java.lang.Throwable -> L8b
            r1.close()
            r2.close()
            r9.close()
            r3.close()
            r4.close()
            return r0
        L38:
            r7 = 0
            r4.write(r5, r7, r6)     // Catch: java.lang.Exception -> L3d java.lang.Throwable -> L8b
            goto L1e
        L3d:
            r5 = move-exception
            goto L6e
        L3f:
            r4 = move-exception
            r8 = r4
            r4 = r0
            r0 = r8
            goto L8c
        L45:
            r5 = move-exception
            r4 = r0
            goto L6e
        L48:
            r3 = move-exception
            r4 = r0
            r0 = r3
            r3 = r4
            goto L8c
        L4d:
            r5 = move-exception
            r3 = r0
            goto L6d
        L50:
            r2 = move-exception
            r3 = r0
            r4 = r3
            r0 = r2
            r2 = r4
            goto L8c
        L56:
            r5 = move-exception
            r2 = r0
            goto L6c
        L59:
            r9 = move-exception
            r2 = r0
            goto L63
        L5c:
            r5 = move-exception
            r9 = r0
            r2 = r9
            goto L6c
        L60:
            r9 = move-exception
            r1 = r0
            r2 = r1
        L63:
            r3 = r2
            r4 = r3
            r0 = r9
            r9 = r4
            goto L8c
        L68:
            r5 = move-exception
            r9 = r0
            r1 = r9
            r2 = r1
        L6c:
            r3 = r2
        L6d:
            r4 = r3
        L6e:
            r5.printStackTrace()     // Catch: java.lang.Throwable -> L8b
            if (r1 == 0) goto L76
            r1.close()
        L76:
            if (r2 == 0) goto L7b
            r2.close()
        L7b:
            if (r9 == 0) goto L80
            r9.close()
        L80:
            if (r3 == 0) goto L85
            r3.close()
        L85:
            if (r4 == 0) goto L8a
            r4.close()
        L8a:
            return r0
        L8b:
            r0 = move-exception
        L8c:
            if (r1 == 0) goto L91
            r1.close()
        L91:
            if (r2 == 0) goto L96
            r2.close()
        L96:
            if (r9 == 0) goto L9b
            r9.close()
        L9b:
            if (r3 == 0) goto La0
            r3.close()
        La0:
            if (r4 == 0) goto La5
            r4.close()
        La5:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.telpo.tps550.api.idcard.UsbIdCard.getBytes4File(java.lang.String):byte[]");
    }

    private static byte getXor(byte[] bArr) {
        byte b = bArr[0];
        for (int i = 1; i < bArr.length; i++) {
            b = (byte) (b ^ bArr[i]);
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

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    private long getCRC(byte[] bArr, int i) {
        long j = 0;
        for (int i2 = 0; i2 < i; i2++) {
            j = (j << 8) ^ this.crc_16tab[(int) (((j >> 8) ^ bArr[i2]) & 255)];
        }
        return j;
    }
}
