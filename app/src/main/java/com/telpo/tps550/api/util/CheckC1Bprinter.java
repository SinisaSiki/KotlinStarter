package com.telpo.tps550.api.util;

import android.util.Log;
import com.telpo.tps550.api.serial.Serial;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Arrays;

/* loaded from: classes.dex */
public class CheckC1Bprinter {
    private static InputStream inputStream = null;
    private static OutputStream mOutputStream = null;
    private static ReadThread readThread = null;
    private static boolean receiveComplete = false;
    private static byte[] receiveData;
    private static Serial serial;

    public static byte[] checkVersion() {
        initSerial(checkSerialFromFile());
        sendData(toBytes("1B77"));
        long currentTimeMillis = System.currentTimeMillis();
        while (System.currentTimeMillis() - currentTimeMillis < 500) {
            try {
                Thread.sleep(1L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        setLog("time[" + (System.currentTimeMillis() - currentTimeMillis) + "]");
        destroySerial();
        return receiveData;
    }

    public static String checkSerialFromFile() {
        String fileContent = getFileContent("/sdcard/tpsdk/c1bserial.txt");
        return fileContent == null ? "/dev/ttyUSB9" : fileContent;
    }

    public static String checkSerialPort() {
        long currentTimeMillis = System.currentTimeMillis();
        initSerial("/dev/ttyUSB9");
        sendData(toBytes("1B16"));
        long currentTimeMillis2 = System.currentTimeMillis();
        while (!receiveComplete && System.currentTimeMillis() - currentTimeMillis2 < 200) {
            try {
                Thread.sleep(1L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        setLog("ttyusb9 time[" + (System.currentTimeMillis() - currentTimeMillis2) + "]");
        destroySerial();
        if (receiveComplete) {
            setLog("ttyUSB9 receiveComplete true");
            setLog("allTime[" + (System.currentTimeMillis() - currentTimeMillis) + "]");
            return "/dev/ttyUSB9";
        }
        setLog("no ttyUSB9 test ttyUSB8");
        initSerial("/dev/ttyUSB8");
        sendData(toBytes("1B16"));
        long currentTimeMillis3 = System.currentTimeMillis();
        while (!receiveComplete && System.currentTimeMillis() - currentTimeMillis3 < 200) {
            try {
                Thread.sleep(1L);
            } catch (InterruptedException e2) {
                e2.printStackTrace();
            }
        }
        setLog("ttyusb8 time[" + (System.currentTimeMillis() - currentTimeMillis3) + "]");
        destroySerial();
        if (receiveComplete) {
            setLog("ttyUSB8 receiveComplete true");
            setLog("allTime[" + (System.currentTimeMillis() - currentTimeMillis) + "]");
            return "/dev/ttyUSB8";
        }
        setLog("allTime[" + (System.currentTimeMillis() - currentTimeMillis) + "]");
        return null;
    }

    private static void initSerial(String str) {
        try {
            if (serial == null) {
                serial = new Serial(str, 460800, 0);
            }
            if (mOutputStream == null) {
                mOutputStream = serial.getOutputStream();
            }
            if (inputStream == null) {
                inputStream = serial.getInputStream();
            }
            if (readThread != null) {
                return;
            }
            ReadThread readThread2 = new ReadThread(null);
            readThread = readThread2;
            readThread2.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void destroySerial() {
        try {
            ReadThread readThread2 = readThread;
            if (readThread2 != null) {
                readThread2.interrupt();
                readThread = null;
            }
            OutputStream outputStream = mOutputStream;
            if (outputStream != null) {
                outputStream.close();
                mOutputStream = null;
            }
            InputStream inputStream2 = inputStream;
            if (inputStream2 != null) {
                inputStream2.close();
                inputStream = null;
            }
            Serial serial2 = serial;
            if (serial2 == null) {
                return;
            }
            serial2.close();
            serial = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* loaded from: classes.dex */
    public static class ReadThread extends Thread {
        private ReadThread() {
        }

        /* synthetic */ ReadThread(ReadThread readThread) {
            this();
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            int read;
            super.run();
            while (!isInterrupted()) {
                try {
                    byte[] bArr = new byte[64];
                    if (CheckC1Bprinter.inputStream != null && CheckC1Bprinter.inputStream.available() > 0 && (read = CheckC1Bprinter.inputStream.read(bArr)) > 0) {
                        CheckC1Bprinter.receiveComplete = true;
                        CheckC1Bprinter.receiveData = CheckC1Bprinter.merge(CheckC1Bprinter.receiveData, Arrays.copyOfRange(bArr, 0, read));
                        CheckC1Bprinter.setLog("receiveData[" + CheckC1Bprinter.toHexString(CheckC1Bprinter.receiveData) + "]");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void sendData(byte[] bArr) {
        try {
            OutputStream outputStream = mOutputStream;
            if (outputStream == null) {
                return;
            }
            receiveData = null;
            receiveComplete = false;
            outputStream.write(bArr);
        } catch (Exception e) {
            e.printStackTrace();
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

    public static void setLog(String str) {
        Log.d("tagg", str);
    }

    private static String getFileContent(String str) {
        String str2 = null;
        try {
            File file = new File(str);
            if (file.isFile() && file.exists()) {
                InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file));
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                while (true) {
                    String readLine = bufferedReader.readLine();
                    if (readLine == null) {
                        break;
                    }
                    str2 = readLine;
                }
                inputStreamReader.close();
                bufferedReader.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str2;
    }
}
