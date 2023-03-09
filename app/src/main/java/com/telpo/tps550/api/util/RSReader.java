package com.telpo.tps550.api.util;

import android.content.Context;
import android.util.Log;
import com.telpo.tps550.api.InternalErrorException;
import com.telpo.tps550.api.InvalidDeviceStateException;
import com.telpo.tps550.api.TelpoException;
import com.telpo.tps550.api.TimeoutException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class RSReader {
    private StringBuffer buffer = new StringBuffer();

    public void closeReader() {
        this.buffer.setLength(0);
    }

    private List<byte[]> decodeReceive(String str) {
        ArrayList arrayList = new ArrayList();
        while (str.contains("0D0A")) {
            int indexOf = str.indexOf("0D0A");
            String substring = str.substring(0, indexOf);
            str = str.substring(indexOf + 4, str.length());
            arrayList.add(StringUtil.toBytes(substring));
        }
        this.buffer.setLength(0);
        this.buffer.append(str);
        Log.d("tagg", "buffer:" + this.buffer.toString());
        return arrayList;
    }

    public void rs_write(Context context, byte[] bArr, int i) throws TelpoException {
        try {
            Class<?> cls = Class.forName("com.common.sdk.rs232.RSServiceManager");
            Object systemService = context.getSystemService("RS232");
            try {
                try {
                    cls.getMethod("RsWrite", byte[].class, Integer.TYPE).invoke(systemService, bArr, Integer.valueOf(i));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    throw new InternalErrorException();
                } catch (IllegalArgumentException e2) {
                    e2.printStackTrace();
                    throw new InternalErrorException();
                } catch (InvocationTargetException e3) {
                    if (e3.getTargetException().toString().contains("Timeout")) {
                        throw new TimeoutException();
                    }
                    if (e3.getTargetException().toString().contains("InvalidDeviceState")) {
                        throw new InvalidDeviceStateException();
                    }
                    throw new TelpoException();
                }
            } catch (NoSuchMethodException e4) {
                e4.printStackTrace();
                throw new InternalErrorException();
            }
        } catch (ClassNotFoundException e5) {
            e5.printStackTrace();
            throw new InternalErrorException();
        }
    }

    public List<byte[]> rs_read(Context context, int i, int i2) throws TelpoException {
        return rs_read(context, i, i2, false);
    }

    public List<byte[]> rs_read(Context context, int i, int i2, boolean z) throws TelpoException {
        ArrayList arrayList;
        try {
            Class<?> cls = Class.forName("com.common.sdk.rs232.RSServiceManager");
            Object systemService = context.getSystemService("RS232");
            try {
                try {
                    byte[] bArr = (byte[]) cls.getMethod("RsRead", Integer.TYPE, Integer.TYPE).invoke(systemService, Integer.valueOf(i), Integer.valueOf(i2));
                    Log.d("tagg", "rs_read result:" + StringUtil.toHexString(bArr));
                    if (bArr != null) {
                        arrayList = new ArrayList();
                        arrayList.add(bArr);
                    } else {
                        arrayList = null;
                    }
                    if (!z || bArr == null) {
                        return arrayList;
                    }
                    this.buffer.append(StringUtil.toHexString(bArr));
                    return decodeReceive(this.buffer.toString());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    throw new InternalErrorException();
                } catch (IllegalArgumentException e2) {
                    e2.printStackTrace();
                    throw new InternalErrorException();
                } catch (InvocationTargetException e3) {
                    if (e3.getTargetException().toString().contains("Timeout")) {
                        throw new TimeoutException();
                    }
                    if (e3.getTargetException().toString().contains("InvalidDeviceState")) {
                        throw new InvalidDeviceStateException();
                    }
                    throw new TelpoException();
                }
            } catch (NoSuchMethodException e4) {
                e4.printStackTrace();
                throw new InternalErrorException();
            }
        } catch (ClassNotFoundException e5) {
            e5.printStackTrace();
            throw new InternalErrorException();
        }
    }

    public byte[] RsConfig(Context context, int i, byte b, byte b2, byte b3, byte b4) throws TelpoException {
        try {
            Class<?> cls = Class.forName("com.common.sdk.rs232.RSServiceManager");
            try {
                try {
                    return (byte[]) cls.getMethod("RsConfig", Integer.TYPE, Byte.TYPE, Byte.TYPE, Byte.TYPE, Byte.TYPE).invoke(context.getSystemService("RS232"), Integer.valueOf(i), Byte.valueOf(b), Byte.valueOf(b2), Byte.valueOf(b3), Byte.valueOf(b4));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    throw new InternalErrorException();
                } catch (IllegalArgumentException e2) {
                    e2.printStackTrace();
                    throw new InternalErrorException();
                } catch (InvocationTargetException e3) {
                    if (e3.getTargetException().toString().contains("Timeout")) {
                        throw new TimeoutException();
                    }
                    if (e3.getTargetException().toString().contains("InvalidDeviceState")) {
                        throw new InvalidDeviceStateException();
                    }
                    throw new TelpoException();
                }
            } catch (NoSuchMethodException e4) {
                e4.printStackTrace();
                throw new InternalErrorException();
            }
        } catch (ClassNotFoundException e5) {
            e5.printStackTrace();
            throw new InternalErrorException();
        }
    }

    public synchronized int HeatConfig(Context context, int i) throws TelpoException {
        Class<?> cls;
        Object systemService;
        try {
            try {
                cls = Class.forName("com.common.sdk.rs232.RSServiceManager");
                systemService = context.getSystemService("RS232");
                try {
                    try {
                        try {
                            try {
                            } catch (InvocationTargetException e) {
                                if (e.getTargetException().toString().contains("Timeout")) {
                                    throw new TimeoutException();
                                }
                                if (e.getTargetException().toString().contains("InvalidDeviceState")) {
                                    throw new InvalidDeviceStateException();
                                }
                                throw new TelpoException();
                            }
                        } catch (IllegalAccessException e2) {
                            e2.printStackTrace();
                            throw new InternalErrorException();
                        }
                    } catch (IllegalArgumentException e3) {
                        e3.printStackTrace();
                        throw new InternalErrorException();
                    }
                } catch (NoSuchMethodException e4) {
                    e4.printStackTrace();
                    throw new InternalErrorException();
                }
            } catch (ClassNotFoundException e5) {
                e5.printStackTrace();
                throw new InternalErrorException();
            }
        } catch (Throwable th) {
            throw th;
        }
        return ((Integer) cls.getMethod("HeatConfig", Integer.TYPE).invoke(systemService, Integer.valueOf(i))).intValue();
    }
}
