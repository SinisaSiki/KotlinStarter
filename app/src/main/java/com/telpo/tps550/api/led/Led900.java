package com.telpo.tps550.api.led;

import android.content.Context;
import com.telpo.tps550.api.InternalErrorException;
import com.telpo.tps550.api.TelpoException;
import java.lang.reflect.InvocationTargetException;

/* loaded from: classes.dex */
public class Led900 {
    private Context mContext;

    public Led900(Context context) {
        this.mContext = context;
    }

    public synchronized void on(int i) throws TelpoException {
        try {
            try {
                Class<?> cls = Class.forName("com.common.sdk.led.LEDServiceManager");
                Object systemService = this.mContext.getSystemService("LED");
                try {
                    try {
                        try {
                            cls.getMethod("on", Integer.TYPE).invoke(systemService, Integer.valueOf(i));
                        } catch (InvocationTargetException unused) {
                            throw new TelpoException();
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new InternalErrorException();
                    } catch (IllegalArgumentException e2) {
                        e2.printStackTrace();
                        throw new InternalErrorException();
                    }
                } catch (NoSuchMethodException e3) {
                    e3.printStackTrace();
                    throw new InternalErrorException();
                }
            } catch (ClassNotFoundException e4) {
                e4.printStackTrace();
                throw new InternalErrorException();
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void off(int i) throws TelpoException {
        try {
            try {
                Class<?> cls = Class.forName("com.common.sdk.led.LEDServiceManager");
                Object systemService = this.mContext.getSystemService("LED");
                try {
                    try {
                        try {
                            cls.getMethod("off", Integer.TYPE).invoke(systemService, Integer.valueOf(i));
                        } catch (InvocationTargetException unused) {
                            throw new TelpoException();
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new InternalErrorException();
                    } catch (IllegalArgumentException e2) {
                        e2.printStackTrace();
                        throw new InternalErrorException();
                    }
                } catch (NoSuchMethodException e3) {
                    e3.printStackTrace();
                    throw new InternalErrorException();
                }
            } catch (ClassNotFoundException e4) {
                e4.printStackTrace();
                throw new InternalErrorException();
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized void blink(int i, int i2) throws TelpoException {
        try {
            try {
                Class<?> cls = Class.forName("com.common.sdk.led.LEDServiceManager");
                Object systemService = this.mContext.getSystemService("LED");
                try {
                    try {
                        cls.getMethod("blink", Integer.TYPE, Integer.TYPE).invoke(systemService, Integer.valueOf(i), Integer.valueOf(i2));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new InternalErrorException();
                    } catch (IllegalArgumentException e2) {
                        e2.printStackTrace();
                        throw new InternalErrorException();
                    } catch (InvocationTargetException unused) {
                        throw new TelpoException();
                    }
                } catch (NoSuchMethodException e3) {
                    e3.printStackTrace();
                    throw new InternalErrorException();
                }
            } catch (ClassNotFoundException e4) {
                e4.printStackTrace();
                throw new InternalErrorException();
            }
        } catch (Throwable th) {
            throw th;
        }
    }
}
