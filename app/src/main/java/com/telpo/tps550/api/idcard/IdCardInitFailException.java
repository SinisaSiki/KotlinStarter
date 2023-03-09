package com.telpo.tps550.api.idcard;

import com.telpo.tps550.api.TelpoException;

/* loaded from: classes.dex */
public class IdCardInitFailException extends TelpoException {
    private static final long serialVersionUID = -1629955331916689474L;

    @Override // com.telpo.tps550.api.TelpoException
    public String getDescription() {
        return "Failed to init the decode library!";
    }

    public IdCardInitFailException() {
    }

    public IdCardInitFailException(String str, Throwable th) {
        super(str, th);
    }

    public IdCardInitFailException(String str) {
        super(str);
    }

    public IdCardInitFailException(Throwable th) {
        super(th);
    }
}
