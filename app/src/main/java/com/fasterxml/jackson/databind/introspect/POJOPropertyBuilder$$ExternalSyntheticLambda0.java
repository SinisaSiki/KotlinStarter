package com.fasterxml.jackson.databind.introspect;

import java.util.function.Function;

/* compiled from: D8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class POJOPropertyBuilder$$ExternalSyntheticLambda0 implements Function {
    public static final /* synthetic */ POJOPropertyBuilder$$ExternalSyntheticLambda0 INSTANCE = new POJOPropertyBuilder$$ExternalSyntheticLambda0();

    private /* synthetic */ POJOPropertyBuilder$$ExternalSyntheticLambda0() {
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        return ((AnnotatedMethod) obj).getFullName();
    }
}
