package com.telpo.tps550.api.reader;

import android.content.Context;
import android.util.Log;
import com.telpo.tps550.api.InternalErrorException;
import com.telpo.tps550.api.TelpoException;
import com.telpo.tps550.api.fingerprint.FingerPrint;
import com.telpo.tps550.api.util.StringUtil;
import com.telpo.tps550.api.util.SystemUtil;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/* loaded from: classes.dex */
public class ICCardReader {
    private String internalModel = SystemUtil.getInternalModel();
    private Context mContext;

    public ICCardReader(Context context) {
        this.mContext = null;
        this.mContext = context;
    }

    public synchronized void open(int i) throws TelpoException {
        Class<?> cls;
        Log.d("tagg", "ICCardReader open()>> slot=" + i);
        if (SystemUtil.getDeviceType() == StringUtil.DeviceModelEnum.TPS360IC.ordinal()) {
            FingerPrint.iccardPower(1);
        } else {
            "TPS450P".equals(this.internalModel);
        }
        try {
            if ("TPS580P".equals(this.internalModel)) {
                cls = Class.forName("com.common.telpo.sdk.iccard.ICCardServiceManager");
            } else {
                cls = Class.forName("com.common.sdk.iccard.ICCardServiceManager");
            }
            Object systemService = this.mContext.getSystemService("ICCard");
            try {
                try {
                    try {
                        cls.getMethod("open", Integer.TYPE).invoke(systemService, Integer.valueOf(i));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new InternalErrorException();
                    }
                } catch (IllegalArgumentException e2) {
                    e2.printStackTrace();
                    throw new InternalErrorException();
                } catch (InvocationTargetException e3) {
                    throwException(e3);
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

    public synchronized void close(int i) throws TelpoException {
        Class<?> cls;
        try {
            try {
                if ("TPS580P".equals(this.internalModel)) {
                    cls = Class.forName("com.common.telpo.sdk.iccard.ICCardServiceManager");
                } else {
                    cls = Class.forName("com.common.sdk.iccard.ICCardServiceManager");
                }
                Object systemService = this.mContext.getSystemService("ICCard");
                try {
                    try {
                        try {
                            cls.getMethod("close", Integer.TYPE).invoke(systemService, Integer.valueOf(i));
                        } catch (InvocationTargetException e) {
                            throwException(e);
                        }
                        if ("TPS450P".equals(this.internalModel) && i != 1) {
                            Log.d("tagg", "450P iccard power off");
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

    public synchronized int detect(int i, int i2) throws TelpoException {
        Class<?> cls;
        int i3;
        try {
            try {
                if ("TPS580P".equals(this.internalModel)) {
                    cls = Class.forName("com.common.telpo.sdk.iccard.ICCardServiceManager");
                } else {
                    cls = Class.forName("com.common.sdk.iccard.ICCardServiceManager");
                }
                Object systemService = this.mContext.getSystemService("ICCard");
                try {
                    i3 = 0;
                    try {
                        try {
                            try {
                                i3 = ((Integer) cls.getMethod("detect", Integer.TYPE, Integer.TYPE).invoke(systemService, Integer.valueOf(i), Integer.valueOf(i2))).intValue();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                                throw new InternalErrorException();
                            }
                        } catch (InvocationTargetException e2) {
                            throwException(e2);
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
        return i3;
    }

    public synchronized void power_on(int i) throws TelpoException {
        Class<?> cls;
        try {
            try {
                if ("TPS580P".equals(this.internalModel)) {
                    cls = Class.forName("com.common.telpo.sdk.iccard.ICCardServiceManager");
                } else {
                    cls = Class.forName("com.common.sdk.iccard.ICCardServiceManager");
                }
                Object systemService = this.mContext.getSystemService("ICCard");
                try {
                    try {
                        try {
                            cls.getMethod("power_on", Integer.TYPE).invoke(systemService, Integer.valueOf(i));
                        } catch (InvocationTargetException e) {
                            throwException(e);
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

    public synchronized String getAtr(int i) throws TelpoException {
        Class<?> cls;
        String str;
        try {
            try {
                if ("TPS580P".equals(this.internalModel)) {
                    cls = Class.forName("com.common.telpo.sdk.iccard.ICCardServiceManager");
                } else {
                    cls = Class.forName("com.common.sdk.iccard.ICCardServiceManager");
                }
                Object systemService = this.mContext.getSystemService("ICCard");
                try {
                    try {
                        try {
                            str = (String) cls.getMethod("get_atr", Integer.TYPE).invoke(systemService, Integer.valueOf(i));
                        } catch (InvocationTargetException e) {
                            throwException(e);
                            str = null;
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
        return str;
    }

    public synchronized byte[] transmit(int i, byte[] bArr, int i2) throws TelpoException {
        Class<?> cls;
        byte[] bArr2;
        try {
            try {
                if ("TPS580P".equals(this.internalModel)) {
                    cls = Class.forName("com.common.telpo.sdk.iccard.ICCardServiceManager");
                } else {
                    cls = Class.forName("com.common.sdk.iccard.ICCardServiceManager");
                }
                Object systemService = this.mContext.getSystemService("ICCard");
                try {
                    try {
                        try {
                            bArr2 = (byte[]) cls.getMethod("transmit", Integer.TYPE, byte[].class, Integer.TYPE).invoke(systemService, Integer.valueOf(i), bArr, Integer.valueOf(i2));
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                            throw new InternalErrorException();
                        }
                    } catch (IllegalAccessException e2) {
                        e2.printStackTrace();
                        throw new InternalErrorException();
                    } catch (InvocationTargetException e3) {
                        throwException(e3);
                        bArr2 = null;
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
        return bArr2;
    }

    public synchronized void power_off(int i) throws TelpoException {
        Class<?> cls;
        try {
            try {
                if ("TPS580P".equals(this.internalModel)) {
                    cls = Class.forName("com.common.telpo.sdk.iccard.ICCardServiceManager");
                } else {
                    cls = Class.forName("com.common.sdk.iccard.ICCardServiceManager");
                }
                Object systemService = this.mContext.getSystemService("ICCard");
                try {
                    try {
                        try {
                            cls.getMethod("power_off", Integer.TYPE).invoke(systemService, Integer.valueOf(i));
                        } catch (InvocationTargetException e) {
                            throwException(e);
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

    public synchronized int set_mode(int i, int i2) throws TelpoException {
        Class<?> cls;
        int i3;
        try {
            try {
                if ("TPS580P".equals(this.internalModel)) {
                    cls = Class.forName("com.common.telpo.sdk.iccard.ICCardServiceManager");
                } else {
                    cls = Class.forName("com.common.sdk.iccard.ICCardServiceManager");
                }
                Object systemService = this.mContext.getSystemService("ICCard");
                try {
                    i3 = 0;
                    try {
                        try {
                            try {
                                i3 = ((Integer) cls.getMethod("set_mode", Integer.TYPE, Integer.TYPE).invoke(systemService, Integer.valueOf(i), Integer.valueOf(i2))).intValue();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                                throw new InternalErrorException();
                            }
                        } catch (InvocationTargetException e2) {
                            throwException(e2);
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
        return i3;
    }

    public synchronized void open(int i, int i2) throws TelpoException {
        Class<?> cls;
        try {
            try {
                if ("TPS580P".equals(this.internalModel)) {
                    cls = Class.forName("com.common.telpo.sdk.iccard.ICCardServiceManager");
                } else {
                    cls = Class.forName("com.common.sdk.iccard.ICCardServiceManager");
                }
                Object systemService = this.mContext.getSystemService("ICCard");
                try {
                    try {
                        try {
                            cls.getMethod("open", Integer.TYPE, Integer.TYPE).invoke(systemService, Integer.valueOf(i), Integer.valueOf(i2));
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
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized int AT24CXX_write(int i, int i2, byte[] bArr, int i3) throws TelpoException {
        Class<?> cls;
        Object systemService;
        try {
            try {
                if ("TPS580P".equals(this.internalModel)) {
                    cls = Class.forName("com.common.telpo.sdk.iccard.ICCardServiceManager");
                } else {
                    cls = Class.forName("com.common.sdk.iccard.ICCardServiceManager");
                }
                systemService = this.mContext.getSystemService("ICCard");
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
        } catch (Throwable th) {
            throw th;
        }
        return ((Integer) cls.getMethod("AT24CXX_write", Integer.TYPE, Integer.TYPE, byte[].class, Integer.TYPE).invoke(systemService, Integer.valueOf(i), Integer.valueOf(i2), bArr, Integer.valueOf(i3))).intValue();
    }

    public synchronized int AT24CXX_read(int i, int i2, int i3, byte[] bArr, int[] iArr) throws TelpoException {
        Class<?> cls;
        Object systemService;
        try {
            try {
                if ("TPS580P".equals(this.internalModel)) {
                    cls = Class.forName("com.common.telpo.sdk.iccard.ICCardServiceManager");
                } else {
                    cls = Class.forName("com.common.sdk.iccard.ICCardServiceManager");
                }
                systemService = this.mContext.getSystemService("ICCard");
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
        } catch (Throwable th) {
            throw th;
        }
        return ((Integer) cls.getMethod("AT24CXX_read", Integer.TYPE, Integer.TYPE, Integer.TYPE, byte[].class, int[].class).invoke(systemService, Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3), bArr, iArr)).intValue();
    }

    public synchronized int switchMode(int i, int i2) {
        Class<?> cls;
        Object invoke;
        Method method = null;
        try {
            cls = Class.forName("com.common.sdk.iccard.ICCardServiceManager");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            cls = null;
        }
        Object systemService = this.mContext.getSystemService("ICCard");
        try {
            method = cls.getMethod("icc_switch_mode", Integer.TYPE, Integer.TYPE);
        } catch (NoSuchMethodException e2) {
            e2.printStackTrace();
        }
        int i3 = -1;
        try {
            try {
                invoke = method.invoke(systemService, Integer.valueOf(i), Integer.valueOf(i2));
            } catch (IllegalArgumentException e3) {
                e3.printStackTrace();
            }
        } catch (IllegalAccessException e4) {
            e4.printStackTrace();
        } catch (InvocationTargetException e5) {
            e5.printStackTrace();
        }
        if (invoke == null) {
            return -1;
        }
        i3 = ((Integer) invoke).intValue();
        return i3;
    }

    public synchronized int verifyPsc(int i, byte[] bArr) {
        Class<?> cls;
        int i2;
        Object invoke;
        Method method = null;
        try {
            cls = Class.forName("com.common.sdk.iccard.ICCardServiceManager");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            cls = null;
        }
        Object systemService = this.mContext.getSystemService("ICCard");
        try {
            method = cls.getMethod("icc_verify_psc", Integer.TYPE, byte[].class);
        } catch (NoSuchMethodException e2) {
            e2.printStackTrace();
        }
        try {
            try {
                invoke = method.invoke(systemService, Integer.valueOf(i), bArr);
            } catch (InvocationTargetException e3) {
                e3.printStackTrace();
                i2 = -100;
                return i2;
            }
        } catch (IllegalAccessException e4) {
            e4.printStackTrace();
            i2 = -100;
            return i2;
        } catch (IllegalArgumentException e5) {
            e5.printStackTrace();
            i2 = -100;
            return i2;
        }
        if (invoke == null) {
            return -1;
        }
        i2 = ((Integer) invoke).intValue();
        return i2;
    }

    public synchronized int updatePsc(int i, byte[] bArr) {
        Class<?> cls;
        int i2;
        Method method = null;
        try {
            cls = Class.forName("com.common.sdk.iccard.ICCardServiceManager");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            cls = null;
        }
        Object systemService = this.mContext.getSystemService("ICCard");
        try {
            method = cls.getMethod("icc_update_psc", Integer.TYPE, byte[].class);
        } catch (NoSuchMethodException e2) {
            e2.printStackTrace();
        }
        try {
            try {
                i2 = ((Integer) method.invoke(systemService, Integer.valueOf(i), bArr)).intValue();
            } catch (IllegalAccessException e3) {
                e3.printStackTrace();
                i2 = -1;
                return i2;
            }
        } catch (IllegalArgumentException e4) {
            e4.printStackTrace();
            i2 = -1;
            return i2;
        } catch (InvocationTargetException e5) {
            e5.printStackTrace();
            i2 = -1;
            return i2;
        }
        return i2;
    }

    public synchronized int readmMinMemory(int i, int i2, int i3, byte[] bArr, int[] iArr) {
        Class<?> cls;
        int i4;
        Method method = null;
        try {
            cls = Class.forName("com.common.sdk.iccard.ICCardServiceManager");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            cls = null;
        }
        Object systemService = this.mContext.getSystemService("ICCard");
        try {
            method = cls.getMethod("icc_read_main_memory", Integer.TYPE, Integer.TYPE, Integer.TYPE, byte[].class, int[].class);
        } catch (NoSuchMethodException e2) {
            e2.printStackTrace();
        }
        try {
            try {
                i4 = ((Integer) method.invoke(systemService, Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3), bArr, iArr)).intValue();
            } catch (IllegalAccessException e3) {
                e3.printStackTrace();
                i4 = -1;
                return i4;
            }
        } catch (IllegalArgumentException e4) {
            e4.printStackTrace();
            i4 = -1;
            return i4;
        } catch (InvocationTargetException e5) {
            e5.printStackTrace();
            i4 = -1;
            return i4;
        }
        return i4;
    }

    public synchronized int writeMainMemory(int i, int i2, byte[] bArr, int i3) {
        Class<?> cls;
        int i4;
        Method method = null;
        try {
            cls = Class.forName("com.common.sdk.iccard.ICCardServiceManager");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            cls = null;
        }
        Object systemService = this.mContext.getSystemService("ICCard");
        try {
            method = cls.getMethod("icc_write_main_memory", Integer.TYPE, Integer.TYPE, byte[].class, Integer.TYPE);
        } catch (NoSuchMethodException e2) {
            e2.printStackTrace();
        }
        try {
            i4 = ((Integer) method.invoke(systemService, Integer.valueOf(i), Integer.valueOf(i2), bArr, Integer.valueOf(i3))).intValue();
        } catch (IllegalAccessException e3) {
            e3.printStackTrace();
            i4 = -1;
            return i4;
        } catch (IllegalArgumentException e4) {
            e4.printStackTrace();
            i4 = -1;
            return i4;
        } catch (InvocationTargetException e5) {
            e5.printStackTrace();
            i4 = -1;
            return i4;
        }
        return i4;
    }

    private static synchronized void throwException(InvocationTargetException invocationTargetException) throws TelpoException {
        synchronized (ICCardReader.class) {
            throw new TelpoException();
        }
    }
}
