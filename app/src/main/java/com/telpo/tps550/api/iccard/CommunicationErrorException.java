package com.telpo.tps550.api.iccard;

/* loaded from: classes.dex */
public class CommunicationErrorException extends ICCardException {
    private static final long serialVersionUID = 4679295633927227471L;

    @Override // com.telpo.tps550.api.iccard.ICCardException, com.telpo.tps550.api.TelpoException
    public String getDescription() {
        return "Communition error!";
    }

    public CommunicationErrorException() {
    }

    public CommunicationErrorException(String str, Throwable th) {
        super(str, th);
    }

    public CommunicationErrorException(String str) {
        super(str);
    }

    public CommunicationErrorException(Throwable th) {
        super(th);
    }
}
