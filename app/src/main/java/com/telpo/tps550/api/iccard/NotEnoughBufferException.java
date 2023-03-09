package com.telpo.tps550.api.iccard;

/* loaded from: classes.dex */
public class NotEnoughBufferException extends ICCardException {
    private static final long serialVersionUID = -2405024364586250998L;

    @Override // com.telpo.tps550.api.iccard.ICCardException, com.telpo.tps550.api.TelpoException
    public String getDescription() {
        return "The given buffer is not enough for data to receive!";
    }

    public NotEnoughBufferException() {
    }

    public NotEnoughBufferException(String str, Throwable th) {
        super(str, th);
    }

    public NotEnoughBufferException(String str) {
        super(str);
    }

    public NotEnoughBufferException(Throwable th) {
        super(th);
    }
}
