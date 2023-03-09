package com.telpo.tps550.api.printer;

/* loaded from: classes.dex */
public interface BitmapDecodeCallback {
    void decodeComplete();

    void decoding(byte b);

    void decodingByte(byte[] bArr);
}
