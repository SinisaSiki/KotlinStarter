package com.telpo.tps550.api.typea;

import android.content.Context;
import java.io.Serializable;

/* loaded from: classes.dex */
public class TAInfo implements Serializable {
    private static Context mContext;
    private String num;

    public static synchronized void open(Context context) {
        synchronized (TAInfo.class) {
            mContext = context;
        }
    }

    public String getNum() {
        return this.num;
    }

    public void setNum(String str) {
        this.num = str;
    }
}
