package com.telpo.tps550.api.idcard;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Environment;
import android.util.Log;
import com.telpo.tps550.api.fingerprint.FingerPrint;
import com.telpo.tps550.api.serial.Serial;
import com.telpo.tps550.api.util.ReaderUtils;
import com.telpo.tps550.api.util.StringUtil;
import com.telpo.tps550.api.util.SystemUtil;
import com.zkteco.android.IDReader.IDPhotoHelper;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Iterator;
import rs.teron.launcher.android.Configurator;

/* loaded from: classes.dex */
public class T2OReader {
    private static final int IS_SENDING_DATA = 9;
    public static final int KEYA = 0;
    public static final int KEYB = 1;
    private static final int SWITCH_TO_UPDATE = 8;
    private static final int TYPE_APDU = 11;
    private static final int TYPE_CHECK_VERSION = 10;
    private static final int TYPE_FIND_ID = 1;
    private static final int TYPE_GETPHYADDR_ID = 9;
    private static final int TYPE_GETSAM_ID = 8;
    private static final int TYPE_READDATA_TYPEA = 6;
    private static final int TYPE_READUID_TYPEA = 4;
    private static final int TYPE_READ_ID = 3;
    private static final int TYPE_SELECT_ID = 2;
    private static final int TYPE_VERIFY_TYPEA = 5;
    private static final int TYPE_WRITEDATA_TYPEA = 7;
    private int block_;
    private T2OReaderCallBack callBack;
    private Context mContext;
    byte[] project_bin;
    byte psw_check_block;
    byte psw_check_section;
    private int section_;
    private static final byte[] TSAM_HEADER = {-86, -86, -86, -106, 105};
    private static final byte[] FIND_ID_CMD = {-86, -86, -86, -106, 105, 0, 3, 32, 1, 34};
    private static final byte[] SELECT_ID_CMD = {-86, -86, -86, -106, 105, 0, 3, 32, 2, 33};
    private static final byte[] READ_ID_CMD = {-86, -86, -86, -106, 105, 0, 3, 48, 16, 35};
    private static final byte[] READ_TYPEA_CMD = {-86, -86, -86, -106, 105, 0, 4, Byte.MIN_VALUE, 5, 16, -111};
    private static final byte[] GET_ID_SAM = {-86, -86, -86, -106, 105, 0, 3, 18, -1, -18};
    private static final byte[] CHECK_VERSION = {102, 1, 2, 3, -1, 107, 22};
    private static final byte[] GET_ID_PHYADDR = {0, 54, 0, 0, 8};
    private static final byte[] VERIFYPASSWORD1 = {-86, -86, -86, -106, 105, 0, 10, 5, 0, -112, -80, 13, 56, 30, 0, 0, 4, -86, -86, -86, -106, 105, 0, 3, 11, 0, -112, -104};
    private static final byte[] VERIFYPASSWORD2 = {-86, -86, -86, -106, 105, 0, 3, 11, 0, -112, -104};
    private static final byte[] INTOUPDATE = {-86, -86, -86, -106, 105, 0, 5, Byte.MIN_VALUE, 32, 0, 0, -91};
    private static final byte[] PACKAGESENDSUCCESS = {-86, -86, -86, -106, 105, 0, 5, Byte.MIN_VALUE, 32, -1, 0, 90};
    private InputStream mInputStream = null;
    private OutputStream mOutputStream = null;
    private Serial serial = null;
    private ReadThread mReadThread = null;
    private boolean isUpdatingModule = false;
    private boolean switchUpdateSuccess = false;
    private boolean receivePackageSuccess = false;
    private int doingType = -1;
    private UsbDevice usbReader = null;
    private UsbManager mUsbManager = null;
    private boolean findSuccess = false;
    private boolean selectSuccess = false;
    private boolean readComplete = false;
    private boolean readDataWrong = false;
    private byte[] readData = null;
    private UsbDevice updater = null;
    private final int REQUEST = 0;
    private final int CHECK = 1;
    private final int DOWNLOAD_1 = 2;
    private final int DOWNLOAD_FINISH = 3;
    private final int NEW_PID = 649;
    private final int NEW_VID = IdCard.READER_VID_WINDOWS;
    private int checkCount = 0;
    private boolean finalRet = false;
    private boolean serialUpdateSuccess = false;
    int sendingCount = 1;
    private long[] crc_16tab = {0, 4129, 8258, 12387, 16516, 20645, 24774, 28903, 33032, 37161, 41290, 45419, 49548, 53677, 57806, 61935, 4657, 528, 12915, 8786, 21173, 17044, 29431, 25302, 37689, 33560, 45947, 41818, 54205, 50076, 62463, 58334, 9314, 13379, 1056, 5121, 25830, 29895, 17572, 21637, 42346, 46411, 34088, 38153, 58862, 62927, 50604, 54669, 13907, 9842, 5649, 1584, 30423, 26358, 22165, 18100, 46939, 42874, 38681, 34616, 63455, 59390, 55197, 51132, 18628, 22757, 26758, 30887, 2112, 6241, 10242, 14371, 51660, 55789, 59790, 63919, 35144, 39273, 43274, 47403, 23285, 19156, 31415, 27286, 6769, 2640, 14899, 10770, 56317, 52188, 64447, 60318, 39801, 35672, 47931, 43802, 27814, 31879, 19684, 23749, 11298, 15363, 3168, 7233, 60846, 64911, 52716, 56781, 44330, 48395, 36200, 40265, 32407, 28342, 24277, 20212, 15891, 11826, 7761, 3696, 65439, 61374, 57309, 53244, 48923, 44858, 40793, 36728, 37256, 33193, 45514, 41451, 53516, 49453, 61774, 57711, 4224, 161, 12482, 8419, 20484, 16421, 28742, 24679, 33721, 37784, 41979, 46042, 49981, 54044, 58239, 62302, 689, 4752, 8947, 13010, 16949, 21012, 25207, 29270, 46570, 42443, 38312, 34185, 62830, 58703, 54572, 50445, 13538, 9411, 5280, 1153, 29798, 25671, 21540, 17413, 42971, 47098, 34713, 38840, 59231, 63358, 50973, 55100, 9939, 14066, 1681, 5808, 26199, 30326, 17941, 22068, 55628, 51565, 63758, 59695, 39368, 35305, 47498, 43435, 22596, 18533, 30726, 26663, 6336, 2273, 14466, 10403, 52093, 56156, 60223, 64286, 35833, 39896, 43963, 48026, 19061, 23124, 27191, 31254, 2801, 6864, 10931, 14994, 64814, 60687, 56684, 52557, 48554, 44427, 40424, 36297, 31782, 27655, 23652, 19525, 15522, 11395, 7392, 3265, 61215, 65342, 53085, 57212, 44955, 49082, 36825, 40952, 28183, 32310, 20053, 24180, 11923, 16050, 3793, 7920};

    public boolean isSerialIDReader() {
        if (this.usbReader != null) {
            return false;
        }
        this.readData = null;
        sendCommand(FIND_ID_CMD, 1);
        long currentTimeMillis = System.currentTimeMillis();
        while (System.currentTimeMillis() - currentTimeMillis < 50) {
            threadSleep(10);
        }
        byte[] bArr = this.readData;
        return bArr != null && bArr.length >= 5;
    }

    public boolean isSerialNFCReader() {
        if (this.usbReader != null) {
            return false;
        }
        this.readData = null;
        sendCommand(READ_TYPEA_CMD, 4);
        long currentTimeMillis = System.currentTimeMillis();
        while (System.currentTimeMillis() - currentTimeMillis < 50) {
            threadSleep(10);
        }
        byte[] bArr = this.readData;
        return bArr != null && bArr.length >= 5;
    }

    public boolean isUSBReader(Context context) {
        String internalModel = SystemUtil.getInternalModel();
        int deviceType = SystemUtil.getDeviceType();
        if ("TPS450P".equals(internalModel) || "TPS360C".equals(internalModel) || deviceType == StringUtil.DeviceModelEnum.TPS900.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS900MB.ordinal()) {
            if ("TPS450P".equals(internalModel)) {
                FingerPrint.fingerprintIccPower450P(1);
            } else if ("TPS360C".equals(internalModel) || deviceType == StringUtil.DeviceModelEnum.TPS900.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS900MB.ordinal()) {
                FingerPrint.idcardPower(1);
            }
            long currentTimeMillis = System.currentTimeMillis();
            while (!hasUSBreaderNoPower(context) && System.currentTimeMillis() - currentTimeMillis < 10000) {
                threadSleep(50);
            }
            setLog("isUSBReader 450P/360C/900 load usb time[" + (System.currentTimeMillis() - currentTimeMillis) + "]");
        }
        try {
            this.mContext = context;
            for (UsbDevice usbDevice : ((UsbManager) context.getSystemService("usb")).getDeviceList().values()) {
                int productId = usbDevice.getProductId();
                int vendorId = usbDevice.getVendorId();
                setLog("isUSBReader(context) >>> pid:" + productId + ";vid:" + vendorId);
                if (productId == 50010 && vendorId == 1024) {
                    return true;
                }
                if (productId == 22352 && vendorId == 1155 && !usbDevice.getProductName().contains("SCANNER")) {
                    return true;
                }
                if (productId == 650 && vendorId == 10473) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean hasUSBreaderNoPower(Context context) {
        try {
            this.mContext = context;
            for (UsbDevice usbDevice : ((UsbManager) context.getSystemService("usb")).getDeviceList().values()) {
                int productId = usbDevice.getProductId();
                int vendorId = usbDevice.getVendorId();
                setLog("pid[" + productId + "] vid[" + vendorId + "]");
                if ((productId == 50010 && vendorId == 1024) || ((productId == 22352 && vendorId == 1155 && !usbDevice.getProductName().contains("SCANNER")) || (productId == 650 && vendorId == 10473))) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean openReader() {
        return openReader(selectSerial());
    }

    public boolean openReader(String str) {
        try {
            if (this.serial != null) {
                return false;
            }
            Serial serial = new Serial(str, 115200, 0);
            this.serial = serial;
            if (this.mOutputStream == null) {
                this.mOutputStream = serial.getOutputStream();
            }
            if (this.mInputStream == null) {
                this.mInputStream = this.serial.getInputStream();
            }
            if (this.mReadThread == null) {
                ReadThread readThread = new ReadThread(this, null);
                this.mReadThread = readThread;
                readThread.start();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void sleepThread(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean openReader(Context context) {
        String internalModel = SystemUtil.getInternalModel();
        int deviceType = SystemUtil.getDeviceType();
        boolean z = true;
        if ("TPS450P".equals(internalModel) || "TPS360C".equals(internalModel) || deviceType == StringUtil.DeviceModelEnum.TPS900.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS900MB.ordinal()) {
            if ("TPS450P".equals(internalModel)) {
                FingerPrint.fingerprintIccPower450P(1);
            } else if ("TPS360C".equals(internalModel) || deviceType == StringUtil.DeviceModelEnum.TPS900.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS900MB.ordinal()) {
                FingerPrint.idcardPower(1);
            }
            long currentTimeMillis = System.currentTimeMillis();
            while (!hasUSBreaderNoPower(context) && System.currentTimeMillis() - currentTimeMillis < 10000) {
                threadSleep(50);
            }
            setLog("isUSBReader 450P/360C/900 load usb time[" + (System.currentTimeMillis() - currentTimeMillis) + "]");
        }
        try {
            this.mContext = context;
            if (this.usbReader == null) {
                PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, new Intent("com.android.example.USB_PERMISSION"), 0);
                UsbManager usbManager = (UsbManager) this.mContext.getSystemService("usb");
                this.mUsbManager = usbManager;
                Iterator<UsbDevice> it = usbManager.getDeviceList().values().iterator();
                boolean z2 = false;
                while (true) {
                    if (!it.hasNext()) {
                        z = z2;
                        break;
                    }
                    UsbDevice next = it.next();
                    int productId = next.getProductId();
                    int vendorId = next.getVendorId();
                    setLog("openReader(context) >>> usb device pid:" + productId + "/vid:" + vendorId);
                    if ((productId == 50010 && vendorId == 1024) || ((productId == 22352 && vendorId == 1155 && !next.getProductName().contains("SCANNER")) || (productId == 650 && vendorId == 10473))) {
                        this.usbReader = next;
                        if (this.mUsbManager.hasPermission(next)) {
                            break;
                        }
                        this.mUsbManager.requestPermission(next, broadcast);
                        z2 = true;
                    }
                }
            } else {
                z = false;
            }
            setLog("isUsbReader[" + z + "]");
            return z;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean resetModule() {
        String internalModel = SystemUtil.getInternalModel();
        if ("TPS708".equals(internalModel)) {
            if (IdCard.resetModule(0) != 0 || !setProperty("persist.start.value", Configurator.SCREEN_TIMEOUT_10_MIN) || !setProperty("telpo.start.telposervice", "1") || IdCard.resetModule(1) != 0) {
                return false;
            }
        } else if (!"TPS711".equals(internalModel) || !FingerPrint.idcardPower(0)) {
            return false;
        } else {
            threadSleep(100);
            if (!FingerPrint.idcardPower(1)) {
                return false;
            }
        }
        return true;
    }

    private boolean setProperty(String str, String str2) {
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            cls.getMethod("set", String.class, String.class).invoke(cls, str, str2);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void closeReader() {
        try {
            String internalModel = SystemUtil.getInternalModel();
            int deviceType = SystemUtil.getDeviceType();
            if ("TPS450P".equals(internalModel)) {
                FingerPrint.fingerprintIccPower450P(0);
            } else if ("TPS360C".equals(internalModel) || deviceType == StringUtil.DeviceModelEnum.TPS900.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS900MB.ordinal()) {
                FingerPrint.idcardPower(0);
            }
            if (this.usbReader == null && this.mUsbManager == null) {
                ReadThread readThread = this.mReadThread;
                if (readThread != null) {
                    readThread.interrupt();
                    this.mReadThread = null;
                }
                OutputStream outputStream = this.mOutputStream;
                if (outputStream != null) {
                    outputStream.close();
                    this.mOutputStream = null;
                }
                InputStream inputStream = this.mInputStream;
                if (inputStream != null) {
                    inputStream.close();
                    this.mInputStream = null;
                }
                Serial serial = this.serial;
                if (serial == null) {
                    return;
                }
                serial.close();
                this.serial = null;
                return;
            }
            this.usbReader = null;
            this.mUsbManager = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean findIDCard() {
        this.findSuccess = false;
        this.readData = null;
        if (this.usbReader != null) {
            byte[] sendCommandUSB = sendCommandUSB(FIND_ID_CMD, 1);
            this.readData = sendCommandUSB;
            if (sendCommandUSB != null && "AAAAAA9669000800009F0000000097".equals(StringUtil.toHexString(sendCommandUSB))) {
                this.findSuccess = true;
            }
        } else {
            sendCommand(FIND_ID_CMD, 1);
            long currentTimeMillis = System.currentTimeMillis();
            while (System.currentTimeMillis() - currentTimeMillis < 150) {
                threadSleep(10);
            }
        }
        return this.findSuccess;
    }

    public boolean selectIDCard() {
        this.selectSuccess = false;
        this.readData = null;
        if (this.usbReader != null) {
            byte[] sendCommandUSB = sendCommandUSB(SELECT_ID_CMD, 2);
            this.readData = sendCommandUSB;
            if (sendCommandUSB != null && "AAAAAA9669000C00009000000000000000009C".equals(StringUtil.toHexString(sendCommandUSB))) {
                this.selectSuccess = true;
            }
        } else {
            sendCommand(SELECT_ID_CMD, 2);
            long currentTimeMillis = System.currentTimeMillis();
            while (System.currentTimeMillis() - currentTimeMillis < 150) {
                threadSleep(10);
            }
        }
        return this.selectSuccess;
    }

    public byte[] readIDCard() {
        this.readComplete = false;
        this.readDataWrong = false;
        this.readData = null;
        if (this.usbReader != null) {
            byte[] sendCommandUSB = sendCommandUSB(READ_ID_CMD, 3);
            this.readData = sendCommandUSB;
            if (sendCommandUSB != null && sendCommandUSB.length > 5) {
                if (sendCommandUSB[5] != 9 && sendCommandUSB[5] != 5) {
                    this.readData = null;
                    setLog("read id head wrong");
                } else if (sendCommandUSB[5] == 5 && sendCommandUSB.length == 1297) {
                    setLog("read:1297");
                } else if (sendCommandUSB[5] == 9 && sendCommandUSB.length == 2321) {
                    setLog("read:2321");
                }
            }
        } else {
            sendCommand(READ_ID_CMD, 3);
            long currentTimeMillis = System.currentTimeMillis();
            while (System.currentTimeMillis() - currentTimeMillis < 2800) {
                threadSleep(100);
                if (this.readComplete) {
                    break;
                }
            }
            if (this.readDataWrong) {
                threadSleep(50);
                this.readData = null;
            }
        }
        return this.readData;
    }

    public IdentityMsg checkIDCard() {
        findIDCard();
        selectIDCard();
        return IdCard.decodeIdCardBaseInfo(readIDCard());
    }

    public void checkID_IC(ReadCallBack readCallBack) {
        if (readCallBack != null) {
            IdentityMsg checkIDCard = checkIDCard();
            if (checkIDCard != null) {
                readCallBack.checkIDSuccess(checkIDCard);
                return;
            }
            readCallBack.checkIDfailed();
            String readUIDTypeA = readUIDTypeA();
            if (readUIDTypeA != null) {
                readCallBack.checkICSuccess(readUIDTypeA);
            } else {
                readCallBack.checkICfailed();
            }
        }
    }

    public String getVersion() {
        byte b;
        this.readData = null;
        byte b2 = 0;
        if (this.usbReader != null) {
            this.readData = sendCommandUSB(CHECK_VERSION, 10, false);
        } else {
            sendCommand(CHECK_VERSION, 10);
            long currentTimeMillis = System.currentTimeMillis();
            while (System.currentTimeMillis() - currentTimeMillis < 100) {
                threadSleep(10);
            }
        }
        setLog("version:" + StringUtil.toHexString(this.readData));
        byte[] bArr = this.readData;
        if (bArr != null) {
            b2 = bArr[6];
            b = bArr[5];
        } else {
            b = 0;
        }
        return String.valueOf((int) b2) + "." + ((int) b);
    }

    public String getIDSam() {
        this.readData = null;
        if (this.usbReader != null) {
            this.readData = sendCommandUSB(GET_ID_SAM, 8);
        } else {
            sendCommand(GET_ID_SAM, 8);
            long currentTimeMillis = System.currentTimeMillis();
            while (System.currentTimeMillis() - currentTimeMillis < 100) {
                threadSleep(10);
            }
        }
        byte[] bArr = this.readData;
        if (bArr != null && bArr.length > 7 && bArr[7] == 0 && bArr[8] == 0 && bArr[9] == -112) {
            byte[] copyOfRange = Arrays.copyOfRange(bArr, TSAM_HEADER.length + 5, bArr.length - 1);
            this.readData = copyOfRange;
            if (copyOfRange.length != 16) {
                return null;
            }
            return bytearray2Str(this.readData, 0, 2, 2) + bytearray2Str(this.readData, 2, 2, 2) + bytearray2Str(this.readData, 4, 4, 8) + bytearray2Str(this.readData, 8, 4, 10) + bytearray2Str(this.readData, 12, 4, 10);
        }
        return null;
    }

    public byte[] getIDPhyAddr() {
        this.readData = null;
        if (this.usbReader != null) {
            this.readData = sendCommandUSB(GET_ID_PHYADDR, 9);
        } else {
            sendCommand(GET_ID_PHYADDR, 9);
            long currentTimeMillis = System.currentTimeMillis();
            while (System.currentTimeMillis() - currentTimeMillis < 100) {
                threadSleep(10);
            }
        }
        if ("AAAAAA9669000400008084".equals(StringUtil.toHexString(this.readData))) {
            return null;
        }
        return this.readData;
    }

    public byte[] getIDImage(IdentityMsg identityMsg) {
        if (identityMsg == null) {
            return null;
        }
        return identityMsg.getHead_photo();
    }

    public Bitmap decodeIDImage(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        try {
            byte[] bArr2 = new byte[38556];
            if (1 != IdCard.getimage(bArr, bArr2)) {
                return null;
            }
            return IDPhotoHelper.Bgr2Bitmap(bArr2);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Bitmap old32DecodeIDImage(byte[] bArr) {
        Bitmap bitmap = null;
        if (bArr == null) {
            return null;
        }
        try {
            OldIDimage.get_imageusb(bArr, "/sdcard/head.bmp");
            bitmap = BitmapFactory.decodeFile("/sdcard/head.bmp");
            new File("/sdcard/head.bmp").delete();
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return bitmap;
        }
    }

    public byte[] getIDFinger(IdentityMsg identityMsg) {
        byte[] head_photo;
        if (identityMsg == null || (head_photo = identityMsg.getHead_photo()) == null) {
            return null;
        }
        return Arrays.copyOfRange(head_photo, 1024, head_photo.length);
    }

    public void sendAPDU(byte[] bArr) {
        int length = bArr.length + 10;
        byte[] bArr2 = new byte[length];
        bArr2[0] = -86;
        bArr2[1] = -86;
        bArr2[2] = -86;
        bArr2[3] = -106;
        bArr2[4] = 105;
        String hexString = Integer.toHexString(bArr.length + 3);
        while (hexString.length() < 4) {
            hexString = "0" + hexString;
        }
        byte[] bytes = StringUtil.toBytes(hexString);
        bArr2[5] = bytes[0];
        bArr2[6] = bytes[1];
        bArr2[7] = Byte.MIN_VALUE;
        bArr2[8] = 25;
        int i = 9;
        for (byte b : bArr) {
            bArr2[i] = b;
            i++;
        }
        int i2 = length - 1;
        bArr2[i2] = getXor(Arrays.copyOfRange(bArr2, 5, i2));
        this.readData = null;
        if (this.usbReader != null) {
            this.readData = sendCommandUSB(bArr2, 11);
        } else {
            sendCommand(bArr2, 11);
            long currentTimeMillis = System.currentTimeMillis();
            while (System.currentTimeMillis() - currentTimeMillis < 500) {
                threadSleep(10);
            }
        }
        setLog("apdu readData[" + StringUtil.toHexString(this.readData) + "]");
    }

    public String readUIDTypeA() {
        return readUIDTypeA(true);
    }

    public String readUIDTypeA(boolean z) {
        String str = null;
        this.readData = null;
        if (this.usbReader != null) {
            this.readData = sendCommandUSB(READ_TYPEA_CMD, 4);
        } else {
            sendCommand(READ_TYPEA_CMD, 4);
            long currentTimeMillis = System.currentTimeMillis();
            while (System.currentTimeMillis() - currentTimeMillis < 100) {
                threadSleep(10);
            }
        }
        byte[] bArr = this.readData;
        if (bArr != null && bArr.length > 9 && bArr[7] == 5 && bArr[8] == 0 && bArr[9] == -112) {
            byte[] copyOfRange = Arrays.copyOfRange(bArr, 10, 14);
            if (z) {
                int i = 0;
                for (int length = copyOfRange.length - 1; i < length; length--) {
                    byte b = copyOfRange[length];
                    copyOfRange[length] = copyOfRange[i];
                    copyOfRange[i] = b;
                    i++;
                }
            }
            str = decodetcarduid(copyOfRange);
        }
        threadSleep(50);
        return str;
    }

    public boolean passwordCheckTypeA(String str, String str2) {
        return passwordCheckTypeA(str, str2, 0);
    }

    public boolean passwordCheckTypeA(String str, String str2, int i) {
        byte[] bArr;
        this.readData = null;
        if (str == null || "".equals(str) || str2 == null || "".equals(str2)) {
            setLog("passwordCheckTypeA parmas wrong");
            return false;
        }
        String upperCase = str2.toUpperCase();
        String upperCase2 = str.toUpperCase();
        byte[] bytes = StringUtil.toBytes(upperCase);
        byte[] bytes2 = StringUtil.toBytes(upperCase2);
        byte[] bArr2 = new byte[17];
        System.arraycopy(new byte[]{-86, -86, -86, -106, 105, 0, 16, Byte.MIN_VALUE, 11, 1}, 0, bArr2, 0, 10);
        for (int i2 = 0; i2 < bytes.length; i2++) {
            bArr2[i2 + 10] = bytes[i2];
        }
        bArr2[16] = -102;
        String hexString = StringUtil.toHexString(bytes2);
        if (hexString.substring(0, 1).equals("0")) {
            hexString = hexString.substring(1, 2);
        }
        this.block_ = Integer.valueOf(hexString).intValue() / 4;
        this.section_ = Integer.valueOf(hexString).intValue() % 4;
        if (Integer.toHexString(Integer.valueOf(this.block_).intValue()).length() < 2) {
            bArr = StringUtil.toBytes("0" + Integer.toHexString(Integer.valueOf(this.block_).intValue()));
        } else {
            bArr = StringUtil.toBytes(Integer.toHexString(Integer.valueOf(this.block_).intValue()));
        }
        byte b = bArr[0];
        if (i == 1) {
            String binaryString = Integer.toBinaryString(Integer.valueOf(StringUtil.toHexString(bArr), 16).intValue());
            while (binaryString.length() < 8) {
                binaryString = "0" + binaryString;
            }
            b = StringUtil.toBytes(Integer.toHexString(Integer.valueOf("1" + binaryString.substring(1, binaryString.length()), 2).intValue()))[0];
        }
        byte[] bArr3 = {-86, -86, -86, -106, 105, 0, 16, Byte.MIN_VALUE, 11, b, bytes[0], bytes[1], bytes[2], bytes[3], bytes[4], bytes[5], getXor(Arrays.copyOfRange(new byte[]{0, 16, Byte.MIN_VALUE, 11, b, bytes[0], bytes[1], bytes[2], bytes[3], bytes[4], bytes[5]}, 0, 11))};
        if (this.usbReader != null) {
            this.readData = sendCommandUSB(bArr3, 5);
        } else {
            sendCommand(bArr3, 5);
        }
        threadSleep(100);
        byte[] bArr4 = this.readData;
        if (bArr4 != null && bArr4 == null) {
            return false;
        }
        if (isSame()) {
            this.readData = new byte[64];
            int i3 = 0;
            while (true) {
                byte[] bArr5 = VERIFYPASSWORD2;
                if (i3 >= bArr5.length) {
                    break;
                }
                this.readData[i3] = bArr5[i3];
                i3++;
            }
        }
        byte[] bArr6 = {11, 0, -112};
        int i4 = 0;
        while (true) {
            byte[] bArr7 = TSAM_HEADER;
            if (i4 < bArr7.length) {
                if (this.readData[i4] != bArr7[i4]) {
                    setLog("passwordCheckTypeA head wrong");
                    return false;
                }
                i4++;
            } else {
                for (int i5 = 0; i5 < 3; i5++) {
                    if (this.readData[TSAM_HEADER.length + i5 + 2] != bArr6[i5]) {
                        setLog("passwordCheckTypeA ask wrong");
                        return false;
                    }
                }
                setLog("passwordCheckTypeA success");
                return true;
            }
        }
    }

    public String readDataTYPEA() {
        String str;
        byte[] bArr;
        String str2 = null;
        this.readData = null;
        int i = this.block_;
        if (i < 10 || Integer.toHexString(i).length() < 2) {
            str = "0006800D0" + Integer.toHexString(this.block_).toUpperCase() + "0" + this.section_ + "10";
        } else {
            str = "0006800D" + Integer.toHexString(this.block_).toUpperCase() + "0" + this.section_ + "10";
        }
        byte xor = getXor(StringUtil.toBytes(str));
        if (Integer.toHexString(Integer.valueOf(this.block_).intValue()).length() < 2) {
            bArr = StringUtil.toBytes("0" + Integer.toHexString(Integer.valueOf(this.block_).intValue()));
        } else {
            bArr = StringUtil.toBytes(Integer.toHexString(Integer.valueOf(this.block_).intValue()));
        }
        byte[] bytes = StringUtil.toBytes("0" + Integer.toHexString(Integer.valueOf(this.section_).intValue()));
        byte b = bArr[0];
        this.psw_check_block = b;
        byte b2 = bytes[0];
        this.psw_check_section = b2;
        byte[] bArr2 = {-86, -86, -86, -106, 105, 0, 6, Byte.MIN_VALUE, 13, b, b2, 16, xor};
        if (this.usbReader != null) {
            this.readData = sendCommandUSB(bArr2, 6);
        } else {
            sendCommand(bArr2, 6);
        }
        threadSleep(100);
        if (this.readData == null) {
            return null;
        }
        byte[] bArr3 = {13, 0, -112};
        Boolean bool = true;
        int i2 = 0;
        while (true) {
            byte[] bArr4 = TSAM_HEADER;
            if (i2 >= bArr4.length) {
                break;
            }
            if (this.readData[i2] != bArr4[i2]) {
                setLog("readDataTYPEA head wrong");
                bool = false;
            }
            i2++;
        }
        for (int i3 = 0; i3 < 3; i3++) {
            if (this.readData[TSAM_HEADER.length + i3 + 2] != bArr3[i3]) {
                setLog("readDataTYPEA ask wrong");
                bool = false;
            }
        }
        if (bool.booleanValue()) {
            setLog("readDataTYPEA success");
            byte[] bArr5 = this.readData;
            byte[] bArr6 = TSAM_HEADER;
            str2 = decodetcarduid(Arrays.copyOfRange(bArr5, bArr6.length + 5, bArr6.length + 21));
        }
        return str2 != null ? str2.toUpperCase() : str2;
    }

    public boolean writeDataTypeA(String str) {
        this.readData = null;
        if (str == null || "".equals(str)) {
            setLog("writeDataTypeA param wrong");
            return false;
        }
        byte[] bytes = StringUtil.toBytes(str.toUpperCase());
        int length = bytes.length + 15;
        byte[] bArr = new byte[length];
        byte b = 16;
        if (bytes.length != 16) {
            b = StringUtil.toBytes("0" + Integer.toHexString(bytes.length))[0];
        }
        boolean z = true;
        System.arraycopy(new byte[]{-86, -86, -86, -106, 105, 0, 22, Byte.MIN_VALUE, 14, this.psw_check_block, this.psw_check_section, b}, 0, bArr, 0, 12);
        for (int i = 0; i < bytes.length; i++) {
            bArr[i + 12] = bytes[i];
        }
        byte[] bArr2 = new byte[bytes.length + 7];
        System.arraycopy(new byte[]{0, 22, Byte.MIN_VALUE, 14, this.psw_check_block, this.psw_check_section, b}, 0, bArr2, 0, 7);
        for (int i2 = 0; i2 < bytes.length; i2++) {
            bArr2[i2 + 7] = bytes[i2];
        }
        bArr[length - 1] = crc(bArr2);
        if (this.usbReader != null) {
            this.readData = sendCommandUSB(bArr, 7);
        } else {
            sendCommand(bArr, 7);
        }
        threadSleep(100);
        if (this.readData == null) {
            return false;
        }
        byte[] bArr3 = {14, 0, -112};
        int i3 = 0;
        while (true) {
            byte[] bArr4 = TSAM_HEADER;
            if (i3 >= bArr4.length) {
                break;
            }
            if (this.readData[i3] != bArr4[i3]) {
                setLog("writeDataTypeA head wrong");
                z = false;
            }
            i3++;
        }
        for (int i4 = 0; i4 < 3; i4++) {
            if (this.readData[TSAM_HEADER.length + i4 + 2] != bArr3[i4]) {
                setLog("writeDataTypeA ask wrong");
                z = false;
            }
        }
        setLog("writeDataTypeA success");
        return z;
    }

    public void sendCommand(byte[] bArr, int i) {
        try {
            OutputStream outputStream = this.mOutputStream;
            if (outputStream == null) {
                return;
            }
            this.doingType = i;
            outputStream.write(bArr);
            setLog("send[" + StringUtil.toHexString(bArr) + "]");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] sendCommandUSB(byte[] bArr, int i) {
        return sendCommandUSB(bArr, i, true);
    }

    private void reopen() {
        SystemUtil.getInternalModel();
        this.usbReader = null;
        this.mUsbManager = null;
        PendingIntent broadcast = PendingIntent.getBroadcast(this.mContext, 0, new Intent("com.android.example.USB_PERMISSION"), 0);
        UsbManager usbManager = (UsbManager) this.mContext.getSystemService("usb");
        this.mUsbManager = usbManager;
        for (UsbDevice usbDevice : usbManager.getDeviceList().values()) {
            int productId = usbDevice.getProductId();
            int vendorId = usbDevice.getVendorId();
            setLog("reopen() >>> usb device pid:" + productId + "/vid:" + vendorId);
            if ((productId == 50010 && vendorId == 1024) || ((productId == 22352 && vendorId == 1155 && !usbDevice.getProductName().contains("SCANNER")) || (productId == 650 && vendorId == 10473))) {
                this.usbReader = usbDevice;
                if (this.mUsbManager.hasPermission(usbDevice)) {
                    return;
                }
                this.mUsbManager.requestPermission(usbDevice, broadcast);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x0052 A[Catch: all -> 0x0165, Exception -> 0x0167, TRY_ENTER, TRY_LEAVE, TryCatch #2 {all -> 0x0165, blocks: (B:24:0x0052, B:28:0x005d, B:30:0x0092, B:36:0x00b6, B:37:0x00bc, B:39:0x00d4, B:43:0x00e1, B:44:0x00fe, B:46:0x011e, B:49:0x0126, B:51:0x012a, B:53:0x012e, B:55:0x0132, B:57:0x0137, B:59:0x013b, B:61:0x0140, B:67:0x014a, B:70:0x0150, B:72:0x0156, B:79:0x0168), top: B:92:0x0050 }] */
    /* JADX WARN: Removed duplicated region for block: B:28:0x005d A[Catch: all -> 0x0165, Exception -> 0x0167, TRY_ENTER, TryCatch #2 {all -> 0x0165, blocks: (B:24:0x0052, B:28:0x005d, B:30:0x0092, B:36:0x00b6, B:37:0x00bc, B:39:0x00d4, B:43:0x00e1, B:44:0x00fe, B:46:0x011e, B:49:0x0126, B:51:0x012a, B:53:0x012e, B:55:0x0132, B:57:0x0137, B:59:0x013b, B:61:0x0140, B:67:0x014a, B:70:0x0150, B:72:0x0156, B:79:0x0168), top: B:92:0x0050 }] */
    /* JADX WARN: Removed duplicated region for block: B:76:0x0161  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x0032 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private byte[] sendCommandUSB(byte[] r12, int r13, boolean r14) {
        /*
            Method dump skipped, instructions count: 381
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.telpo.tps550.api.idcard.T2OReader.sendCommandUSB(byte[], int, boolean):byte[]");
    }

    /* loaded from: classes.dex */
    public class ReadThread extends Thread {
        private ReadThread() {
            T2OReader.this = r1;
        }

        /* synthetic */ ReadThread(T2OReader t2OReader, ReadThread readThread) {
            this();
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            super.run();
            while (!isInterrupted()) {
                try {
                    if (T2OReader.this.mInputStream != null && T2OReader.this.mInputStream.available() > 0) {
                        byte[] bArr = new byte[1024];
                        byte[] copyOfRange = Arrays.copyOfRange(bArr, 0, T2OReader.this.mInputStream.read(bArr));
                        T2OReader t2OReader = T2OReader.this;
                        t2OReader.readData = ReaderUtils.merge(t2OReader.readData, copyOfRange);
                        T2OReader.this.setLog("receive:" + StringUtil.toHexString(T2OReader.this.readData));
                        if (T2OReader.this.isUpdatingModule) {
                            if (T2OReader.this.doingType != 8) {
                                if (T2OReader.this.doingType == 9) {
                                    if (T2OReader.this.readData != null && T2OReader.this.readData.length > 11 && StringUtil.toHexString(T2OReader.PACKAGESENDSUCCESS).equals(StringUtil.toHexString(Arrays.copyOfRange(T2OReader.this.readData, T2OReader.this.readData.length - 12, T2OReader.this.readData.length)))) {
                                        T2OReader.this.readData = null;
                                        T2OReader.this.receivePackageSuccess = true;
                                        T2OReader.this.setLog("receiveUpdatePackage success");
                                    }
                                    if (T2OReader.this.readData != null && T2OReader.this.readData.length > 11 && StringUtil.toHexString(T2OReader.INTOUPDATE).equals(StringUtil.toHexString(Arrays.copyOfRange(T2OReader.this.readData, T2OReader.this.readData.length - 12, T2OReader.this.readData.length)))) {
                                        T2OReader.this.readData = null;
                                        T2OReader.this.isUpdatingModule = false;
                                        T2OReader.this.serialUpdateSuccess = true;
                                        if (T2OReader.this.callBack != null) {
                                            T2OReader.this.callBack.updateComplete(T2OReader.this.serialUpdateSuccess);
                                        }
                                        T2OReader.this.setLog("module update success!");
                                    }
                                }
                            } else if (T2OReader.this.readData != null && T2OReader.this.readData.length > 11 && StringUtil.toHexString(T2OReader.INTOUPDATE).equals(StringUtil.toHexString(Arrays.copyOfRange(T2OReader.this.readData, T2OReader.this.readData.length - 12, T2OReader.this.readData.length)))) {
                                T2OReader.this.readData = null;
                                T2OReader.this.switchUpdateSuccess = true;
                                T2OReader.this.setLog("switchToUpdate success");
                                T2OReader.this.sendUpdate();
                            }
                        } else if (T2OReader.this.readData != null) {
                            if (T2OReader.this.doingType != 1) {
                                if (T2OReader.this.doingType != 2) {
                                    if (T2OReader.this.doingType != 3) {
                                        if (T2OReader.this.doingType != 4 && T2OReader.this.doingType != 5) {
                                            int unused = T2OReader.this.doingType;
                                        }
                                    } else if (T2OReader.this.readData.length > 10) {
                                        if (T2OReader.this.readData[5] != 9 && T2OReader.this.readData[5] != 5) {
                                            T2OReader.this.readComplete = true;
                                            T2OReader.this.readDataWrong = true;
                                            T2OReader.this.readData = null;
                                            T2OReader.this.setLog("read id head wrong");
                                        } else if (T2OReader.this.readData[5] == 5 && T2OReader.this.readData.length == 1297) {
                                            T2OReader.this.readComplete = true;
                                            T2OReader.this.setLog("id read success : 1297");
                                        } else if (T2OReader.this.readData[5] == 9 && T2OReader.this.readData.length == 2321) {
                                            T2OReader.this.readComplete = true;
                                            T2OReader.this.setLog("id read success : 2321");
                                        }
                                    }
                                } else if (T2OReader.this.readData.length == 19 && "AAAAAA9669000C00009000000000000000009C".equals(StringUtil.toHexString(T2OReader.this.readData))) {
                                    T2OReader.this.setLog("id-select success");
                                    T2OReader.this.selectSuccess = true;
                                }
                            } else if (T2OReader.this.readData.length == 15 && "AAAAAA9669000800009F0000000097".equals(StringUtil.toHexString(T2OReader.this.readData))) {
                                T2OReader.this.setLog("id-find success");
                                T2OReader.this.findSuccess = true;
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setLog(String str) {
        Log.d("T2OReader", str);
    }

    public void threadSleep(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
        return sb.toString().toUpperCase();
    }

    private static byte getXor(byte[] bArr) {
        byte b = bArr[0];
        Log.d("taggg", "datas[0]=" + ((int) b));
        for (int i = 1; i < bArr.length; i++) {
            b = (byte) (b ^ bArr[i]);
            Log.d("taggg", "datas[" + i + "]=" + ((int) bArr[i]) + " temp=" + ((int) b));
        }
        return b;
    }

    private boolean isSame() {
        if (this.readData.length < 18) {
            byte[] bArr = new byte[64];
            int i = 0;
            while (true) {
                byte[] bArr2 = this.readData;
                if (i >= bArr2.length) {
                    break;
                }
                bArr[i] = bArr2[i];
                i++;
            }
            this.readData = bArr;
        }
        for (int i2 = 17; i2 < 28; i2++) {
            if (this.readData[i2] != VERIFYPASSWORD1[i2]) {
                setLog("isSame false");
                return false;
            }
        }
        return true;
    }

    private static byte crc(byte[] bArr) {
        byte b = 0;
        for (byte b2 : bArr) {
            b = (byte) (b ^ b2);
        }
        return b;
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

    public void serialModuleUpdate(T2OReaderCallBack t2OReaderCallBack) {
        this.serialUpdateSuccess = false;
        this.callBack = t2OReaderCallBack;
        if (!new File("/sdcard/project.bin").exists()) {
            setLog("not found update file");
            if (t2OReaderCallBack == null) {
                return;
            }
            this.callBack.updateComplete(this.serialUpdateSuccess);
            return;
        }
        byte[] decodeProjectBin = decodeProjectBin();
        if (decodeProjectBin != null) {
            this.isUpdatingModule = true;
            sendCommand(decodeProjectBin, 8);
        } else if (t2OReaderCallBack == null) {
        } else {
            this.callBack.updateComplete(this.serialUpdateSuccess);
        }
    }

    public boolean usbModuleUpdate(Context context) {
        if (!new File("/sdcard/project.bin").exists()) {
            setLog("not found update file");
            return false;
        }
        if (connectUsb(context)) {
            if (getUpdate(context)) {
                setLog("update success");
                return true;
            }
            setLog("update failed");
        }
        return false;
    }

    public void sendUpdate() {
        this.sendingCount = 1;
        final int length = (this.project_bin.length / 128) + 1;
        this.receivePackageSuccess = true;
        new Thread(new Runnable() { // from class: com.telpo.tps550.api.idcard.T2OReader.1
            @Override // java.lang.Runnable
            public void run() {
                byte[] bArr;
                while (T2OReader.this.sendingCount <= length) {
                    try {
                        if (T2OReader.this.receivePackageSuccess) {
                            T2OReader.this.receivePackageSuccess = false;
                            T2OReader.this.setLog("sending package " + T2OReader.this.sendingCount);
                            if (T2OReader.this.sendingCount < length) {
                                bArr = Arrays.copyOfRange(T2OReader.this.project_bin, (T2OReader.this.sendingCount - 1) * 128, T2OReader.this.sendingCount * 128);
                            } else {
                                bArr = Arrays.copyOfRange(T2OReader.this.project_bin, (T2OReader.this.sendingCount - 1) * 128, T2OReader.this.project_bin.length);
                                new Thread(new Runnable() { // from class: com.telpo.tps550.api.idcard.T2OReader.1.1
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        T2OReader.this.threadSleep(1000);
                                        if (T2OReader.this.callBack == null || T2OReader.this.serialUpdateSuccess) {
                                            return;
                                        }
                                        T2OReader.this.callBack.updateComplete(T2OReader.this.serialUpdateSuccess);
                                    }
                                }).start();
                            }
                            T2OReader.this.sendCommand(bArr, 9);
                            T2OReader.this.sendingCount++;
                        }
                        Thread.sleep(50L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public byte[] decodeProjectBin() {
        String hexString;
        String str;
        this.project_bin = null;
        try {
            this.project_bin = getBytes4File(String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath()) + "/project.bin");
        } catch (IOException e) {
            e.printStackTrace();
            T2OReaderCallBack t2OReaderCallBack = this.callBack;
            if (t2OReaderCallBack != null) {
                t2OReaderCallBack.updateComplete(this.serialUpdateSuccess);
            }
        }
        Log.d("taggg", "project_bin length[" + this.project_bin.length + "]");
        int i = 0;
        while (this.project_bin.length - i > 512) {
            if (i == 0) {
                Log.d("taggg", "project_bin content[" + StringUtil.toHexString(Arrays.copyOfRange(this.project_bin, i, i + 512)) + "]");
            }
            i += 512;
        }
        StringBuilder sb = new StringBuilder("usb receive[");
        byte[] bArr = this.project_bin;
        Log.d("taggg", sb.append(StringUtil.toHexString(Arrays.copyOfRange(bArr, i, bArr.length))).append("]").toString());
        byte[] bArr2 = this.project_bin;
        if (bArr2 == null) {
            T2OReaderCallBack t2OReaderCallBack2 = this.callBack;
            if (t2OReaderCallBack2 != null) {
                t2OReaderCallBack2.updateComplete(this.serialUpdateSuccess);
            }
            return null;
        }
        String hexString2 = Integer.toHexString(bArr2.length);
        Log.d("taggg", "codeLenth[" + hexString2 + "]");
        while (hexString2.length() < 8) {
            hexString2 = "0" + hexString2;
        }
        int length = hexString2.length();
        Log.d("taggg", "codeSize[" + length + "]");
        int i2 = length - 2;
        StringBuilder sb2 = new StringBuilder(String.valueOf(hexString2.substring(i2, length)));
        int i3 = length - 4;
        int i4 = length - 6;
        String sb3 = sb2.append(hexString2.substring(i3, i2)).append(hexString2.substring(i4, i3)).append(hexString2.substring(0, i4)).toString();
        Log.d("taggg", "codeLenth[" + sb3 + "]");
        byte[] bArr3 = this.project_bin;
        Log.d("taggg", "tempCRC[" + Long.toHexString(getCRC(bArr3, bArr3.length)) + "]");
        String str2 = String.valueOf(hexString.substring(hexString.length() - 2, hexString.length())) + hexString.substring(hexString.length() - 4, hexString.length() - 2);
        Log.d("taggg", "crc[" + str2 + "]");
        Log.d("taggg", "xorString[" + ("00078020" + sb3 + str2) + "]");
        String str3 = "AAAAAA966900078020" + sb3 + str2 + StringUtil.toHexString(new byte[]{getXor(StringUtil.toBytes(str))});
        Log.d("taggg", "cmdStr[" + str3 + "]");
        Log.d("tagg", "cmdStr length[" + str3.length() + "]");
        setLog("cmdStr:" + str3);
        return StringUtil.toBytes(str3);
    }

    private boolean connectUsb(Context context) {
        return connectUsb(context, -1, -1);
    }

    private boolean connectUsb(Context context, int i, int i2) {
        setLog("update-connectUSB");
        boolean z = false;
        PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, new Intent("com.android.example.USB_PERMISSION"), 0);
        UsbManager usbManager = (UsbManager) context.getSystemService("usb");
        this.mUsbManager = usbManager;
        for (UsbDevice usbDevice : usbManager.getDeviceList().values()) {
            int productId = usbDevice.getProductId();
            int vendorId = usbDevice.getVendorId();
            if ((productId == 50010 && vendorId == 1024) || ((productId == 22352 && vendorId == 1155) || ((productId == 650 && vendorId == 10473) || (productId == i && vendorId == i2)))) {
                this.updater = usbDevice;
                if (this.mUsbManager.hasPermission(usbDevice)) {
                    return true;
                }
                this.mUsbManager.requestPermission(usbDevice, broadcast);
                z = true;
            }
        }
        return z;
    }

    private boolean getUpdate(Context context) {
        requestOrCheck(context, 0);
        sleep();
        while (this.checkCount < 5 && !downloadFile(context)) {
            sleep();
            requestOrCheck(context, 1);
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

    private boolean downloadFile(Context context) {
        if (connectUsb(context, 649, IdCard.READER_VID_WINDOWS)) {
            try {
                byte[] bytes4File = getBytes4File(String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath()) + "/project.bin");
                int length = bytes4File.length;
                Log.d("tagg", "project_bin_all length[" + length + "]");
                if (length <= 7000) {
                    return requestUpdateDataBtn(Arrays.copyOfRange(bytes4File, 0, length), 3);
                }
                int i = 0;
                while (length - i > 7000) {
                    int i2 = i + 7000;
                    requestUpdateDataBtn(Arrays.copyOfRange(bytes4File, i, i2), 2);
                    Log.d("tagg", "count1[" + i + "] count2[" + i2 + "]");
                    i = i2;
                }
                Log.d("tagg", "count1[" + i + "] count2[" + length + "]");
                return requestUpdateDataBtn(Arrays.copyOfRange(bytes4File, i, length), 3);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
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
        throw new UnsupportedOperationException("Method not decompiled: com.telpo.tps550.api.idcard.T2OReader.getBytes4File(java.lang.String):byte[]");
    }

    private boolean requestOrCheck(Context context, int i) {
        String str;
        if (i == 0) {
            connectUsb(context);
        } else if (i == 1) {
            connectUsb(context, 649, IdCard.READER_VID_WINDOWS);
        }
        byte[] bArr = null;
        try {
            bArr = getBytes4File(String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath()) + "/project.bin");
        } catch (IOException e) {
            e.printStackTrace();
        }
        String hexString = Integer.toHexString(bArr.length);
        while (hexString.length() < 8) {
            hexString = "0" + hexString;
        }
        int length = hexString.length();
        int i2 = length - 2;
        StringBuilder sb = new StringBuilder(String.valueOf(hexString.substring(i2, length)));
        int i3 = length - 4;
        int i4 = length - 6;
        String sb2 = sb.append(hexString.substring(i3, i2)).append(hexString.substring(i4, i3)).append(hexString.substring(0, i4)).toString();
        String hexString2 = Long.toHexString(getCRC(bArr, bArr.length));
        return requestUpdateDataBtn(StringUtil.toBytes("AAAAAA966900078020" + sb2 + (String.valueOf(hexString2.substring(hexString2.length() - 2, hexString2.length())) + hexString2.substring(hexString2.length() - 4, hexString2.length() - 2)) + StringUtil.toHexString(new byte[]{getXor(StringUtil.toBytes("00078020" + sb2 + str))})), i);
    }

    private long getCRC(byte[] bArr, int i) {
        long j = 0;
        int i2 = 0;
        int i3 = i;
        while (i2 < i3) {
            long j2 = j >> 8;
            byte b = bArr[i2];
            long j3 = b ^ j2;
            long j4 = 255 & j3;
            long j5 = j << 8;
            int i4 = (int) j4;
            long j6 = this.crc_16tab[i4] ^ j5;
            if (i2 == 21) {
                Log.d("taggg", "temp1[" + j2 + "] temp2[" + ((int) b) + "] temp3[" + j3 + "] temp4[" + j4 + "] temp5[" + j5 + "] temp6[" + i4 + "] cksum[" + j6 + "]");
            }
            i2++;
            i3 = i;
            j = j6;
        }
        return j;
    }

    private boolean requestUpdateDataBtn(byte[] bArr, int i) {
        byte[] bArr2;
        byte[] bArr3;
        UsbDevice usbDevice = this.updater;
        if (usbDevice != null) {
            UsbInterface usbInterface = usbDevice.getInterface(0);
            UsbEndpoint endpoint = usbInterface.getEndpoint(0);
            UsbEndpoint endpoint2 = usbInterface.getEndpoint(1);
            try {
                UsbDeviceConnection openDevice = this.mUsbManager.openDevice(this.updater);
                if (openDevice == null) {
                    return false;
                }
                openDevice.claimInterface(usbInterface, true);
                openDevice.bulkTransfer(endpoint2, bArr, bArr.length, 5000);
                Log.d("tagg", "send >> length[" + bArr.length + "] " + StringUtil.toHexString(bArr));
                byte[] bArr4 = new byte[5120];
                int bulkTransfer = openDevice.bulkTransfer(endpoint, bArr4, 5120, 5000);
                openDevice.close();
                this.readData = null;
                if (bulkTransfer != -1) {
                    this.readData = Arrays.copyOfRange(bArr4, 0, bulkTransfer);
                    Log.d("tagg", "receive >> " + StringUtil.toHexString(this.readData));
                } else {
                    Log.d("tagg", "receive >> -1");
                }
                if (i == 0) {
                    return true;
                }
                if (i == 1 && (bArr3 = this.readData) != null && bArr3.length > 10) {
                    if (bulkTransfer != -1 && bArr3[9] == 1) {
                        return true;
                    }
                } else if (i == 3 && (bArr2 = this.readData) != null && bArr2.length > 10 && bulkTransfer != -1 && bArr2[9] == 0) {
                    this.finalRet = true;
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private String selectSerial() {
        int deviceType = SystemUtil.getDeviceType();
        if (deviceType == StringUtil.DeviceModelEnum.TPS550.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS550A.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS510.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS580A.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS510A.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS510A_NHW.ordinal() || "TPS980P".equals(SystemUtil.getInternalModel()) || "TPS990H".equals(SystemUtil.getInternalModel())) {
            return "/dev/ttyS0";
        }
        if (deviceType == StringUtil.DeviceModelEnum.TPS580.ordinal()) {
            return "/dev/ttyS2";
        }
        if ("F10".equals(SystemUtil.getInternalModel())) {
            return "/dev/ttyHSL1";
        }
        if (deviceType == StringUtil.DeviceModelEnum.TPS550MTK.ordinal()) {
            return "/dev/ttyMT3";
        }
        if (deviceType != StringUtil.DeviceModelEnum.TPS462.ordinal() && deviceType != StringUtil.DeviceModelEnum.TPS468.ordinal()) {
            return null;
        }
        return "/dev/ttyS3";
    }
}
