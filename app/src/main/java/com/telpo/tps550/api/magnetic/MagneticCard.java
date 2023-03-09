package com.telpo.tps550.api.magnetic;

import android.content.Context;
import com.telpo.tps550.api.DeviceAlreadyOpenException;
import com.telpo.tps550.api.DeviceNotOpenException;
import com.telpo.tps550.api.InternalErrorException;
import com.telpo.tps550.api.TelpoException;
import com.telpo.tps550.api.TimeoutException;
import com.telpo.tps550.api.util.StringUtil;
import com.telpo.tps550.api.util.SystemUtil;
import java.io.UnsupportedEncodingException;

/* loaded from: classes.dex */
public class MagneticCard {
    private static final String TAG = "TELPO_SDK";

    private static native int check_msr(int i, byte[] bArr);

    private static native void close_msr();

    private static native int open_msr();

    private static native int ready_for_read();

    public static synchronized void open() throws TelpoException {
        synchronized (MagneticCard.class) {
            int deviceType = SystemUtil.getDeviceType();
            if (deviceType != StringUtil.DeviceModelEnum.TPS900.ordinal() && deviceType != StringUtil.DeviceModelEnum.TPS900MB.ordinal()) {
                int open_msr = open_msr();
                if (open_msr == -3) {
                    throw new InternalErrorException("Cannot open magnetic stripe card reader!");
                }
                if (open_msr == -2) {
                    throw new DeviceAlreadyOpenException("The magnetic stripe card reader has been opened!");
                }
            }
        }
    }

    public static synchronized void open(Context context) throws TelpoException {
        synchronized (MagneticCard.class) {
            int deviceType = SystemUtil.getDeviceType();
            if (deviceType != StringUtil.DeviceModelEnum.TPS900.ordinal() && deviceType != StringUtil.DeviceModelEnum.TPS900MB.ordinal()) {
                int open_msr = open_msr();
                if (open_msr == -3) {
                    throw new InternalErrorException("Cannot open magnetic stripe card reader!");
                }
                if (open_msr == -2) {
                    throw new DeviceAlreadyOpenException("The magnetic stripe card reader has been opened!");
                }
                return;
            }
            MagneticCard2.open(context);
        }
    }

    public static synchronized void close() {
        synchronized (MagneticCard.class) {
            int deviceType = SystemUtil.getDeviceType();
            if (deviceType != StringUtil.DeviceModelEnum.TPS900.ordinal() && deviceType != StringUtil.DeviceModelEnum.TPS900MB.ordinal()) {
                close_msr();
                return;
            }
            try {
                MagneticCard2.close();
            } catch (TelpoException e) {
                e.printStackTrace();
            }
        }
    }

    public static synchronized String[] check(int i) throws TelpoException {
        synchronized (MagneticCard.class) {
            int deviceType = SystemUtil.getDeviceType();
            if (deviceType != StringUtil.DeviceModelEnum.TPS900.ordinal() && deviceType != StringUtil.DeviceModelEnum.TPS900MB.ordinal()) {
                byte[] bArr = new byte[256];
                int check_msr = check_msr(i, bArr);
                if (check_msr == -4) {
                    throw new TimeoutException("Timeout to get magnetic stripe card data!");
                }
                if (check_msr == -3) {
                    throw new InternalErrorException("Cannot get magnetic stripe card data!");
                }
                if (check_msr == -1) {
                    throw new DeviceNotOpenException("The magnetic stripe card reader has not been opened!");
                }
                return ParseData(check_msr, bArr);
            }
            if (MagneticCard2.check(i) != 0) {
                throw new TimeoutException("Timeout to get magnetic stripe card data!");
            }
            return MagneticCard2.read();
        }
    }

    private static String[] ParseData(int i, byte[] bArr) throws TelpoException {
        String[] strArr = new String[3];
        byte b = bArr[0];
        if (b == 0) {
            strArr[0] = "";
        } else {
            try {
                strArr[0] = new String(bArr, 1, b, "GBK");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                throw new InternalErrorException();
            }
        }
        int i2 = bArr[0] + 1;
        byte b2 = bArr[i2];
        if (b2 == 0) {
            strArr[1] = "";
        } else {
            try {
                strArr[1] = new String(bArr, i2 + 1, b2, "GBK");
            } catch (UnsupportedEncodingException e2) {
                e2.printStackTrace();
                throw new InternalErrorException();
            }
        }
        int i3 = i2 + bArr[i2] + 1;
        byte b3 = bArr[i3];
        if (b3 == 0) {
            strArr[2] = "";
        } else {
            try {
                strArr[2] = new String(bArr, i3 + 1, b3, "GBK");
            } catch (UnsupportedEncodingException e3) {
                e3.printStackTrace();
                throw new InternalErrorException();
            }
        }
        return strArr;
    }

    public static int startReading() {
        int deviceType = SystemUtil.getDeviceType();
        if (deviceType == StringUtil.DeviceModelEnum.TPS900.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS900MB.ordinal()) {
            return 0;
        }
        return ready_for_read();
    }

    static {
        int deviceType = SystemUtil.getDeviceType();
        if (deviceType == StringUtil.DeviceModelEnum.TPS900.ordinal() || deviceType == StringUtil.DeviceModelEnum.TPS900MB.ordinal()) {
            return;
        }
        System.loadLibrary("telpo_msr");
    }
}
