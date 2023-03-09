package com.telpo.tps550.api.printer;

import com.telpo.tps550.api.TelpoException;

/* loaded from: classes.dex */
public class PaperCutException extends TelpoException {
    private static final long serialVersionUID = 9014308459626928976L;

    @Override // com.telpo.tps550.api.TelpoException
    public String getDescription() {
        return "A error has happened when cutting paper";
    }

    public PaperCutException() {
    }

    public PaperCutException(String str, Throwable th) {
        super(str, th);
    }

    public PaperCutException(String str) {
        super(str);
    }

    public PaperCutException(Throwable th) {
        super(th);
    }
}
