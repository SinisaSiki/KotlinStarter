package com.fasterxml.jackson.databind.introspect;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
public class AnnotatedMethodCollector extends CollectorBase {
    private final boolean _collectAnnotations;
    private final ClassIntrospector.MixInResolver _mixInResolver;

    AnnotatedMethodCollector(AnnotationIntrospector annotationIntrospector, ClassIntrospector.MixInResolver mixInResolver, boolean z) {
        super(annotationIntrospector);
        this._mixInResolver = annotationIntrospector == null ? null : mixInResolver;
        this._collectAnnotations = z;
    }

    public static AnnotatedMethodMap collectMethods(AnnotationIntrospector annotationIntrospector, TypeResolutionContext typeResolutionContext, ClassIntrospector.MixInResolver mixInResolver, TypeFactory typeFactory, JavaType javaType, List<JavaType> list, Class<?> cls, boolean z) {
        return new AnnotatedMethodCollector(annotationIntrospector, mixInResolver, z).collect(typeFactory, typeResolutionContext, javaType, list, cls);
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x006c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    com.fasterxml.jackson.databind.introspect.AnnotatedMethodMap collect(com.fasterxml.jackson.databind.type.TypeFactory r5, com.fasterxml.jackson.databind.introspect.TypeResolutionContext r6, com.fasterxml.jackson.databind.JavaType r7, java.util.List<com.fasterxml.jackson.databind.JavaType> r8, java.lang.Class<?> r9) {
        /*
            r4 = this;
            java.util.LinkedHashMap r0 = new java.util.LinkedHashMap
            r0.<init>()
            java.lang.Class r1 = r7.getRawClass()
            r4._addMemberMethods(r6, r1, r0, r9)
            java.util.Iterator r8 = r8.iterator()
        L10:
            boolean r9 = r8.hasNext()
            if (r9 == 0) goto L3b
            java.lang.Object r9 = r8.next()
            com.fasterxml.jackson.databind.JavaType r9 = (com.fasterxml.jackson.databind.JavaType) r9
            com.fasterxml.jackson.databind.introspect.ClassIntrospector$MixInResolver r1 = r4._mixInResolver
            if (r1 != 0) goto L22
            r1 = 0
            goto L2a
        L22:
            java.lang.Class r2 = r9.getRawClass()
            java.lang.Class r1 = r1.findMixInClassFor(r2)
        L2a:
            com.fasterxml.jackson.databind.introspect.TypeResolutionContext$Basic r2 = new com.fasterxml.jackson.databind.introspect.TypeResolutionContext$Basic
            com.fasterxml.jackson.databind.type.TypeBindings r3 = r9.getBindings()
            r2.<init>(r5, r3)
            java.lang.Class r9 = r9.getRawClass()
            r4._addMemberMethods(r2, r9, r0, r1)
            goto L10
        L3b:
            com.fasterxml.jackson.databind.introspect.ClassIntrospector$MixInResolver r5 = r4._mixInResolver
            r8 = 0
            if (r5 == 0) goto L51
            java.lang.Class<java.lang.Object> r9 = java.lang.Object.class
            java.lang.Class r5 = r5.findMixInClassFor(r9)
            if (r5 == 0) goto L51
            java.lang.Class r7 = r7.getRawClass()
            r4._addMethodMixIns(r6, r7, r0, r5)
            r5 = 1
            goto L52
        L51:
            r5 = r8
        L52:
            if (r5 == 0) goto Lae
            com.fasterxml.jackson.databind.AnnotationIntrospector r5 = r4._intr
            if (r5 == 0) goto Lae
            boolean r5 = r0.isEmpty()
            if (r5 != 0) goto Lae
            java.util.Set r5 = r0.entrySet()
            java.util.Iterator r5 = r5.iterator()
        L66:
            boolean r6 = r5.hasNext()
            if (r6 == 0) goto Lae
            java.lang.Object r6 = r5.next()
            java.util.Map$Entry r6 = (java.util.Map.Entry) r6
            java.lang.Object r7 = r6.getKey()
            com.fasterxml.jackson.databind.introspect.MemberKey r7 = (com.fasterxml.jackson.databind.introspect.MemberKey) r7
            java.lang.String r9 = r7.getName()
            java.lang.String r1 = "hashCode"
            boolean r9 = r1.equals(r9)
            if (r9 == 0) goto L66
            int r9 = r7.argCount()
            if (r9 == 0) goto L8b
            goto L66
        L8b:
            java.lang.Class<java.lang.Object> r9 = java.lang.Object.class
            java.lang.String r7 = r7.getName()     // Catch: java.lang.Exception -> L66
            java.lang.Class[] r1 = new java.lang.Class[r8]     // Catch: java.lang.Exception -> L66
            java.lang.reflect.Method r7 = r9.getDeclaredMethod(r7, r1)     // Catch: java.lang.Exception -> L66
            if (r7 == 0) goto L66
            java.lang.Object r6 = r6.getValue()     // Catch: java.lang.Exception -> L66
            com.fasterxml.jackson.databind.introspect.AnnotatedMethodCollector$MethodBuilder r6 = (com.fasterxml.jackson.databind.introspect.AnnotatedMethodCollector.MethodBuilder) r6     // Catch: java.lang.Exception -> L66
            com.fasterxml.jackson.databind.introspect.AnnotationCollector r9 = r6.annotations     // Catch: java.lang.Exception -> L66
            java.lang.annotation.Annotation[] r1 = r7.getDeclaredAnnotations()     // Catch: java.lang.Exception -> L66
            com.fasterxml.jackson.databind.introspect.AnnotationCollector r9 = r4.collectDefaultAnnotations(r9, r1)     // Catch: java.lang.Exception -> L66
            r6.annotations = r9     // Catch: java.lang.Exception -> L66
            r6.method = r7     // Catch: java.lang.Exception -> L66
            goto L66
        Lae:
            boolean r5 = r0.isEmpty()
            if (r5 == 0) goto Lba
            com.fasterxml.jackson.databind.introspect.AnnotatedMethodMap r5 = new com.fasterxml.jackson.databind.introspect.AnnotatedMethodMap
            r5.<init>()
            return r5
        Lba:
            java.util.LinkedHashMap r5 = new java.util.LinkedHashMap
            int r6 = r0.size()
            r5.<init>(r6)
            java.util.Set r6 = r0.entrySet()
            java.util.Iterator r6 = r6.iterator()
        Lcb:
            boolean r7 = r6.hasNext()
            if (r7 == 0) goto Leb
            java.lang.Object r7 = r6.next()
            java.util.Map$Entry r7 = (java.util.Map.Entry) r7
            java.lang.Object r8 = r7.getValue()
            com.fasterxml.jackson.databind.introspect.AnnotatedMethodCollector$MethodBuilder r8 = (com.fasterxml.jackson.databind.introspect.AnnotatedMethodCollector.MethodBuilder) r8
            com.fasterxml.jackson.databind.introspect.AnnotatedMethod r8 = r8.build()
            if (r8 == 0) goto Lcb
            java.lang.Object r7 = r7.getKey()
            r5.put(r7, r8)
            goto Lcb
        Leb:
            com.fasterxml.jackson.databind.introspect.AnnotatedMethodMap r6 = new com.fasterxml.jackson.databind.introspect.AnnotatedMethodMap
            r6.<init>(r5)
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.fasterxml.jackson.databind.introspect.AnnotatedMethodCollector.collect(com.fasterxml.jackson.databind.type.TypeFactory, com.fasterxml.jackson.databind.introspect.TypeResolutionContext, com.fasterxml.jackson.databind.JavaType, java.util.List, java.lang.Class):com.fasterxml.jackson.databind.introspect.AnnotatedMethodMap");
    }

    private void _addMemberMethods(TypeResolutionContext typeResolutionContext, Class<?> cls, Map<MemberKey, MethodBuilder> map, Class<?> cls2) {
        Method[] classMethods;
        if (cls2 != null) {
            _addMethodMixIns(typeResolutionContext, cls, map, cls2);
        }
        if (cls == null) {
            return;
        }
        for (Method method : ClassUtil.getClassMethods(cls)) {
            if (_isIncludableMemberMethod(method)) {
                MemberKey memberKey = new MemberKey(method);
                MethodBuilder methodBuilder = map.get(memberKey);
                if (methodBuilder == null) {
                    map.put(memberKey, new MethodBuilder(typeResolutionContext, method, this._intr == null ? AnnotationCollector.emptyCollector() : collectAnnotations(method.getDeclaredAnnotations())));
                } else {
                    if (this._collectAnnotations) {
                        methodBuilder.annotations = collectDefaultAnnotations(methodBuilder.annotations, method.getDeclaredAnnotations());
                    }
                    Method method2 = methodBuilder.method;
                    if (method2 == null) {
                        methodBuilder.method = method;
                    } else if (Modifier.isAbstract(method2.getModifiers()) && !Modifier.isAbstract(method.getModifiers())) {
                        methodBuilder.method = method;
                        methodBuilder.typeContext = typeResolutionContext;
                    }
                }
            }
        }
    }

    protected void _addMethodMixIns(TypeResolutionContext typeResolutionContext, Class<?> cls, Map<MemberKey, MethodBuilder> map, Class<?> cls2) {
        Method[] declaredMethods;
        if (this._intr == null) {
            return;
        }
        for (Class<?> cls3 : ClassUtil.findRawSuperTypes(cls2, cls, true)) {
            for (Method method : cls3.getDeclaredMethods()) {
                if (_isIncludableMemberMethod(method)) {
                    MemberKey memberKey = new MemberKey(method);
                    MethodBuilder methodBuilder = map.get(memberKey);
                    Annotation[] declaredAnnotations = method.getDeclaredAnnotations();
                    if (methodBuilder == null) {
                        map.put(memberKey, new MethodBuilder(typeResolutionContext, null, collectAnnotations(declaredAnnotations)));
                    } else {
                        methodBuilder.annotations = collectDefaultAnnotations(methodBuilder.annotations, declaredAnnotations);
                    }
                }
            }
        }
    }

    private static boolean _isIncludableMemberMethod(Method method) {
        return !Modifier.isStatic(method.getModifiers()) && !method.isSynthetic() && !method.isBridge() && method.getParameterTypes().length <= 2;
    }

    /* loaded from: classes.dex */
    public static final class MethodBuilder {
        public AnnotationCollector annotations;
        public Method method;
        public TypeResolutionContext typeContext;

        public MethodBuilder(TypeResolutionContext typeResolutionContext, Method method, AnnotationCollector annotationCollector) {
            this.typeContext = typeResolutionContext;
            this.method = method;
            this.annotations = annotationCollector;
        }

        public AnnotatedMethod build() {
            Method method = this.method;
            if (method == null) {
                return null;
            }
            return new AnnotatedMethod(this.typeContext, method, this.annotations.asAnnotationMap(), null);
        }
    }
}
