package com.telpo.tps550.api.printer;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import com.telpo.tps550.api.InternalErrorException;
import com.telpo.tps550.api.TelpoException;
import com.telpo.tps550.api.moneybox.MoneyBox;
import com.telpo.tps550.api.util.ShellUtils;
import com.telpo.tps550.api.util.StringUtil;
import com.telpo.tps550.api.util.SystemUtil;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import rs.teron.launcher.android.Configurator;

/* loaded from: classes.dex */
public class UsbThermalPrinter {
    public static final int ALGIN_LEFT = 0;
    public static final int ALGIN_MIDDLE = 1;
    public static final int ALGIN_RIGHT = 2;
    public static final int DIRECTION_BACK = 1;
    public static final int DIRECTION_FORWORD = 0;
    public static final int STATUS_NO_PAPER = 1;
    public static final int STATUS_OK = 0;
    public static final int STATUS_OVER_FLOW = 3;
    public static final int STATUS_OVER_HEAT = 2;
    public static final int STATUS_UNKNOWN = 4;
    public static final int WALK_DOTLINE = 0;
    public static final int WALK_LINE = 1;
    private Object EscPos;
    private Context mContext;
    private StringBuffer buffer = new StringBuffer();
    private int modelType = SystemUtil.getDeviceType();
    private String modelTypeString = SystemUtil.getInternalModel();

    public UsbThermalPrinter(Context context) {
        this.mContext = null;
        this.mContext = context;
    }

    public synchronized void start(int i) throws TelpoException {
        if (this.modelType == StringUtil.DeviceModelEnum.TPS651.ordinal()) {
            MoneyBox.printerPower(1);
        }
        if (this.modelType == StringUtil.DeviceModelEnum.TPS900.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS320.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS390P.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS390L.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS900MB.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS570L.ordinal() || "TPS575L".equals(this.modelTypeString)) {
            try {
                try {
                    try {
                        try {
                            Class.forName("com.common.sdk.thermalprinter.ThermalPrinterServiceManager").getMethod("start", new Class[0]).invoke(this.mContext.getSystemService("ThermalPrinter"), new Object[0]);
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                            throw new InternalErrorException();
                        }
                    } catch (IllegalAccessException e2) {
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
        } else {
            try {
                Class<?> cls = Class.forName("com.common.sdk.printer.UsbPrinterManager");
                Object systemService = this.mContext.getSystemService("UsbPrinter");
                try {
                    try {
                        try {
                            try {
                                cls.getMethod("start", Integer.TYPE).invoke(systemService, Integer.valueOf(i));
                            } catch (IllegalAccessException e6) {
                                e6.printStackTrace();
                                throw new InternalErrorException();
                            }
                        } catch (IllegalArgumentException e7) {
                            e7.printStackTrace();
                            throw new InternalErrorException();
                        }
                    } catch (InvocationTargetException e8) {
                        throwException(e8);
                    }
                } catch (NoSuchMethodException e9) {
                    e9.printStackTrace();
                    throw new InternalErrorException();
                }
            } catch (ClassNotFoundException e10) {
                e10.printStackTrace();
                throw new InternalErrorException();
            }
        }
    }

    public synchronized void start(String str) throws TelpoException {
        try {
            try {
                Class<?> cls = Class.forName("com.common.sdk.thermalprinter.ThermalPrinterServiceManager");
                try {
                    try {
                        cls.getMethod("start", String.class).invoke(this.mContext.getSystemService("ThermalPrinter"), str);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new InternalErrorException();
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

    public synchronized void setPrintAuthLevel(int i, String str) throws TelpoException {
        try {
            try {
                Class<?> cls = Class.forName("com.common.sdk.thermalprinter.ThermalPrinterServiceManager");
                Object systemService = this.mContext.getSystemService("ThermalPrinter");
                try {
                    try {
                        try {
                            cls.getMethod("setPrintAuthLevel", Integer.TYPE, String.class).invoke(systemService, Integer.valueOf(i), str);
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                            throw new InternalErrorException();
                        }
                    } catch (IllegalAccessException e2) {
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

    public synchronized void reset() throws TelpoException {
        if (this.modelType == StringUtil.DeviceModelEnum.TPS900.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS320.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS390P.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS390L.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS900MB.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS570L.ordinal() || "TPS575L".equals(this.modelTypeString)) {
            try {
                Class<?> cls = Class.forName("com.common.sdk.thermalprinter.ThermalPrinterServiceManager");
                try {
                    try {
                        cls.getMethod(Configurator.FUNCTION_KEY_SET_NONE, new Class[0]).invoke(this.mContext.getSystemService("ThermalPrinter"), new Object[0]);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new InternalErrorException();
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
        } else {
            try {
                Class<?> cls2 = Class.forName("com.common.sdk.printer.UsbPrinterManager");
                try {
                    try {
                        try {
                            cls2.getMethod(Configurator.FUNCTION_KEY_SET_NONE, new Class[0]).invoke(this.mContext.getSystemService("UsbPrinter"), new Object[0]);
                        } catch (IllegalAccessException e6) {
                            e6.printStackTrace();
                            throw new InternalErrorException();
                        }
                    } catch (IllegalArgumentException e7) {
                        e7.printStackTrace();
                        throw new InternalErrorException();
                    } catch (InvocationTargetException e8) {
                        throwException(e8);
                    }
                } catch (NoSuchMethodException e9) {
                    e9.printStackTrace();
                    throw new InternalErrorException();
                }
            } catch (ClassNotFoundException e10) {
                e10.printStackTrace();
                throw new InternalErrorException();
            }
        }
    }

    public synchronized void walkPaper(int i) throws TelpoException {
        if (this.modelType == StringUtil.DeviceModelEnum.TPS900.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS320.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS390P.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS390L.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS900MB.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS570L.ordinal() || "TPS575L".equals(this.modelTypeString)) {
            try {
                Class<?> cls = Class.forName("com.common.sdk.thermalprinter.ThermalPrinterServiceManager");
                Object systemService = this.mContext.getSystemService("ThermalPrinter");
                try {
                    try {
                        try {
                            cls.getMethod("walkPaper", Integer.TYPE).invoke(systemService, Integer.valueOf(i));
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
        } else {
            try {
                Class<?> cls2 = Class.forName("com.common.sdk.printer.UsbPrinterManager");
                Object systemService2 = this.mContext.getSystemService("UsbPrinter");
                try {
                    try {
                        try {
                            try {
                                cls2.getMethod("walkPaper", Integer.TYPE).invoke(systemService2, Integer.valueOf(i));
                            } catch (IllegalAccessException e6) {
                                e6.printStackTrace();
                                throw new InternalErrorException();
                            }
                        } catch (IllegalArgumentException e7) {
                            e7.printStackTrace();
                            throw new InternalErrorException();
                        }
                    } catch (InvocationTargetException e8) {
                        throwException(e8);
                    }
                } catch (NoSuchMethodException e9) {
                    e9.printStackTrace();
                    throw new InternalErrorException();
                }
            } catch (ClassNotFoundException e10) {
                e10.printStackTrace();
                throw new InternalErrorException();
            }
        }
    }

    public synchronized void stop() {
        Class<?> cls;
        Class<?> cls2;
        if (this.modelType == StringUtil.DeviceModelEnum.TPS651.ordinal()) {
            MoneyBox.printerPower(0);
        }
        Method method = null;
        if (this.modelType == StringUtil.DeviceModelEnum.TPS900.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS320.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS390P.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS390L.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS900MB.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS570L.ordinal() || "TPS575L".equals(this.modelTypeString)) {
            try {
                cls = Class.forName("com.common.sdk.thermalprinter.ThermalPrinterServiceManager");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                cls = null;
            }
            Object systemService = this.mContext.getSystemService("ThermalPrinter");
            try {
                method = cls.getMethod("stop", new Class[0]);
            } catch (NoSuchMethodException e2) {
                e2.printStackTrace();
            }
            try {
                try {
                    method.invoke(systemService, new Object[0]);
                } catch (IllegalAccessException e3) {
                    e3.printStackTrace();
                }
            } catch (IllegalArgumentException e4) {
                e4.printStackTrace();
            } catch (InvocationTargetException e5) {
                e5.printStackTrace();
            }
        } else {
            try {
                cls2 = Class.forName("com.common.sdk.printer.UsbPrinterManager");
            } catch (ClassNotFoundException e6) {
                e6.printStackTrace();
                cls2 = null;
            }
            Object systemService2 = this.mContext.getSystemService("UsbPrinter");
            try {
                method = cls2.getMethod("stop", new Class[0]);
            } catch (NoSuchMethodException e7) {
                e7.printStackTrace();
            }
            try {
                try {
                    method.invoke(systemService2, new Object[0]);
                } catch (IllegalArgumentException e8) {
                    e8.printStackTrace();
                }
            } catch (IllegalAccessException e9) {
                e9.printStackTrace();
            } catch (InvocationTargetException e10) {
                e10.printStackTrace();
            }
        }
    }

    public synchronized int checkStatus() throws TelpoException {
        int i;
        i = 0;
        if (this.modelType == StringUtil.DeviceModelEnum.TPS900.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS320.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS390P.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS390L.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS900MB.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS570L.ordinal() || "TPS575L".equals(this.modelTypeString)) {
            try {
                Class<?> cls = Class.forName("com.common.sdk.thermalprinter.ThermalPrinterServiceManager");
                try {
                    try {
                        i = ((Integer) cls.getMethod("checkStatus", new Class[0]).invoke(this.mContext.getSystemService("ThermalPrinter"), new Object[0])).intValue();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new InternalErrorException();
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
        } else {
            try {
                Class<?> cls2 = Class.forName("com.common.sdk.printer.UsbPrinterManager");
                try {
                    try {
                        i = ((Integer) cls2.getMethod("checkStatus", new Class[0]).invoke(this.mContext.getSystemService("UsbPrinter"), new Object[0])).intValue();
                    } catch (IllegalAccessException e6) {
                        e6.printStackTrace();
                        throw new InternalErrorException();
                    } catch (IllegalArgumentException e7) {
                        e7.printStackTrace();
                        throw new InternalErrorException();
                    } catch (InvocationTargetException e8) {
                        throwException(e8);
                    }
                } catch (NoSuchMethodException e9) {
                    e9.printStackTrace();
                    throw new InternalErrorException();
                }
            } catch (ClassNotFoundException e10) {
                e10.printStackTrace();
                throw new InternalErrorException();
            }
        }
        return i;
    }

    public synchronized void enlargeFontSize(int i, int i2) throws TelpoException {
        if (this.modelType == StringUtil.DeviceModelEnum.TPS900.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS320.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS390P.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS390L.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS900MB.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS570L.ordinal() || "TPS575L".equals(this.modelTypeString)) {
            try {
                Class<?> cls = Class.forName("com.common.sdk.thermalprinter.ThermalPrinterServiceManager");
                Object systemService = this.mContext.getSystemService("ThermalPrinter");
                try {
                    try {
                        try {
                            try {
                                cls.getMethod("enlargeFontSize", Integer.TYPE, Integer.TYPE).invoke(systemService, Integer.valueOf(i), Integer.valueOf(i2));
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
        } else {
            try {
                Class<?> cls2 = Class.forName("com.common.sdk.printer.UsbPrinterManager");
                Object systemService2 = this.mContext.getSystemService("UsbPrinter");
                try {
                    try {
                        try {
                            cls2.getMethod("enlargeFontSize", Integer.TYPE, Integer.TYPE).invoke(systemService2, Integer.valueOf(i), Integer.valueOf(i2));
                        } catch (InvocationTargetException e6) {
                            throwException(e6);
                        }
                    } catch (IllegalAccessException e7) {
                        e7.printStackTrace();
                        throw new InternalErrorException();
                    } catch (IllegalArgumentException e8) {
                        e8.printStackTrace();
                        throw new InternalErrorException();
                    }
                } catch (NoSuchMethodException e9) {
                    e9.printStackTrace();
                    throw new InternalErrorException();
                }
            } catch (ClassNotFoundException e10) {
                e10.printStackTrace();
                throw new InternalErrorException();
            }
        }
    }

    public synchronized void setFontSize(int i) throws TelpoException {
        if (this.modelType == StringUtil.DeviceModelEnum.TPS900.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS320.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS390P.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS390L.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS900MB.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS570L.ordinal() || "TPS575L".equals(this.modelTypeString)) {
            try {
                Class<?> cls = Class.forName("com.common.sdk.thermalprinter.ThermalPrinterServiceManager");
                Object systemService = this.mContext.getSystemService("ThermalPrinter");
                try {
                    try {
                        try {
                            cls.getMethod("setFontSize", Integer.TYPE).invoke(systemService, Integer.valueOf(i));
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
        } else {
            try {
                Class<?> cls2 = Class.forName("com.common.sdk.printer.UsbPrinterManager");
                Object systemService2 = this.mContext.getSystemService("UsbPrinter");
                try {
                    try {
                        try {
                            try {
                                cls2.getMethod("setFontSize", Integer.TYPE).invoke(systemService2, Integer.valueOf(i));
                            } catch (IllegalAccessException e6) {
                                e6.printStackTrace();
                                throw new InternalErrorException();
                            }
                        } catch (IllegalArgumentException e7) {
                            e7.printStackTrace();
                            throw new InternalErrorException();
                        }
                    } catch (InvocationTargetException e8) {
                        throwException(e8);
                    }
                } catch (NoSuchMethodException e9) {
                    e9.printStackTrace();
                    throw new InternalErrorException();
                }
            } catch (ClassNotFoundException e10) {
                e10.printStackTrace();
                throw new InternalErrorException();
            }
        }
    }

    public synchronized void setTextSize(int i) throws TelpoException {
        if (this.modelType == StringUtil.DeviceModelEnum.TPS900.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS320.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS390P.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS390L.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS900MB.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS570L.ordinal() || "TPS575L".equals(this.modelTypeString)) {
            try {
                Class<?> cls = Class.forName("com.common.sdk.thermalprinter.ThermalPrinterServiceManager");
                Object systemService = this.mContext.getSystemService("ThermalPrinter");
                try {
                    try {
                        cls.getMethod("setTextSize", Integer.TYPE).invoke(systemService, Integer.valueOf(i));
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
                    }
                } catch (NoSuchMethodException e4) {
                    e4.printStackTrace();
                    throw new InternalErrorException();
                }
            } catch (ClassNotFoundException e5) {
                e5.printStackTrace();
                throw new InternalErrorException();
            }
        } else {
            try {
                Class<?> cls2 = Class.forName("com.common.sdk.printer.UsbPrinterManager");
                Object systemService2 = this.mContext.getSystemService("UsbPrinter");
                try {
                    try {
                        cls2.getMethod("setTextSize", Integer.TYPE).invoke(systemService2, Integer.valueOf(i));
                    } catch (IllegalAccessException e6) {
                        e6.printStackTrace();
                        throw new InternalErrorException();
                    } catch (IllegalArgumentException e7) {
                        e7.printStackTrace();
                        throw new InternalErrorException();
                    } catch (InvocationTargetException e8) {
                        e8.printStackTrace();
                        Exception exc2 = (Exception) e8.getTargetException();
                        if (exc2 instanceof TelpoException) {
                            throw ((TelpoException) exc2);
                        }
                    }
                } catch (NoSuchMethodException e9) {
                    e9.printStackTrace();
                    throw new InternalErrorException();
                }
            } catch (ClassNotFoundException e10) {
                e10.printStackTrace();
                throw new InternalErrorException();
            }
        }
    }

    public synchronized void setUnderline(boolean z) throws TelpoException {
        if (this.modelType == StringUtil.DeviceModelEnum.TPS900.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS320.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS390P.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS390L.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS900MB.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS570L.ordinal() || "TPS575L".equals(this.modelTypeString)) {
            try {
                Class<?> cls = Class.forName("com.common.sdk.thermalprinter.ThermalPrinterServiceManager");
                Object systemService = this.mContext.getSystemService("ThermalPrinter");
                try {
                    try {
                        cls.getMethod("setUnderline", Boolean.TYPE).invoke(systemService, Boolean.valueOf(z));
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
                    }
                } catch (NoSuchMethodException e4) {
                    e4.printStackTrace();
                    throw new InternalErrorException();
                }
            } catch (ClassNotFoundException e5) {
                e5.printStackTrace();
                throw new InternalErrorException();
            }
        } else {
            try {
                Class<?> cls2 = Class.forName("com.common.sdk.printer.UsbPrinterManager");
                Object systemService2 = this.mContext.getSystemService("UsbPrinter");
                try {
                    try {
                        cls2.getMethod("setUnderline", Boolean.TYPE).invoke(systemService2, Boolean.valueOf(z));
                    } catch (IllegalAccessException e6) {
                        e6.printStackTrace();
                        throw new InternalErrorException();
                    } catch (IllegalArgumentException e7) {
                        e7.printStackTrace();
                        throw new InternalErrorException();
                    } catch (InvocationTargetException e8) {
                        e8.printStackTrace();
                        Exception exc2 = (Exception) e8.getTargetException();
                        if (exc2 instanceof TelpoException) {
                            throw ((TelpoException) exc2);
                        }
                    }
                } catch (NoSuchMethodException e9) {
                    e9.printStackTrace();
                    throw new InternalErrorException();
                }
            } catch (ClassNotFoundException e10) {
                e10.printStackTrace();
                throw new InternalErrorException();
            }
        }
    }

    public synchronized void setMonoSpace(boolean z) throws TelpoException {
        if (this.modelType == StringUtil.DeviceModelEnum.TPS900.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS320.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS390P.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS390L.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS900MB.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS570L.ordinal() || "TPS575L".equals(this.modelTypeString)) {
            try {
                Class<?> cls = Class.forName("com.common.sdk.thermalprinter.ThermalPrinterServiceManager");
                Object systemService = this.mContext.getSystemService("ThermalPrinter");
                try {
                    try {
                        cls.getMethod("setMonoSpace", Boolean.TYPE).invoke(systemService, Boolean.valueOf(z));
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

    public synchronized void setHighlight(boolean z) throws TelpoException {
        if (this.modelType == StringUtil.DeviceModelEnum.TPS900.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS320.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS390P.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS390L.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS900MB.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS570L.ordinal() || "TPS575L".equals(this.modelTypeString)) {
            try {
                Class<?> cls = Class.forName("com.common.sdk.thermalprinter.ThermalPrinterServiceManager");
                Object systemService = this.mContext.getSystemService("ThermalPrinter");
                try {
                    try {
                        try {
                            cls.getMethod("setHighlight", Boolean.TYPE).invoke(systemService, Boolean.valueOf(z));
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
        } else {
            try {
                Class<?> cls2 = Class.forName("com.common.sdk.printer.UsbPrinterManager");
                Object systemService2 = this.mContext.getSystemService("UsbPrinter");
                try {
                    try {
                        try {
                            try {
                                cls2.getMethod("setHighlight", Boolean.TYPE).invoke(systemService2, Boolean.valueOf(z));
                            } catch (IllegalAccessException e6) {
                                e6.printStackTrace();
                                throw new InternalErrorException();
                            }
                        } catch (IllegalArgumentException e7) {
                            e7.printStackTrace();
                            throw new InternalErrorException();
                        }
                    } catch (InvocationTargetException e8) {
                        throwException(e8);
                    }
                } catch (NoSuchMethodException e9) {
                    e9.printStackTrace();
                    throw new InternalErrorException();
                }
            } catch (ClassNotFoundException e10) {
                e10.printStackTrace();
                throw new InternalErrorException();
            }
        }
    }

    public synchronized void setGray(int i) throws TelpoException {
        Log.d("tagg", "setGray:" + i);
        if (this.modelType == StringUtil.DeviceModelEnum.TPS900.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS320.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS390P.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS390L.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS900MB.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS570L.ordinal() || "TPS575L".equals(this.modelTypeString)) {
            try {
                Class<?> cls = Class.forName("com.common.sdk.thermalprinter.ThermalPrinterServiceManager");
                Object systemService = this.mContext.getSystemService("ThermalPrinter");
                try {
                    try {
                        try {
                            try {
                                cls.getMethod("setGray", Integer.TYPE).invoke(systemService, Integer.valueOf(i));
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
        } else {
            try {
                Class<?> cls2 = Class.forName("com.common.sdk.printer.UsbPrinterManager");
                Object systemService2 = this.mContext.getSystemService("UsbPrinter");
                try {
                    try {
                        try {
                            cls2.getMethod("setGray", Integer.TYPE).invoke(systemService2, Integer.valueOf(i));
                        } catch (IllegalAccessException e6) {
                            e6.printStackTrace();
                            throw new InternalErrorException();
                        }
                    } catch (IllegalArgumentException e7) {
                        e7.printStackTrace();
                        throw new InternalErrorException();
                    } catch (InvocationTargetException e8) {
                        throwException(e8);
                    }
                } catch (NoSuchMethodException e9) {
                    e9.printStackTrace();
                    throw new InternalErrorException();
                }
            } catch (ClassNotFoundException e10) {
                e10.printStackTrace();
                throw new InternalErrorException();
            }
        }
    }

    public synchronized void setAlgin(int i) throws TelpoException {
        if (this.modelType == StringUtil.DeviceModelEnum.TPS900.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS320.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS390P.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS390L.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS900MB.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS570L.ordinal() || "TPS575L".equals(this.modelTypeString)) {
            try {
                Class<?> cls = Class.forName("com.common.sdk.thermalprinter.ThermalPrinterServiceManager");
                Object systemService = this.mContext.getSystemService("ThermalPrinter");
                try {
                    try {
                        try {
                            cls.getMethod("setAlgin", Integer.TYPE).invoke(systemService, Integer.valueOf(i));
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
        } else {
            try {
                Class<?> cls2 = Class.forName("com.common.sdk.printer.UsbPrinterManager");
                Object systemService2 = this.mContext.getSystemService("UsbPrinter");
                try {
                    try {
                        try {
                            try {
                                cls2.getMethod("setAlgin", Integer.TYPE).invoke(systemService2, Integer.valueOf(i));
                            } catch (IllegalAccessException e6) {
                                e6.printStackTrace();
                                throw new InternalErrorException();
                            }
                        } catch (IllegalArgumentException e7) {
                            e7.printStackTrace();
                            throw new InternalErrorException();
                        }
                    } catch (InvocationTargetException e8) {
                        throwException(e8);
                    }
                } catch (NoSuchMethodException e9) {
                    e9.printStackTrace();
                    throw new InternalErrorException();
                }
            } catch (ClassNotFoundException e10) {
                e10.printStackTrace();
                throw new InternalErrorException();
            }
        }
    }

    public synchronized void addSringOneLine(String str) {
        if (str != null) {
            this.buffer.append(str);
        }
    }

    public synchronized void addString(String str) throws TelpoException {
        if (!TextUtils.isEmpty(this.buffer.toString())) {
            if (TextUtils.isEmpty(str)) {
                str = this.buffer.toString();
            } else {
                str = String.valueOf(this.buffer.toString()) + ShellUtils.COMMAND_LINE_END + str;
            }
            this.buffer.setLength(0);
        }
        if (this.modelType == StringUtil.DeviceModelEnum.TPS900.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS320.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS390P.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS390L.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS900MB.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS570L.ordinal() || "TPS575L".equals(this.modelTypeString)) {
            try {
                try {
                    try {
                        try {
                            Class.forName("com.common.sdk.thermalprinter.ThermalPrinterServiceManager").getMethod("addString", String.class).invoke(this.mContext.getSystemService("ThermalPrinter"), str);
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                            throw new InternalErrorException();
                        }
                    } catch (IllegalAccessException e2) {
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
        } else {
            try {
                try {
                    try {
                        try {
                            try {
                                Class.forName("com.common.sdk.printer.UsbPrinterManager").getMethod("addString", String.class).invoke(this.mContext.getSystemService("UsbPrinter"), str);
                            } catch (IllegalAccessException e6) {
                                e6.printStackTrace();
                                throw new InternalErrorException();
                            }
                        } catch (IllegalArgumentException e7) {
                            e7.printStackTrace();
                            throw new InternalErrorException();
                        }
                    } catch (InvocationTargetException e8) {
                        throwException(e8);
                    }
                } catch (NoSuchMethodException e9) {
                    e9.printStackTrace();
                    throw new InternalErrorException();
                }
            } catch (ClassNotFoundException e10) {
                e10.printStackTrace();
                throw new InternalErrorException();
            }
        }
    }

    public synchronized void addStringOffset(int i, String str) throws TelpoException {
        if (this.modelType == StringUtil.DeviceModelEnum.TPS900.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS320.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS390P.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS390L.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS900MB.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS570L.ordinal() || "TPS575L".equals(this.modelTypeString)) {
            try {
                Class<?> cls = Class.forName("com.common.sdk.thermalprinter.ThermalPrinterServiceManager");
                Object systemService = this.mContext.getSystemService("ThermalPrinter");
                try {
                    try {
                        cls.getMethod("addStringOffset", Integer.TYPE, String.class).invoke(systemService, Integer.valueOf(i), str);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new InternalErrorException();
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
        } else {
            try {
                Class<?> cls2 = Class.forName("com.common.sdk.printer.UsbPrinterManager");
                Object systemService2 = this.mContext.getSystemService("UsbPrinter");
                try {
                    try {
                        cls2.getMethod("addStringOffset", Integer.TYPE, String.class).invoke(systemService2, Integer.valueOf(i), str);
                    } catch (IllegalAccessException e6) {
                        e6.printStackTrace();
                        throw new InternalErrorException();
                    } catch (IllegalArgumentException e7) {
                        e7.printStackTrace();
                        throw new InternalErrorException();
                    } catch (InvocationTargetException e8) {
                        throwException(e8);
                    }
                } catch (NoSuchMethodException e9) {
                    e9.printStackTrace();
                    throw new InternalErrorException();
                }
            } catch (ClassNotFoundException e10) {
                e10.printStackTrace();
                throw new InternalErrorException();
            }
        }
    }

    public synchronized void endLine() throws TelpoException {
        if (this.modelType == StringUtil.DeviceModelEnum.TPS900.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS320.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS390P.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS390L.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS900MB.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS570L.ordinal() || "TPS575L".equals(this.modelTypeString)) {
            try {
                Class<?> cls = Class.forName("com.common.sdk.thermalprinter.ThermalPrinterServiceManager");
                try {
                    try {
                        cls.getMethod("endLine", new Class[0]).invoke(this.mContext.getSystemService("ThermalPrinter"), new Object[0]);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new InternalErrorException();
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
        } else {
            try {
                Class<?> cls2 = Class.forName("com.common.sdk.printer.UsbPrinterManager");
                try {
                    try {
                        try {
                            cls2.getMethod("endLine", new Class[0]).invoke(this.mContext.getSystemService("UsbPrinter"), new Object[0]);
                        } catch (IllegalAccessException e6) {
                            e6.printStackTrace();
                            throw new InternalErrorException();
                        }
                    } catch (IllegalArgumentException e7) {
                        e7.printStackTrace();
                        throw new InternalErrorException();
                    } catch (InvocationTargetException e8) {
                        throwException(e8);
                    }
                } catch (NoSuchMethodException e9) {
                    e9.printStackTrace();
                    throw new InternalErrorException();
                }
            } catch (ClassNotFoundException e10) {
                e10.printStackTrace();
                throw new InternalErrorException();
            }
        }
    }

    public synchronized void clearString() throws TelpoException {
        if (this.modelType == StringUtil.DeviceModelEnum.TPS900.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS320.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS390P.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS390L.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS900MB.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS570L.ordinal() || "TPS575L".equals(this.modelTypeString)) {
            try {
                Class<?> cls = Class.forName("com.common.sdk.thermalprinter.ThermalPrinterServiceManager");
                try {
                    try {
                        cls.getMethod("clearString", new Class[0]).invoke(this.mContext.getSystemService("ThermalPrinter"), new Object[0]);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new InternalErrorException();
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
        } else {
            try {
                Class<?> cls2 = Class.forName("com.common.sdk.printer.UsbPrinterManager");
                try {
                    try {
                        try {
                            cls2.getMethod("clearString", new Class[0]).invoke(this.mContext.getSystemService("UsbPrinter"), new Object[0]);
                        } catch (IllegalAccessException e6) {
                            e6.printStackTrace();
                            throw new InternalErrorException();
                        }
                    } catch (IllegalArgumentException e7) {
                        e7.printStackTrace();
                        throw new InternalErrorException();
                    } catch (InvocationTargetException e8) {
                        throwException(e8);
                    }
                } catch (NoSuchMethodException e9) {
                    e9.printStackTrace();
                    throw new InternalErrorException();
                }
            } catch (ClassNotFoundException e10) {
                e10.printStackTrace();
                throw new InternalErrorException();
            }
        }
    }

    public synchronized void printString() throws TelpoException {
        if (!TextUtils.isEmpty(this.buffer.toString())) {
            addString(this.buffer.toString());
            this.buffer.setLength(0);
        }
        if (this.modelType == StringUtil.DeviceModelEnum.TPS900.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS320.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS390P.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS390L.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS900MB.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS570L.ordinal() || "TPS575L".equals(this.modelTypeString)) {
            try {
                Class<?> cls = Class.forName("com.common.sdk.thermalprinter.ThermalPrinterServiceManager");
                try {
                    try {
                        try {
                            try {
                                cls.getMethod("printString", new Class[0]).invoke(this.mContext.getSystemService("ThermalPrinter"), new Object[0]);
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
        } else {
            try {
                Class<?> cls2 = Class.forName("com.common.sdk.printer.UsbPrinterManager");
                try {
                    try {
                        cls2.getMethod("printString", new Class[0]).invoke(this.mContext.getSystemService("UsbPrinter"), new Object[0]);
                    } catch (IllegalAccessException e6) {
                        e6.printStackTrace();
                        throw new InternalErrorException();
                    } catch (IllegalArgumentException e7) {
                        e7.printStackTrace();
                        throw new InternalErrorException();
                    } catch (InvocationTargetException e8) {
                        throwException(e8);
                    }
                } catch (NoSuchMethodException e9) {
                    e9.printStackTrace();
                    throw new InternalErrorException();
                }
            } catch (ClassNotFoundException e10) {
                e10.printStackTrace();
                throw new InternalErrorException();
            }
        }
    }

    public synchronized void printStringAndWalk(int i, int i2, int i3) throws TelpoException {
        if (!TextUtils.isEmpty(this.buffer.toString())) {
            addString(this.buffer.toString());
            this.buffer.setLength(0);
        }
        if (this.modelType == StringUtil.DeviceModelEnum.TPS900.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS320.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS390P.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS390L.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS900MB.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS570L.ordinal() || "TPS575L".equals(this.modelTypeString)) {
            try {
                Class<?> cls = Class.forName("com.common.sdk.thermalprinter.ThermalPrinterServiceManager");
                Object systemService = this.mContext.getSystemService("ThermalPrinter");
                try {
                    try {
                        try {
                            try {
                                cls.getMethod("printStringAndWalk", Integer.TYPE, Integer.TYPE, Integer.TYPE).invoke(systemService, Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3));
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
        } else {
            try {
                Class<?> cls2 = Class.forName("com.common.sdk.printer.UsbPrinterManager");
                Object systemService2 = this.mContext.getSystemService("UsbPrinter");
                try {
                    try {
                        cls2.getMethod("printStringAndWalk", Integer.TYPE, Integer.TYPE, Integer.TYPE).invoke(systemService2, Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3));
                    } catch (IllegalAccessException e6) {
                        e6.printStackTrace();
                        throw new InternalErrorException();
                    } catch (IllegalArgumentException e7) {
                        e7.printStackTrace();
                        throw new InternalErrorException();
                    } catch (InvocationTargetException e8) {
                        throwException(e8);
                    }
                } catch (NoSuchMethodException e9) {
                    e9.printStackTrace();
                    throw new InternalErrorException();
                }
            } catch (ClassNotFoundException e10) {
                e10.printStackTrace();
                throw new InternalErrorException();
            }
        }
    }

    public synchronized void setLineSpace(int i) throws TelpoException {
        if (this.modelType == StringUtil.DeviceModelEnum.TPS900.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS320.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS390P.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS390L.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS900MB.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS570L.ordinal() || "TPS575L".equals(this.modelTypeString)) {
            try {
                Class<?> cls = Class.forName("com.common.sdk.thermalprinter.ThermalPrinterServiceManager");
                Object systemService = this.mContext.getSystemService("ThermalPrinter");
                try {
                    try {
                        try {
                            cls.getMethod("setLineSpace", Integer.TYPE).invoke(systemService, Integer.valueOf(i));
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
        } else {
            try {
                Class<?> cls2 = Class.forName("com.common.sdk.printer.UsbPrinterManager");
                Object systemService2 = this.mContext.getSystemService("UsbPrinter");
                try {
                    try {
                        try {
                            try {
                                cls2.getMethod("setLineSpace", Integer.TYPE).invoke(systemService2, Integer.valueOf(i));
                            } catch (IllegalAccessException e6) {
                                e6.printStackTrace();
                                throw new InternalErrorException();
                            }
                        } catch (IllegalArgumentException e7) {
                            e7.printStackTrace();
                            throw new InternalErrorException();
                        }
                    } catch (InvocationTargetException e8) {
                        throwException(e8);
                    }
                } catch (NoSuchMethodException e9) {
                    e9.printStackTrace();
                    throw new InternalErrorException();
                }
            } catch (ClassNotFoundException e10) {
                e10.printStackTrace();
                throw new InternalErrorException();
            }
        }
    }

    public synchronized void setLeftIndent(int i) throws TelpoException {
        if (this.modelType == StringUtil.DeviceModelEnum.TPS900.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS320.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS390P.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS390L.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS900MB.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS570L.ordinal() || "TPS575L".equals(this.modelTypeString)) {
            try {
                Class<?> cls = Class.forName("com.common.sdk.thermalprinter.ThermalPrinterServiceManager");
                Object systemService = this.mContext.getSystemService("ThermalPrinter");
                try {
                    try {
                        try {
                            cls.getMethod("setLeftIndent", Integer.TYPE).invoke(systemService, Integer.valueOf(i));
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
        } else {
            try {
                Class<?> cls2 = Class.forName("com.common.sdk.printer.UsbPrinterManager");
                Object systemService2 = this.mContext.getSystemService("UsbPrinter");
                try {
                    try {
                        try {
                            try {
                                cls2.getMethod("setLeftIndent", Integer.TYPE).invoke(systemService2, Integer.valueOf(i));
                            } catch (IllegalAccessException e6) {
                                e6.printStackTrace();
                                throw new InternalErrorException();
                            }
                        } catch (IllegalArgumentException e7) {
                            e7.printStackTrace();
                            throw new InternalErrorException();
                        }
                    } catch (InvocationTargetException e8) {
                        throwException(e8);
                    }
                } catch (NoSuchMethodException e9) {
                    e9.printStackTrace();
                    throw new InternalErrorException();
                }
            } catch (ClassNotFoundException e10) {
                e10.printStackTrace();
                throw new InternalErrorException();
            }
        }
    }

    public synchronized void printLogo(Bitmap bitmap, boolean z) throws TelpoException {
        if (this.modelType == StringUtil.DeviceModelEnum.TPS900.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS320.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS390P.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS390L.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS900MB.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS570L.ordinal() || "TPS575L".equals(this.modelTypeString)) {
            try {
                Class<?> cls = Class.forName("com.common.sdk.thermalprinter.ThermalPrinterServiceManager");
                Object systemService = this.mContext.getSystemService("ThermalPrinter");
                try {
                    try {
                        cls.getMethod("printLogo", Bitmap.class, Boolean.TYPE).invoke(systemService, bitmap, Boolean.valueOf(z));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new InternalErrorException();
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
        } else {
            try {
                Class<?> cls2 = Class.forName("com.common.sdk.printer.UsbPrinterManager");
                Object systemService2 = this.mContext.getSystemService("UsbPrinter");
                try {
                    try {
                        cls2.getMethod("printLogo", Bitmap.class, Boolean.TYPE).invoke(systemService2, bitmap, Boolean.valueOf(z));
                    } catch (IllegalAccessException e6) {
                        e6.printStackTrace();
                        throw new InternalErrorException();
                    } catch (IllegalArgumentException e7) {
                        e7.printStackTrace();
                        throw new InternalErrorException();
                    } catch (InvocationTargetException e8) {
                        throwException(e8);
                    }
                } catch (NoSuchMethodException e9) {
                    e9.printStackTrace();
                    throw new InternalErrorException();
                }
            } catch (ClassNotFoundException e10) {
                e10.printStackTrace();
                throw new InternalErrorException();
            }
        }
    }

    public synchronized String getVersion() throws TelpoException {
        String str;
        String str2;
        str = null;
        if (this.modelType == StringUtil.DeviceModelEnum.TPS900.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS320.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS390P.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS390L.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS900MB.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS570L.ordinal() || "TPS575L".equals(this.modelTypeString)) {
            try {
                Class<?> cls = Class.forName("com.common.sdk.thermalprinter.ThermalPrinterServiceManager");
                try {
                    try {
                        try {
                            str2 = (String) cls.getMethod("getVersion", new Class[0]).invoke(this.mContext.getSystemService("ThermalPrinter"), new Object[0]);
                            str = str2;
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                            throw new InternalErrorException();
                        }
                    } catch (IllegalAccessException e2) {
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
        } else {
            try {
                Class<?> cls2 = Class.forName("com.common.sdk.printer.UsbPrinterManager");
                try {
                    try {
                        try {
                            try {
                                str2 = (String) cls2.getMethod("getVersion", new Class[0]).invoke(this.mContext.getSystemService("UsbPrinter"), new Object[0]);
                                str = str2;
                            } catch (IllegalAccessException e6) {
                                e6.printStackTrace();
                                throw new InternalErrorException();
                            }
                        } catch (IllegalArgumentException e7) {
                            e7.printStackTrace();
                            throw new InternalErrorException();
                        }
                    } catch (InvocationTargetException e8) {
                        throwException(e8);
                    }
                } catch (NoSuchMethodException e9) {
                    e9.printStackTrace();
                    throw new InternalErrorException();
                }
            } catch (ClassNotFoundException e10) {
                e10.printStackTrace();
                throw new InternalErrorException();
            }
        }
        return str;
    }

    public synchronized void searchMark(int i, int i2) throws TelpoException {
        if (this.modelType == StringUtil.DeviceModelEnum.TPS900.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS320.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS390P.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS390L.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS900MB.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS570L.ordinal() || "TPS575L".equals(this.modelTypeString)) {
            try {
                Class<?> cls = Class.forName("com.common.sdk.thermalprinter.ThermalPrinterServiceManager");
                Object systemService = this.mContext.getSystemService("ThermalPrinter");
                try {
                    try {
                        cls.getMethod("searchMark", Integer.TYPE, Integer.TYPE, Integer.TYPE).invoke(systemService, 0, Integer.valueOf(i), Integer.valueOf(i2));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new InternalErrorException();
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
        } else {
            try {
                Class<?> cls2 = Class.forName("com.common.sdk.printer.UsbPrinterManager");
                Object systemService2 = this.mContext.getSystemService("UsbPrinter");
                try {
                    try {
                        cls2.getMethod("searchMark", Integer.TYPE, Integer.TYPE, Integer.TYPE).invoke(systemService2, 0, Integer.valueOf(i), Integer.valueOf(i2));
                    } catch (IllegalAccessException e6) {
                        e6.printStackTrace();
                        throw new InternalErrorException();
                    } catch (IllegalArgumentException e7) {
                        e7.printStackTrace();
                        throw new InternalErrorException();
                    } catch (InvocationTargetException e8) {
                        throwException(e8);
                    }
                } catch (NoSuchMethodException e9) {
                    e9.printStackTrace();
                    throw new InternalErrorException();
                }
            } catch (ClassNotFoundException e10) {
                e10.printStackTrace();
                throw new InternalErrorException();
            }
        }
    }

    public synchronized void paperCut() throws TelpoException {
        if (this.modelType == StringUtil.DeviceModelEnum.TPS900.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS320.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS390P.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS390L.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS900MB.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS570L.ordinal() || "TPS575L".equals(this.modelTypeString)) {
            try {
                Class<?> cls = Class.forName("com.common.sdk.thermalprinter.ThermalPrinterServiceManager");
                try {
                    try {
                        cls.getMethod("paperCut", new Class[0]).invoke(this.mContext.getSystemService("ThermalPrinter"), new Object[0]);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new InternalErrorException();
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
        } else {
            try {
                Class<?> cls2 = Class.forName("com.common.sdk.printer.UsbPrinterManager");
                try {
                    try {
                        try {
                            cls2.getMethod("paperCut", new Class[0]).invoke(this.mContext.getSystemService("UsbPrinter"), new Object[0]);
                        } catch (IllegalAccessException e6) {
                            e6.printStackTrace();
                            throw new InternalErrorException();
                        }
                    } catch (IllegalArgumentException e7) {
                        e7.printStackTrace();
                        throw new InternalErrorException();
                    } catch (InvocationTargetException e8) {
                        throwException(e8);
                    }
                } catch (NoSuchMethodException e9) {
                    e9.printStackTrace();
                    throw new InternalErrorException();
                }
            } catch (ClassNotFoundException e10) {
                e10.printStackTrace();
                throw new InternalErrorException();
            }
        }
    }

    public synchronized void setBold(boolean z) throws TelpoException {
        if (this.modelType == StringUtil.DeviceModelEnum.TPS900.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS320.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS390P.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS390L.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS900MB.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS570L.ordinal() || "TPS575L".equals(this.modelTypeString)) {
            try {
                Class<?> cls = Class.forName("com.common.sdk.thermalprinter.ThermalPrinterServiceManager");
                Object systemService = this.mContext.getSystemService("ThermalPrinter");
                try {
                    try {
                        try {
                            cls.getMethod("setBold", Boolean.TYPE).invoke(systemService, Boolean.valueOf(z));
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
        } else {
            try {
                Class<?> cls2 = Class.forName("com.common.sdk.printer.UsbPrinterManager");
                Object systemService2 = this.mContext.getSystemService("UsbPrinter");
                try {
                    try {
                        try {
                            try {
                                cls2.getMethod("setBold", Boolean.TYPE).invoke(systemService2, Boolean.valueOf(z));
                            } catch (IllegalAccessException e6) {
                                e6.printStackTrace();
                                throw new InternalErrorException();
                            }
                        } catch (IllegalArgumentException e7) {
                            e7.printStackTrace();
                            throw new InternalErrorException();
                        }
                    } catch (InvocationTargetException e8) {
                        throwException(e8);
                    }
                } catch (NoSuchMethodException e9) {
                    e9.printStackTrace();
                    throw new InternalErrorException();
                }
            } catch (ClassNotFoundException e10) {
                e10.printStackTrace();
                throw new InternalErrorException();
            }
        }
    }

    public int measureText(String str) throws TelpoException {
        if (this.modelType == StringUtil.DeviceModelEnum.TPS900.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS320.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS390P.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS390L.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS900MB.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS570L.ordinal() || "TPS575L".equals(this.modelTypeString)) {
            try {
                Class<?> cls = Class.forName("com.common.sdk.thermalprinter.ThermalPrinterServiceManager");
                try {
                    try {
                        return ((Integer) cls.getMethod("measureText", String.class).invoke(this.mContext.getSystemService("ThermalPrinter"), str)).intValue();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new InternalErrorException();
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
        } else {
            try {
                Class<?> cls2 = Class.forName("com.common.sdk.printer.UsbPrinterManager");
                try {
                    try {
                        return ((Integer) cls2.getMethod("measureText", String.class).invoke(this.mContext.getSystemService("UsbPrinter"), str)).intValue();
                    } catch (IllegalAccessException e6) {
                        e6.printStackTrace();
                        throw new InternalErrorException();
                    } catch (IllegalArgumentException e7) {
                        e7.printStackTrace();
                        throw new InternalErrorException();
                    } catch (InvocationTargetException e8) {
                        throwException(e8);
                    }
                } catch (NoSuchMethodException e9) {
                    e9.printStackTrace();
                    throw new InternalErrorException();
                }
            } catch (ClassNotFoundException e10) {
                e10.printStackTrace();
                throw new InternalErrorException();
            }
        }
        return -1;
    }

    public synchronized void autoBreakSet(boolean z) throws TelpoException {
        if (this.modelType == StringUtil.DeviceModelEnum.TPS900.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS320.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS390P.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS390L.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS900MB.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS570L.ordinal() || "TPS575L".equals(this.modelTypeString)) {
            try {
                Class<?> cls = Class.forName("com.common.sdk.thermalprinter.ThermalPrinterServiceManager");
                Object systemService = this.mContext.getSystemService("ThermalPrinter");
                try {
                    try {
                        try {
                            cls.getMethod("setAutoBreak", Boolean.TYPE).invoke(systemService, Boolean.valueOf(z));
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
        } else {
            try {
                Class<?> cls2 = Class.forName("com.common.sdk.printer.UsbPrinterManager");
                Object systemService2 = this.mContext.getSystemService("UsbPrinter");
                try {
                    try {
                        try {
                            try {
                                cls2.getMethod("setAutoBreak", Boolean.TYPE).invoke(systemService2, Boolean.valueOf(z));
                            } catch (IllegalAccessException e6) {
                                e6.printStackTrace();
                                throw new InternalErrorException();
                            }
                        } catch (IllegalArgumentException e7) {
                            e7.printStackTrace();
                            throw new InternalErrorException();
                        }
                    } catch (InvocationTargetException e8) {
                        throwException(e8);
                    }
                } catch (NoSuchMethodException e9) {
                    e9.printStackTrace();
                    throw new InternalErrorException();
                }
            } catch (ClassNotFoundException e10) {
                e10.printStackTrace();
                throw new InternalErrorException();
            }
        }
    }

    public synchronized void setThripleHeight(boolean z) throws TelpoException {
        if (this.modelType == StringUtil.DeviceModelEnum.TPS900.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS320.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS900MB.ordinal() || "TPS575L".equals(this.modelTypeString)) {
            try {
                Class<?> cls = Class.forName("com.common.sdk.thermalprinter.ThermalPrinterServiceManager");
                Object systemService = this.mContext.getSystemService("ThermalPrinter");
                try {
                    try {
                        try {
                            cls.getMethod("setTripleHeight", Boolean.TYPE).invoke(systemService, Boolean.valueOf(z));
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
        } else {
            try {
                Class<?> cls2 = Class.forName("com.common.sdk.printer.UsbPrinterManager");
                Object systemService2 = this.mContext.getSystemService("UsbPrinter");
                try {
                    try {
                        try {
                            try {
                                cls2.getMethod("setTripleHeight", Boolean.TYPE).invoke(systemService2, Boolean.valueOf(z));
                            } catch (IllegalArgumentException e6) {
                                e6.printStackTrace();
                                throw new InternalErrorException();
                            }
                        } catch (InvocationTargetException e7) {
                            throwException(e7);
                        }
                    } catch (IllegalAccessException e8) {
                        e8.printStackTrace();
                        throw new InternalErrorException();
                    }
                } catch (NoSuchMethodException e9) {
                    e9.printStackTrace();
                    throw new InternalErrorException();
                }
            } catch (ClassNotFoundException e10) {
                e10.printStackTrace();
                throw new InternalErrorException();
            }
        }
    }

    public synchronized void printLogoRaw(byte[] bArr, int i, int i2) throws TelpoException {
        printLogoRaw(bArr, i, i2, false);
    }

    public synchronized void printLogoRaw(byte[] bArr, int i, int i2, boolean z) throws TelpoException {
        if (this.modelType == StringUtil.DeviceModelEnum.TPS900.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS320.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS390P.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS390L.ordinal() || this.modelType == StringUtil.DeviceModelEnum.TPS570L.ordinal() || "TPS575L".equals(this.modelTypeString)) {
            try {
                Class<?> cls = Class.forName("com.common.sdk.thermalprinter.ThermalPrinterServiceManager");
                Object systemService = this.mContext.getSystemService("ThermalPrinter");
                try {
                    try {
                        try {
                            cls.getMethod("printLogoRaw", byte[].class, Integer.TYPE, Integer.TYPE, Boolean.TYPE).invoke(systemService, bArr, Integer.valueOf(i), Integer.valueOf(i2), Boolean.valueOf(z));
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
        } else {
            try {
                Class<?> cls2 = Class.forName("com.common.sdk.printer.UsbPrinterManager");
                Object systemService2 = this.mContext.getSystemService("UsbPrinter");
                try {
                    try {
                        try {
                            try {
                                cls2.getMethod("printLogoRaw", byte[].class, Integer.TYPE, Integer.TYPE).invoke(systemService2, bArr, Integer.valueOf(i), Integer.valueOf(i2));
                            } catch (IllegalAccessException e6) {
                                e6.printStackTrace();
                                throw new InternalErrorException();
                            }
                        } catch (IllegalArgumentException e7) {
                            e7.printStackTrace();
                            throw new InternalErrorException();
                        }
                    } catch (InvocationTargetException e8) {
                        throwException(e8);
                    }
                } catch (NoSuchMethodException e9) {
                    e9.printStackTrace();
                    throw new InternalErrorException();
                }
            } catch (ClassNotFoundException e10) {
                e10.printStackTrace();
                throw new InternalErrorException();
            }
        }
    }

    public synchronized void EscPosCommandExe(byte[] bArr) {
        Class<?> cls;
        Constructor<?> constructor;
        Method method = null;
        try {
            cls = Class.forName("com.common.sdk.thermalprinter.escpos.EscPos");
            try {
                constructor = cls.getConstructor(Context.class);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                constructor = null;
            }
            if (this.EscPos == null) {
                try {
                    try {
                        try {
                            try {
                                this.EscPos = constructor.newInstance(this.mContext);
                            } catch (InvocationTargetException e2) {
                                e2.printStackTrace();
                            }
                        } catch (InstantiationException e3) {
                            e3.printStackTrace();
                        }
                    } catch (IllegalArgumentException e4) {
                        e4.printStackTrace();
                    }
                } catch (IllegalAccessException e5) {
                    e5.printStackTrace();
                }
            }
            try {
                method = cls.getMethod("EscPosCommandExe", byte[].class);
                try {
                    try {
                        try {
                            method.invoke(this.EscPos, bArr);
                        } catch (IllegalArgumentException e6) {
                            e6.printStackTrace();
                            try {
                                throw new InternalErrorException();
                            } catch (InternalErrorException e7) {
                                e7.printStackTrace();
                            }
                        }
                    } catch (InvocationTargetException e8) {
                        e8.printStackTrace();
                        Exception exc = (Exception) e8.getTargetException();
                        if (exc instanceof TelpoException) {
                            try {
                                throw ((TelpoException) exc);
                            } catch (TelpoException e9) {
                                e9.printStackTrace();
                            }
                        }
                    }
                } catch (IllegalAccessException e10) {
                    e10.printStackTrace();
                    try {
                        throw new InternalErrorException();
                    } catch (InternalErrorException e11) {
                        e11.printStackTrace();
                    }
                }
            } catch (NoSuchMethodException e12) {
                e12.printStackTrace();
                try {
                    throw new InternalErrorException();
                } catch (InternalErrorException e13) {
                    e13.printStackTrace();
                }
            }
        } catch (ClassNotFoundException e14) {
            e14.printStackTrace();
            try {
                throw new InternalErrorException();
            } catch (InternalErrorException e15) {
                e15.printStackTrace();
                cls = null;
            }
        }
    }

    public synchronized void setAutoAdjGray(boolean z) throws TelpoException {
        try {
            try {
                Class<?> cls = Class.forName("com.common.sdk.thermalprinter.ThermalPrinterServiceManager");
                Object systemService = this.mContext.getSystemService("ThermalPrinter");
                try {
                    try {
                        try {
                            cls.getMethod("setAutoAdjGray", Boolean.TYPE).invoke(systemService, Boolean.valueOf(z));
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                            throw new InternalErrorException();
                        }
                    } catch (IllegalAccessException e2) {
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

    public synchronized boolean isAutoAdjGray() throws TelpoException {
        boolean z;
        try {
            try {
                Class<?> cls = Class.forName("com.common.sdk.thermalprinter.ThermalPrinterServiceManager");
                try {
                    z = false;
                    try {
                        z = ((Boolean) cls.getMethod("isAutoAdjGray", new Class[0]).invoke(this.mContext.getSystemService("ThermalPrinter"), new Object[0])).booleanValue();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        throw new InternalErrorException();
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
        return z;
    }

    public void setItalic(boolean z) throws TelpoException {
        try {
            Class<?> cls = Class.forName("com.common.sdk.printer.UsbPrinterManager");
            Object systemService = this.mContext.getSystemService("UsbPrinter");
            try {
                try {
                    cls.getMethod("setItalic", Boolean.TYPE).invoke(systemService, Boolean.valueOf(z));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    throw new InternalErrorException();
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

    private void throwException(InvocationTargetException invocationTargetException) throws TelpoException {
        Log.d("tagg", "exception:" + invocationTargetException.getTargetException().toString());
        if (invocationTargetException.getTargetException().toString().contains("NoPaper")) {
            throw new NoPaperException();
        }
        if (invocationTargetException.getTargetException().toString().contains("OverHeat")) {
            throw new OverHeatException();
        }
        if (invocationTargetException.getTargetException().toString().contains("BlackBlock")) {
            throw new BlackBlockNotFoundException();
        }
        if (invocationTargetException.getTargetException().toString().contains("LowPower")) {
            throw new LowPowerException();
        }
        throw new TelpoException();
    }
}
