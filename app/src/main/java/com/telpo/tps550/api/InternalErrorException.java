package com.telpo.tps550.api;

/* loaded from: classes.dex */
public class InternalErrorException extends TelpoException {
    private static final long serialVersionUID = 8612451431999960519L;

    @Override // com.telpo.tps550.api.TelpoException
    public String getDescription() {
        return "Unexpected error occur!";
    }

    public InternalErrorException() {
    }

    public InternalErrorException(String str, Throwable th) {
        super(str, th);
    }

    public InternalErrorException(String str) {
        super(str);
    }

    public InternalErrorException(Throwable th) {
        super(th);
    }
}
