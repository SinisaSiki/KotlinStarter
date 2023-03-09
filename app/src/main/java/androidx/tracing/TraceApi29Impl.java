package androidx.tracing;

import android.os.Trace;

/* loaded from: classes.dex */
final class TraceApi29Impl {
    private TraceApi29Impl() {
    }

    public static void beginAsyncSection(String str, int i) {
        Trace.beginAsyncSection(str, i);
    }

    public static void endAsyncSection(String str, int i) {
        Trace.endAsyncSection(str, i);
    }

    public static void setCounter(String str, int i) {
        Trace.setCounter(str, i);
    }
}
