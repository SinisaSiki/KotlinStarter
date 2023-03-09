package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.StreamReadCapability;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ContainerNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.LogicalType;
import com.fasterxml.jackson.databind.util.RawValue;
import java.io.IOException;
import java.util.Arrays;

/* compiled from: JsonNodeDeserializer.java */
/* loaded from: classes.dex */
abstract class BaseNodeDeserializer<T extends JsonNode> extends StdDeserializer<T> {
    protected final Boolean _supportsUpdates;

    @Override // com.fasterxml.jackson.databind.JsonDeserializer
    public boolean isCachable() {
        return true;
    }

    public BaseNodeDeserializer(Class<T> cls, Boolean bool) {
        super((Class<?>) cls);
        this._supportsUpdates = bool;
    }

    @Override // com.fasterxml.jackson.databind.deser.std.StdDeserializer, com.fasterxml.jackson.databind.JsonDeserializer
    public Object deserializeWithType(JsonParser jsonParser, DeserializationContext deserializationContext, TypeDeserializer typeDeserializer) throws IOException {
        return typeDeserializer.deserializeTypedFromAny(jsonParser, deserializationContext);
    }

    @Override // com.fasterxml.jackson.databind.JsonDeserializer
    public LogicalType logicalType() {
        return LogicalType.Untyped;
    }

    @Override // com.fasterxml.jackson.databind.JsonDeserializer
    public Boolean supportsUpdate(DeserializationConfig deserializationConfig) {
        return this._supportsUpdates;
    }

    protected void _handleDuplicateField(JsonParser jsonParser, DeserializationContext deserializationContext, JsonNodeFactory jsonNodeFactory, String str, ObjectNode objectNode, JsonNode jsonNode, JsonNode jsonNode2) throws IOException {
        if (deserializationContext.isEnabled(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY)) {
            deserializationContext.reportInputMismatch(JsonNode.class, "Duplicate field '%s' for `ObjectNode`: not allowed when `DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY` enabled", str);
        }
        if (deserializationContext.isEnabled(StreamReadCapability.DUPLICATE_PROPERTIES)) {
            if (jsonNode.isArray()) {
                ((ArrayNode) jsonNode).add(jsonNode2);
                objectNode.replace(str, jsonNode);
                return;
            }
            ArrayNode arrayNode = jsonNodeFactory.arrayNode();
            arrayNode.add(jsonNode);
            arrayNode.add(jsonNode2);
            objectNode.replace(str, arrayNode);
        }
    }

    public final ObjectNode _deserializeObjectAtName(JsonParser jsonParser, DeserializationContext deserializationContext, JsonNodeFactory jsonNodeFactory, ContainerStack containerStack) throws IOException {
        JsonNode jsonNode;
        ObjectNode objectNode = jsonNodeFactory.objectNode();
        String currentName = jsonParser.currentName();
        while (currentName != null) {
            JsonToken nextToken = jsonParser.nextToken();
            if (nextToken == null) {
                nextToken = JsonToken.NOT_AVAILABLE;
            }
            int id = nextToken.id();
            if (id == 1) {
                jsonNode = _deserializeContainerNoRecursion(jsonParser, deserializationContext, jsonNodeFactory, containerStack, jsonNodeFactory.objectNode());
            } else if (id == 3) {
                jsonNode = _deserializeContainerNoRecursion(jsonParser, deserializationContext, jsonNodeFactory, containerStack, jsonNodeFactory.arrayNode());
            } else {
                jsonNode = _deserializeAnyScalar(jsonParser, deserializationContext);
            }
            JsonNode jsonNode2 = jsonNode;
            JsonNode replace = objectNode.replace(currentName, jsonNode2);
            if (replace != null) {
                _handleDuplicateField(jsonParser, deserializationContext, jsonNodeFactory, currentName, objectNode, replace, jsonNode2);
            }
            currentName = jsonParser.nextFieldName();
        }
        return objectNode;
    }

    public final JsonNode updateObject(JsonParser jsonParser, DeserializationContext deserializationContext, ObjectNode objectNode, ContainerStack containerStack) throws IOException {
        String str;
        JsonNode jsonNode;
        if (jsonParser.isExpectedStartObjectToken()) {
            str = jsonParser.nextFieldName();
        } else if (!jsonParser.hasToken(JsonToken.FIELD_NAME)) {
            return (JsonNode) deserialize(jsonParser, deserializationContext);
        } else {
            str = jsonParser.currentName();
        }
        JsonNodeFactory nodeFactory = deserializationContext.getNodeFactory();
        while (str != null) {
            JsonToken nextToken = jsonParser.nextToken();
            JsonNode jsonNode2 = objectNode.get(str);
            if (jsonNode2 != null) {
                if (jsonNode2 instanceof ObjectNode) {
                    if (nextToken == JsonToken.START_OBJECT) {
                        JsonNode updateObject = updateObject(jsonParser, deserializationContext, (ObjectNode) jsonNode2, containerStack);
                        if (updateObject != jsonNode2) {
                            objectNode.set(str, updateObject);
                        }
                        str = jsonParser.nextFieldName();
                    }
                } else if ((jsonNode2 instanceof ArrayNode) && nextToken == JsonToken.START_ARRAY) {
                    _deserializeContainerNoRecursion(jsonParser, deserializationContext, nodeFactory, containerStack, (ArrayNode) jsonNode2);
                    str = jsonParser.nextFieldName();
                }
            }
            if (nextToken == null) {
                nextToken = JsonToken.NOT_AVAILABLE;
            }
            int id = nextToken.id();
            if (id == 1) {
                jsonNode = _deserializeContainerNoRecursion(jsonParser, deserializationContext, nodeFactory, containerStack, nodeFactory.objectNode());
            } else if (id == 3) {
                jsonNode = _deserializeContainerNoRecursion(jsonParser, deserializationContext, nodeFactory, containerStack, nodeFactory.arrayNode());
            } else if (id == 6) {
                jsonNode = nodeFactory.textNode(jsonParser.getText());
            } else if (id == 7) {
                jsonNode = _fromInt(jsonParser, deserializationContext, nodeFactory);
            } else {
                switch (id) {
                    case 9:
                        jsonNode = nodeFactory.booleanNode(true);
                        break;
                    case 10:
                        jsonNode = nodeFactory.booleanNode(false);
                        break;
                    case 11:
                        jsonNode = nodeFactory.nullNode();
                        break;
                    default:
                        jsonNode = _deserializeRareScalar(jsonParser, deserializationContext);
                        break;
                }
            }
            objectNode.set(str, jsonNode);
            str = jsonParser.nextFieldName();
        }
        return objectNode;
    }

    public final ContainerNode<?> _deserializeContainerNoRecursion(JsonParser jsonParser, DeserializationContext deserializationContext, JsonNodeFactory jsonNodeFactory, ContainerStack containerStack, ContainerNode<?> containerNode) throws IOException {
        ObjectNode objectNode;
        JsonNode textNode;
        ObjectNode objectNode2;
        int deserializationFeatures = deserializationContext.getDeserializationFeatures() & F_MASK_INT_COERCIONS;
        ContainerNode<?> containerNode2 = containerNode;
        do {
            boolean z = true;
            if (containerNode2 instanceof ObjectNode) {
                ContainerNode<?> containerNode3 = containerNode2;
                ObjectNode objectNode3 = (ObjectNode) containerNode2;
                String nextFieldName = jsonParser.nextFieldName();
                while (nextFieldName != null) {
                    JsonToken nextToken = jsonParser.nextToken();
                    if (nextToken == null) {
                        nextToken = JsonToken.NOT_AVAILABLE;
                    }
                    int id = nextToken.id();
                    if (id == z) {
                        ObjectNode objectNode4 = objectNode3;
                        ObjectNode objectNode5 = jsonNodeFactory.objectNode();
                        JsonNode replace = objectNode4.replace(nextFieldName, objectNode5);
                        if (replace != null) {
                            objectNode = objectNode5;
                            _handleDuplicateField(jsonParser, deserializationContext, jsonNodeFactory, nextFieldName, objectNode4, replace, objectNode5);
                        } else {
                            objectNode = objectNode5;
                        }
                        containerStack.push(containerNode3);
                        objectNode3 = objectNode;
                        containerNode3 = objectNode3;
                    } else if (id == 3) {
                        ObjectNode objectNode6 = objectNode3;
                        ArrayNode arrayNode = jsonNodeFactory.arrayNode();
                        JsonNode replace2 = objectNode6.replace(nextFieldName, arrayNode);
                        if (replace2 != null) {
                            _handleDuplicateField(jsonParser, deserializationContext, jsonNodeFactory, nextFieldName, objectNode6, replace2, arrayNode);
                        }
                        containerStack.push(containerNode3);
                        containerNode2 = arrayNode;
                        continue;
                    } else {
                        switch (id) {
                            case 6:
                                textNode = jsonNodeFactory.textNode(jsonParser.getText());
                                break;
                            case 7:
                                textNode = _fromInt(jsonParser, deserializationFeatures, jsonNodeFactory);
                                break;
                            case 8:
                                textNode = _fromFloat(jsonParser, deserializationContext, jsonNodeFactory);
                                break;
                            case 9:
                                textNode = jsonNodeFactory.booleanNode(z);
                                break;
                            case 10:
                                textNode = jsonNodeFactory.booleanNode(false);
                                break;
                            case 11:
                                textNode = jsonNodeFactory.nullNode();
                                break;
                            default:
                                textNode = _deserializeRareScalar(jsonParser, deserializationContext);
                                break;
                        }
                        JsonNode jsonNode = textNode;
                        JsonNode replace3 = objectNode3.replace(nextFieldName, jsonNode);
                        if (replace3 != null) {
                            objectNode2 = objectNode3;
                            _handleDuplicateField(jsonParser, deserializationContext, jsonNodeFactory, nextFieldName, objectNode3, replace3, jsonNode);
                        } else {
                            objectNode2 = objectNode3;
                        }
                        objectNode3 = objectNode2;
                    }
                    nextFieldName = jsonParser.nextFieldName();
                    z = true;
                }
                containerNode2 = containerStack.popOrNull();
                continue;
            } else {
                ArrayNode arrayNode2 = (ArrayNode) containerNode2;
                while (true) {
                    JsonToken nextToken2 = jsonParser.nextToken();
                    if (nextToken2 == null) {
                        nextToken2 = JsonToken.NOT_AVAILABLE;
                    }
                    switch (nextToken2.id()) {
                        case 1:
                            containerStack.push(containerNode2);
                            containerNode2 = jsonNodeFactory.objectNode();
                            arrayNode2.add(containerNode2);
                            continue;
                        case 2:
                        case 5:
                        default:
                            arrayNode2.add(_deserializeRareScalar(jsonParser, deserializationContext));
                        case 3:
                            containerStack.push(containerNode2);
                            containerNode2 = jsonNodeFactory.arrayNode();
                            arrayNode2.add(containerNode2);
                            continue;
                        case 4:
                            break;
                        case 6:
                            arrayNode2.add(jsonNodeFactory.textNode(jsonParser.getText()));
                        case 7:
                            arrayNode2.add(_fromInt(jsonParser, deserializationFeatures, jsonNodeFactory));
                        case 8:
                            arrayNode2.add(_fromFloat(jsonParser, deserializationContext, jsonNodeFactory));
                        case 9:
                            arrayNode2.add(jsonNodeFactory.booleanNode(true));
                        case 10:
                            arrayNode2.add(jsonNodeFactory.booleanNode(false));
                        case 11:
                            arrayNode2.add(jsonNodeFactory.nullNode());
                    }
                }
            }
        } while (containerNode2 != null);
        return containerNode;
    }

    public final JsonNode _deserializeAnyScalar(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNodeFactory nodeFactory = deserializationContext.getNodeFactory();
        int currentTokenId = jsonParser.currentTokenId();
        if (currentTokenId == 2) {
            return nodeFactory.objectNode();
        }
        switch (currentTokenId) {
            case 6:
                return nodeFactory.textNode(jsonParser.getText());
            case 7:
                return _fromInt(jsonParser, deserializationContext, nodeFactory);
            case 8:
                return _fromFloat(jsonParser, deserializationContext, nodeFactory);
            case 9:
                return nodeFactory.booleanNode(true);
            case 10:
                return nodeFactory.booleanNode(false);
            case 11:
                return nodeFactory.nullNode();
            case 12:
                return _fromEmbedded(jsonParser, deserializationContext);
            default:
                return (JsonNode) deserializationContext.handleUnexpectedToken(handledType(), jsonParser);
        }
    }

    protected final JsonNode _deserializeRareScalar(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        int currentTokenId = jsonParser.currentTokenId();
        if (currentTokenId != 2) {
            if (currentTokenId == 8) {
                return _fromFloat(jsonParser, deserializationContext, deserializationContext.getNodeFactory());
            }
            if (currentTokenId == 12) {
                return _fromEmbedded(jsonParser, deserializationContext);
            }
            return (JsonNode) deserializationContext.handleUnexpectedToken(handledType(), jsonParser);
        }
        return deserializationContext.getNodeFactory().objectNode();
    }

    protected final JsonNode _fromInt(JsonParser jsonParser, int i, JsonNodeFactory jsonNodeFactory) throws IOException {
        if (i != 0) {
            if (DeserializationFeature.USE_BIG_INTEGER_FOR_INTS.enabledIn(i)) {
                return jsonNodeFactory.numberNode(jsonParser.getBigIntegerValue());
            }
            return jsonNodeFactory.numberNode(jsonParser.getLongValue());
        }
        JsonParser.NumberType numberType = jsonParser.getNumberType();
        if (numberType == JsonParser.NumberType.INT) {
            return jsonNodeFactory.numberNode(jsonParser.getIntValue());
        }
        if (numberType == JsonParser.NumberType.LONG) {
            return jsonNodeFactory.numberNode(jsonParser.getLongValue());
        }
        return jsonNodeFactory.numberNode(jsonParser.getBigIntegerValue());
    }

    protected final JsonNode _fromInt(JsonParser jsonParser, DeserializationContext deserializationContext, JsonNodeFactory jsonNodeFactory) throws IOException {
        JsonParser.NumberType numberType;
        int deserializationFeatures = deserializationContext.getDeserializationFeatures();
        if ((F_MASK_INT_COERCIONS & deserializationFeatures) != 0) {
            if (DeserializationFeature.USE_BIG_INTEGER_FOR_INTS.enabledIn(deserializationFeatures)) {
                numberType = JsonParser.NumberType.BIG_INTEGER;
            } else if (DeserializationFeature.USE_LONG_FOR_INTS.enabledIn(deserializationFeatures)) {
                numberType = JsonParser.NumberType.LONG;
            } else {
                numberType = jsonParser.getNumberType();
            }
        } else {
            numberType = jsonParser.getNumberType();
        }
        if (numberType == JsonParser.NumberType.INT) {
            return jsonNodeFactory.numberNode(jsonParser.getIntValue());
        }
        if (numberType == JsonParser.NumberType.LONG) {
            return jsonNodeFactory.numberNode(jsonParser.getLongValue());
        }
        return jsonNodeFactory.numberNode(jsonParser.getBigIntegerValue());
    }

    protected final JsonNode _fromFloat(JsonParser jsonParser, DeserializationContext deserializationContext, JsonNodeFactory jsonNodeFactory) throws IOException {
        JsonParser.NumberType numberType = jsonParser.getNumberType();
        if (numberType == JsonParser.NumberType.BIG_DECIMAL) {
            return jsonNodeFactory.numberNode(jsonParser.getDecimalValue());
        }
        if (deserializationContext.isEnabled(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)) {
            if (jsonParser.isNaN()) {
                return jsonNodeFactory.numberNode(jsonParser.getDoubleValue());
            }
            return jsonNodeFactory.numberNode(jsonParser.getDecimalValue());
        } else if (numberType == JsonParser.NumberType.FLOAT) {
            return jsonNodeFactory.numberNode(jsonParser.getFloatValue());
        } else {
            return jsonNodeFactory.numberNode(jsonParser.getDoubleValue());
        }
    }

    protected final JsonNode _fromEmbedded(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNodeFactory nodeFactory = deserializationContext.getNodeFactory();
        Object embeddedObject = jsonParser.getEmbeddedObject();
        if (embeddedObject == null) {
            return nodeFactory.nullNode();
        }
        if (embeddedObject.getClass() == byte[].class) {
            return nodeFactory.binaryNode((byte[]) embeddedObject);
        }
        if (embeddedObject instanceof RawValue) {
            return nodeFactory.rawValueNode((RawValue) embeddedObject);
        }
        if (embeddedObject instanceof JsonNode) {
            return (JsonNode) embeddedObject;
        }
        return nodeFactory.pojoNode(embeddedObject);
    }

    /* compiled from: JsonNodeDeserializer.java */
    /* loaded from: classes.dex */
    public static final class ContainerStack {
        private int _end;
        private ContainerNode[] _stack;
        private int _top;

        public int size() {
            return this._top;
        }

        public void push(ContainerNode containerNode) {
            int i = this._top;
            int i2 = this._end;
            if (i < i2) {
                ContainerNode[] containerNodeArr = this._stack;
                this._top = i + 1;
                containerNodeArr[i] = containerNode;
                return;
            }
            if (this._stack == null) {
                this._end = 10;
                this._stack = new ContainerNode[10];
            } else {
                int min = i2 + Math.min(4000, Math.max(20, i2 >> 1));
                this._end = min;
                this._stack = (ContainerNode[]) Arrays.copyOf(this._stack, min);
            }
            ContainerNode[] containerNodeArr2 = this._stack;
            int i3 = this._top;
            this._top = i3 + 1;
            containerNodeArr2[i3] = containerNode;
        }

        public ContainerNode popOrNull() {
            int i = this._top;
            if (i == 0) {
                return null;
            }
            ContainerNode[] containerNodeArr = this._stack;
            int i2 = i - 1;
            this._top = i2;
            return containerNodeArr[i2];
        }
    }
}
