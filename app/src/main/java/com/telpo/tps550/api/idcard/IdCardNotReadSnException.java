package com.telpo.tps550.api.idcard;

import com.telpo.tps550.api.TelpoException;

/* loaded from: classes.dex */
public class IdCardNotReadSnException extends TelpoException {
    private static final long serialVersionUID = 1323450920926209145L;

    @Override // com.telpo.tps550.api.TelpoException
    public String getDescription() {
        return "Id Card SN Read Fail!";
    }

    public IdCardNotReadSnException() {
    }

    public IdCardNotReadSnException(String str, Throwable th) {
        super(str, th);
    }

    public IdCardNotReadSnException(String str) {
        super(str);
    }

    public IdCardNotReadSnException(Throwable th) {
        super(th);
    }
}
