package org.eaglescript.compiler;

import java.util.LinkedHashMap;
import java.util.Map;

class IdentifierTable {
    private Map<String, Integer> map = new LinkedHashMap<>();

    public int get(String identifier) {
        return map.getOrDefault(identifier, -1);
    }

    public int put(String identifier) {
        Integer index = map.get(identifier);
        if (index == null) {
            index = map.size();
            map.put(identifier, index);
        }
        return index;
    }
}
