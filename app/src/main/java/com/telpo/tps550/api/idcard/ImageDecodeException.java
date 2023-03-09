package com.telpo.tps550.api.idcard;

import com.telpo.tps550.api.TelpoException;

/* loaded from: classes.dex */
public class ImageDecodeException extends TelpoException {
    private static final long serialVersionUID = 7316042461528468647L;

    @Override // com.telpo.tps550.api.TelpoException
    public String getDescription() {
        return "Failed to decode the image data!";
    }

    public ImageDecodeException() {
    }

    public ImageDecodeException(String str, Throwable th) {
        super(str, th);
    }

    public ImageDecodeException(String str) {
        super(str);
    }

    public ImageDecodeException(Throwable th) {
        super(th);
    }
}
