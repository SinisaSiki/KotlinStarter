package com.telpo.tps550.api.idcard;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Log;
import android.widget.ImageView;
import com.zkteco.android.biometric.core.device.TransportType;
import com.zkteco.android.biometric.core.utils.ToolUtils;
import com.zkteco.android.biometric.nidfpsensor.NIDFPFactory;
import com.zkteco.android.biometric.nidfpsensor.NIDFPSensor;
import com.zkteco.android.biometric.nidfpsensor.exception.NIDFPException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/* loaded from: classes.dex */
public class FingerReader {
    public static final int PID = 770;
    public static final int PID_110 = 772;
    private static final String TAG = "FingerReader";
    public static final int VID = 6997;
    private static volatile FingerReader instance;
    private Context context;
    private byte[] fringerprint;
    private byte[] fringerprint_one;
    private byte[] fringerprint_two;
    private byte[] mBufImage;
    private float matchingvalue_one;
    private float matchingvalue_two;
    private byte[] returnByte;
    private byte returnScore;
    private boolean mbStart = false;
    private boolean mbStop = true;
    private NIDFPSensor mNIDFPSensor = null;
    private ZKWorkThread mWorkThread = null;
    private UsbManager musbManager = null;
    private UsbDevice usbDevice = null;
    private Bitmap fingerBitmap = null;
    private boolean verifyFinger = false;
    private int now_PID = 0;
    private CountDownLatch countdownLatch = new CountDownLatch(1);
    private final String ACTION_USB_PERMISSION = "com.zkteco.idfprdemo.USB_PERMISSION";
    private int isMatchSuccess = 0;

    private FingerReader() {
    }

    public static FingerReader getInstance(Context context) {
        if (instance == null) {
            synchronized (FingerReader.class) {
                if (instance == null) {
                    instance = new FingerReader(context);
                }
            }
        }
        return instance;
    }

    private FingerReader(Context context) {
        this.context = context;
    }

    public int isMatchSuccess() {
        return this.isMatchSuccess;
    }

    public boolean openFingerReader() {
        return initFingerReader(new byte[1024]);
    }

    public boolean openFingerReader(byte[] bArr) {
        return initFingerReader(bArr);
    }

    public void getFingerDetect() {
        getFingerDetect(null);
    }

    public void getFingerDetect(ImageView imageView) {
        NIDFPSensor nIDFPSensor;
        if (this.mbStart) {
            return;
        }
        try {
            nIDFPSensor = this.mNIDFPSensor;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (nIDFPSensor != null) {
            nIDFPSensor.open(0);
            this.mBufImage = new byte[this.mNIDFPSensor.getFpImgWidth() * this.mNIDFPSensor.getFpImgHeight()];
            ZKWorkThread zKWorkThread = new ZKWorkThread(imageView);
            this.mWorkThread = zKWorkThread;
            zKWorkThread.start();
            this.mbStart = true;
            return;
        }
        Log.d(TAG, "FingerReader open failed");
    }

    public Bitmap getFingerBitmap() {
        return this.fingerBitmap;
    }

    public void verifyFinger(boolean z) {
        this.verifyFinger = z;
    }

    public void closeFingerReader() {
        NIDFPSensor nIDFPSensor = this.mNIDFPSensor;
        if (nIDFPSensor != null) {
            if (this.mbStart) {
                this.mbStop = true;
                try {
                    this.countdownLatch.await(10000L, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    this.mNIDFPSensor.close(0);
                    NIDFPFactory.destroy(this.mNIDFPSensor);
                } catch (NIDFPException e2) {
                    e2.printStackTrace();
                }
            } else {
                try {
                    nIDFPSensor.close(0);
                    NIDFPFactory.destroy(this.mNIDFPSensor);
                } catch (NIDFPException e3) {
                    e3.printStackTrace();
                }
            }
            this.mbStart = false;
        }
    }

    public void setReaderFingerByte(byte[] bArr) {
        this.returnByte = bArr;
    }

    public byte[] getReaderFingerByte() {
        return this.returnByte;
    }

    public byte getQualityScore() {
        return this.returnScore;
    }

    public int fingerprintCompare(byte[] bArr, byte[] bArr2, byte b) {
        if (decodeSrcFingerPrint(bArr) && this.returnScore > b) {
            Log.d(TAG, "returnScore > score");
            float ImageMatch = this.mNIDFPSensor.ImageMatch(0, bArr2, this.fringerprint_one);
            float ImageMatch2 = this.mNIDFPSensor.ImageMatch(0, bArr2, this.fringerprint_two);
            Log.d(TAG, "ret 1:" + ImageMatch);
            Log.d(TAG, "ret 2:" + ImageMatch2);
            if (ImageMatch == 1.0d || ImageMatch2 == 1.0d) {
                this.isMatchSuccess = 1;
                return 1;
            }
            this.isMatchSuccess = 2;
            return 2;
        }
        return -1;
    }

    /* loaded from: classes.dex */
    public class ZKWorkThread extends Thread {
        private ImageView imageView;

        public ZKWorkThread(ImageView imageView) {
            FingerReader.this = r1;
            this.imageView = imageView;
            r1.isMatchSuccess = 0;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            super.run();
            FingerReader.this.mbStop = false;
            while (!FingerReader.this.mbStop) {
                try {
                    Thread.sleep(50L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    FingerReader.this.mNIDFPSensor.GetFPRawData(0, FingerReader.this.mBufImage);
                    FingerReader fingerReader = FingerReader.this;
                    fingerReader.setReaderFingerByte(fingerReader.mBufImage);
                    if (FingerReader.this.fringerprint != null) {
                        byte[] bArr = new byte[1];
                        FingerReader.this.mNIDFPSensor.getQualityScore(FingerReader.this.mBufImage, bArr);
                        byte b = bArr[0];
                        FingerReader.this.returnScore = b;
                        try {
                            FingerReader fingerReader2 = FingerReader.this;
                            fingerReader2.fingerBitmap = ToolUtils.renderCroppedGreyScaleBitmap(fingerReader2.mBufImage, FingerReader.this.mNIDFPSensor.getFpImgWidth(), FingerReader.this.mNIDFPSensor.getFpImgHeight());
                            ImageView imageView = this.imageView;
                            if (imageView != null) {
                                imageView.post(new Runnable() { // from class: com.telpo.tps550.api.idcard.FingerReader.ZKWorkThread.1
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        ZKWorkThread.this.imageView.setImageBitmap(FingerReader.this.fingerBitmap);
                                    }
                                });
                            }
                        } catch (Exception unused) {
                        }
                        if (FingerReader.this.verifyFinger && b > 80) {
                            float ImageMatch = FingerReader.this.mNIDFPSensor.ImageMatch(0, FingerReader.this.mBufImage, FingerReader.this.fringerprint_one);
                            float ImageMatch2 = FingerReader.this.mNIDFPSensor.ImageMatch(0, FingerReader.this.mBufImage, FingerReader.this.fringerprint_two);
                            FingerReader.this.mNIDFPSensor.ImageMatch(0, FingerReader.this.mBufImage, FingerReader.this.fringerprint);
                            FingerReader.this.matchingvalue_one = ImageMatch;
                            FingerReader.this.matchingvalue_two = ImageMatch2;
                            if (FingerReader.this.matchingvalue_one == 1.0d || FingerReader.this.matchingvalue_two == 1.0d) {
                                FingerReader.this.isMatchSuccess = 1;
                                if (FingerReader.this.mNIDFPSensor != null) {
                                    FingerReader.this.closeFingerReader();
                                    NIDFPFactory.destroy(FingerReader.this.mNIDFPSensor);
                                }
                            } else {
                                FingerReader.this.isMatchSuccess = 2;
                                if (FingerReader.this.mNIDFPSensor != null) {
                                    FingerReader.this.closeFingerReader();
                                    NIDFPFactory.destroy(FingerReader.this.mNIDFPSensor);
                                }
                            }
                        }
                    }
                    FingerReader.this.countdownLatch.countDown();
                } catch (NIDFPException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    private boolean initFingerReader(byte[] bArr) {
        if (this.mbStart) {
            Log.d(TAG, "设备已连接，请先断开连接");
            return false;
        }
        this.musbManager = (UsbManager) this.context.getSystemService("usb");
        if (bArr == null) {
            return false;
        }
        this.fringerprint = bArr;
        if (bArr.length == 1024) {
            this.fringerprint_one = Arrays.copyOfRange(bArr, 0, 512);
            this.fringerprint_two = Arrays.copyOfRange(bArr, 512, bArr.length);
        }
        if (checkFingerReader()) {
            this.musbManager.requestPermission(this.usbDevice, PendingIntent.getBroadcast(this.context, 0, new Intent("com.zkteco.idfprdemo.USB_PERMISSION"), 0));
            StartFingerSensor();
            return true;
        }
        Log.d(TAG, "FingerReader is not connected");
        return false;
    }

    private boolean decodeSrcFingerPrint(byte[] bArr) {
        if (bArr == null) {
            return false;
        }
        this.fringerprint = bArr;
        if (bArr.length != 1024) {
            return false;
        }
        this.fringerprint_one = Arrays.copyOfRange(bArr, 0, 512);
        byte[] bArr2 = this.fringerprint;
        this.fringerprint_two = Arrays.copyOfRange(bArr2, 512, bArr2.length);
        return true;
    }

    private void StartFingerSensor() {
        HashMap hashMap = new HashMap();
        hashMap.put("param.key.vid", Integer.valueOf((int) VID));
        hashMap.put("param.key.pid", Integer.valueOf(this.now_PID));
        this.mNIDFPSensor = NIDFPFactory.createNIDFPSensor(this.context, TransportType.USBSCSI, hashMap);
    }

    private boolean checkFingerReader() {
        for (UsbDevice usbDevice : ((UsbManager) this.context.getSystemService("usb")).getDeviceList().values()) {
            if ((usbDevice.getVendorId() == 6997 && usbDevice.getProductId() == 770) || (usbDevice.getVendorId() == 6997 && usbDevice.getProductId() == 772)) {
                if (usbDevice.getProductId() == 770) {
                    this.now_PID = PID;
                } else if (usbDevice.getProductId() == 772) {
                    this.now_PID = PID_110;
                }
                this.usbDevice = usbDevice;
                return true;
            }
        }
        return false;
    }
}
