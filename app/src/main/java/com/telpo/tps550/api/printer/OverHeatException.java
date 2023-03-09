package com.telpo.tps550.api.printer;

import com.telpo.tps550.api.TelpoException;

/* loaded from: classes.dex */
public class OverHeatException extends TelpoException {
    private static final long serialVersionUID = 8044094932145456087L;

    @Override // com.telpo.tps550.api.TelpoException
    public String getDescription() {
        return "The printer is overheating!";
    }

    public OverHeatException() {
    }

    public OverHeatException(String str, Throwable th) {
        super(str, th);
    }

    public OverHeatException(String str) {
        super(str);
    }

    public OverHeatException(Throwable th) {
        super(th);
    }
}
