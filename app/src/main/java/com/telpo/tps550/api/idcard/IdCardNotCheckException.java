package com.telpo.tps550.api.idcard;

import com.telpo.tps550.api.TelpoException;

/* loaded from: classes.dex */
public class IdCardNotCheckException extends TelpoException {
    private static final long serialVersionUID = 1234509209262091485L;

    @Override // com.telpo.tps550.api.TelpoException
    public String getDescription() {
        return "Id Card infomation has not been checked!";
    }

    public IdCardNotCheckException() {
    }

    public IdCardNotCheckException(String str, Throwable th) {
        super(str, th);
    }

    public IdCardNotCheckException(String str) {
        super(str);
    }

    public IdCardNotCheckException(Throwable th) {
        super(th);
    }
}
