package com.telpo.tps550.api.idcard;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import com.telpo.tps550.api.TelpoException;
import com.telpo.tps550.api.util.ReaderUtils;
import com.zkteco.android.IDReader.IDPhotoHelper;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/* loaded from: classes.dex */
public class BlueToothIdCard {
    private static final int STEP_CHECK = 0;
    private static final int STEP_READ = 2;
    private static final int STEP_SELECT = 1;
    private static final String TAG = "BlueToothIdCard";
    private static BluetoothGattCharacteristic notifyCharacteristic;
    private static BluetoothGattCharacteristic writeCharacteristic;
    private int contentLength;
    private int fplength;
    private int imageDatalength;
    private Context mContext;
    static String[] nation_list = {"汉", "蒙古", "回", "藏", "维吾尔", "苗", "彝", "壮", "布依", "朝鲜", "满", "侗", "瑶", "白", "土家", "哈尼", "哈萨克", "傣", "黎", "傈僳", "佤", "畲", "高山", "拉祜", "水", "东乡", "纳西", "景颇", "柯尔克孜", "土", "达斡尔", "仫佬", "羌", "布朗", "撒拉", "毛南", "仡佬", "锡伯", "阿昌", "普米", "塔吉克", "怒", "乌孜别克", "俄罗斯", "鄂温克", "德昂", "保安", "裕固", "京", "塔塔尔", "独龙", "鄂伦春", "赫哲", "门巴", "珞巴", "基诺", "其他", "外国血统中国籍人士"};
    private static final UUID SERVICE_WRITE_UUID = UUID.fromString("0000ffe5-0000-1000-8000-00805f9b34fb");
    private static final UUID SERVICE_NOTIFY_UUID = UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb");
    private static final UUID CHARACTERISTIC_OUTPUT_UUID = UUID.fromString("0000ffe9-0000-1000-8000-00805f9b34fb");
    private static final UUID CHARACTERISTIC_NOTIFY_UUID = UUID.fromString("0000ffe4-0000-1000-8000-00805f9b34fb");
    private static byte[] receiveData = null;
    private static byte[] sendData = null;
    private static final byte[] SAM_HEADER = {-86, -86, -86, -106, 105};
    private BluetoothGatt gatt = null;
    public int charSendBufferPos = 0;
    private int currentStep = 0;
    public boolean isConnect = false;
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() { // from class: com.telpo.tps550.api.idcard.BlueToothIdCard.1
        @Override // android.bluetooth.BluetoothGattCallback
        public void onCharacteristicRead(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic, int i) {
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onCharacteristicWrite(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic, int i) {
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onDescriptorRead(BluetoothGatt bluetoothGatt, BluetoothGattDescriptor bluetoothGattDescriptor, int i) {
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onDescriptorWrite(BluetoothGatt bluetoothGatt, BluetoothGattDescriptor bluetoothGattDescriptor, int i) {
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onReliableWriteCompleted(BluetoothGatt bluetoothGatt, int i) {
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onConnectionStateChange(BluetoothGatt bluetoothGatt, int i, int i2) {
            if (i2 == 2) {
                bluetoothGatt.discoverServices();
                Log.i(BlueToothIdCard.TAG, "Connected to GATT server.");
            } else if (i2 != 0) {
            } else {
                Log.i(BlueToothIdCard.TAG, "Disconnected from GATT server.");
            }
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onServicesDiscovered(BluetoothGatt bluetoothGatt, int i) {
            if (i == 0) {
                Log.i(BlueToothIdCard.TAG, "onServicesDiscovered");
                List<BluetoothGattService> services = bluetoothGatt.getServices();
                BluetoothGattService bluetoothGattService = null;
                BluetoothGattService bluetoothGattService2 = null;
                for (int i2 = 0; i2 < services.size(); i2++) {
                    BluetoothGattService bluetoothGattService3 = services.get(i2);
                    Log.d(BlueToothIdCard.TAG, "service " + i2 + ":" + bluetoothGattService3.getUuid().toString());
                    if (BlueToothIdCard.SERVICE_WRITE_UUID.toString().equals(bluetoothGattService3.getUuid().toString())) {
                        bluetoothGattService = bluetoothGattService3;
                    }
                    if (BlueToothIdCard.SERVICE_NOTIFY_UUID.toString().equals(bluetoothGattService3.getUuid().toString())) {
                        bluetoothGattService2 = bluetoothGattService3;
                    }
                }
                List<BluetoothGattCharacteristic> characteristics = bluetoothGattService.getCharacteristics();
                for (int i3 = 0; i3 < characteristics.size(); i3++) {
                    BluetoothGattCharacteristic bluetoothGattCharacteristic = characteristics.get(i3);
                    Log.d(BlueToothIdCard.TAG, "characteristic " + i3 + ":" + bluetoothGattCharacteristic.getUuid().toString());
                    if (BlueToothIdCard.CHARACTERISTIC_OUTPUT_UUID.toString().equals(bluetoothGattCharacteristic.getUuid().toString())) {
                        BlueToothIdCard.writeCharacteristic = bluetoothGattCharacteristic;
                    }
                }
                List<BluetoothGattCharacteristic> characteristics2 = bluetoothGattService2.getCharacteristics();
                for (int i4 = 0; i4 < characteristics2.size(); i4++) {
                    BluetoothGattCharacteristic bluetoothGattCharacteristic2 = characteristics2.get(i4);
                    Log.d(BlueToothIdCard.TAG, "characteristic " + i4 + ":" + bluetoothGattCharacteristic2.getUuid().toString());
                    if (BlueToothIdCard.CHARACTERISTIC_NOTIFY_UUID.toString().equals(bluetoothGattCharacteristic2.getUuid().toString())) {
                        BlueToothIdCard.notifyCharacteristic = bluetoothGattCharacteristic2;
                    }
                }
                if (BlueToothIdCard.writeCharacteristic != null && BlueToothIdCard.notifyCharacteristic != null && BlueToothIdCard.this.isCharacteristicWritable(BlueToothIdCard.writeCharacteristic) && BlueToothIdCard.this.isCharacteristicNotifiable(BlueToothIdCard.notifyCharacteristic)) {
                    if (bluetoothGatt.setCharacteristicNotification(BlueToothIdCard.notifyCharacteristic, true)) {
                        Log.d(BlueToothIdCard.TAG, "连接成功");
                        BlueToothIdCard.this.isConnect = true;
                        return;
                    }
                    Log.d(BlueToothIdCard.TAG, "连接失败");
                    BlueToothIdCard.this.isConnect = false;
                    return;
                }
                Log.d(BlueToothIdCard.TAG, "未知设备");
                BlueToothIdCard.this.isConnect = false;
                return;
            }
            Log.d(BlueToothIdCard.TAG, "连接失败");
            BlueToothIdCard.this.isConnect = false;
        }

        @Override // android.bluetooth.BluetoothGattCallback
        public void onCharacteristicChanged(BluetoothGatt bluetoothGatt, BluetoothGattCharacteristic bluetoothGattCharacteristic) {
            byte[] value = bluetoothGattCharacteristic.getValue();
            if (value == null || value.length <= 0) {
                return;
            }
            BlueToothIdCard.receiveData = ReaderUtils.merge(BlueToothIdCard.receiveData, value);
        }
    };

    public BlueToothIdCard(Context context) {
        this.mContext = null;
        this.mContext = context;
    }

    public void connectDevice(BluetoothDevice bluetoothDevice) {
        if (bluetoothDevice != null) {
            this.gatt = bluetoothDevice.connectGatt(this.mContext, false, this.mGattCallback);
        }
    }

    private boolean isCharacteristicReadable(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        return (bluetoothGattCharacteristic.getProperties() & 2) > 0;
    }

    public boolean isCharacteristicWritable(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        return (bluetoothGattCharacteristic.getProperties() & 8) > 0 || (bluetoothGattCharacteristic.getProperties() & 4) > 0;
    }

    public boolean isCharacteristicNotifiable(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        return (bluetoothGattCharacteristic.getProperties() & 16) > 0 || (bluetoothGattCharacteristic.getProperties() & 32) > 0;
    }

    private byte[] packageCommand(byte[] bArr) {
        if (bArr == null || bArr.length <= 0) {
            return null;
        }
        int length = bArr.length;
        byte[] bArr2 = new byte[length + 4 + 2];
        int i = 0;
        bArr2[0] = 104;
        bArr2[1] = (byte) (length % 255);
        bArr2[2] = (byte) (length / 255);
        bArr2[3] = 104;
        int length2 = bArr.length;
        int i2 = 4;
        byte b = 0;
        while (i < length2) {
            byte b2 = bArr[i];
            bArr2[i2] = b2;
            b = (byte) (b ^ b2);
            i++;
            i2++;
        }
        bArr2[i2] = b;
        bArr2[i2 + 1] = 22;
        return bArr2;
    }

    private void sendCommand() {
        int i = this.charSendBufferPos;
        int i2 = i + 20;
        byte[] bArr = sendData;
        int length = i2 > bArr.length ? (byte) (bArr.length - i) : 20;
        byte[] bArr2 = new byte[length];
        for (byte b = 0; b < length; b = (byte) (b + 1)) {
            bArr2[b] = sendData[this.charSendBufferPos + b];
        }
        writeCharacteristic.setValue(bArr2);
        this.gatt.writeCharacteristic(writeCharacteristic);
        try {
            Thread.sleep(20L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int i3 = this.charSendBufferPos + length;
        this.charSendBufferPos = i3;
        byte[] bArr3 = sendData;
        if (i3 == bArr3.length) {
            this.charSendBufferPos = 0;
        }
        this.charSendBufferPos %= bArr3.length;
    }

    public IdentityMsg checkIdCard() {
        receiveData = null;
        byte[] packageCommand = packageCommand(new byte[]{-86, -86, -86, -106, 105, 0, 3, 32, 1, 34});
        sendData = packageCommand;
        if (packageCommand != null) {
            sendCommand();
            this.currentStep = 0;
        }
        try {
            Thread.sleep(200L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        checkReceivedData();
        receiveData = null;
        byte[] packageCommand2 = packageCommand(new byte[]{-86, -86, -86, -106, 105, 0, 3, 32, 2, 33});
        sendData = packageCommand2;
        if (packageCommand2 != null) {
            sendCommand();
            this.currentStep = 1;
        }
        try {
            Thread.sleep(200L);
        } catch (InterruptedException e2) {
            e2.printStackTrace();
        }
        checkReceivedData();
        receiveData = null;
        byte[] packageCommand3 = packageCommand(new byte[]{-86, -86, -86, -106, 105, 0, 3, 48, 16, 35});
        sendData = packageCommand3;
        if (packageCommand3 != null) {
            sendCommand();
            this.currentStep = 2;
        }
        byte[] checkReceivedData = checkReceivedData();
        receiveData = null;
        if (checkReceivedData != null) {
            Log.e(TAG, "stepReceiveData.length is" + checkReceivedData.length);
        }
        return decodeIdCardBaseInfo(checkReceivedData);
    }

    public static synchronized byte[] getIdCardImage(IdentityMsg identityMsg) throws TelpoException {
        byte[] head_photo;
        synchronized (BlueToothIdCard.class) {
            head_photo = identityMsg.getHead_photo();
            if (head_photo == null) {
                throw new IdCardNotCheckException();
            }
        }
        return head_photo;
    }

    public static synchronized byte[] getFringerPrint(IdentityMsg identityMsg) throws TelpoException {
        byte[] copyOfRange;
        synchronized (BlueToothIdCard.class) {
            byte[] idCardImage = getIdCardImage(identityMsg);
            if (idCardImage == null) {
                throw new IdCardNotCheckException();
            }
            try {
                copyOfRange = Arrays.copyOfRange(idCardImage, 1024, idCardImage.length);
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

    /* JADX WARN: Code restructure failed: missing block: B:30:0x0046, code lost:
        r5 = r5 + 2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x0048, code lost:
        if (r3 < r1.length) goto L35;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x004c, code lost:
        return com.telpo.tps550.api.idcard.BlueToothIdCard.receiveData;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x004d, code lost:
        r4 = r5 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x0055, code lost:
        if (com.telpo.tps550.api.idcard.BlueToothIdCard.receiveData[r5] == r1[r3]) goto L38;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x0057, code lost:
        return null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x0058, code lost:
        r3 = r3 + 1;
        r5 = r4;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private byte[] checkReceivedData() {
        /*
            r9 = this;
            r0 = 2
            int r1 = r9.currentStep     // Catch: java.lang.InterruptedException -> Lb
            if (r1 != r0) goto Lf
            r1 = 2500(0x9c4, double:1.235E-320)
            java.lang.Thread.sleep(r1)     // Catch: java.lang.InterruptedException -> Lb
            goto Lf
        Lb:
            r1 = move-exception
            r1.printStackTrace()
        Lf:
            byte[] r1 = com.telpo.tps550.api.idcard.BlueToothIdCard.receiveData
            r2 = 0
            if (r1 != 0) goto L15
            return r2
        L15:
            int r3 = r9.currentStep
            if (r3 != r0) goto L1f
            int r1 = r1.length
            r4 = 1024(0x400, float:1.435E-42)
            if (r1 >= r4) goto L1f
            return r2
        L1f:
            r1 = 3
            if (r3 == 0) goto L35
            r4 = -112(0xffffffffffffff90, float:NaN)
            r5 = 1
            if (r3 == r5) goto L30
            if (r3 == r0) goto L2b
            r1 = r2
            goto L3b
        L2b:
            byte[] r1 = new byte[r1]
            r1[r0] = r4
            goto L3b
        L30:
            byte[] r1 = new byte[r1]
            r1[r0] = r4
            goto L3b
        L35:
            byte[] r1 = new byte[r1]
            r3 = -97
            r1[r0] = r3
        L3b:
            if (r1 != 0) goto L3e
            return r2
        L3e:
            r3 = 0
            r4 = r3
            r5 = r4
        L41:
            byte[] r6 = com.telpo.tps550.api.idcard.BlueToothIdCard.SAM_HEADER     // Catch: java.lang.Exception -> L6b
            int r7 = r6.length     // Catch: java.lang.Exception -> L6b
            if (r4 < r7) goto L5c
            int r5 = r5 + r0
        L47:
            int r0 = r1.length     // Catch: java.lang.Exception -> L6b
            if (r3 < r0) goto L4d
            byte[] r0 = com.telpo.tps550.api.idcard.BlueToothIdCard.receiveData
            return r0
        L4d:
            byte[] r0 = com.telpo.tps550.api.idcard.BlueToothIdCard.receiveData     // Catch: java.lang.Exception -> L6b
            int r4 = r5 + 1
            r0 = r0[r5]     // Catch: java.lang.Exception -> L6b
            r5 = r1[r3]     // Catch: java.lang.Exception -> L6b
            if (r0 == r5) goto L58
            return r2
        L58:
            int r3 = r3 + 1
            r5 = r4
            goto L47
        L5c:
            byte[] r7 = com.telpo.tps550.api.idcard.BlueToothIdCard.receiveData     // Catch: java.lang.Exception -> L6b
            int r8 = r5 + 1
            r5 = r7[r5]     // Catch: java.lang.Exception -> L6b
            r6 = r6[r4]     // Catch: java.lang.Exception -> L6b
            if (r5 == r6) goto L67
            return r2
        L67:
            int r4 = r4 + 1
            r5 = r8
            goto L41
        L6b:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.telpo.tps550.api.idcard.BlueToothIdCard.checkReceivedData():byte[]");
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x00f8  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x01a8  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x01e7  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x025d  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x0263  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x0280  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x0295  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x02cc  */
    /* JADX WARN: Removed duplicated region for block: B:85:0x022d A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public com.telpo.tps550.api.idcard.IdentityMsg decodeIdCardBaseInfo(byte[] r17) {
        /*
            Method dump skipped, instructions count: 722
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.telpo.tps550.api.idcard.BlueToothIdCard.decodeIdCardBaseInfo(byte[]):com.telpo.tps550.api.idcard.IdentityMsg");
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

    private static String formatDate(String str) {
        return str.substring(0, 4) + "." + str.substring(4, 6) + "." + str.substring(6, 8);
    }

    public boolean checkConnect() {
        try {
            Thread.sleep(3500L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return this.isConnect;
    }

    public String getVersion() throws TelpoException {
        sendData = packageCommand(new byte[]{102, 1, 2, 3, -1, 107, 22});
        sendCommand();
        try {
            Thread.sleep(300L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        byte[] bArr = receiveData;
        if (bArr != null) {
            byte[] copyOfRange = Arrays.copyOfRange(bArr, 5, 7);
            receiveData = copyOfRange;
            byte b = copyOfRange[1];
            byte b2 = copyOfRange[0];
            Log.d(TAG, String.valueOf((int) b) + "." + ((int) b2));
            return String.valueOf((int) b) + "." + ((int) b2);
        }
        return null;
    }
}
