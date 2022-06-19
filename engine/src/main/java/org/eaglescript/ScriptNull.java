package org.eaglescript;

public class ScriptNull extends ScriptObject {
    public static final ScriptNull NULL = new ScriptNull();

    @Override
    public String toString() {
        return "null";
    }
}
