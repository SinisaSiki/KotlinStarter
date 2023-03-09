package com.telpo.tps550.api.iccard;

import com.telpo.tps550.api.TelpoException;

/* loaded from: classes.dex */
public class ICCardException extends TelpoException {
    private static final long serialVersionUID = 6856857831426865055L;

    @Override // com.telpo.tps550.api.TelpoException
    public String getDescription() {
        return "Exception occur during IC card operation!";
    }

    public ICCardException() {
    }

    public ICCardException(String str, Throwable th) {
        super(str, th);
    }

    public ICCardException(String str) {
        super(str);
    }

    public ICCardException(Throwable th) {
        super(th);
    }
}
