package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.Converter;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* loaded from: classes.dex */
public abstract class JavaUtilCollectionsDeserializers {
    private static final String PREFIX_JAVA_UTIL_ARRAYS = "java.util.Arrays$";
    private static final String PREFIX_JAVA_UTIL_COLLECTIONS = "java.util.Collections$";
    private static final String PREFIX_JAVA_UTIL_IMMUTABLE_COLL = "java.util.ImmutableCollections$";
    public static final int TYPE_AS_LIST = 11;
    private static final int TYPE_SINGLETON_LIST = 2;
    private static final int TYPE_SINGLETON_MAP = 3;
    private static final int TYPE_SINGLETON_SET = 1;
    private static final int TYPE_SYNC_COLLECTION = 8;
    private static final int TYPE_SYNC_LIST = 9;
    private static final int TYPE_SYNC_MAP = 10;
    private static final int TYPE_SYNC_SET = 7;
    private static final int TYPE_UNMODIFIABLE_LIST = 5;
    private static final int TYPE_UNMODIFIABLE_MAP = 6;
    private static final int TYPE_UNMODIFIABLE_SET = 4;

    /* JADX WARN: Removed duplicated region for block: B:36:0x0098  */
    /* JADX WARN: Removed duplicated region for block: B:52:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static com.fasterxml.jackson.databind.JsonDeserializer<?> findForCollection(com.fasterxml.jackson.databind.DeserializationContext r5, com.fasterxml.jackson.databind.JavaType r6) throws com.fasterxml.jackson.databind.JsonMappingException {
        /*
            java.lang.Class r5 = r6.getRawClass()
            java.lang.String r5 = r5.getName()
            java.lang.String r0 = "java.util."
            boolean r0 = r5.startsWith(r0)
            r1 = 0
            if (r0 != 0) goto L12
            return r1
        L12:
            java.lang.String r0 = _findUtilCollectionsTypeName(r5)
            r2 = 5
            java.lang.String r3 = "List"
            if (r0 == 0) goto L9e
            java.lang.String r5 = _findUnmodifiableTypeName(r0)
            java.lang.String r4 = "Set"
            if (r5 == 0) goto L3e
            boolean r0 = r5.endsWith(r4)
            if (r0 == 0) goto L31
            r5 = 4
            java.lang.Class<java.util.Set> r0 = java.util.Set.class
            com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers$JavaUtilCollectionsConverter r5 = converter(r5, r6, r0)
            goto L95
        L31:
            boolean r5 = r5.endsWith(r3)
            if (r5 == 0) goto L94
            java.lang.Class<java.util.List> r5 = java.util.List.class
            com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers$JavaUtilCollectionsConverter r5 = converter(r2, r6, r5)
            goto L95
        L3e:
            java.lang.String r5 = _findSingletonTypeName(r0)
            if (r5 == 0) goto L60
            boolean r0 = r5.endsWith(r4)
            if (r0 == 0) goto L52
            r5 = 1
            java.lang.Class<java.util.Set> r0 = java.util.Set.class
            com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers$JavaUtilCollectionsConverter r5 = converter(r5, r6, r0)
            goto L95
        L52:
            boolean r5 = r5.endsWith(r3)
            if (r5 == 0) goto L94
            r5 = 2
            java.lang.Class<java.util.List> r0 = java.util.List.class
            com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers$JavaUtilCollectionsConverter r5 = converter(r5, r6, r0)
            goto L95
        L60:
            java.lang.String r5 = _findSyncTypeName(r0)
            if (r5 == 0) goto L94
            boolean r0 = r5.endsWith(r4)
            if (r0 == 0) goto L74
            r5 = 7
            java.lang.Class<java.util.Set> r0 = java.util.Set.class
            com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers$JavaUtilCollectionsConverter r5 = converter(r5, r6, r0)
            goto L95
        L74:
            boolean r0 = r5.endsWith(r3)
            if (r0 == 0) goto L83
            r5 = 9
            java.lang.Class<java.util.List> r0 = java.util.List.class
            com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers$JavaUtilCollectionsConverter r5 = converter(r5, r6, r0)
            goto L95
        L83:
            java.lang.String r0 = "Collection"
            boolean r5 = r5.endsWith(r0)
            if (r5 == 0) goto L94
            r5 = 8
            java.lang.Class<java.util.Collection> r0 = java.util.Collection.class
            com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers$JavaUtilCollectionsConverter r5 = converter(r5, r6, r0)
            goto L95
        L94:
            r5 = r1
        L95:
            if (r5 != 0) goto L98
            goto L9d
        L98:
            com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer r1 = new com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer
            r1.<init>(r5)
        L9d:
            return r1
        L9e:
            java.lang.String r0 = _findUtilArrayTypeName(r5)
            if (r0 == 0) goto Lb7
            boolean r5 = r0.contains(r3)
            if (r5 == 0) goto Lb6
            com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer r5 = new com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer
            java.lang.Class<java.util.List> r0 = java.util.List.class
            com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers$JavaUtilCollectionsConverter r6 = converter(r2, r6, r0)
            r5.<init>(r6)
            return r5
        Lb6:
            return r1
        Lb7:
            java.lang.String r5 = _findUtilCollectionsImmutableTypeName(r5)
            if (r5 == 0) goto Ld1
            boolean r5 = r5.contains(r3)
            if (r5 == 0) goto Ld1
            com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer r5 = new com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer
            r0 = 11
            java.lang.Class<java.util.List> r1 = java.util.List.class
            com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers$JavaUtilCollectionsConverter r6 = converter(r0, r6, r1)
            r5.<init>(r6)
            return r5
        Ld1:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.fasterxml.jackson.databind.deser.impl.JavaUtilCollectionsDeserializers.findForCollection(com.fasterxml.jackson.databind.DeserializationContext, com.fasterxml.jackson.databind.JavaType):com.fasterxml.jackson.databind.JsonDeserializer");
    }

    public static JsonDeserializer<?> findForMap(DeserializationContext deserializationContext, JavaType javaType) throws JsonMappingException {
        JavaUtilCollectionsConverter javaUtilCollectionsConverter;
        String name = javaType.getRawClass().getName();
        String _findUtilCollectionsTypeName = _findUtilCollectionsTypeName(name);
        if (_findUtilCollectionsTypeName != null) {
            String _findUnmodifiableTypeName = _findUnmodifiableTypeName(_findUtilCollectionsTypeName);
            if (_findUnmodifiableTypeName != null) {
                if (_findUnmodifiableTypeName.contains("Map")) {
                    javaUtilCollectionsConverter = converter(6, javaType, Map.class);
                }
                javaUtilCollectionsConverter = null;
            } else {
                String _findSingletonTypeName = _findSingletonTypeName(_findUtilCollectionsTypeName);
                if (_findSingletonTypeName != null) {
                    if (_findSingletonTypeName.contains("Map")) {
                        javaUtilCollectionsConverter = converter(3, javaType, Map.class);
                    }
                    javaUtilCollectionsConverter = null;
                } else {
                    String _findSyncTypeName = _findSyncTypeName(_findUtilCollectionsTypeName);
                    if (_findSyncTypeName != null && _findSyncTypeName.contains("Map")) {
                        javaUtilCollectionsConverter = converter(10, javaType, Map.class);
                    }
                    javaUtilCollectionsConverter = null;
                }
            }
        } else {
            String _findUtilCollectionsImmutableTypeName = _findUtilCollectionsImmutableTypeName(name);
            if (_findUtilCollectionsImmutableTypeName != null && _findUtilCollectionsImmutableTypeName.contains("Map")) {
                javaUtilCollectionsConverter = converter(6, javaType, Map.class);
            }
            javaUtilCollectionsConverter = null;
        }
        if (javaUtilCollectionsConverter == null) {
            return null;
        }
        return new StdDelegatingDeserializer(javaUtilCollectionsConverter);
    }

    static JavaUtilCollectionsConverter converter(int i, JavaType javaType, Class<?> cls) {
        return new JavaUtilCollectionsConverter(i, javaType.findSuperType(cls));
    }

    private static String _findUtilArrayTypeName(String str) {
        if (str.startsWith(PREFIX_JAVA_UTIL_ARRAYS)) {
            return str.substring(17);
        }
        return null;
    }

    private static String _findUtilCollectionsTypeName(String str) {
        if (str.startsWith(PREFIX_JAVA_UTIL_COLLECTIONS)) {
            return str.substring(22);
        }
        return null;
    }

    private static String _findUtilCollectionsImmutableTypeName(String str) {
        if (str.startsWith(PREFIX_JAVA_UTIL_IMMUTABLE_COLL)) {
            return str.substring(31);
        }
        return null;
    }

    private static String _findSingletonTypeName(String str) {
        if (str.startsWith("Singleton")) {
            return str.substring(9);
        }
        return null;
    }

    private static String _findSyncTypeName(String str) {
        if (str.startsWith("Synchronized")) {
            return str.substring(12);
        }
        return null;
    }

    private static String _findUnmodifiableTypeName(String str) {
        if (str.startsWith("Unmodifiable")) {
            return str.substring(12);
        }
        return null;
    }

    /* loaded from: classes.dex */
    public static class JavaUtilCollectionsConverter implements Converter<Object, Object> {
        private final JavaType _inputType;
        private final int _kind;

        JavaUtilCollectionsConverter(int i, JavaType javaType) {
            this._inputType = javaType;
            this._kind = i;
        }

        @Override // com.fasterxml.jackson.databind.util.Converter
        public Object convert(Object obj) {
            if (obj == null) {
                return null;
            }
            switch (this._kind) {
                case 1:
                    Set set = (Set) obj;
                    _checkSingleton(set.size());
                    return Collections.singleton(set.iterator().next());
                case 2:
                    List list = (List) obj;
                    _checkSingleton(list.size());
                    return Collections.singletonList(list.get(0));
                case 3:
                    Map map = (Map) obj;
                    _checkSingleton(map.size());
                    Map.Entry entry = (Map.Entry) map.entrySet().iterator().next();
                    return Collections.singletonMap(entry.getKey(), entry.getValue());
                case 4:
                    return Collections.unmodifiableSet((Set) obj);
                case 5:
                    return Collections.unmodifiableList((List) obj);
                case 6:
                    return Collections.unmodifiableMap((Map) obj);
                case 7:
                    return Collections.synchronizedSet((Set) obj);
                case 8:
                    return Collections.synchronizedCollection((Collection) obj);
                case 9:
                    return Collections.synchronizedList((List) obj);
                case 10:
                    return Collections.synchronizedMap((Map) obj);
                default:
                    return obj;
            }
        }

        @Override // com.fasterxml.jackson.databind.util.Converter
        public JavaType getInputType(TypeFactory typeFactory) {
            return this._inputType;
        }

        @Override // com.fasterxml.jackson.databind.util.Converter
        public JavaType getOutputType(TypeFactory typeFactory) {
            return this._inputType;
        }

        private void _checkSingleton(int i) {
            if (i == 1) {
                return;
            }
            throw new IllegalArgumentException("Can not deserialize Singleton container from " + i + " entries");
        }
    }
}
