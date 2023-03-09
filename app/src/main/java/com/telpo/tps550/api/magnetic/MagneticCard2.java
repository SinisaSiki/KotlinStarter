package com.telpo.tps550.api.magnetic;

import android.content.Context;
import com.telpo.tps550.api.InternalErrorException;
import com.telpo.tps550.api.TelpoException;
import java.lang.reflect.InvocationTargetException;

/* loaded from: classes.dex */
public class MagneticCard2 {
    private static Context mContext;

    public static synchronized void open(Context context) throws TelpoException {
        synchronized (MagneticCard2.class) {
            mContext = context;
            try {
                Class<?> cls = Class.forName("com.common.sdk.magneticcard.MagneticCardServiceManager");
                try {
                    try {
                        try {
                            cls.getMethod("open", new Class[0]).invoke(mContext.getSystemService("MagneticCard"), new Object[0]);
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
    }

    public static synchronized void close() throws TelpoException {
        synchronized (MagneticCard2.class) {
            try {
                try {
                    Class<?> cls = Class.forName("com.common.sdk.magneticcard.MagneticCardServiceManager");
                    try {
                        try {
                            try {
                                cls.getMethod("close", new Class[0]).invoke(mContext.getSystemService("MagneticCard"), new Object[0]);
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
                        mContext = null;
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
    }

    public static synchronized int check(int i) throws TelpoException {
        int i2;
        synchronized (MagneticCard2.class) {
            try {
                try {
                    Class<?> cls = Class.forName("com.common.sdk.magneticcard.MagneticCardServiceManager");
                    Object systemService = mContext.getSystemService("MagneticCard");
                    try {
                        i2 = 0;
                        try {
                            try {
                                i2 = ((Integer) cls.getMethod("check", Integer.TYPE).invoke(systemService, Integer.valueOf(i))).intValue();
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
            } catch (Throwable th) {
                throw th;
            }
        }
        return i2;
    }

    public static synchronized String[] read() throws TelpoException {
        String[] strArr;
        synchronized (MagneticCard2.class) {
            strArr = new String[3];
            try {
                Class<?> cls = Class.forName("com.common.sdk.magneticcard.MagneticCardServiceManager");
                try {
                    try {
                        try {
                            try {
                                strArr = (String[]) cls.getMethod("read", new Class[0]).invoke(mContext.getSystemService("MagneticCard"), new Object[0]);
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
        }
        return strArr;
    }

    private static synchronized void throwException(InvocationTargetException invocationTargetException) throws TelpoException {
        synchronized (MagneticCard2.class) {
            throw new TelpoException();
        }
    }
}
