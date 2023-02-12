package org.eaglescript;

import org.eaglescript.vm.ScriptAwareException;

import java.io.Serializable;

public class ScriptBoolean implements ScriptObject, Serializable {
    /**
     * Try to convert an object to boolean value.
     *
     * @param object an object to be processed.
     * @return boolean value of specified object.
     */
    public static boolean valueOf(Object object) {
        if (object == null || object.equals(ScriptNull.NULL)) {
            return false;
        } else if (object instanceof Boolean) {
            return ((Boolean) object).booleanValue();
        } else if (object instanceof ScriptBoolean) {
            return ((ScriptBoolean) object).value;
        } else if (object instanceof Double) {
            return ((Double) object).doubleValue() != 0;
        }

        return Boolean.valueOf(object.toString().toLowerCase());
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
