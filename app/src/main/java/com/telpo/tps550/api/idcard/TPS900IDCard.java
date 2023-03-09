package com.telpo.tps550.api.idcard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import com.telpo.tps550.api.DeviceNotFoundException;
import com.telpo.tps550.api.TelpoException;
import com.telpo.tps550.api.TimeoutException;

/* loaded from: classes.dex */
public class TPS900IDCard {
    private static Context mContext;
    static IdentityMsg identityinfo = new IdentityMsg();
    static IdentityMsg idcardinfo = null;
    static byte[] imgae = new byte[1024];
    public static String name = null;
    public static String sex = null;
    public static String nation = null;
    public static String born = null;
    public static String address = null;
    public static String number = null;
    public static String office = null;
    public static String term = null;

    private static native Object check_idcard(int i, int[] iArr);

    private static native boolean connect_idcard();

    private static native boolean decode_image(byte[] bArr);

    private static native boolean disconnect_idcard();

    private static native byte[] get_image();

    static {
        System.loadLibrary("idcard900");
    }

    public static synchronized void open(Context context) throws DeviceNotFoundException {
        synchronized (TPS900IDCard.class) {
            if (!connect_idcard()) {
                throw new DeviceNotFoundException();
            }
        }
    }

    public static synchronized IdentityMsg checkIdCard(int i) throws TelpoException {
        IdentityMsg identityMsg;
        synchronized (TPS900IDCard.class) {
            int[] iArr = new int[2];
            identityMsg = (IdentityMsg) check_idcard(i, iArr);
            if (identityMsg == null) {
                if (iArr[0] == -5) {
                    disconnect_idcard();
                    throw new DeviceNotFoundException();
                }
                throw new TimeoutException();
            }
        }
        return identityMsg;
    }

    public static synchronized byte[] getIdCardImage() throws TelpoException {
        byte[] bArr;
        synchronized (TPS900IDCard.class) {
            bArr = get_image();
        }
        return bArr;
    }

    public static synchronized Bitmap decodeIdCardImage(byte[] bArr) throws TelpoException {
        Bitmap decodeFile;
        synchronized (TPS900IDCard.class) {
            try {
                if (bArr == null) {
                    throw new ImageDecodeException();
                }
                if (decode_image(bArr)) {
                    try {
                        decodeFile = BitmapFactory.decodeFile(String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath()) + "/tps900/zp.bmp");
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new ImageDecodeException();
                    }
                } else {
                    throw new ImageDecodeException();
                }
            } catch (Throwable th) {
                throw th;
            }
        }
        return decodeFile;
    }

    public static synchronized void close() {
        synchronized (TPS900IDCard.class) {
            disconnect_idcard();
        }
    }
}
