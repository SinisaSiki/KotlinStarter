package com.telpo.tps550.api.hdmi;

import android.content.Context;
import java.lang.reflect.InvocationTargetException;

/* loaded from: classes.dex */
public class HdmiCtrl {
    public static int switchDisplay(Context context) {
        int i = 0;
        try {
            Class<?> cls = Class.forName("android.util.Vendor");
            cls.getMethod("SwitchDisplay", Context.class).invoke(cls, context);
        } catch (Exception e) {
            i = -1;
            if (e instanceof InvocationTargetException) {
                ((InvocationTargetException) e).getTargetException().printStackTrace();
            } else {
                e.printStackTrace();
            }
        }
        return i;
    }
}
