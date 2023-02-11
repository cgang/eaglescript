package org.eaglescript;

import org.eaglescript.util.Console;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class Bindings {
    public static Bindings newDefault() {
        Bindings bindings = new Bindings();
        bindings.put("console", new Console());
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

    public Iterable<? extends Map.Entry<String, Object>> entries() {
        return map.entrySet();
    }
}
