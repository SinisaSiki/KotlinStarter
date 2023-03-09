package com.telpo.tps550.api.printer;

import com.telpo.tps550.api.TelpoException;

/* loaded from: classes.dex */
public class BlackBlockNotFoundException extends TelpoException {
    private static final long serialVersionUID = 1;

    @Override // com.telpo.tps550.api.TelpoException
    public String getDescription() {
        return "The Printer Cannot Find Black Block!";
    }

    public BlackBlockNotFoundException() {
    }

    public BlackBlockNotFoundException(String str, Throwable th) {
        super(str, th);
    }

    public BlackBlockNotFoundException(String str) {
        super(str);
    }

    public BlackBlockNotFoundException(Throwable th) {
        super(th);
    }
}
