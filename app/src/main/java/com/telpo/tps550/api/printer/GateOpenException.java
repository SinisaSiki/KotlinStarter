package com.telpo.tps550.api.printer;

import com.telpo.tps550.api.TelpoException;

/* loaded from: classes.dex */
public class GateOpenException extends TelpoException {
    private static final long serialVersionUID = 9004308459626928976L;

    @Override // com.telpo.tps550.api.TelpoException
    public String getDescription() {
        return "The gate is opened";
    }

    public GateOpenException() {
    }

    public GateOpenException(String str, Throwable th) {
        super(str, th);
    }

    public GateOpenException(String str) {
        super(str);
    }

    public GateOpenException(Throwable th) {
        super(th);
    }
}
