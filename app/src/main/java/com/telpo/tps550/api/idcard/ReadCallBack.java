package com.telpo.tps550.api.idcard;

/* loaded from: classes.dex */
public interface ReadCallBack {
    void checkICSuccess(String str);

    void checkICfailed();

    void checkIDSuccess(IdentityMsg identityMsg);

    void checkIDfailed();
}
