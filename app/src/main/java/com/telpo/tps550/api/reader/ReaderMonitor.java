package com.telpo.tps550.api.reader;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.telpo.tps550.api.magnetic.MagneticCard;

/* loaded from: classes.dex */
public class ReaderMonitor {
    public static final String ACTION_ICC_PRESENT = "com.pos.icc.present";
    public static final String ACTION_MSC = "com.pos.msc";
    public static final String EXTRA_CARD_TYPE = "card_type";
    public static final String EXTRA_IS_PRESENT = "present";
    public static final String EXTRA_MSC_TRACK = "track_data";
    public static final String TAG = "ReaderMonitor";
    private static int cardType = 1;
    private static Context context = null;
    private static boolean iccPresent = false;
    private static boolean isStarted = false;
    private static Object lock = new Object();
    private static Thread monitorThread = null;
    private static String[] mscData = null;
    private static boolean mscFlag = false;
    private static Thread mscMonitorThread = null;
    private static boolean openFlag = false;
    private static boolean poweronFlag = false;
    private static CardReader reader;
    private static int type;

    public static void setContext(Context context2) {
        context = context2;
    }

    public static synchronized void startMonitor() {
        synchronized (ReaderMonitor.class) {
            if (context == null) {
                Log.e(TAG, "context null");
                return;
            }
            Thread thread = new Thread(new Runnable() { // from class: com.telpo.tps550.api.reader.ReaderMonitor.1
                Intent intent = null;

                @Override // java.lang.Runnable
                public void run() {
                    while (!ReaderMonitor.monitorThread.isInterrupted()) {
                        try {
                            Thread.sleep(500L);
                            if (!ReaderMonitor.openFlag) {
                                ReaderMonitor.reader = new SmartCardReader(ReaderMonitor.context);
                                if (ReaderMonitor.reader.open()) {
                                    ReaderMonitor.openFlag = true;
                                    ReaderMonitor.reader.switchMode(1);
                                } else {
                                    Log.e(ReaderMonitor.TAG, "reader open failed");
                                }
                            }
                            synchronized (ReaderMonitor.lock) {
                                if (ReaderMonitor.iccPresent) {
                                    if (!ReaderMonitor.reader.isICCPresent()) {
                                        ReaderMonitor.iccPresent = false;
                                        Intent intent = new Intent();
                                        this.intent = intent;
                                        intent.setAction(ReaderMonitor.ACTION_ICC_PRESENT);
                                        this.intent.putExtra(ReaderMonitor.EXTRA_IS_PRESENT, false);
                                        if (ReaderMonitor.context != null) {
                                            ReaderMonitor.context.sendBroadcast(this.intent);
                                        }
                                        ReaderMonitor.poweronFlag = false;
                                        ReaderMonitor.reader.close();
                                        ReaderMonitor.openFlag = false;
                                    }
                                } else if (ReaderMonitor.reader.isICCPresent()) {
                                    ReaderMonitor.iccPresent = true;
                                    Intent intent2 = new Intent();
                                    this.intent = intent2;
                                    intent2.setAction(ReaderMonitor.ACTION_ICC_PRESENT);
                                    this.intent.putExtra(ReaderMonitor.EXTRA_IS_PRESENT, true);
                                    if (!ReaderMonitor.reader.iccPowerOn()) {
                                        ReaderMonitor.reader.switchMode(3);
                                        if (ReaderMonitor.reader.iccPowerOn()) {
                                            ReaderMonitor.type = ReaderMonitor.reader.getCardType();
                                            this.intent.putExtra(ReaderMonitor.EXTRA_CARD_TYPE, ReaderMonitor.type);
                                            if (ReaderMonitor.context != null) {
                                                ReaderMonitor.context.sendBroadcast(this.intent);
                                            }
                                            if (ReaderMonitor.type != 3) {
                                                if (ReaderMonitor.type == 2) {
                                                    Log.d(ReaderMonitor.TAG, "card type: SLE4442");
                                                    ReaderMonitor.cardType = 2;
                                                    if (ReaderMonitor.reader.close()) {
                                                        ReaderMonitor.openFlag = false;
                                                        ReaderMonitor.reader = new SLE4442Reader(ReaderMonitor.context);
                                                        if (ReaderMonitor.reader.open()) {
                                                            ReaderMonitor.openFlag = true;
                                                            if (ReaderMonitor.reader.iccPowerOn()) {
                                                                Log.d(ReaderMonitor.TAG, "SLE4442 poweron success");
                                                                ReaderMonitor.poweronFlag = true;
                                                            } else {
                                                                Log.d(ReaderMonitor.TAG, "SLE4442 poweron failed");
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    Log.e(ReaderMonitor.TAG, "card type unknown");
                                                    ReaderMonitor.cardType = -1;
                                                }
                                            } else {
                                                Log.d(ReaderMonitor.TAG, "card type: SLE4428");
                                                ReaderMonitor.cardType = 3;
                                                if (ReaderMonitor.reader.close()) {
                                                    ReaderMonitor.openFlag = false;
                                                    ReaderMonitor.reader = new SLE4428Reader(ReaderMonitor.context);
                                                    if (ReaderMonitor.reader.open()) {
                                                        ReaderMonitor.openFlag = true;
                                                        if (ReaderMonitor.reader.iccPowerOn()) {
                                                            Log.d(ReaderMonitor.TAG, "SLE4428 poweron success");
                                                            ReaderMonitor.poweronFlag = true;
                                                        } else {
                                                            Log.d(ReaderMonitor.TAG, "SLE4428 poweron failed");
                                                        }
                                                    }
                                                }
                                            }
                                        } else {
                                            Log.e(ReaderMonitor.TAG, "ICC poweron failed");
                                        }
                                    } else {
                                        Log.e(ReaderMonitor.TAG, "smart card poweron success");
                                        this.intent.putExtra(ReaderMonitor.EXTRA_CARD_TYPE, 1);
                                        if (ReaderMonitor.context != null) {
                                            ReaderMonitor.context.sendBroadcast(this.intent);
                                        }
                                        ReaderMonitor.poweronFlag = true;
                                        ReaderMonitor.cardType = 1;
                                    }
                                }
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                }
            });
            monitorThread = thread;
            thread.start();
            isStarted = true;
        }
    }

    public static synchronized void stopMonitor() {
        synchronized (ReaderMonitor.class) {
            Thread thread = monitorThread;
            if (thread != null) {
                thread.interrupt();
            }
            Thread thread2 = mscMonitorThread;
            if (thread2 != null) {
                thread2.interrupt();
            }
            if (openFlag && reader.close()) {
                openFlag = false;
            }
            MagneticCard.close();
            mscFlag = false;
            poweronFlag = false;
            isStarted = false;
            iccPresent = false;
        }
    }

    public static synchronized boolean isStarted() {
        boolean z;
        synchronized (ReaderMonitor.class) {
            z = isStarted;
        }
        return z;
    }

    public static synchronized boolean isICCPresent() {
        boolean z;
        synchronized (ReaderMonitor.class) {
            z = iccPresent;
        }
        return z;
    }

    public static synchronized byte[] readMainMemory(int i, int i2) {
        synchronized (ReaderMonitor.class) {
            if (!openFlag) {
                Log.e(TAG, "reader has not opened");
                return null;
            }
            synchronized (lock) {
                if (!poweronFlag) {
                    if (!reader.iccPowerOn()) {
                        return null;
                    }
                    poweronFlag = true;
                }
                int i3 = cardType;
                if (i3 == 2) {
                    return ((SLE4442Reader) reader).readMainMemory(i, i2);
                } else if (i3 != 3) {
                    return null;
                } else {
                    return ((SLE4428Reader) reader).readMainMemory(i, i2);
                }
            }
        }
    }

    public static synchronized boolean updateMainMemory(int i, byte[] bArr) {
        synchronized (ReaderMonitor.class) {
            if (!openFlag) {
                Log.e(TAG, "reader has not opened");
                return false;
            }
            synchronized (lock) {
                int i2 = cardType;
                if (i2 == 2) {
                    return ((SLE4442Reader) reader).updateMainMemory(i, bArr);
                } else if (i2 != 3) {
                    return true;
                } else {
                    return ((SLE4428Reader) reader).updateMainMemory(i, bArr);
                }
            }
        }
    }

    public static synchronized boolean pscVerify(byte[] bArr) {
        synchronized (ReaderMonitor.class) {
            if (!openFlag) {
                Log.e(TAG, "reader has not opened");
                return false;
            }
            synchronized (lock) {
                if (!poweronFlag) {
                    if (!reader.iccPowerOn()) {
                        return false;
                    }
                    poweronFlag = true;
                }
                int i = cardType;
                if (i == 2) {
                    if (!((SLE4442Reader) reader).pscVerify(bArr)) {
                        Log.e(TAG, "SLE4442 psc verification failed");
                        return false;
                    }
                } else if (i == 3 && !((SLE4428Reader) reader).pscVerify(bArr)) {
                    Log.e(TAG, "SLE4428 psc verification failed");
                    return false;
                }
                return true;
            }
        }
    }

    public static synchronized void reset() {
        synchronized (ReaderMonitor.class) {
            if (!openFlag) {
                Log.e(TAG, "reader has not opened");
                return;
            }
            synchronized (lock) {
                if (reader.iccPowerOff()) {
                    poweronFlag = false;
                }
                if (reader.iccPowerOn()) {
                    poweronFlag = true;
                }
            }
        }
    }

    public static synchronized byte[] getUserCode() {
        synchronized (ReaderMonitor.class) {
            int i = cardType;
            if (i == 2 || i == 3) {
                return readMainMemory(21, 6);
            }
            return null;
        }
    }

    public static synchronized boolean pscModify(byte[] bArr) {
        synchronized (ReaderMonitor.class) {
            if (!openFlag) {
                Log.e(TAG, "reader has not opened");
                return false;
            }
            synchronized (lock) {
                int i = cardType;
                if (i == 2) {
                    return ((SLE4442Reader) reader).pscModify(bArr);
                } else if (i != 3) {
                    return false;
                } else {
                    return ((SLE4428Reader) reader).pscModify(bArr);
                }
            }
        }
    }

    public static synchronized byte[] transmit(byte[] bArr) {
        synchronized (ReaderMonitor.class) {
            if (!openFlag) {
                Log.e(TAG, "reader has not opened");
                return null;
            }
            synchronized (lock) {
                if (cardType != 1) {
                    return null;
                }
                return ((SmartCardReader) reader).transmit(bArr);
            }
        }
    }

    public static synchronized int getProtocol() {
        synchronized (ReaderMonitor.class) {
            if (!openFlag) {
                Log.e(TAG, "reader has not opened");
                return 2;
            }
            synchronized (lock) {
                if (cardType != 1) {
                    return 2;
                }
                return ((SmartCardReader) reader).getProtocol();
            }
        }
    }

    public static synchronized String getATRString() {
        String aTRString;
        synchronized (ReaderMonitor.class) {
            if (!openFlag) {
                Log.e(TAG, "reader has not opened");
                return null;
            }
            synchronized (lock) {
                aTRString = reader.getATRString();
            }
            return aTRString;
        }
    }
}
