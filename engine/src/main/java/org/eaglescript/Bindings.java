package org.eaglescript;

import java.util.LinkedHashMap;
import java.util.Map;

public class Bindings {
    private Map<String, Object> map;

    public Bindings(Map<String, Object> map) {
        this.map = new LinkedHashMap<>(map);
    }
}
