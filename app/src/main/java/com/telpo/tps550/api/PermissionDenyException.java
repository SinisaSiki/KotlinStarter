package com.telpo.tps550.api;

/* loaded from: classes.dex */
public class PermissionDenyException extends TelpoException {
    private static final long serialVersionUID = 1149990948139378861L;

    @Override // com.telpo.tps550.api.TelpoException
    public String getDescription() {
        return "Permission Deny!";
    }

    public PermissionDenyException() {
    }

    public PermissionDenyException(String str, Throwable th) {
        super(str, th);
    }

    public PermissionDenyException(String str) {
        super(str);
    }

    public PermissionDenyException(Throwable th) {
        super(th);
    }
}
