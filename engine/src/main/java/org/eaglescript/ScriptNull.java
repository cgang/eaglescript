package org.eaglescript;

public class ScriptNull extends ScriptObject {
    public static final ScriptNull NULL = new ScriptNull();

    private ScriptNull() {
    }

    @Override
    public Object toJavaObject(Class<?> type) {
        return null;
    }

    @Override
    public String toString() {
        return "null";
    }
}
