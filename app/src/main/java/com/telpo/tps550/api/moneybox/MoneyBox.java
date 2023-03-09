package com.telpo.tps550.api.moneybox;

/* loaded from: classes.dex */
public class MoneyBox {
    private static native int close_box();

    private static native int open_box();

    public static native int printerPower(int i);

    public static int open() {
        int open_box = open_box();
        if (open_box < 0) {
            return open_box == -1 ? -3 : -1;
        }
        return 0;
    }

    public static int close() {
        return close_box();
    }

    static {
        System.loadLibrary("moneybox");
    }
}
