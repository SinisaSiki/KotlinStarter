package com.telpo.tps550.api.printer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.telpo.tps550.api.DeviceAlreadyOpenException;
import com.telpo.tps550.api.DeviceNotOpenException;
import com.telpo.tps550.api.TelpoException;
import com.telpo.tps550.api.printer.StyleConfig;
import com.telpo.tps550.api.util.StringUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class Printer {
    private static final int ACTION_CMD = 5;
    private static final int ACTION_LED = 2;
    private static final int ACTION_MARK = 4;
    private static final int ACTION_PRINT = 0;
    private static final int ACTION_STATUS = 1;
    private static final int ACTION_VERSION = 3;
    public static final int ERROR_LED = 7;
    public static final int LED_OFF = 0;
    public static final int LED_ON = 1;
    private static final int MODE_BAR = 2;
    private static final int MODE_PIC = 1;
    private static final int MODE_TEXT = 0;
    public static final int NO_PAPER_LED = 4;
    public static final int POWER_LED = 1;
    public static final int STATUS_DISCONNECT = -1004;
    public static final int STATUS_NO_PAPER = -1001;
    public static final int STATUS_OK = 0;
    public static final int STATUS_OVER_FLOW = -1003;
    public static final int STATUS_OVER_HEAT = -1002;
    public static final int STATUS_UNKNOWN = -9999;
    private static final String TAG = "Printer";
    public static final int WORKING_LED = 2;
    private static int barcode_mode = -1;
    private static Handler handler = null;
    private static HandlerThread handlerThread = null;
    private static Object lock = new Object();
    private static int mStatus = -9999;
    private static String mVersion;
    private static List<PrintItem> printList;

    /* loaded from: classes.dex */
    public static class PrintItem {
        public Bitmap bitmap;
        public int feed;
        public int mode;
        public String string;
        public StyleConfig styleConfig;

        public PrintItem(String str, StyleConfig styleConfig) {
            this.bitmap = null;
            this.feed = 0;
            this.mode = 0;
            this.string = str;
            this.styleConfig = styleConfig;
        }

        public PrintItem(Bitmap bitmap, StyleConfig styleConfig) {
            this.string = null;
            this.feed = 0;
            this.styleConfig = styleConfig;
            this.bitmap = bitmap;
            this.mode = 1;
        }
    }

    public static synchronized void printText(String str, StyleConfig styleConfig) {
        synchronized (Printer.class) {
            if (str != null) {
                if (printList != null) {
                    StyleConfig styleConfig2 = new StyleConfig();
                    if (styleConfig != null) {
                        styleConfig2.fontFamily = styleConfig.fontFamily;
                        styleConfig2.fontSize = styleConfig.fontSize;
                        styleConfig2.fontStyle = styleConfig.fontStyle;
                        styleConfig2.align = styleConfig.align;
                        styleConfig2.gray = styleConfig.gray;
                        styleConfig2.lineSpace = styleConfig.lineSpace;
                        styleConfig2.newLine = styleConfig.newLine;
                    }
                    printList.add(new PrintItem(str, styleConfig2));
                }
            }
        }
    }

    public static synchronized void printBarCode(String str, StyleConfig.Align align) {
        synchronized (Printer.class) {
            if (str != null) {
                if (printList != null) {
                    if (barcode_mode < 0) {
                        barcode_mode = 0;
                        try {
                            String trim = ThermalPrinter.getVersion().trim();
                            if (trim.substring(trim.length() - 8).compareTo("20151106") >= 0) {
                                barcode_mode = 1;
                            }
                        } catch (TelpoException e) {
                            e.printStackTrace();
                        }
                    }
                    if (barcode_mode == 1) {
                        StyleConfig styleConfig = new StyleConfig();
                        styleConfig.align = align;
                        PrintItem printItem = new PrintItem(str, styleConfig);
                        printItem.mode = 2;
                        printList.add(printItem);
                    } else {
                        try {
                            printList.add(new PrintItem(adjustBitmap(CreateCode(str, BarcodeFormat.CODE_128, 360, 64), align), new StyleConfig()));
                        } catch (WriterException e2) {
                            e2.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public static synchronized void printQRCode(String str, StyleConfig.Align align) {
        synchronized (Printer.class) {
            if (str != null) {
                if (printList != null) {
                    try {
                        Bitmap CreateCode = CreateCode(str, BarcodeFormat.QR_CODE, 256, 256);
                        printList.add(new PrintItem(adjustBitmap(Bitmap.createBitmap(CreateCode, 40, 40, CreateCode.getWidth() - 80, CreateCode.getHeight() - 80), align), new StyleConfig()));
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static synchronized void printImage(String str, StyleConfig.Align align) {
        synchronized (Printer.class) {
            if (str != null) {
                if (printList != null) {
                    if (new File(str).exists()) {
                        Bitmap decodeFile = BitmapFactory.decodeFile(str);
                        printList.add(new PrintItem(adjustBitmap(Bitmap.createBitmap(decodeFile, 0, 16, decodeFile.getWidth(), decodeFile.getHeight() - 32), align), new StyleConfig()));
                    }
                }
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:116:0x019c  */
    /* JADX WARN: Removed duplicated region for block: B:155:0x0192 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static void commitOperation(java.util.List<com.telpo.tps550.api.printer.Printer.PrintItem> r19, com.telpo.tps550.api.printer.ICommitCallback r20) {
        /*
            Method dump skipped, instructions count: 489
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.telpo.tps550.api.printer.Printer.commitOperation(java.util.List, com.telpo.tps550.api.printer.ICommitCallback):void");
    }

    public static synchronized void commitOperation() {
        synchronized (Printer.class) {
            if (printList != null && handler != null) {
                ArrayList arrayList = new ArrayList(printList.size());
                for (PrintItem printItem : printList) {
                    arrayList.add(printItem);
                }
                CommitData commitData = new CommitData(null);
                commitData.printList = arrayList;
                commitData.callback = null;
                handler.sendMessage(handler.obtainMessage(0, commitData));
                printList.clear();
            }
        }
    }

    public static synchronized void commitOperation(ICommitCallback iCommitCallback) {
        synchronized (Printer.class) {
            if (printList != null && handler != null) {
                ArrayList arrayList = new ArrayList(printList.size());
                for (PrintItem printItem : printList) {
                    arrayList.add(printItem);
                }
                CommitData commitData = new CommitData(null);
                commitData.printList = arrayList;
                commitData.callback = iCommitCallback;
                handler.sendMessage(handler.obtainMessage(0, commitData));
                printList.clear();
            }
        }
    }

    /* loaded from: classes.dex */
    private static class MyHandler extends Handler {
        public MyHandler(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what == 0) {
                CommitData commitData = (CommitData) message.obj;
                Printer.commitOperation(commitData.printList, commitData.callback);
                try {
                    ThermalPrinter.clearString();
                } catch (TelpoException e) {
                    e.printStackTrace();
                }
            } else if (message.what == 1) {
                synchronized (Printer.lock) {
                    try {
                        int checkStatus = ThermalPrinter.checkStatus();
                        if (checkStatus == 0) {
                            Printer.mStatus = 0;
                        } else if (checkStatus == 1) {
                            Printer.mStatus = Printer.STATUS_NO_PAPER;
                        } else if (checkStatus == 2) {
                            Printer.mStatus = Printer.STATUS_OVER_HEAT;
                        } else if (checkStatus != 3) {
                            Printer.mStatus = Printer.STATUS_UNKNOWN;
                        } else {
                            Printer.mStatus = Printer.STATUS_OVER_FLOW;
                        }
                    } catch (DeviceNotOpenException unused) {
                        Printer.mStatus = Printer.STATUS_DISCONNECT;
                    } catch (TelpoException e2) {
                        e2.printStackTrace();
                        Printer.mStatus = Printer.STATUS_UNKNOWN;
                    }
                    Printer.lock.notify();
                }
            } else if (message.what == 2) {
                try {
                    ThermalPrinter.sendCommand(new byte[]{27, 8, (byte) message.arg1, (byte) message.arg2}, 4);
                } catch (TelpoException e3) {
                    e3.printStackTrace();
                }
            } else if (message.what == 3) {
                synchronized (Printer.lock) {
                    try {
                        Printer.mVersion = ThermalPrinter.getVersion();
                    } catch (TelpoException e4) {
                        e4.printStackTrace();
                    }
                    Printer.lock.notify();
                }
            } else if (message.what == 4) {
                synchronized (Printer.lock) {
                    int[] iArr = (int[]) message.obj;
                    try {
                        ThermalPrinter.searchMark(iArr[0], iArr[1]);
                    } catch (TelpoException e5) {
                        e5.printStackTrace();
                    }
                    Printer.lock.notify();
                }
            } else if (message.what != 5) {
            } else {
                synchronized (Printer.lock) {
                    try {
                        ThermalPrinter.sendCommand((byte[]) message.obj, message.arg1);
                    } catch (TelpoException e6) {
                        e6.printStackTrace();
                    }
                    Printer.lock.notify();
                }
            }
        }
    }

    public static synchronized int connect() {
        int i;
        synchronized (Printer.class) {
            try {
                if (printList == null) {
                    printList = new ArrayList();
                }
                if (handlerThread == null) {
                    HandlerThread handlerThread2 = new HandlerThread(TAG);
                    handlerThread = handlerThread2;
                    handlerThread2.start();
                    handler = new MyHandler(handlerThread.getLooper());
                }
                ThermalPrinter.start();
            } catch (DeviceAlreadyOpenException unused) {
            } catch (TelpoException e) {
                e.printStackTrace();
                i = -1;
            }
            i = 0;
        }
        return i;
    }

    public static synchronized void disconnect() {
        synchronized (Printer.class) {
            ThermalPrinter.stop();
            List<PrintItem> list = printList;
            if (list != null) {
                list.clear();
                printList = null;
            }
            HandlerThread handlerThread2 = handlerThread;
            if (handlerThread2 != null) {
                handlerThread2.quit();
                handlerThread = null;
                handler = null;
            }
            barcode_mode = -1;
            mVersion = null;
        }
    }

    public static synchronized void reset() {
        synchronized (Printer.class) {
            try {
                ThermalPrinter.reset();
            } catch (TelpoException e) {
                e.printStackTrace();
            }
            List<PrintItem> list = printList;
            if (list != null) {
                list.clear();
            }
        }
    }

    public static synchronized void feedPaper(int i) {
        synchronized (Printer.class) {
            if (i > 0) {
                if (printList != null) {
                    PrintItem printItem = new PrintItem("", new StyleConfig());
                    printItem.feed = i;
                    printList.add(printItem);
                }
            }
        }
    }

    public static synchronized int getStatus() {
        int i;
        synchronized (Printer.class) {
            synchronized (lock) {
                mStatus = STATUS_UNKNOWN;
                handler.sendEmptyMessage(1);
                try {
                    lock.wait(30000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            i = mStatus;
        }
        return i;
    }

    public static synchronized boolean isConnected() {
        synchronized (Printer.class) {
            if (printList != null) {
                if (handlerThread != null) {
                    return true;
                }
            }
            return false;
        }
    }

    public static synchronized void ledCtrl(int i, int i2) {
        synchronized (Printer.class) {
            handler.sendMessage(handler.obtainMessage(2, i, i2));
        }
    }

    public static synchronized String getVersion() {
        String str;
        synchronized (Printer.class) {
            if (mVersion == null) {
                synchronized (lock) {
                    handler.sendEmptyMessage(3);
                    try {
                        lock.wait(30000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            str = mVersion;
        }
        return str;
    }

    public static synchronized void searchMark(int i, int i2) {
        synchronized (Printer.class) {
            synchronized (lock) {
                handler.sendMessage(handler.obtainMessage(4, new int[]{i, i2}));
                try {
                    lock.wait(30000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static synchronized void sendCommand(byte[] bArr, int i) {
        synchronized (Printer.class) {
            synchronized (lock) {
                handler.sendMessage(handler.obtainMessage(5, i, i, bArr));
                try {
                    lock.wait(30000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static synchronized void sendCommand(String str) {
        synchronized (Printer.class) {
            if (str == null) {
                return;
            }
            synchronized (lock) {
                byte[] bytes = StringUtil.toBytes(str.replace(" ", ""));
                handler.sendMessage(handler.obtainMessage(5, bytes.length, bytes.length, bytes));
                try {
                    lock.wait(30000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /* loaded from: classes.dex */
    private static class CommitData {
        public ICommitCallback callback;
        public List<PrintItem> printList;

        private CommitData() {
            this.printList = null;
            this.callback = null;
        }

        /* synthetic */ CommitData(CommitData commitData) {
            this();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:14:0x0029, code lost:
        if (r12 != 0) goto L9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x0018, code lost:
        if (r12 != 0) goto L9;
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x001a, code lost:
        r1 = r1 + (8 - r12);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static android.graphics.Bitmap adjustBitmap(android.graphics.Bitmap r11, com.telpo.tps550.api.printer.StyleConfig.Align r12) {
        /*
            r0 = 0
            if (r11 != 0) goto L4
            return r0
        L4:
            int r1 = r11.getWidth()
            int r2 = r11.getHeight()
            r3 = 0
            com.telpo.tps550.api.printer.StyleConfig$Align r4 = com.telpo.tps550.api.printer.StyleConfig.Align.CENTER
            if (r12 != r4) goto L1e
            int r12 = 384 - r1
            int r3 = r12 / 2
            int r1 = r1 + r3
            int r12 = r1 % 8
            if (r12 == 0) goto L2c
        L1a:
            int r12 = 8 - r12
            int r1 = r1 + r12
            goto L2c
        L1e:
            com.telpo.tps550.api.printer.StyleConfig$Align r4 = com.telpo.tps550.api.printer.StyleConfig.Align.RIGHT
            if (r12 != r4) goto L27
            int r3 = 384 - r1
            r1 = 384(0x180, float:5.38E-43)
            goto L2c
        L27:
            int r12 = r1 % 8
            if (r12 == 0) goto L2c
            goto L1a
        L2c:
            android.graphics.Bitmap$Config r12 = r11.getConfig()
            android.graphics.Bitmap r12 = android.graphics.Bitmap.createBitmap(r1, r2, r12)
            android.graphics.Paint r9 = new android.graphics.Paint
            r9.<init>()
            r4 = -1
            r9.setColor(r4)
            android.graphics.Canvas r10 = new android.graphics.Canvas
            r10.<init>(r12)
            r5 = 0
            r6 = 0
            float r7 = (float) r1
            float r8 = (float) r2
            r4 = r10
            r4.drawRect(r5, r6, r7, r8, r9)
            float r1 = (float) r3
            r2 = 0
            r10.drawBitmap(r11, r1, r2, r0)
            return r12
        */
        throw new UnsupportedOperationException("Method not decompiled: com.telpo.tps550.api.printer.Printer.adjustBitmap(android.graphics.Bitmap, com.telpo.tps550.api.printer.StyleConfig$Align):android.graphics.Bitmap");
    }

    private static Bitmap CreateCode(String str, BarcodeFormat barcodeFormat, int i, int i2) throws WriterException {
        BitMatrix encode = new MultiFormatWriter().encode(str, barcodeFormat, i, i2);
        int width = encode.getWidth();
        int height = encode.getHeight();
        int[] iArr = new int[width * height];
        for (int i3 = 0; i3 < height; i3++) {
            for (int i4 = 0; i4 < width; i4++) {
                if (encode.get(i4, i3)) {
                    iArr[(i3 * width) + i4] = -16777216;
                } else {
                    iArr[(i3 * width) + i4] = -1;
                }
            }
        }
        Bitmap createBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        createBitmap.setPixels(iArr, 0, width, 0, 0, width, height);
        return createBitmap;
    }

    private static void printerReset() {
        try {
            ThermalPrinter.reset();
        } catch (TelpoException e) {
            e.printStackTrace();
        }
    }
}
