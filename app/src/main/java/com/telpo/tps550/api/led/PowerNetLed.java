package com.telpo.tps550.api.led;

import android.content.Context;
import com.telpo.tps550.api.InternalErrorException;
import com.telpo.tps550.api.TelpoException;
import java.lang.reflect.InvocationTargetException;

/* loaded from: classes.dex */
public class PowerNetLed {
    private Context mContext;

    public PowerNetLed(Context context) {
        this.mContext = context;
    }

    public synchronized int powerRedLed(int i) throws TelpoException {
        Class<?> cls;
        Object systemService;
        try {
            try {
                cls = Class.forName("com.common.sdk.led.LEDServiceManager");
                systemService = this.mContext.getSystemService("LED");
                try {
                    try {
                        try {
                            try {
                            } catch (InvocationTargetException unused) {
                                throw new TelpoException();
                            }
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                            throw new InternalErrorException();
                        }
                    } catch (IllegalAccessException e2) {
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
        return ((Integer) cls.getMethod("powerRedLed", Integer.TYPE).invoke(systemService, Integer.valueOf(i))).intValue();
    }

    public synchronized int powerGreenLed(int i) throws TelpoException {
        Class<?> cls;
        Object systemService;
        try {
            try {
                cls = Class.forName("com.common.sdk.led.LEDServiceManager");
                systemService = this.mContext.getSystemService("LED");
                try {
                    try {
                        try {
                            try {
                            } catch (InvocationTargetException unused) {
                                throw new TelpoException();
                            }
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                            throw new InternalErrorException();
                        }
                    } catch (IllegalAccessException e2) {
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
        return ((Integer) cls.getMethod("powerGreenLed", Integer.TYPE).invoke(systemService, Integer.valueOf(i))).intValue();
    }

    public synchronized int powerYellowLed(int i) throws TelpoException {
        Class<?> cls;
        Object systemService;
        try {
            try {
                cls = Class.forName("com.common.sdk.led.LEDServiceManager");
                systemService = this.mContext.getSystemService("LED");
                try {
                    try {
                        try {
                            try {
                            } catch (InvocationTargetException unused) {
                                throw new TelpoException();
                            }
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                            throw new InternalErrorException();
                        }
                    } catch (IllegalAccessException e2) {
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
        return ((Integer) cls.getMethod("powerYellowLed", Integer.TYPE).invoke(systemService, Integer.valueOf(i))).intValue();
    }

    public synchronized int networkRedLed(int i) throws TelpoException {
        Class<?> cls;
        Object systemService;
        try {
            try {
                cls = Class.forName("com.common.sdk.led.LEDServiceManager");
                systemService = this.mContext.getSystemService("LED");
                try {
                    try {
                        try {
                            try {
                            } catch (InvocationTargetException unused) {
                                throw new TelpoException();
                            }
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                            throw new InternalErrorException();
                        }
                    } catch (IllegalAccessException e2) {
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
        return ((Integer) cls.getMethod("networkRedLed", Integer.TYPE).invoke(systemService, Integer.valueOf(i))).intValue();
    }

    public synchronized int networkGreenLed(int i) throws TelpoException {
        Class<?> cls;
        Object systemService;
        try {
            try {
                cls = Class.forName("com.common.sdk.led.LEDServiceManager");
                systemService = this.mContext.getSystemService("LED");
                try {
                    try {
                        try {
                            try {
                            } catch (InvocationTargetException unused) {
                                throw new TelpoException();
                            }
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                            throw new InternalErrorException();
                        }
                    } catch (IllegalAccessException e2) {
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
        return ((Integer) cls.getMethod("networkGreenLed", Integer.TYPE).invoke(systemService, Integer.valueOf(i))).intValue();
    }

    public synchronized int networkYellowLed(int i) throws TelpoException {
        Class<?> cls;
        Object systemService;
        try {
            try {
                cls = Class.forName("com.common.sdk.led.LEDServiceManager");
                systemService = this.mContext.getSystemService("LED");
                try {
                    try {
                        try {
                            try {
                            } catch (InvocationTargetException unused) {
                                throw new TelpoException();
                            }
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                            throw new InternalErrorException();
                        }
                    } catch (IllegalAccessException e2) {
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
        return ((Integer) cls.getMethod("networkYellowLed", Integer.TYPE).invoke(systemService, Integer.valueOf(i))).intValue();
    }
}
