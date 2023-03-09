package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.databind.introspect.AnnotatedAndMetadata;
import java.util.function.Predicate;

/* compiled from: D8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class StdKeyDeserializers$$ExternalSyntheticLambda0 implements Predicate {
    public static final /* synthetic */ StdKeyDeserializers$$ExternalSyntheticLambda0 INSTANCE = new StdKeyDeserializers$$ExternalSyntheticLambda0();

    private /* synthetic */ StdKeyDeserializers$$ExternalSyntheticLambda0() {
    }

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        return StdKeyDeserializers.lambda$findStringBasedKeyDeserializer$0((AnnotatedAndMetadata) obj);
    }
}
