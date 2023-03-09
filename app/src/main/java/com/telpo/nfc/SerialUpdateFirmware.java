package com.telpo.nfc;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import com.fasterxml.jackson.core.JsonPointer;
import com.telpo.tps550.api.serial.Serial;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

/* loaded from: classes.dex */
public class SerialUpdateFirmware {
    private static final int IS_SENDING_DATA = 9;
    private static final int SWITCH_TO_UPDATE = 8;
    private T2OReaderCallBack callBack;
    byte[] project_bin;
    private static final byte[] INTOUPDATE = {-86, -86, -86, -106, 105, 0, 5, Byte.MIN_VALUE, 32, 0, 0, -91};
    private static final byte[] PACKAGESENDSUCCESS = {-86, -86, -86, -106, 105, 0, 5, Byte.MIN_VALUE, 32, -1, 0, 90};
    int sendingCount = 1;
    private boolean isUpdatingModule = false;
    private InputStream mInputStream = null;
    private OutputStream mOutputStream = null;
    private Serial serial = null;
    private ReadThread mReadThread = null;
    private int doingType = -1;
    private byte[] readData = null;
    private boolean switchUpdateSuccess = false;
    private boolean receivePackageSuccess = false;
    private int getFirmwareVersion = 11;
    private boolean serialUpdateSuccess = false;
    boolean receiveComplete = false;
    private long[] crc_16tab = {0, 4129, 8258, 12387, 16516, 20645, 24774, 28903, 33032, 37161, 41290, 45419, 49548, 53677, 57806, 61935, 4657, 528, 12915, 8786, 21173, 17044, 29431, 25302, 37689, 33560, 45947, 41818, 54205, 50076, 62463, 58334, 9314, 13379, 1056, 5121, 25830, 29895, 17572, 21637, 42346, 46411, 34088, 38153, 58862, 62927, 50604, 54669, 13907, 9842, 5649, 1584, 30423, 26358, 22165, 18100, 46939, 42874, 38681, 34616, 63455, 59390, 55197, 51132, 18628, 22757, 26758, 30887, 2112, 6241, 10242, 14371, 51660, 55789, 59790, 63919, 35144, 39273, 43274, 47403, 23285, 19156, 31415, 27286, 6769, 2640, 14899, 10770, 56317, 52188, 64447, 60318, 39801, 35672, 47931, 43802, 27814, 31879, 19684, 23749, 11298, 15363, 3168, 7233, 60846, 64911, 52716, 56781, 44330, 48395, 36200, 40265, 32407, 28342, 24277, 20212, 15891, 11826, 7761, 3696, 65439, 61374, 57309, 53244, 48923, 44858, 40793, 36728, 37256, 33193, 45514, 41451, 53516, 49453, 61774, 57711, 4224, 161, 12482, 8419, 20484, 16421, 28742, 24679, 33721, 37784, 41979, 46042, 49981, 54044, 58239, 62302, 689, 4752, 8947, 13010, 16949, 21012, 25207, 29270, 46570, 42443, 38312, 34185, 62830, 58703, 54572, 50445, 13538, 9411, 5280, 1153, 29798, 25671, 21540, 17413, 42971, 47098, 34713, 38840, 59231, 63358, 50973, 55100, 9939, 14066, 1681, 5808, 26199, 30326, 17941, 22068, 55628, 51565, 63758, 59695, 39368, 35305, 47498, 43435, 22596, 18533, 30726, 26663, 6336, 2273, 14466, 10403, 52093, 56156, 60223, 64286, 35833, 39896, 43963, 48026, 19061, 23124, 27191, 31254, 2801, 6864, 10931, 14994, 64814, 60687, 56684, 52557, 48554, 44427, 40424, 36297, 31782, 27655, 23652, 19525, 15522, 11395, 7392, 3265, 61215, 65342, 53085, 57212, 44955, 49082, 36825, 40952, 28183, 32310, 20053, 24180, 11923, 16050, 3793, 7920};

    /* loaded from: classes.dex */
    public interface T2OReaderCallBack {
        void updateComplete(boolean z);
    }

    public void setLog(String str) {
        Log.d("T2OReader", str);
    }

    private String[] CopyAssetsDir(Context context, String str, String str2) {
        String[] strArr;
        try {
            strArr = context.getResources().getAssets().list(str);
        } catch (IOException e) {
            e.printStackTrace();
            strArr = null;
        }
        if (strArr != null) {
            String[] strArr2 = new String[strArr.length];
            for (int i = 0; i < strArr.length; i++) {
                strArr2[i] = strArr[i];
                CopyAssetsFile(context, String.valueOf(str) + "/" + strArr[i], str2);
            }
            return strArr2;
        }
        return null;
    }

    private Boolean CopyAssetsFile(Context context, String str, String str2) {
        try {
            InputStream open = context.getAssets().open(str);
            FileOutputStream fileOutputStream = new FileOutputStream(String.valueOf(str2) + JsonPointer.SEPARATOR + str.substring(str.indexOf(47) + 1, str.length()));
            byte[] bArr = new byte[1024];
            while (true) {
                int read = open.read(bArr);
                if (read != -1) {
                    fileOutputStream.write(bArr, 0, read);
                } else {
                    open.close();
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int checkVersion() {
        try {
            OutputStream outputStream = this.mOutputStream;
            if (outputStream != null) {
                this.receiveComplete = false;
                this.readData = null;
                this.doingType = this.getFirmwareVersion;
                outputStream.write(new byte[]{-18, -18, -18, -120, 3, 0, 17, 0, 0, 20});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        long currentTimeMillis = System.currentTimeMillis();
        while (System.currentTimeMillis() - currentTimeMillis < 1000 && !this.receiveComplete) {
        }
        if (this.receiveComplete) {
            String hexString = NfcUtil.toHexString(this.readData);
            setLog("checkVersion[" + hexString + "]");
            return (Integer.valueOf(hexString.substring(20, 22), 16).intValue() * 100) + Integer.valueOf(hexString.substring(18, 20), 16).intValue();
        }
        return -1;
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

    public byte[] changeToUpdateMode(Context context, String str, int i) {
        this.serialUpdateSuccess = false;
        if (!new File("/sdcard/project.bin").exists()) {
            setLog("not found update file");
            return null;
        }
        byte[] decodeProjectBin = decodeProjectBin();
        if (decodeProjectBin == null) {
            return null;
        }
        openReader(context, str, i);
        sendCommand(decodeProjectBin, 8);
        threadSleep(1000);
        closeReader();
        return this.readData;
    }

    public void serialModuleUpdateDifferentBaudrate(T2OReaderCallBack t2OReaderCallBack) {
        this.serialUpdateSuccess = false;
        this.callBack = t2OReaderCallBack;
        if (!new File("/sdcard/project.bin").exists()) {
            setLog("not found update file");
            if (t2OReaderCallBack == null) {
                return;
            }
            this.callBack.updateComplete(this.serialUpdateSuccess);
        } else if (decodeProjectBin() != null) {
            this.isUpdatingModule = true;
            sendUpdate();
        } else if (t2OReaderCallBack == null) {
        } else {
            this.callBack.updateComplete(this.serialUpdateSuccess);
        }
    }

    public boolean openReader(Context context, String str, int i) {
        try {
            File file = new File("/sdcard/project.bin");
            if (file.exists()) {
                setLog("deleteResult[" + file.delete() + "]");
            }
            CopyAssetsDir(context, "updateFile", "/sdcard");
            if (this.serial != null) {
                return false;
            }
            Serial serial = new Serial(str, i, 0);
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

    public void sendCommand(byte[] bArr, int i) {
        try {
            OutputStream outputStream = this.mOutputStream;
            if (outputStream == null) {
                return;
            }
            this.doingType = i;
            this.readData = null;
            outputStream.write(bArr);
            setLog("send[" + NfcUtil.toHexString(bArr) + "]");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeReader() {
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* loaded from: classes.dex */
    public class ReadThread extends Thread {
        private ReadThread() {
            SerialUpdateFirmware.this = r1;
        }

        /* synthetic */ ReadThread(SerialUpdateFirmware serialUpdateFirmware, ReadThread readThread) {
            this();
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            super.run();
            while (!isInterrupted()) {
                try {
                    byte[] bArr = new byte[1024];
                    byte[] copyOfRange = Arrays.copyOfRange(bArr, 0, SerialUpdateFirmware.this.mInputStream.read(bArr));
                    SerialUpdateFirmware serialUpdateFirmware = SerialUpdateFirmware.this;
                    serialUpdateFirmware.readData = NfcUtil.merge(serialUpdateFirmware.readData, copyOfRange);
                    SerialUpdateFirmware.this.setLog("receive:" + NfcUtil.toHexString(SerialUpdateFirmware.this.readData));
                    if (SerialUpdateFirmware.this.isUpdatingModule) {
                        if (SerialUpdateFirmware.this.doingType != 8) {
                            if (SerialUpdateFirmware.this.doingType == 9) {
                                if (SerialUpdateFirmware.this.readData != null && SerialUpdateFirmware.this.readData.length > 11 && NfcUtil.toHexString(SerialUpdateFirmware.PACKAGESENDSUCCESS).equals(NfcUtil.toHexString(Arrays.copyOfRange(SerialUpdateFirmware.this.readData, SerialUpdateFirmware.this.readData.length - 12, SerialUpdateFirmware.this.readData.length)))) {
                                    SerialUpdateFirmware.this.readData = null;
                                    SerialUpdateFirmware.this.receivePackageSuccess = true;
                                    SerialUpdateFirmware.this.setLog("receiveUpdatePackage success");
                                }
                                if (SerialUpdateFirmware.this.readData != null && SerialUpdateFirmware.this.readData.length > 11 && NfcUtil.toHexString(SerialUpdateFirmware.INTOUPDATE).equals(NfcUtil.toHexString(Arrays.copyOfRange(SerialUpdateFirmware.this.readData, SerialUpdateFirmware.this.readData.length - 12, SerialUpdateFirmware.this.readData.length)))) {
                                    SerialUpdateFirmware.this.readData = null;
                                    SerialUpdateFirmware.this.isUpdatingModule = false;
                                    SerialUpdateFirmware.this.serialUpdateSuccess = true;
                                    if (SerialUpdateFirmware.this.callBack != null) {
                                        SerialUpdateFirmware.this.callBack.updateComplete(SerialUpdateFirmware.this.serialUpdateSuccess);
                                    }
                                    SerialUpdateFirmware.this.setLog("module update success!");
                                }
                            }
                        } else if (SerialUpdateFirmware.this.readData != null && SerialUpdateFirmware.this.readData.length > 11 && NfcUtil.toHexString(SerialUpdateFirmware.INTOUPDATE).equals(NfcUtil.toHexString(Arrays.copyOfRange(SerialUpdateFirmware.this.readData, SerialUpdateFirmware.this.readData.length - 12, SerialUpdateFirmware.this.readData.length)))) {
                            SerialUpdateFirmware.this.readData = null;
                            SerialUpdateFirmware.this.switchUpdateSuccess = true;
                            SerialUpdateFirmware.this.setLog("switchToUpdate success");
                            SerialUpdateFirmware.this.sendUpdate();
                        }
                    } else if (SerialUpdateFirmware.this.doingType == SerialUpdateFirmware.this.getFirmwareVersion && SerialUpdateFirmware.this.readData.length > 5 && SerialUpdateFirmware.this.readData.length - 7 == Integer.valueOf(NfcUtil.toHexString(new byte[]{SerialUpdateFirmware.this.readData[5], SerialUpdateFirmware.this.readData[4]}), 16).intValue()) {
                        SerialUpdateFirmware.this.receiveComplete = true;
                        SerialUpdateFirmware.this.setLog("tempString[" + NfcUtil.toHexString(SerialUpdateFirmware.this.readData) + "]");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void sendUpdate() {
        this.sendingCount = 1;
        final int length = (this.project_bin.length / 128) + 1;
        this.receivePackageSuccess = true;
        new Thread(new Runnable() { // from class: com.telpo.nfc.SerialUpdateFirmware.1
            @Override // java.lang.Runnable
            public void run() {
                byte[] bArr;
                while (SerialUpdateFirmware.this.sendingCount <= length) {
                    try {
                        if (SerialUpdateFirmware.this.receivePackageSuccess) {
                            SerialUpdateFirmware.this.receivePackageSuccess = false;
                            SerialUpdateFirmware.this.setLog("sending package " + SerialUpdateFirmware.this.sendingCount);
                            if (SerialUpdateFirmware.this.sendingCount < length) {
                                bArr = Arrays.copyOfRange(SerialUpdateFirmware.this.project_bin, (SerialUpdateFirmware.this.sendingCount - 1) * 128, SerialUpdateFirmware.this.sendingCount * 128);
                            } else {
                                bArr = Arrays.copyOfRange(SerialUpdateFirmware.this.project_bin, (SerialUpdateFirmware.this.sendingCount - 1) * 128, SerialUpdateFirmware.this.project_bin.length);
                                new Thread(new Runnable() { // from class: com.telpo.nfc.SerialUpdateFirmware.1.1
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        SerialUpdateFirmware.this.threadSleep(1000);
                                        if (SerialUpdateFirmware.this.callBack == null || SerialUpdateFirmware.this.serialUpdateSuccess) {
                                            return;
                                        }
                                        SerialUpdateFirmware.this.callBack.updateComplete(SerialUpdateFirmware.this.serialUpdateSuccess);
                                    }
                                }).start();
                            }
                            SerialUpdateFirmware.this.sendCommand(bArr, 9);
                            SerialUpdateFirmware.this.sendingCount++;
                        }
                        Thread.sleep(50L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void threadSleep(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private byte[] decodeProjectBin() {
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
                Log.d("taggg", "project_bin content[" + NfcUtil.toHexString(Arrays.copyOfRange(this.project_bin, i, i + 512)) + "]");
            }
            i += 512;
        }
        StringBuilder sb = new StringBuilder("project_bin content[");
        byte[] bArr = this.project_bin;
        Log.d("taggg", sb.append(NfcUtil.toHexString(Arrays.copyOfRange(bArr, i, bArr.length))).append("]").toString());
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
        String str3 = "AAAAAA966900078020" + sb3 + str2 + NfcUtil.toHexString(new byte[]{getXor(NfcUtil.toBytes(str))});
        Log.d("taggg", "cmdStr[" + str3 + "]");
        Log.d("tagg", "cmdStr length[" + str3.length() + "]");
        setLog("cmdStr:" + str3);
        return NfcUtil.toBytes(str3);
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
        throw new UnsupportedOperationException("Method not decompiled: com.telpo.nfc.SerialUpdateFirmware.getBytes4File(java.lang.String):byte[]");
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

    private static byte getXor(byte[] bArr) {
        byte b = bArr[0];
        Log.d("taggg", "datas[0]=" + ((int) b));
        for (int i = 1; i < bArr.length; i++) {
            b = (byte) (b ^ bArr[i]);
            Log.d("taggg", "datas[" + i + "]=" + ((int) bArr[i]) + " temp=" + ((int) b));
        }
        return b;
    }
}
