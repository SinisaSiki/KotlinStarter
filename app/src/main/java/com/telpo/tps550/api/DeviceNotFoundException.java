package com.telpo.tps550.api;

/* loaded from: classes.dex */
public class DeviceNotFoundException extends TelpoException {
    private static final long serialVersionUID = 1;

    @Override // com.telpo.tps550.api.TelpoException
    public String getDescription() {
        return "Cannot find device!";
    }

    public DeviceNotFoundException() {
    }

    public DeviceNotFoundException(String str, Throwable th) {
        super(str, th);
    }

    public DeviceNotFoundException(String str) {
        super(str);
    }

    public DeviceNotFoundException(Throwable th) {
        super(th);
    }
}
