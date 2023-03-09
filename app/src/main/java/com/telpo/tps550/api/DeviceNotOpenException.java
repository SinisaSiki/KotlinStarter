package com.telpo.tps550.api;

/* loaded from: classes.dex */
public class DeviceNotOpenException extends TelpoException {
    private static final long serialVersionUID = -3693748800173552918L;

    @Override // com.telpo.tps550.api.TelpoException
    public String getDescription() {
        return "Device not open!";
    }

    public DeviceNotOpenException() {
    }

    public DeviceNotOpenException(String str, Throwable th) {
        super(str, th);
    }

    public DeviceNotOpenException(String str) {
        super(str);
    }

    public DeviceNotOpenException(Throwable th) {
        super(th);
    }
}
