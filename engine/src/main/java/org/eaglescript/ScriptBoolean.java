package org.eaglescript;

import org.eaglescript.vm.ScriptAwareException;

import java.io.Serializable;

public class ScriptBoolean implements ScriptObject, Serializable {
    public static ScriptBoolean parse(String text) {
        return new ScriptBoolean(Boolean.valueOf(text));
    }
    
    private boolean value;

    public ScriptBoolean(boolean value) {
        this.value = value;
    }

    @Override
    public Object toJavaObject(Class<?> type) throws ScriptAwareException {
        return Boolean.valueOf(value);
    }
}
