package com.telpo.tps550.api;

/* loaded from: classes.dex */
public class NotSupportYetException extends TelpoException {
    private static final long serialVersionUID = 2975865503741011861L;

    @Override // com.telpo.tps550.api.TelpoException
    public String getDescription() {
        return "The operation or the function is not supported for current SDK/Hardware !";
    }

    public NotSupportYetException() {
    }

    public NotSupportYetException(String str, Throwable th) {
        super(str, th);
    }

    public NotSupportYetException(String str) {
        super(str);
    }

    public NotSupportYetException(Throwable th) {
        super(th);
    }
}
