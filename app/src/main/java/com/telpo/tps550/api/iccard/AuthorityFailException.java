package com.telpo.tps550.api.iccard;

/* loaded from: classes.dex */
public class AuthorityFailException extends ICCardException {
    private static final long serialVersionUID = 7101656189764787793L;

    @Override // com.telpo.tps550.api.iccard.ICCardException, com.telpo.tps550.api.TelpoException
    public String getDescription() {
        return "Failed to authority the special sector!";
    }

    public AuthorityFailException() {
    }

    public AuthorityFailException(String str, Throwable th) {
        super(str, th);
    }

    public AuthorityFailException(String str) {
        super(str);
    }

    public AuthorityFailException(Throwable th) {
        super(th);
    }
}
