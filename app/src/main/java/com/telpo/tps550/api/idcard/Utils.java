package com.telpo.tps550.api.idcard;

import android.os.Environment;
import android.util.Log;
import com.telpo.tps550.api.util.ShellUtils;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.RandomAccessFile;

/* loaded from: classes.dex */
public class Utils {
    public static void setStatusRecord(String str, boolean z) {
        try {
            File file = new File("/sdcard/iccardRecord.txt");
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } else if (!z) {
                file.delete();
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rwd");
            randomAccessFile.seek(file.length());
            randomAccessFile.write(str.getBytes());
            randomAccessFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean hasRootPermission() {
        Process process;
        Throwable th;
        DataOutputStream dataOutputStream;
        boolean z = false;
        DataOutputStream dataOutputStream2 = null;
        try {
            try {
                process = Runtime.getRuntime().exec(ShellUtils.COMMAND_SU);
                try {
                    dataOutputStream = new DataOutputStream(process.getOutputStream());
                } catch (Exception unused) {
                } catch (Throwable th2) {
                    th = th2;
                }
            } catch (Exception unused2) {
            }
        } catch (Exception unused3) {
            process = null;
        } catch (Throwable th3) {
            th = th3;
            process = null;
        }
        try {
            dataOutputStream.writeBytes(ShellUtils.COMMAND_EXIT);
            dataOutputStream.flush();
            process.waitFor();
            if (process.exitValue() == 0) {
                z = true;
            }
            dataOutputStream.close();
        } catch (Exception unused4) {
            dataOutputStream2 = dataOutputStream;
            if (dataOutputStream2 != null) {
                dataOutputStream2.close();
                process.destroy();
            }
            return z;
        } catch (Throwable th4) {
            th = th4;
            dataOutputStream2 = dataOutputStream;
            if (dataOutputStream2 != null) {
                try {
                    dataOutputStream2.close();
                    process.destroy();
                } catch (Exception unused5) {
                }
            }
            throw th;
        }
        process.destroy();
        return z;
    }

    public static boolean upgradeRootPermission(String str) {
        Process process;
        Throwable th;
        String str2;
        DataOutputStream dataOutputStream;
        DataOutputStream dataOutputStream2 = null;
        try {
            Log.d("initUsbDevice", "techshino  pkgCodePath = " + str);
            str2 = "chmod 0777 " + str;
            process = Runtime.getRuntime().exec(ShellUtils.COMMAND_SU);
            try {
                dataOutputStream = new DataOutputStream(process.getOutputStream());
            } catch (Exception unused) {
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Exception unused2) {
            process = null;
        } catch (Throwable th3) {
            th = th3;
            process = null;
        }
        try {
            dataOutputStream.writeBytes(String.valueOf(str2) + ShellUtils.COMMAND_LINE_END);
            dataOutputStream.writeBytes(ShellUtils.COMMAND_EXIT);
            dataOutputStream.flush();
            process.waitFor();
            try {
                dataOutputStream.close();
                process.destroy();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return true;
            }
        } catch (Exception unused3) {
            dataOutputStream2 = dataOutputStream;
            if (dataOutputStream2 != null) {
                try {
                    dataOutputStream2.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                    return false;
                }
            }
            process.destroy();
            return false;
        } catch (Throwable th4) {
            th = th4;
            dataOutputStream2 = dataOutputStream;
            if (dataOutputStream2 != null) {
                try {
                    dataOutputStream2.close();
                } catch (Exception e3) {
                    e3.printStackTrace();
                    throw th;
                }
            }
            process.destroy();
            throw th;
        }
    }

    public static byte[] readSDFile(String str) {
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(Environment.getExternalStorageDirectory(), str));
            byte[] bArr = new byte[fileInputStream.available()];
            fileInputStream.read(bArr);
            return bArr;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
