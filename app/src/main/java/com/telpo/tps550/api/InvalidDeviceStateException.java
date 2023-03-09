package com.telpo.tps550.api;

/* loaded from: classes.dex */
public class InvalidDeviceStateException extends TelpoException {
    private static final long serialVersionUID = 1;

    @Override // com.telpo.tps550.api.TelpoException
    public String getDescription() {
        return "InvalidDeviceStateException!";
    }

    public InvalidDeviceStateException() {
    }

    public InvalidDeviceStateException(String str, Throwable th) {
        super(str, th);
    }

    public InvalidDeviceStateException(String str) {
        super(str);
    }

    public InvalidDeviceStateException(Throwable th) {
        super(th);
    }
}
