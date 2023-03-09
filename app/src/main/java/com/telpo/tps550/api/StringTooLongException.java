package com.telpo.tps550.api;

/* loaded from: classes.dex */
public class StringTooLongException extends TelpoException {
    private static final long serialVersionUID = 1;

    @Override // com.telpo.tps550.api.TelpoException
    public String getDescription() {
        return "content too long";
    }

    public StringTooLongException() {
    }

    public StringTooLongException(String str, Throwable th) {
        super(str, th);
    }

    public StringTooLongException(String str) {
        super(str);
    }

    public StringTooLongException(Throwable th) {
        super(th);
    }
}
