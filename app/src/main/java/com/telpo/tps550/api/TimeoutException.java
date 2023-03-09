package com.telpo.tps550.api;

/* loaded from: classes.dex */
public class TimeoutException extends TelpoException {
    private static final long serialVersionUID = 8323068427519122516L;

    @Override // com.telpo.tps550.api.TelpoException
    public String getDescription() {
        return "Operation timeout!";
    }

    public TimeoutException() {
    }

    public TimeoutException(String str, Throwable th) {
        super(str, th);
    }

    public TimeoutException(String str) {
        super(str);
    }

    public TimeoutException(Throwable th) {
        super(th);
    }
}
