package com.telpo.tps550.api.iccard;

/* loaded from: classes.dex */
public class RemovedCardException extends ICCardException {
    private static final long serialVersionUID = -3487809352556097894L;

    @Override // com.telpo.tps550.api.iccard.ICCardException, com.telpo.tps550.api.TelpoException
    public String getDescription() {
        return "Cannot find a valid IC card!";
    }

    public RemovedCardException() {
    }

    public RemovedCardException(String str, Throwable th) {
        super(str, th);
    }

    public RemovedCardException(String str) {
        super(str);
    }

    public RemovedCardException(Throwable th) {
        super(th);
    }
}
