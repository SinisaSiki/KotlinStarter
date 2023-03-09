package com.telpo.tps550.api.printer;

import com.telpo.tps550.api.TelpoException;

/* loaded from: classes.dex */
public class NoPaperException extends TelpoException {
    private static final long serialVersionUID = 9004308459676928976L;

    @Override // com.telpo.tps550.api.TelpoException
    public String getDescription() {
        return "The printer paper out!";
    }

    public NoPaperException() {
    }

    public NoPaperException(String str, Throwable th) {
        super(str, th);
    }

    public NoPaperException(String str) {
        super(str);
    }

    public NoPaperException(Throwable th) {
        super(th);
    }
}
