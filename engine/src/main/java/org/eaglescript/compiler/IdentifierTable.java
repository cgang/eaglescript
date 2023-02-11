package org.eaglescript.compiler;

import java.util.ArrayList;
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

    String[] toArray() {
        ArrayList<String> identifiers = new ArrayList<>(map.size());
        for (String id : map.keySet()) {
            identifiers.add(id);
        }
        return identifiers.toArray(new String[0]);
    }
}
