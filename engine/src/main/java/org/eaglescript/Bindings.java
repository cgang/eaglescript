package org.eaglescript;

import org.eaglescript.util.Console;
import org.eaglescript.util.JSONSupport;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Bindings {
    public static Bindings newDefault() {
        Bindings bindings = new Bindings();
        bindings.put("console", new Console());
        bindings.put("JSON", new JSONSupport());
        return bindings;
    }

    private Map<String, Object> map;

    Bindings() {
        this(Collections.emptyMap());
    }

    public Bindings(Map<String, Object> map) {
        this.map = new LinkedHashMap<>(map);
    }

    public void put(String key, Object value) {
        this.map.put(key, value);
    }

    public Map<String, ScriptObject> toScriptObjects() {
        Map<String, ScriptObject> result = new HashMap<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            result.put(entry.getKey(), JavaAdapter.adapt(entry.getValue()));
        }
        return result;
    }
}
