package org.eaglescript;

import org.eaglescript.vm.ScriptAwareException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A {@link OrdinaryObject} represents an ordinary object.
 * @see <a href="https://262.ecma-international.org/6.0/#sec-ordinary-object">Ordinary Object</a>
 */
public class OrdinaryObject extends ScriptObject {
    private static final long serialVersionUID = -5181082318222397790L;

    public static class Property {
        private final String key;
        private Object value;

        public Property(String key) {
            this.key = key;
        }

        public Property(String key, Object value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }
    }

    private Map<String, Object> map = new LinkedHashMap<>();

    @Override
    public Object get(Object key) {
        if (ScriptString.isString(key)) {
            return map.get(key.toString());
        } else {
            return null;
        }
    }

    @Override
    public void set(Object key, Object value) {
        if (ScriptString.isString(key)) {
            map.put(key.toString(), value);
        }
    }

    public List<Property> getProperties() {
        List<Property> props = new ArrayList<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            props.add(new Property(entry.getKey(), entry.getValue()));
        }
        return props;
    }

    @Override
    public Object toJavaObject(Class<?> type) throws ScriptAwareException {
        Map<String, Object> result = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            result.put(entry.getKey(), ScriptObject.toJava(entry.getValue(), Object.class));
        }
        return result;
    }
}
