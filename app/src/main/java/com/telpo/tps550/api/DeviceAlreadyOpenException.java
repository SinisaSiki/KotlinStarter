package com.telpo.tps550.api;

/* loaded from: classes.dex */
public class DeviceAlreadyOpenException extends TelpoException {
    private static final long serialVersionUID = 775254919822242857L;

    @Override // com.telpo.tps550.api.TelpoException
    public String getDescription() {
        return "Device already opened!";
    }

    public DeviceAlreadyOpenException() {
    }

    public DeviceAlreadyOpenException(String str, Throwable th) {
        super(str, th);
    }

    public DeviceAlreadyOpenException(String str) {
        super(str);
    }

    public DeviceAlreadyOpenException(Throwable th) {
        super(th);
    }
}
