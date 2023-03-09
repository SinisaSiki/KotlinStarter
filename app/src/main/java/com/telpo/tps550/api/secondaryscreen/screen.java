package com.telpo.tps550.api.secondaryscreen;

import android.content.Context;
import android.util.Log;
import com.telpo.tps550.api.InternalErrorException;
import com.telpo.tps550.api.InvalidDeviceStateException;
import com.telpo.tps550.api.TelpoException;
import com.telpo.tps550.api.TimeoutException;
import java.lang.reflect.InvocationTargetException;

/* loaded from: classes.dex */
public class screen {
    private Context mContext;

    public screen(Context context) {
        this.mContext = context;
    }

    public synchronized int openScreen() throws TelpoException {
        Class<?> cls;
        try {
            try {
                cls = Class.forName("com.common.sdk.secondaryscreen.SecondaryScreenServiceManager");
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
        } catch (Throwable th) {
            throw th;
        }
        return ((Integer) cls.getMethod("lcd_open", new Class[0]).invoke(this.mContext.getSystemService("Secondaryscreen"), new Object[0])).intValue();
    }

    public synchronized int closeScreen() throws TelpoException {
        Class<?> cls;
        try {
            try {
                cls = Class.forName("com.common.sdk.secondaryscreen.SecondaryScreenServiceManager");
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
        } catch (Throwable th) {
            throw th;
        }
        return ((Integer) cls.getMethod("lcd_close", new Class[0]).invoke(this.mContext.getSystemService("Secondaryscreen"), new Object[0])).intValue();
    }

    public synchronized int checkLcdState() throws TelpoException {
        Class<?> cls;
        try {
            try {
                cls = Class.forName("com.common.sdk.secondaryscreen.SecondaryScreenServiceManager");
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
        } catch (Throwable th) {
            throw th;
        }
        return ((Integer) cls.getMethod("lcd_detect", new Class[0]).invoke(this.mContext.getSystemService("Secondaryscreen"), new Object[0])).intValue();
    }

    public synchronized int clearScreen() throws TelpoException {
        Class<?> cls;
        try {
            try {
                cls = Class.forName("com.common.sdk.secondaryscreen.SecondaryScreenServiceManager");
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
        } catch (Throwable th) {
            throw th;
        }
        return ((Integer) cls.getMethod("lcd_clear_screen", new Class[0]).invoke(this.mContext.getSystemService("Secondaryscreen"), new Object[0])).intValue();
    }

    public synchronized int lcdPrintf(String str, int i, int i2) throws TelpoException {
        Class<?> cls;
        Object systemService;
        Log.i("ming", "the text = " + str);
        Log.i("ming", "the lineNum = " + i);
        Log.i("ming", "the runTime = " + i2);
        try {
            cls = Class.forName("com.common.sdk.secondaryscreen.SecondaryScreenServiceManager");
            systemService = this.mContext.getSystemService("Secondaryscreen");
            try {
                try {
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
        return ((Integer) cls.getMethod("lcd_printf", String.class, Integer.TYPE, Integer.TYPE).invoke(systemService, str, Integer.valueOf(i), Integer.valueOf(i2))).intValue();
    }
}
