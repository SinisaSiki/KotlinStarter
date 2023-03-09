package com.telpo.tps550.api.reader;

import amlib.ccid.Reader;
import amlib.ccid.Reader4428;
import amlib.ccid.Reader4442;
import amlib.hw.HWType;
import amlib.hw.HardwareInterface;
import amlib.hw.ReaderHwException;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.vectordrawable.graphics.drawable.PathInterpolatorCompat;
import com.telpo.tps550.api.TelpoException;
import com.telpo.tps550.api.fingerprint.FingerPrint;
import com.telpo.tps550.api.usb.UsbUtil;
import com.telpo.tps550.api.util.PowerUtil;
import com.telpo.tps550.api.util.ShellUtils;
import com.telpo.tps550.api.util.StringUtil;
import com.telpo.tps550.api.util.SystemUtil;
import java.io.FileOutputStream;

/* loaded from: classes.dex */
public class CardReader {
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    public static final int CARDREADER_HUB = 1;
    public static final int CARD_TYPE_AT88SC153 = 4;
    public static final int CARD_TYPE_ISO7816 = 1;
    public static final int CARD_TYPE_MSC = 0;
    public static final int CARD_TYPE_SLE4428 = 3;
    public static final int CARD_TYPE_SLE4442 = 2;
    public static final int CARD_TYPE_UNKNOWN = -1;
    public static final int SLOT_STATUS_ICC_ABSENT = 2;
    public static final int SLOT_STATUS_ICC_ACTIVE = 0;
    public static final int SLOT_STATUS_ICC_INACTIVE = 1;
    private static final String TAG = "TELPO_SDK";
    protected int calSlot;
    protected int cardType;
    protected Context context;
    protected boolean correct_psc_verification;
    private Handler handler;
    private HandlerThread handlerThread;
    private Object lock;
    Reader4442 m4442;
    protected byte[] mATR;
    protected ICCardReader mICCardReader;
    private final BroadcastReceiver mReceiver;
    protected int mSlot;
    protected HardwareInterface myDev;
    private PendingIntent permissionIntent;
    String[] portNum;
    String[] productNum;
    protected Reader reader;
    String[] readerNum;
    protected int reader_type;
    protected UsbDevice usbDev;
    protected UsbManager usbManager;

    protected static native int close_device();

    private native int device_power(int i);

    protected static native int get_card_status();

    protected static native int icc_power_off();

    protected static native byte[] icc_power_on(int i);

    protected static native int open_device(int i, int i2);

    public static native int psc_modify(int i, byte[] bArr);

    public static native int psc_verify(int i, byte[] bArr);

    public static native byte[] read_main_mem(int i, int i2, int i3);

    public static native int send_apdu(byte[] bArr, byte[] bArr2);

    protected static native int switch_mode(int i);

    protected static native int telpo_switch_psam(int i);

    public static native int update_main_mem(int i, int i2, byte[] bArr);

    public CardReader() {
        this.cardType = 1;
        this.correct_psc_verification = false;
        this.mSlot = 0;
        this.calSlot = 0;
        this.reader = null;
        this.mATR = null;
        this.reader_type = -1;
        this.m4442 = null;
        this.lock = new Object();
        this.portNum = new String[20];
        this.productNum = new String[20];
        this.readerNum = new String[4];
        this.mReceiver = new BroadcastReceiver() { // from class: com.telpo.tps550.api.reader.CardReader.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                Log.d(CardReader.TAG, "Broadcast Receiver");
                String action = intent.getAction();
                if (CardReader.ACTION_USB_PERMISSION.equals(action)) {
                    synchronized (CardReader.this.lock) {
                        UsbDevice usbDevice = (UsbDevice) intent.getParcelableExtra("device");
                        if (intent.getBooleanExtra("permission", false)) {
                            if (usbDevice != null && usbDevice.equals(CardReader.this.usbDev)) {
                                try {
                                    if (CardReader.this.myDev.Init(CardReader.this.usbManager, CardReader.this.usbDev)) {
                                        try {
                                            if (CardReader.this.cardType == 2) {
                                                CardReader.this.reader = new Reader4442(CardReader.this.myDev);
                                                Log.d("TAG", "reader = new Reader4442(myDev);\ndevice name:" + CardReader.this.usbDev.getDeviceName());
                                            } else if (CardReader.this.cardType == 3) {
                                                CardReader.this.reader = new Reader4428(CardReader.this.myDev);
                                            } else {
                                                CardReader.this.reader = new Reader(CardReader.this.myDev);
                                                CardReader.this.reader.switchMode((byte) 1);
                                            }
                                            Log.d("TAG", "reader.open:" + CardReader.this.reader.open());
                                            CardReader.this.reader.setSlot((byte) 0);
                                        } catch (Exception e) {
                                            Log.e(CardReader.TAG, "Get Exception : " + e.getMessage());
                                            e.printStackTrace();
                                        }
                                    }
                                } catch (ReaderHwException e2) {
                                    e2.printStackTrace();
                                }
                                CardReader.this.lock.notify();
                            }
                        } else {
                            Log.d(CardReader.TAG, "Permission denied for device " + usbDevice.getDeviceName());
                            CardReader.this.lock.notify();
                        }
                    }
                } else if (!"android.hardware.usb.action.USB_DEVICE_DETACHED".equals(action)) {
                } else {
                    Log.d(CardReader.TAG, "Device Detached");
                    UsbDevice usbDevice2 = (UsbDevice) intent.getParcelableExtra("device");
                    if (usbDevice2 == null || !usbDevice2.equals(CardReader.this.usbDev)) {
                        return;
                    }
                    if (CardReader.this.reader != null) {
                        CardReader.this.reader.close();
                    }
                    if (CardReader.this.myDev == null) {
                        return;
                    }
                    CardReader.this.myDev.Close();
                }
            }
        };
    }

    public CardReader(Context context) {
        this.cardType = 1;
        this.correct_psc_verification = false;
        this.mSlot = 0;
        this.calSlot = 0;
        this.reader = null;
        this.mATR = null;
        this.reader_type = -1;
        this.m4442 = null;
        this.lock = new Object();
        this.portNum = new String[20];
        this.productNum = new String[20];
        this.readerNum = new String[4];
        this.mReceiver = new BroadcastReceiver() { // from class: com.telpo.tps550.api.reader.CardReader.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                Log.d(CardReader.TAG, "Broadcast Receiver");
                String action = intent.getAction();
                if (CardReader.ACTION_USB_PERMISSION.equals(action)) {
                    synchronized (CardReader.this.lock) {
                        UsbDevice usbDevice = (UsbDevice) intent.getParcelableExtra("device");
                        if (intent.getBooleanExtra("permission", false)) {
                            if (usbDevice != null && usbDevice.equals(CardReader.this.usbDev)) {
                                try {
                                    if (CardReader.this.myDev.Init(CardReader.this.usbManager, CardReader.this.usbDev)) {
                                        try {
                                            if (CardReader.this.cardType == 2) {
                                                CardReader.this.reader = new Reader4442(CardReader.this.myDev);
                                                Log.d("TAG", "reader = new Reader4442(myDev);\ndevice name:" + CardReader.this.usbDev.getDeviceName());
                                            } else if (CardReader.this.cardType == 3) {
                                                CardReader.this.reader = new Reader4428(CardReader.this.myDev);
                                            } else {
                                                CardReader.this.reader = new Reader(CardReader.this.myDev);
                                                CardReader.this.reader.switchMode((byte) 1);
                                            }
                                            Log.d("TAG", "reader.open:" + CardReader.this.reader.open());
                                            CardReader.this.reader.setSlot((byte) 0);
                                        } catch (Exception e) {
                                            Log.e(CardReader.TAG, "Get Exception : " + e.getMessage());
                                            e.printStackTrace();
                                        }
                                    }
                                } catch (ReaderHwException e2) {
                                    e2.printStackTrace();
                                }
                                CardReader.this.lock.notify();
                            }
                        } else {
                            Log.d(CardReader.TAG, "Permission denied for device " + usbDevice.getDeviceName());
                            CardReader.this.lock.notify();
                        }
                    }
                } else if (!"android.hardware.usb.action.USB_DEVICE_DETACHED".equals(action)) {
                } else {
                    Log.d(CardReader.TAG, "Device Detached");
                    UsbDevice usbDevice2 = (UsbDevice) intent.getParcelableExtra("device");
                    if (usbDevice2 == null || !usbDevice2.equals(CardReader.this.usbDev)) {
                        return;
                    }
                    if (CardReader.this.reader != null) {
                        CardReader.this.reader.close();
                    }
                    if (CardReader.this.myDev == null) {
                        return;
                    }
                    CardReader.this.myDev.Close();
                }
            }
        };
        this.context = context;
    }

    public boolean open(int i, int i2) {
        if (SystemUtil.getDeviceType() == StringUtil.DeviceModelEnum.TPS900.ordinal()) {
            try {
                this.mICCardReader.open(i, i2);
                return true;
            } catch (TelpoException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public int AT24CXX_read(int i, int i2, int i3, byte[] bArr, int[] iArr) {
        if (SystemUtil.getDeviceType() == StringUtil.DeviceModelEnum.TPS900.ordinal()) {
            try {
                return this.mICCardReader.AT24CXX_read(i, i2, i3, bArr, iArr);
            } catch (TelpoException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public int AT24CXX_write(int i, int i2, byte[] bArr, int i3) {
        if (SystemUtil.getDeviceType() == StringUtil.DeviceModelEnum.TPS900.ordinal()) {
            try {
                return this.mICCardReader.AT24CXX_write(i, i2, bArr, i3);
            } catch (TelpoException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public boolean open() {
        Log.d("taggg", "open >>>>>>>");
        int deviceType = SystemUtil.getDeviceType();
        int i = 3;
        if (deviceType == StringUtil.DeviceModelEnum.TPS900.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS390P.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS390L.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS900MB.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS320.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS450CIC.ordinal() || "TPS580P".equals(SystemUtil.getInternalModel())) {
            try {
                if ("TPS530".contains(SystemUtil.getInternalModel()) && !"S5".equals(SystemUtil.getInternalModel())) {
                    this.mICCardReader.open(this.calSlot);
                } else if (deviceType == StringUtil.DeviceModelEnum.TPS900.ordinal() && this.cardType == 3) {
                    this.mICCardReader.open(256);
                } else {
                    this.mICCardReader.open(this.mSlot);
                }
                return true;
            } catch (TelpoException e) {
                e.printStackTrace();
                return false;
            }
        }
        int iCCReaderType = SystemUtil.getICCReaderType();
        Log.d("tagg", "CardReader type is:" + iCCReaderType + " mSlot is:" + this.mSlot);
        int i2 = this.mSlot;
        if ((i2 == 0 && (iCCReaderType == 2 || iCCReaderType == 1 || iCCReaderType == 0)) || ((i2 == 1 && (deviceType == StringUtil.DeviceModelEnum.TPS350_4G.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS350L.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS573.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS450.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS450C.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS365.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS360C.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS360IC.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS360A.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS470.ordinal())) || ((this.mSlot == 2 && deviceType == StringUtil.DeviceModelEnum.TPS573.ordinal()) || (this.mSlot == 2 && deviceType == StringUtil.DeviceModelEnum.TPS365.ordinal())))) {
            Log.e("tagg", "open_device-------");
            if (open_device(this.cardType, this.mSlot) < 0) {
                Log.d("TAG", "open_device(cardType, mSlot) < 0");
                return false;
            }
            i = iCCReaderType;
        } else {
            String internalModel = SystemUtil.getInternalModel();
            if (deviceType == StringUtil.DeviceModelEnum.TPS390U.ordinal()) {
                device_power(1);
            } else if (deviceType == StringUtil.DeviceModelEnum.TPS360U.ordinal()) {
                FingerPrint.iccardPower(1);
                sleepThread(PathInterpolatorCompat.MAX_NUM_POINTS);
            } else if ("C10P".equals(internalModel) || "C10Y".equals(internalModel) || "C10T".equals(internalModel)) {
                PowerUtil.c10ioctl(2, 1);
            }
            if ("C10P".equals(internalModel) || "C10Y".equals(internalModel) || "C10T".equals(internalModel)) {
                long currentTimeMillis = System.currentTimeMillis();
                boolean z = false;
                while (!z) {
                    z = checkReader(this.context);
                    sleepThread(50);
                    if (System.currentTimeMillis() - currentTimeMillis > 5000) {
                        break;
                    }
                }
                Log.d("tagg", "hasReader[" + z + "] time[" + (System.currentTimeMillis() - currentTimeMillis) + "]");
            }
            if ((deviceType == StringUtil.DeviceModelEnum.TPS980P.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS716.ordinal()) && this.mSlot == 2) {
                return false;
            }
            this.usbManager = (UsbManager) this.context.getSystemService("usb");
            UsbDevice usbDevice = getUsbDevice();
            this.usbDev = usbDevice;
            if (usbDevice == null) {
                Log.e(TAG, "get usb manager failed");
                if ("C10P".equals(internalModel) || "C10Y".equals(internalModel) || "C10T".equals(internalModel)) {
                    PowerUtil.c10ioctl(2, 0);
                    sleepThread(ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION);
                }
                return false;
            }
            this.reader = null;
            this.myDev = new HardwareInterface(HWType.eUSB, this.context);
            HandlerThread handlerThread = new HandlerThread("card reader");
            this.handlerThread = handlerThread;
            handlerThread.start();
            this.handler = new Handler(this.handlerThread.getLooper());
            toRegisterReceiver();
            this.usbManager.requestPermission(this.usbDev, this.permissionIntent);
            synchronized (this.lock) {
                if (this.reader == null) {
                    try {
                        this.lock.wait(30000L);
                    } catch (InterruptedException e2) {
                        e2.printStackTrace();
                    }
                }
            }
            this.context.unregisterReceiver(this.mReceiver);
            this.handlerThread.quit();
            this.handlerThread = null;
            if (this.reader == null) {
                Log.e(TAG, "reader is null");
                this.myDev.Close();
                this.myDev = null;
                this.usbManager = null;
                this.usbDev = null;
                if ("C10P".equals(internalModel) || "C10Y".equals(internalModel) || "C10T".equals(internalModel)) {
                    PowerUtil.c10ioctl(2, 0);
                    sleepThread(ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION);
                }
                return false;
            }
        }
        this.reader_type = i;
        return true;
    }

    public boolean open(int i) {
        int deviceType = SystemUtil.getDeviceType();
        if (deviceType == StringUtil.DeviceModelEnum.TPS900.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS390P.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS390L.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS900MB.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS320.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS360IC.ordinal()) {
            try {
                this.mICCardReader.open(this.mSlot);
                return true;
            } catch (TelpoException e) {
                e.printStackTrace();
                return false;
            }
        }
        int i2 = 3;
        int iCCReaderType = i == 0 ? SystemUtil.getICCReaderType() : 3;
        Log.d("tagg", "CardReader type is:" + iCCReaderType);
        int i3 = this.mSlot;
        if ((i3 == 0 && (iCCReaderType == 2 || iCCReaderType == 1 || iCCReaderType == 0)) || (i3 == 1 && (deviceType == StringUtil.DeviceModelEnum.TPS350_4G.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS350L.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS450.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS365.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS360C.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS360A.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS470.ordinal()))) {
            Log.e("TAG", "AU9560_GCS-------");
            if (open_device(this.cardType, this.mSlot) < 0) {
                Log.d("TAG", "open_device(cardType, mSlot) < 0");
                return false;
            }
            i2 = iCCReaderType;
        } else {
            if (deviceType == StringUtil.DeviceModelEnum.TPS390U.ordinal()) {
                device_power(1);
            }
            this.usbManager = (UsbManager) this.context.getSystemService("usb");
            UsbDevice usbDevice = getUsbDevice();
            this.usbDev = usbDevice;
            if (usbDevice == null) {
                Log.e(TAG, "get usb manager failed");
                return false;
            }
            this.reader = null;
            this.myDev = new HardwareInterface(HWType.eUSB, this.context);
            HandlerThread handlerThread = new HandlerThread("card reader");
            this.handlerThread = handlerThread;
            handlerThread.start();
            this.handler = new Handler(this.handlerThread.getLooper());
            toRegisterReceiver();
            this.usbManager.requestPermission(this.usbDev, this.permissionIntent);
            synchronized (this.lock) {
                if (this.reader == null) {
                    try {
                        this.lock.wait(30000L);
                    } catch (InterruptedException e2) {
                        e2.printStackTrace();
                    }
                }
            }
            this.context.unregisterReceiver(this.mReceiver);
            this.handlerThread.quit();
            this.handlerThread = null;
            if (this.reader == null) {
                Log.e(TAG, "reader is null");
                this.myDev.Close();
                this.myDev = null;
                this.usbManager = null;
                this.usbDev = null;
                return false;
            }
        }
        this.reader_type = i2;
        return true;
    }

    public UsbDevice getAT88SC153Device() {
        if (this.usbManager == null) {
            this.usbManager = (UsbManager) this.context.getSystemService("usb");
        }
        UsbDevice usbDevice = getUsbDevice();
        Log.d("TAG", "getAT88SC153Device:pid:" + usbDevice.getProductId() + "device name:" + usbDevice.getDeviceName());
        return usbDevice;
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x005f  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x006e  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0072  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean close() {
        /*
            r6 = this;
            int r0 = com.telpo.tps550.api.util.SystemUtil.getDeviceType()
            com.telpo.tps550.api.util.StringUtil$DeviceModelEnum r1 = com.telpo.tps550.api.util.StringUtil.DeviceModelEnum.TPS900
            int r1 = r1.ordinal()
            r2 = 1
            r3 = 0
            if (r0 == r1) goto Lb1
            com.telpo.tps550.api.util.StringUtil$DeviceModelEnum r1 = com.telpo.tps550.api.util.StringUtil.DeviceModelEnum.TPS390P
            int r1 = r1.ordinal()
            if (r0 == r1) goto Lb1
            com.telpo.tps550.api.util.StringUtil$DeviceModelEnum r1 = com.telpo.tps550.api.util.StringUtil.DeviceModelEnum.TPS390L
            int r1 = r1.ordinal()
            if (r0 == r1) goto Lb1
            com.telpo.tps550.api.util.StringUtil$DeviceModelEnum r1 = com.telpo.tps550.api.util.StringUtil.DeviceModelEnum.TPS900MB
            int r1 = r1.ordinal()
            if (r0 == r1) goto Lb1
            com.telpo.tps550.api.util.StringUtil$DeviceModelEnum r1 = com.telpo.tps550.api.util.StringUtil.DeviceModelEnum.TPS320
            int r1 = r1.ordinal()
            if (r0 == r1) goto Lb1
            com.telpo.tps550.api.util.StringUtil$DeviceModelEnum r1 = com.telpo.tps550.api.util.StringUtil.DeviceModelEnum.TPS450CIC
            int r1 = r1.ordinal()
            if (r0 == r1) goto Lb1
            java.lang.String r1 = com.telpo.tps550.api.util.SystemUtil.getInternalModel()
            java.lang.String r4 = "TPS580P"
            boolean r1 = r4.equals(r1)
            if (r1 == 0) goto L44
            goto Lb1
        L44:
            int r1 = r6.reader_type
            r4 = 2
            if (r1 == r4) goto L9f
            if (r1 == r2) goto L9f
            if (r1 != 0) goto L4e
            goto L9f
        L4e:
            amlib.ccid.Reader r1 = r6.reader
            if (r1 == 0) goto L5b
        L52:
            amlib.ccid.Reader r1 = r6.reader
            int r1 = r1.close()
            r5 = 3
            if (r1 == r5) goto L52
        L5b:
            amlib.hw.HardwareInterface r1 = r6.myDev
            if (r1 == 0) goto L62
            r1.Close()
        L62:
            java.lang.String r1 = com.telpo.tps550.api.util.SystemUtil.getInternalModel()
            com.telpo.tps550.api.util.StringUtil$DeviceModelEnum r5 = com.telpo.tps550.api.util.StringUtil.DeviceModelEnum.TPS390U
            int r5 = r5.ordinal()
            if (r0 != r5) goto L72
            r6.device_power(r3)
            goto Lad
        L72:
            com.telpo.tps550.api.util.StringUtil$DeviceModelEnum r5 = com.telpo.tps550.api.util.StringUtil.DeviceModelEnum.TPS360U
            int r5 = r5.ordinal()
            if (r0 != r5) goto L7e
            com.telpo.tps550.api.fingerprint.FingerPrint.iccardPower(r3)
            goto Lad
        L7e:
            java.lang.String r0 = "C10P"
            boolean r0 = r0.equals(r1)
            if (r0 != 0) goto L96
            java.lang.String r0 = "C10Y"
            boolean r0 = r0.equals(r1)
            if (r0 != 0) goto L96
            java.lang.String r0 = "C10T"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto Lad
        L96:
            com.telpo.tps550.api.util.PowerUtil.c10ioctl(r4, r3)
            r0 = 200(0xc8, float:2.8E-43)
            r6.sleepThread(r0)
            goto Lad
        L9f:
            java.lang.String r0 = "tagg"
            java.lang.String r1 = "CardReader > close() > serial reader"
            android.util.Log.d(r0, r1)
            int r0 = close_device()
            if (r0 >= 0) goto Lad
            return r3
        Lad:
            r0 = -1
            r6.reader_type = r0
            return r2
        Lb1:
            com.telpo.tps550.api.reader.ICCardReader r0 = r6.mICCardReader     // Catch: com.telpo.tps550.api.TelpoException -> Lb9
            int r1 = r6.mSlot     // Catch: com.telpo.tps550.api.TelpoException -> Lb9
            r0.close(r1)     // Catch: com.telpo.tps550.api.TelpoException -> Lb9
            return r2
        Lb9:
            r0 = move-exception
            r0.printStackTrace()
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.telpo.tps550.api.reader.CardReader.close():boolean");
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x0052  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x005d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean close(int r5) {
        /*
            r4 = this;
            int r5 = com.telpo.tps550.api.util.SystemUtil.getDeviceType()
            com.telpo.tps550.api.util.StringUtil$DeviceModelEnum r0 = com.telpo.tps550.api.util.StringUtil.DeviceModelEnum.TPS900
            int r0 = r0.ordinal()
            r1 = 1
            r2 = 0
            if (r5 == r0) goto L6c
            com.telpo.tps550.api.util.StringUtil$DeviceModelEnum r0 = com.telpo.tps550.api.util.StringUtil.DeviceModelEnum.TPS390P
            int r0 = r0.ordinal()
            if (r5 == r0) goto L6c
            com.telpo.tps550.api.util.StringUtil$DeviceModelEnum r0 = com.telpo.tps550.api.util.StringUtil.DeviceModelEnum.TPS390L
            int r0 = r0.ordinal()
            if (r5 == r0) goto L6c
            com.telpo.tps550.api.util.StringUtil$DeviceModelEnum r0 = com.telpo.tps550.api.util.StringUtil.DeviceModelEnum.TPS900MB
            int r0 = r0.ordinal()
            if (r5 == r0) goto L6c
            com.telpo.tps550.api.util.StringUtil$DeviceModelEnum r0 = com.telpo.tps550.api.util.StringUtil.DeviceModelEnum.TPS320
            int r0 = r0.ordinal()
            if (r5 == r0) goto L6c
            com.telpo.tps550.api.util.StringUtil$DeviceModelEnum r0 = com.telpo.tps550.api.util.StringUtil.DeviceModelEnum.TPS360IC
            int r0 = r0.ordinal()
            if (r5 != r0) goto L37
            goto L6c
        L37:
            int r0 = r4.reader_type
            r3 = 2
            if (r0 == r3) goto L61
            if (r0 == r1) goto L61
            if (r0 != 0) goto L41
            goto L61
        L41:
            amlib.ccid.Reader r0 = r4.reader
            if (r0 == 0) goto L4e
        L45:
            amlib.ccid.Reader r0 = r4.reader
            int r0 = r0.close()
            r3 = 3
            if (r0 == r3) goto L45
        L4e:
            amlib.hw.HardwareInterface r0 = r4.myDev
            if (r0 == 0) goto L55
            r0.Close()
        L55:
            com.telpo.tps550.api.util.StringUtil$DeviceModelEnum r0 = com.telpo.tps550.api.util.StringUtil.DeviceModelEnum.TPS390U
            int r0 = r0.ordinal()
            if (r5 != r0) goto L68
            r4.device_power(r2)
            goto L68
        L61:
            int r5 = close_device()
            if (r5 >= 0) goto L68
            return r2
        L68:
            r5 = -1
            r4.reader_type = r5
            return r1
        L6c:
            com.telpo.tps550.api.reader.ICCardReader r5 = r4.mICCardReader     // Catch: com.telpo.tps550.api.TelpoException -> L74
            int r0 = r4.mSlot     // Catch: com.telpo.tps550.api.TelpoException -> L74
            r5.close(r0)     // Catch: com.telpo.tps550.api.TelpoException -> L74
            return r1
        L74:
            r5 = move-exception
            r5.printStackTrace()
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.telpo.tps550.api.reader.CardReader.close(int):boolean");
    }

    public boolean iccPowerOn() {
        int deviceType = SystemUtil.getDeviceType();
        Log.d("tagg", "iccPowerOn > reader_type:" + this.reader_type);
        if (deviceType == StringUtil.DeviceModelEnum.TPS900.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS390P.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS390L.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS900MB.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS320.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS450CIC.ordinal() || "TPS580P".equals(SystemUtil.getInternalModel())) {
            try {
                Log.d("tagg", "iccPowerOn detect/power_on >> mSlot[" + this.mSlot + "]");
                if (this.mICCardReader.detect(this.mSlot, 500) != 0) {
                    return false;
                }
                this.mICCardReader.power_on(this.mSlot);
                return true;
            } catch (TelpoException e) {
                e.printStackTrace();
                return false;
            }
        }
        int i = this.reader_type;
        if (i == 2 || i == 1 || i == 0) {
            byte[] icc_power_on = icc_power_on(this.cardType);
            this.mATR = icc_power_on;
            if (icc_power_on == null) {
                Log.d("tagg", "mATR == null");
                return false;
            }
        } else if (!usbReaderIccPowerOn()) {
            String internalModel = SystemUtil.getInternalModel();
            if (!"C10P".equals(internalModel) && !"C10Y".equals(internalModel) && !"C10T".equals(internalModel)) {
                return false;
            }
            close();
            sleepThread(ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION);
            open();
            sleepThread(ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION);
            if (!usbReaderIccPowerOn()) {
                return false;
            }
        }
        return true;
    }

    private void sleepThread(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean usbReaderIccPowerOn() {
        byte[] bArr = new byte[1];
        int cardStatus = this.reader.getCardStatus(bArr);
        Reader reader = this.reader;
        if (reader == null || cardStatus != 0) {
            Log.d("tagg", "getCardStatus(status) != READER_SUCCESSFUL");
            return false;
        } else if (bArr[0] == 2) {
            Log.d("tagg", "card absent");
            return false;
        } else {
            int power = reader.setPower(1);
            if (power == 0) {
                return true;
            }
            Log.e(TAG, "setPower failed: " + power);
            return false;
        }
    }

    public void escape4057() {
        int[] iArr = new int[1];
        byte[] bytes = StringUtil.toBytes("4057020013000000");
        byte[] bArr = new byte[512];
        Reader reader = this.reader;
        Log.d("tagg", "resultSend[" + (reader != null ? reader.escape(bytes, bytes.length, bArr, iArr) : -1) + "]");
    }

    public boolean iccPowerOn(int i) {
        int deviceType = SystemUtil.getDeviceType();
        if (deviceType == StringUtil.DeviceModelEnum.TPS900.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS390P.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS390L.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS900MB.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS320.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS360IC.ordinal()) {
            try {
                if (this.mICCardReader.detect(this.mSlot, 500) != 0) {
                    return false;
                }
                this.mICCardReader.power_on(this.mSlot);
                return true;
            } catch (TelpoException e) {
                e.printStackTrace();
                return false;
            }
        }
        int i2 = this.reader_type;
        if (i2 == 2 || i2 == 1 || i2 == 0) {
            byte[] icc_power_on = icc_power_on(this.cardType);
            this.mATR = icc_power_on;
            if (icc_power_on == null) {
                return false;
            }
        } else {
            byte[] bArr = new byte[1];
            Reader reader = this.reader;
            if (reader == null || reader.getCardStatus(bArr) != 0 || bArr[0] == 2) {
                return false;
            }
            int power = this.reader.setPower(1);
            if (power != 0) {
                Log.e(TAG, "setPower failed: " + power);
                return false;
            }
        }
        return true;
    }

    public boolean iccPowerOff() {
        int deviceType = SystemUtil.getDeviceType();
        if (deviceType == StringUtil.DeviceModelEnum.TPS900.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS390P.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS390L.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS900MB.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS320.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS450CIC.ordinal() || "TPS580P".equals(SystemUtil.getInternalModel())) {
            try {
                this.mICCardReader.power_off(this.mSlot);
                return true;
            } catch (TelpoException e) {
                e.printStackTrace();
                return false;
            }
        }
        int i = this.reader_type;
        if (i == 2 || i == 1 || i == 0) {
            if (icc_power_off() < 0) {
                return false;
            }
        } else {
            byte[] bArr = new byte[1];
            Reader reader = this.reader;
            if (reader == null || reader.getCardStatus(bArr) != 0 || bArr[0] == 2 || this.reader.setPower(-1) != 0) {
                return false;
            }
        }
        this.mATR = null;
        this.correct_psc_verification = false;
        return true;
    }

    public boolean iccPowerOff(int i) {
        int deviceType = SystemUtil.getDeviceType();
        if (deviceType == StringUtil.DeviceModelEnum.TPS900.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS390P.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS390L.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS900MB.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS320.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS360IC.ordinal()) {
            try {
                this.mICCardReader.power_off(this.mSlot);
                return true;
            } catch (TelpoException e) {
                e.printStackTrace();
                return false;
            }
        }
        int i2 = this.reader_type;
        if (i2 == 2 || i2 == 1 || i2 == 0) {
            if (icc_power_off() < 0) {
                return false;
            }
        } else {
            byte[] bArr = new byte[1];
            Reader reader = this.reader;
            if (reader == null || reader.getCardStatus(bArr) != 0 || bArr[0] == 2 || this.reader.setPower(-1) != 0) {
                return false;
            }
        }
        this.mATR = null;
        this.correct_psc_verification = false;
        return true;
    }

    public int getICCStatus() {
        int i = this.reader_type;
        if (i == 2 || i == 1 || i == 0) {
            int i2 = get_card_status();
            if (i2 >= 0) {
                return i2;
            }
        } else {
            byte[] bArr = new byte[1];
            if (this.reader.getCardStatus(bArr) == 0) {
                if (bArr[0] == 0) {
                    return 0;
                }
                if (bArr[0] == 1) {
                    return 1;
                }
            }
        }
        return 2;
    }

    public boolean isICCPresent() {
        int deviceType = SystemUtil.getDeviceType();
        if (deviceType == StringUtil.DeviceModelEnum.TPS900.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS390P.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS390L.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS900MB.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS320.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS450CIC.ordinal() || "TPS580P".equals(SystemUtil.getInternalModel())) {
            try {
                return this.mICCardReader.detect(this.mSlot, 500) == 0;
            } catch (TelpoException e) {
                e.printStackTrace();
                return false;
            }
        }
        int i = this.reader_type;
        if (i == 2 || i == 1 || i == 0) {
            int i2 = get_card_status();
            if (i2 == 0 || i2 == 1) {
                return true;
            }
        } else {
            byte[] bArr = new byte[1];
            if (this.reader.getCardStatus(bArr) == 0 && bArr[0] != 2) {
                return true;
            }
        }
        return false;
    }

    public boolean isICCPresent(int i) {
        int deviceType = SystemUtil.getDeviceType();
        if (deviceType == StringUtil.DeviceModelEnum.TPS900.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS390P.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS390L.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS900MB.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS320.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS360IC.ordinal()) {
            try {
                return this.mICCardReader.detect(this.mSlot, 500) == 0;
            } catch (TelpoException e) {
                e.printStackTrace();
                return false;
            }
        }
        int i2 = this.reader_type;
        if (i2 == 2 || i2 == 1 || i2 == 0) {
            int i3 = get_card_status();
            if (i3 == 0 || i3 == 1) {
                return true;
            }
        } else {
            byte[] bArr = new byte[1];
            if (this.reader.getCardStatus(bArr) == 0 && bArr[0] != 2) {
                return true;
            }
        }
        return false;
    }

    public String getATRString() {
        int deviceType = SystemUtil.getDeviceType();
        if (deviceType == StringUtil.DeviceModelEnum.TPS900.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS390P.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS390L.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS900MB.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS320.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS450CIC.ordinal() || "TPS580P".equals(SystemUtil.getInternalModel())) {
            try {
                return this.mICCardReader.getAtr(this.mSlot);
            } catch (TelpoException e) {
                e.printStackTrace();
                return null;
            }
        }
        int i = this.reader_type;
        if (i == 2 || i == 1 || i == 0) {
            byte[] bArr = this.mATR;
            if (bArr != null) {
                return StringUtil.toHexString(bArr);
            }
            return null;
        }
        return this.reader.getAtrString();
    }

    public String getATRString(int i) {
        int deviceType = SystemUtil.getDeviceType();
        if (deviceType == StringUtil.DeviceModelEnum.TPS900.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS390P.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS390L.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS900MB.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS320.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS360IC.ordinal()) {
            try {
                return this.mICCardReader.getAtr(this.mSlot);
            } catch (TelpoException e) {
                e.printStackTrace();
                return null;
            }
        }
        int i2 = this.reader_type;
        if (i2 == 2 || i2 == 1 || i2 == 0) {
            byte[] bArr = this.mATR;
            if (bArr != null) {
                return StringUtil.toHexString(bArr);
            }
            return null;
        }
        return this.reader.getAtrString();
    }

    public int getCardType() {
        String aTRString = getATRString();
        if (aTRString == null) {
            return -1;
        }
        String replace = aTRString.replace(" ", "");
        Log.i(TAG, "ATR: " + replace);
        if (replace.contains("A2131091")) {
            return 2;
        }
        return replace.contains("92231091") ? 3 : -1;
    }

    public boolean switchMode(int i) {
        int i2 = this.reader_type;
        return (i2 == 2 || i2 == 1 || i2 == 0) ? switch_mode(i) == 0 : i == 1 ? this.reader.switchMode((byte) 1) == 0 : i == 2 ? this.reader.switchMode((byte) 4) == 0 : i == 3 && this.reader.switchMode((byte) 3) == 0;
    }

    private UsbDevice getUsbDevice() {
        String str;
        int deviceType = SystemUtil.getDeviceType();
        SystemUtil.getInternalModel();
        if (deviceType == StringUtil.DeviceModelEnum.TPS510A_NHW.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS510D.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS510P.ordinal()) {
            String str2 = ShellUtils.execCommand("cat /sys/kernel/debug/usb/devices", false).successMsg;
            searchAllIndex(str2, "Port=", 1);
            searchAllIndex(str2, "Vendor=", 2);
            checkPort(this.portNum, this.productNum);
            String[] strArr = this.readerNum;
            if (strArr[0] == null || !strArr[0].equals("03")) {
                String[] strArr2 = this.readerNum;
                if (strArr2[1] == null || !strArr2[1].equals("03")) {
                    String[] strArr3 = this.readerNum;
                    if (strArr3[2] == null || !strArr3[2].equals("03")) {
                        String[] strArr4 = this.readerNum;
                        if (strArr4[3] == null || !strArr4[3].equals("03")) {
                            Log.d("TAG", "old usb_utils");
                            str = UsbUtil.getUsbDevice(this.mSlot);
                        }
                    }
                }
            }
            Log.d("TAG", "new usb_utils");
            str = UsbUtil.getUsbDevicehub(this.mSlot, 1);
        } else {
            str = UsbUtil.getUsbDevice(this.mSlot);
        }
        Log.d("tagg", "getUsbDevice() >> deviceName[" + str + "]");
        if (str != null) {
            for (UsbDevice usbDevice : this.usbManager.getDeviceList().values()) {
                if (str.equals(usbDevice.getDeviceName())) {
                    switchPsam();
                    return usbDevice;
                }
            }
            return null;
        }
        return null;
    }

    private boolean checkReader(Context context) {
        try {
            for (UsbDevice usbDevice : ((UsbManager) context.getSystemService("usb")).getDeviceList().values()) {
                int productId = usbDevice.getProductId();
                int vendorId = usbDevice.getVendorId();
                if (productId == 38208 && vendorId == 1423) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void searchAllIndex(String str, String str2, int i) {
        int indexOf = str.indexOf(str2);
        int i2 = -1;
        while (indexOf != -1) {
            i2++;
            if (i == 1) {
                this.portNum[i2] = str.substring(indexOf + 5, indexOf + 7);
            } else if (i == 2) {
                this.productNum[i2] = str.substring(indexOf, str.indexOf("Rev=", indexOf) - 1);
            }
            indexOf = str.indexOf(str2, indexOf + 1);
        }
    }

    private void checkPort(String[] strArr, String[] strArr2) {
        int i = -1;
        for (int i2 = 0; i2 < 20; i2++) {
            String[] strArr3 = this.productNum;
            if (strArr3[i2] != null && strArr3[i2].equals("Vendor=058f ProdID=9540")) {
                i++;
                this.readerNum[i] = this.portNum[i2];
                Log.d("tagg", "readnum[]:" + this.readerNum[i]);
            }
        }
    }

    private void toRegisterReceiver() {
        this.permissionIntent = PendingIntent.getBroadcast(this.context, 0, new Intent(ACTION_USB_PERMISSION), 0);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_USB_PERMISSION);
        intentFilter.addAction("android.hardware.usb.action.USB_DEVICE_DETACHED");
        this.context.registerReceiver(this.mReceiver, intentFilter, null, this.handler);
    }

    private void readerPoweron() {
        String[] strArr = {"/sys/devices/platform/battery/GPIO30_PIN", "/sys/devices/platform/battery/GPIO31_PIN", "/sys/devices/platform/battery/GPIO142_PIN", "/sys/devices/platform/battery/GPIO145_PIN"};
        for (int i = 0; i < 4; i++) {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(strArr[i]);
                fileOutputStream.write(49);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        sleepThread(100);
    }

    private void readerPoweroff() {
        String[] strArr = {"/sys/devices/platform/battery/GPIO30_PIN", "/sys/devices/platform/battery/GPIO31_PIN", "/sys/devices/platform/battery/GPIO142_PIN", "/sys/devices/platform/battery/GPIO145_PIN"};
        for (int i = 0; i < 4; i++) {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(strArr[i]);
                fileOutputStream.write(48);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        sleepThread(100);
    }

    private int switchPsam() {
        int deviceType = SystemUtil.getDeviceType();
        if (deviceType == StringUtil.DeviceModelEnum.TPS510A.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS510A_NHW.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS510D.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS450C.ordinal()) {
            int i = this.mSlot;
            if (i == 2) {
                return telpo_switch_psam(2);
            }
            if (i != 3) {
                return 0;
            }
            return telpo_switch_psam(3);
        } else if (deviceType == StringUtil.DeviceModelEnum.TPS613.ordinal()) {
            int i2 = this.mSlot;
            if (i2 == 1) {
                return telpo_switch_psam(1);
            }
            if (i2 != 2) {
                return 0;
            }
            return telpo_switch_psam(2);
        } else if (deviceType == StringUtil.DeviceModelEnum.TPS390U.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS980P.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS716.ordinal() || "TPS980".equals(SystemUtil.getInternalModel())) {
            return 0;
        } else {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream("/sys/class/hdxio/psam_select");
                int i3 = this.mSlot;
                if (i3 == 2) {
                    if (deviceType == StringUtil.DeviceModelEnum.FFP2.ordinal()) {
                        fileOutputStream.write(50);
                    } else {
                        fileOutputStream.write(49);
                    }
                } else if (i3 == 3) {
                    fileOutputStream.write(51);
                } else {
                    fileOutputStream.write(49);
                }
                fileOutputStream.flush();
                fileOutputStream.close();
                return 0;
            } catch (Exception unused) {
                Log.e(TAG, "switchPsam() > switch slot[" + this.mSlot + "] -1");
                return -1;
            }
        }
    }

    static {
        int deviceType = SystemUtil.getDeviceType();
        if (deviceType == StringUtil.DeviceModelEnum.TPS900.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS390P.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS390L.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS900MB.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS320.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS450CIC.ordinal() || "TPS580P".equals(SystemUtil.getInternalModel())) {
            return;
        }
        System.loadLibrary("card_reader");
    }
}
