package com.telpo.tps550.api;

/* loaded from: classes.dex */
public class TelpoException extends Exception {
    private static final long serialVersionUID = 1136193940236894072L;

    public String getDescription() {
        return "Exception occur during pos device operation!";
    }

    public TelpoException() {
    }

    public TelpoException(String str, Throwable th) {
        super(str, th);
    }

    public TelpoException(String str) {
        super(str);
    }

    public TelpoException(Throwable th) {
        super(th);
    }
}
