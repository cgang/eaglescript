package org.eaglescript;

import java.io.Serializable;

public class ScriptNull implements ScriptObject, Serializable {
    public static final ScriptNull NULL = new ScriptNull();

    private ScriptNull() {
    }

    @Override
    public Object toJavaObject(Class<?> type) {
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ScriptNull;
    }

    @Override
    public String toString() {
        return "null";
    }
}
