package com.telpo.tps550.api;

/* loaded from: classes.dex */
public class DeviceOverFlowException extends TelpoException {
    private static final long serialVersionUID = -2720559549044825415L;

    @Override // com.telpo.tps550.api.TelpoException
    public String getDescription() {
        return "Memory or buffer over flow!";
    }

    public DeviceOverFlowException() {
    }

    public DeviceOverFlowException(String str, Throwable th) {
        super(str, th);
    }

    public DeviceOverFlowException(String str) {
        super(str);
    }

    public DeviceOverFlowException(Throwable th) {
        super(th);
    }
}
