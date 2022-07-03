package org.eaglescript.compiler;

import org.eaglescript.ScriptNull;

import java.util.ArrayList;
import java.util.List;

public class ConstantTable {
    public static final short UNDEFINED = 0;
    public static final short ZERO = 1;
    public static final short NULL = 2;

    private List<Object> constants = new ArrayList<>();

    ConstantTable() {
        constants.add(null);
        constants.add(0.0D);
        constants.add(ScriptNull.NULL);
    }

    short put(String text) {
        short offset = (short) constants.size();
        constants.add(text);
        return offset;
    }

    short put(double value) {
        short offset = (short) constants.size();
        constants.add(value);
        return offset;
    }
}
