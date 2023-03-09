package com.telpo.tps550.api.printer;

import com.telpo.tps550.api.TelpoException;

/* loaded from: classes.dex */
public class LowPowerException extends TelpoException {
    private static final long serialVersionUID = 7038736269712690302L;

    @Override // com.telpo.tps550.api.TelpoException
    public String getDescription() {
        return "Lowpower,stop printing";
    }

    public LowPowerException() {
    }

    public LowPowerException(String str, Throwable th) {
        super(str, th);
    }

    public LowPowerException(String str) {
        super(str);
    }

    public LowPowerException(Throwable th) {
        super(th);
    }
}
