package org.eaglescript.compiler;

import org.eaglescript.ScriptNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConstantTable {
    public static final short UNDEFINED = 0;
    public static final short ZERO = 1;
    public static final short NULL = 2;

    public static final short FALSE = 3;
    public static final short TRUE = 4;

    private List<Object> constants = new ArrayList<>();
    private Map<Object, Short> offsetMap = new HashMap<>();

    ConstantTable() {
        constants.add(null);
        constants.add(0.0D);
        constants.add(ScriptNull.NULL);
        constants.add(Boolean.FALSE);
        constants.add(Boolean.TRUE);
    }

    private short putValue(Object object) {
        Short existing = offsetMap.get(object);
        if (existing != null) {
            return existing.shortValue();
        }

        short offset = (short) constants.size();
        constants.add(object);
        offsetMap.put(object, Short.valueOf(offset));
        return offset;
    }

    short put(String text) {
        return putValue(text);
    }

    short put(double value) {
        return putValue(value);
    }

    short put(boolean value) {
        return putValue(value);
    }

    Object[] toArray() {
        return constants.toArray();
    }
}
