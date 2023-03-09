package com.telpo.nfc;

import android.util.Log;
import com.telpo.nfc.exception.NotInitWalletException;
import com.telpo.nfc.exception.ReadWalletMoneyWrongException;
import com.telpo.nfc.exception.WrongParamException;
import com.telpo.tps550.api.serial.Serial;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/* loaded from: classes.dex */
public class NfcUtil {
    private static final String TAG = "NfcUtil";
    private InputStream inputStream;
    private String internalModel;
    private OutputStream mOutputStream;
    private ReadThread readThread;
    private boolean receiveComplete;
    private byte[] receiveData;
    private Serial serial;
    private Lock serialLock;
    private static final byte[] CHECK_VERSION = {-18, -18, -18, -120, 3, 0, 17, 0, 0, 20};
    private static final byte[] SELECT_CARD_SUPERSIM = {-18, -18, -18, -120, 3, 0, 17, 0, 1, 21};
    private static final byte[] SELECT_CARD = {-18, -18, -18, -120, 3, 0, 18, -1, 0, 20};

    /* loaded from: classes.dex */
    private static class StateHolder {
        private static final NfcUtil INSTANCE = new NfcUtil(null);

        private StateHolder() {
        }
    }

    private NfcUtil() {
        this.serialLock = new ReentrantLock();
        this.receiveData = null;
        this.receiveComplete = false;
    }

    /* synthetic */ NfcUtil(NfcUtil nfcUtil) {
        this();
    }

    public static final NfcUtil getInstance() {
        return StateHolder.INSTANCE;
    }

    public void initSerial() {
        String property = getProperty("ro.internal.model", "");
        this.internalModel = property;
        if ("TPS468".equals(property)) {
            initSerial("/dev/ttyS3", 9600);
        } else if ("TPS537".equals(this.internalModel)) {
            initSerial("/dev/ttyUSB7", 115200);
        } else if (!"D2".equals(this.internalModel)) {
        } else {
            initSerial("/dev/ttyUSB0", 9600);
        }
    }

    public void initSerial(String str, int i) {
        try {
            this.internalModel = getProperty("ro.internal.model", "");
            if (this.serial == null) {
                this.serial = new Serial(str, i, 0);
            }
            if (this.mOutputStream == null) {
                this.mOutputStream = this.serial.getOutputStream();
            }
            if (this.inputStream == null) {
                this.inputStream = this.serial.getInputStream();
            }
            if (this.readThread != null) {
                return;
            }
            ReadThread readThread = new ReadThread(this, null);
            this.readThread = readThread;
            readThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void destroySerial() {
        try {
            ReadThread readThread = this.readThread;
            if (readThread != null) {
                readThread.interrupt();
                this.readThread = null;
            }
            OutputStream outputStream = this.mOutputStream;
            if (outputStream != null) {
                outputStream.close();
                this.mOutputStream = null;
            }
            InputStream inputStream = this.inputStream;
            if (inputStream != null) {
                inputStream.close();
                this.inputStream = null;
            }
            Serial serial = this.serial;
            if (serial == null) {
                return;
            }
            serial.close();
            this.serial = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int checkVersion() {
        try {
            byte[] sendData = sendData(CHECK_VERSION);
            if (sendData != null && sendData.length > 9 && sendData[0] == -18 && sendData[1] == -18 && sendData[2] == -18 && sendData[3] == -120 && sendData[4] == 5 && sendData[5] == 0 && sendData[6] == 17 && sendData[7] == 0 && sendData[8] == 0) {
                String hexString = toHexString(sendData);
                return (Integer.valueOf(hexString.substring(20, 22), 16).intValue() * 100) + Integer.valueOf(hexString.substring(18, 20), 16).intValue();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public byte[] selectCardSuperSim() {
        try {
            byte[] sendData = sendData(SELECT_CARD_SUPERSIM);
            if (sendData != null && sendData[sendData.length - 2] != -6) {
                return Arrays.copyOfRange(sendData, 9, sendData.length - 1);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] sendAPDU(byte[] bArr, int i) {
        try {
            byte[] sendData = sendData(SpliceM1cmd.spliceAPDUcmd(bArr), i);
            if (sendData != null && sendData.length > 10) {
                return Arrays.copyOfRange(sendData, 9, sendData.length - 1);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public SelectCardReturn selectCard() {
        Exception e;
        SelectCardReturn selectCardReturn = null;
        if ("TPS537".equals(this.internalModel)) {
            try {
                byte[] sendData = sendData(SELECT_CARD);
                if (sendData == null || sendData[sendData.length - 2] == -1) {
                    return null;
                }
                byte[] copyOfRange = Arrays.copyOfRange(sendData, 8, sendData.length - 1);
                SelectCardReturn selectCardReturn2 = new SelectCardReturn();
                try {
                    selectCardReturn2.setCardType(SpliceM1cmd.getCardType(copyOfRange));
                    selectCardReturn2.setCardNum(Arrays.copyOfRange(copyOfRange, 1, copyOfRange.length));
                    return selectCardReturn2;
                } catch (Exception e2) {
                    e = e2;
                    selectCardReturn = selectCardReturn2;
                    e.printStackTrace();
                    return selectCardReturn;
                }
            } catch (Exception e3) {
                e = e3;
            }
        } else {
            return null;
        }
    }

    public boolean M1_AUTHENTICATE(int i, int i2, byte[] bArr) throws WrongParamException {
        if ("TPS537".equals(this.internalModel)) {
            if (i < 0 || i > 255 || i2 < 0 || i2 > 1 || bArr == null || bArr.length != 6) {
                throw new WrongParamException();
            }
            try {
                byte[] sendData = sendData(SpliceM1cmd.spliceM1cmd_AUTHENTICATE(i, i2, bArr));
                if (sendData != null) {
                    if (sendData[sendData.length - 2] == 0) {
                        return true;
                    }
                    byte b = sendData[sendData.length - 2];
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean M1_WRITE_BLOCK(int i, byte[] bArr) throws WrongParamException {
        if ("TPS537".equals(this.internalModel)) {
            if (i < 0 || i > 255 || bArr == null || bArr.length != 16) {
                throw new WrongParamException();
            }
            try {
                byte[] sendData = sendData(SpliceM1cmd.spliceM1cmd_WRITE_BLOCK(i, bArr));
                if (sendData == null) {
                    return false;
                }
                return sendData[sendData.length + (-2)] == 0;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public byte[] M1_READ_BLOCK(int i) throws WrongParamException {
        if ("TPS537".equals(this.internalModel)) {
            if (i < 0 || i > 255) {
                throw new WrongParamException();
            }
            try {
                byte[] sendData = sendData(SpliceM1cmd.spliceM1cmd_READ_BLOCK(i));
                if (sendData != null && sendData.length > 11) {
                    return Arrays.copyOfRange(sendData, 9, sendData.length - 1);
                }
                return null;
            } catch (Exception unused) {
                return null;
            }
        }
        return null;
    }

    public boolean M1_INIT_WALLET(int i, int i2) throws WrongParamException {
        if ("TPS537".equals(this.internalModel)) {
            if (i < 0 || i > 255) {
                throw new WrongParamException();
            }
            try {
                byte[] sendData = sendData(SpliceM1cmd.spliceM1cmd_INIT_WALLET(i, i2));
                if (sendData == null) {
                    return false;
                }
                return sendData[sendData.length + (-2)] == 0;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public Long M1_READ_WALLET(int i) throws WrongParamException, ReadWalletMoneyWrongException, NotInitWalletException {
        if ("TPS537".equals(this.internalModel)) {
            if (i < 0 || i > 255) {
                throw new WrongParamException();
            }
            try {
                byte[] sendData = sendData(SpliceM1cmd.spliceM1cmd_READ_WALLET(i));
                if (sendData != null && sendData.length > 11) {
                    String hexString = toHexString(Arrays.copyOfRange(sendData, 9, sendData.length - 1));
                    if ("FFFFFFFF".equals(hexString)) {
                        throw new NotInitWalletException();
                    }
                    if (hexString.length() < 8) {
                        hexString = String.valueOf(hexString) + "0";
                    }
                    String str = "";
                    for (int i2 = 8; i2 > 0; i2 -= 2) {
                        str = String.valueOf(str) + hexString.substring(i2 - 2, i2);
                    }
                    setLog("M1_READ_WALLET > newMoneyHex[" + str + "]");
                    return Long.valueOf(str, 16);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        throw new ReadWalletMoneyWrongException();
    }

    public boolean M1_WRITE_WALLET(int i, int i2, int i3) throws WrongParamException {
        if ("TPS537".equals(this.internalModel)) {
            if (i < 0 || i > 255 || i2 < 0 || i2 > 1) {
                throw new WrongParamException();
            }
            try {
                byte[] sendData = sendData(SpliceM1cmd.spliceM1cmd_WRITE_WALLET(i, i2, i3));
                if (sendData == null) {
                    return false;
                }
                return sendData[sendData.length + (-2)] == 0;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean M1_TRANSFER(int i) throws WrongParamException {
        if ("TPS537".equals(this.internalModel)) {
            if (i < 0 || i > 255) {
                throw new WrongParamException();
            }
            try {
                byte[] sendData = sendData(SpliceM1cmd.spliceM1cmd_TRANSFER(i));
                if (sendData == null) {
                    return false;
                }
                return sendData[sendData.length + (-2)] == 0;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean M1_RESTORE(int i) throws WrongParamException {
        if ("TPS537".equals(this.internalModel)) {
            if (i < 0 || i > 255) {
                throw new WrongParamException();
            }
            try {
                byte[] sendData = sendData(SpliceM1cmd.spliceM1cmd_RESTORE(i));
                if (sendData == null) {
                    return false;
                }
                return sendData[sendData.length + (-2)] == 0;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean M1_CLOSE_CARD() {
        if ("TPS537".equals(this.internalModel)) {
            try {
                byte[] sendData = sendData(SpliceM1cmd.spliceM1cmd_CLOSE_CARD());
                if (sendData == null) {
                    return false;
                }
                return sendData[sendData.length + (-2)] == 0;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /* loaded from: classes.dex */
    public class ReadThread extends Thread {
        private ReadThread() {
            NfcUtil.this = r1;
        }

        /* synthetic */ ReadThread(NfcUtil nfcUtil, ReadThread readThread) {
            this();
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            int read;
            super.run();
            while (!isInterrupted()) {
                try {
                    byte[] bArr = new byte[64];
                    if (NfcUtil.this.inputStream != null && NfcUtil.this.inputStream.available() > 0 && (read = NfcUtil.this.inputStream.read(bArr)) > 0) {
                        byte[] copyOfRange = Arrays.copyOfRange(bArr, 0, read);
                        NfcUtil nfcUtil = NfcUtil.this;
                        nfcUtil.receiveData = NfcUtil.merge(nfcUtil.receiveData, copyOfRange);
                        if (NfcUtil.this.receiveData.length > 5 && NfcUtil.this.receiveData.length - 7 == Integer.valueOf(NfcUtil.toHexString(new byte[]{NfcUtil.this.receiveData[5], NfcUtil.this.receiveData[4]}), 16).intValue()) {
                            NfcUtil.this.receiveComplete = true;
                            NfcUtil.this.setLog("receiveData[" + NfcUtil.toHexString(NfcUtil.this.receiveData) + "]");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private byte[] sendData(byte[] bArr) {
        return sendData(bArr, 1000);
    }

    private byte[] sendData(byte[] bArr, int i) {
        this.serialLock.lock();
        try {
            try {
                if (this.mOutputStream != null) {
                    this.receiveData = null;
                    this.receiveComplete = false;
                    setLog("send >>> " + toHexString(bArr));
                    this.mOutputStream.write(bArr);
                }
            } catch (Exception e) {
                try {
                    e.printStackTrace();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            long currentTimeMillis = System.currentTimeMillis();
            while (!this.receiveComplete && System.currentTimeMillis() - currentTimeMillis < i) {
                try {
                    Thread.sleep(1L);
                } catch (InterruptedException e3) {
                    e3.printStackTrace();
                }
            }
            if (!this.receiveComplete) {
                this.receiveData = null;
            }
            this.serialLock.unlock();
            return this.receiveData;
        } catch (Throwable th) {
            this.serialLock.unlock();
            throw th;
        }
    }

    public static byte[] toBytes(String str) {
        String upperCase = str.toUpperCase();
        int length = upperCase.length();
        if (length % 2 == 1) {
            upperCase = String.valueOf(upperCase) + "0";
            length++;
        }
        int i = length >> 1;
        byte[] bArr = new byte[i];
        int i2 = 0;
        int i3 = 0;
        while (i2 < i) {
            bArr[i2] = (byte) (((byte) ("0123456789ABCDEF".indexOf(upperCase.charAt(i3)) << 4)) | ((byte) "0123456789ABCDEF".indexOf(upperCase.charAt(i3 + 1))));
            i2++;
            i3 += 2;
        }
        return bArr;
    }

    public static String toHexString(byte[] bArr) {
        if (bArr == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (byte b : bArr) {
            String hexString = Integer.toHexString(b & 255);
            if (hexString.length() == 1) {
                sb.append("0");
            }
            sb.append(hexString.toUpperCase());
        }
        return sb.toString();
    }

    public static byte[] merge(byte[] bArr, byte[] bArr2) {
        if (bArr == null) {
            return bArr2;
        }
        byte[] bArr3 = new byte[bArr.length + bArr2.length];
        System.arraycopy(bArr, 0, bArr3, 0, bArr.length);
        System.arraycopy(bArr2, 0, bArr3, bArr.length, bArr2.length);
        return bArr3;
    }

    private static String getProperty(String str, String str2) {
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            return (String) cls.getMethod("get", String.class, String.class).invoke(cls, str, str2);
        } catch (Exception e) {
            e.printStackTrace();
            return str2;
        }
    }

    public void setLog(String str) {
        Log.d(TAG, str);
    }
}
