package com.telpo.tps550.api.iccard;

/* loaded from: classes.dex */
public class NoAuthorityCardException extends ICCardException {
    private static final long serialVersionUID = 7101656189764787793L;

    @Override // com.telpo.tps550.api.iccard.ICCardException, com.telpo.tps550.api.TelpoException
    public String getDescription() {
        return "Current sector has not been authorized!";
    }

    public NoAuthorityCardException() {
    }

    public NoAuthorityCardException(String str, Throwable th) {
        super(str, th);
    }

    public NoAuthorityCardException(String str) {
        super(str);
    }

    public NoAuthorityCardException(Throwable th) {
        super(th);
    }
}
