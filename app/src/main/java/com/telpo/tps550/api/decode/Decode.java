package com.telpo.tps550.api.decode;

import com.telpo.tps550.api.serial.Serial;
import com.telpo.tps550.api.util.ReaderUtils;
import com.telpo.tps550.api.util.StringUtil;
import com.telpo.tps550.api.util.SystemUtil;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/* loaded from: classes.dex */
public class Decode {
    private static InputStream mInputStream;
    private static ReadThread mReadThread;
    private static byte[] scanContent;
    private static boolean scansuccess;
    private static Serial serial;

    private static native void decode_close();

    private static native int decode_open();

    private static native int decode_read(int i, byte[] bArr);

    /* loaded from: classes.dex */
    public static class ReadThread extends Thread {
        private ReadThread() {
        }

        /* synthetic */ ReadThread(ReadThread readThread) {
            this();
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            byte[] copyOfRange;
            super.run();
            while (!isInterrupted()) {
                try {
                    byte[] bArr = new byte[64];
                    copyOfRange = Arrays.copyOfRange(bArr, 0, Decode.mInputStream.read(bArr));
                    Decode.scanContent = ReaderUtils.merge(Decode.scanContent, copyOfRange);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (copyOfRange[copyOfRange.length - 1] == 13) {
                    Decode.scansuccess = true;
                    return;
                }
                continue;
            }
        }
    }

    private static void open360c() {
        scanContent = null;
        try {
            if (serial == null) {
                Serial serial2 = new Serial("/dev/ttyHSL0", 115200, 0);
                serial = serial2;
                if (mInputStream == null) {
                    mInputStream = serial2.getInputStream();
                }
                if (mReadThread == null) {
                    ReadThread readThread = new ReadThread(null);
                    mReadThread = readThread;
                    readThread.start();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        decode_open();
    }

    private static void close360c() {
        ReadThread readThread = mReadThread;
        if (readThread != null) {
            readThread.interrupt();
            mReadThread = null;
        }
        try {
            InputStream inputStream = mInputStream;
            if (inputStream != null) {
                inputStream.close();
                mInputStream = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Serial serial2 = serial;
        if (serial2 != null) {
            serial2.close();
            serial = null;
        }
        decode_close();
    }

    private static void scan360c() {
        scansuccess = false;
        long currentTimeMillis = System.currentTimeMillis();
        while (System.currentTimeMillis() - currentTimeMillis < 5000 && !scansuccess) {
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static synchronized void open() throws Exception {
        synchronized (Decode.class) {
            if (SystemUtil.getDeviceType() != StringUtil.DeviceModelEnum.TPS360C.ordinal()) {
                int decode_open = decode_open();
                if (decode_open != 0) {
                    if (decode_open == -1) {
                        throw new Exception("unknown device");
                    }
                    if (decode_open == -2) {
                        throw new Exception("invalid baudrate");
                    }
                    if (decode_open == -3) {
                        throw new Exception("cannot open port");
                    }
                    if (decode_open == -4) {
                        throw new Exception("tcgetattr() failed");
                    }
                    if (decode_open == -5) {
                        throw new Exception("tcsetattr() failed");
                    }
                    if (decode_open == -6) {
                        throw new Exception("externcard already opened");
                    }
                    if (decode_open == -7) {
                        throw new Exception("cannot open externcard");
                    }
                    if (decode_open == -8) {
                        throw new Exception("switch to qrcode error");
                    }
                }
            } else {
                open360c();
            }
        }
    }

    public static synchronized void close() {
        synchronized (Decode.class) {
            if (SystemUtil.getDeviceType() != StringUtil.DeviceModelEnum.TPS360C.ordinal()) {
                decode_close();
            } else {
                close360c();
            }
        }
    }

    public static synchronized byte[] readWithFormat(int i) throws Exception {
        byte[] bArr;
        synchronized (Decode.class) {
            byte[] bArr2 = new byte[2048];
            bArr = new byte[2048];
            int decode_read = decode_read(i, bArr2);
            if (decode_read < 0) {
                if (decode_read == -2) {
                    throw new Exception("Read Timeout");
                }
                throw new Exception("Read Error");
            } else if (bArr2[0] != 3) {
                throw new Exception("Invalid Format");
            } else {
                System.arraycopy(bArr2, 5, bArr, 0, (bArr2[6] * 256) + bArr2[7] + 3);
            }
        }
        return bArr;
    }

    public static synchronized String read(int i) throws Exception {
        synchronized (Decode.class) {
            if (SystemUtil.getDeviceType() != StringUtil.DeviceModelEnum.TPS360C.ordinal()) {
                byte[] bArr = new byte[2048];
                int decode_read = decode_read(i, bArr);
                if (decode_read >= 0) {
                    return new String(bArr, 0, decode_read, "UTF-8");
                } else if (decode_read == -2) {
                    throw new Exception("Read Timeout");
                } else {
                    throw new Exception("Read Error");
                }
            }
            scan360c();
            if (scanContent == null) {
                return "";
            }
            return new String(scanContent);
        }
    }

    static {
        System.loadLibrary("decode");
    }
}
