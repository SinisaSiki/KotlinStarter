package com.telpo.tps550.api.printer;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import com.telpo.tps550.api.InternalErrorException;
import com.telpo.tps550.api.TelpoException;
import com.telpo.tps550.api.util.ShellUtils;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import rs.teron.launcher.android.Configurator;

/* loaded from: classes.dex */
public class Aaa {
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
    private StringBuffer buffer = new StringBuffer();
    private Context mContext;

    public Aaa(Context context) {
        this.mContext = null;
        this.mContext = context;
    }

    public synchronized void start(int i) throws TelpoException {
        try {
            try {
                Class<?> cls = Class.forName("com.common.sdk.printer.UsbPrinterManager");
                Object systemService = this.mContext.getSystemService("UsbPrinter");
                try {
                    try {
                        try {
                            cls.getMethod("start", Integer.TYPE).invoke(systemService, Integer.valueOf(i));
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
        try {
            try {
                Class<?> cls = Class.forName("com.common.sdk.printer.UsbPrinterManager");
                try {
                    try {
                        try {
                            cls.getMethod(Configurator.FUNCTION_KEY_SET_NONE, new Class[0]).invoke(this.mContext.getSystemService("UsbPrinter"), new Object[0]);
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

    public synchronized void walkPaper(int i) throws TelpoException {
        try {
            try {
                Class<?> cls = Class.forName("com.common.sdk.printer.UsbPrinterManager");
                Object systemService = this.mContext.getSystemService("UsbPrinter");
                try {
                    try {
                        try {
                            cls.getMethod("walkPaper", Integer.TYPE).invoke(systemService, Integer.valueOf(i));
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

    public synchronized void stop() {
        Class<?> cls;
        Method method = null;
        try {
            cls = Class.forName("com.common.sdk.printer.UsbPrinterManager");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            cls = null;
        }
        Object systemService = this.mContext.getSystemService("UsbPrinter");
        try {
            method = cls.getMethod("stop", new Class[0]);
        } catch (NoSuchMethodException e2) {
            e2.printStackTrace();
        }
        try {
            try {
                method.invoke(systemService, new Object[0]);
            } catch (IllegalArgumentException e3) {
                e3.printStackTrace();
            }
        } catch (IllegalAccessException e4) {
            e4.printStackTrace();
        } catch (InvocationTargetException e5) {
            e5.printStackTrace();
        }
    }

    public synchronized int checkStatus() throws TelpoException {
        int i;
        try {
            try {
                Class<?> cls = Class.forName("com.common.sdk.printer.UsbPrinterManager");
                try {
                    i = 0;
                    try {
                        i = ((Integer) cls.getMethod("checkStatus", new Class[0]).invoke(this.mContext.getSystemService("UsbPrinter"), new Object[0])).intValue();
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
        return i;
    }

    public synchronized void enlargeFontSize(int i, int i2) throws TelpoException {
        try {
            try {
                Class<?> cls = Class.forName("com.common.sdk.printer.UsbPrinterManager");
                Object systemService = this.mContext.getSystemService("UsbPrinter");
                try {
                    try {
                        try {
                            cls.getMethod("enlargeFontSize", Integer.TYPE, Integer.TYPE).invoke(systemService, Integer.valueOf(i), Integer.valueOf(i2));
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

    public synchronized void setFontSize(int i) throws TelpoException {
        try {
            try {
                Class<?> cls = Class.forName("com.common.sdk.printer.UsbPrinterManager");
                Object systemService = this.mContext.getSystemService("UsbPrinter");
                try {
                    try {
                        try {
                            cls.getMethod("setFontSize", Integer.TYPE).invoke(systemService, Integer.valueOf(i));
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

    public synchronized void setTextSize(int i) throws TelpoException {
        try {
            try {
                Class<?> cls = Class.forName("com.common.sdk.printer.UsbPrinterManager");
                Object systemService = this.mContext.getSystemService("UsbPrinter");
                try {
                    try {
                        try {
                            cls.getMethod("setTextSize", Integer.TYPE).invoke(systemService, Integer.valueOf(i));
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                            Exception exc = (Exception) e.getTargetException();
                            if (exc instanceof TelpoException) {
                                throw ((TelpoException) exc);
                            }
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

    public synchronized void setUnderline(boolean z) throws TelpoException {
        try {
            try {
                Class<?> cls = Class.forName("com.common.sdk.printer.UsbPrinterManager");
                Object systemService = this.mContext.getSystemService("UsbPrinter");
                try {
                    try {
                        try {
                            cls.getMethod("setUnderline", Boolean.TYPE).invoke(systemService, Boolean.valueOf(z));
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                            Exception exc = (Exception) e.getTargetException();
                            if (exc instanceof TelpoException) {
                                throw ((TelpoException) exc);
                            }
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

    public synchronized void setHighlight(boolean z) throws TelpoException {
        try {
            try {
                Class<?> cls = Class.forName("com.common.sdk.printer.UsbPrinterManager");
                Object systemService = this.mContext.getSystemService("UsbPrinter");
                try {
                    try {
                        try {
                            cls.getMethod("setHighlight", Boolean.TYPE).invoke(systemService, Boolean.valueOf(z));
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

    public synchronized void setGray(int i) throws TelpoException {
        try {
            try {
                Class<?> cls = Class.forName("com.common.sdk.printer.UsbPrinterManager");
                Object systemService = this.mContext.getSystemService("UsbPrinter");
                try {
                    try {
                        try {
                            cls.getMethod("setGray", Integer.TYPE).invoke(systemService, Integer.valueOf(i));
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

    public synchronized void setAlgin(int i) throws TelpoException {
        try {
            try {
                Class<?> cls = Class.forName("com.common.sdk.printer.UsbPrinterManager");
                Object systemService = this.mContext.getSystemService("UsbPrinter");
                try {
                    try {
                        try {
                            cls.getMethod("setAlgin", Integer.TYPE).invoke(systemService, Integer.valueOf(i));
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
        try {
            try {
                try {
                    Class.forName("com.common.sdk.printer.UsbPrinterManager").getMethod("addString", String.class).invoke(this.mContext.getSystemService("UsbPrinter"), str);
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

    public synchronized void addStringOffset(int i, String str) throws TelpoException {
        try {
            try {
                Class<?> cls = Class.forName("com.common.sdk.printer.UsbPrinterManager");
                Object systemService = this.mContext.getSystemService("UsbPrinter");
                try {
                    try {
                        try {
                            cls.getMethod("addStringOffset", Integer.TYPE, String.class).invoke(systemService, Integer.valueOf(i), str);
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

    public synchronized void endLine() throws TelpoException {
        try {
            try {
                Class<?> cls = Class.forName("com.common.sdk.printer.UsbPrinterManager");
                try {
                    try {
                        try {
                            cls.getMethod("endLine", new Class[0]).invoke(this.mContext.getSystemService("UsbPrinter"), new Object[0]);
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

    public synchronized void clearString() throws TelpoException {
        try {
            try {
                Class<?> cls = Class.forName("com.common.sdk.printer.UsbPrinterManager");
                try {
                    try {
                        try {
                            cls.getMethod("clearString", new Class[0]).invoke(this.mContext.getSystemService("UsbPrinter"), new Object[0]);
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

    public synchronized void printString() throws TelpoException {
        if (!TextUtils.isEmpty(this.buffer.toString())) {
            addString(this.buffer.toString());
            this.buffer.setLength(0);
        }
        try {
            Class<?> cls = Class.forName("com.common.sdk.printer.UsbPrinterManager");
            try {
                try {
                    try {
                        try {
                            cls.getMethod("printString", new Class[0]).invoke(this.mContext.getSystemService("UsbPrinter"), new Object[0]);
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

    public synchronized void printStringAndWalk(int i, int i2, int i3) throws TelpoException {
        if (!TextUtils.isEmpty(this.buffer.toString())) {
            addString(this.buffer.toString());
            this.buffer.setLength(0);
        }
        try {
            Class<?> cls = Class.forName("com.common.sdk.printer.UsbPrinterManager");
            Object systemService = this.mContext.getSystemService("UsbPrinter");
            try {
                try {
                    try {
                        cls.getMethod("printStringAndWalk", Integer.TYPE, Integer.TYPE, Integer.TYPE).invoke(systemService, Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3));
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
    }

    public synchronized void setLineSpace(int i) throws TelpoException {
        try {
            try {
                Class<?> cls = Class.forName("com.common.sdk.printer.UsbPrinterManager");
                Object systemService = this.mContext.getSystemService("UsbPrinter");
                try {
                    try {
                        try {
                            cls.getMethod("setLineSpace", Integer.TYPE).invoke(systemService, Integer.valueOf(i));
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

    public synchronized void setLeftIndent(int i) throws TelpoException {
        try {
            try {
                Class<?> cls = Class.forName("com.common.sdk.printer.UsbPrinterManager");
                Object systemService = this.mContext.getSystemService("UsbPrinter");
                try {
                    try {
                        try {
                            cls.getMethod("setLeftIndent", Integer.TYPE).invoke(systemService, Integer.valueOf(i));
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

    public synchronized void printLogo(Bitmap bitmap, boolean z) throws TelpoException {
        try {
            try {
                Class<?> cls = Class.forName("com.common.sdk.printer.UsbPrinterManager");
                Object systemService = this.mContext.getSystemService("UsbPrinter");
                try {
                    try {
                        try {
                            cls.getMethod("printLogo", Bitmap.class, Boolean.TYPE).invoke(systemService, bitmap, Boolean.valueOf(z));
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

    public synchronized String getVersion() throws TelpoException {
        String str;
        try {
            try {
                Class<?> cls = Class.forName("com.common.sdk.printer.UsbPrinterManager");
                try {
                    try {
                        try {
                            str = (String) cls.getMethod("getVersion", new Class[0]).invoke(this.mContext.getSystemService("UsbPrinter"), new Object[0]);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                            throw new InternalErrorException();
                        }
                    } catch (IllegalArgumentException e2) {
                        e2.printStackTrace();
                        throw new InternalErrorException();
                    } catch (InvocationTargetException e3) {
                        throwException(e3);
                        str = null;
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

    public synchronized void searchMark(int i, int i2) throws TelpoException {
        try {
            try {
                Class<?> cls = Class.forName("com.common.sdk.printer.UsbPrinterManager");
                Object systemService = this.mContext.getSystemService("UsbPrinter");
                try {
                    try {
                        try {
                            cls.getMethod("searchMark", Integer.TYPE, Integer.TYPE, Integer.TYPE).invoke(systemService, 0, Integer.valueOf(i), Integer.valueOf(i2));
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

    public synchronized void paperCut() throws TelpoException {
        try {
            try {
                Class<?> cls = Class.forName("com.common.sdk.printer.UsbPrinterManager");
                try {
                    try {
                        try {
                            cls.getMethod("paperCut", new Class[0]).invoke(this.mContext.getSystemService("UsbPrinter"), new Object[0]);
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

    public synchronized void setBold(boolean z) throws TelpoException {
        try {
            try {
                Class<?> cls = Class.forName("com.common.sdk.printer.UsbPrinterManager");
                Object systemService = this.mContext.getSystemService("UsbPrinter");
                try {
                    try {
                        try {
                            cls.getMethod("setBold", Boolean.TYPE).invoke(systemService, Boolean.valueOf(z));
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

    public int measureText(String str) throws TelpoException {
        try {
            Class<?> cls = Class.forName("com.common.sdk.printer.UsbPrinterManager");
            try {
                try {
                    return ((Integer) cls.getMethod("measureText", String.class).invoke(this.mContext.getSystemService("UsbPrinter"), str)).intValue();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    throw new InternalErrorException();
                } catch (IllegalArgumentException e2) {
                    e2.printStackTrace();
                    throw new InternalErrorException();
                } catch (InvocationTargetException e3) {
                    throwException(e3);
                    return -1;
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

    public synchronized void autoBreakSet(boolean z) throws TelpoException {
        try {
            try {
                Class<?> cls = Class.forName("com.common.sdk.printer.UsbPrinterManager");
                Object systemService = this.mContext.getSystemService("UsbPrinter");
                try {
                    try {
                        try {
                            cls.getMethod("setAutoBreak", Boolean.TYPE).invoke(systemService, Boolean.valueOf(z));
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

    public synchronized void setThripleHeight(boolean z) throws TelpoException {
        try {
            try {
                Class<?> cls = Class.forName("com.common.sdk.printer.UsbPrinterManager");
                Object systemService = this.mContext.getSystemService("UsbPrinter");
                try {
                    try {
                        try {
                            cls.getMethod("setTripleHeight", Boolean.TYPE).invoke(systemService, Boolean.valueOf(z));
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

    public synchronized void printLogoRaw(byte[] bArr, int i, int i2) throws TelpoException {
        try {
            try {
                Class<?> cls = Class.forName("com.common.sdk.printer.UsbPrinterManager");
                Object systemService = this.mContext.getSystemService("UsbPrinter");
                try {
                    try {
                        cls.getMethod("printLogoRaw", byte[].class, Integer.TYPE, Integer.TYPE).invoke(systemService, bArr, Integer.valueOf(i), Integer.valueOf(i2));
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
