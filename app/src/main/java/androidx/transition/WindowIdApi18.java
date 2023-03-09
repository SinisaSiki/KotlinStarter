package androidx.transition;

import android.view.View;
import android.view.WindowId;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class WindowIdApi18 implements WindowIdImpl {
    private final WindowId mWindowId;

    public WindowIdApi18(View view) {
        this.mWindowId = view.getWindowId();
    }

    public boolean equals(Object obj) {
        return (obj instanceof WindowIdApi18) && ((WindowIdApi18) obj).mWindowId.equals(this.mWindowId);
    }

    public int hashCode() {
        return this.mWindowId.hashCode();
    }
}
