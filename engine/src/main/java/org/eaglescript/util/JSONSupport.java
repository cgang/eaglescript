package org.eaglescript.util;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.eaglescript.ArrayObject;
import org.eaglescript.OrdinaryObject;
import org.eaglescript.ScriptNull;
import org.eaglescript.ScriptNumber;
import org.eaglescript.vm.ScriptAwareException;

import java.io.IOException;

/**
 * The {@link JSONSupport} provides basic JSON related support for script.
 * It's intended to be exposed as 'JSON' to script.
 */
public class JSONSupport {
    static class OrdinaryObjectSerializer extends StdSerializer<OrdinaryObject> {
        OrdinaryObjectSerializer() {
            super(OrdinaryObject.class);
        }

        @Override
        public void serialize(OrdinaryObject object, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeStartObject();
            for (OrdinaryObject.Property property : object.getProperties()) {
                Object value = property.getValue();
                if (value == null) {
                    // TODO support undefined value
                    continue;
                }

                gen.writeFieldName(property.getKey());
                if (value instanceof Double) {
                    gen.writeRawValue(ScriptNumber.toString((Double) value));
                } else {
                    gen.writeObject(value);
                }
            }
            gen.writeEndObject();
        }
    }

    static class OrdinaryObjectDeserializer extends StdDeserializer<OrdinaryObject> {
        OrdinaryObjectDeserializer() {
            super(OrdinaryObject.class);
        }

        @Override
        public OrdinaryObject deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            OrdinaryObject object = new OrdinaryObject();
            JsonToken token = p.nextToken();
            String key = null;
            while (token != JsonToken.END_OBJECT) {
                switch (token) {
                    case FIELD_NAME:
                        key = p.currentName();
                        break;
                    case VALUE_STRING:
                        object.set(key, p.getText());
                        break;
                    case VALUE_TRUE:
                        object.set(key, Boolean.TRUE);
                        break;
                    case VALUE_FALSE:
                        object.set(key, Boolean.FALSE);
                        break;
                    case VALUE_NUMBER_INT:
                        object.set(key, (double) p.getIntValue());
                        break;
                    case VALUE_NUMBER_FLOAT:
                        object.set(key, p.getDoubleValue());
                        break;
                    case VALUE_NULL:
                        object.set(key, ScriptNull.NULL);
                        break;
                    case START_ARRAY:
                        ArrayObject array = ctxt.readValue(p, ArrayObject.class);
                        object.set(key, array);
                        break;
                    default:
                        throw new JsonParseException(p, "Unknown token: " + token);
                }
                token = p.nextToken();
            }

            return object;
        }
    }
    static class ArrayObjectSerializer extends StdSerializer<ArrayObject> {
        ArrayObjectSerializer() {
            super(ArrayObject.class);
        }

        @Override
        public void serialize(ArrayObject array, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeStartArray();
            for (int i = 0; i< array.size(); i++) {
                // TODO handle undefined
                Object value = array.get(i);
                if (value instanceof Double) {
                    gen.writeRawValue(ScriptNumber.toString((Double) value));
                } else {
                    gen.writeObject(value);
                }
            }
            gen.writeEndArray();
        }
    }

    static class ArrayObjectDeserializer extends StdDeserializer<ArrayObject> {

        ArrayObjectDeserializer() {
            super(ArrayObject.class);
        }

        @Override
        public ArrayObject deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            ArrayObject array = new ArrayObject();
            JsonToken token = p.nextToken();
            while (token != JsonToken.END_ARRAY) {
                switch (token) {
                    case VALUE_STRING:
                        array.add(p.getText());
                        break;
                    case VALUE_TRUE:
                        array.add(Boolean.TRUE);
                        break;
                    case VALUE_FALSE:
                        array.add(Boolean.FALSE);
                        break;
                    case VALUE_NUMBER_INT:
                        array.add((double) p.getIntValue());
                        break;
                    case VALUE_NUMBER_FLOAT:
                        array.add(p.getDoubleValue());
                        break;
                    case VALUE_NULL:
                        array.add(ScriptNull.NULL);
                        break;
                    case START_OBJECT:
                        OrdinaryObject object = ctxt.readValue(p, OrdinaryObject.class);
                        array.add(object);
                        break;
                    default:
                        throw new JsonParseException(p, "Unknown token: " + token);
                }
                token = p.nextToken();
            }
            return array;
        }
    }

    private final ObjectMapper mapper;
    private ObjectReader ordinaryReader;
    private ObjectReader arrayReader;

    public JSONSupport() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(ArrayObject.class, new ArrayObjectSerializer());
        module.addSerializer(OrdinaryObject.class, new OrdinaryObjectSerializer());
        module.addDeserializer(ArrayObject.class, new ArrayObjectDeserializer());
        module.addDeserializer(OrdinaryObject.class, new OrdinaryObjectDeserializer());

        mapper = new ObjectMapper();
        mapper.registerModule(module);

        ordinaryReader = mapper.readerFor(OrdinaryObject.class);
        arrayReader = mapper.readerFor(ArrayObject.class);
    }

    public String stringify(Object object) throws ScriptAwareException {
        try {
            return mapper.writeValueAsString(object);
        } catch (IOException e) {
            throw new ScriptAwareException(e);
        }
    }

    public Object parse(String json) throws ScriptAwareException {
        try {
            JsonNode tree = mapper.reader().readTree(json);
            if (tree.isTextual()) {
                return tree.asText();
            } else if (tree.isBoolean()) {
                return tree.asBoolean();
            } else if (tree.isNumber()) {
                return tree.asDouble();
            } else if (tree.isNull()) {
                return ScriptNull.NULL;
            } else if (tree.isArray()) {
                return arrayReader.readValue(tree);
            } else if (tree.isObject()) {
                return ordinaryReader.readValue(tree);
            } else {
                throw new ScriptAwareException("Unsupported JSON: " + json);
            }
        } catch (IOException e) {
            throw new ScriptAwareException(e);
        }
    }
}
