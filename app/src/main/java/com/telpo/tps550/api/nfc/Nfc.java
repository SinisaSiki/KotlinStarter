package com.telpo.tps550.api.nfc;

import android.content.Context;
import android.util.Log;
import com.telpo.tps550.api.DeviceAlreadyOpenException;
import com.telpo.tps550.api.DeviceNotOpenException;
import com.telpo.tps550.api.InternalErrorException;
import com.telpo.tps550.api.InvalidDeviceStateException;
import com.telpo.tps550.api.TelpoException;
import com.telpo.tps550.api.TimeoutException;
import java.lang.reflect.InvocationTargetException;

/* loaded from: classes.dex */
public class Nfc {
    public static final int MODE_CPU = 0;
    public static final int MODE_M1 = 1;
    private Context mContext;
    private boolean openFlag = false;

    public boolean getOpenFlag() {
        return this.openFlag;
    }

    public Nfc(Context context) {
        this.mContext = context;
    }

    public synchronized void open() throws TelpoException {
        if (this.openFlag) {
            throw new DeviceAlreadyOpenException();
        }
        try {
            Class<?> cls = Class.forName("com.common.sdk.nfc.NFCServiceManager");
            try {
                try {
                    try {
                        cls.getMethod("open", new Class[0]).invoke(this.mContext.getSystemService("NFC"), new Object[0]);
                        this.openFlag = true;
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new InternalErrorException();
                    }
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

    public synchronized void close() throws TelpoException {
        try {
            try {
                Class<?> cls = Class.forName("com.common.sdk.nfc.NFCServiceManager");
                try {
                    try {
                        try {
                            cls.getMethod("close", new Class[0]).invoke(this.mContext.getSystemService("NFC"), new Object[0]);
                            this.openFlag = false;
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
    }

    public synchronized byte[] activate(int i) throws TelpoException {
        Class<?> cls;
        Object systemService;
        if (!this.openFlag) {
            throw new DeviceNotOpenException();
        }
        try {
            cls = Class.forName("com.common.sdk.nfc.NFCServiceManager");
            systemService = this.mContext.getSystemService("NFC");
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
        return (byte[]) cls.getMethod("activate", Integer.TYPE).invoke(systemService, Integer.valueOf(i));
    }

    public synchronized byte[] cpu_get_ats() throws TelpoException {
        Class<?> cls;
        if (!this.openFlag) {
            throw new DeviceNotOpenException();
        }
        try {
            cls = Class.forName("com.common.sdk.nfc.NFCServiceManager");
            try {
                try {
                    try {
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new InternalErrorException();
                    }
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
        return (byte[]) cls.getMethod("cpu_get_ats", new Class[0]).invoke(this.mContext.getSystemService("NFC"), new Object[0]);
    }

    public synchronized byte[] transmit(byte[] bArr, int i) throws TelpoException {
        Class<?> cls;
        Object systemService;
        if (!this.openFlag) {
            throw new DeviceNotOpenException();
        }
        try {
            cls = Class.forName("com.common.sdk.nfc.NFCServiceManager");
            systemService = this.mContext.getSystemService("NFC");
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
        return (byte[]) cls.getMethod("cpu_transmit", byte[].class, Integer.TYPE).invoke(systemService, bArr, Integer.valueOf(i));
    }

    public synchronized byte[] m1_read_block(byte b) throws TelpoException {
        Class<?> cls;
        Object systemService;
        if (!this.openFlag) {
            throw new DeviceNotOpenException();
        }
        try {
            cls = Class.forName("com.common.sdk.nfc.NFCServiceManager");
            systemService = this.mContext.getSystemService("NFC");
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
        return (byte[]) cls.getMethod("m1_read_block", Byte.TYPE).invoke(systemService, Byte.valueOf(b));
    }

    public synchronized void m1_write_block(byte b, byte[] bArr, int i) throws TelpoException {
        if (!this.openFlag) {
            throw new DeviceNotOpenException();
        }
        try {
            Class<?> cls = Class.forName("com.common.sdk.nfc.NFCServiceManager");
            Object systemService = this.mContext.getSystemService("NFC");
            try {
                try {
                    try {
                        cls.getMethod("m1_write_block", Byte.TYPE, byte[].class, Integer.TYPE).invoke(systemService, Byte.valueOf(b), bArr, Integer.valueOf(i));
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                        throw new InternalErrorException();
                    }
                } catch (IllegalAccessException e2) {
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

    public synchronized byte[] m1_read_value(byte b) throws TelpoException {
        Class<?> cls;
        Object systemService;
        if (!this.openFlag) {
            throw new DeviceNotOpenException();
        }
        try {
            cls = Class.forName("com.common.sdk.nfc.NFCServiceManager");
            systemService = this.mContext.getSystemService("NFC");
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
        return (byte[]) cls.getMethod("m1_read_value", Byte.TYPE).invoke(systemService, Byte.valueOf(b));
    }

    public synchronized void m1_write_value(byte b, int i) throws TelpoException {
        if (!this.openFlag) {
            throw new DeviceNotOpenException();
        }
        try {
            Class<?> cls = Class.forName("com.common.sdk.nfc.NFCServiceManager");
            Object systemService = this.mContext.getSystemService("NFC");
            try {
                try {
                    try {
                        cls.getMethod("m1_write_value", Byte.TYPE, Integer.TYPE).invoke(systemService, Byte.valueOf(b), Integer.valueOf(i));
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
    }

    public synchronized byte[] m1_multi_sector_read(byte b, byte b2) throws TelpoException {
        Class<?> cls;
        Object systemService;
        if (!this.openFlag) {
            throw new DeviceNotOpenException();
        }
        try {
            cls = Class.forName("com.common.sdk.nfc.NFCServiceManager");
            systemService = this.mContext.getSystemService("NFC");
            try {
                try {
                    try {
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                        throw new InternalErrorException();
                    }
                } catch (IllegalAccessException e2) {
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
        return (byte[]) cls.getMethod("m1_multi_sector_read", Byte.TYPE, Byte.TYPE).invoke(systemService, Byte.valueOf(b), Byte.valueOf(b2));
    }

    public synchronized int m1_multi_sector_write(byte b, byte b2, byte[] bArr, int i) throws TelpoException {
        Class<?> cls;
        Object systemService;
        if (!this.openFlag) {
            throw new DeviceNotOpenException();
        }
        try {
            cls = Class.forName("com.common.sdk.nfc.NFCServiceManager");
            systemService = this.mContext.getSystemService("NFC");
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
        return ((Integer) cls.getMethod("m1_multi_sector_write", Byte.TYPE, Byte.TYPE, byte[].class, Integer.TYPE).invoke(systemService, Byte.valueOf(b), Byte.valueOf(b2), bArr, Integer.valueOf(i))).intValue();
    }

    public synchronized int m1_multi_sector_set_password(byte b, byte b2, byte[] bArr, int i) throws TelpoException {
        Class<?> cls;
        Object systemService;
        if (!this.openFlag) {
            throw new DeviceNotOpenException();
        }
        try {
            cls = Class.forName("com.common.sdk.nfc.NFCServiceManager");
            systemService = this.mContext.getSystemService("NFC");
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
        return ((Integer) cls.getMethod("m1_multi_sector_set_password", Byte.TYPE, Byte.TYPE, byte[].class, Integer.TYPE).invoke(systemService, Byte.valueOf(b), Byte.valueOf(b2), bArr, Integer.valueOf(i))).intValue();
    }

    public synchronized int m1_multi_sector_set_multi_password(byte b, byte b2, byte[] bArr, int i) throws TelpoException {
        Class<?> cls;
        Object systemService;
        if (!this.openFlag) {
            throw new DeviceNotOpenException();
        }
        try {
            cls = Class.forName("com.common.sdk.nfc.NFCServiceManager");
            systemService = this.mContext.getSystemService("NFC");
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
        return ((Integer) cls.getMethod("m1_multi_sector_set_multi_password", Byte.TYPE, Byte.TYPE, byte[].class, Integer.TYPE).invoke(systemService, Byte.valueOf(b), Byte.valueOf(b2), bArr, Integer.valueOf(i))).intValue();
    }

    public synchronized void m1_increment(byte b, byte b2, int i) throws TelpoException {
        if (!this.openFlag) {
            throw new DeviceNotOpenException();
        }
        try {
            Class<?> cls = Class.forName("com.common.sdk.nfc.NFCServiceManager");
            Object systemService = this.mContext.getSystemService("NFC");
            try {
                try {
                    try {
                        cls.getMethod("m1_increment", Byte.TYPE, Byte.TYPE, Integer.TYPE).invoke(systemService, Byte.valueOf(b), Byte.valueOf(b2), Integer.valueOf(i));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new InternalErrorException();
                    }
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

    public synchronized void m1_decrement(byte b, byte b2, int i) throws TelpoException {
        if (!this.openFlag) {
            throw new DeviceNotOpenException();
        }
        try {
            Class<?> cls = Class.forName("com.common.sdk.nfc.NFCServiceManager");
            Object systemService = this.mContext.getSystemService("NFC");
            try {
                try {
                    try {
                        cls.getMethod("m1_decrement", Byte.TYPE, Byte.TYPE, Integer.TYPE).invoke(systemService, Byte.valueOf(b), Byte.valueOf(b2), Integer.valueOf(i));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new InternalErrorException();
                    }
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

    public synchronized void halt() throws TelpoException {
        if (!this.openFlag) {
            throw new DeviceNotOpenException();
        }
        try {
            Class<?> cls = Class.forName("com.common.sdk.nfc.NFCServiceManager");
            try {
                try {
                    try {
                        cls.getMethod("halt", new Class[0]).invoke(this.mContext.getSystemService("NFC"), new Object[0]);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                        throw new InternalErrorException();
                    }
                } catch (IllegalAccessException e2) {
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

    public synchronized void remove(int i) throws TelpoException {
        if (!this.openFlag) {
            throw new DeviceNotOpenException();
        }
        try {
            Class<?> cls = Class.forName("com.common.sdk.nfc.NFCServiceManager");
            Object systemService = this.mContext.getSystemService("NFC");
            try {
                try {
                    cls.getMethod("remove", Integer.TYPE).invoke(systemService, Integer.valueOf(i));
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

    public synchronized void m1_authenticate(byte b, byte b2, byte[] bArr) throws TelpoException {
        if (!this.openFlag) {
            throw new DeviceNotOpenException();
        }
        try {
            Class<?> cls = Class.forName("com.common.sdk.nfc.NFCServiceManager");
            Object systemService = this.mContext.getSystemService("NFC");
            try {
                try {
                    try {
                        cls.getMethod("m1_authenticate", Byte.TYPE, Byte.TYPE, byte[].class).invoke(systemService, Byte.valueOf(b), Byte.valueOf(b2), bArr);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                        throw new InternalErrorException();
                    }
                } catch (IllegalAccessException e2) {
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

    public synchronized byte[] read_idcard(int i) throws TelpoException {
        Class<?> cls;
        Object systemService;
        if (!this.openFlag) {
            throw new DeviceNotOpenException();
        }
        try {
            cls = Class.forName("com.common.sdk.nfc.NFCServiceManager");
            systemService = this.mContext.getSystemService("NFC");
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
        return (byte[]) cls.getMethod("read_idcard", Integer.TYPE).invoke(systemService, Integer.valueOf(i));
    }

    public synchronized void mp_sl0_personalization(byte[] bArr, int i, byte[] bArr2, int i2) throws TelpoException {
        if (!this.openFlag) {
            throw new DeviceNotOpenException();
        }
        for (int i3 = 0; i3 < bArr.length; i3++) {
            Log.w("Nfc", "keyBuf = " + ((int) bArr[i3]));
        }
        for (int i4 = 0; i4 < bArr2.length; i4++) {
            Log.w("Nfc", "dataBuf = " + ((int) bArr2[i4]));
        }
        Log.w("Nfc", "keyBuf len = " + bArr.length + "  keyLen = " + i);
        Log.w("Nfc", "dataBuf len = " + bArr2.length + "  dataLen = " + i2);
        try {
            Class<?> cls = Class.forName("com.common.sdk.nfc.NFCServiceManager");
            try {
                try {
                    try {
                        try {
                            cls.getMethod("mp_sl0_personalization", byte[].class, Integer.TYPE, byte[].class, Integer.TYPE).invoke(this.mContext.getSystemService("NFC"), bArr, Integer.valueOf(i), bArr2, Integer.valueOf(i2));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                            throw new InternalErrorException();
                        }
                    } catch (InvocationTargetException e2) {
                        e2.printStackTrace();
                        Exception exc = (Exception) e2.getTargetException();
                        if (exc instanceof TelpoException) {
                            throw ((TelpoException) exc);
                        }
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
    }

    public synchronized void mp_sl0_commit() throws TelpoException {
        if (!this.openFlag) {
            throw new DeviceNotOpenException();
        }
        try {
            Class<?> cls = Class.forName("com.common.sdk.nfc.NFCServiceManager");
            try {
                try {
                    try {
                        cls.getMethod("mp_sl0_commit", new Class[0]).invoke(this.mContext.getSystemService("NFC"), new Object[0]);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new InternalErrorException();
                    }
                } catch (IllegalArgumentException e2) {
                    e2.printStackTrace();
                    throw new InternalErrorException();
                } catch (InvocationTargetException e3) {
                    e3.printStackTrace();
                    Exception exc = (Exception) e3.getTargetException();
                    if (exc instanceof TelpoException) {
                        throw ((TelpoException) exc);
                    }
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
    }

    public synchronized void mp_sl1_switch_sl3(byte[] bArr) throws TelpoException {
        if (!this.openFlag) {
            throw new DeviceNotOpenException();
        }
        try {
            Class<?> cls = Class.forName("com.common.sdk.nfc.NFCServiceManager");
            try {
                try {
                    cls.getMethod("mp_sl1_switch_sl3", byte[].class).invoke(this.mContext.getSystemService("NFC"), bArr);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    throw new InternalErrorException();
                } catch (IllegalArgumentException e2) {
                    e2.printStackTrace();
                    throw new InternalErrorException();
                } catch (InvocationTargetException e3) {
                    e3.printStackTrace();
                    Exception exc = (Exception) e3.getTargetException();
                    if (exc instanceof TelpoException) {
                        throw ((TelpoException) exc);
                    }
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
    }

    public synchronized void mp_sl3_auth(byte b, byte b2, byte[] bArr) throws TelpoException {
        Log.w("Nfc", "enter in the mp_sl3_auth");
        Log.w("Nfc", "noBlock = " + ((int) b));
        Log.w("Nfc", "key = " + ((int) b2));
        for (int i = 0; i < 16; i++) {
            Log.w("Nfc", "aesKey = " + ((int) bArr[i]));
        }
        if (!this.openFlag) {
            throw new DeviceNotOpenException();
        }
        try {
            Class<?> cls = Class.forName("com.common.sdk.nfc.NFCServiceManager");
            try {
                try {
                    try {
                        cls.getMethod("mp_sl3_auth", Byte.TYPE, Byte.TYPE, byte[].class).invoke(this.mContext.getSystemService("NFC"), Byte.valueOf(b), Byte.valueOf(b2), bArr);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                        Log.e("Nfc", "the mp_sl3_auth invoke fail");
                        throw new InternalErrorException();
                    }
                } catch (IllegalAccessException e2) {
                    e2.printStackTrace();
                    Log.e("Nfc", "the mp_sl3_auth invoke fail 2");
                    throw new InternalErrorException();
                } catch (InvocationTargetException e3) {
                    e3.printStackTrace();
                    Log.e("Nfc", "the mp_sl3_auth invoke fail 3");
                    Exception exc = (Exception) e3.getTargetException();
                    if (exc instanceof TelpoException) {
                        Log.e("Nfc", "the mp_sl3_auth invoke fail 4");
                        throw ((TelpoException) exc);
                    } else {
                        Log.e("Nfc", "the mp_sl3_auth invoke fail 5");
                        throw new InternalErrorException();
                    }
                }
            } catch (NoSuchMethodException e4) {
                e4.printStackTrace();
                Log.e("Nfc", "the mp_sl3_auth method fail");
                throw new InternalErrorException();
            }
        } catch (ClassNotFoundException e5) {
            e5.printStackTrace();
            Log.e("Nfc", "mp_sl3_auth fail");
            throw new InternalErrorException();
        }
    }

    public synchronized byte[] mp_sl3_readblock_plain(byte b) throws TelpoException {
        Class<?> cls;
        Object systemService;
        if (!this.openFlag) {
            throw new DeviceNotOpenException();
        }
        try {
            cls = Class.forName("com.common.sdk.nfc.NFCServiceManager");
            systemService = this.mContext.getSystemService("NFC");
            try {
                try {
                    try {
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new InternalErrorException();
                    }
                } catch (IllegalArgumentException e2) {
                    e2.printStackTrace();
                    throw new InternalErrorException();
                } catch (InvocationTargetException e3) {
                    e3.printStackTrace();
                    Exception exc = (Exception) e3.getTargetException();
                    if (exc instanceof TelpoException) {
                        throw ((TelpoException) exc);
                    }
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
        return (byte[]) cls.getMethod("mp_sl3_readblock_plain", Byte.TYPE).invoke(systemService, Byte.valueOf(b));
    }

    public synchronized void mp_sl3_writeblock_plain(byte b, byte[] bArr, int i) throws TelpoException {
        if (!this.openFlag) {
            throw new DeviceNotOpenException();
        }
        Log.w("Nfc", "enter in the mp_sl3_writeblock_plain");
        Log.w("Nfc", "noBlock = " + ((int) b));
        try {
            Class<?> cls = Class.forName("com.common.sdk.nfc.NFCServiceManager");
            for (int i2 = 0; i2 < i; i2++) {
                Log.w("Nfc", "inBuf = " + ((int) bArr[i2]));
            }
            try {
                try {
                    cls.getMethod("mp_sl3_writeblock_plain", Byte.TYPE, byte[].class, Integer.TYPE).invoke(this.mContext.getSystemService("NFC"), Byte.valueOf(b), bArr, Integer.valueOf(i));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    throw new InternalErrorException();
                } catch (IllegalArgumentException e2) {
                    e2.printStackTrace();
                    throw new InternalErrorException();
                } catch (InvocationTargetException e3) {
                    e3.printStackTrace();
                    Exception exc = (Exception) e3.getTargetException();
                    if (exc instanceof TelpoException) {
                        throw ((TelpoException) exc);
                    }
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
    }

    public synchronized byte[] get_uid() throws TelpoException {
        Class<?> cls;
        if (!this.openFlag) {
            throw new DeviceNotOpenException();
        }
        try {
            cls = Class.forName("com.common.sdk.nfc.NFCServiceManager");
            try {
                try {
                    try {
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new InternalErrorException();
                    }
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
        return (byte[]) cls.getMethod("get_uid", new Class[0]).invoke(this.mContext.getSystemService("NFC"), new Object[0]);
    }

    public synchronized byte[] ultraigh_read_block(byte b) throws TelpoException {
        Class<?> cls;
        Object systemService;
        if (!this.openFlag) {
            throw new DeviceNotOpenException();
        }
        try {
            cls = Class.forName("com.common.sdk.nfc.NFCServiceManager");
            systemService = this.mContext.getSystemService("NFC");
            try {
                try {
                    try {
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new InternalErrorException();
                    }
                } catch (IllegalArgumentException e2) {
                    e2.printStackTrace();
                    throw new InternalErrorException();
                } catch (InvocationTargetException e3) {
                    e3.printStackTrace();
                    Exception exc = (Exception) e3.getTargetException();
                    if (exc instanceof TelpoException) {
                        throw ((TelpoException) exc);
                    }
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
        return (byte[]) cls.getMethod("ultraigh_read_block", Byte.TYPE).invoke(systemService, Byte.valueOf(b));
    }

    public synchronized byte[] ultraigh_read_page(byte b) throws TelpoException {
        Class<?> cls;
        Object systemService;
        if (!this.openFlag) {
            throw new DeviceNotOpenException();
        }
        try {
            cls = Class.forName("com.common.sdk.nfc.NFCServiceManager");
            systemService = this.mContext.getSystemService("NFC");
            try {
                try {
                    try {
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new InternalErrorException();
                    }
                } catch (IllegalArgumentException e2) {
                    e2.printStackTrace();
                    throw new InternalErrorException();
                } catch (InvocationTargetException e3) {
                    e3.printStackTrace();
                    Exception exc = (Exception) e3.getTargetException();
                    if (exc instanceof TelpoException) {
                        throw ((TelpoException) exc);
                    }
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
        return (byte[]) cls.getMethod("ultraigh_read_page", Byte.TYPE).invoke(systemService, Byte.valueOf(b));
    }

    public synchronized void ultraigh_write_block(byte b, byte[] bArr, int i) throws TelpoException {
        if (!this.openFlag) {
            throw new DeviceNotOpenException();
        }
        try {
            Class<?> cls = Class.forName("com.common.sdk.nfc.NFCServiceManager");
            Object systemService = this.mContext.getSystemService("NFC");
            try {
                try {
                    try {
                        cls.getMethod("ultraigh_write_block", Byte.TYPE, byte[].class, Integer.TYPE).invoke(systemService, Byte.valueOf(b), bArr, Integer.valueOf(i));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new InternalErrorException();
                    }
                } catch (IllegalArgumentException e2) {
                    e2.printStackTrace();
                    throw new InternalErrorException();
                } catch (InvocationTargetException e3) {
                    e3.printStackTrace();
                    Exception exc = (Exception) e3.getTargetException();
                    if (exc instanceof TelpoException) {
                        throw ((TelpoException) exc);
                    }
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
    }

    public synchronized void ultraigh_write_page(byte b, byte[] bArr, int i) throws TelpoException {
        if (!this.openFlag) {
            throw new DeviceNotOpenException();
        }
        try {
            Class<?> cls = Class.forName("com.common.sdk.nfc.NFCServiceManager");
            Object systemService = this.mContext.getSystemService("NFC");
            try {
                try {
                    try {
                        cls.getMethod("ultraigh_write_page", Byte.TYPE, byte[].class, Integer.TYPE).invoke(systemService, Byte.valueOf(b), bArr, Integer.valueOf(i));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new InternalErrorException();
                    }
                } catch (IllegalArgumentException e2) {
                    e2.printStackTrace();
                    throw new InternalErrorException();
                } catch (InvocationTargetException e3) {
                    e3.printStackTrace();
                    Exception exc = (Exception) e3.getTargetException();
                    if (exc instanceof TelpoException) {
                        throw ((TelpoException) exc);
                    }
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
    }

    public synchronized void ultraigh_write_key(byte[] bArr) throws TelpoException {
        if (!this.openFlag) {
            throw new DeviceNotOpenException();
        }
        try {
            Class<?> cls = Class.forName("com.common.sdk.nfc.NFCServiceManager");
            try {
                try {
                    cls.getMethod("ultraigh_write_key", byte[].class).invoke(this.mContext.getSystemService("NFC"), bArr);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    throw new InternalErrorException();
                } catch (IllegalArgumentException e2) {
                    e2.printStackTrace();
                    throw new InternalErrorException();
                } catch (InvocationTargetException e3) {
                    e3.printStackTrace();
                    Exception exc = (Exception) e3.getTargetException();
                    if (exc instanceof TelpoException) {
                        throw ((TelpoException) exc);
                    }
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
    }

    public synchronized void ultraigh_auth(byte[] bArr) throws TelpoException {
        if (!this.openFlag) {
            throw new DeviceNotOpenException();
        }
        try {
            Class<?> cls = Class.forName("com.common.sdk.nfc.NFCServiceManager");
            try {
                try {
                    cls.getMethod("ultraigh_auth", byte[].class).invoke(this.mContext.getSystemService("NFC"), bArr);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    throw new InternalErrorException();
                } catch (IllegalArgumentException e2) {
                    e2.printStackTrace();
                    throw new InternalErrorException();
                } catch (InvocationTargetException e3) {
                    e3.printStackTrace();
                    Exception exc = (Exception) e3.getTargetException();
                    if (exc instanceof TelpoException) {
                        throw ((TelpoException) exc);
                    }
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
    }

    public synchronized void ultraigh_auth_config(byte b, byte b2) throws TelpoException {
        if (!this.openFlag) {
            throw new DeviceNotOpenException();
        }
        try {
            Class<?> cls = Class.forName("com.common.sdk.nfc.NFCServiceManager");
            Object systemService = this.mContext.getSystemService("NFC");
            try {
                try {
                    try {
                        cls.getMethod("ultraigh_auth_config", Byte.TYPE, Byte.TYPE).invoke(systemService, Byte.valueOf(b), Byte.valueOf(b2));
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
    }

    public synchronized byte[] ultraigh_exchange(byte[] bArr, int i) throws TelpoException {
        Class<?> cls;
        Object systemService;
        if (!this.openFlag) {
            throw new DeviceNotOpenException();
        }
        try {
            cls = Class.forName("com.common.sdk.nfc.NFCServiceManager");
            systemService = this.mContext.getSystemService("NFC");
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
        return (byte[]) cls.getMethod("ultraigh_exchange", byte[].class, Integer.TYPE).invoke(systemService, bArr, Integer.valueOf(i));
    }

    public synchronized int md_auth(byte b, byte b2, byte[] bArr) throws TelpoException {
        Class<?> cls;
        Object systemService;
        if (!this.openFlag) {
            throw new DeviceNotOpenException();
        }
        try {
            cls = Class.forName("com.common.sdk.nfc.NFCServiceManager");
            systemService = this.mContext.getSystemService("NFC");
            try {
                try {
                    try {
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new InternalErrorException();
                    }
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
        return ((Integer) cls.getMethod("md_auth", Byte.TYPE, Byte.TYPE, byte[].class).invoke(systemService, Byte.valueOf(b), Byte.valueOf(b2), bArr)).intValue();
    }

    public synchronized int md_change_key(byte b, byte[] bArr) throws TelpoException {
        Class<?> cls;
        Object systemService;
        if (!this.openFlag) {
            throw new DeviceNotOpenException();
        }
        try {
            cls = Class.forName("com.common.sdk.nfc.NFCServiceManager");
            systemService = this.mContext.getSystemService("NFC");
            try {
                try {
                    try {
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                        throw new InternalErrorException();
                    }
                } catch (IllegalAccessException e2) {
                    e2.printStackTrace();
                    throw new InternalErrorException();
                } catch (InvocationTargetException e3) {
                    e3.printStackTrace();
                    Exception exc = (Exception) e3.getTargetException();
                    if (exc instanceof TelpoException) {
                        throw ((TelpoException) exc);
                    }
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
        return ((Integer) cls.getMethod("md_change_key", Byte.TYPE, byte[].class).invoke(systemService, Byte.valueOf(b), bArr)).intValue();
    }

    public synchronized int md_create_app(int i, byte b, byte b2) throws TelpoException {
        Class<?> cls;
        Object systemService;
        if (!this.openFlag) {
            throw new DeviceNotOpenException();
        }
        try {
            cls = Class.forName("com.common.sdk.nfc.NFCServiceManager");
            systemService = this.mContext.getSystemService("NFC");
            try {
                try {
                    try {
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                        throw new InternalErrorException();
                    }
                } catch (IllegalAccessException e2) {
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
        return ((Integer) cls.getMethod("md_create_app", Integer.TYPE, Byte.TYPE, Byte.TYPE).invoke(systemService, Integer.valueOf(i), Byte.valueOf(b), Byte.valueOf(b2))).intValue();
    }

    public synchronized int md_del_app(int i) throws TelpoException {
        Class<?> cls;
        Object systemService;
        if (!this.openFlag) {
            throw new DeviceNotOpenException();
        }
        try {
            cls = Class.forName("com.common.sdk.nfc.NFCServiceManager");
            systemService = this.mContext.getSystemService("NFC");
            try {
                try {
                    try {
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new InternalErrorException();
                    }
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
        return ((Integer) cls.getMethod("md_del_app", Integer.TYPE).invoke(systemService, Integer.valueOf(i))).intValue();
    }

    public synchronized byte[] md_get_app_ids() throws TelpoException {
        Class<?> cls;
        if (!this.openFlag) {
            throw new DeviceNotOpenException();
        }
        try {
            cls = Class.forName("com.common.sdk.nfc.NFCServiceManager");
            try {
                try {
                    try {
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new InternalErrorException();
                    }
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
        return (byte[]) cls.getMethod("md_get_app_ids", new Class[0]).invoke(this.mContext.getSystemService("NFC"), new Object[0]);
    }

    public synchronized int md_select_app(int i) throws TelpoException {
        Class<?> cls;
        Object systemService;
        if (!this.openFlag) {
            throw new DeviceNotOpenException();
        }
        try {
            cls = Class.forName("com.common.sdk.nfc.NFCServiceManager");
            systemService = this.mContext.getSystemService("NFC");
            try {
                try {
                    try {
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new InternalErrorException();
                    }
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
        return ((Integer) cls.getMethod("md_select_app", Integer.TYPE).invoke(systemService, Integer.valueOf(i))).intValue();
    }

    public synchronized void changeMode(int i) throws TelpoException {
        if (!this.openFlag) {
            throw new DeviceNotOpenException();
        }
        try {
            Class<?> cls = Class.forName("com.common.sdk.nfc.NFCServiceManager");
            Object systemService = this.mContext.getSystemService("NFC");
            try {
                try {
                    cls.getMethod("changeMode", Integer.TYPE).invoke(systemService, Integer.valueOf(i));
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

    public synchronized byte[] md_get_file_ids() throws TelpoException {
        Class<?> cls;
        if (!this.openFlag) {
            throw new DeviceNotOpenException();
        }
        try {
            cls = Class.forName("com.common.sdk.nfc.NFCServiceManager");
            try {
                try {
                    try {
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new InternalErrorException();
                    }
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
        return (byte[]) cls.getMethod("md_get_file_ids", new Class[0]).invoke(this.mContext.getSystemService("NFC"), new Object[0]);
    }

    public synchronized int md_create_std_data_file(byte b, byte b2, byte[] bArr, int i, int i2) throws TelpoException {
        Class<?> cls;
        Object systemService;
        if (!this.openFlag) {
            throw new DeviceNotOpenException();
        }
        try {
            cls = Class.forName("com.common.sdk.nfc.NFCServiceManager");
            systemService = this.mContext.getSystemService("NFC");
            try {
                try {
                    try {
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                        Exception exc = (Exception) e.getTargetException();
                        if (exc instanceof TelpoException) {
                            throw ((TelpoException) exc);
                        }
                        throw new InternalErrorException();
                    }
                } catch (IllegalAccessException e2) {
                    e2.printStackTrace();
                    throw new InternalErrorException();
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
        return ((Integer) cls.getMethod("md_create_std_data_file", Byte.TYPE, Byte.TYPE, byte[].class, Integer.TYPE, Integer.TYPE).invoke(systemService, Byte.valueOf(b), Byte.valueOf(b2), bArr, Integer.valueOf(i), Integer.valueOf(i2))).intValue();
    }

    public synchronized int md_del_file(byte b) throws TelpoException {
        Class<?> cls;
        Object systemService;
        if (!this.openFlag) {
            throw new DeviceNotOpenException();
        }
        try {
            cls = Class.forName("com.common.sdk.nfc.NFCServiceManager");
            systemService = this.mContext.getSystemService("NFC");
            try {
                try {
                    try {
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new InternalErrorException();
                    }
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
        return ((Integer) cls.getMethod("md_del_file", Byte.TYPE).invoke(systemService, Byte.valueOf(b))).intValue();
    }

    public synchronized byte[] md_read_data(byte b, int i, int i2) throws TelpoException {
        Class<?> cls;
        Object systemService;
        if (!this.openFlag) {
            throw new DeviceNotOpenException();
        }
        try {
            cls = Class.forName("com.common.sdk.nfc.NFCServiceManager");
            systemService = this.mContext.getSystemService("NFC");
            try {
                try {
                    try {
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new InternalErrorException();
                    }
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
        return (byte[]) cls.getMethod("md_read_data", Byte.TYPE, Integer.TYPE, Integer.TYPE).invoke(systemService, Byte.valueOf(b), Integer.valueOf(i), Integer.valueOf(i2));
    }

    public synchronized int md_write_data(byte b, int i, int i2, byte[] bArr, int i3) throws TelpoException {
        Class<?> cls;
        Object systemService;
        if (!this.openFlag) {
            throw new DeviceNotOpenException();
        }
        try {
            cls = Class.forName("com.common.sdk.nfc.NFCServiceManager");
            systemService = this.mContext.getSystemService("NFC");
            try {
                try {
                    try {
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                        Exception exc = (Exception) e.getTargetException();
                        if (exc instanceof TelpoException) {
                            throw ((TelpoException) exc);
                        }
                        throw new InternalErrorException();
                    }
                } catch (IllegalAccessException e2) {
                    e2.printStackTrace();
                    throw new InternalErrorException();
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
        return ((Integer) cls.getMethod("md_write_data", Byte.TYPE, Integer.TYPE, Integer.TYPE, byte[].class, Integer.TYPE).invoke(systemService, Byte.valueOf(b), Integer.valueOf(i), Integer.valueOf(i2), bArr, Integer.valueOf(i3))).intValue();
    }

    public synchronized int md_factory_reset() throws TelpoException {
        Class<?> cls;
        if (!this.openFlag) {
            throw new DeviceNotOpenException();
        }
        try {
            cls = Class.forName("com.common.sdk.nfc.NFCServiceManager");
            try {
                try {
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    throw new InternalErrorException();
                } catch (IllegalArgumentException e2) {
                    e2.printStackTrace();
                    throw new InternalErrorException();
                } catch (InvocationTargetException e3) {
                    e3.printStackTrace();
                    Exception exc = (Exception) e3.getTargetException();
                    if (exc instanceof TelpoException) {
                        throw ((TelpoException) exc);
                    }
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
        return ((Integer) cls.getMethod("md_factory_reset", new Class[0]).invoke(this.mContext.getSystemService("NFC"), new Object[0])).intValue();
    }
}
